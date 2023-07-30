package br.eng.rodrigogml.rfw.vaadin.interfaces;

import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;
import br.eng.rodrigogml.rfw.kernel.vo.RFWVO;

/**
 * Interface do Listener do evento de confirmação do Picker.
 */
public interface RFWPickerConfirmListener<VO extends RFWVO> {

  /**
   * Método Chamado quando o evento de escolha do Picker foi realizado.
   *
   * @param vo Objeto selecionado
   * @throws RFWException Se for lançada alguma exception, ela será tratada e impedirá que a janela do Picker seja fechada, mantendo ela aberta para a solução do problema. Caso em caso de erro queira que a janela se feche, trate a Exception dentro do método evitando o seu lançamento.
   */
  @SuppressWarnings("unchecked")
  public void confirm(VO... vo) throws RFWException;
}