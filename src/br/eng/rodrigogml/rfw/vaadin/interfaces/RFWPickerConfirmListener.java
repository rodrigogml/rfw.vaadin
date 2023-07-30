package br.eng.rodrigogml.rfw.vaadin.interfaces;

import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;
import br.eng.rodrigogml.rfw.kernel.vo.RFWVO;

/**
 * Interface do Listener do evento de confirma��o do Picker.
 */
public interface RFWPickerConfirmListener<VO extends RFWVO> {

  /**
   * M�todo Chamado quando o evento de escolha do Picker foi realizado.
   *
   * @param vo Objeto selecionado
   * @throws RFWException Se for lan�ada alguma exception, ela ser� tratada e impedir� que a janela do Picker seja fechada, mantendo ela aberta para a solu��o do problema. Caso em caso de erro queira que a janela se feche, trate a Exception dentro do m�todo evitando o seu lan�amento.
   */
  @SuppressWarnings("unchecked")
  public void confirm(VO... vo) throws RFWException;
}