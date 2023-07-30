package br.eng.rodrigogml.rfw.vaadin.components;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;

import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWRunTimeException;
import br.eng.rodrigogml.rfw.kernel.interfaces.RFWDBProvider;
import br.eng.rodrigogml.rfw.kernel.location.LocationCityDefaultVO;
import br.eng.rodrigogml.rfw.kernel.location.LocationCityDefaultVO_;
import br.eng.rodrigogml.rfw.kernel.location.LocationStateDefaultVO;
import br.eng.rodrigogml.rfw.kernel.location.LocationStateDefaultVO_;
import br.eng.rodrigogml.rfw.kernel.preprocess.PreProcess;
import br.eng.rodrigogml.rfw.kernel.vo.RFWMO;
import br.eng.rodrigogml.rfw.kernel.vo.RFWOrderBy;
import br.eng.rodrigogml.rfw.vaadin.providers.UIDataProvider;
import br.eng.rodrigogml.rfw.vaadin.utils.FWVad;

/**
 * Description: Cria um componente para exibir "Estado/Cidade" em um fomul?rio para o usu?rio.<br>
 *
 * @author Rodrigo Leit?o
 * @since 10.0.0 (21 de ago de 2018)
 */
public class RFWComboBoxCity extends CustomField<LocationCityDefaultVO> {

  private static final long serialVersionUID = -8383167816081264303L;

  private ComboBox<LocationCityDefaultVO> cityCB = new ComboBox<>();
  private ComboBox<LocationStateDefaultVO> stateCB = new ComboBox<>();

  private UIDataProvider<LocationCityDefaultVO> cityProvider = null;
  private final HorizontalLayout hl = new HorizontalLayout();

  public RFWComboBoxCity(String caption, RFWDBProvider dataProvider) throws RFWException {
    PreProcess.requiredNonNullCritical(dataProvider, "O RFWComboBoxCity requer um dataProvider para funcionar corretamente!");

    // Demais configurações
    setCaption(caption);
    addStyleName(FWVad.STYLE_COMPONENT_ASSOCIATIVE); // Estilo para deixar os campos unidos

    try {
      hl.setSizeFull();
      hl.setSpacing(false);

      final UIDataProvider<LocationStateDefaultVO> stateProvider = new UIDataProvider<>(LocationStateDefaultVO.class, RFWOrderBy.createInstance(LocationStateDefaultVO_.vo().name()), null, dataProvider);
      stateProvider.setFilterAttribute(LocationStateDefaultVO_.vo().name());
      stateCB.setDataProvider(stateProvider);
      stateCB.setWidth("100%");
      stateCB.addBlurListener(e -> updateCityCB());
      stateCB.setItemCaptionGenerator(stateVO -> stateVO.getName());
      stateCB.addValueChangeListener(e -> {
        // Verificamos se o conteúdo do combo da cidade ainda é válido
        if (cityCB.getValue() != null) {
          if (stateCB.getValue() == null || !cityCB.getValue().getLocationStateVO().getId().equals(stateCB.getValue().getId())) cityCB.setValue(null);
        }
      });

      cityProvider = new UIDataProvider<>(LocationCityDefaultVO.class, RFWOrderBy.createInstance(LocationCityDefaultVO_.vo().name()), new String[] { LocationCityDefaultVO_.vo().locationStateVO().id() }, dataProvider);
      cityProvider.setFilterAttribute(LocationCityDefaultVO_.vo().name());
      cityCB.setDataProvider(cityProvider);
      cityCB.setWidth("100%");
      cityCB.setItemCaptionGenerator(cityVO -> cityVO.getName());
      cityCB.addValueChangeListener(e -> {
        // Se o valor do estado estiver nulo, definimos
        if (cityCB.getValue() != null) stateCB.setValue(cityCB.getValue().getLocationStateVO());
      });

      hl.addComponent(this.stateCB);
      hl.setExpandRatio(this.stateCB, 0.5f);
      hl.addComponent(this.cityCB);
      hl.setExpandRatio(this.cityCB, 1f);
      hl.setComponentAlignment(this.cityCB, Alignment.TOP_LEFT);
    } catch (RFWException e) {
      throw new RFWRunTimeException(e);
    }
  }

  @Override
  protected Component initContent() {
    return hl;
  }

  private void updateCityCB() {
    RFWMO mo = new RFWMO();
    if (stateCB.getValue() != null) {
      mo.equal(LocationCityDefaultVO_.vo().locationStateVO().id(), stateCB.getValue().getId());
    }
    try {
      this.cityProvider.setRfwMO(mo);
    } catch (RFWException e) {
      throw new RFWRunTimeException(e);
    }
  }

  @Override
  public LocationCityDefaultVO getValue() {
    return this.cityCB.getValue();
  }

  @Override
  protected void doSetValue(LocationCityDefaultVO cityVO) {
    this.stateCB.setValue(null);
    if (cityVO != null) {
      if (cityVO.getLocationStateVO() == null) throw new RFWRunTimeException("Para definir a cidade no combo, é necessário que ela traga a informação do estado junto!");
      updateCityCB();
    }
    this.cityCB.setValue(cityVO);
  }

  @Override
  public boolean isEnabled() {
    return this.cityCB.isEnabled() && this.stateCB.isEnabled();
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.cityCB.setEnabled(enabled);
    this.stateCB.setEnabled(enabled);
  }

  @Override
  public boolean isReadOnly() {
    return this.cityCB.isReadOnly() && this.stateCB.isReadOnly();
  }

  @Override
  public void setReadOnly(boolean readOnly) {
    this.cityCB.setReadOnly(readOnly);
    this.stateCB.setReadOnly(readOnly);
  }

  @Override
  public Registration addValueChangeListener(ValueChangeListener<LocationCityDefaultVO> listener) {
    return this.cityCB.addValueChangeListener(listener);
  }

  @Override
  public Registration addListener(Listener listener) {
    return this.cityCB.addListener(listener);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void removeListener(Listener listener) {
    this.cityCB.removeListener(listener);
  }

}
