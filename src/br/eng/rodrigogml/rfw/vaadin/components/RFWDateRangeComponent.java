package br.eng.rodrigogml.rfw.vaadin.components;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.vaadin.hene.popupbutton.PopupButton;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import br.eng.rodrigogml.rfw.kernel.RFW;
import br.eng.rodrigogml.rfw.vaadin.utils.FWVad;
import br.eng.rodrigogml.rfw.vaadin.utils.FWVad.ButtonType;

/**
 * Description: Componente Uitlizado para exibir um Range da Datas, utilizado principalmente para filtros..<br>
 *
 * @author Rodrigo Leitão
 * @since 7.1.0 (5 de mai de 2019)
 */
public class RFWDateRangeComponent extends CustomField<LocalDateTime> {

  private static final long serialVersionUID = 5790484322081224019L;

  public static enum RFWDateRangeScope {
    /**
     * Define as sugestões somente com períodos Futuros
     */
    FUTURE,
    /**
     * Define as sugestões somente com períodos Passados
     */
    PAST,
    /**
     * Define as sugestões com todos os períodos existentes.
     */
    ALL
  }

  final HorizontalLayout hl = new HorizontalLayout();

  final RFWDateRangeScope scope;

  /**
   * Componente {@link DateTimeField} utilizado para entrada do início do período.
   */
  final DateTimeField startDateTime = new DateTimeField();
  /**
   * Componente {@link DateTimeField} utilizado para entrada do final do período.
   */
  final DateTimeField endDateTime = new DateTimeField();

  public RFWDateRangeComponent(String caption, DateTimeResolution resolution, RFWDateRangeScope scope) {
    super();
    super.setCaption(caption);

    this.scope = scope;

    this.startDateTime.setResolution(resolution);
    this.endDateTime.setResolution(resolution);
    this.startDateTime.setWidth("150px");
    this.endDateTime.setWidth("150px");

    this.hl.setSpacing(false);
    this.hl.setSizeFull();
    this.hl.addStyleName("rfwAssociativeComponent"); // Estilo para deixar os campos unidos
    this.hl.setSizeUndefined();
    super.setSizeUndefined();
  }

  @Override
  protected Component initContent() {
    PopupButton rangeBT = new PopupButton();
    rangeBT.setIcon(new ThemeResource("icon/changeDate_24.png"));
    {
      VerticalLayout vl = new VerticalLayout();
      vl.setMargin(false);
      vl.setSpacing(false);
      rangeBT.setContent(vl);

      // Hoje
      {
        Button optbt = new Button("Hoje");
        optbt.addStyleName(ValoTheme.BUTTON_LINK);
        optbt.setCaptionAsHtml(true);
        optbt.addClickListener(e -> {
          setToday();
          rangeBT.setPopupVisible(Boolean.FALSE);
        });
        vl.addComponent(optbt);
      }
      // Hoje + 7 Dias
      if (this.scope == RFWDateRangeScope.ALL || this.scope == RFWDateRangeScope.FUTURE) {
        Button optbt = new Button("Hoje + 7 Dias");
        optbt.addStyleName(ValoTheme.BUTTON_LINK);
        optbt.setCaptionAsHtml(true);
        optbt.addClickListener(e -> {
          setTodayToSevenDays();
          rangeBT.setPopupVisible(Boolean.FALSE);
        });
        vl.addComponent(optbt);
      }
      // Mês anterior
      if (this.scope == RFWDateRangeScope.ALL || this.scope == RFWDateRangeScope.PAST) {
        Button optbt = new Button("Mês Anterior");
        optbt.addStyleName(ValoTheme.BUTTON_LINK);
        optbt.setCaptionAsHtml(true);
        optbt.addClickListener(e -> {
          setMonthLast();
          rangeBT.setPopupVisible(Boolean.FALSE);
        });
        vl.addComponent(optbt);
      }
      // Mês Atual
      {
        Button optbt = new Button("Mês Atual");
        optbt.addStyleName(ValoTheme.BUTTON_LINK);
        optbt.setCaptionAsHtml(true);
        optbt.addClickListener(e -> {
          setMonthCurrent();
          rangeBT.setPopupVisible(Boolean.FALSE);
        });
        vl.addComponent(optbt);
      }
      // Mês Próximo
      if (this.scope == RFWDateRangeScope.ALL || this.scope == RFWDateRangeScope.FUTURE) {
        Button optbt = new Button("Próximo Mês");
        optbt.addStyleName(ValoTheme.BUTTON_LINK);
        optbt.setCaptionAsHtml(true);
        optbt.addClickListener(e -> {
          setMonthCurrent();
          rangeBT.setPopupVisible(Boolean.FALSE);
        });
        vl.addComponent(optbt);
      }

      rangeBT.addPopupVisibilityListener(evt -> {
        if (evt.isPopupVisible()) {
          ((Button) vl.getComponent(0)).focus();
        } else {
          rangeBT.focus();
        }
      });
    }
    rangeBT.setWidth("70px");

    Button repeatValue = FWVad.createButton(ButtonType.REPEATVALUE, evt -> {
      endDateTime.setValue(startDateTime.getValue());
    });
    repeatValue.setWidth("57px");
    repeatValue.setDescription("Repete o valor de início do período no campo de final do período.");

    this.hl.addComponent(startDateTime);
    this.hl.addComponent(endDateTime);
    this.hl.addComponent(repeatValue);
    this.hl.addComponent(rangeBT);

    hl.setExpandRatio(rangeBT, 1f);
    hl.setComponentAlignment(rangeBT, Alignment.TOP_LEFT);

    return hl;
  }

