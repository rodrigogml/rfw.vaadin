package br.eng.rodrigogml.rfw.vaadin.interfaces;

/**
 * Description: Listener utilizado na caixa de di�logo gerado pela UIFactory.<br>
 *
 * @author Rodrigo Leit�o
 * @since 10.0.0 (31 de ago de 2018)
 */
public interface RFWDialogButtonClickListener {

  /**
   * Cole��o de bot�es aceitos na caixa de di�logo de questionamento do usu�rio.
   */
  public static enum DialogButton {
    CONFIRM, CANCEL, YES, NO
  }

  public void clickedButton(DialogButton button);

}
