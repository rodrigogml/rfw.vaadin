package br.eng.rodrigogml.rfw.vaadin.components;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;

/**
 * Description: Componente que transforma o {@link Button} em um botão de estado do tipo "liga/desliga".<br>
 * O estado de selecionado/desselecionado pode ser verificado pelo método {@link #isSelected()} e/ou definido por {@link #setSelected(boolean)}.<br>
 * Além disso o componente pode ser configurado um valor em {@link #setSelectedValue(Object)} e {@link #setUnselectedValue(Object)}, que podem ser retornados através do método {@link #getValue()} conforme o botão estiver ou não selecionado.
 *
 * @author Rodrigo Leitão
 * @since 7.1.0 (28/03/2016)
 */
public class RFWButtonToggle<T extends Object> extends Button {

  private static final long serialVersionUID = -2655086810499318287L;

  public static final String STYLE_DEFAULT_PUSHED = "rfwButtonToggleDown";
  public static final String STYLE_FRIENDLY_PUSHED = "rfwButtonToggleDownFriendly";
  public static final String STYLE_DANGER_PUSHED = "rfwButtonToggleDownDanger";

  /**
   * Valor definido como valor a ser utilizado quando botão está selecionado.
   */
  private T selectedValue = null;

  /**
   * Valor definido como valor a ser utilizado quando botão NÃO está selecionado.
   */
  private T unselectedValue = null;

  /**
   * Define o Style a ser aplicado no botão quando estiver pressionado.
   */
  private String pushedStyle = STYLE_DEFAULT_PUSHED;

  /**
   * Define o Style a ser aplicado no botão quando não estiver pressionado.
   */
  private String unpushedStyle = null;

  /**
   * Indicador se o botão está selecionado (apertado ou não)
   */
  private boolean selected = false;

  public RFWButtonToggle() {
    super();
    configure();
  }

  public RFWButtonToggle(String caption) {
    super(caption);
    configure();
  }

  public RFWButtonToggle(String caption, ThemeResource icon) {
    super(caption, icon);
    configure();
  }

  public RFWButtonToggle(String caption, ThemeResource icon, ClickListener listener) {
    super(caption, icon);
    this.addClickListener(listener);
    configure();
  }

  /**
   * Método usado para configura o BISButton em um Toggle
   */
  private void configure() {
    // Coloca o listener para que a cada clique o botão troque de status
    this.addClickListener(e -> clickButton());
  }

  /**
   * Método chamado a cada clique do botão para trocar o seu valor
   */
  private void clickButton() {
    this.selected = !this.selected; // Inverte o valor atual
    updateButtonStyle(); // Atualiza a aparência do componente
  }

  /*
   * Retorna o valor definido em {@link #setSelectedValue(Object)} e {@link #setUnselectedValue(Object)} de acordo com o status de {@link #isSelected()}.
   *
   * @return O valor do objeto de acordo com o status se seleção.
   */
  /**
   * Retorna o valor definido em {@link #setSelectedValue(Object)} e {@link #setUnselectedValue(Object)} de acordo com o status de {@link #isSelected()}.
   *
   * @return O valor do objeto de acordo com o status se seleção.
   */
  public T getValue() {
    return (this.selected ? this.selectedValue : this.unselectedValue);
  }

  /**
   * Este método atualiza a aparência do botão de acordo com o valor em {@link #value2}.
   */
  private void updateButtonStyle() {
    if (this.pushedStyle != null) this.removeStyleName(this.pushedStyle);
    if (this.unpushedStyle != null) this.removeStyleName(this.unpushedStyle);
    if (this.selected) {
      if (this.pushedStyle != null) this.addStyleName(this.pushedStyle);
    } else {
      if (this.unpushedStyle != null) this.addStyleName(this.unpushedStyle);
    }
  }

  /**
   * # indicador se o botão está selecionado (apertado ou não).
   *
   * @return the indicador se o botão está selecionado (apertado ou não)
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * # valor definido como valor a ser utilizado quando botão está selecionado.
   *
   * @param selectedValue the new valor definido como valor a ser utilizado quando botão está selecionado
   */
  public void setSelectedValue(T selectedValue) {
    this.selectedValue = selectedValue;
  }

  /**
   * # valor definido como valor a ser utilizado quando botão NÃO está selecionado.
   *
   * @param unselectedValue the new valor definido como valor a ser utilizado quando botão NÃO está selecionado
   */
  public void setUnselectedValue(T unselectedValue) {
    this.unselectedValue = unselectedValue;
  }

  /**
   * # indicador se o botão está selecionado (apertado ou não).
   *
   * @param selected the new indicador se o botão está selecionado (apertado ou não)
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
    updateButtonStyle();
  }

  /**
   * # valor definido como valor a ser utilizado quando botão está selecionado.
   *
   * @return the valor definido como valor a ser utilizado quando botão está selecionado
   */
  public Object getSelectedValue() {
    return selectedValue;
  }

  /**
   * # valor definido como valor a ser utilizado quando botão NÃO está selecionado.
   *
   * @return the valor definido como valor a ser utilizado quando botão NÃO está selecionado
   */
  public Object getUnselectedValue() {
    return unselectedValue;
  }

  /**
   * # define o Style a ser aplicado no botão quando estiver pressionado.
   *
   * @return the define o Style a ser aplicado no botão quando estiver pressionado
   */
  public String getPushedStyle() {
    return pushedStyle;
  }

  /**
   * # define o Style a ser aplicado no botão quando estiver pressionado.
   *
   * @param pushedStyle the new define o Style a ser aplicado no botão quando estiver pressionado
   */
  public void setPushedStyle(String pushedStyle) {
    // Remove o estilo anterior para que não fique perdido no objeto
    if (this.pushedStyle != null) this.removeStyleName(this.pushedStyle);
    this.pushedStyle = pushedStyle;
    updateButtonStyle();
  }

  /**
   * # define o Style a ser aplicado no botão quando não estiver pressionado.
   *
   * @return the define o Style a ser aplicado no botão quando não estiver pressionado
   */
  public String getUnpushedStyle() {
    return unpushedStyle;
  }

  /**
   * # define o Style a ser aplicado no botão quando não estiver pressionado.
   *
   * @param unpushedStyle the new define o Style a ser aplicado no botão quando não estiver pressionado
   */
  public void setUnpushedStyle(String unpushedStyle) {
    // Remove o estilo anterior para que não fique perdido no objeto
    if (this.unpushedStyle != null) this.removeStyleName(this.unpushedStyle);
    this.unpushedStyle = unpushedStyle;
    updateButtonStyle();
  }

}