  private void setToday() {
    LocalDate now = RFW.getDate();
    this.startDateTime.setValue(now.atStartOfDay());
    this.endDateTime.setValue(now.atStartOfDay().plusDays(1).minusSeconds(1));
  }

  private void setTodayToSevenDays() {
    LocalDate now = RFW.getDate();
    this.startDateTime.setValue(now.atStartOfDay());
    this.endDateTime.setValue(now.atStartOfDay().plusDays(8).minusSeconds(1));
  }

  /**
   * Define o período como sendo o mês corrente.
   */
  public void setMonthCurrent() {
    LocalDate now = RFW.getDate();
    final LocalDateTime start = now.withDayOfMonth(1).atStartOfDay();
    this.startDateTime.setValue(start);
    final LocalDateTime end = now.plusMonths(1).withDayOfMonth(1).atStartOfDay().minusSeconds(1);
    this.endDateTime.setValue(end);
  }

  /**
   * Define o período como sendo o mês anterior.
   */
  public void setMonthLast() {
    LocalDate now = RFW.getDate();
    final LocalDateTime start = now.withDayOfMonth(1).minusMonths(1).atStartOfDay();
    this.startDateTime.setValue(start);
    final LocalDateTime end = now.withDayOfMonth(1).atStartOfDay().minusSeconds(1);
    this.endDateTime.setValue(end);
  }

  /**
   *
   *
   * @return
   */
  @Override
  public LocalDateTime getValue() {
    return this.startDateTime.getValue();
  }

  /**
   *
   *
   * @return
   */
  public LocalDateTime getValueFinal() {
    return this.endDateTime.getValue();
  }

  /**
   *
   *
   * @param value
   */
  @Override
  public void setValue(LocalDateTime value) {
    doSetValue(value);
  }

  @Override
  protected void doSetValue(LocalDateTime value) {
    this.startDateTime.setValue(value);
  }

  public void setValueFinal(LocalDateTime value) {
    this.endDateTime.setValue(value);
  }

  @Override
  public void clear() {
    this.startDateTime.clear();
    this.endDateTime.clear();
  }

  /**
   * Recupera o componente {@link DateTimeField} utilizado para entrada do início do período.
   *
   * @return the componente {@link DateTimeField} utilizado para entrada do início do período
   */
  public DateTimeField getStartDateTime() {
    return startDateTime;
  }

  /**
   * Recupera o componente {@link DateTimeField} utilizado para entrada do final do período.
   *
   * @return the componente {@link DateTimeField} utilizado para entrada do final do período
   */
  public DateTimeField getEndDateTime() {
    return endDateTime;
  }

  @Override
  public float getWidth() {
    return 430;
  }

  @Override
  public Unit getWidthUnits() {
    return Unit.PIXELS;
  }

}
