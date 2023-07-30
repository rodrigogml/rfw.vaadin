package br.eng.rodrigogml.rfw.vaadin.components.window;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.TextField;

import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWCEPDataFormatter;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;
import br.eng.rodrigogml.rfw.kernel.interfaces.RFWDBProvider;
import br.eng.rodrigogml.rfw.kernel.location.LocationAddressDefaultVO;
import br.eng.rodrigogml.rfw.kernel.location.LocationAddressDefaultVO_;
import br.eng.rodrigogml.rfw.kernel.vo.GVO;
import br.eng.rodrigogml.rfw.vaadin.interfaces.RFWUI;

/**
 * Description: Janela para buscar um endereço/CEP.<br>
 *
 * @author Rodrigo Leitão
 * @since 10.0.0 (21 de ago de 2018)
 */
public class RFWCEPPickerWindow extends RFWPickerWindow<LocationAddressDefaultVO> {

  private static final long serialVersionUID = -763666747006766921L;

  public RFWCEPPickerWindow(RFWDBProvider dataProvider) throws RFWException {
    this(null, dataProvider);
  }

  /**
   * Cria o Picker já com um filtro no campo CEP.
   *
   * @param cep CEP a ser usado como filtro.
   * @throws RFWException
   */
  public RFWCEPPickerWindow(String cep, RFWDBProvider dataProvider) throws RFWException {
    super(LocationAddressDefaultVO.class, "Busca CEP", new ThemeResource("icon/pickerblack_64.png"), dataProvider);

    // ==>>> Cria os campos de do Filtro
    final TextField cepField = (TextField) this.getUiFac().createMOField(LocationAddressDefaultVO_.vo().cep(), "100px", "100px");

    this.getUiFac().createMOField(LocationAddressDefaultVO_.vo().name(), "300px", "500px");
    this.getUiFac().createMOField(LocationAddressDefaultVO_.vo().neighborhood(), "100px", "300px");
    this.getUiFac().createMOField(LocationAddressDefaultVO_.vo().locationCityVO().path(), "300px", "500px");

    // Se temos um cep definido, já colocamos esse conteúdo no campo para ser utilizado no MO de filtro
    if (cep != null) cepField.setValue(RFWCEPDataFormatter.getInstance().toPresentation(cep, RFWUI.getLocaleCurrent()));

    // ==>>> Criamos o GRID de listagem e suas colunas
    final Grid<GVO<LocationAddressDefaultVO>> grid = this.getUiFac().createGridForMO(SelectionMode.SINGLE, null, new String[] { LocationAddressDefaultVO_.vo().locationCityVO().locationStateVO().name() }, null);
    this.getUiFac().addGridForMOColumn(LocationAddressDefaultVO_.vo().cep());
    this.getUiFac().addGridForMOColumn(LocationAddressDefaultVO_.vo().name());
    this.getUiFac().addGridForMOColumn(LocationAddressDefaultVO_.vo().neighborhood());
    this.getUiFac().addGridForMOColumn(LocationAddressDefaultVO_.vo().locationCityVO().name());
    this.getUiFac().addGridForMOColumn(LocationAddressDefaultVO_.vo().locationCityVO().locationStateVO().name());

    // Coloca o Grid no layout
    super.setGrid(grid);
  }

}
