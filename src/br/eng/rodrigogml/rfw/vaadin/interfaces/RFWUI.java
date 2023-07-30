package br.eng.rodrigogml.rfw.vaadin.interfaces;

import java.util.Locale;

import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;

import br.eng.rodrigogml.rfw.kernel.RFW;

/**
 * Description: Define uma interface da UI para adicionar algumas funcionalidades gerenciadas pelo RFW.Vaadin.<br>
 * A UI da aplicação, se implementar esta interface receberá chamadas dos métodos abaixo para melhor integração.
 *
 * @author Rodrigo GML
 * @since 10.0 (30 de out de 2020)
 */
public abstract class RFWUI extends UI {

  private static final long serialVersionUID = 6777337849690940779L;

  /**
   * Retorna o Locale da sessão atual. Caso não esteja em uma sessão lançará NullPointerException.<Br>
   * Extamente o mesmo que <Code>BISUI.getCurrent().getLocale()</code>.
   */
  public static Locale getLocaleCurrent() {
    final RFWUI ui = (RFWUI) CurrentInstance.get(UI.class);
    // Algumas vezes o getCurrent retorna null, principalmente quando usado em classes "desanexas" à janela principal. Neste caso, para evitar o nullpointer retornamos o locale de sistema até achar uma solução melhor
    if (ui == null) {
      return RFW.getLocale();
    }
    return ui.getLocale();
  }

  /**
   * Orienta a UI a realizar o LogOff do usuário fechando a tela e ocultando os dados.
   */
  public abstract void doLogout();

}
