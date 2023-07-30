package br.eng.rodrigogml.rfw.vaadin.components.window;

import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Description: Classe base para montar uma janela para o sistema que � aceita pelo SystemFrame.<br>
 *
 * @author Rodrigo Leit�o
 * @since 10.0.0 (6 de ago de 2018)
 */
public abstract class RFWWindow extends Panel {

  private static final long serialVersionUID = -8064439929838311546L;

  public RFWWindow() {
    super();

    // Configura o Painel que extendemos para fincar invis�vel e aceitar as teclas de atalho
    this.setSizeFull();
    this.addStyleName(ValoTheme.PANEL_BORDERLESS);
  }

  /**
   * M�todo chamado quando a janela perde o foco.
   */
  public void blur(BlurEvent blurEvent) {
  }

  /**
   * M�todo chamado quando a janela ganha o foco.
   */
  public void focus(FocusEvent focusEvent) {
  };

  /**
   * Deve retornar o caminho do �cone da janela.
   */
  public abstract String getWindowIcon();

  /**
   * Deve retornar o caption da Janela.
   */
  public abstract String getWindowCaption();

}
