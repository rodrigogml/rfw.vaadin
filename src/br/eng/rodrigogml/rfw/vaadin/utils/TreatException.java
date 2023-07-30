package br.eng.rodrigogml.rfw.vaadin.utils;

import java.util.Map;

import com.vaadin.data.HasValue;

import br.eng.rodrigogml.rfw.kernel.RFW;
import br.eng.rodrigogml.rfw.kernel.bundle.RFWBundle;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWCriticalException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWValidationException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWValidationGroupException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWWarningException;
import br.eng.rodrigogml.rfw.kernel.logger.RFWLogger;
import br.eng.rodrigogml.rfw.kernel.vo.RFWVO;
import br.eng.rodrigogml.rfw.vaadin.interfaces.RFWUI;

/**
 * Description: Recebe e trata exce��es para que sejam exibidas adequadamente pela interface do programa.<br>
 *
 * @author Rodrigo Leit�o
 * @since 3.0.0 (JUL / 2009)
 */
public class TreatException {

  private TreatException() {
  }

  public static void treat(Throwable e) {
    treat(e, null);
  }

  /**
   * Faz o tratamento da Exception recebida e exibe um Dialog na Tela.<br>
   * Trata de forma diferente as exceptions do padr�o do RFWDeprec.<br>
   * Qualquer exception que n�o perten�a ao sistema RFWDeprec � tratada como uma {@link RFWCriticalException}, por considerar que a falta de tratamento de exceptions para exceptions do sistema seja um erro cr�tico de falha do desvolvedor.
   *
   * @param <VO>
   * @param e Throwable para ser tratado.
   * @param fields Campos que est�o sendo exibidos na tela. Quando a Exception � do tipo {@link RFWValidationGroupException} ou {@link RFWValidationException} e as exceptions tiverem o "field" definido, o m�todo tentar� associar as mensagems de falha diretamente nos campos para deixar mais evidente para o usu�rio. Caso contr�rio a mensagem � exibida no popup de qualquer modo.<br>
   *          Esta Hash deve ter a chave definida com o mesmo caminho que o BISValidation cria.
   */
  public static <VO extends RFWVO> void treat(Throwable e, Map<String, HasValue<?>> fields) {
    if (RFW.isDevelopmentEnvironment()) e.printStackTrace();

    // Procura se temos a mensagem de falta de sess�o em algum lugar, mesmo que encapsulada por outra exception
    Throwable last = null;
    Throwable next = e;
    while (last != next) {
      if (next == null) break;
      if (next instanceof RFWException) {
        if ("RFW_000019".equals(((RFWException) next).getExceptionCode())) {
          // Trocamos pela Exception de Warning comum para evitar logs
          e = new RFWValidationException("RFW_000019", e);
          RFWUI ui = FWVad.getRFWUI();
          if (ui != null) ui.doLogout();
          break;
        }
      }
      last = next;
      next = next.getCause();
    }

    // Verifica o tipo de excec�o recebida
    int countWarnings = 0;
    StringBuilder warnings = new StringBuilder();
    if (e instanceof RFWValidationGroupException) {
      // Se � um grupo de valida��o tentamos associar cada erro ao seu campo, se o campo n�o for encontrado gera um "log" e � exibido como um Warning na tela
      for (RFWValidationException ex : ((RFWValidationGroupException) e).getValidationlist()) {
        String msg = RFWBundle.get(ex);

        final String path = ex.getFieldPath();
        if (fields != null && path != null) {
          HasValue<?> c = fields.get(path);
          if (c != null) {
            FWVad.setComponentError(c, msg);
          }
        }

        if (countWarnings < 10) {
          warnings.append("<li>").append(msg);
        } else if (countWarnings == 10) {
          warnings.append("<li>...");
        }
        countWarnings++;
      }
    }
    showExceptionDialog(e, warnings.toString());
  }

  private static void showExceptionDialog(Throwable e, String warnings) {
    if (e instanceof RFWValidationGroupException) {
      if (warnings == null || "".equals(warnings)) {
        FWVad.showValidationMessage("Algumas informa��es s�o inv�lidas! Verifique as exclama��es de erros em cada campo com problema para corrigi-los.");
      } else {
        FWVad.showValidationMessage(warnings);
      }
    } else if (e instanceof RFWValidationException) {
      // Tenta associara mensagem ao campo, mas como s� temos uma valida��o e n�o o grupo, vamos exibir a mesma mensagem na caixa de informa��es ao inv�s de uma informa��o gen�rica
      // TreatExceptionMessages.associateValidationsToFields((RFWValidationException) e, hashMap, msg);
      FWVad.showValidationMessage(RFWBundle.get(e));
    } else if (e instanceof RFWWarningException) {
      FWVad.showWarningMessage(RFWBundle.get(e));
      RFWLogger.logException(e);
    } else if (e instanceof RFWCriticalException) {
      FWVad.showErrorMessage(RFWBundle.get(e), e);
      RFWLogger.logException(e);
    } else if (e instanceof RFWException) {
      FWVad.showErrorMessage(RFWBundle.get(e), e);
      RFWLogger.logException(e);
    } else {
      FWVad.showUncatchExceptionMessage(e);
      RFWLogger.logException(e);
    }
  }

}