package br.eng.rodrigogml.rfw.vaadin.components;

import java.util.List;

import com.vaadin.server.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

import br.eng.rodrigogml.rfw.kernel.bundle.RFWBundle;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWCEPDataFormatter;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWValidationException;
import br.eng.rodrigogml.rfw.kernel.interfaces.RFWDBProvider;
import br.eng.rodrigogml.rfw.kernel.location.LocationAddressDefaultVO;
import br.eng.rodrigogml.rfw.kernel.location.LocationAddressDefaultVO_;
import br.eng.rodrigogml.rfw.kernel.preprocess.PreProcess;
import br.eng.rodrigogml.rfw.kernel.vo.RFWMO;
import br.eng.rodrigogml.rfw.vaadin.components.window.RFWCEPPickerWindow;
import br.eng.rodrigogml.rfw.vaadin.interfaces.RFWUI;
import br.eng.rodrigogml.rfw.vaadin.utils.FWVad;
import br.eng.rodrigogml.rfw.vaadin.utils.FWVad.ButtonType;
import br.eng.rodrigogml.rfw.vaadin.utils.TreatException;

/**
 * Description: Permite a entrade de um CEP e j� inclui automaticamente o bot�o de "pick" de endere�os.<br>
 *
 * @author Rodrigo Leit�o
 * @since 10.0.0 (16 de ago de 2018)
 */
public class RFWCEPField extends CustomField<String> {

  private static final long serialVersionUID = -8728758975717178279L;

  private final HorizontalLayout hl = new HorizontalLayout();
  private final TextField cepField = new TextField();
  private final Button magicButton = FWVad.createButton(ButtonType.MAGIC, e -> clickedButtonMagic(e));
  private final Button pickerButton = FWVad.createButton(ButtonType.PICKER, e -> clickedButtonPicker(e));

  private RFWCEPFieldNewCEPPickedListener newCEPPickedListener = null;

  private final RFWDBProvider dataProvider;

  /**
   * Interface de listener do evento de quando um novo cep for selecionado.
   */
  public static interface RFWCEPFieldNewCEPPickedListener {
    public void newCEPPickedEvent(LocationAddressDefaultVO vo) throws RFWException;
  }

  public RFWCEPField(String caption, RFWDBProvider dataProvider) throws RFWException {
    PreProcess.requiredNonNullCritical(dataProvider, "O RFWCEPField requer um dataProvider para funcionar corretamente!");

    // Demais configura��es
    setCaption(caption);
    addStyleName("rfwAssociativeComponent"); // Estilo para deixar os campos unidos
    this.hl.setSpacing(false);
    this.setWidth("220px");
    this.dataProvider = dataProvider;
  }

  private void clickedButtonMagic(ClickEvent e) {
    try {
      // Quando clica no bot�o m�gico, validamos se j� temos um CEP preenchido para realizar a busca
      RFWCEPDataFormatter.getInstance().validate(cepField.getValue(), RFWUI.getLocaleCurrent());
      final String value = RFWCEPDataFormatter.getInstance().toVO(cepField.getValue(), RFWUI.getLocaleCurrent());
      if (!"".equals(value)) {
        // Se � v�lido e diferente de vazio realizamos a busca pelo CEP para ver quantos existem
        RFWMO mo = new RFWMO();
        mo.equal(LocationAddressDefaultVO_.vo().cep(), value);
        final List<LocationAddressDefaultVO> cepList = this.dataProvider.findList(LocationAddressDefaultVO.class, mo, null, null, null, null);
        if (cepList.size() == 0) {
          throw new RFWValidationException("CEP n�o encontrado! Ou o CEP est� errado, ou o CEP � desconhecido.");
        } else if (cepList.size() > 1) {
          // Se encontramos mais de um CEP abrimos o picker j� com o filtro do CEP para que o usu�rio possa escolher o endere�o correto.
          clickedButtonPicker(e);
        } else {
          // Se s� temos um, utilizamos ele direto para preencher o endere�o
          newCEPPicked(cepList.get(0));
        }
      } else {
        // Se est� vazio, exibimos a mensagem para preencher o campo
        throw new RFWValidationException("Informe corretamente o CEP para preencher o endere�o automaticamente. Ou clique no bot�o ao lado para buscar o CEP pelo endere�o.");
      }
    } catch (Throwable e1) {
      TreatException.treat(e1);
    }
  }

