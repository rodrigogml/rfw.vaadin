package br.eng.rodrigogml.rfw.vaadin.components;

import java.util.ArrayList;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSingleSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.GridLayout;

import br.eng.rodrigogml.rfw.kernel.exceptions.RFWRunTimeException;

/**
 * Description: Componente que permite que componentes sejam associados vizualmente um colado com o outro.<br>
 *
 * @author Rodrigo Leitão
 * @since 10.0.0 (27 de nov de 2018)
 */
public class RFWAssociativeComponent extends CustomField<Object> {

  private static final long serialVersionUID = 7931393985643338192L;

  private final GridLayout gl = new GridLayout();

  public RFWAssociativeComponent(String caption) {
    super();
    super.setCaption(caption);
    addStyleName("rfwAssociativeComponent"); // Estilo para deixar os campos unidos

    this.gl.setWidth("100%");
    this.gl.setSpacing(false);
  }

  /**
   * Retorna o GridLayout para manipulação direta do layout, caso necessário, como troca do tamanho dos componentes, etc.
   *
   * @return Layout utilizado na aparência do componente
   */
  public GridLayout getLayout() {
    return this.gl;
  }

  /**
   * Repassa a chamada para o {@link GridLayout} utilizado na base da aparência deste componente.
   */
  public void setColumnExpandRatio(int columnIndex, float ratio) {
    this.gl.setColumnExpandRatio(columnIndex, ratio);
  }

  /**
   * Repassa a chamada para o {@link GridLayout} utilizado na base da aparência deste componente.
   */
  public void addComponents(Component... c) {
    for (Component cp : c) {
      this.addComponent(cp);
    }
  }

  /**
   * Recupera uma lista com os components atualmente colocanos no {@link RFWAssociativeComponent}.
   *
   * @return List com os components.
   */
  public ArrayList<Component> getComponents() {
    ArrayList<Component> componentsList = new ArrayList<>(this.gl.getComponentCount());
    for (Component component : this.gl) {
      componentsList.add(component);
    }
    return componentsList;
  }

  /**
   * Repassa a chamada para o {@link GridLayout} utilizado na base da aparência deste componente.
   */
  public void addComponent(Component c) {
    this.gl.setColumns(this.gl.getColumns() + 1);
    if (!(c instanceof Button)) c.setCaption(null);
    if (c instanceof AbstractField) {
      // Atualiza o required deste componente se encontrar algum componente com o required = true
      this.setRequiredIndicatorVisible(this.isRequiredIndicatorVisible() | ((AbstractField<?>) c).isRequiredIndicatorVisible());
      ((AbstractField<?>) c).setRequiredIndicatorVisible(false);
    } else if (c instanceof AbstractSingleSelect<?>) {
      // Atualiza o required deste componente se encontrar algum componente com o required = true
      this.setRequiredIndicatorVisible(this.isRequiredIndicatorVisible() | ((AbstractSingleSelect<?>) c).isRequiredIndicatorVisible());
      ((AbstractSingleSelect<?>) c).setRequiredIndicatorVisible(false);
    }
    this.gl.addComponent(c);
  }

  @Override
  protected Component initContent() {
    return this.gl;
  }

  @Override
  public Object getValue() {
    throw new RFWRunTimeException("O RFWAssociativeComponent não trata os valores dos campos, ele tem a finalidade apenas de uni-los visualmente. Por favor chame o método diretamente do próprio componente!");
  }

  @Override
  protected void doSetValue(Object value) {
    throw new RFWRunTimeException("O RFWAssociativeComponent não trata os valores dos campos, ele tem a finalidade apenas de uni-los visualmente. Por favor chame o método diretamente do próprio componente!");
  }
}
