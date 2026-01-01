package br.eng.rodrigogml.rfw.vaadin.interfaces;

/**
 * Description: Listener utilizado na caixa de diálogo gerado pela UIFactory.<br>
 *
 * @author Rodrigo Leitão
 * @since 10.0.0 (31 de ago de 2018)
 */
public interface RFWDialogButtonClickListener {

  /**
   * Coleção de botões aceitos na caixa de diálogo de questionamento do usuário.
   */
  public static enum DialogButton {
    CONFIRM, CANCEL, YES, NO
  }

  public void clickedButton(DialogButton button);

}
