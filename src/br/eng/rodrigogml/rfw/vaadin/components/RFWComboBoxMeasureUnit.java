package br.eng.rodrigogml.rfw.vaadin.components;

import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.shared.Registration;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;

import br.eng.rodrigogml.rfw.kernel.bundle.RFWBundle;
import br.eng.rodrigogml.rfw.kernel.measureruler.interfaces.MeasureUnit;
import br.eng.rodrigogml.rfw.kernel.measureruler.interfaces.MeasureUnit.MeasureDimension;
import br.eng.rodrigogml.rfw.kernel.utils.RUArray;
import br.eng.rodrigogml.rfw.vaadin.utils.FWVad;

/**
 * Description: Cria um componente a partir de 2 ComboBox utilizado para escolher a dimensão e a unidade de medida.<br>
 *
 * @author Rodrigo Leitão
 * @since 7.1.0 (7 de nov de 2018)
 */
public class RFWComboBoxMeasureUnit extends CustomField<MeasureUnit> {

  private static final long serialVersionUID = 6076395778634186048L;

  private final HorizontalLayout hl = new HorizontalLayout();

  private final ComboBox<MeasureDimension> dimensionCB = new ComboBox<>();
  private final ComboBox<MeasureUnit> unitCB = new ComboBox<>();

  public RFWComboBoxMeasureUnit(String caption) {
    setCaption(caption);
    addStyleName(FWVad.STYLE_COMPONENT_ASSOCIATIVE); // Estilo para deixar os campos unidos

    hl.setSizeFull();
    hl.setSpacing(false);

    dimensionCB.setWidth("100%");
    dimensionCB.setItems(MeasureDimension.values());
    hl.addComponent(dimensionCB);
    dimensionCB.addValueChangeListener(e -> changedDimensionValue());
    dimensionCB.setItemCaptionGenerator(e -> RFWBundle.get(e));

    unitCB.setWidth("100%");
    unitCB.setItemCaptionGenerator(e -> RFWBundle.get(e.getClass().getCanonicalName() + '.' + e.name()));
    hl.addComponent(unitCB);
  }

  private void changedDimensionValue() {
    // Se o valor da dimensão mudou temos que refazer todo o conteúdo do combo de unidades
    unitCB.setValue(null);
    unitCB.setItems(new MeasureUnit[0]);
    final MeasureDimension dValue = this.dimensionCB.getValue();
    if (dValue != null && dValue.getUnits() != null) {
      unitCB.setItems(dValue.getUnits());
    }
  }

  @Override
  protected Component initContent() {
    return this.hl;
  }

  @Override
  public MeasureUnit getValue() {
    return this.unitCB.getValue();
  }

  @Override
  protected void doSetValue(MeasureUnit value) {
    // Ao definir um novo valor, primeiro temos de acertar o combo de Dimensões pois ele altera o valor da unidade
    if (value == null) {
      this.dimensionCB.setValue(null);
      this.unitCB.setValue(null);
    } else {
      this.dimensionCB.setValue(value.getDimension());
      this.unitCB.setValue(value);
    }
  }

  /**
   * Repassa o ValueChangeListener para o Combo de Unidade da Medida, já que é o valor desse combo que realmente fica associado ao "value" deste componente associative
   */
  @Override
  public Registration addValueChangeListener(ValueChangeListener<MeasureUnit> listener) {
    return this.unitCB.addValueChangeListener(listener);
  }

  public void setDimensions(MeasureDimension... dimensions) {
    this.dimensionCB.setItems(dimensions);
    if (!RUArray.contains(dimensions, this.dimensionCB.getValue())) {
      this.dimensionCB.setValue(null);
    }
  }

  public void addBlurListener(BlurListener listener) {
    this.unitCB.addBlurListener(listener);
    this.dimensionCB.addBlurListener(listener);
  }

  @Override
  public void setReadOnly(boolean readOnly) {
    super.setReadOnly(readOnly);
    this.unitCB.setReadOnly(readOnly);
    this.dimensionCB.setReadOnly(readOnly);
  }
}