  private void clickedButtonPicker(ClickEvent e) {
    try {
      RFWCEPDataFormatter.getInstance().validate(cepField.getValue(), RFWUI.getLocaleCurrent());
      final String value = RFWCEPDataFormatter.getInstance().toVO(cepField.getValue(), RFWUI.getLocaleCurrent());
      RFWCEPPickerWindow pick = new RFWCEPPickerWindow(value, this.dataProvider);
      pick.setConfirmListener(vo -> newCEPPicked(vo));
      FWVad.addWindow(pick);
    } catch (Throwable e1) {
      TreatException.treat(e1);
    }
  }

  /**
   * M�todo chamado quando o picker for executado e um novo CEP for selecionado.
   */
  private void newCEPPicked(LocationAddressDefaultVO... vos) throws RFWException {
    LocationAddressDefaultVO vo = vos[0];
    this.cepField.setValue(RFWCEPDataFormatter.getInstance().toPresentation(vo.getCep(), RFWUI.getLocaleCurrent()));
    // Se existir um listener avisamos ele tamb�m
    if (this.newCEPPickedListener != null) this.newCEPPickedListener.newCEPPickedEvent(vo);
  }

  @Override
  protected Component initContent() {
    final RFWCEPDataFormatter df = RFWCEPDataFormatter.getInstance();

    this.magicButton.setHeight("100%");
    this.pickerButton.setHeight("100%");

    this.cepField.setWidth("100%");
    this.cepField.addStyleName(FWVad.STYLE_ALIGN_CENTER);
    this.cepField.setMaxLength(df.getMaxLenght());

    this.cepField.addValueChangeListener(e -> this.fireEvent(e)); // Dispara o evento de altera��o do pr�prio component RFWCEPField
    this.cepField.addValueChangeListener(e -> validate());
    this.cepField.addBlurListener(e -> {
      try {
        // Temos de converter primeiro para o VO, pq o m�todo que formata "convertToPresentation()" s� espera um valor j� v�lidado e pronto para ser utilizado
        df.validate(cepField.getValue(), RFWUI.getLocaleCurrent());
        String voValue = df.toVO(cepField.getValue(), RFWUI.getLocaleCurrent());
        cepField.setValue(df.toPresentation(voValue, RFWUI.getLocaleCurrent()));
      } catch (RFWException e1) {
        // N�o fazemos nada pois o erro j� foi exibido atraves da valida��o do converter.
      }
    });

    this.hl.setWidth("215px");
    this.hl.addComponent(cepField);
    this.hl.setExpandRatio(cepField, 1F);
    this.hl.addComponent(magicButton);
    this.hl.addComponent(pickerButton);

    return this.hl;
  }

  /**
   * Valida os campos e define a mensagem no componente.
   */
  private void validate() {
    RFWCEPField.this.setComponentError(null);

    try {
      cepField.removeStyleName("error");
      if (!"".equals(cepField.getValue())) {
        RFWCEPDataFormatter.getInstance().validate(cepField.getValue(), RFWUI.getLocaleCurrent());
      }
    } catch (RFWException e1) {
      RFWCEPField.this.setComponentError(new UserError(RFWBundle.get(e1)));
      cepField.addStyleName("error");
    }
  }

  /**
   *
   *
   * @return
   */
  @Override
  public String getValue() {
    return this.cepField.getValue();
  }

  @Override
  protected void doSetValue(String value) {
    if (value == null || "".equals(value)) {
      this.cepField.setValue("");
    } else {
      this.cepField.setValue(RFWCEPDataFormatter.getInstance().toPresentation(value, RFWUI.getLocaleCurrent()));
    }
  }

  /**
   *
   *
   * @return
   */
  public RFWCEPFieldNewCEPPickedListener getNewCEPPickedListener() {
    return newCEPPickedListener;
  }

  /**
   *
   *
   * @param newCEPPickedListener
   */
  public void setNewCEPPickedListener(RFWCEPFieldNewCEPPickedListener newCEPPickedListener) {
    this.newCEPPickedListener = newCEPPickedListener;
  }

  /**
   *
   *
   * @return
   */
  public TextField getCepField() {
    return cepField;
  }

  /**
   *
   *
   * @return
   */
  public Button getMagicButton() {
    return magicButton;
  }

  /**
   *
   *
   * @return
   */
  public Button getPickerButton() {
    return pickerButton;
  }

  @Override
  public boolean isEnabled() {
    return this.cepField.isEnabled();
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.cepField.setEnabled(enabled);
    this.magicButton.setEnabled(enabled);
    this.pickerButton.setEnabled(enabled);
  }

  @Override
  public boolean isReadOnly() {
    return this.cepField.isReadOnly();
  }

  @Override
  public void setReadOnly(boolean readOnly) {
    this.cepField.setReadOnly(readOnly);
    this.magicButton.setEnabled(!readOnly);
    this.pickerButton.setEnabled(!readOnly);
  }

}
