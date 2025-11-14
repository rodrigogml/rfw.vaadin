package br.eng.rodrigogml.rfw.vaadin.main.utils;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.vaadin.addons.ComboBoxMultiselect;
import org.vaadin.hene.popupbutton.PopupButton;

import com.vaadin.data.Binder;
import com.vaadin.data.Binder.Binding;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.FieldEvents.BlurNotifier;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.shared.ui.datefield.DateTimeResolution;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.StyleGenerator;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.FooterCell;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.components.grid.MultiSelectionModel.SelectAllCheckBoxVisibility;
import com.vaadin.ui.renderers.TextRenderer;
import com.vaadin.ui.themes.ValoTheme;

import br.eng.rodrigogml.rfw.kernel.RFW;
import br.eng.rodrigogml.rfw.kernel.bundle.RFWBundle;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWBigDecimalDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWCEPDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWCNPJDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWCPFDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWCPFOrCNPJDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWCurrencyBigDecimalDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWDateTimeDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWEmailDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWIEDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWIntegerDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWLongDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWMeasureUnitDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWPercentageDataFormatter;
import br.eng.rodrigogml.rfw.kernel.dataformatters.RFWPhoneDataFormatter;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWCriticalException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWRunTimeException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWValidationException;
import br.eng.rodrigogml.rfw.kernel.interfaces.RFWDBProvider;
import br.eng.rodrigogml.rfw.kernel.logger.RFWLogger;
import br.eng.rodrigogml.rfw.kernel.measureruler.interfaces.MeasureUnit;
import br.eng.rodrigogml.rfw.kernel.measureruler.interfaces.MeasureUnit.MeasureDimension;
import br.eng.rodrigogml.rfw.kernel.preprocess.PreProcess;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaBigDecimalCurrencyField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaBigDecimalField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaBigDecimalPercentageField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaBooleanField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaCollectionField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaDateField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaEnumField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaGenericField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaIntegerField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaLongField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaRelationshipField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaStringCEPField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaStringCNPJField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaStringCPFField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaStringCPFOrCNPJField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaStringEmailField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaStringField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaStringIEField;
import br.eng.rodrigogml.rfw.kernel.rfwmeta.RFWMetaStringPhoneField;
import br.eng.rodrigogml.rfw.kernel.utils.RUArray;
import br.eng.rodrigogml.rfw.kernel.utils.RUDateTime;
import br.eng.rodrigogml.rfw.kernel.utils.RUNumber;
import br.eng.rodrigogml.rfw.kernel.utils.RUReflex;
import br.eng.rodrigogml.rfw.kernel.utils.RUTypes;
import br.eng.rodrigogml.rfw.kernel.vo.GVO;
import br.eng.rodrigogml.rfw.kernel.vo.RFWMO;
import br.eng.rodrigogml.rfw.kernel.vo.RFWMO.AppendMethod;
import br.eng.rodrigogml.rfw.kernel.vo.RFWOrderBy;
import br.eng.rodrigogml.rfw.kernel.vo.RFWVO;
import br.eng.rodrigogml.rfw.vaadin.components.RFWAssociativeComponent;
import br.eng.rodrigogml.rfw.vaadin.components.RFWButtonToggle;
import br.eng.rodrigogml.rfw.vaadin.components.RFWComboBoxMeasureUnit;
import br.eng.rodrigogml.rfw.vaadin.components.RFWDateRangeComponent;
import br.eng.rodrigogml.rfw.vaadin.components.RFWDateRangeComponent.RFWDateRangeScope;
import br.eng.rodrigogml.rfw.vaadin.components.RFWTagsSelector;
import br.eng.rodrigogml.rfw.vaadin.components.stylegenerators.UIGridRowHighlight;
import br.eng.rodrigogml.rfw.vaadin.interfaces.RFWGridDoubleClickListener;
import br.eng.rodrigogml.rfw.vaadin.interfaces.RFWShortcutListener;
import br.eng.rodrigogml.rfw.vaadin.interfaces.RFWUI;
import br.eng.rodrigogml.rfw.vaadin.providers.IconProvider;
import br.eng.rodrigogml.rfw.vaadin.providers.UIDataProvider;
import br.eng.rodrigogml.rfw.vaadin.providers.UIGridDataProvider;
import br.eng.rodrigogml.rfw.vaadin.providers.UITreeDataProvider;
import br.eng.rodrigogml.rfw.vaadin.utils.FWVad;
import br.eng.rodrigogml.rfw.vaadin.utils.FWVad.ButtonType;
import br.eng.rodrigogml.rfw.vaadin.utils.TreatException;

/**
 * Description: Classe utilitária para manipular e gerar os componentes do Vaadin. A ideia é impedir o excesso de hierarquia com os componentes mais simples do Vaadin.<br>
 *
 * @author Rodrigo Leitão
 * @since 10.0.0 (6 de ago de 2018)
 */
public class UIFactory<VO extends RFWVO> {

  /**
   * StyleGenerator reutilizável (menos memória criando uma classe para cada coluna).
   */
  private static StyleGenerator<?> STYLEGENERATOR_ALIGNCENTER = cs -> FWVad.STYLE_ALIGN_CENTER;
  /**
   * StyleGenerator reutilizável (menos memória criando uma classe para cada coluna).
   */
  private static StyleGenerator<?> STYLEGENERATOR_ALIGNRIGHT = cs -> FWVad.STYLE_ALIGN_RIGHT;

  /**
   * Description: Classe para realizar o Binder entre o RFWVO e os campos do Vaadin.<br>
   * DISCLAIMER: Deixamos de utilizar o Binder do próprio vaadin por ele trabalhar com um cache, e só transferir os dados para/do o VO nos métodos de writeBean/readBean. Desta forma muitas vezes o usuário já fez alterações nos campos, mas quando precisamos da informação é necessário procurar o campo para obter o valor ainda não tratado (com a formatação de locale, números em formato String, etc.).
   * Também não é possível chamar o método o tempo todo pq ao chamar ele valida os campos obrigatórios que não foram preenchidos e os deixa todos em vemelho com código de erro.<br>
   * <br>
   * Toda a funcionalidade do Binder do vaadin se encontra dentro do UIFactory. O bind é feito através desta classe e utiliza o evento de 'valueChange' para ler o valor do campo e converter para o VO conforme a necessidade. Deixando tudo de forma mais automática para realizar o Bind utilizando a própria estrutura do RFW.
   *
   * @author Rodrigo Leitão
   * @since 10.0.0 (31 de out de 2018)
   */
  public static class UIBinder<FIELDTYPE, BEANTYPE, BEAN> {

    /**
     * "Campo da tela" Objeto do vaadin que cria o field na tela.
     */
    private final HasValue<FIELDTYPE> field;

    /**
     * VO do RFW onde a propriedade/valor será lido.
     */
    private final BEAN bean;

    /**
     * Propriedade do VO que será ligada ao campo da tela. Aceita toda a estrutura de Neasted Properties do RFW.
     */
    private String property = null;

    /**
     * Define um {@link RFWDataFormatter} para tratar e converter os valores regionalizados da tela para o objeto, e vice-versa.
     */
    private final RFWDataFormatter<FIELDTYPE, BEANTYPE> formatter;

    /**
     * Permite definir algum objeto que será considerado como valor nulo. Isto é, se o valor retornado pelo campo for "equals" este objeto, o valor definido no VO será 'null', e se o required = true, marcará o campo como de preenchimento obrigatório.
     */
    private FIELDTYPE nullValue = null;

    /**
     * Salva referência para o objeto de registro do listener no Field. Necessária para remover o Listener de ValueChange do Field.
     */
    private Registration listenerRegistration = null;

    /**
     * Indica se devemos trocar listas de VO para GVO<VO> ou não.
     */
    private boolean wrapIntoGVO = false;

    /**
     * Construtor padrão para criar um Bind entre o VO e um Field de tela.<br>
     * Assim que o objeto for criado o valor do VO será automaticamente sincronizado para o Field.
     *
     * @param field "Campo da tela" Objeto do vaadin que cria o field na tela.
     * @param bean Qualquer objeto do java (tipo Bean, com métodos get/set) onde a propriedade/valor será lido.
     * @param property Propriedade do VO que será ligada ao campo da tela. Aceita toda a estrutura de Neasted Properties do RFW.
     * @param formatter Define um {@link RFWDataFormatter} para tratar e converter os valores regionalizados da tela para o objeto, e vice-versa.
     * @param required Define se o campo é obrigatório. Ajusta automaticamente a validação dos campos bem como o indicador de obrigatoriedade no campo.<br>
     *          <b>ATENÇÃO:</B> O valor passado aqui é repassado para o field, mas não é armazenado aqui! A mudança do atributo 'setRequiredIndicatorVisible(boolean)' do campo muda automaticamente as validações do bind. Isso permite que o desenvolvedor possa alterar as condições de obrigatoriedade diretamente no componente.
     * @param nullValue Permite definir algum objeto que será considerado como valor nulo. Isto é, se o valor retornado pelo campo for "equals" este objeto, o valor definido no VO será 'null', e se o required = true, marcará o campo como de preenchimento obrigatório. Caso o valod no VO seja nulo, este valor será atribuido no campo da tela.
     * @param wrapIntoGVO Caso definido como true, e o tipo do objeto do VO seja uma lista, o Bind vai reencapsular os objetos com o GVO e vice versa.
     */
    public UIBinder(HasValue<FIELDTYPE> field, BEAN bean, String property, RFWDataFormatter<FIELDTYPE, BEANTYPE> formatter, boolean required, FIELDTYPE nullValue, boolean wrapIntoGVO) {
      this.field = field;
      this.bean = bean;
      this.property = property;
      this.formatter = formatter;
      this.nullValue = nullValue;
      this.wrapIntoGVO = wrapIntoGVO;
      field.setRequiredIndicatorVisible(required);

      // Copiamos o valor inicial do Vo para o Field antes de colocar o listener
      toPresentation();

      // Colocamos o listener no campo para ouvir a cada vez que seu valor se alterar
      this.listenerRegistration = this.field.addValueChangeListener(e -> toVO(this.bean, this.property, this.field, this.formatter, this.nullValue, this.wrapIntoGVO));

      // Se temos um dataformatter, habilitamos o listener de onBlur() para refomatar o valor digitado pelo usuário
      if (this.formatter != null && (this.field instanceof BlurNotifier)) ((BlurNotifier) this.field).addBlurListener(e -> reformatContent());
    }

    /**
     * Verifica se o valor do campo está em um formato aceito e pode ser definido no objeto sem erros.
     *
     * @return true caso o campo esteja válido, false caso contrário. Quando false a mensagem do problema de preenchimento é aplicado diretamente no campo deixando-o com a aparência de erro.
     */
    public boolean validate() {
      boolean valid = true;
      try {
        try {
          FWVad.setComponentError(this.field, null);
          // Validamos se é possível converter o valor do campo para o objeto que é aplicado no VO
          Object value = auxConvertValueToVO(field, formatter, nullValue);
          // Se não lançou exception, verificamos agora se o valor é nulo, e se o campo é obrigatório
          if (value == null && this.field.isRequiredIndicatorVisible()) {
            FWVad.setComponentError(this.field, "É obrigatório preencher este campo.");
            valid = false;
          }
        } catch (RFWValidationException e) {
          FWVad.setComponentError(this.field, RFWBundle.get(e));
          valid = false;
        }
      } catch (Throwable e) {
        TreatException.treat(e);
        valid = false;
      }
      return valid;
    }

    /**
     * Copia o dado do campo para o VO.
     */
    @SuppressWarnings("unchecked")
    public static <BEAN, FIELDTYPE, BEANTYPE> void toVO(BEAN bean, String property, HasValue<FIELDTYPE> field, RFWDataFormatter<FIELDTYPE, BEANTYPE> formatter, Object nullValue, boolean wrapIntoGVO) {
      try {
        try {
          FWVad.setComponentError(field, null); // Limpa a exibição de erros
          Object value = auxConvertValueToVO(field, formatter, nullValue);
          final Object actualValue = RUReflex.getPropertyValue(bean, property);

          // Verificamos a necessidade de converter e criamos algumas regras
          if (value != null && Set.class.isAssignableFrom(value.getClass())) {
            Class<?> type = RUReflex.getPropertyTypeByType(bean.getClass(), property);
            if (List.class.isAssignableFrom(type)) {
              // Converte de Set para List - Essa regra é necessária por exemplo para o ComboBoxMultiSelect
              if (wrapIntoGVO) {
                value = ((Set<GVO<?>>) value).stream().map(GVO::getVO).collect(Collectors.toList());
              } else {
                value = ((Set<?>) value).stream().collect(Collectors.toList());
              }
            }
          } else if (value != null && LocalDate.class.isAssignableFrom(value.getClass())) { // Faz a conversão automática entre LocalDateTime e LocalDate
            Class<?> type = RUReflex.getPropertyTypeByType(bean.getClass(), property);
            if (LocalDateTime.class.isAssignableFrom(type)) {
              value = RUTypes.toLocalDate((LocalDateTime) value);
            }
          } else if (value != null && LocalDateTime.class.isAssignableFrom(value.getClass())) { // Faz a conversão automática entre LocalDateTime e LocalDate
            Class<?> type = RUReflex.getPropertyTypeByType(bean.getClass(), property);
            if (LocalDate.class.isAssignableFrom(type)) {
              value = ((LocalDateTime) value).toLocalDate();
            } else if (LocalTime.class.isAssignableFrom(type)) {
              value = ((LocalDateTime) value).toLocalTime();
            }
          }

          // Só vamos definir o valor se ele for diferente do valor atual.
          // Embora dê um overhead, ao fazer isso estamos protegendo o caso de um campo dentro de um subVO ter o valor definido para NULL.
          // E o próprio subVO seja nulo, não só o campo. Ao tentar escrever em um campo cujo o caminho seja nulo, o método setPropertyValue(...) resulta em critical exception
          if ((value == null ^ actualValue == null) || (value != null && !value.equals(actualValue))) RUReflex.setPropertyValue(bean, property, value, false);
        } catch (RFWCriticalException e) {
          if ("BIS10_000066".equals(e.getExceptionCode())) {
            throw new RFWCriticalException("O caminho para o atributo '${0}' está nulo! Verifique se todos os objetos intermediários já foram iniciados! O bind entre field e VOs não inicializa objetos automaticamente!", new String[] { property }, e);
          } else {
            throw e;
          }
        } catch (RFWValidationException e) {
          FWVad.setComponentError(field, RFWBundle.get(e));
        }
      } catch (Throwable e) {
        TreatException.treat(e);
      }
    }

    /**
     * Este método recupera o valor e faz a conversão para o objeto que será escrito no VO. Utilizado no método {@link #toVO()} e no {@link #validate()}. Assim centralizamos a implementação da lógica de conversão, utilização do DataFormatter, etc.
     *
     * @return Valor pronto para ir para o VO
     * @throws RFWException Lançado caso ocorra algum erro de conversão
     */
    @SuppressWarnings("unchecked")
    private static <FIELDTYPE, BEANTYPE> Object auxConvertValueToVO(HasValue<FIELDTYPE> field, RFWDataFormatter<FIELDTYPE, BEANTYPE> formatter, Object nullValue) throws RFWException {
      Object value = field.getValue();
      if (field instanceof ComboBoxMultiselect<?>) {
        if (value == null || ((Set<?>) value).size() == 0) {
          if (field.isRequiredIndicatorVisible()) FWVad.setComponentError(field, "É obrigatório preencher este campo.");
          value = null;
        }
      } else if (field instanceof CheckBoxGroup<?>) {
        if (value == null || ((Set<?>) value).size() == 0) {
          if (field.isRequiredIndicatorVisible()) FWVad.setComponentError(field, "É obrigatório preencher este campo.");
          value = null;
        } else {
          value = ((Set<?>) value).iterator().next();
        }
      } else {
        if (formatter != null) value = formatter.toVO((FIELDTYPE) value, RFWUI.getLocaleCurrent());
        if (value == null || value.equals(nullValue)) {
          if (field.isRequiredIndicatorVisible()) FWVad.setComponentError(field, "É obrigatório preencher este campo.");
          value = null;
        }
      }
      return value;
    }

    /**
     * Copia o dado do VO para o campo da tela.
     */
    @SuppressWarnings("unchecked")
    public void toPresentation() {
      try {
        FIELDTYPE value = UIBinder.toPresentation(bean, property, field, formatter, nullValue, wrapIntoGVO);
        field.clear();// Damos um Clear antes de redefinir o valor, pq caso seja o mesmo valor, em campos que tenham "Caption Generator", ele não atualiza se não houver mudança de valor.
        if (value == null) {
          // Alguns tipos de Field não aceitam o nulo, para isso convertemos o nulo no objeto que representa o nulo de cada componente visual
          if (field instanceof TextField) {
            ((TextField) field).setValue("");
          } else if (field instanceof CheckBoxGroup) {
            field.setValue((FIELDTYPE) new HashSet<FIELDTYPE>());
          } else {
            field.setValue(null);
          }
        } else {
          field.setValue(value);
        }
      } catch (Throwable e) {
        // Ignora a exception pq o método já colocou a mensagem no component e tratou a exceção
        if (RFW.isDevelopmentEnvironment()) e.printStackTrace();
      }
    }

    /**
     * Converte o dado para ser colocado no VO, MAS NÃO COLOCA NO CAMPO.
     *
     * @throws RFWException
     */
    @SuppressWarnings("unchecked")
    public static <BEAN, FIELDTYPE, BEANTYPE> FIELDTYPE toPresentation(BEAN bean, String property, HasValue<FIELDTYPE> field, RFWDataFormatter<FIELDTYPE, BEANTYPE> formatter, FIELDTYPE nullValue, boolean wrapIntoGVO) throws RFWException {
      FIELDTYPE value = null;
      try {
        FWVad.setComponentError(field, null); // Limpa a exibição de erros

        value = (FIELDTYPE) RUReflex.getPropertyValue(bean, property);

        if (wrapIntoGVO && List.class.isAssignableFrom(value.getClass())) {
          value = (FIELDTYPE) GVO.wrapToSet((List<RFWVO>) value);
        }

        // Se temos um valor substituto para o valor NULL do VO e no VO estiver nulo, o utilizamos
        if (value == null && nullValue != null) value = nullValue;

        // Se tiver um DataFormatter o utilizamos agora
        if (formatter != null) value = formatter.toPresentation((BEANTYPE) value, RFWUI.getLocaleCurrent());

        if (value != null && Date.class.isAssignableFrom(value.getClass())) {
          // Se o valor é um java.util.Date, temos de converter para LocalDateTime, pois o UIFactory cria o DataTimeField do Vaadin 8 que trabalha com as classes de data do Java 8, enquanto o RFW ainda usa as java.util.Date
          throw new RFWCriticalException("Por definição o RFW não deve trabalhar com java.utils.Date. Verifique o código e substitua por LocalDate, LocalTime ou LocalDateTime.");
        } else if (value == null && CheckBox.class.isAssignableFrom(field.getClass())) {
          // Como checkbox não aceita o valor nulo, vamos forçar um valor "False" quando o valor é nulo
          RUReflex.setPropertyValue(bean, property, Boolean.FALSE, false); // Já força o mesmo valor no VO para que não fiquem descasados (inicializa o VO)
          value = (FIELDTYPE) Boolean.FALSE;
        } else if (value != null && CheckBoxGroup.class.isAssignableFrom(field.getClass())) {
          // Se é um CheckBoxGroup e temos um valor boolean, temos que colocar o valor dentro de um Set
          HashSet<Boolean> hash = new HashSet<Boolean>();
          hash.add((Boolean) value);
          value = (FIELDTYPE) hash;
        } else if (ComboBoxMultiselect.class.isAssignableFrom(field.getClass())) {
          if (value != null) {
            value = (FIELDTYPE) ((List<?>) value).stream().collect(Collectors.toSet());
          } else {
            value = (FIELDTYPE) new HashSet<Object>();
          }
        }
      } catch (RFWValidationException e) {
        FWVad.setComponentError(field, RFWBundle.get(e));
        throw e;
      } catch (Throwable e) {
        TreatException.treat(e);
        throw e;
      }
      return value;
    }

    /**
     * Cancela o binding com o campo.<Br>
     * É necessário chamar este método sempre que quiser continuar utilizando o Field com outro Bind. Isso pq o Bind monitora a mudança do valor do campo da tela pelo valueChange, se o listener não for cancelado a cada mudança no campo da tela ele será acionado e alterará o valor do VO.
     */
    public void cancelBinding() {
      this.listenerRegistration.remove();
    }

    /**
     * Método auxiliar com a função de "pegar" o valor que está no campo (recem digitado pelo usuário), e formata-lo para ficar com a aparência adequada para o usuário.<br>
     * Este método deve ser chamado quando o usuário tira o foco do campo para não atrapalhar a digitação.<br>
     * Só tem alguma finalidade quando tempos um DataFormatter para fazer a formatação do valor.
     *
     * @param formatter RFWDataFormatter adequado para o tipo de valor do campo.
     * @param field Campo que precisa ter seu conteúdo reformatado.
     */
    @SuppressWarnings("unchecked")
    private void reformatContent() {
      try {
        if (this.formatter != null) {
          // Se o componente estiver em erro (por conta de uma entrada inválida) não fazemos nada aqui pois a reformatação também dará erro
          if (UIFactory.getComponentError(this.field) == null) {
            FIELDTYPE content = this.field.getValue();
            content = this.formatter.reformatPresentationContent(content, RFWUI.getLocaleCurrent());
            if (content == null) {
              // Alguns tipos de Field não aceitam o nulo, para isso convertemos o nulo no objeto que representa o nulo de cada componente visual
              if (field instanceof TextField) {
                ((TextField) field).setValue("");
              } else if (field instanceof CheckBoxGroup) {
                field.setValue((FIELDTYPE) new HashSet<FIELDTYPE>());
              } else {
                field.setValue(null);
              }
            } else {
              field.setValue(content);
            }
          }
        }
      } catch (RFWValidationException e) {
        // Não faz nada pois o método de toVO() já tratou de exibir o erro no componente
      } catch (Throwable e) {
        TreatException.treat(e);
      }
    }

    /**
     * Define o propriedade do VO que será ligada ao campo da tela. Aceita toda a estrutura de Neasted Properties do RFW.
     *
     * @param property the new propriedade do VO que será ligada ao campo da tela
     */
    public void setProperty(String property) {
      this.property = property;
    }

  }

  /**
   * Para os campos que suportam, deixa o seu conteúdo alinhado.
   */
  public static enum FieldAlignment {
    LEFT,
    CENTER,
    RIGHT
  }

  /**
   * Define os tipos de Label (aparência) que o RFW trabalha.
   */
  public static enum LabelType {
    /**
     * Tipo padrão do label, geralmente o tipo usado quando nenhum outro é explicitamente definido.
     */
    DEFAULT,
    /**
     * Deixa o label com uma aparência que se sobressaia em relação a outros, como para exibir dados alterados.
     */
    HIGHLIGHT,
    /**
     * Deixa o Label com o texto em negrito destacado, como o caption de um field.
     */
    FIELDCAPTION,

  }

  /**
   * Classe usada para registrar o componente e dados relacionados ao componente que não estão dentro do objeto.
   */
  private static class FieldMetaData {

    public final HasValue<?> component;
    public final String attributePath;
    /**
     * Define o tamanho mínimo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
     * O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço cedido será
     * de 3 colunas =~ 300px
     */
    private final String minWidth;
    /**
     * Define o tamanho máximo que o componente terá, mesmo que haja mais espaço na tela.<br>
     * O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço cedido será
     * de 3 colunas =~ 300px
     */
    private final String maxWidth;

    /**
     * DataFormatter utilizado no campo, quando utilizado.<br>
     * Necessário para desfazer a formatação do campo antes de incluir os dados no MO.
     */
    private final RFWDataFormatter<Object, Object> df;

    /**
     * Listener para criação do RFWMO. Utilizado nos campos custom feito pelo usuário.
     */
    private MOBuilderListener moBuilderListener;

    private FieldMetaData(HasValue<?> component, String attributePath, String minWidth, String maxWidth, RFWDataFormatter<Object, Object> df, MOBuilderListener moBuilderListener) {
      this.component = component;
      this.attributePath = attributePath;
      this.df = df;
      if (minWidth == null) {
        this.minWidth = "300px";
      } else {
        if (!minWidth.matches("[0-9]+(px|%)")) throw new RFWRunTimeException("O valor '" + minWidth + "' não é um tamanho aceitável. Utilize por exemplo '300px' ou '50%'");
        this.minWidth = minWidth;
      }
      if (maxWidth == null) {
        if (minWidth != null) {
          this.maxWidth = this.minWidth;
        } else {
          this.maxWidth = "300px";
        }
      } else {
        if (!maxWidth.matches("[0-9]+(px|%)")) throw new RFWRunTimeException("O valor '" + minWidth + "' não é um tamanho aceitável. Utilize por exemplo '300px' ou '50%'");
        this.maxWidth = maxWidth;
      }
      this.moBuilderListener = moBuilderListener;
    }
  }

  /**
   * Interface utilizada para criar camos customizados para filtro do MO.
   */
  public static interface MOBuilderListener {
    /**
     * Método chamado quando o UIFactory precisar que o campo customizado inclua sua clausula no MO.
     *
     * @param mo MO que será aplicado ao DataProvider do Grid para filtrar os dados.
     * @param attirbutePathOrID Atributo passado na hora de registro do campo no UIFactory.
     * @param comp Componente passado no momento do resitro.
     * @throws RFWException
     */
    public void build(RFWMO mo, String attirbutePathOrID, HasValue<?> comp) throws RFWException;
  }

  /**
   * Armazena referência para o objeto principal da Factory.<br>
   * Note que toda ves que um VO for definido, todos os objetos relacionados ao VO serão descartados, comos e uma nova instância de UIFactory tivesse sido criada. Campos do MO e outros objetos relacionados ao MO permanecem intocados.
   */
  private VO vo = null;

  /**
   * Armazena a classe do objeto que essa UIFactory Trabalha.
   */
  private Class<VO> voClass = null;

  /**
   * Esta Hash armazena os componentes que foram criados para campos de filtro (MO) e é usada para gerar o MO de filtro.<br>
   * A chave é o "attributePath" do campo.
   */
  private final LinkedHashMap<String, FieldMetaData> moFieldHash = new LinkedHashMap<>();

  /**
   * Esta Hash armazena os componentes que foram criados para campos de edição (CO) e é usada para recueprar o campo a partir da propriedade.
   */
  private final LinkedHashMap<String, HasValue<?>> voFieldHash = new LinkedHashMap<>();

  /**
   * Registra todos os UIBinding criados pelo path de acesso da propriedade.<br>
   */
  private final HashMap<String, UIBinder<?, ?, VO>> bindingMap = new HashMap<>();

  /**
   * Guarda referência do Grid criado para utilizar com os dados do MO.
   */
  private Grid<GVO<VO>> moGrid = null;

  /**
   * Define um RFWMO "base" padrão para ser utilizado no GRID do MO {@link #moGrid}. Se definido, este MO é clonado e utilizado como base para colocar as condições dos campos de filtro.<br>
   * Este MO deve ser utilizado quando temos algum filtro que deve ser sempre utilizado recuperar os dados para o Grid. Por exemplo, queremos apenas que só os objetos ativos sejam exibidos independente das opções de filtro que o usuário escolha.
   */
  private RFWMO baseRFWMO = null;

  /**
   * Referência para a linha de rodapé do Grid de MO (quando criada).
   */
  private FooterRow moFooterRow = null;

  /**
   * Salva a referência para o listener do botão de search.
   */
  private ClickListener searchListener = null;

  /**
   * Cria uma Factory já com um VO inicializado.
   */
  @SuppressWarnings("unchecked")
  public UIFactory(VO vo) {
    this.voClass = (Class<VO>) vo.getClass();
    setVO(vo);
  }

  /**
   * Cria uma factory sem um Objeto inicializado. Neste caso precisamos passar a classe do VO.
   */
  public UIFactory(Class<VO> voClass) {
    this.voClass = voClass;
  }

  /**
   * Este método cria um campo para ser usado como campo de busca. Baseia-se no tipo de dado presente no VO para criar o campo, mas o registra como campo de busca. Assim ele será utilizado para popular o MO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @return
   * @throws RFWException
   */
  public HasValue<?> createMOField(String propertyPath) throws RFWException {
    return createMOFieldImp(propertyPath, null, null, null, null, null, null, null, null);
  }

  /**
   * Este método cria um campo para ser usado como campo de busca. Baseia-se no tipo de dado presente no VO para criar o campo, mas o registra como campo de busca. Assim ele será utilizado para popular o MO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param minWidth Tamanho mínimo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço mínimo
   *          cedido será de 3 colunas =~ 300px.
   * @param maxWidth Tamanho máximo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço máximo
   *          cedido será de 3 colunas =~ 300px.
   * @return
   * @throws RFWException
   */
  public HasValue<?> createMOField(String propertyPath, String minWidth, String maxWidth) throws RFWException {
    return createMOFieldImp(propertyPath, minWidth, maxWidth, null, null, null, null, null, null);
  }

  /**
   * Este método cria um campo para ser usado como campo de busca. Baseia-se no tipo de dado presente no VO para criar o campo, mas o registra como campo de busca. Assim ele será utilizado para popular o MO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param minWidth Tamanho mínimo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço mínimo
   *          cedido será de 3 colunas =~ 300px.
   * @param maxWidth Tamanho máximo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço máximo
   *          cedido será de 3 colunas =~ 300px.
   * @param df
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   *
   * @return
   * @throws RFWException
   */
  public <FIELDTYPE, BEANTYPE> HasValue<?> createMOField(String propertyPath, String minWidth, String maxWidth, RFWDataFormatter<FIELDTYPE, BEANTYPE> df) throws RFWException {
    return createMOFieldImp(propertyPath, minWidth, maxWidth, null, null, null, null, df, null);
  }

  /**
   * Este método cria um campo para ser usado como campo de busca. Baseia-se no tipo de dado presente no VO para criar o campo, mas o registra como campo de busca. Assim ele será utilizado para popular o MO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param minWidth Tamanho mínimo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço mínimo
   *          cedido será de 3 colunas =~ 300px.
   * @param maxWidth Tamanho máximo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço máximo
   *          cedido será de 3 colunas =~ 300px.
   * @param captionAttribute Usado quando o componente criado refere-se à um {@link RFWMetaRelationshipField} e o objeto em questão é um RFWVO. Aqui devemos passar qual o atributo do VO será utilizado como Caption no ComboBox Criado.<br>
   *          <b>ATENÇÃO:</B> Note que o atributo passado aqui é em relação ao objeto que estará no Provider do ComboBox e não do objeto sobre o qual estamos criando os campos.<br>
   *          Por exemplo, o ItemVO tem um relaiconamento com o ItemTypeVO e utiliza este método paracriar o campo. O valor passado aqui deve ser o 'name' ou ItemTypeVO_.vo().name(), e não o caminho a partir do ItemVO, utilizado para gerar o campo.
   * @param filterAttributes Lista de atributos (ou atributo único) que serão utilizados para realizar o filtro do componente. Se passado nulo ou vazio, será utilizado o atributo passado em captionAttribute.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return
   * @throws RFWException
   */
  public HasValue<?> createMOField(String propertyPath, String minWidth, String maxWidth, String captionAttribute, LinkedList<String> filterAttributes, RFWDBProvider dbProvider) throws RFWException {
    return createMOFieldImp(propertyPath, minWidth, maxWidth, null, captionAttribute, filterAttributes, null, null, dbProvider);
  }

  /**
   * Este método cria um campo para ser usado como campo de busca. Baseia-se no tipo de dado presente no VO para criar o campo, mas o registra como campo de busca. Assim ele será utilizado para popular o MO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param minWidth Tamanho mínimo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço mínimo
   *          cedido será de 3 colunas =~ 300px.
   * @param maxWidth Tamanho máximo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço máximo
   *          cedido será de 3 colunas =~ 300px.
   * @param captionAttribute Usado quando o componente criado refere-se à um {@link RFWMetaRelationshipField} e o objeto em questão é um RFWVO. Aqui devemos passar qual o atributo do VO será utilizado como Caption no ComboBox Criado.<br>
   *          <b>ATENÇÃO:</B> Note que o atributo passado aqui é em relação ao objeto que estará no Provider do ComboBox e não do objeto sobre o qual estamos criando os campos.<br>
   *          Por exemplo, o ItemVO tem um relaiconamento com o ItemTypeVO e utiliza este método paracriar o campo. O valor passado aqui deve ser o 'name' ou ItemTypeVO_.vo().name(), e não o caminho a partir do ItemVO, utilizado para gerar o campo.
   * @param filterAttributes Lista de atributos (ou atributo único) que serão utilizados para realizar o filtro do componente. Se passado nulo ou vazio, será utilizado o atributo passado em captionAttribute.
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @return
   * @throws RFWException
   */
  public <FIELDTYPE, BEANTYPE> HasValue<?> createMOField(String propertyPath, String minWidth, String maxWidth, String captionAttribute, LinkedList<String> filterAttributes, RFWDataFormatter<FIELDTYPE, BEANTYPE> df) throws RFWException {
    return createMOFieldImp(propertyPath, minWidth, maxWidth, null, captionAttribute, filterAttributes, null, df, null);
  }

  /**
   * Este método cria um campo para ser usado como campo de busca. Baseia-se no tipo de dado presente no VO para criar o campo, mas o registra como campo de busca. Assim ele será utilizado para popular o MO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param minWidth Tamanho mínimo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço mínimo
   *          cedido será de 3 colunas =~ 300px.
   * @param maxWidth Tamanho máximo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço máximo
   *          cedido será de 3 colunas =~ 300px.
   * @param fieldAlignment Alinhamendo do conteúdo co campo. Suportado até o momento apenas em TextFields.
   * @param captionAttribute Usado quando o componente criado refere-se à um {@link RFWMetaRelationshipField} e o objeto em questão é um RFWVO. Aqui devemos passar qual o atributo do VO será utilizado como Caption no ComboBox Criado.<br>
   *          <b>ATENÇÃO:</B> Note que o atributo passado aqui é em relação ao objeto que estará no Provider do ComboBox e não do objeto sobre o qual estamos criando os campos.<br>
   *          Por exemplo, o ItemVO tem um relaiconamento com o ItemTypeVO e utiliza este método paracriar o campo. O valor passado aqui deve ser o 'name' ou ItemTypeVO_.vo().name(), e não o caminho a partir do ItemVO, utilizado para gerar o campo.
   * @param filterAttributes Lista de atributos (ou atributo único) que serão utilizados para realizar o filtro do componente. Se passado nulo ou vazio, será utilizado o atributo passado em captionAttribute.
   * @param resolutionOverride Quando passado, nos campos da date/periodo sobrepõe a precisão de data/hora definido no RFWMeta para o campo.
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   *
   * @return
   * @throws RFWException
   */
  @SuppressWarnings("unchecked")
  private <FIELDTYPE, BEANTYPE> HasValue<?> createMOFieldImp(String propertyPath, String minWidth, String maxWidth, FieldAlignment fieldAlignment, String captionAttribute, LinkedList<String> filterAttributes, DateTimeResolution resolutionOverride, RFWDataFormatter<FIELDTYPE, BEANTYPE> df, RFWDBProvider dbProvider) throws RFWException {
    final Boolean forceRequired = Boolean.FALSE; // Nunca queremos um campo de filtro obrigatório.
    final Boolean ignoreTextArea = Boolean.TRUE; // Nunca queremos um TextArea nos campos de Filtro

    final HasValue<Object> c = UIFactory.createField(this.voClass, propertyPath, null, forceRequired, fieldAlignment, forceRequired, captionAttribute, filterAttributes, resolutionOverride, ignoreTextArea, df, dbProvider);

    // Verificamos se tem um DataFormatter Padrão para a propriedade
    if (df == null) df = getDataFormatterByRFWMetaAnnotation(this.voClass, propertyPath);
    if (df != null) applyRFWDataFormatter(c, (RFWDataFormatter<Object, Object>) df);

    this.moFieldHash.put(propertyPath, new FieldMetaData(c, propertyPath, minWidth, maxWidth, (RFWDataFormatter<Object, Object>) df, null));
    return c;
  }

  /**
   * Este método aplica o RFWDataFormatter em um Campo como um "reformatador".<br>
   * Isto é, toda vez que o campo perder o foco ele pega o valor do campo, valida e reformata o valor. Em seguida coloca o valor retornado pelo {@link RFWDataFormatter} novamente no campo. Garantindo assim a aprência do dado.<br>
   * <br>
   * <b>Note que para funcionar o component passado deve implementar a interface {@link BlurNotifier}.</b> <br>
   * <br>
   * Não utilize este método junto com o método de Bind, ou se o campo for criado para VO ou para MO, todos esses métodos aplicam essa funcionalidade internamente.
   *
   * @param component Componente que terá seu valor reformatado quando perder o foco.
   * @param df Formatador responsável pela reformatação do valor.
   * @throws RFWException
   */
  public static <FIELDTYPE, BEANTYPE> void applyRFWDataFormatter(HasValue<FIELDTYPE> component, RFWDataFormatter<FIELDTYPE, BEANTYPE> df) throws RFWException {
    // Verificamos se o field emite o evento de onBlur
    if (component instanceof BlurNotifier) {
      // Se temos um Dataformater e o evento onBlur, criamos o "reformater" para o campo do MO, que faz com que ao sair do campo o conteúdo seja reformatado para ficar no padrão visual adequado. e não do jeito que o usuário deixou.
      // Note que diferentemente do Bind que temos a validação no onChange(). Aqui teremos apenas quando perder o foco. Por isso a definição do erro de validação etá feita no onBlur().
      ((BlurNotifier) component).addBlurListener(e -> {
        try {
          try {
            FWVad.setComponentError(component, null);
            final FIELDTYPE value = df.reformatPresentationContent(component.getValue(), RFWUI.getLocaleCurrent());
            component.setValue(value);
          } catch (RFWValidationException e1) {
            FWVad.setComponentError(component, RFWBundle.get(e1));
          }
        } catch (Throwable e1) {
          TreatException.treat(e1);
        }
      });
    }
  }

  /**
   * Cria um campo utlizado nos filtros de MO de forma customizada. Permite que o usuário passe um componente pronto, com as características desejadas. E toda vez que o UIFactory precisar que o conteúdo do campo seja incluso no RFWMO para realizar o filtro, o listener MOBuilderLieter será chamado para que o usuário faça essa inclusão de clausula no RFWMO.
   *
   * @param propertyPathOrID Nome da propriedade ou qualquer outro identificador String para servir de chave e identificar o campo registrado.
   * @param component Comnente/Campo a ser exibido na tela.
   * @param moBuilder Lister que será chamado quando o UIFactory precisar do valor desse campo no RFWMO.
   * @param minWidth Tamanho mínimo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço mínimo
   *          cedido será de 3 colunas =~ 300px.
   * @param maxWidth Tamanho máximo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço máximo
   *          cedido será de 3 colunas =~ 300px.
   * @throws RFWException
   */
  public void createMOField_Custom(String propertyPathOrID, HasValue<?> component, MOBuilderListener moBuilder, String minWidth, String maxWidth) throws RFWException {
    this.moFieldHash.put(propertyPathOrID, new FieldMetaData(component, propertyPathOrID, minWidth, maxWidth, null, moBuilder));
  }

  /**
   * Cria um componente de Data Range para ser utilizado no bloco de filtros do MO.
   *
   * @param propertyPath Propriedade do tipo LocalDate ou LocalDateTime para que o campo seja associado.
   * @return Componente criado.
   * @throws RFWException Lançado em caso de erro
   */

  public <FIELDTYPE, BEANTYPE> RFWDateRangeComponent createMOField_DateRange(String propertyPath) throws RFWException {
    return createMOField_DateRange(propertyPath, null);
  }

  /**
   * Cria um componente de Data Range para ser utilizado no bloco de filtros do MO.
   *
   * @param propertyPath Propriedade do tipo LocalDate ou LocalDateTime para que o campo seja associado.
   * @param resolutionOverride Define a resolução do campo, indicando se ele terá precisão de dias, horas, minutos, segundos, etc.
   * @param scope Define as sugestões de preenchimento automático do campo. Se ele dará sugestões de datas passadas, futuras ou ambas.
   * @return Componente criado.
   * @throws RFWException Lançado em caso de erro
   */
  public <FIELDTYPE, BEANTYPE> RFWDateRangeComponent createMOField_DateRange(String propertyPath, DateTimeResolution resolutionOverride) throws RFWException {
    return createMOField_DateRange(propertyPath, resolutionOverride, RFWDateRangeScope.ALL);
  }

  /**
   * Cria um componente de Data Range para ser utilizado no bloco de filtros do MO.
   *
   * @param propertyPath Propriedade do tipo LocalDate ou LocalDateTime para que o campo seja associado.
   * @param resolutionOverride Define a resolução do campo, indicando se ele terá precisão de dias, horas, minutos, segundos, etc.
   * @param scope Define as sugestões de preenchimento automático do campo. Se ele dará sugestões de datas passadas, futuras ou ambas.
   * @return Componente criado.
   * @throws RFWException Lançado em caso de erro
   */
  public <FIELDTYPE, BEANTYPE> RFWDateRangeComponent createMOField_DateRange(String propertyPath, DateTimeResolution resolutionOverride, RFWDateRangeScope scope) throws RFWException {
    DateTimeResolution resolution = resolutionOverride;
    if (resolution == null) {
      final RFWMetaDateField ann = (RFWMetaDateField) RUReflex.getRFWMetaAnnotation(voClass, propertyPath);
      switch (ann.resolution()) {
        case YEAR:
          resolution = DateTimeResolution.YEAR;
          break;
        case MONTH:
          resolution = DateTimeResolution.MONTH;
          break;
        case DAY:
          resolution = DateTimeResolution.DAY;
          break;
        case HOUR:
          resolution = DateTimeResolution.HOUR;
          break;
        case MINUTE:
          resolution = DateTimeResolution.MINUTE;
          break;
        case SECOND:
        case MILLISECONDS:
          resolution = DateTimeResolution.SECOND;
          break;
      }
    }

    // Cria o primeiro campo que será "menor ou igual"
    RFWDateRangeComponent rangeComp = new RFWDateRangeComponent(RUReflex.getRFWMetaAnnotationCaption(voClass, propertyPath), resolution, scope);
    this.moFieldHash.put(propertyPath, new FieldMetaData(rangeComp, propertyPath, "440px", "440px", null, null));
    return rangeComp;
  }

  /**
   * Cria um componente montado com um RFWAssociativeComponent com uma coeção de RFWButtonToggle para ligar/desligar os itens que queremos no filtro.
   *
   * @param propertyPath Propriedade do VO que será utilizada para o filtro, deve ser do tipo Enumeration.
   * @param minWidth Tamanho mínimo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço mínimo
   *          cedido será de 3 colunas =~ 300px.
   * @param maxWidth Tamanho máximo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço máximo
   *          cedido será de 3 colunas =~ 300px.
   * @return Componente criado.
   * @throws RFWException Lançado em caso de erro
   */
  @SuppressWarnings("unchecked")
  public <ENUM extends Enum<?>> RFWAssociativeComponent createMOField_ToggleEnumeration(String propertyPath, IconProvider<ENUM> iconProvider, String minWidth, String maxWidth) throws RFWException {
    final Class<ENUM> enumType = (Class<ENUM>) RUReflex.getPropertyTypeByType(voClass, propertyPath);
    final ENUM[] values = enumType.getEnumConstants();

    RFWAssociativeComponent ass = new RFWAssociativeComponent(RUReflex.getRFWMetaAnnotationCaption(voClass, propertyPath) + ":");
    ass.setSizeUndefined();
    ass.getLayout().setSizeUndefined();
    for (ENUM value : values) {
      final RFWButtonToggle<ENUM> tgButton = FWVad.createButtonToggle(null, null, iconProvider.getIconPath(24, value), null);
      tgButton.setDescription(RFWBundle.get(value));
      tgButton.setSelectedValue(value);
      ass.addComponent(tgButton);
    }

    this.moFieldHash.put(propertyPath, new FieldMetaData(ass, propertyPath, minWidth, maxWidth, null, null));

    return ass;
  }

  /**
   * Cria um ComboBoxMultiselect para ser utilizado em campos de Lista para o MO.
   *
   * @param propertyPath Propriedade do VO que será utilizada para o filtro, deve ser do tipo Enumeration.
   * @param minWidth Tamanho mínimo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço mínimo
   *          cedido será de 3 colunas =~ 300px.
   * @param maxWidth Tamanho máximo que o componente precisa. Caso não caiba na tela ele será jogado para o campo de "mais filtros".<br>
   *          O espaço é dividido em um layoutGrid com larguras de no mínimo 100px. Para o Vaadin o componente terá sempre a largura "100%" para ocupar todo o espaço dado dentro do grid. As medidas aqui são utilizadas para calcular quantas colunas cada componente usará. Medidas em percentual são transformadas de acordo com a medida da largura da tela. Caso o valor passado seja nulo o espaço máximo
   *          cedido será de 3 colunas =~ 300px.
   * @return Componente criado.
   * @throws RFWException Lançado em caso de erro
   */
  public <ENUM extends Enum<?>> ComboBoxMultiselect<Enum<?>> createMOField_ComboBoxMultiselect(String propertyPath, String minWidth, String maxWidth) throws RFWException {
    ComboBoxMultiselect<Enum<?>> field = createField_ComboBoxMultiselect_Enum(voClass, propertyPath, null, false, null);
    field.setWidth("100%");
    this.moFieldHash.put(propertyPath, new FieldMetaData(field, propertyPath, minWidth, maxWidth, null, null));
    return field;
  }

  /**
   * Este método gera um RFWMO a partir das informações preenchidas nos campos de busca criados pelo UIFactory.
   *
   * @return RFWMO montado com as informações, prontas para serem consultadas no banco de dados.
   * @throws RFWException Em caso de algum problema durante a geração do RFWMO.
   */
  @SuppressWarnings("unchecked")
  public RFWMO createRFWMO() throws RFWException {
    final RFWMO mo;
    if (this.baseRFWMO != null) {
      mo = this.baseRFWMO.cloneRecursive();
    } else {
      mo = new RFWMO();
    }

    for (FieldMetaData fieldData : this.moFieldHash.values()) {
      if (fieldData.moBuilderListener != null) {
        fieldData.moBuilderListener.build(mo, fieldData.attributePath, fieldData.component);
      } else {
        Annotation ann = RUReflex.getRFWMetaAnnotation(this.voClass, fieldData.attributePath);
        if (ann == null) throw new RFWCriticalException("Impossível gerar um campo para o atributo '${0}' da classe '${1}'. O atributo não tem uma RFWMeta Annotation!", new String[] { fieldData.attributePath, this.voClass.getCanonicalName() });

        Object value = null;
        try {
          value = getMOFieldValue(fieldData.attributePath);
        } catch (RFWException e) {
          if ("BIS10_000013".equals(e.getExceptionCode())) {
            // Aceita a exception e deixa o valor continuar como nulo, provavelmente o componente será tratado melhor abaixo.
          } else {
            throw e;
          }
        }

        if (RFWMetaStringField.class.isAssignableFrom(ann.getClass())) {
          // Campos de texto nós fazemos a operação 'LIKE' por padrão.
          if (value != null && !"".equals(value)) mo.like(fieldData.attributePath, '%' + value.toString() + '%');
        } else if (RFWMetaStringCEPField.class.isAssignableFrom(ann.getClass())) {
          // Campos de cep só buscamos os exatos
          if (value != null && !"".equals(value)) mo.equal(fieldData.attributePath, RFWCEPDataFormatter.getInstance().toVO((String) value, RFWUI.getLocaleCurrent()));
        } else if (RFWMetaStringCNPJField.class.isAssignableFrom(ann.getClass())) {
          // Campos de CNPJ só buscamos os exatos
          if (value != null && !"".equals(value)) mo.equal(fieldData.attributePath, RFWCEPDataFormatter.getInstance().toVO((String) value, RFWUI.getLocaleCurrent()));
        } else if (RFWMetaStringCPFOrCNPJField.class.isAssignableFrom(ann.getClass())) {
          // Campos de CPF/CNPJ só buscamos os exatos
          if (value != null && !"".equals(value)) mo.equal(fieldData.attributePath, RFWCEPDataFormatter.getInstance().toVO((String) value, RFWUI.getLocaleCurrent()));
        } else if (RFWMetaStringEmailField.class.isAssignableFrom(ann.getClass())) {
          // O capmo do e-mail buscamos por termos, caso a pessoa queira digitar só uma parte do e-mail
          value = fieldData.df.toVO(value, RFWUI.getLocaleCurrent());
          if (value != null && !"".equals(value)) mo.like(fieldData.attributePath, '%' + RFWEmailDataFormatter.getInstance().toVO((String) value, RFWUI.getLocaleCurrent()) + '%');
        } else if (RFWMetaBigDecimalCurrencyField.class.isAssignableFrom(ann.getClass())) {
          BigDecimal bigValue = (BigDecimal) fieldData.df.toVO(value, RFWUI.getLocaleCurrent());
          if (bigValue != null) mo.equal(fieldData.attributePath, bigValue);
        } else if (RFWMetaBigDecimalPercentageField.class.isAssignableFrom(ann.getClass())) {
          BigDecimal bigValue = (BigDecimal) fieldData.df.toVO(value, RFWUI.getLocaleCurrent());
          if (bigValue != null) mo.equal(fieldData.attributePath, bigValue);
        } else if (RFWMetaIntegerField.class.isAssignableFrom(ann.getClass())) {
          Integer intValue = (Integer) fieldData.df.toVO(value, RFWUI.getLocaleCurrent());
          if (intValue != null) mo.equal(fieldData.attributePath, intValue);
        } else if (RFWMetaLongField.class.isAssignableFrom(ann.getClass())) {
          Long longValue = null;
          if (value != null) longValue = (Long) fieldData.df.toVO(value.toString(), RFWUI.getLocaleCurrent());
          if (longValue != null) mo.equal(fieldData.attributePath, longValue);
        } else if (RFWMetaEnumField.class.isAssignableFrom(ann.getClass())) {
          // Enumeração pode ser feita de duas maneiras, ComboBox (Criação padrão do UIFactory), ou como um RFWAssociativeComponent2 de RFWButtonToggle
          if (fieldData.component instanceof ComboBox) {
            if (value != null) mo.equal(fieldData.attributePath, value);
          } else if (fieldData.component instanceof RFWAssociativeComponent) {
            // Iteramos todos os componsntes do RFWAssociaciteComponente para lêr os valores e saber quais os valores que foram clicados e adicionar no MO
            RFWAssociativeComponent ass = (RFWAssociativeComponent) fieldData.component;
            RFWMO assMO = null;
            for (Component comp : ass.getLayout()) {
              final RFWButtonToggle<Object> tgbt = (RFWButtonToggle<Object>) comp;
              if (tgbt.isSelected()) {
                if (assMO == null) assMO = new RFWMO(AppendMethod.OR);
                assMO.equal(fieldData.attributePath, tgbt.getValue());
              }
            }
            if (assMO != null) {
              mo.getSubmo().add(assMO);
            }
          } else if (fieldData.component instanceof ComboBoxMultiselect<?>) {
            if (value != null && ((Set<?>) value).size() > 0) mo.in(fieldData.attributePath, (Set<?>) value);
          } else {
            throw new RFWCriticalException("Componente desconhecido para o RFWMetaEnumField: '" + fieldData.component.getClass().getCanonicalName());
          }
        } else if (RFWMetaDateField.class.isAssignableFrom(ann.getClass())) {
          if (fieldData.component instanceof RFWDateRangeComponent) {
            RFWDateRangeComponent rangeComp = (RFWDateRangeComponent) fieldData.component;
            // Data de Inicío do Período
            LocalDateTime startDate = rangeComp.getValue();
            if (startDate != null) {
              // Verificamos o resolution do campo para saber como proceder com o resto da precisão do valor do início do período. No início do período jogamos tudo para zero, para que o valor selecionado seja inclusivo.
              switch (rangeComp.getStartDateTime().getResolution()) {
                case YEAR:
                  startDate = startDate.withMonth(1);
                case MONTH:
                  startDate = startDate.withDayOfMonth(1);
                case DAY:
                  startDate = startDate.withHour(0);
                case HOUR:
                  startDate = startDate.withMinute(0);
                case MINUTE:
                  startDate = startDate.withNano(0);
                case SECOND:
                  break;
              }
              mo.greaterThanOrEqualTo(fieldData.attributePath, startDate);
            }
            // Data do fim do Período
            LocalDateTime endDate = rangeComp.getValueFinal();
            if (endDate != null) {
              // Verificamos o resolution do campo para saber como proceder com o resto da precisão do valor do final do período. No final do período jogamos tudo para o máximo, para que o valor selecionado seja inclusivo.
              switch (rangeComp.getEndDateTime().getResolution()) {
                case YEAR:
                  endDate = endDate.withMonth(12);
                case MONTH:
                  endDate = endDate.withDayOfMonth(RUDateTime.getLastDayOfMonth(endDate.toLocalDate()));
                case DAY:
                  endDate = endDate.withHour(23);
                case HOUR:
                  endDate = endDate.withMinute(59);
                case MINUTE:
                  endDate = endDate.withNano(999999999);
                case SECOND:
                  break;
              }
              mo.lessThanOrEqualTo(fieldData.attributePath, endDate);
            }
          } else {
            throw new RFWCriticalException("A RFWMetaDateField não suporta o componente utilizado na geração do MO!", new String[] { fieldData.attributePath, this.voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
          }
        } else if (RFWMetaBooleanField.class.isAssignableFrom(ann.getClass())) {
          if (fieldData.component instanceof RadioButtonGroup) {
            if (value != null) {
              mo.equal(fieldData.attributePath, value);
            }
          } else if (fieldData.component instanceof CheckBox) {
            if (value != null) {
              mo.equal(fieldData.attributePath, value);
            }
          } else if (fieldData.component instanceof CheckBoxGroup) {
            if (value != null) {
              if (((Set<?>) value).size() == 1) {
                // se só temos 1 utilizamos o equals que é melhor pro sql
                mo.equal(fieldData.attributePath, ((Set<?>) value).iterator().next());
              } else if (((Set<?>) value).size() > 1) {
                // se for maior que 1, utilizamos o IN para incluir todas as igualdades
                mo.in(fieldData.attributePath, (Set<?>) value);
              }
            }
          } else {
            throw new RFWCriticalException("Método despreparado para Criar o RFWMO a com um campo do tipo '" + fieldData.component.getClass().getCanonicalName() + "'.");
          }
        } else if (RFWMetaRelationshipField.class.isAssignableFrom(ann.getClass())) {
          // Verificamos se o Tipo é um RFWVO
          final Class<?> type = RUReflex.getPropertyTypeByType(this.voClass, fieldData.attributePath);
          if (RFWVO.class.isAssignableFrom(type)) {
            final RFWVO rfwVO = (RFWVO) value;
            if (rfwVO != null) mo.equal(fieldData.attributePath + ".id", rfwVO.getId());
          } else {
            throw new RFWCriticalException("Tipo de Dados '${3}' não suportado pela RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { fieldData.attributePath, this.voClass.getCanonicalName(), ann.annotationType().getSimpleName(), type.getCanonicalName() });
          }
        } else if (RFWMetaCollectionField.class.isAssignableFrom(ann.getClass())) {
          final Class<?> type = RUReflex.getPropertyTypeByType(this.voClass, fieldData.attributePath);
          if (List.class.isAssignableFrom(type)) {
            if (fieldData.component instanceof RFWTagsSelector) {
              List<?> valList = (List<?>) value;
              if (valList != null && valList.size() > 0) mo.in(fieldData.attributePath, valList);
            } else if (fieldData.component instanceof ComboBoxMultiselect) {
              Set<?> valSet = (Set<?>) value;
              if (valSet != null && valSet.size() > 0) mo.in(fieldData.attributePath, valSet);
            } else {
              throw new RFWCriticalException("Método despreparado para Criar o RFWMO a com um campo do tipo '" + fieldData.component.getClass().getCanonicalName() + "'.");
            }
          } else {
            throw new RFWCriticalException("Tipo de Dados '${3}' não suportado pela RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { fieldData.attributePath, this.voClass.getCanonicalName(), ann.annotationType().getSimpleName(), type.getCanonicalName() });
          }
        } else {
          throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { fieldData.attributePath, this.voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
        }
      }
    }
    return mo;

  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Component createVOField(String propertyPath) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, null, null, false, null, null, null, null, null, null, null);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Component createVOField(String propertyPath, RFWDBProvider dbProvider) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, null, null, false, null, null, null, null, null, null, dbProvider);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param forceRequired Indica se devemos sobrepor a definição de obrigatório presente no VO. True indica que é obrigatório, False indica que não é obrigatório, null utiliza a definição do RFWMeta existente no VO.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Component createVOField(String propertyPath, Boolean forceRequired) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, null, null, false, null, null, null, null, null, forceRequired, null);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param resolutionOverride Quando passado, nos campos da date/periodo sobrepõe a precisão de data/hora definido no RFWMeta para o campo.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Component createVOField(String propertyPath, DateTimeResolution resolutionOverride) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, null, null, null, null, null, resolutionOverride, null, null, null, null);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public <FIELDTYPE, BEANTYPE> Component createVOField(String propertyPath, RFWDataFormatter<FIELDTYPE, BEANTYPE> df) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, null, null, null, null, null, null, null, df, null, null);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param fieldAlignment Alinhamendo do conteúdo co campo. Suportado até o momento apenas em TextFields.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public <FIELDTYPE, BEANTYPE> Component createVOField(String propertyPath, FieldAlignment fieldAlignment) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, null, fieldAlignment, null, null, null, null, null, null, null, null);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param fieldAlignment Alinhamendo do conteúdo co campo. Suportado até o momento apenas em TextFields.
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public <FIELDTYPE, BEANTYPE> Component createVOField(String propertyPath, FieldAlignment fieldAlignment, RFWDataFormatter<FIELDTYPE, BEANTYPE> df) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, null, fieldAlignment, null, null, null, null, null, df, null, null);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Component createVOField(String propertyPath, String helpPopupKey) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, helpPopupKey, null, null, null, null, null, null, null, null, null);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @param fieldAlignment Alinhamendo do conteúdo co campo. Suportado até o momento apenas em TextFields.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Component createVOField(String propertyPath, String helpPopupKey, FieldAlignment fieldAlignment) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, helpPopupKey, fieldAlignment, null, null, null, null, null, null, null, null);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @param fieldAlignment Alinhamendo do conteúdo co campo. Suportado até o momento apenas em TextFields.
   * @param masked Utilizável quando é um campo de Texto, {@link RFWMetaStringField}, cria um campo de senha, cujo conteúdo fica oculto (PasswordField)
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Component createVOField(String propertyPath, String helpPopupKey, FieldAlignment fieldAlignment, boolean masked) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, helpPopupKey, fieldAlignment, masked, null, null, null, null, null, null, null);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @param captionAttribute Usado quando o componente criado refere-se à um {@link RFWMetaRelationshipField} e o objeto em questão é um RFWVO. Aqui devemos passar qual o atributo do VO será utilizado como Caption no ComboBox Criado.<br>
   *          <b>ATENÇÃO:</B> Note que o atributo passado aqui é em relação ao objeto que estará no Provider do ComboBox e não do objeto sobre o qual estamos criando os campos.<br>
   *          Por exemplo, o ItemVO tem um relaiconamento com o ItemTypeVO e utiliza este método paracriar o campo. O valor passado aqui deve ser o 'name' ou ItemTypeVO_.vo().name(), e não o caminho a partir do ItemVO, utilizado para gerar o campo.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Component createVOField(String propertyPath, String helpPopupKey, String captionAttribute) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, helpPopupKey, null, null, captionAttribute, null, null, null, null, null, null);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @param captionAttribute Usado quando o componente criado refere-se à um {@link RFWMetaRelationshipField} e o objeto em questão é um RFWVO. Aqui devemos passar qual o atributo do VO será utilizado como Caption no ComboBox Criado.<br>
   *          <b>ATENÇÃO:</B> Note que o atributo passado aqui é em relação ao objeto que estará no Provider do ComboBox e não do objeto sobre o qual estamos criando os campos.<br>
   *          Por exemplo, o ItemVO tem um relaiconamento com o ItemTypeVO e utiliza este método paracriar o campo. O valor passado aqui deve ser o 'name' ou ItemTypeVO_.vo().name(), e não o caminho a partir do ItemVO, utilizado para gerar o campo.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Component createVOField(String propertyPath, String helpPopupKey, String captionAttribute, RFWDBProvider dbProvider) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, helpPopupKey, null, null, captionAttribute, null, null, null, null, null, dbProvider);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @param captionAttribute Usado quando o componente criado refere-se à um {@link RFWMetaRelationshipField} e o objeto em questão é um RFWVO. Aqui devemos passar qual o atributo do VO será utilizado como Caption no ComboBox Criado.<br>
   *          <b>ATENÇÃO:</B> Note que o atributo passado aqui é em relação ao objeto que estará no Provider do ComboBox e não do objeto sobre o qual estamos criando os campos.<br>
   *          Por exemplo, o ItemVO tem um relaiconamento com o ItemTypeVO e utiliza este método paracriar o campo. O valor passado aqui deve ser o 'name' ou ItemTypeVO_.vo().name(), e não o caminho a partir do ItemVO, utilizado para gerar o campo.
   * @param filterAttributes Lista de atributos (ou atributo único) que serão utilizados para realizar o filtro do componente. Se passado nulo ou vazio, será utilizado o atributo passado em captionAttribute.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Component createVOField(String propertyPath, String helpPopupKey, String captionAttribute, List<String> filterAttributes) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, helpPopupKey, null, null, captionAttribute, filterAttributes, null, null, null, null, null);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @param captionAttribute Usado quando o componente criado refere-se à um {@link RFWMetaRelationshipField} e o objeto em questão é um RFWVO. Aqui devemos passar qual o atributo do VO será utilizado como Caption no ComboBox Criado.<br>
   *          <b>ATENÇÃO:</B> Note que o atributo passado aqui é em relação ao objeto que estará no Provider do ComboBox e não do objeto sobre o qual estamos criando os campos.<br>
   *          Por exemplo, o ItemVO tem um relaiconamento com o ItemTypeVO e utiliza este método paracriar o campo. O valor passado aqui deve ser o 'name' ou ItemTypeVO_.vo().name(), e não o caminho a partir do ItemVO, utilizado para gerar o campo.
   * @param filterAttributes Lista de atributos (ou atributo único) que serão utilizados para realizar o filtro do componente. Se passado nulo ou vazio, será utilizado o atributo passado em captionAttribute.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Component createVOField(String propertyPath, String helpPopupKey, String captionAttribute, List<String> filterAttributes, RFWDBProvider dbProvider) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, helpPopupKey, null, null, captionAttribute, filterAttributes, null, null, null, null, dbProvider);
  }

  /**
   * Este método cria um campo padrão conforme o RFWMetaAnnotation o tipo do atributo do VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @param captionAttribute Usado quando o componente criado refere-se à um {@link RFWMetaRelationshipField} e o objeto em questão é um RFWVO. Aqui devemos passar qual o atributo do VO será utilizado como Caption no ComboBox Criado.<br>
   *          <b>ATENÇÃO:</B> Note que o atributo passado aqui é em relação ao objeto que estará no Provider do ComboBox e não do objeto sobre o qual estamos criando os campos.<br>
   *          Por exemplo, o ItemVO tem um relaiconamento com o ItemTypeVO e utiliza este método paracriar o campo. O valor passado aqui deve ser o 'name' ou ItemTypeVO_.vo().name(), e não o caminho a partir do ItemVO, utilizado para gerar o campo.
   * @param filterAttributes Lista de atributos (ou atributo único) que serão utilizados para realizar o filtro do componente. Se passado nulo ou vazio, será utilizado o atributo passado em captionAttribute.
   * @param forceRequired Indica se devemos sobrepor a definição de obrigatório presente no VO. True indica que é obrigatório, False indica que não é obrigatório, null utiliza a definição do RFWMeta existente no VO.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Component createVOField(String propertyPath, String helpPopupKey, String captionAttribute, List<String> filterAttributes, Boolean forceRequired) throws RFWException {
    return (Component) createVOFieldImp(propertyPath, helpPopupKey, null, null, captionAttribute, filterAttributes, null, null, null, forceRequired, null);
  }

  /**
   * Este método cria um campo para ser usado como campo de edição de um VO.<br>
   * O campo é criado pelo método {@link #createField(String, RFWVO, String, boolean, FieldAlignment, Boolean, Boolean, String)} e registra como um campo do VO e realiza o "BIND" entre o campo e o VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @param fieldAlignment Alinhamendo do conteúdo co campo. Suportado até o momento apenas em TextFields.
   * @param masked Utilizável quando é um campo de Texto, {@link RFWMetaStringField}, cria um campo de senha, cujo conteúdo fica oculto (PasswordField)
   * @param captionAttribute Usado quando o componente criado refere-se à um {@link RFWMetaRelationshipField} e o objeto em questão é um RFWVO. Aqui devemos passar qual o atributo do VO será utilizado como Caption no ComboBox Criado.<br>
   *          <b>ATENÇÃO:</B> Note que o atributo passado aqui é em relação ao objeto que estará no Provider do ComboBox e não do objeto sobre o qual estamos criando os campos.<br>
   *          Por exemplo, o ItemVO tem um relaiconamento com o ItemTypeVO e utiliza este método paracriar o campo. O valor passado aqui deve ser o 'name' ou ItemTypeVO_.vo().name(), e não o caminho a partir do ItemVO, utilizado para gerar o campo.
   * @param filterAttributes Lista de atributos (ou atributo único) que serão utilizados para realizar o filtro do componente. Se passado nulo ou vazio, será utilizado o atributo passado em captionAttribute.
   * @param resolutionOverride Quando passado, nos campos da date/periodo sobrepõe a precisão de data/hora definido no RFWMeta para o campo.
   * @param ignoreTextArea Quando o atributo é do tipo String e o tamanho máximo passa dos 1000 caracteres, o RFW cria por padrão um TextArea ao invés de um TextField. Quando esse atributo for true, o método continuará criando um TextField. Útil quando queremos criar um campo de filtro para o atributo.
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @param forceRequired Indica se devemos sobrepor a definição de obrigatório presente no VO. True indica que é obrigatório, False indica que não é obrigatório, null utiliza a definição do RFWMeta existente no VO.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return
   * @throws RFWException
   */
  @SuppressWarnings("unchecked")
  private <FIELDTYPE, BEANTYPE> HasValue<?> createVOFieldImp(String propertyPath, String helpPopupKey, FieldAlignment fieldAlignment, Boolean masked, String captionAttribute, List<String> filterAttributes, DateTimeResolution resolutionOverride, Boolean ignoreTextArea, RFWDataFormatter<FIELDTYPE, BEANTYPE> df, Boolean forceRequired, RFWDBProvider dbProvider) throws RFWException {
    Objects.requireNonNull(this.vo, "Impossível criar o campo sem que o VO esteja definido no UIFactory!");
    // Criamos o campo com base na informação do VO
    HasValue<FIELDTYPE> c = (HasValue<FIELDTYPE>) UIFactory.createField(vo.getClass(), propertyPath, helpPopupKey, forceRequired, fieldAlignment, masked, captionAttribute, filterAttributes, resolutionOverride, ignoreTextArea, df, dbProvider);
    // Realizamos o Bind no VO
    final UIBinder<FIELDTYPE, BEANTYPE, VO> uiBinder = UIFactory.bindVO(c, this.voClass, this.vo, propertyPath, df, forceRequired);
    // Registra o field na Hash de Fields de VOs do UIFactory
    this.createVOField_Custom(propertyPath, c);
    // Registra o Binder na hash
    this.bindingMap.put(propertyPath, uiBinder);
    return c;
  }

  /**
   * Este método registra um campo criado externamente para ser utilizado com o UIFactory.<Br>
   * Chamar este método tem a mesma função que registrar o componente no UIFactory com o {@link #createVOField_Custom(String, HasValue)} e realizar o bind com algum dos métodos de Bind no VO que está em edição no UIFactory.<Br>
   *
   * @param <FIELDTYPE> Classe do Atributo do VO que o campo está relacionado.
   * @param attribute caminho do atributo que será associado e editado com este campo.
   * @param field Campo para edição do atributo.
   * @param formatter Formatador para apresentação dos valores do campo. Muitas vezes não obrigatório e pode ser passado nulo.
   * @param required Indica se este campo deve ser obrigatório para a validação de preenchimento (e exibição como obrigatório, se suportado pelo campo).
   * @param nullValue Valor que será utilizado para exibição de nulo. Por exemplo, para Strings o valor "" pode ser considerado como nulo.
   * @throws RFWException
   */
  public <FIELDTYPE> void registerField(String attribute, HasValue<FIELDTYPE> field, RFWDataFormatter<FIELDTYPE, VO> formatter, boolean required, FIELDTYPE nullValue) throws RFWException {
    this.voFieldHash.put(attribute, field);
    bind(field, attribute, formatter, required, nullValue);
  }

  /**
   * Este método cria um campo para ser usado como campo de edição de um VO.<br>
   * Força a criação de um ComboBox para valores Booleanos do VO. Útil quando temos só duas opções (representadas por um Boolean no VO), mas queremos exibir como uma caixa de seleção de opções para o usuário.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo. Deve ser Boolean obrigatoriamente e conter a {@link RFWMetaBooleanField}
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @param forceRequired Indica se devemos sobrepor a definição de obrigatório presente no VO. True indica que é obrigatório, False indica que não é obrigatório, null utiliza a definição do RFWMeta existente no VO.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public ComboBox<Boolean> createVOField_ComboBox_Boolean(String propertyPath, String helpPopupKey, Boolean forceRequired) throws RFWException {
    Objects.requireNonNull(this.vo, "Impossível criar o campo sem que o VO esteja definido no UIFactory!");

    Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, propertyPath);
    if (ann == null || !(ann instanceof RFWMetaBooleanField)) {
      throw new RFWCriticalException("O atributo '${0}' da classe '${1}' não contém uma RFWMetaBooleanField!", new String[] { propertyPath, voClass.getCanonicalName() });
    }

    // Criamos o campo com base na informação do VO
    ComboBox<Boolean> c = UIFactory.createField_ComboBox_Boolean(vo.getClass(), propertyPath, helpPopupKey, forceRequired);
    // Realizamos o Bind no VO
    final UIBinder<Boolean, Boolean, VO> uiBinder = UIFactory.bindVO(c, this.voClass, this.vo, propertyPath, null, null);
    // Registra o field na Hash de Fields de VOs do UIFactory
    this.createVOField_Custom(propertyPath, c);
    // Registra o Binder na hash
    this.bindingMap.put(propertyPath, uiBinder);
    return c;
  }

  /**
   * Este método cria um campo para ser usado como campo de edição de um VO.<br>
   * O campo é criado pelo método {@link #createField_ComboBox_Enum(Class, String, String, Boolean, Enum[])} e registra como um campo do VO e realiza o "BIND" entre o campo e o VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @param values Array com os valores da Enumeration que estarão disponíveis no Combo. Caso não queira nenhum valor definido passe um array Vazio. Ao passar NULL o método colocará automaticamente todos os valores disponíveis da Enumeration.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public <E extends Enum<?>> ComboBox<E> createVOField_ComboBox_Enum(String propertyPath, String helpPopupKey, E[] values) throws RFWException {
    Objects.requireNonNull(this.vo, "Impossível criar o campo sem que o VO esteja definido no UIFactory!");
    // Criamos o campo com base na informação do VO
    ComboBox<E> c = UIFactory.createField_ComboBox_Enum(vo.getClass(), propertyPath, helpPopupKey, false, values);
    // Realizamos o Bind no VO
    final UIBinder<E, E, VO> uiBinder = UIFactory.bindVO(c, this.voClass, this.vo, propertyPath, null, null);
    // Registra o field na Hash de Fields de VOs do UIFactory
    this.createVOField_Custom(propertyPath, c);
    // Registra o Binder na hash
    this.bindingMap.put(propertyPath, uiBinder);
    return c;
  }

  /**
   * Este método cria um campo para ser usado como campo de edição de um VO.<br>
   * O campo é criado pelo método {@link #createField_ComboBoxMultiselect_Enum(Class, String, String, Boolean, Enum[])} e registra como um campo do VO e realiza o "BIND" entre o campo e o VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @param values Array com os valores da Enumeration que estarão disponíveis no Combo. Caso não queira nenhum valor definido passe um array Vazio. Ao passar NULL o método colocará automaticamente todos os valores disponíveis da Enumeration.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public <E extends Enum<?>> ComboBoxMultiselect<E> createVOField_ComboBoxMultiselect_Enum(String propertyPath, String helpPopupKey, E[] values) throws RFWException {
    Objects.requireNonNull(this.vo, "Impossível criar o campo sem que o VO esteja definido no UIFactory!");
    // Criamos o campo com base na informação do VO
    ComboBoxMultiselect<E> c = UIFactory.createField_ComboBoxMultiselect_Enum(vo.getClass(), propertyPath, helpPopupKey, false, values);
    // Realizamos o Bind no VO
    final UIBinder<Set<E>, Object, VO> uiBinder = UIFactory.bindVO(c, this.voClass, this.vo, propertyPath, null, null);
    // Registra o field na Hash de Fields de VOs do UIFactory
    this.createVOField_Custom(propertyPath, c);
    // Registra o Binder na hash
    this.bindingMap.put(propertyPath, uiBinder);
    return c;
  }

  /**
   * Este método cria um campo para ser usado como campo de edição de um VO.<br>
   * O campo é criado pelo método {@link #createField_RFWTagsSelector(Class, String, String, Boolean)} e registra como um campo do VO e realiza o "BIND" entre o campo e o VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public RFWTagsSelector<?> createVOField_RFWTagsSelector(String propertyPath, String helpPopupKey) throws RFWException {
    Objects.requireNonNull(this.vo, "Impossível criar o campo sem que o VO esteja definido no UIFactory!");
    // Criamos o campo com base na informação do VO
    RFWTagsSelector<?> c = UIFactory.createField_RFWTagsSelector(vo.getClass(), propertyPath, helpPopupKey, false);
    // Realizamos o Bind no VO
    final UIBinder<?, Object, VO> uiBinder = UIFactory.bindVO(c, this.voClass, this.vo, propertyPath, null, null);
    // Registra o field na Hash de Fields de VOs do UIFactory
    this.createVOField_Custom(propertyPath, c);
    // Registra o Binder na hash
    this.bindingMap.put(propertyPath, uiBinder);
    return c;
  }

  /**
   * Este método cria um campo para ser usado como campo de edição de um VO.<br>
   * O campo é criado pelo método {@link #createField(Class, String, String, Boolean, FieldAlignment, Boolean, String, List, DateTimeResolution, Boolean, RFWDataFormatter)} e registra como um campo do VO e realiza o "BIND" entre o campo e o VO.
   *
   * @param propertyPath Atributo / Caminho do Atributo para gerar o campo.
   * @param helpPopupKey Chave do Bundle com o texto de ajuda/explicação para o campo
   * @param dimension Dimensão para filtrar as opções de unidades dentro do componente. Se passado null nenhum valor será colocado no Combo.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public <FIELDTYPE, BEANTYPE> ComboBox<MeasureUnit> createVOField_ComboBox_MeasureUnit(String propertyPath, String helpPopupKey, MeasureDimension dimension) throws RFWException {
    Objects.requireNonNull(this.vo, "Impossível criar o campo sem que o VO esteja definido no UIFactory!");
    // Criamos o campo com base na informação do VO
    ComboBox<MeasureUnit> c = UIFactory.createField_ComboBox_MeasureUnit(vo.getClass(), propertyPath, helpPopupKey, false, dimension);
    // Realizamos o Bind no VO
    final UIBinder<MeasureUnit, Object, VO> uiBinder = UIFactory.bindVO(c, this.voClass, this.vo, propertyPath, null, null);
    // Registra o field na Hash de Fields de VOs do UIFactory
    this.createVOField_Custom(propertyPath, c);
    // Registra o Binder na hash
    this.bindingMap.put(propertyPath, uiBinder);
    return c;
  }

  /**
   * Este método tem a finalidade criar um DataFormatter de acordo com o tipo de campo do VO. O tipo do campo é avaliado pela RFWMetaAnnotation do field.
   *
   * @param voClass Class do VO
   * @param propertyPath Atributo que se deseja o DataFormatter.
   * @return DataFormatter para o tipo de atributo do VO solicitado.
   * @throws RFWException
   */
  @SuppressWarnings("unchecked")
  private static <BEANTYPE, FIELDTYPE, VO extends RFWVO> RFWDataFormatter<FIELDTYPE, BEANTYPE> getDataFormatterByRFWMetaAnnotation(Class<VO> voClass, String propertyPath) throws RFWException {
    final Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, propertyPath);
    if (ann == null) {
      // throw new RFWCriticalException("Impossível gerar um campo para o atributo '${0}' da classe '${1}'. O atributo não tem uma RFWMeta Annotation!", new String[] { propertyPath, voClass.getCanonicalName() });
      return null; // Retornamos null ao invés do erro acima para que seja possível criar colunas no Grid sem que seja um VO com RFWMeta.
    }

    RFWDataFormatter<FIELDTYPE, BEANTYPE> df = null;
    if (ann instanceof RFWMetaBigDecimalPercentageField) {
      final RFWMetaBigDecimalPercentageField intAnn = (RFWMetaBigDecimalPercentageField) ann;

      BigDecimal min = null;
      if (!"".equals(intAnn.minValue())) min = new BigDecimal(intAnn.minValue());
      BigDecimal max = null;
      if (!"".equals(intAnn.maxValue())) max = new BigDecimal(intAnn.maxValue());
      df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) new RFWPercentageDataFormatter(RFW.getRoundingMode(), intAnn.scale(), intAnn.scaleMax(), min, max, intAnn.absolute());
    } else if (ann instanceof RFWMetaBigDecimalField) {
      final RFWMetaBigDecimalField intAnn = (RFWMetaBigDecimalField) ann;

      BigDecimal min = null;
      if (!"".equals(intAnn.minValue())) min = new BigDecimal(intAnn.minValue());
      BigDecimal max = null;
      if (!"".equals(intAnn.maxValue())) max = new BigDecimal(intAnn.maxValue());
      df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) new RFWBigDecimalDataFormatter(RFW.getRoundingMode(), intAnn.scale(), intAnn.scaleMax(), min, max, intAnn.absolute());
    } else if (ann instanceof RFWMetaBigDecimalCurrencyField) {
      final RFWMetaBigDecimalCurrencyField intAnn = (RFWMetaBigDecimalCurrencyField) ann;

      BigDecimal min = null;
      if (!"".equals(intAnn.minValue())) min = new BigDecimal(intAnn.minValue());
      BigDecimal max = null;
      if (!"".equals(intAnn.maxValue())) max = new BigDecimal(intAnn.maxValue());
      Integer scaleMax = null;
      if (intAnn.scaleMax() >= 0) scaleMax = intAnn.scaleMax();
      df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) new RFWCurrencyBigDecimalDataFormatter(RFW.getRoundingMode(), intAnn.scale(), scaleMax, min, max, intAnn.absolute());
    } else if (ann instanceof RFWMetaIntegerField) {
      final RFWMetaIntegerField intAnn = (RFWMetaIntegerField) ann;
      df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) new RFWIntegerDataFormatter(intAnn.minValue(), intAnn.maxValue(), intAnn.minValue() >= 0);
    } else if (ann instanceof RFWMetaLongField) {
      final RFWMetaLongField intAnn = (RFWMetaLongField) ann;
      df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) new RFWLongDataFormatter(intAnn.minvalue(), intAnn.maxvalue(), intAnn.minvalue() >= 0);
    } else if (ann instanceof RFWMetaStringCNPJField) {
      df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) RFWCNPJDataFormatter.createInstance();
    } else if (ann instanceof RFWMetaStringCPFField) {
      df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) RFWCPFDataFormatter.getInstance();
    } else if (ann instanceof RFWMetaStringCPFOrCNPJField) {
      df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) RFWCPFOrCNPJDataFormatter.getInstance();
    } else if (ann instanceof RFWMetaStringCEPField) {
      df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) RFWCEPDataFormatter.getInstance();
    } else if (ann instanceof RFWMetaStringIEField) {
      df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) RFWIEDataFormatter.getInstance();
    } else if (ann instanceof RFWMetaStringEmailField) {
      df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) RFWEmailDataFormatter.getInstance();
    } else if (ann instanceof RFWMetaStringPhoneField) {
      df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) new RFWPhoneDataFormatter();
    } else if (ann instanceof RFWMetaStringField) {
    } else if (ann instanceof RFWMetaEnumField) {
    } else if (ann instanceof RFWMetaBooleanField) {
    } else if (ann instanceof RFWMetaRelationshipField) {
    } else if (ann instanceof RFWMetaGenericField) {
      if (MeasureUnit.class.isAssignableFrom(RUReflex.getPropertyTypeByType(voClass, propertyPath))) {
        df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) new RFWMeasureUnitDataFormatter();
      }
    } else if (ann instanceof RFWMetaCollectionField) {
    } else if (ann instanceof RFWMetaDateField) {
      switch (((RFWMetaDateField) ann).resolution()) {
        case YEAR:
        case MONTH:
        case DAY:
          df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) RFWDateTimeDataFormatter.createDateInstance();
          break;
        case HOUR:
        case MINUTE:
        case SECOND:
        case MILLISECONDS:
          df = (RFWDataFormatter<FIELDTYPE, BEANTYPE>) RFWDateTimeDataFormatter.createDateTimeInstance();
          break;
      }
    } else {
      // Se não está previsto ainda o tipo de Annotation lançamos uma Exception para garantir que o desenvolvedor prepare adequadamente o Bind, criando converter se necessário.
      throw new RFWCriticalException("RFWMetaAnnotation '${0}' não suportada pelo getDataFormatterByRFWMetaAnnotation(...) do UIFactory", new String[] { ann.annotationType().getSimpleName() });
    }
    return df;
  }

  /**
   * Este método cria um campo conforme o tipo do attributo do VO passado. Para cada tipo de attirbuto e RFWMetaAnnotation este método retornará um tipo de campo. <br>
   * ATENÇÃO: Os métodos com o prefixo "createField_" não registra o campo no UIFactory, nem realiza nenhum tipo de BIND, apenas cria o Field e o configura conforme as definições do atributo do VO.
   *
   * @param voClass Classe do VO para o qual estamos montando o campo.
   * @param attribute Atributo / Caminho do atributo para gerar o campo.
   * @param helpPopupKey Chave da mensagem de ajuda. NULL caso não tenha.
   * @param forceRequired Se passado nulo será utilizado a definição da Annotation. Se definido TRUE o campo será deifnido como brigatório, se definido como FALSE o campo será definido como opcional.
   * @param fieldAlignment Alinhamendo do conteúdo co campo. Suportado até o momento apenas em TextFields.
   * @param masked Utilizável quando é um campo de Texto, {@link RFWMetaStringField}, cria um campo de senha, cujo conteúdo fica oculto (PasswordField). Valor Padrão = FALSE.
   * @param captionAttribute Usado quando o componente criado refere-se à um {@link RFWMetaRelationshipField} e o objeto em questão é um RFWVO. Aqui devemos passar qual o atributo do VO será utilizado como Caption no ComboBox Criado.<br>
   *          <b>ATENÇÃO:</B> Note que o atributo passado aqui é em relação ao objeto que estará no Provider do ComboBox e não do objeto sobre o qual estamos criando os campos.<br>
   *          Por exemplo, o ItemVO tem um relaiconamento com o ItemTypeVO e utiliza este método paracriar o campo. O valor passado aqui deve ser o 'name' ou ItemTypeVO_.vo().name(), e não o caminho a partir do ItemVO, utilizado para gerar o campo.
   * @param filterAttributes Lista de atributos (ou atributo único) que serão utilizados para realizar o filtro do componente. Se passado nulo ou vazio, será utilizado o atributo passado em captionAttribute.
   * @param resolutionOverride Quando passado, nos campos da date/periodo sobrepõe a precisão de data/hora definido no RFWMeta para o campo.
   * @param ignoreTextArea Quando o atributo é do tipo String e o tamanho máximo passa dos 1000 caracteres, o RFW cria por padrão um TextArea ao invés de um TextField. Quando esse atributo for true, o método continuará criando um TextField. Útil quando queremos criar um campo de filtro para o atributo.
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return Componente criado conforme o tipo do atributo. TextFields para Strings, Combos para Enums, etc.
   * @throws RFWException
   */
  @SuppressWarnings("unchecked")
  private static <VO extends RFWVO, FIELDTYPE> HasValue<FIELDTYPE> createField(Class<VO> voClass, String attribute, String helpPopupKey, Boolean forceRequired, FieldAlignment fieldAlignment, Boolean masked, String captionAttribute, List<String> filterAttributes, DateTimeResolution resolutionOverride, Boolean ignoreTextArea, RFWDataFormatter<?, ?> df, RFWDBProvider dbProvider) throws RFWException {
    HasValue<FIELDTYPE> c = null;

    // Define valores padrões dos parâmetros
    if (masked == null) masked = Boolean.FALSE;
    if (ignoreTextArea == null) ignoreTextArea = Boolean.FALSE;

    Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, attribute);
    if (ann == null) {
      throw new RFWCriticalException("Impossível gerar um campo para o atributo '${0}' da classe '${1}'. O atributo não tem uma RFWMeta Annotation!", new String[] { attribute, voClass.getCanonicalName() });
    }

    // Verifica o tipo de RFWMeta para criar o campo adequado
    if (ann instanceof RFWMetaStringField) {
      if (!ignoreTextArea && ((RFWMetaStringField) ann).maxLength() >= 1000) {
        c = (HasValue<FIELDTYPE>) createField_TextArea(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, df);
      } else {
        c = (HasValue<FIELDTYPE>) createField_TextField(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, masked, df);
      }
    } else if (ann instanceof RFWMetaEnumField) {
      c = (HasValue<FIELDTYPE>) createField_ComboBox_Enum(voClass, attribute, helpPopupKey, forceRequired, null);
    } else if (ann instanceof RFWMetaIntegerField) {
      c = (HasValue<FIELDTYPE>) createField_TextField(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, masked, df);
    } else if (ann instanceof RFWMetaLongField) {
      c = (HasValue<FIELDTYPE>) createField_TextField(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, masked, df);
    } else if (ann instanceof RFWMetaBigDecimalPercentageField) {
      c = (HasValue<FIELDTYPE>) createField_TextField(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, masked, df);
    } else if (ann instanceof RFWMetaBigDecimalCurrencyField) {
      c = (HasValue<FIELDTYPE>) createField_TextField(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, masked, df);
    } else if (ann instanceof RFWMetaBigDecimalField) {
      c = (HasValue<FIELDTYPE>) createField_TextField(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, masked, df);
    } else if (ann instanceof RFWMetaStringCNPJField) {
      c = (HasValue<FIELDTYPE>) createField_TextField(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, masked, df);
    } else if (ann instanceof RFWMetaStringCPFField) {
      c = (HasValue<FIELDTYPE>) createField_TextField(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, masked, df);
    } else if (ann instanceof RFWMetaStringCPFOrCNPJField) {
      c = (HasValue<FIELDTYPE>) createField_TextField(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, masked, df);
    } else if (ann instanceof RFWMetaStringIEField) {
      c = (HasValue<FIELDTYPE>) createField_TextField(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, masked, df);
    } else if (ann instanceof RFWMetaStringEmailField) {
      c = (HasValue<FIELDTYPE>) createField_TextField(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, masked, df);
    } else if (ann instanceof RFWMetaStringPhoneField) {
      c = (HasValue<FIELDTYPE>) createField_TextField(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, masked, df);
      // c = (HasValue<FIELDTYPE>) createField_RFWPhoneField(voClass, attribute, helpPopupKey, forceRequired);
    } else if (ann instanceof RFWMetaDateField) {
      c = (HasValue<FIELDTYPE>) createField_DateTime(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, resolutionOverride);
    } else if (ann instanceof RFWMetaRelationshipField) {
      // Quando é uma meta de relacionamento, precisamos destinguir o tipo de relacionamento para saber se sabemos que campo criar
      final Class<VO> type = (Class<VO>) RUReflex.getPropertyTransparentType(voClass, attribute);
      if (RFWVO.class.isAssignableFrom(type)) {
        c = (HasValue<FIELDTYPE>) createField_ComboBox_RFWVO(voClass, attribute, helpPopupKey, forceRequired, type, null, null, captionAttribute, filterAttributes, null, dbProvider);
      } else {
        throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getCanonicalName(), type.getCanonicalName() });
      }
    } else if (ann instanceof RFWMetaCollectionField) {
      c = (HasValue<FIELDTYPE>) createField_RFWTagsSelector(voClass, attribute, helpPopupKey, forceRequired);
    } else if (ann instanceof RFWMetaStringCEPField) {
      // c = (HasValue<FIELDTYPE>) createField_RFWCEPField(voClass, attribute, helpPopupKey, forceRequired, dbProvider);
      c = (HasValue<FIELDTYPE>) createField_TextField(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment, masked, df);
    } else if (ann instanceof RFWMetaGenericField) {
      final Class<?> type = RUReflex.getPropertyTransparentType(voClass, attribute);
      if (MeasureUnit.class.isAssignableFrom(type)) {
        c = (HasValue<FIELDTYPE>) createField_RFWComboMeasureUnit(voClass, attribute, helpPopupKey, forceRequired);
      } else {
        throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
      }
    } else if (ann instanceof RFWMetaBooleanField) {
      c = (HasValue<FIELDTYPE>) createField_CheckBoxOrRadioGroup(voClass, attribute, helpPopupKey, forceRequired, fieldAlignment);
    } else {
      throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
    }
    // Se não temos um campo, EXCEPTION NELES!!!
    if (c == null) throw new RFWCriticalException("O UIFactory falhou ao criar um campo para o atributo '${0}' presente no VO '${1}'.", new String[] { attribute, voClass.getCanonicalName() });
    return c;
  }

  /**
   * Cria um {@link CheckBox} ou um {@link RadioButtonGroup} baseado nas condições do atributo do VO passado.<br>
   * ATENÇÃO: Os métodos com o prefixo "createField_" não registra o campo no UIFactory, nem realiza nenhum tipo de BIND, apenas cria o Field e o configura conforme as definições do atributo do VO.<br>
   * Se for obrigatório criamos um única caixa de seleção, se não for criamos um RadioGroup (Precisa ser trocado para um conjunto de checkbox que ao selecionar um desmarque o outro, para que seja possível voltar o valor "nulo")
   *
   * @param voClass Classe do VO para o qual estamos montando o campo.
   * @param attribute Atributo / Caminho do atributo para gerar o campo.
   * @param helpPopupKey Chave da mensagem de ajuda. NULL caso não tenha.
   * @param forceRequired Se passado nulo será utilizado a definição da Annotation. Se definido TRUE o campo será deifnido como brigatório, se definido como FALSE o campo será definido como opcional.
   * @param fieldAlignment Alinhamendo do conteúdo co campo. Suportado até o momento apenas em TextFields.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public static <VO extends RFWVO> AbstractComponent createField_CheckBoxOrRadioGroup(Class<VO> voClass, String attribute, String helpPopupKey, Boolean forceRequired, FieldAlignment fieldAlignment) throws RFWException {
    Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, attribute);
    if (ann == null) throw new RFWCriticalException("Impossível gerar um campo para o atributo '${0}' da classe '${1}'. O atributo não tem uma RFWMeta Annotation!", new String[] { attribute, voClass.getCanonicalName() });

    // Caso force required seja definido, utilizamos ele, caso contrário procuramos o valor da Annotation
    if (forceRequired == null) {
      forceRequired = RUReflex.getRFWMetaAnnotationRequired(voClass, attribute);
      if (forceRequired == null) forceRequired = Boolean.FALSE;
    }

    if (ann instanceof RFWMetaBooleanField) {
      RFWMetaBooleanField annBool = (RFWMetaBooleanField) ann;

      // Se for obrigatório criamos um única caixa de seleção, se não for criamos um RadioGroup
      if (forceRequired) {
        CheckBox chk = new CheckBox(((RFWMetaBooleanField) ann).caption());
        chk.setRequiredIndicatorVisible(forceRequired);
        if (helpPopupKey != null) chk.setDescription(createHelpDescription(helpPopupKey), ContentMode.HTML);
        if (fieldAlignment != null) {
          chk.removeStyleName(FWVad.STYLE_ALIGN_CENTER);
          chk.removeStyleName(FWVad.STYLE_ALIGN_RIGHT);
          switch (fieldAlignment) {
            case CENTER:
              chk.addStyleName(FWVad.STYLE_ALIGN_CENTER);
              break;
            case LEFT:
              break; // Já é o padrão, não é preciso fazer nada além de remover os outros styles
            case RIGHT:
              chk.addStyleName(FWVad.STYLE_ALIGN_RIGHT);
              break;
          }
        }
        return chk;
      } else {
        LinkedList<Boolean> list = new LinkedList<>();
        list.add(Boolean.TRUE);
        list.add(Boolean.FALSE);

        CheckBoxGroup<Boolean> opt = new CheckBoxGroup<Boolean>(annBool.caption() + '?', list);
        opt.setItems(Boolean.TRUE, Boolean.FALSE);
        opt.setItemCaptionGenerator(i -> (i ? annBool.trueCaption() : annBool.falseCaption()));
        opt.setRequiredIndicatorVisible(annBool.required());
        if (helpPopupKey != null) opt.setDescription(createHelpDescription(helpPopupKey), ContentMode.HTML);

        // Listener para remover as outras opções marcadas
        opt.addSelectionListener(evt -> {
          Set<Boolean> newSelections = evt.getAddedSelection();
          if (newSelections.size() > 0) {
            for (Boolean totSel : opt.getSelectedItems()) {
              if (!newSelections.contains(totSel)) {
                opt.deselect(totSel);
              }
            }
          }
        });

        opt.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        if (fieldAlignment != null) {
          opt.removeStyleName(FWVad.STYLE_ALIGN_CENTER);
          opt.removeStyleName(FWVad.STYLE_ALIGN_RIGHT);
          switch (fieldAlignment) {
            case CENTER:
              opt.addStyleName(FWVad.STYLE_ALIGN_CENTER);
              break;
            case LEFT:
              break; // Já é o padrão, não é preciso fazer nada além de remover os outros styles
            case RIGHT:
              opt.addStyleName(FWVad.STYLE_ALIGN_RIGHT);
              break;
          }
        }
        return opt;
      }
    } else {
      throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
    }
  }

  /**
   * Cria um componente especial, composto por dois ComboBoxes, sendo um para escolher a dimensão da medida e o segundo para a unidade de medida.<br>
   * ATENÇÃO: Os métodos com o prefixo "createField_" não registra o campo no UIFactory, nem realiza nenhum tipo de BIND, apenas cria o Field e o configura conforme as definições do atributo do VO.
   *
   * @param voClass Classe do VO para o qual estamos montando o campo.
   * @param attribute Atributo / Caminho do atributo para gerar o campo.
   * @param helpPopupKey Chave da mensagem de ajuda. NULL caso não tenha.
   * @param forceRequired Se passado nulo será utilizado a definição da Annotation. Se definido TRUE o campo será deifnido como brigatório, se definido como FALSE o campo será definido como opcional.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public static <VO extends RFWVO> RFWComboBoxMeasureUnit createField_RFWComboMeasureUnit(Class<VO> voClass, String attribute, String helpPopupKey, Boolean forceRequired) throws RFWException {
    Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, attribute);
    if (ann == null) throw new RFWCriticalException("Impossível gerar um campo para o atributo '${0}' da classe '${1}'. O atributo não tem uma RFWMeta Annotation!", new String[] { attribute, voClass.getCanonicalName() });

    // Caso force required seja definido, utilizamos ele, caso contrário procuramos o valor da Annotation
    if (forceRequired == null) {
      forceRequired = RUReflex.getRFWMetaAnnotationRequired(voClass, attribute);
      if (forceRequired == null) forceRequired = Boolean.FALSE;
    }

    if (ann instanceof RFWMetaGenericField) {
      final Class<?> type = RUReflex.getPropertyTransparentType(voClass, attribute);

      if (MeasureUnit.class.isAssignableFrom(type)) {
        RFWComboBoxMeasureUnit cb = new RFWComboBoxMeasureUnit(((RFWMetaGenericField) ann).caption());
        cb.setRequiredIndicatorVisible(((RFWMetaGenericField) ann).required());
        if (helpPopupKey != null) cb.setDescription(createHelpDescription(helpPopupKey), ContentMode.HTML);
        return cb;
      } else {
        throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getCanonicalName(), type.getCanonicalName() });
      }
    } else {
      throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
    }
  }

  /**
   * Cria um ComboBox com as opções de MeasureUnit Filtrados conforme o {@link MeasureDimension} passado nos parametros. Útil para criar um componente para o usuário escolher a unidade baseada em uma única Dimensão.<br>
   * ATENÇÃO: Os métodos com o prefixo "createField_" não registra o campo no UIFactory, nem realiza nenhum tipo de BIND, apenas cria o Field e o configura conforme as definições do atributo do VO.
   *
   * @param voClass Classe do VO para o qual estamos montando o campo.
   * @param attribute Atributo / Caminho do atributo para gerar o campo.
   * @param helpPopupKey Chave da mensagem de ajuda. NULL caso não tenha.
   * @param forceRequired Se passado nulo será utilizado a definição da Annotation. Se definido TRUE o campo será deifnido como brigatório, se definido como FALSE o campo será definido como opcional.
   * @param dimension Dimensão para filtrar as opções de unidades dentro do componente. Se passado null nenhum valor será colocado no Combo.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public static <VO extends RFWVO> ComboBox<MeasureUnit> createField_ComboBox_MeasureUnit(Class<VO> voClass, String attribute, String helpPopupKey, Boolean forceRequired, MeasureDimension dimension) throws RFWException {
    Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, attribute);
    if (ann == null) throw new RFWCriticalException("Impossível gerar um campo para o atributo '${0}' da classe '${1}'. O atributo não tem uma RFWMeta Annotation!", new String[] { attribute, voClass.getCanonicalName() });

    // Caso force required seja definido, utilizamos ele, caso contrário procuramos o valor da Annotation
    if (forceRequired == null) {
      forceRequired = RUReflex.getRFWMetaAnnotationRequired(voClass, attribute);
      if (forceRequired == null) forceRequired = Boolean.FALSE;
    }

    if (ann instanceof RFWMetaGenericField) {
      final Class<?> type = RUReflex.getPropertyTransparentType(voClass, attribute);

      if (MeasureUnit.class.isAssignableFrom(type)) {
        ComboBox<MeasureUnit> cb = new ComboBox<>(((RFWMetaGenericField) ann).caption());
        cb.setRequiredIndicatorVisible(((RFWMetaGenericField) ann).required());
        if (helpPopupKey != null) cb.setDescription(createHelpDescription(helpPopupKey), ContentMode.HTML);
        if (dimension != null) cb.setItems(dimension.getUnits());
        cb.setItemCaptionGenerator(e -> {
          // String caption = "";
          // caption = e.getSymbol() + " (" + e.name() + ")";
          // } else {
          // caption = RFWBundle.get(e.getClass().getCanonicalName() + '.' + e.name());
          // }
          // return caption;
          // A lógica do código cima foi embutida no RFWBundle para que fique centralizada para qualquer parte do sistema.
          return RFWBundle.get(e);
        });
        return cb;
      } else {
        throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getCanonicalName(), type.getCanonicalName() });
      }
    } else {
      throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
    }
  }

  /**
   * Cria um componente {@link RFWTagsSelector} para selecionar entre opções.<Br>
   * ATENÇÃO: Os métodos com o prefixo "createField_" não registra o campo no UIFactory, nem realiza nenhum tipo de BIND, apenas cria o Field e o configura conforme as definições do atributo do VO.
   *
   * @param voClass Classe do VO para o qual estamos montando o campo.
   * @param attribute Atributo / Caminho do atributo para gerar o campo.
   * @param helpPopupKey Chave da mensagem de ajuda. NULL caso não tenha.
   * @param forceRequired Se passado nulo será utilizado a definição da Annotation. Se definido TRUE o campo será deifnido como brigatório, se definido como FALSE o campo será definido como opcional.
   * @return Componente criado conforme o tipo do atributo.
   * @throws RFWException
   */
  @SuppressWarnings("unchecked")
  public static <VO extends RFWVO> RFWTagsSelector<?> createField_RFWTagsSelector(Class<VO> voClass, String attribute, String helpPopupKey, Boolean forceRequired) throws RFWException {
    Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, attribute);
    if (ann == null) throw new RFWCriticalException("Impossível gerar um campo para o atributo '${0}' da classe '${1}'. O atributo não tem uma RFWMeta Annotation!", new String[] { attribute, voClass.getCanonicalName() });

    // Caso force required seja definido, utilizamos ele, caso contrário procuramos o valor da Annotation
    if (forceRequired == null) {
      forceRequired = RUReflex.getRFWMetaAnnotationRequired(voClass, attribute);
      if (forceRequired == null) forceRequired = Boolean.FALSE;
    }

    RFWTagsSelector<?> tags = null;
    if (ann instanceof RFWMetaCollectionField) {
      // Recuperamos o tipo de Objeto que está na lista para saber que componente criar
      final Class<?> type = RUReflex.getPropertyTransparentType(voClass, attribute);
      if (Enum.class.isAssignableFrom(type)) {
        // Cria a lista com os elementos da enumeração
        final ArrayList<Enum<?>> list = new ArrayList<>();
        for (Enum<?> e : ((Class<Enum<?>>) type).getEnumConstants())
          list.add(e);

        // Só será "organizável" se o atributo for uma lista, se for uma Hash não permite organização
        final Class<?> collectionType = RUReflex.getPropertyTypeByType(voClass, attribute);
        Boolean sortable = List.class.isAssignableFrom(collectionType);
        // Para as coleções de enum, vamos criar o campo RFWTagsSelector
        tags = new RFWTagsSelector<>(((RFWMetaCollectionField) ann).caption(), list, ((RFWMetaCollectionField) ann).required(), !((RFWMetaCollectionField) ann).uniqueValues(), sortable);
        tags.setRequiredIndicatorVisible(forceRequired);
        if (helpPopupKey != null) tags.setDescription(createHelpDescription(helpPopupKey), ContentMode.HTML);
        return tags;
      } else {
        throw new RFWCriticalException("O UIFactory não sabe que campo criar para o atributo '${0}' da classe '${1}', pois está anotado com a RFWMetaCollectionField e tem uma coleção do tipo '${2}'.", new String[] { attribute, voClass.getCanonicalName(), type.getCanonicalName() });
      }
    } else {
      throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
    }
  }

  /**
   * Cria um componente {@link RFWTagsSelector} para selecionar entre opções.<Br>
   * ATENÇÃO: Os métodos com o prefixo "createField_" não registra o campo no UIFactory, nem realiza nenhum tipo de BIND, apenas cria o Field e o configura conforme as definições do atributo do VO.
   *
   * @param voClass Classe do VO para o qual estamos montando o campo.
   * @param attribute Atributo / Caminho do atributo para gerar o campo.
   * @param helpPopupKey Chave da mensagem de ajuda. NULL caso não tenha.
   * @param forceRequired Se passado nulo será utilizado a definição da Annotation. Se definido TRUE o campo será deifnido como brigatório, se definido como FALSE o campo será definido como opcional.
   * @param values Array com os valores da Enumeration que estarão disponíveis no Combo. Caso não queira nenhum valor definido passe um array Vazio. Ao passar NULL o método colocará automaticamente todos os valores disponíveis da Enumeration.
   * @return Componente criado conforme o tipo do atributo.
   * @throws RFWException
   */
  @SuppressWarnings("unchecked")
  public static <VO extends RFWVO, E extends Enum<?>> ComboBoxMultiselect<E> createField_ComboBoxMultiselect_Enum(Class<VO> voClass, String attribute, String helpPopupKey, Boolean forceRequired, E[] values) throws RFWException {
    Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, attribute);
    if (ann == null) throw new RFWCriticalException("Impossível gerar um campo para o atributo '${0}' da classe '${1}'. O atributo não tem uma RFWMeta Annotation!", new String[] { attribute, voClass.getCanonicalName() });

    // Caso force required seja definido, utilizamos ele, caso contrário procuramos o valor da Annotation
    if (forceRequired == null) {
      forceRequired = RUReflex.getRFWMetaAnnotationRequired(voClass, attribute);
      if (forceRequired == null) forceRequired = Boolean.FALSE;
    }

    ComboBoxMultiselect<E> tags = null;
    if (ann instanceof RFWMetaCollectionField || ann instanceof RFWMetaEnumField) {
      // Recuperamos o tipo de Objeto que está na lista para saber que componente criar
      Class<?> type = null;
      String caption = "";
      if (ann instanceof RFWMetaEnumField) {
        type = RUReflex.getPropertyTypeByType(voClass, attribute);
        caption = ((RFWMetaEnumField) ann).caption();
      } else if (ann instanceof RFWMetaCollectionField) {
        type = RUReflex.getPropertyTransparentType(voClass, attribute);
        caption = ((RFWMetaCollectionField) ann).caption();
      }
      if (Enum.class.isAssignableFrom(type)) {
        // Cria a lista com os elementos da enumeração
        final ArrayList<E> list;
        if (values == null) {
          list = RUArray.createArrayList(((Class<E>) type).getEnumConstants());
        } else {
          if (values.length > 0) {
            list = RUArray.createArrayList(values);
          } else {
            list = new ArrayList<E>();
          }
        }

        // Para as coleções de enum, vamos criar o campo RFWTagsSelector
        tags = new ComboBoxMultiselect<E>(caption);
        tags.setRequiredIndicatorVisible(forceRequired);
        if (helpPopupKey != null) tags.setDescription(createHelpDescription(helpPopupKey), ContentMode.HTML);
        tags.setItems(list);
        tags.setItemCaptionGenerator(item -> RFWBundle.get(item));
        tags.setWidth("300px");
        return tags;
      } else {
        throw new RFWCriticalException("O UIFactory não sabe que campo criar para o atributo '${0}' da classe '${1}', pois está anotado com a RFWMetaCollectionField e tem uma coleção do tipo '${2}'.", new String[] { attribute, voClass.getCanonicalName(), type.getCanonicalName() });
      }
    } else {
      throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
    }
  }

  /**
   * Cria um {@link ComboBox} cuja as opções são objetos RFWVO. Utilizado para definir um VO como valor no atributo de outro VO.<br>
   * ATENÇÃO: Os métodos com o prefixo "createField_" não registra o campo no UIFactory, nem realiza nenhum tipo de BIND, apenas cria o Field e o configura conforme as definições do atributo do VO.
   *
   * @param voClass Classe do VO a Qual este Combo será associado.
   * @param attribute Nome do atributo no VO onde será associado o VO deste combo como valor.
   * @param helpPopupKey Chave de acesso do HelpPopup do campo.
   * @param forceRequired Caso true, força a exibição de que a informação é obrigtória, independente da definição da RFWMeta do atributo.
   * @param providerClass Classe dos VOs que serão utilizados para popular o ComboBox. Este atributo é ignorado quando o atributo provider é fornecido com um provider pronto.
   * @param orderBy OrderBy para organizar os dados dentro do Combo. Este atributo é ignorado quando o atributo provider é fornecido com um provider pronto.
   * @param attributes Attributos que os objetos do combo precisam carregar. Recomendado passar nulo e não carregar nada a não ser que o caption esteja em algum subatributo. Evitando assim um "overload" do banco. Caso alguma informação seja necessária do objeto escolhido, faça a consulta completa desse objeto isolado quando for utilizar. Este atributo é ignorado quando o atributo provider é
   *          fornecido com um provider pronto.
   * @param captionAttribute Atributo da 'providerClass' que será utilizado para gerar os Captions no ComboBox. (Pode ser substituido por outro ItemCaptionGenerator posteriormente.
   * @param filterAttributes Lista de atributos (ou atributo único) que serão utilizados para realizar o filtro do componente. Se passado nulo ou vazio, será utilizado o atributo passado em captionAttribute.
   * @param provider Define um Provider pronto para ser utilizado no combo. Caso seja passado nulo, um provider padrão é criado a partir dos argumentos providerClass, orderBy e Attributes.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return RFWComboBox com generics adequado.
   * @throws RFWException
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <VO extends RFWVO, VALUE extends RFWVO> ComboBox<VALUE> createField_ComboBox_RFWVO(Class<VO> voClass, String attribute, String helpPopupKey, Boolean forceRequired, Class<VALUE> providerClass, RFWOrderBy orderBy, String[] attributes, String captionAttribute, List<String> filterAttributes, UIDataProvider<VALUE> provider, RFWDBProvider dbProvider) throws RFWException {
    Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, attribute);
    if (ann == null) throw new RFWCriticalException("Impossível gerar um campo para o atributo '${0}' da classe '${1}'. O atributo não tem uma RFWMeta Annotation!", new String[] { attribute, voClass.getCanonicalName() });

    if (captionAttribute == null) throw new RFWCriticalException("O parâmetro 'captionAttribute' é obrigatório para criação do campo para o atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName() });

    // Caso force required seja definido, utilizamos ele, caso contrário procuramos o valor da Annotation
    if (forceRequired == null) {
      forceRequired = RUReflex.getRFWMetaAnnotationRequired(voClass, attribute);
      if (forceRequired == null) forceRequired = Boolean.FALSE;
    }

    // Verifica o tipo de RFWMeta para criar o campo adequado
    if (ann instanceof RFWMetaRelationshipField) {
      ComboBox<VALUE> cb = new ComboBox<>(((RFWMetaRelationshipField) ann).caption() + ':');
      cb.setRequiredIndicatorVisible(forceRequired);
      if (helpPopupKey != null) cb.setDescription(createHelpDescription(helpPopupKey), ContentMode.HTML);

      if (provider == null) {
        PreProcess.requiredNonNullCritical(dbProvider);
        provider = new UIDataProvider(providerClass, orderBy, attributes, dbProvider);
      }
      if (filterAttributes == null || filterAttributes.size() == 0) {
        filterAttributes = new LinkedList<>();
        filterAttributes.add(captionAttribute);
      }
      provider.setFilterAttributes(filterAttributes); // Definimos que o filtro do combo deve ser aplicado no mesmo atributo que estamos utilizando para exibir o caption.
      cb.setDataProvider(provider);
      cb.setItemCaptionGenerator(value -> {
        try {
          return "" + RUReflex.getPropertyValue(value, captionAttribute);
        } catch (RFWException e) {
          TreatException.treat(e);
        }
        return "";
      });
      cb.setWidth("100%");
      return cb;
    } else {
      throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
    }
  }

  /**
   * Este método cria um campo do tipo {@link DateField} ou um {@link DateTimeField} para trabalhar com Datas e Horas baseado no atributo passado.<br>
   * Note que o Vaadin obriga que seja um {@link DateField} para o tipo {@link LocalDate} e um {@link DateTimeField} para o {@link LocalDateTime}. Assim este método cria o componente conforme o tipo do atributo passado.<br>
   * ATENÇÃO: Os métodos com o prefixo "createField_" não registra o campo no UIFactory, nem realiza nenhum tipo de BIND, apenas cria o Field e o configura conforme as definições do atributo do VO.
   *
   * @param attribute Atributo / Caminho do atributo para gerar o campo.
   * @param voClass Classe do VO para o qual estamos montando o campo.
   * @param helpPopupKey Chave da mensagem de ajuda. NULL caso não tenha.
   * @param forceRequired Se passado nulo será utilizado a definição da Annotation. Se definido TRUE o campo será deifnido como brigatório, se definido como FALSE o campo será definido como opcional.
   * @param fieldAlignment Alinhamendo do conteúdo co campo. Suportado até o momento apenas em TextFields.
   * @param resolutionOverride Quando passado, nos campos da date/periodo sobrepõe a precisão de data/hora definido no RFWMeta para o campo.
   * @return Componente criado conforme o tipo do atributo. TextFields para Strings, Combos para Enums, etc.
   * @throws RFWException
   */
  public static <VO extends RFWVO> AbstractField<?> createField_DateTime(Class<VO> voClass, String attribute, String helpPopupKey, Boolean forceRequired, FieldAlignment fieldAlignment, DateTimeResolution resolutionOverride) throws RFWException {
    Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, attribute);
    if (ann == null) throw new RFWCriticalException("Impossível gerar um campo para o atributo '${0}' da classe '${1}'. O atributo não tem uma RFWMeta Annotation!", new String[] { attribute, voClass.getCanonicalName() });

    // Caso force required seja definido, utilizamos ele, caso contrário procuramos o valor da Annotation
    if (forceRequired == null) {
      forceRequired = RUReflex.getRFWMetaAnnotationRequired(voClass, attribute);
      if (forceRequired == null) forceRequired = Boolean.FALSE;
    }

    if (ann instanceof RFWMetaDateField) {
      final Class<?> type = RUReflex.getPropertyTransparentType(voClass, attribute);
      AbstractField<?> field = null;

      if (LocalDateTime.class.isAssignableFrom(type)) {
        if (resolutionOverride == null) resolutionOverride = convertToDateTimeResolution(((RFWMetaDateField) ann).resolution());

        DateTimeField dateField = new DateTimeField(((RFWMetaDateField) ann).caption() + ':');
        dateField.setResolution(resolutionOverride);
        field = dateField;
      } else if (LocalDate.class.isAssignableFrom(type)) {
        DateResolution resolution = null;
        if (resolutionOverride != null) resolution = convertToDateResolution(resolutionOverride);
        if (resolution == null) resolution = convertToDateResolution(((RFWMetaDateField) ann).resolution());

        DateField dateField = new DateField(((RFWMetaDateField) ann).caption() + ':');
        dateField.setResolution(resolution);
        field = dateField;
      } else {
        throw new RFWCriticalException("A 'RFWMetaDateField' não pode ser usado com o tipo '" + type.getCanonicalName() + "' do atributo '" + attribute + "'.");
      }

      if (fieldAlignment == null) fieldAlignment = FieldAlignment.CENTER;
      field.removeStyleName(FWVad.STYLE_ALIGN_CENTER);
      field.removeStyleName(FWVad.STYLE_ALIGN_RIGHT);
      switch (fieldAlignment) {
        case CENTER:
          field.addStyleName(FWVad.STYLE_ALIGN_CENTER);
          break;
        case LEFT:
          break; // Já é o padrão, não é preciso fazer nada além de remover os outros styles
        case RIGHT:
          field.addStyleName(FWVad.STYLE_ALIGN_RIGHT);
          break;
      }
      if (helpPopupKey != null) field.setDescription(createHelpDescription(helpPopupKey), ContentMode.HTML);
      field.setRequiredIndicatorVisible(forceRequired);
      return field;
    } else {
      throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
    }
  }

  /**
   * Este método cria um campo do tipo ComboBox para trabalhar com Enumerations baseado no atributo passado.<br>
   * ATENÇÃO: Os métodos com o prefixo "createField_" não registra o campo no UIFactory, nem realiza nenhum tipo de BIND, apenas cria o Field e o configura conforme as definições do atributo do VO.
   *
   * @param attribute Atributo / Caminho do atributo para gerar o campo.
   * @param voClass Classe do VO para o qual estamos montando o campo.
   * @param helpPopupKey Chave da mensagem de ajuda. NULL caso não tenha.
   * @param forceRequired Se passado nulo será utilizado a definição da Annotation. Se definido TRUE o campo será deifnido como brigatório, se definido como FALSE o campo será definido como opcional.
   * @param values Array com os valores da Enumeration que estarão disponíveis no Combo. Caso não queira nenhum valor definido passe um array Vazio. Ao passar NULL o método colocará automaticamente todos os valores disponíveis da Enumeration.
   * @return ComboBox com as opções da Enumeration.
   * @throws RFWException
   */
  @SuppressWarnings("unchecked")
  public static <E extends Enum<?>, VO extends RFWVO> ComboBox<E> createField_ComboBox_Enum(Class<VO> voClass, String attribute, String helpPopupKey, Boolean forceRequired, E[] values) throws RFWException {
    Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, attribute);
    if (ann == null) throw new RFWCriticalException("Impossível gerar um campo para o atributo '${0}' da classe '${1}'. O atributo não tem uma RFWMeta Annotation!", new String[] { attribute, voClass.getCanonicalName() });

    final Class<?> type = RUReflex.getPropertyTransparentType(voClass, attribute);
    if (!Enum.class.isAssignableFrom(type)) throw new RFWCriticalException("A RFWMetaEnumField foi utilizada no atributo '${0}' da classe '${1}', mas seu tipo não é uma Enumeration.", new String[] { attribute, voClass.getCanonicalName() });

    // Caso force required seja definido, utilizamos ele, caso contrário procuramos o valor da Annotation
    if (forceRequired == null) {
      forceRequired = RUReflex.getRFWMetaAnnotationRequired(voClass, attribute);
      if (forceRequired == null) forceRequired = Boolean.FALSE;
    }

    // Verifica o tipo de RFWMeta para criar o campo adequado
    if (ann instanceof RFWMetaEnumField) {
      ComboBox<E> cb = new ComboBox<>(((RFWMetaEnumField) ann).caption() + ':');
      cb.setRequiredIndicatorVisible(forceRequired);
      if (helpPopupKey != null) cb.setDescription(createHelpDescription(helpPopupKey), ContentMode.HTML);
      if (values == null) {
        cb.setItems(((Class<E>) type).getEnumConstants());
      } else {
        if (values.length > 0) cb.setItems(values);
      }
      cb.setItemCaptionGenerator(enumValue -> RFWBundle.get(enumValue));
      cb.setWidth("100%");
      return cb;
    } else {
      throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
    }
  }

  /**
   * Este método cria um campo do tipo ComboBox para trabalhar com os Valores Booleanos. É obrigatório que o atributo tenha a {@link RFWMetaBooleanField}.<br>
   * ATENÇÃO: Os métodos com o prefixo "createField_" não registra o campo no UIFactory, nem realiza nenhum tipo de BIND, apenas cria o Field e o configura conforme as definições do atributo do VO.
   *
   * @param attribute Atributo / Caminho do atributo para gerar o campo.
   * @param voClass Classe do VO para o qual estamos montando o campo.
   * @param helpPopupKey Chave da mensagem de ajuda. NULL caso não tenha.
   * @param forceRequired Se passado nulo será utilizado a definição da Annotation. Se definido TRUE o campo será deifnido como brigatório, se definido como FALSE o campo será definido como opcional.
   * @return ComboBox com as opções do Boolean.
   * @throws RFWException
   */
  public static <VO extends RFWVO> ComboBox<Boolean> createField_ComboBox_Boolean(Class<VO> voClass, String attribute, String helpPopupKey, Boolean forceRequired) throws RFWException {

    Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, attribute);
    if (ann == null || !(ann instanceof RFWMetaBooleanField)) {
      throw new RFWCriticalException("O atributo '${0}' da classe '${1}' não contém uma RFWMetaBooleanField!", new String[] { attribute, voClass.getCanonicalName() });
    }

    final Class<?> type = RUReflex.getPropertyTransparentType(voClass, attribute);
    if (!Boolean.class.isAssignableFrom(type)) throw new RFWCriticalException("A RFWMetaBooleanField foi utilizada no atributo '${0}' da classe '${1}', mas seu tipo não é um Boolean.", new String[] { attribute, voClass.getCanonicalName() });

    // Caso force required seja definido, utilizamos ele, caso contrário procuramos o valor da Annotation
    if (forceRequired == null) {
      forceRequired = RUReflex.getRFWMetaAnnotationRequired(voClass, attribute);
      if (forceRequired == null) forceRequired = Boolean.FALSE;
    }

    // Verifica o tipo de RFWMeta para criar o campo adequado
    ComboBox<Boolean> cb = new ComboBox<>(((RFWMetaBooleanField) ann).caption() + ':');
    cb.setRequiredIndicatorVisible(forceRequired);
    if (helpPopupKey != null) cb.setDescription(createHelpDescription(helpPopupKey), ContentMode.HTML);
    cb.setItems(Boolean.TRUE, Boolean.FALSE);
    cb.setItemCaptionGenerator(value -> {
      if (value == null) {
        return "";
      } else if (value) {
        return ((RFWMetaBooleanField) ann).trueCaption();
      } else {
        return ((RFWMetaBooleanField) ann).falseCaption();
      }
    });
    cb.setWidth("100%");
    return cb;
  }

  /**
   * Este método cria um campo do tipo TextField baseado no atributo passado.<br>
   * ATENÇÃO: Os métodos com o prefixo "createField_" não registra o campo no UIFactory, nem realiza nenhum tipo de BIND, apenas cria o Field e o configura conforme as definições do atributo do VO.
   *
   * @param voClass Classe do VO para o qual estamos montando o campo.
   * @param attribute Atributo / Caminho do atributo para gerar o campo.
   * @param helpPopupKey Chave da mensagem de ajuda. NULL caso não tenha.
   * @param forceRequired Se passado nulo será utilizado a definição da Annotation. Se definido TRUE o campo será deifnido como brigatório, se definido como FALSE o campo será definido como opcional.
   * @param fieldAlignment Alinhamendo do conteúdo co campo. Suportado até o momento apenas em TextFields.
   * @param masked Utilizável quando é um campo de Texto, {@link RFWMetaStringField}, cria um campo de senha, cujo conteúdo fica oculto (PasswordField). Valor Padrão = FALSE.
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   *
   * @return Component criado conforme atributos passados
   * @throws RFWException Em caso de erro, ou caso o tipo de annonation não seja suportado por este método
   */
  public static <VO extends RFWVO> TextField createField_TextField(Class<VO> voClass, String attribute, String helpPopupKey, Boolean forceRequired, FieldAlignment fieldAlignment, Boolean masked, RFWDataFormatter<?, ?> df) throws RFWException {
    final Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, attribute);
    if (ann == null) throw new RFWCriticalException("Impossível gerar um campo para o atributo '${0}' da classe '${1}'. O atributo não tem uma RFWMeta Annotation!", new String[] { attribute, voClass.getCanonicalName() });

    // Caso force required seja definido, utilizamos ele, caso contrário procuramos o valor da Annotation
    if (forceRequired == null) {
      forceRequired = RUReflex.getRFWMetaAnnotationRequired(voClass, attribute);
      if (forceRequired == null) forceRequired = Boolean.FALSE;
    }

    if (masked == null) masked = false;

    TextField t = null;
    if (ann instanceof RFWMetaStringField) {
      if (masked) {
        t = new PasswordField(((RFWMetaStringField) ann).caption() + ':');
      } else {
        t = new TextField(((RFWMetaStringField) ann).caption() + ':');
      }
      t.setRequiredIndicatorVisible(forceRequired);
      t.setMaxLength(((RFWMetaStringField) ann).maxLength());
      t.setWidth("100%");
    } else if (ann instanceof RFWMetaIntegerField) {
      t = new TextField(((RFWMetaIntegerField) ann).caption() + ':');
      t.setRequiredIndicatorVisible(forceRequired);
      t.setMaxLength(6);
      t.setWidth("130px");
    } else if (ann instanceof RFWMetaLongField) {
      t = new TextField(((RFWMetaLongField) ann).caption() + ':');
      t.setRequiredIndicatorVisible(forceRequired);
      t.setMaxLength(10);
      t.setWidth("130px");
    } else if (ann instanceof RFWMetaBigDecimalPercentageField) {
      t = new TextField(((RFWMetaBigDecimalPercentageField) ann).caption() + ':');
      t.setRequiredIndicatorVisible(forceRequired);
      if (fieldAlignment == null) fieldAlignment = FieldAlignment.CENTER;
      t.setMaxLength(6);
      t.setWidth("130px");
    } else if (ann instanceof RFWMetaBigDecimalCurrencyField) {
      t = new TextField(((RFWMetaBigDecimalCurrencyField) ann).caption() + ':');
      t.setRequiredIndicatorVisible(forceRequired);
      if (fieldAlignment == null) fieldAlignment = FieldAlignment.CENTER;
      t.setMaxLength(20);
      t.setWidth("130px");
    } else if (ann instanceof RFWMetaBigDecimalField) {
      t = new TextField(((RFWMetaBigDecimalField) ann).caption() + ':');
      t.setRequiredIndicatorVisible(forceRequired);
      t.setMaxLength(((RFWMetaBigDecimalField) ann).maxValue().length() + RUNumber.max(((RFWMetaBigDecimalField) ann).scaleMax(), ((RFWMetaBigDecimalField) ann).scale()));
      t.setWidth("130px");
    } else if (ann instanceof RFWMetaStringCNPJField) {
      t = new TextField(((RFWMetaStringCNPJField) ann).caption() + ':');
      t.setRequiredIndicatorVisible(forceRequired);
      if (fieldAlignment == null) fieldAlignment = FieldAlignment.CENTER;
      t.setMaxLength(RFWCNPJDataFormatter.createInstance().getMaxLenght());
      t.setWidth("170px");
    } else if (ann instanceof RFWMetaStringCPFField) {
      t = new TextField(((RFWMetaStringCPFField) ann).caption() + ':');
      t.setRequiredIndicatorVisible(forceRequired);
      if (fieldAlignment == null) fieldAlignment = FieldAlignment.CENTER;
      t.setMaxLength(RFWCPFDataFormatter.getInstance().getMaxLenght());
      t.setWidth("170px");
    } else if (ann instanceof RFWMetaStringCPFOrCNPJField) {
      t = new TextField(((RFWMetaStringCPFOrCNPJField) ann).caption() + ':');
      t.setRequiredIndicatorVisible(forceRequired);
      if (fieldAlignment == null) fieldAlignment = FieldAlignment.CENTER;
      t.setMaxLength(RFWCPFOrCNPJDataFormatter.getInstance().getMaxLenght());
      t.setWidth("170px");
    } else if (ann instanceof RFWMetaStringIEField) {
      t = new TextField(((RFWMetaStringIEField) ann).caption() + ':');
      t.setRequiredIndicatorVisible(forceRequired);
      if (fieldAlignment == null) fieldAlignment = FieldAlignment.CENTER;
      t.setMaxLength(RFWIEDataFormatter.getInstance().getMaxLenght());
      t.setWidth("200px");
    } else if (ann instanceof RFWMetaStringPhoneField) {
      t = new TextField(((RFWMetaStringPhoneField) ann).caption() + ':');
      t.setRequiredIndicatorVisible(forceRequired);
      t.setMaxLength(new RFWPhoneDataFormatter().getMaxLenght());
      t.setWidth("100%");
    } else if (ann instanceof RFWMetaStringEmailField) {
      t = new TextField(((RFWMetaStringEmailField) ann).caption() + ':');
      t.setRequiredIndicatorVisible(forceRequired);
      t.setMaxLength(RFWEmailDataFormatter.getInstance().getMaxLenght());
      t.setWidth("100%");
    } else if (ann instanceof RFWMetaStringCEPField) {
      t = new TextField(((RFWMetaStringCEPField) ann).caption() + ':');
      t.setRequiredIndicatorVisible(forceRequired);
      if (fieldAlignment == null) fieldAlignment = FieldAlignment.CENTER;
      t.setMaxLength(RFWCEPDataFormatter.getInstance().getMaxLenght());
      t.setWidth("130px");
    } else {
      throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
    }

    // =====>>>>> Configura o Field
    if (helpPopupKey != null) t.setDescription(createHelpDescription(helpPopupKey), ContentMode.HTML);

    if (fieldAlignment != null) {
      t.removeStyleName(FWVad.STYLE_ALIGN_CENTER);
      t.removeStyleName(FWVad.STYLE_ALIGN_RIGHT);
      switch (fieldAlignment) {
        case CENTER:
          t.addStyleName(FWVad.STYLE_ALIGN_CENTER);
          break;
        case LEFT:
          break; // Já é o padrão, não é preciso fazer nada além de remover os outros styles
        case RIGHT:
          t.addStyleName(FWVad.STYLE_ALIGN_RIGHT);
          break;
      }
    }

    // Se tiver um df, e tiver um maxlength utilizamos ele, pois normalmente com a formatação é necessário mais caracteres
    if (df != null && df.getMaxLenght() > 0) t.setMaxLength(df.getMaxLenght());

    return t;
  }

  /**
   * Este método cria um campo do tipo TextArea baseado no atributo passado.<br>
   * ATENÇÃO: Os métodos com o prefixo "createField_" não registra o campo no UIFactory, nem realiza nenhum tipo de BIND, apenas cria o Field e o configura conforme as definições do atributo do VO.
   *
   * @param voClass Classe do VO para o qual estamos montando o campo.
   * @param attribute Atributo / Caminho do atributo para gerar o campo.
   * @param helpPopupKey Chave da mensagem de ajuda. NULL caso não tenha.
   * @param forceRequired Se passado nulo será utilizado a definição da Annotation. Se definido TRUE o campo será deifnido como brigatório, se definido como FALSE o campo será definido como opcional.
   * @param fieldAlignment Alinhamendo do conteúdo co campo. Suportado até o momento apenas em TextFields.
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   *
   * @return Component criado conforme atributos passados
   * @throws RFWException Em caso de erro, ou caso o tipo de annonation não seja suportado por este método
   */
  public static <VO extends RFWVO> TextArea createField_TextArea(Class<VO> voClass, String attribute, String helpPopupKey, Boolean forceRequired, FieldAlignment fieldAlignment, RFWDataFormatter<?, ?> df) throws RFWException {
    final Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, attribute);
    if (ann == null) throw new RFWCriticalException("Impossível gerar um campo para o atributo '${0}' da classe '${1}'. O atributo não tem uma RFWMeta Annotation!", new String[] { attribute, voClass.getCanonicalName() });

    // Caso force required seja definido, utilizamos ele, caso contrário procuramos o valor da Annotation
    if (forceRequired == null) {
      forceRequired = RUReflex.getRFWMetaAnnotationRequired(voClass, attribute);
      if (forceRequired == null) forceRequired = Boolean.FALSE;
    }

    TextArea t = null;
    if (ann instanceof RFWMetaStringField) {
      t = new TextArea(((RFWMetaStringField) ann).caption() + ':');
      t.setHeight("100%");
      t.setRequiredIndicatorVisible(forceRequired);
      if (helpPopupKey != null) t.setDescription(createHelpDescription(helpPopupKey), ContentMode.HTML);
      if (fieldAlignment != null) {
        t.removeStyleName(FWVad.STYLE_ALIGN_CENTER);
        t.removeStyleName(FWVad.STYLE_ALIGN_RIGHT);
        switch (fieldAlignment) {
          case CENTER:
            t.addStyleName(FWVad.STYLE_ALIGN_CENTER);
            break;
          case LEFT:
            break; // Já é o padrão, não é preciso fazer nada além de remover os outros styles
          case RIGHT:
            t.addStyleName(FWVad.STYLE_ALIGN_RIGHT);
            break;
        }
      }
      t.setMaxLength(((RFWMetaStringField) ann).maxLength());
      // Se tiver um df, e tiver um maxlength utilizamos ele, pois normalmente com a formatação é necessário mais caracteres
      if (df != null && df.getMaxLenght() > 0) t.setMaxLength(df.getMaxLenght());
      t.setWidth("100%");
    } else {
      throw new RFWCriticalException("RFWMeta '${2}' não suportada pelo UIFactory: atributo '${0}' da classe '${1}'.", new String[] { attribute, voClass.getCanonicalName(), ann.annotationType().getSimpleName() });
    }
    return t;
  }

  /**
   * Método utilitário para montar a descrição utilizada nos "Help Popup" dos componentes. <Br>
   * ATENÇÃO:</B> Obrigatóriamente coloque o bunde de help no arquivo para que seja mantido o padrão de
   * <h1>/
   * <h2>.
   *
   * @param helpPopupKey Chave do texto de ajuna no arquivo de Bundle.
   * @return String contendo HTML para ser colocado no description do componente.
   */
  public static String createHelpDescription(String helpPopupKey) {
    if (helpPopupKey != null) {
      return "<img class=\"helpicon\" src=\"VAADIN/themes/" + RFWUI.getCurrent().getTheme() + "/icon/help_32.png\"/>" + RFWBundle.get(helpPopupKey);
    }
    return null;
  }

  /**
   * Este método coloca uma mensagem de ajuda no padrão do RFW em qualquer componente.
   *
   * @param tab Componente que deve receber a mensagem de ajuda (popup help description).
   * @param helpKey Conteúdo da Mensagem de ajuda. Se estiver no formato "HLP\\d{6,}" o conteúdo será procurado como chave no Bundle. Se nada for encontrado a chave é colocada na mensagem de HELP.
   */
  public static void setHelpDescription(Tab tab, String helpKey) {
    createDescription(helpKey);
    tab.setDescription(createHelpDescription(helpKey), ContentMode.HTML);
  }

  /**
   * Este método coloca uma mensagem de ajuda no padrão do RFW em qualquer componente.
   *
   * @param component Componente que deve receber a mensagem de ajuda (popup help description).
   * @param helpKey Conteúdo da Mensagem de ajuda. Se estiver no formato "HLP\\d{6,}" o conteúdo será procurado como chave no Bundle. Se nada for encontrado a chave é colocada na mensagem de HELP.
   */
  public static void setHelpDescription(AbstractComponent component, String helpKey) {
    createDescription(helpKey);
    component.setDescription(createHelpDescription(helpKey), ContentMode.HTML);
  }

  /**
   * Cria o padrão de mensagem utilizado em descrições de ajuda de componentes. (Método Auxiliar)
   *
   * @param helpKey Chave da mensagem do bundle.
   */
  private static void createDescription(String helpKey) {
    String msg = helpKey;
    if (helpKey.matches("HLP\\d{6,}")) {
      msg = RFWBundle.get(helpKey);
      if (msg == null) msg = helpKey;
    }
  }

  public static Label createLabel(String content, LabelType type) {
    return createLabel(content, type, ContentMode.TEXT);
  }

  public static Label createLabel(String content, LabelType type, ContentMode contentMode) {
    Label c = new Label();

    c.setContentMode(contentMode);
    c.setValue(content);

    switch (type) {
      case DEFAULT:
        break;
      case HIGHLIGHT:
        c.addStyleName(FWVad.STYLE_BACKGROUND_GAINSBORO);
        c.addStyleName(FWVad.STYLE_FONT_BOLD);
        break;
      case FIELDCAPTION:
        c.addStyleName(FWVad.STYLE_FONT_BOLD);
        break;
    }

    return c;
  }

  /**
   * Este método o conteúdo do painel de busca utilizado no RFW em um GridLayout.<br>
   * A largura total será dividida em colunas de 100px, e os campos serão alocados de acordo com sua largura mínima até que se acabe os componentes.<br>
   * Ao chegar no final, se um componente não couber, tentaremos o próximo e assim por diante até que se acabem os componentes. Ao acabar os componentes, vemos o espaço que não foi utilizado e vamos espandindo os componentes até que os que entraram na tela ocupem o espaço todo disponível.
   *
   * @param rows número de linhas do grid que podemos escrever os componentes.
   * @param listener Aceita um Listener que será chamado antes de executar a função padrão do botão SEARCH. Permitindo assim alguma validação antes de forçar a atualização do Grid. Note que para impedir a atualização do Grid uma exception deve ser lançada, caso contrário o método será executado da mesma maneira.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   */
  public Layout createSearchPanel(int rows, final ClickListener listener, final RFWDBProvider dbProvider) {
    final HorizontalLayout mainLayout = new HorizontalLayout();
    mainLayout.setSizeFull();
    mainLayout.setMargin(false);

    LinkedList<String> mainViewElected = new LinkedList<>(); // Armazena os caminhos dos componentes que foram "eleitos" para a página principal de filtros.

    {
      double maxWidth = FWVad.getCanvasWidth() - 240; // Consideramos o espaço total da janela tirando a borde de margin que nosso tema considera para saber o espaço "líquido" -28 de margens e bordas - 220 da largura dos botões
      int cols = Math.floorDiv((int) maxWidth, 5);

      // O Array marca um mapa de posicionamento dos campos que serão colocados no Grid:
      // Indice 1 = Número da linha
      // Indice 2 = Indice do componente. Inicializamos com o número de colunas pq no máximo teremos um componente por coluna. Mas se só tivermos 2 componente apenas as posições 0 e 1 terão conteúdo, o restante permanecerá nulo.
      // Indice 3 = Dividido em 2 informações: 0 - referência do próprio FieldMetaData e; 1- numero de colunas total que o componente está utilizando
      Object[][][] fieldsMap = new Object[rows][cols][3];

      int[] occupacy = new int[rows]; // Registra o total de colunas que já ocupamos para a cada linha.

      // Iteramos os componentes para ir alocando
      for (FieldMetaData fd : this.moFieldHash.values()) {
        int width = 0; // Em colunas
        if (fd.minWidth.matches("[0-9]+px")) {
          width = (int) Math.ceil(Double.parseDouble(fd.minWidth.replaceAll("[^0-9]*", "")) / 5);
        } else {
          // Se não é px então é % pq já foi validado na criação do FieldMetaData. Neste caso verificamos o percentual da tela arredondado para 5px
          width = (int) Math.ceil(Double.parseDouble(fd.minWidth.replaceAll("[^0-9]*", "")) * maxWidth / 5);
        }

        // Caso o tamanho do componente seja maior que as colunas disponíveis, ele nunca caberá em nenhuma linha. Nesse caso vamos desrespeitar o tamanho mínimo do componente e espremer ele no espaço disponível
        if (width > cols) width = cols;

        // Vamos iterar todas as linhas em busca de um espaço vazio para encaixar esse componente
        for (int row = 0; row < rows; row++) {
          if (occupacy[row] + width <= cols) {
            // Se a ocupação atual, mais o espaço que precisamos ainda for menor que o número de colunas, procuramos o próximo lugar para colocar
            int col = 0;
            while (fieldsMap[row][col][0] != null)
              col++; // Loop com while de propósito, pois col aqui nunca deve estourar se a conta acima estiver correta. Se estourar é erro fudido de lógica!
            // Se encontrou, ocupamos a coluna e o tamanho mínimo
            fieldsMap[row][col][0] = fd;
            fieldsMap[row][col][1] = width;
            occupacy[row] += width;
            mainViewElected.add(fd.attributePath);
            break; // Quebra o for pq não precisamos procurar espaço na próxima linha para este componente.
          }
        }
      }

      // Agora que iteramos todos os componentes e já colocamos "o que deu" nas linhas, vamos tentar ampliar os componentes para que não fiquem expremidos enquanto sobra espaço na tela.
      for (int row = 0; row < rows; row++) {
        if (occupacy[row] == cols) continue; // Se essa linha já está com o máximo de colunas ocupadas, pulamos para a próxima linha
        for (int col = 0; col < cols; col++) {
          if (fieldsMap[row][col][0] == null) break; // Acabaram os components da linha, sai fora dessa linha.

          // Analiza o tamanho máximo e o tamanho disponível do componente encontrado
          FieldMetaData fd = (FieldMetaData) fieldsMap[row][col][0];
          int width = 0; // Em colunas
          if (fd.maxWidth.matches("[0-9]+px")) {
            width = (int) Math.ceil(Double.parseDouble(fd.maxWidth.replaceAll("[^0-9]*", "")) / 5);
          } else {
            // Se não é px então é % pq já foi validado na criação do FieldMetaData. Neste caso verificamos o percentual da tela arredondado para 5px
            width = (int) Math.ceil(Double.parseDouble(fd.maxWidth.replaceAll("[^0-9]*", "")) * maxWidth / 5);
          }
          width += 5; // Incluimos 5 na largura do componente pois é a margem que o vaadin inclui entre os componentes.

          // Calcula a possibilidade de crescimento deste componente
          final int possibleGrow = width - ((int) fieldsMap[row][col][1]);
          if (possibleGrow > 0) {
            // Se há possibilidade de crescimento deste componente verificamos o que é menor: a possibilidade de crescimento ou espaço de crescimento, e fazemos o componente crescer esse tanto
            int grow = Math.min(possibleGrow, cols - occupacy[row]);
            fieldsMap[row][col][1] = ((int) fieldsMap[row][col][1]) + grow;
            occupacy[row] += grow;
            if (occupacy[row] == cols) break; // Se chegamos no máximo de crescimento, aborta esse FOR para passar para a próxima linha
          }
        }
      }

      // Com o mapa criado, agora criamos o layout e colocamos os componentes nele
      final GridLayout gl = new GridLayout(cols, rows);
      gl.setSizeFull();
      gl.setMargin(true);
      gl.setSpacing(true);

      for (int row = 0; row < rows; row++) {
        int acuCol = 0; // Coluna acumulada do Grid
        // note que a variável col abaixo só incrementa em 1 por componente colocado no grid, e no grid precisamos de uma variável que conte as colunas do grid, que na verdade é soma de espaços já utilizados pelos componentes anteriores.
        // Exemplo: se temos 2 componentes que ocupam 2 colunas cada um. A var 'col' só terá os valores 0 e 1 que são suas posiões no array. Mas no Grid precisamos somar o tamanho do componente colocando eles na coluna 0 e 2
        for (int col = 0; col < cols; col++) {
          // if (fieldsMap[row][col][0] == null) break; // Acabaram os components da linha, sai fora dessa linha.
          if (acuCol >= cols) break; // Se já completamos o Grid, sai fora

          if (fieldsMap[row][col][0] == null) {
            final Label c = new Label("");
            gl.addComponent(c, acuCol, row);
            acuCol += 1;
          } else {
            int width = (int) fieldsMap[row][col][1];
            Component c = (Component) ((FieldMetaData) fieldsMap[row][col][0]).component;
            gl.addComponent(c, acuCol, row, acuCol + width - 1, row); // Subtraímos 1 da coluna final pq para o tamanho '1' a coluna deve ser a mesma e não a próxima.
            acuCol += width;
          }
        }
      }
      mainLayout.addComponent(gl);
      mainLayout.setExpandRatio(gl, 1f);
    }

    // Criamos um layout horizontal que incluirá o botão de search e clear
    HorizontalLayout hlButtons = new HorizontalLayout();
    hlButtons.setMargin(false);
    hlButtons.setSpacing(false);
    hlButtons.setSizeFull();

    RFWAssociativeComponent assBt = new RFWAssociativeComponent(null);

    // Atualiza o listener de Search para incluir o novo listener recebido.
    searchListener = new ClickListener() {
      private static final long serialVersionUID = 7206274795926771303L;

      @Override
      public void buttonClick(ClickEvent event) {
        // Se não for nulo, chamamos o listener recebido, se não chamamos o padrão
        if (listener != null) {
          listener.buttonClick(event);
        } else {
          try {
            applyMOOnGrid(dbProvider);
          } catch (RFWException e1) {
            TreatException.treat(e1);
          }
        }
      }
    };
    final Button searchButton = FWVad.createButton(ButtonType.SEARCH, searchListener);
    // hlButtons.addComponent(searchButton);
    // hlButtons.setExpandRatio(searchButton, 1f);
    assBt.addComponent(searchButton);

    final Button clearButton = FWVad.createButton(ButtonType.CLEARFIELDS, e -> UIFactory.this.clearMOFields());
    // clearButton.setSizeFull();
    // hlButtons.addComponent(clearButton);
    assBt.addComponent(clearButton);

    // Criamos agora o Vertical Layout que levará O HL dos botões acima, e for o caso levará também o botão de mais filtros gerado abaixo
    VerticalLayout vlButtons = new VerticalLayout();
    vlButtons.setWidth("220px");
    vlButtons.setMargin(true);
    vlButtons.setSpacing(false);
    // vlButtons.addComponent(hlButtons);
    vlButtons.addComponent(assBt);
    assBt.getLayout().setSizeUndefined();

    mainLayout.addComponent(vlButtons);
    mainLayout.setComponentAlignment(vlButtons, Alignment.MIDDLE_CENTER);

    // Consideramos agora o total de fields de MO existentes e o total que foi elegível para a primeira página para saber se temos de criar os campos extras
    boolean hasExtras = this.moFieldHash.size() > mainViewElected.size();

    if (hasExtras) {
      PopupButton moreFieldsBT = new PopupButton();
      // moreFields.setSizeFull();
      moreFieldsBT.setIcon(new ThemeResource("icon/more_24.png"));
      // vlButtons.addComponent(moreFields);
      // hlButtons.addComponent(moreFields);
      assBt.addComponent(moreFieldsBT);

      double maxWidth = FWVad.getCanvasWidth() - 5; // Retiramos as bordas do popuppanel
      int cols = Math.floorDiv((int) maxWidth, 5);

      // Como não sabemos quantas linhas vamos precisar para acomodar todos os componentes, criamos um array com o total de linhas de acordo com os componentes que faltam, como se fossemos colocar 1 por linha.
      rows = this.moFieldHash.size() - mainViewElected.size();
      Object[][][] fieldsMap = new Object[rows][cols][3];

      int[] occupacy = new int[rows];
      int realRows = 0; // Guarda quantas linhas de fato precisamos para criar o Grid no final
      {
        int row = 0;
        int col = 0;
        // Iteramos os componentes para ir alocando. A diferença do método anterior é que agora manteremos a ordem, se não couber vai para a próxima linha automaticamente, não "adiantamos" outros componentes mas sim deixaremos o espaço vazio a frente
        for (FieldMetaData fd : this.moFieldHash.values()) {
          if (!mainViewElected.contains(fd.attributePath)) {
            int width = 0; // Em colunas
            if (fd.minWidth.matches("[0-9]+px")) {
              width = (int) Math.ceil(Double.parseDouble(fd.minWidth.replaceAll("[^0-9]*", "")) / 5);
            } else {
              // Se não é px então é % pq já foi validado na criação do FieldMetaData. Neste caso verificamos o percentual da tela arredondado para 5px
              width = (int) Math.ceil(Double.parseDouble(fd.minWidth.replaceAll("[^0-9]*", "")) * maxWidth / 5);
            }
            width += 5; // Incluimos 5 na largura do componente pois é a margem que o vaadin inclui entre os componentes.

            // Caso o tamanho do componente seja maior que as colunas disponíveis, ele nunca caberá em nenhuma linha. Nesse caso vamos desrespeitar o tamanho mínimo do componente e espremer ele no espaço disponível
            if (width > cols) width = cols;

            if (occupacy[row] + width > cols) {
              // Quebramos para a próxima linha
              col = 0;
              row += 1;
            }

            while (fieldsMap[row][col][0] != null)
              col++; // Loop com while de propósito, pois col aqui nunca deve estourar se a conta acima estiver correta. Se estourar é erro fudido de lógica!

            // Colocamos este campo nesta posição
            fieldsMap[row][col][0] = fd;
            fieldsMap[row][col][1] = width;
            occupacy[row] += width;
          }
        }
        realRows = row;
      }

      // Agora que iteramos todos os componentes para tentar cresce-los no espaço disponível da linha
      for (int row = 0; row < rows; row++) {
        if (occupacy[row] == cols) continue; // Se essa linha já está com o máximo de colunas ocupadas, pulamos para a próxima linha
        for (int col = 0; col < cols; col++) {
          if (fieldsMap[row][col][0] == null) break; // Acabaram os components da linha, sai fora dessa linha.

          // Analiza o tamanho máximo e o tamanho disponível do componente encontrado
          FieldMetaData fd = (FieldMetaData) fieldsMap[row][col][0];
          int width = 0; // Em colunas
          if (fd.maxWidth.matches("[0-9]+px")) {
            width = (int) Math.ceil(Double.parseDouble(fd.maxWidth.replaceAll("[^0-9]*", "")) / 5);
          } else {
            // Se não é px então é % pq já foi validado na criação do FieldMetaData. Neste caso verificamos o percentual da tela arredondado para 5px
            width = (int) Math.ceil(Double.parseDouble(fd.maxWidth.replaceAll("[^0-9]*", "")) * maxWidth / 5);
          }
          // Calcula a possibilidade de crescimento deste componente
          final int possibleGrow = width - ((int) fieldsMap[row][col][1]);
          if (possibleGrow > 0) {
            // Se há possibilidade de crescimento deste componente verificamos o que é menor: a possibilidade de crescimento ou espaço de crescimento, e fazemos o componente crescer esse tanto
            int grow = Math.min(possibleGrow, cols - occupacy[row]);
            fieldsMap[row][col][1] = ((int) fieldsMap[row][col][1]) + grow;
            occupacy[row] += grow;
            if (occupacy[row] == cols) break; // Se chegamos no máximo de crescimento, aborta esse FOR para passar para a próxima linha
          }
        }
      }

      // Por fim, escrevemos o GridLayout que vai dentro do popup de mais opções
      final GridLayout gl = new GridLayout(cols, realRows + 1); // Somamos 1 pq em realRows está na verdade o indice da última coluna, e não o total de colunas
      gl.setWidth(maxWidth + "px");
      gl.setMargin(true);
      gl.setSpacing(true);
      moreFieldsBT.setContent(gl);

      for (int row = 0; row <= realRows; row++) {
        int acuCol = 0; // Coluna acumulada do Grid
        // note que a variável col abaixo só incrementa em 1 por componente colocado no grid, e no grid precisamos de uma variável que conte as colunas do grid, que na verdade é soma de espaços já utilizados pelos componentes anteriores.
        // Exemplo: se temos 2 componentes que ocupam 2 colunas cada um. A var 'col' só terá os valores 0 e 1 que são suas posiões no array. Mas no Grid precisamos somar o tamanho do componente colocando eles na coluna 0 e 2
        for (int col = 0; col < cols; col++) {
          // if (fieldsMap[row][col][0] == null) break; // Acabaram os components da linha, sai fora dessa linha.
          if (acuCol >= cols) break; // Se já completamos o Grid, sai fora

          if (fieldsMap[row][col][0] == null) {
            final Label c = new Label("");
            gl.addComponent(c, acuCol, row);
            acuCol += 1;
          } else {
            int width = (int) fieldsMap[row][col][1];
            Component c = (Component) ((FieldMetaData) fieldsMap[row][col][0]).component;
            gl.addComponent(c, acuCol, row, acuCol + width - 1, row); // Subtraímos 1 da coluna final pq para o tamanho '1' a coluna deve ser a mesma e não a próxima.
            acuCol += width;
          }
        }
      }
    }

    return mainLayout;

  }

  /**
   * Este método executa a mesma ação que o clique no botão de "Search" do painel de filtros. Note que se não tiver sido criado o layout do filtro ainda resultará em NullPointer.
   */
  public void doSearch() {
    this.searchListener.buttonClick(null);
  }

  /**
   * Coloca o foco no primeiro campo de busca do layout de Search. Resulta em nullpointer caso o não tenha sido criado nenhum campo de busca ainda.
   */
  public void focusFirstSearchField() {
    final HasValue<?> c = this.moFieldHash.entrySet().iterator().next().getValue().component;
    if (c instanceof Focusable) ((Focusable) c).focus();
  }

  /**
   * Coloca o foco no Grid de listagem (MOGrid). Resulta em nullpointer caso o não tenha sido criado nenhum campo de busca ainda.
   */
  public void focusMOGrid() {
    this.moGrid.focus();
  }

  /**
   * Limpa todos os campos criados par Filtro
   */
  public void clearMOFields() {
    // try {
    for (FieldMetaData metaData : this.moFieldHash.values()) {
      if (metaData.component instanceof RFWAssociativeComponent) {
        // Trata especial os RFWAssociativeComponent2, pq eles não tem o clear();
        for (Component component : ((RFWAssociativeComponent) metaData.component).getLayout()) {
          ((RFWButtonToggle<?>) component).setSelected(false);
        }
      } else {
        final HasValue<?> c = metaData.component;
        c.clear();
      }
    }
  }

  /**
   * Cria um grid para ser usado em conjunto com os campos de MO criado por esta Factory. Exibindo neste Grid os dados filtrados pelos campos de filtro.
   *
   * @return Component criado conforme atributos passados
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @throws RFWException
   */
  public Grid<GVO<VO>> createGridForMO(RFWDBProvider dbProvider) throws RFWException {
    return createGridForMOImp(null, null, null, null, null, null, dbProvider);
  }

  /**
   * Cria um grid para ser usado em conjunto com os campos de MO criado por esta Factory. Exibindo neste Grid os dados filtrados pelos campos de filtro.
   *
   * @param limitResults Caso definido, limita os resultados que são obtidos do banco de dados e exibidos no grid.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Grid<GVO<VO>> createGridForMO(Integer limitResults, RFWDBProvider dbProvider) throws RFWException {
    return createGridForMOImp(null, null, null, null, null, limitResults, dbProvider);
  }

  /**
   * Cria um grid para ser usado em conjunto com os campos de MO criado por esta Factory. Exibindo neste Grid os dados filtrados pelos campos de filtro.
   *
   * @param orderBy Objeto para definir a ordem inicial do Grid
   * @param attributes Lista de subatributos que precisam ser recuperados nos VOs do container também. Só usar quando o dado for exibido no GRID, se só precisar do dado para utilizar em outro objeto quando selecionado ou algo do tipo, buscar o objeto mais completo quando precisar para evitar um overload do Grid.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Grid<GVO<VO>> createGridForMO(RFWOrderBy orderBy, String[] attributes, RFWDBProvider dbProvider) throws RFWException {
    return createGridForMOImp(null, orderBy, attributes, null, null, null, dbProvider);
  }

  /**
   * Cria um grid para ser usado em conjunto com os campos de MO criado por esta Factory. Exibindo neste Grid os dados filtrados pelos campos de filtro.
   *
   * @param selectionMode Define o modo de seleção do Grid
   * @param orderBy Objeto para definir a ordem inicial do Grid
   * @param attributes Lista de subatributos que precisam ser recuperados nos VOs do container também. Só usar quando o dado for exibido no GRID, se só precisar do dado para utilizar em outro objeto quando selecionado ou algo do tipo, buscar o objeto mais completo quando precisar para evitar um overload do Grid.
   * @param doubleClickListener Listener para o caso de duplo click em algum item do Grid. Para obter o item que recebeu o duplo click utilize o método event.getItem().
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Grid<GVO<VO>> createGridForMO(SelectionMode selectionMode, RFWOrderBy orderBy, String[] attributes, RFWGridDoubleClickListener<GVO<VO>> doubleClickListener, RFWDBProvider dbProvider) throws RFWException {
    return createGridForMOImp(selectionMode, orderBy, attributes, doubleClickListener, null, null, dbProvider);
  }

  /**
   * Cria um grid para ser usado em conjunto com os campos de MO criado por esta Factory. Exibindo neste Grid os dados filtrados pelos campos de filtro.
   *
   * @param orderBy Objeto para definir a ordem inicial do Grid
   * @param attributes Lista de subatributos que precisam ser recuperados nos VOs do container também. Só usar quando o dado for exibido no GRID, se só precisar do dado para utilizar em outro objeto quando selecionado ou algo do tipo, buscar o objeto mais completo quando precisar para evitar um overload do Grid.
   * @param limitResults Caso definido, limita os resultados que são obtidos do banco de dados e exibidos no grid.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Grid<GVO<VO>> createGridForMO(RFWOrderBy orderBy, String[] attributes, Integer limitResults, RFWDBProvider dbProvider) throws RFWException {
    return createGridForMOImp(null, orderBy, attributes, null, null, limitResults, dbProvider);
  }

  /**
   * Cria um grid para ser usado em conjunto com os campos de MO criado por esta Factory. Exibindo neste Grid os dados filtrados pelos campos de filtro.
   *
   * @param selectionMode Define o modo de seleção do Grid
   * @param orderBy Objeto para definir a ordem inicial do Grid
   * @param attributes Lista de subatributos que precisam ser recuperados nos VOs do container também. Só usar quando o dado for exibido no GRID, se só precisar do dado para utilizar em outro objeto quando selecionado ou algo do tipo, buscar o objeto mais completo quando precisar para evitar um overload do Grid.
   * @param doubleClickListener Listener para o caso de duplo click em algum item do Grid. Para obter o item que recebeu o duplo click utilize o método event.getItem().
   * @param limitResults Caso definido, limita os resultados que são obtidos do banco de dados e exibidos no grid.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public Grid<GVO<VO>> createGridForMO(SelectionMode selectionMode, RFWOrderBy orderBy, String[] attributes, RFWGridDoubleClickListener<GVO<VO>> doubleClickListener, Integer limitResults, RFWDBProvider dbProvider) throws RFWException {
    return createGridForMOImp(selectionMode, orderBy, attributes, doubleClickListener, null, limitResults, dbProvider);
  }

  /**
   * Cria um grid para ser usado em conjunto com os campos de MO criado por esta Factory. Exibindo neste Grid os dados filtrados pelos campos de filtro.
   *
   * @param selectionMode Define o modo de seleção do Grid
   * @param orderBy Objeto para definir a ordem inicial do Grid
   * @param attributes Lista de subatributos que precisam ser recuperados nos VOs do container também. Só usar quando o dado for exibido no GRID, se só precisar do dado para utilizar em outro objeto quando selecionado ou algo do tipo, buscar o objeto mais completo quando precisar para evitar um overload do Grid.
   * @param doubleClickListener Listener para o caso de duplo click em algum item do Grid. Para obter o item que recebeu o duplo click utilize o método event.getItem().
   * @param provider Cria a MOGrid com um provider específico.
   * @param limitResults Caso definido, limita os resultados que são obtidos do banco de dados e exibidos no grid.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return
   * @throws RFWException
   */
  private Grid<GVO<VO>> createGridForMOImp(SelectionMode selectionMode, RFWOrderBy orderBy, String[] attributes, RFWGridDoubleClickListener<GVO<VO>> doubleClickListener, UIGridDataProvider<VO> provider, Integer limitResults, RFWDBProvider dbProvider) throws RFWException {
    if (selectionMode == null) selectionMode = SelectionMode.MULTI;
    if (provider == null) provider = new UIGridDataProvider<>(this.voClass, this.createRFWMO(), orderBy, attributes, dbProvider);

    if (limitResults != null) provider.setLimitResults(limitResults);

    moGrid = new Grid<>();
    setUpGrid(moGrid, selectionMode, true, doubleClickListener, provider);

    return moGrid;
  }

  /**
   * Cria um grid para ser usado em conjunto com os campos de MO criado por esta Factory. Exibindo neste Grid os dados filtrados pelos campos de filtro.
   *
   * @param parentAttribute nome do atributo que identifica o pai do objeto na Hierarquia.
   * @param orderBy utilizado para ordenar a lista inicialmente.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   *
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public TreeGrid<GVO<VO>> createTreeGridForMO(String parentAttribute, RFWOrderBy orderBy, RFWDBProvider dbProvider) throws RFWException {
    return createTreeGridForMO(SelectionMode.MULTI, orderBy, null, parentAttribute, dbProvider);
  }

  /**
   * Cria um grid de "árvore" para ser usado em conjunto com os campos de MO criado por esta Factory. Exibindo neste Grid os dados filtrados pelos campos de filtro.
   *
   * @param selectionMode Define o modo de seleção do Grid
   * @param orderBy Objeto para definir a ordem inicial do Grid
   * @param doubleClickListener Listener para o caso de duplo click em algum item do Grid. Para obter o item que recebeu o duplo click utilize o método event.getItem().
   * @param parentAttribute nome do atributo que identifica o pai do objeto na Hierarquia.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return Component criado conforme atributos passados
   * @throws RFWException
   */
  public TreeGrid<GVO<VO>> createTreeGridForMO(SelectionMode selectionMode, RFWOrderBy orderBy, RFWGridDoubleClickListener<GVO<VO>> doubleClickListener, String parentAttribute, RFWDBProvider dbProvider) throws RFWException {
    moGrid = new TreeGrid<>();
    UITreeDataProvider<VO> provider = new UITreeDataProvider<>(this.voClass, parentAttribute, orderBy, dbProvider);
    setUpGrid(moGrid, selectionMode, true, doubleClickListener, provider);
    moGrid.setStyleGenerator(new UIGridRowHighlight<VO>());
    return (TreeGrid<GVO<VO>>) moGrid;
  }

  /**
   * Método de configuração do Grid/TreeGrid usada por padrão no RFW. Define as configurações que são utilizadas por padrão no RFW. Abstraímos o método de configuração da Grid para que tenhamos apenas um só, assim as definições comuns para a Grid e a TreeGrid ficam centralizadas.
   *
   * @param grid Grid/TreeGrid a ser configurada
   * @param selectionMode Define o modo de seleção do Grid
   * @param columnReorderingAllowed indica se o usuário pode reordenar as colunas do Grid arrastando pelo cabeçalho
   * @param doubleClickListener Listener para o caso de duplo click em algum item do Grid. Para obter o item que recebeu o duplo click utilize o método event.getItem(). Se passado nulo, nada é configurado.
   * @param provider Provider de dados para ser exibido na Grid Se passado nulo, nenhuma alteração é feita.
   * @throws RFWException
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static void setUpGrid(Grid grid, SelectionMode selectionMode, Boolean columnReorderingAllowed, RFWGridDoubleClickListener doubleClickListener, DataProvider provider) throws RFWException {
    Objects.requireNonNull(grid, "É obrigatório passar o Grid");
    Objects.requireNonNull(selectionMode, "É obrigatório definir o modo de seleção do Grid");

    grid.setSizeFull();
    grid.removeAllColumns(); // O Grid adiciona todas as colunas do VO automaticamente, nós removemos para que sejam adicionadas somente as que queremos e formatadas conforme queremos
    grid.setColumnReorderingAllowed(columnReorderingAllowed);

    grid.setSelectionMode(selectionMode);

    if (grid.getSelectionModel() instanceof MultiSelectionModel) {
      ((MultiSelectionModel) grid.getSelectionModel()).setSelectAllCheckBoxVisibility(SelectAllCheckBoxVisibility.VISIBLE);
    }

    if (provider != null) grid.setDataProvider(provider);

    final DataProvider prv = grid.getDataProvider();
    if (prv != null) {
      // Toda vez que o conteúdo do provider for atualizado, temos de verificar os objeto selecionados e garantir se eles ainda estão no DataProvider. Caso contrário o Grid manterá uma seleção de um objeto que não está mais nele
      if (prv instanceof UIGridDataProvider) {
        prv.addDataProviderListener(e -> {
          final Set sel = grid.getSelectedItems();
          if (sel != null) {
            UIGridDataProvider p = (UIGridDataProvider) prv;
            for (Object obj : new ArrayList<>(sel)) {
              Long id = null;
              if (obj instanceof GVO) {
                id = ((GVO) obj).getVO().getId();
              } else {
                id = ((RFWVO) obj).getId();
              }
              if (!p.contains(id)) {
                grid.deselect(obj);
              } else {
                grid.select(obj);
              }
            }
          }
        });
      } else if (prv instanceof UITreeDataProvider) {
        prv.addDataProviderListener(e -> {
          try {
            final Set sel = grid.getSelectedItems();
            if (sel != null) {
              UITreeDataProvider p = (UITreeDataProvider) prv;
              for (Object obj : new ArrayList<>(sel)) {
                Long id = null;
                if (obj instanceof GVO) {
                  id = ((GVO) obj).getVO().getId();
                } else {
                  id = ((RFWVO) obj).getId();
                }
                if (!p.contains(id)) {
                  grid.deselect(obj);
                } else {
                  grid.select(obj);
                }
              }
            }
          } catch (RFWException e1) {
            throw new RFWRunTimeException(e1);
          }
        });
      } else if (prv instanceof ListDataProvider) {
        // Não faz nada, só não deixa cair no ELSE
        // Quando está com esse provider não estamos fazendo nada.... (por enquanto? rs)
      } else {
        TreatException.treat(new RFWCriticalException("Provider não suportado para o Grid! '" + prv.getClass().getCanonicalName() + "'."));
      }
    }

    if (doubleClickListener != null) UIFactory.addDoubleClickListenerToGrid(grid, doubleClickListener);
  }

  public void addDoubleClickListenerToMOGrid(RFWGridDoubleClickListener<GVO<VO>> listener) throws RFWException {
    Objects.requireNonNull(moGrid, "Não há um MO Grid criado para adicionar o Listener de Duplo Click!");
    UIFactory.addDoubleClickListenerToGrid(moGrid, listener);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static void addDoubleClickListenerToGrid(Grid grid, RFWGridDoubleClickListener listener) throws RFWException {
    Objects.requireNonNull(listener, "Não é possível adicionar um listener nulo!");
    Objects.requireNonNull(grid, "O Grid não pode ser nulo!");
    // Incluir o Listener de Duplo click
    grid.addItemClickListener(e -> {
      try {
        if (e.getMouseEventDetails().isDoubleClick()) {
          // Se for um duplo click, limpamos a seleção que acontece no Browser
          // function clearSelection() {
          // if(document.selection && document.selection.empty) {
          // document.selection.empty();
          // } else if(window.getSelection) {
          // window.getSelection().removeAllRanges();
          // }
          // }
          // clearSelection();
          JavaScript.eval("function clearSelection() {if(document.selection && document.selection.empty) {document.selection.empty();} else if(window.getSelection) {window.getSelection().removeAllRanges();}}clearSelection();");
          // econfirmamos esse objeto
          listener.eventDoubleClick(e);
        }
      } catch (Throwable e1) {
        TreatException.treat(e1);
      }
    });
  }

  /**
   * Cria Colunas no Grid salvo para ser utilizado com os campos de MO já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @return Coluna criada
   * @throws RFWException
   */
  public Column<GVO<VO>, Object> addGridForMOColumn(String attribute) throws RFWException {
    return UIFactory.addGridColumn(this.voClass, this.moGrid, "VO." + attribute, false, false, null, null);
  }

  /**
   * Cria Colunas no Grid salvo para ser utilizado com os campos de MO já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param fieldAlignment Define o alinhamento da coluna. Se passado NULL, a coluna será alinhada a Esquerda (Alinhamento padrão do GRID).
   * @return Coluna criada
   * @throws RFWException
   */
  public Column<GVO<VO>, Object> addGridForMOColumn(String attribute, FieldAlignment fieldAlignment) throws RFWException {
    return UIFactory.addGridColumn(this.voClass, this.moGrid, "VO." + attribute, false, false, null, fieldAlignment);
  }

  /**
   * Cria Colunas no Grid salvo para ser utilizado com os campos de MO já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @param fieldAlignment Define o alinhamento da coluna. Se passado NULL, a coluna será alinhada a Esquerda (Alinhamento padrão do GRID).
   * @return Coluna criada
   * @throws RFWException
   */
  public <FIELDTYPE, BEANTYPE> Column<GVO<VO>, Object> addGridForMOColumn(String attribute, RFWDataFormatter<FIELDTYPE, BEANTYPE> df, FieldAlignment fieldAlignment) throws RFWException {
    return UIFactory.addGridColumn(this.voClass, this.moGrid, "VO." + attribute, false, false, df, fieldAlignment);
  }

  /**
   * Cria Colunas no Grid salvo para ser utilizado com os campos de MO já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @return Coluna criada
   * @throws RFWException
   */
  public <FIELDTYPE, BEANTYPE> Column<GVO<VO>, Object> addGridForMOColumn(String attribute, RFWDataFormatter<FIELDTYPE, BEANTYPE> df) throws RFWException {
    return UIFactory.addGridColumn(this.voClass, this.moGrid, "VO." + attribute, false, false, df, null);
  }

  /**
   * Cria Colunas no Grid salvo para ser utilizado com os campos de MO já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param hidable Define se é possível "esconder" a coluna (true) ou não (false).
   * @param hidden Define se a coluna deve estar escondida/collapsed (true), ou não (false).
   * @return Coluna criada
   * @throws RFWException
   */
  public Column<GVO<VO>, Object> addGridForMOColumn(String attribute, boolean hidable, boolean hidden) throws RFWException {
    return UIFactory.addGridColumn(this.voClass, this.moGrid, "VO." + attribute, hidable, hidden, null, null);
  }

  /**
   * Cria Colunas no Grid salvo para ser utilizado com os campos de MO já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param hidable Define se é possível "esconder" a coluna (true) ou não (false).
   * @param hidden Define se a coluna deve estar escondida/collapsed (true), ou não (false).
   * @param fieldAlignment Define o alinhamento da coluna. Se passado NULL, a coluna será alinhada a Esquerda (Alinhamento padrão do GRID).
   * @return Coluna criada
   * @throws RFWException
   */
  public Column<GVO<VO>, Object> addGridForMOColumn(String attribute, boolean hidable, boolean hidden, FieldAlignment fieldAlignment) throws RFWException {
    return UIFactory.addGridColumn(this.voClass, this.moGrid, "VO." + attribute, hidable, hidden, null, fieldAlignment);
  }

  /**
   * Cria Colunas no Grid salvo para ser utilizado com os campos de MO já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param hidable Define se é possível "esconder" a coluna (true) ou não (false).
   * @param hidden Define se a coluna deve estar escondida/collapsed (true), ou não (false).
   * @param fieldAlignment Define o alinhamento da coluna. Se passado NULL, a coluna será alinhada a Esquerda (Alinhamento padrão do GRID).
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @return Coluna criada
   * @throws RFWException
   */
  public <FIELDTYPE, BEANTYPE> Column<GVO<VO>, Object> addGridForMOColumn(String attribute, boolean hidable, boolean hidden, FieldAlignment fieldAlignment, RFWDataFormatter<FIELDTYPE, BEANTYPE> df) throws RFWException {
    return UIFactory.addGridColumn(this.voClass, this.moGrid, "VO." + attribute, hidable, hidden, df, fieldAlignment);
  }

  /**
   * Cria Coluna em um Grid que utiliza o {@link GVO} já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @return Coluna criada
   * @throws RFWException
   */
  public static <VO extends RFWVO> Column<GVO<VO>, Object> addGridGVOColumn(Class<VO> voClass, Grid<GVO<VO>> grid, String attribute) throws RFWException {
    return UIFactory.addGridColumn(voClass, grid, "VO." + attribute, false, false, null, null);
  }

  /**
   * Cria Coluna em um Grid qualquer já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param hidable Define se é possível "esconder" a coluna (true) ou não (false).
   * @param hidden Define se a coluna deve estar escondida/collapsed (true), ou não (false).
   * @return Coluna criada
   * @throws RFWException
   */
  public static <VO extends RFWVO> Column<GVO<VO>, Object> addGridGVOColumn(Class<VO> voClass, Grid<GVO<VO>> grid, String attribute, boolean hidable, boolean hidden) throws RFWException {
    return addGridColumn(voClass, grid, "VO." + attribute, hidable, hidden, null, null);
  }

  /**
   * Cria Coluna em um Grid qualquer já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param hidable Define se é possível "esconder" a coluna (true) ou não (false).
   * @param hidden Define se a coluna deve estar escondida/collapsed (true), ou não (false).
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @param fieldAlignment Define o alinhamento da coluna. Se passado NULL, a coluna será alinhada a Esquerda (Alinhamento padrão do GRID).
   * @return Coluna criada
   * @throws RFWException
   */
  public static <VO extends RFWVO, FIELDTYPE, BEANTYPE> Column<GVO<VO>, Object> addGridGVOColumn(Class<VO> voClass, Grid<GVO<VO>> grid, String attribute, boolean hidable, boolean hidden, RFWDataFormatter<FIELDTYPE, BEANTYPE> df, FieldAlignment fieldAlignment) throws RFWException {
    return addGridColumnImp(voClass, grid, "VO." + attribute, hidable, hidden, df, fieldAlignment, null);
  }

  /**
   * Cria Coluna em um Grid qualquer que não utilize a classe GVO, já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @return Coluna criada
   * @throws RFWException
   */
  public static <VO extends RFWVO, BEAN> Column<BEAN, Object> addGridColumn(Class<VO> voClass, Grid<BEAN> grid, String attribute) throws RFWException {
    return addGridColumnImp(voClass, grid, attribute, false, false, null, null, null);
  }

  /**
   * Cria Coluna em um Grid qualquer que não utilize a classe GVO, já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param fieldAlignment Define o alinhamento da coluna. Se passado NULL, a coluna será alinhada a Esquerda (Alinhamento padrão do GRID).
   * @return Coluna criada
   * @throws RFWException
   */
  public static <VO extends RFWVO, BEAN> Column<BEAN, Object> addGridColumn(Class<VO> voClass, Grid<BEAN> grid, String attribute, FieldAlignment fieldAlignment) throws RFWException {
    return addGridColumnImp(voClass, grid, attribute, false, false, null, fieldAlignment, null);
  }

  /**
   * Cria Coluna em um Grid qualquer já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param voClass Class do VO que é objeto de listagem do GRID
   * @param grid Grid criado em que será adicionado a coluna editável.
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param hidable Define se é possível "esconder" a coluna (true) ou não (false).
   * @param hidden Define se a coluna deve estar escondida/collapsed (true), ou não (false).
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @param fieldAlignment Define o alinhamento da coluna. Se passado NULL, a coluna será alinhada a Esquerda (Alinhamento padrão do GRID).
   * @return Coluna criada
   * @throws RFWException
   */
  public static <VO extends RFWVO, BEAN, FIELDTYPE, BEANTYPE> Column<BEAN, Object> addGridColumn(Class<VO> voClass, Grid<BEAN> grid, final String attribute, boolean hidable, boolean hidden, RFWDataFormatter<FIELDTYPE, BEANTYPE> df, FieldAlignment fieldAlignment) throws RFWException {
    return addGridColumnImp(voClass, grid, attribute, hidable, hidden, df, fieldAlignment, null);
  }

  /**
   * Cria Coluna em um Grid qualquer já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param voClass Class do VO que é objeto de listagem do GRID
   * @param grid Grid criado em que será adicionado a coluna editável.
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param hidable Define se é possível "esconder" a coluna (true) ou não (false).
   * @param hidden Define se a coluna deve estar escondida/collapsed (true), ou não (false).
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @param fieldAlignment Define o alinhamento da coluna. Se passado NULL, a coluna será alinhada a Esquerda (Alinhamento padrão do GRID).
   * @return Coluna criada
   * @throws RFWException
   */
  public static <VO extends RFWVO, BEAN, FIELDTYPE, BEANTYPE> Column<BEAN, Object> addGridColumn(Class<VO> voClass, Grid<BEAN> grid, final String attribute, boolean hidable, boolean hidden, RFWDataFormatter<FIELDTYPE, BEANTYPE> df, FieldAlignment fieldAlignment, ValueProvider<BEAN, Object> provider) throws RFWException {
    return addGridColumnImp(voClass, grid, attribute, hidable, hidden, df, fieldAlignment, provider);
  }

  /**
   * Cria Coluna em um Grid qualquer já configurando os conversores de acordo com o tipo de informação do Bean.
   *
   * @param voClass Class do VO que é objeto de listagem do GRID
   * @param grid Grid criado em que será adicionado a coluna editável.
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param hidable Define se é possível "esconder" a coluna (true) ou não (false).
   * @param hidden Define se a coluna deve estar escondida/collapsed (true), ou não (false).
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @param fieldAlignment Define o alinhamento da coluna. Se passado NULL, a coluna será alinhada a Esquerda (Alinhamento padrão do GRID).
   * @return Coluna criada
   * @throws RFWException
   */
  @SuppressWarnings({ "unchecked" })
  private static <VO extends RFWVO, BEAN, FIELDTYPE, BEANTYPE> Column<BEAN, Object> addGridColumnImp(Class<VO> voClass, Grid<BEAN> grid, final String attribute, boolean hidable, boolean hidden, RFWDataFormatter<FIELDTYPE, BEANTYPE> df, FieldAlignment fieldAlignment, ValueProvider<BEAN, Object> provider) throws RFWException {
    // Por conta do encapsulamento do GVO, precisamos pular o prefixo "VO." para encontrar corretamente os dados dentro do VO quando procuramos pela classe e não pelo Objeto. Por isso criamos o cleanAttribute, que contém o conteúdo do attribute, mas sem o VO. caso ele exista
    final String cleanAttribute;
    if (attribute.startsWith("VO.")) {
      cleanAttribute = attribute.substring(3);
    } else {
      cleanAttribute = attribute;
    }

    if (provider == null) {
      provider = new ValueProvider<BEAN, Object>() {
        private static final long serialVersionUID = -763897904372350924L;

        @Override
        public Object apply(BEAN bean) {
          try {
            return RUReflex.getPropertyValue(bean, attribute);
          } catch (RFWException e) {
            throw new RFWRunTimeException("Falha ao obter o valor da propriedade no BEAN! Attribute: " + attribute);
          }
        }
      };
    }

    final Column<BEAN, Object> column = grid.addColumn(provider);
    String caption = RUReflex.getRFWMetaAnnotationCaption(voClass, cleanAttribute);
    if (caption == null) caption = cleanAttribute;
    column.setCaption(caption);
    column.setSortProperty(attribute);
    column.setHidable(hidable);
    column.setHidden(hidden);
    column.setId(attribute); // Colocamos o nome da coluna como ID para conseguir recuperar essa coluna posteriormente, como no método de adicionar rodapé

    if (df == null) df = getDataFormatterByRFWMetaAnnotation(voClass, cleanAttribute);
    if (df != null) {
      final RFWDataFormatter<Object, Object> dff = (RFWDataFormatter<Object, Object>) df;
      column.setRenderer(value -> {
        try {
          return dff.toPresentation(value, RFWUI.getLocaleCurrent());
        } catch (RFWException e) {
          RFWLogger.logException(e); // Só faz o log do problema e retornamos o valor sem formatação. Isso pq esse código será chamado apra cada linha da tabela, em caso de erro teremos um caralhão de mensagens de erro na tela.
          return value;
        }
      }, new TextRenderer());
    }

    if (df == null) {
      Annotation ann = RUReflex.getRFWMetaAnnotation(voClass, cleanAttribute);
      if (ann != null) {
        // Verifica o tipo de RFWMeta para criar o campo adequado
        if (ann instanceof RFWMetaEnumField) {
          column.setRenderer(value -> {
            if (value == null) return "";
            return RFWBundle.get((Enum<?>) value);
          }, new TextRenderer());
        } else if (ann instanceof RFWMetaBooleanField) {
          column.setRenderer(value -> {
            if (value == null) return "";
            if (Boolean.TRUE.equals(value)) {
              return ((RFWMetaBooleanField) ann).trueCaption();
            } else {
              return ((RFWMetaBooleanField) ann).falseCaption();
            }
          }, new TextRenderer());
        }
      }
    }

    if (fieldAlignment != null) {
      switch (fieldAlignment) {
        case CENTER:
          setGridColumnAlignCenter(column);
          break;
        case RIGHT:
          setGridColumnAlignRight(column);
          break;
        case LEFT:
          // ALinhamento padrão, se não tiver os outros estilos já fica a esquerda.
          break;
      }
    }

    return column;

  }

  /**
   * Alinha uma coluna do Grid à Direita.
   *
   * @param column Coluna do Grid para alinhamento.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static void setGridColumnAlignRight(final Column column) {
    column.setStyleGenerator(STYLEGENERATOR_ALIGNRIGHT);
  }

  /**
   * Alinha uma coluna do Grid no Centro.
   *
   * @param column Coluna do Grid para alinhamento.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static void setGridColumnAlignCenter(final Column column) {
    column.setStyleGenerator(STYLEGENERATOR_ALIGNCENTER);
  }

  /**
   * Cria Coluna em um Grid qualquer já configurando os conversores de acordo com o tipo de informação do Bean. E criando um componente e um Binder.<br>
   * NOTE que o grid só se torna editável se for definido como editável. Para isso utilize o método {@link #setupGridEditor(Grid)}.
   *
   * @param voClass Class do VO que é objeto de listagem do GRID
   * @param grid Grid criado em que será adicionado a coluna editável.
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @return coluna adicionada do GRID
   * @throws RFWException
   */
  public static <VO extends RFWVO, BEAN, FIELDTYPE, BEANTYPE> Column<BEAN, Object> addGridEditableColumn(Class<VO> voClass, Grid<BEAN> grid, final String attribute) throws RFWException {
    return addGridEditableColumn(voClass, grid, attribute, false, false, null, null, null, null);
  }

  /**
   * Cria Coluna em um Grid qualquer já configurando os conversores de acordo com o tipo de informação do Bean. E criando um componente e um Binder.<br>
   * NOTE que o grid só se torna editável se for definido como editável. Para isso utilize o método {@link #setupGridEditor(Grid)}.
   *
   * @param voClass Class do VO que é objeto de listagem do GRID
   * @param grid Grid criado em que será adicionado a coluna editável.
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param fieldAlignment Define o alinhamento da coluna. Se passado NULL, a coluna será alinhada a Esquerda (Alinhamento padrão do GRID).
   *
   * @return coluna adicionada do GRID
   * @throws RFWException
   */
  public static <VO extends RFWVO, BEAN, FIELDTYPE, BEANTYPE> Column<BEAN, Object> addGridEditableColumn(Class<VO> voClass, Grid<BEAN> grid, final String attribute, FieldAlignment fieldAlignment) throws RFWException {
    return addGridEditableColumn(voClass, grid, attribute, false, false, null, fieldAlignment, null, null);
  }

  /**
   * Cria Coluna em um Grid qualquer já configurando os conversores de acordo com o tipo de informação do Bean. E criando um componente e um Binder.<br>
   * NOTE que o grid só se torna editável se for definido como editável. Para isso utilize o método {@link #setupGridEditor(Grid)}.
   *
   * @param voClass Class do VO que é objeto de listagem do GRID
   * @param grid Grid criado em que será adicionado a coluna editável.
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param hidable Define se é possível "esconder" a coluna (true) ou não (false).
   * @param hidden Define se a coluna deve estar escondida/collapsed (true), ou não (false).
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @param fieldAlignment Define o alinhamento da coluna. Se passado NULL, a coluna será alinhada a Esquerda (Alinhamento padrão do GRID).
   * @param field Permite passar o campo que será utilizado para editar as linhas do Grid. Se for passado null o UIFactory cria um campo com base nos padrões do UIFactory para o atributo do VO.
   * @return coluna adicionada do GRID
   * @throws RFWException
   */
  public static <VO extends RFWVO, BEAN, FIELDTYPE, BEANTYPE> Column<BEAN, Object> addGridEditableColumn(Class<VO> voClass, Grid<BEAN> grid, final String attribute, boolean hidable, boolean hidden, RFWDataFormatter<FIELDTYPE, BEANTYPE> df, FieldAlignment fieldAlignment, HasValue<FIELDTYPE> field, FIELDTYPE nullValue) throws RFWException {
    Column<BEAN, Object> column = addGridColumn(voClass, grid, attribute, hidable, hidden, df, fieldAlignment);
    setGridColumnEditable(column, voClass, grid, attribute, df, fieldAlignment, field, nullValue);
    return column;
  }

  /**
   * Cria Coluna em um Grid qualquer já configurando os conversores de acordo com o tipo de informação do Bean. E criando um componente e um Binder.<br>
   * NOTE que o grid só se torna editável se for definido como editável. Para isso utilize o método {@link #setupGridEditor(Grid)}.
   *
   * @param column Coluna do GRID para ser configurada para edição.
   * @param attribute Coluna (propriedade do VO) a ser adicionada no grid. Aceita atributos "nestead" separados por '.'. Não acieta a notação do rfw para listas ou hash com '[]'.
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @param fieldAlignment Define o alinhamento da coluna. Se passado NULL, a coluna será alinhada a Esquerda (Alinhamento padrão do GRID).
   * @param field Permite passar o campo que será utilizado para editar as linhas do Grid. Se for passado null o UIFactory cria um campo com base nos padrões do UIFactory para o atributo do VO.
   * @param nullValue Valor a ser exibido no componente que representa o valor NULL no VO.
   * @throws RFWException
   */
  @SuppressWarnings("unchecked")
  public static <VO extends RFWVO, BEAN, FIELDTYPE, BEANTYPE> void setGridColumnEditable(Column<BEAN, Object> column, Class<VO> voClass, Grid<BEAN> grid, final String attribute, RFWDataFormatter<FIELDTYPE, BEANTYPE> df, FieldAlignment fieldAlignment, HasValue<FIELDTYPE> field, FIELDTYPE nullValue) throws RFWException {
    if (df == null) df = getDataFormatterByRFWMetaAnnotation(voClass, attribute);
    if (field == null) field = createField(voClass, attribute, null, null, fieldAlignment, null, null, null, null, null, df, null);
    if (nullValue == null && field instanceof AbstractTextField) {
      nullValue = (FIELDTYPE) ""; // Nos campos do tipo TextField o valor de "nulo" no VO, é representado por uma String vazia no componente
    }

    final Binder<BEAN> binder = grid.getEditor().getBinder();

    // Vamos utilizar os métodos estáticos do Binder do UIFactory (utilizado para ligar os campos nos VOs), Não podemos instanciar pq o UIBinder se associa à um único VO, e no Grid, o mesmo campo é utilizado para editar objetos diferentes conforme a linha entra em edição.
    final HasValue<FIELDTYPE> ffield = field;
    final FIELDTYPE fNullValue = nullValue;

    RFWDataFormatter<FIELDTYPE, BEANTYPE> ddf = df;
    final Binding<BEAN, FIELDTYPE> fieldBinding = binder.bind(field, vo -> {
      try {
        return UIBinder.toPresentation(vo, attribute, ffield, ddf, fNullValue, false);
      } catch (RFWException e) {
        // não trata a exception pe o método toPresentation já colocou no componente ou chamou o tratamento
        return null;
      }
    }, (vo, value) -> {
      UIBinder.toVO(vo, attribute, ffield, ddf, fNullValue, false);
    });
    column.setEditorBinding(fieldBinding);
  }

  /**
   * Este método cria o MO a partir de todos os campos de MO registrados na Factory e aplica o MO no Grid, se existir um Grid criado para o MO.
   *
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @throws RFWException
   */
  @SuppressWarnings("unchecked")
  public void applyMOOnGrid(RFWDBProvider dbProvider) throws RFWException {
    if (this.moGrid == null) throw new RFWCriticalException("Não há nenhum GRID para MO criado na UIFactory!");

    RFWMO mo = createRFWMO();

    final DataProvider<GVO<VO>, ?> provider = this.moGrid.getDataProvider();
    if (provider instanceof UIGridDataProvider) {
      ((UIGridDataProvider<VO>) provider).setRfwMO(mo);
    } else if (provider instanceof UITreeDataProvider) {
      final UIGridRowHighlight<VO> sg = (UIGridRowHighlight<VO>) this.moGrid.getStyleGenerator();
      sg.clear();
      // Verifica se temos alguma condição no MO, se não tiver, não buscmaos nada pois retornará TUDO e tudo ficará marcado
      List<Long> ids = null;
      if (mo.getAttributes().size() > 0) {
        // Faz a busca dos IDs de resultado
        ids = dbProvider.findIDs(this.voClass, mo, null);
        sg.add(ids);
      }
      provider.refreshAll(); // Força a releitura dos dados

      // Não fechamos todos os itens do GRID pois o usuário pode estar com alguns itens já expandidos e não queremos estragar isso, mas expandimos todos os itens resultado da busca para que fiquem visíveis
      if (ids != null) {
        for (Long id : ids) { // Itera o resultado do filtro
          expandTreeGridRow(this.voClass, (TreeGrid<GVO<VO>>) moGrid, id);
        }
      }
    } else {
      throw new RFWCriticalException("O Provider do Grid do MO não é suportado pelo UIFactory: '" + provider.getClass().getCanonicalName() + "'!");
    }
  }

  /**
   * Este método é capaz de expandir as linhas de uma TreeGrid até o objeto desejado passando apenas o ID.<br>
   * Note que este método só funciona com o Provider {@link UITreeDataProvider}.
   *
   * @param voClass Classe o VO utilizado dentro do {@link TreeGrid}
   * @param grid {@link TreeGrid} para expandir.
   * @param id ID do objeto procurado.
   * @throws RFWException
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <VO extends RFWVO> void expandTreeGridRow(final Class<VO> voClass, final TreeGrid<GVO<VO>> grid, Long id) throws RFWException {
    try {
      // Recuperamos o caminho até cada um dos objetos encontrados
      final List<Long> pathIDs = ((UITreeDataProvider) grid.getDataProvider()).getParentPath(id);
      // Iteramos o caminho, e mandamos expandir cada um dos itens. Note que o Grid utiliza o objeto ItemCategoryVO e não o ID como referência para saber qual é o objeto. Por trás ele utilizará o .equals() do RFWVO para identificar o objeto. O mesmo só verifica o ID para dizer se é "equals" ou não.
      int count = 0;
      final LinkedList<GVO<VO>> expandList = new LinkedList<>();
      for (Long pID : pathIDs) {
        if (count++ < pathIDs.size() - 1) { // Evita o último objeto, pois ele não é preciso expandir, apenas ser exibido
          final VO t = voClass.newInstance();
          t.setId(pID);
          expandList.add(new GVO<>(t));
        }
      }
      grid.expand(expandList);
    } catch (InstantiationException e) {
      throw new RFWCriticalException(e);
    } catch (IllegalAccessException e) {
      throw new RFWCriticalException(e);
    }
  }

  /**
   * Gets the MO grid.
   *
   * @return the MO grid
   */
  public Grid<GVO<VO>> getMOGrid() {
    return moGrid;
  }

  /**
   * Gets the vo.
   *
   * @return the vo
   */
  public VO getVO() {
    return vo;
  }

  /**
   * Sets the vo.
   *
   * @param vo the new vo
   */
  public void setVO(VO vo) {
    this.vo = vo;
    this.bindingMap.clear();
    this.voFieldHash.clear();
  }

  /**
   * Faz a cópia dos dados do VO para todos os Fields que tem Bind.<br>
   * Embora o UIFactory não tenha cache, as alterações feitas no VO não são repercurtidas automaticamente nos Fields por falta de um listener de 'valueChange' no VO. Neste casos este método torna-se útil.
   */
  public void reloadVOField_All() {
    for (UIBinder<?, ?, VO> binder : this.bindingMap.values()) {
      binder.toPresentation();
    }
  }

  /**
   * Adiciona um listener de Tecla de Atalho um coponente.
   *
   * @param listener
   * @param keyCode
   * @param keyModifier
   */
  public static void addShortcut(AbstractComponent component, RFWShortcutListener listener, int keyCode, int... keyModifier) {
    final ShortcutListener sl = new ShortcutListener(null, null, keyCode, keyModifier) {
      private static final long serialVersionUID = -7557192135160411714L;

      @Override
      public void handleAction(Object sender, Object target) {
        listener.shortCutPressed(component, sender, target, keyCode, keyModifier);
      }
    };
    component.addShortcutListener(sl);
  }

  /**
   * Recupera o campo que foi gerado para um determinado atributo através dos métodos createVOField* ou que tiveram o BIND realizado com este {@link UIFactory}
   *
   * @param attribute Atributo do VO usado na geração do campo.
   * @return O Component criado ou NULL caso ele não seja encontrado.
   */
  public HasValue<?> getVOField(String attribute) {
    return this.voFieldHash.get(attribute);
  }

  /**
   * Recupera o campo que foi gerado para um determinado atributo através do método {@link #createMOField(String)}.
   *
   * @param attribute Atributo do VO usado na geração do campo.
   * @return O Component criado ou NULL caso ele não seja encontrado.
   */
  public HasValue<?> getMOField(String attribute) {
    return this.moFieldHash.get(attribute).component;
  }

  /**
   * Recupera o valor de um campo do MO. O valor obtido já é convertido do "field" para o "VO", ou seja, deve ter a mesma classe que o atributo do VO têm, ou similar conforme o {@link RFWDataFormatter} aplicado.
   *
   * @param attribute Nome do atributo utilizado na criação do campo do MO.
   * @return Valor existente no field, ou NULL caso o valor não esteja preenchido.
   * @throws RFWException em caso de falha. Emite uma RFWException com o código "BIS10_000013" em caso de o componente ser um {@link RFWAssociativeComponent} que não permite a extração direta do valor.
   */
  public Object getMOFieldValue(String attribute) throws RFWException {
    final FieldMetaData fieldMetaData = this.moFieldHash.get(attribute);

    if (fieldMetaData.component instanceof RFWAssociativeComponent) {
      // Não é capaz de obter o valor do RFWAssociative, emitimos uma exception identificada para que o erro possa ser tratado e reconhecido em outras partes do sistema.
      throw new RFWCriticalException("BIS10_000013");
    }
    Object value = fieldMetaData.component.getValue();
    if (fieldMetaData.df != null) value = fieldMetaData.df.toVO(value, RFWUI.getLocaleCurrent());

    return value;
  }

  /**
   * Este método permite atribuir um campo, que tenha sido criado fora do UIFactory, à um caminho do VO. Fazendo que o campo seja registrado junto com qualquer outro campo que o UIFactory tenha criado.<br>
   * <B>ATENÇÃO:</b> Este método não faz o "BIND" entre o componente e o VO, para isso utilize o método {@link #bind(HasValue, String, RFWDataFormatter, boolean, Object)}
   *
   * @param attribute Caminho do atributo a qual este campo está mapeado. Ou um caminho específico para salvar o campo, caso ele não esteja direamente ligado a um attributo do VO.
   * @param field Campo do Vaadin.
   */
  public void createVOField_Custom(String attribute, HasValue<?> field) {
    this.voFieldHash.put(attribute, field);
  }

  /**
   * Recupera um VOFIeld que tenha sido criado, e força a releitura do valor do VO para o field.
   *
   * @param attribute Atributo do VO usado na geração do campo.
   * @throws RFWException Lançada exception critica caso o campo não seja encontrado
   */
  public void reloadVOField(String attribute) throws RFWException {
    final UIBinder<?, ?, VO> bind = this.bindingMap.get(attribute);
    if (bind == null) {
      throw new RFWCriticalException("Binder não encontrado para o caminho '" + attribute + "'.");
    }
    bind.toPresentation();
  }

  /**
   * Procura todos os VO fields que tenham o prefixo passado e e força a releitura do valor do VO para o field.
   *
   * @param parentAttribute Atributo do VO pai utilizado como prefixo para filtros os campos. Se passado nulo tem o mesmo efeito que {@link #reloadVOField_All()}.
   */
  public void reloadVOFieldByPrefix(String parentAttribute) throws RFWException {
    if (parentAttribute == null) {
      parentAttribute = "";
    } else if (parentAttribute.length() > 0) {
      parentAttribute += ".";
    }
    for (Entry<String, UIBinder<?, ?, VO>> entries : this.bindingMap.entrySet()) {
      if (parentAttribute.length() == 0 || entries.getKey().startsWith(parentAttribute)) {
        entries.getValue().toPresentation();
      }
    }
  }

  /**
   * Cria um Grid para exibir uma lista de itens fixos (Sem uso do DataProvider com acesso ao banco de dados). Útil para exibir uma quantidade limitada de dados em telas de edição por exemplo. Pode ser utilizada com um Bean ao invés de um VO.
   *
   * @param voClass Classe do VO/Bean.
   * @param items Coleção de Itens para ser exibido no Grid
   * @param doubleClickListener
   * @return Grid criado para exibir a lista de items.
   * @throws RFWException
   */
  public static <BEAN> Grid<BEAN> createGridForList(Class<BEAN> voClass, List<BEAN> items, RFWGridDoubleClickListener<BEAN> doubleClickListener) throws RFWException {
    Grid<BEAN> grid = new Grid<>(voClass);
    grid.setSizeFull();

    grid.removeAllColumns(); // Remove todas as colunas que o Grid adiona sozinho
    grid.setItems(items);

    if (doubleClickListener != null) UIFactory.addDoubleClickListenerToGrid(grid, doubleClickListener);

    return grid;
  }

  /**
   * Cria um TreeGrid para exibir uma objetos de uma Hierarquia com DataProvider.
   *
   * @param voClass Classe do VO/Bean.
   * @param selectionMode Define o modo de seleção do Grid
   * @param orderBy Objeto para definir a ordem inicial do Grid
   * @param doubleClickListener Listener para o caso de duplo click em algum item do Grid. Para obter o item que recebeu o duplo click utilize o método event.getItem().
   * @param parentAttribute nome do atributo que identifica o pai do objeto na Hierarquia.
   * @param ignoreIDs IDs dos objetos que devem ser ignorados e não serem retornados pelo Provider. Útil quando estamos editando a hierarquia e não queremos que o próprio objeto apareça na listagem.
   * @param dbProvider Provedor de dados para que o componente consiga acessar os outros objetos do sistema.
   * @return Grid hierárquivo para exibir os dados.
   * @throws RFWException
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static <VO extends RFWVO> TreeGrid<GVO<VO>> createTreeGrid(Class<VO> voClass, SelectionMode selectionMode, RFWOrderBy orderBy, RFWGridDoubleClickListener<GVO<VO>> doubleClickListener, String parentAttribute, Long[] ignoreIDs, RFWDBProvider dbProvider) throws RFWException {
    final TreeGrid<GVO<VO>> grid = new TreeGrid<>();

    UITreeDataProvider<VO> provider = new UITreeDataProvider<>(voClass, parentAttribute, orderBy, ignoreIDs, dbProvider);
    setUpGrid(grid, selectionMode, true, doubleClickListener, provider);
    grid.setStyleGenerator(new UIGridRowHighlight());

    if (doubleClickListener != null) UIFactory.addDoubleClickListenerToGrid(grid, doubleClickListener);

    return grid;
  }

  /**
   * Cria um TreeGrid aproveitando um Data Provider já criado anteriormente.
   *
   * @param voClass Classe do VO/Bean.
   * @param selectionMode Define o modo de seleção do Grid
   * @param doubleClickListener Listener para o caso de duplo click em algum item do Grid. Para obter o item que recebeu o duplo click utilize o método event.getItem().
   * @return Grid Hierárquivo para exibir os dados.
   * @throws RFWException
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static <VO extends RFWVO> TreeGrid<GVO<VO>> createTreeGrid(Class<VO> voClass, SelectionMode selectionMode, RFWGridDoubleClickListener<GVO<VO>> doubleClickListener, UITreeDataProvider<VO> provider) throws RFWException {
    final TreeGrid<GVO<VO>> grid = new TreeGrid<>();

    setUpGrid(grid, selectionMode, true, doubleClickListener, provider);
    grid.setStyleGenerator(new UIGridRowHighlight());

    if (doubleClickListener != null) UIFactory.addDoubleClickListenerToGrid(grid, doubleClickListener);

    return grid;
  }

  /**
   * Define um Grid como editável.
   *
   * @param <BEAN> Tipo utilizado no Grid
   * @param grid Grid do Vaadinq ue passará a ser editável
   * @throws RFWException
   */
  public static <BEAN> void setupGridEditor(Grid<BEAN> grid) throws RFWException {
    grid.getEditor().setSaveCaption("Confirmar");
    grid.getEditor().setCancelCaption("Cancelar");
    grid.getEditor().setEnabled(true);
  }

  /**
   * Este método desmonta o bind entre o VO e os Fields de todos os atributos que começarem com o caminho passado.<br>
   * Útil para quando eliminamos um sub-vo do objeto e queremos desmontar o bind entre todos os objetos daquele caminho de uma única vez.
   *
   * @param startingPath Começo do caminho para eliminar os binds.
   *
   * @throws RFWException
   */
  public void unBindStartingWith(String startingPath) throws RFWException {
    for (Entry<String, UIBinder<?, ?, VO>> entry : new ArrayList<>(this.bindingMap.entrySet())) {
      if (entry.getKey().startsWith(startingPath)) {
        entry.getValue().cancelBinding();
        this.bindingMap.remove(entry.getKey());
      }
    }
  }

  /**
   * Este método refaz o bind entre o VO e os Fields de todos os atributos que começarem com o caminho passado.<br>
   * Útil para quando eliminamos um sub-vo do objeto e queremos desmontar o bind entre todos os objetos daquele caminho de uma única vez.
   *
   * @param startingPath Começo do caminho dos binds que serão refeitos.
   * @param newPath Novo começo do caminho pelo qual o startingPath será substituido.
   *
   * @throws RFWException
   */
  public void reBindStartingWith(String startingPath, String newPath) throws RFWException {
    for (Entry<String, UIBinder<?, ?, VO>> entry : new ArrayList<>(this.bindingMap.entrySet())) {
      if (entry.getKey().startsWith(startingPath)) {
        // Atualiza a propriedade do nosso Binding
        final String newFullPath = newPath + entry.getKey().substring(startingPath.length());

        // Valida se já temos algum Field na posição futura, se tiver lançamos exception critica poir o dev está fazendo merda. Ou o campo já devia ter sido removido ou movido antes deste
        if (this.bindingMap.containsKey(newFullPath)) throw new RFWCriticalException("Falha ao terntar o reBind do atributo '${0}' para o '${1}'. Já existe um campo no atributo de destino '${2}'.", new String[] { startingPath, newPath, newFullPath });

        entry.getValue().setProperty(newFullPath);
        // Substituimos sua posição na Hash
        this.bindingMap.remove(entry.getKey());
        this.bindingMap.put(newFullPath, entry.getValue());
      }
    }
  }

  /**
   * Realiza o bind entre o VO da instância do UIFactory.<br>
   * O bind ficará registrado para ser manipulado pelo UIFactory posteriormente. (Como validação, unBind, etc.)
   *
   * @param field Campo/Field que será ligado à propriedade do VO.
   * @param propertyPath Propriedade do VO que será anexada ao campo da tela.
   * @param formatter Permite a definição de um {@link RFWDataFormatter} para converter os dados brutos do VO e a regionalização dos campos na tela do usuário.
   * @param required Define se o campo é de preenchimento obrigatório ou não. Se definido aqui, a validação do {@link UIFactory} detectará e reclamará quando o campo não for definido, bem como atualizará a exibição do indicador de obrigatoriedade do campo.
   * @param nullValue Permite definir algum objeto que será considerado como valor nulo. Isto é, se o valor retornado pelo campo for "equals" este objeto, o valor definido no VO será 'null', e se o required = true, marcará o campo como de preenchimento obrigatório. Caso o valod no VO seja nulo, este valor será atribuido no campo da tela.
   * @return Retorna o Objeto do Binder, permitindo que operações e modificações sejam feitas através dele.
   * @throws RFWException Lançado em caso de erro ou falha ao realizar o bind.
   */
  public <FIELDTYPE, BEANTYPE> UIBinder<FIELDTYPE, BEANTYPE, VO> bind(HasValue<FIELDTYPE> field, String propertyPath, RFWDataFormatter<FIELDTYPE, BEANTYPE> formatter, boolean required, FIELDTYPE nullValue) throws RFWException {
    return bind(field, propertyPath, formatter, required, nullValue, false);
  }

  /**
   * Realiza o bind entre o VO da instância do UIFactory.<br>
   * O bind ficará registrado para ser manipulado pelo UIFactory posteriormente. (Como validação, unBind, etc.)
   *
   * @param field Campo/Field que será ligado à propriedade do VO.
   * @param propertyPath Propriedade do VO que será anexada ao campo da tela.
   * @param formatter Permite a definição de um {@link RFWDataFormatter} para converter os dados brutos do VO e a regionalização dos campos na tela do usuário.
   * @param required Define se o campo é de preenchimento obrigatório ou não. Se definido aqui, a validação do {@link UIFactory} detectará e reclamará quando o campo não for definido, bem como atualizará a exibição do indicador de obrigatoriedade do campo.
   * @param nullValue Permite definir algum objeto que será considerado como valor nulo. Isto é, se o valor retornado pelo campo for "equals" este objeto, o valor definido no VO será 'null', e se o required = true, marcará o campo como de preenchimento obrigatório. Caso o valod no VO seja nulo, este valor será atribuido no campo da tela.
   * @param wrapIntoGVO Caso definido como true, e o tipo do objeto do VO seja uma lista, o Bind vai reencapsular os objetos com o GVO e vice versa.
   * @return Retorna o Objeto do Binder, permitindo que operações e modificações sejam feitas através dele.
   * @throws RFWException Lançado em caso de erro ou falha ao realizar o bind.
   */
  @SuppressWarnings("unchecked")
  public <FIELDTYPE, BEANTYPE> UIBinder<FIELDTYPE, BEANTYPE, VO> bind(HasValue<FIELDTYPE> field, String propertyPath, RFWDataFormatter<FIELDTYPE, BEANTYPE> formatter, boolean required, FIELDTYPE nullValue, boolean wrapIntoGVO) throws RFWException {
    // Procuramos se já temos algum para esta propriedade, se tiver cancelamos o bind antes de registrar o novo
    final UIBinder<FIELDTYPE, BEANTYPE, VO> actual = (UIBinder<FIELDTYPE, BEANTYPE, VO>) this.bindingMap.get(propertyPath);
    if (actual != null) actual.cancelBinding();
    this.bindingMap.remove(propertyPath);

    final UIBinder<FIELDTYPE, BEANTYPE, VO> binder = new UIBinder<>(field, this.vo, propertyPath, formatter, required, nullValue, wrapIntoGVO);
    this.bindingMap.put(propertyPath, binder);

    return binder;
  }

  /**
   * Realiza o bind entre um VO e um campo do Vaadin.<br>
   * <B>Note: </b>Este método é estático e não faz nenhuma referência com a instância do UIFactory. Não registra o campo nem o Bind para ser manipulado em grupo posteriormente.
   *
   * @param field Campo/Field que será ligado à propriedade do VO.
   * @param bean Bean com a propriedade a ser conectada.
   * @param propertyPath Propriedade do VO que será anexada ao campo da tela.
   * @param df Permite a definição de um {@link RFWDataFormatter} para converter os dados brutos do VO e a regionalização dos campos na tela do usuário.
   * @param required Define se o campo é de preenchimento obrigatório ou não. Se definido aqui, a validação do {@link UIFactory} detectará e reclamará quando o campo não for definido, bem como atualizará a exibição do indicador de obrigatoriedade do campo.
   * @param nullValue Permite definir algum objeto que será considerado como valor nulo. Isto é, se o valor retornado pelo campo for "equals" este objeto, o valor definido no VO será 'null', e se o required = true, marcará o campo como de preenchimento obrigatório. Caso o valod no VO seja nulo, este valor será atribuido no campo da tela.
   * @return Retorna o Objeto do Binder, permitindo que operações e modificações sejam feitas através dele.
   * @throws RFWException Lançado em caso de erro ou falha ao realizar o bind.
   */
  public static <FIELDTYPE, BEANTYPE, BEAN> UIBinder<FIELDTYPE, BEANTYPE, BEAN> bind(HasValue<FIELDTYPE> field, BEAN bean, String propertyPath, RFWDataFormatter<FIELDTYPE, BEANTYPE> df, boolean required, FIELDTYPE nullValue) throws RFWException {
    return new UIBinder<>(field, bean, propertyPath, df, required, nullValue, false);
  }

  /**
   * Realiza a mesma tarefa que o método {@link #bind(HasValue, Object, String, RFWDataFormatter, boolean, Object)}, porém é exclusivo para os campos {@link RFWDateRangeComponent}. Este método realiza dois binds ao de ua vez, um para associar a data inicial e outro para a data final em dois atributos distintos do VO.
   *
   * @param field Campo/Field que será ligado à propriedade do VO.
   * @param bean Bean com a propriedade a ser conectada.
   * @param startDatePropertyPath Propriedade do VO que será associada à data de início do período.
   * @param endDatePropertyPath Propriedade do VO que será associada à data de fim do período.
   * @param required Define se o campo é de preenchimento obrigatório ou não. Se definido aqui, a validação do {@link UIFactory} detectará e reclamará quando o campo não for definido, bem como atualizará a exibição do indicador de obrigatoriedade do campo.
   * @param nullValue Permite definir algum objeto que será considerado como valor nulo. Isto é, se o valor retornado pelo campo for "equals" este objeto, o valor definido no VO será 'null', e se o required = true, marcará o campo como de preenchimento obrigatório. Caso o valod no VO seja nulo, este valor será atribuido no campo da tela.
   * @return Array com os 2 binds criados. Na posição '0' o bind da data de início do período, e na posição '1' o bind da data final do período.
   * @throws RFWException
   */
  @SuppressWarnings("unchecked")
  public static <BEAN> UIBinder<LocalDateTime, LocalDateTime, BEAN>[] bind(RFWDateRangeComponent field, BEAN bean, String startDatePropertyPath, String endDatePropertyPath, boolean required, LocalDateTime nullValue) throws RFWException {
    UIBinder<LocalDateTime, LocalDateTime, BEAN>[] binds = new UIBinder[] {
        new UIBinder<>(field.getStartDateTime(), bean, startDatePropertyPath, null, required, nullValue, false),
        new UIBinder<>(field.getEndDateTime(), bean, endDatePropertyPath, null, required, nullValue, false),
    };
    return binds;
  }

  /**
   * Este método faz o Bind de um Field no VO (assim como o {@link #bind(HasValue, RFWVO, String, RFWDataFormatter, boolean, Object)}, mas extrai todas as informações necessarias para o Bind a partir das RFWMetaAnnotation dos atributos do RFWVO.
   *
   * @param field Campo/Field que será ligado à propriedade do VO.
   * @param vo VOcom a propriedade a ser conectada.
   * @param propertyPath Propriedade do VO que será anexada ao campo da tela.
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @return Objeto do Bind entre o campo e o VO.
   * @throws RFWException
   */
  public static <VO extends RFWVO, FIELDTYPE, BEANTYPE> UIBinder<FIELDTYPE, BEANTYPE, VO> bindVO(HasValue<FIELDTYPE> field, Class<VO> voClass, VO vo, String propertyPath, RFWDataFormatter<FIELDTYPE, BEANTYPE> df) throws RFWException {
    return bindVO(field, voClass, vo, propertyPath, df, null);
  }

  /**
   * Este método faz o Bind de um Field no VO (assim como o {@link #bind(HasValue, RFWVO, String, RFWDataFormatter, boolean, Object)}, mas extrai todas as informações necessarias para o Bind a partir das RFWMetaAnnotation dos atributos do RFWVO.
   *
   * @param field Campo/Field que será ligado à propriedade do VO.
   * @param vo VOcom a propriedade a ser conectada.
   * @param propertyPath Propriedade do VO que será anexada ao campo da tela.
   * @param df DataFormatter a ser utilizado no campo. Se for passado NULL o método criará o DataFormatter Padrão conforme a RFWMetaAnnotation do atributo.
   * @param forceRequired Se passado nulo será utilizado a definição da Annotation. Se definido TRUE o campo será deifnido como brigatório, se definido como FALSE o campo será definido como opcional.
   * @return Objeto do Bind entre o campo e o VO.
   * @throws RFWException
   */
  @SuppressWarnings("unchecked")
  public static <VO extends RFWVO, FIELDTYPE, BEANTYPE> UIBinder<FIELDTYPE, BEANTYPE, VO> bindVO(HasValue<FIELDTYPE> field, Class<VO> voClass, VO vo, String propertyPath, RFWDataFormatter<FIELDTYPE, BEANTYPE> df, Boolean forceRequired) throws RFWException {
    // O valor padrão de objeto nulo é o próprio nulo, exceto para alguns tipos de campos
    FIELDTYPE nullValue = null;
    if (field instanceof AbstractTextField) {
      nullValue = (FIELDTYPE) ""; // Nos campos do tipo TextField o valor de "nulo" no VO, é representado por uma String vazia no componente
    }

    Boolean required = forceRequired;
    if (required == null) required = RUReflex.getRFWMetaAnnotationRequired(voClass, propertyPath);
    if (df == null) {
      if (field instanceof DateField) {
        // Não utiliza DF pois o campo já retorna opera com o objeto que deve estar no VO
      } else if (field instanceof DateTimeField) {
        // Não utiliza DF pois o campo já retorna opera com o objeto que deve estar no VO
      } else if (field instanceof RFWComboBoxMeasureUnit) {
        // Não utiliza DF pois o campo já retorna opera com o objeto que deve estar no VO
      } else if (field instanceof ComboBox) {
        // Não permite criar um DataFormatter automaticamente para ComboBox pq ComboBox permite utilizar o objeto diretamente. Casos em que os objetos precisem de dataformatter, este precisa ser definido manualmente. Esta alteração foi feita em 27/10/2021
      } else {
        // Não faz o bind para os tipos de campos acima, pois eles tratam o objeto diretamente
        df = getDataFormatterByRFWMetaAnnotation(voClass, propertyPath);
      }
    }

    // Se temos um DF, e temos um campo do tipo TextField, atualizamos o MaxLength para o definido no DataFormatter. Isso pq o campo gerado utiliza o maxLength da RFWMeta do VO, e a formatação em geral inclui mais caracteres.
    if (df != null && field instanceof AbstractTextField) ((AbstractTextField) field).setMaxLength(df.getMaxLenght());

    return bind(field, vo, propertyPath, df, required, nullValue);
  }

  /**
   * Valida todos os campos que foram "binded" com o método {@link #bind(HasValue, String, RFWDataFormatter, boolean, Object)}, que utiliza o VO da instância. Este método nunca retorna as Exceptions de Validação, as mensagens de validação são aplicaas diretamente nos campos e os deixa com o aspecto vermlho de erro.<br>
   * Todos os campos serão validados! Mesmo que algum retorne que não está válido, o método continuará testando todos os outros campos para ativar as mensagens de erro de todos.
   *
   * @throws RFWException Lançado caso algum campo tenha um valor inválido.
   */
  public void validateVOFields() throws RFWException {
    boolean valid = true;
    for (UIBinder<?, ?, VO> bind : this.bindingMap.values()) {
      valid &= bind.validate();
    }
    if (!valid) {
      throw new RFWValidationException("Algumas informações são inválidas! Verifique as exclamações de erros em cada campo com problema para corrigi-los.");
    }
  }

  /**
   * Método auxiliar para recuperar a mensagem de erro tentando descobrir qual é o tipo de field.
   *
   * @param field Campo para recuperar a mensagem.
   * @return Mensagem no atributo 'ComponentError', ou nulo caso não tenha nenhuma.
   * @throws RFWException Lançado caso o método não consiga reconhecer o tipo do campo.
   */
  public static ErrorMessage getComponentError(HasValue<?> field) throws RFWException {
    if (field instanceof AbstractComponent) {
      return ((AbstractComponent) field).getComponentError();
    } else {
      // Se apareceu esta exception, verifique a mesma condição no método de GET/SET Correspondente
      throw new RFWCriticalException("Tipo de field desconhecido para recuperar a mensagem de 'ComponentError': '${0}'.", new String[] { field.getClass().getCanonicalName() });
    }
  }

  /**
   * Cria um sumarizador de coluna no Grid de MO.
   *
   * @param attributes Atributos do VO que devem ser sumarizados.
   * @param sumWithoutSelection Indicador (quando true) se o rodapé deve ou não somar os valores quando Nenhum item estiver selecionado (ou seja somar tudo). Lembre-se que em tabelas com lazyload esse total será recuperado por comando no banco de dados. Se a informação de nenhum item selecionado não for importante, passe false.
   * @param fieldAlignment Alinhamento do campo
   * @param df DataFormatter, se necessário
   * @throws RFWException
   */
  public <PRESENTATIONTYPE, VOTYPE> void addGridForMOFooterSum(final String[] attributes, final boolean sumWithoutSelection, final FieldAlignment fieldAlignment, RFWDataFormatter<PRESENTATIONTYPE, VOTYPE> df) throws RFWException {
    if (this.moFooterRow == null) {
      this.moFooterRow = this.moGrid.appendFooterRow();
    }

    // Corrigimos o array de atributos recebidos, pois o moGrid tem o prefixo "VO." já que encapsulamos o GVO.
    for (int i = 0; i < attributes.length; i++) {
      attributes[i] = "VO." + attributes[i];
      String attribute = attributes[i];

      final Column<GVO<VO>, ?> column = this.moGrid.getColumn(attribute);
      if (column == null) throw new RFWCriticalException("Coluna '" + attribute + "' não encontrada no MO Grid!");
      final FooterCell cell = this.moFooterRow.getCell(column);

      // Arruma o alinhamento da célula
      if (fieldAlignment != null) {
        switch (fieldAlignment) {
          case CENTER:
            cell.setStyleName(FWVad.STYLE_ALIGN_CENTER);
            break;
          case RIGHT:
            cell.setStyleName(FWVad.STYLE_ALIGN_RIGHT);
            break;
          case LEFT:
            // ALinhamento padrão, se não tiver os outros estilos já fica a esquerda.
            break;
        }
      }
    }

    // Adiciona um único listener que vai atualizar todas as colunas.
    this.moGrid.addSelectionListener(e -> changedMOGridSelectionSumFooterLister(this.moGrid, this.moFooterRow, attributes, sumWithoutSelection, df));
  }

  /**
   * Método para reprocessar os valores de somatório do rodapé dos Grids.
   *
   * @param grid Grid a ter os valores reprocessados.
   * @param footerRow Linha do rodapé onde os valores serão escritos.
   * @param attributes Atributos para serem sumarizados.
   * @param sumWithoutSelection Indicador (quando true) se o rodapé deve ou não somar os valores quando Nenhum item estiver selecionado (ou seja somar tudo). Lembre-se que em tabelas com lazyload esse total será recuperado por comando no banco de dados. Se a informação de nenhum item selecionado não for importante, passe false.
   */
  @SuppressWarnings("unchecked")
  private static <BEAN, PRESENTATIONTYPE, VOTYPE> void changedMOGridSelectionSumFooterLister(Grid<BEAN> grid, FooterRow footerRow, String[] attributes, boolean sumWithoutSelection, RFWDataFormatter<PRESENTATIONTYPE, VOTYPE> df) {
    try {
      for (String attribute : attributes) {
        // Recupera a coluna do Grid, concatena o "VO." antes pq o moGrid tem a o VO encapsulado dentro do GVO.
        final Column<BEAN, ?> column = grid.getColumn(attribute);
        if (column == null) throw new RFWCriticalException("Coluna '" + attribute + "' não encontrada no MO Grid!");
        final FooterCell cell = footerRow.getCell(column);

        if (sumWithoutSelection && grid.getSelectedItems().size() == 0) {
          // TODO para sumarizar os itens não selecionados de um GRID precisamos implementar alguma consulta no UIDataProvider para ele buscar direto do banco, já que não temos todos os objetos em memória. E até fazer um cache lá para que ele só atualize o valor quando ouver mudança no Where sendo aplicado.
          throw new RFWCriticalException("RFW não preparado para realizar a soma dos itens não selecionados!");
        } else {
          BigDecimal sum = null;

          for (BEAN bean : grid.getSelectedItems()) {
            final Object value = RUReflex.getPropertyValue(bean, attribute);
            if (value != null) {
              if (BigDecimal.class.isAssignableFrom(value.getClass())) {
                if (sum == null) sum = BigDecimal.ZERO;
                sum = sum.add((BigDecimal) value);
              } else {
                throw new RFWCriticalException("UIFactory não sabe sumarizar valores da classe '" + value.getClass().getCanonicalName() + "' no rodapé!");
              }
            }
          }

          // Escrevemos o valor de acordo com o totalização que não esteja nulo
          String fValue = "---";
          if (sum != null) {
            fValue = sum.toString();
            if (df != null) fValue = (String) df.toPresentation((VOTYPE) sum, RFWUI.getLocaleCurrent());
          }

          cell.setText(fValue);
        }
      }
    } catch (Throwable e) {
      TreatException.treat(e);
    }
  }

  /**
   * Recupera o define um RFWMO "base" padrão para ser utilizado no GRID do MO {@link #moGrid}. Se definido, este MO é clonado e utilizado como base para colocar as condições dos campos de filtro.<br>
   * Este MO deve ser utilizado quando temos algum filtro que deve ser sempre utilizado recuperar os dados para o Grid. Por exemplo, queremos apenas que só os objetos ativos sejam exibidos independente das opções de filtro que o usuário escolha.
   *
   * @return the define um RFWMO "base" padrão para ser utilizado no GRID do MO {@link #moGrid}
   */
  public RFWMO getBaseRFWMO() {
    return baseRFWMO;
  }

  /**
   * Define o define um RFWMO "base" padrão para ser utilizado no GRID do MO {@link #moGrid}. Se definido, este MO é clonado e utilizado como base para colocar as condições dos campos de filtro.<br>
   * Este MO deve ser utilizado quando temos algum filtro que deve ser sempre utilizado recuperar os dados para o Grid. Por exemplo, queremos apenas que só os objetos ativos sejam exibidos independente das opções de filtro que o usuário escolha.
   *
   * @param moGridDefaultMO the new define um RFWMO "base" padrão para ser utilizado no GRID do MO {@link #moGrid}
   */
  public void setBaseRFWMO(RFWMO moGridDefaultMO) {
    this.baseRFWMO = moGridDefaultMO;
  }

  /**
   * Método utilitário para converter a resolução de período encontrada na {@link RFWMetaDateField} para a {@link DateTimeResolution}.
   */
  public static DateTimeResolution convertToDateTimeResolution(RFWMetaDateField.DateResolution resolution) throws RFWException {
    switch (resolution) {
      case YEAR:
        return DateTimeResolution.YEAR;
      case MONTH:
        return DateTimeResolution.MONTH;
      case DAY:
        return DateTimeResolution.DAY;
      case HOUR:
        return DateTimeResolution.HOUR;
      case MINUTE:
        return DateTimeResolution.MINUTE;
      case SECOND:
      case MILLISECONDS:
        return DateTimeResolution.SECOND;
    }
    return null;
  }

  /**
   * Método utilitário para converter a resolução de período encontrada na {@link RFWMetaDateField} para a {@link DateTimeResolution}.
   */
  public static DateResolution convertToDateResolution(RFWMetaDateField.DateResolution resolution) throws RFWException {
    switch (resolution) {
      case YEAR:
        return DateResolution.YEAR;
      case MONTH:
        return DateResolution.MONTH;
      case DAY:
        return DateResolution.DAY;
      case HOUR:
      case MINUTE:
      case SECOND:
      case MILLISECONDS:
        throw new RFWCriticalException("A resolução '" + resolution + "' não é compatível com o  um DateResolution!");
    }
    return null;
  }

  /**
   * Método utilitário para converter a resolução de período encontrada na {@link RFWMetaDateField} para a {@link DateTimeResolution}.
   */
  public static DateResolution convertToDateResolution(DateTimeResolution resolution) throws RFWException {
    switch (resolution) {
      case YEAR:
        return DateResolution.YEAR;
      case MONTH:
        return DateResolution.MONTH;
      case DAY:
        return DateResolution.DAY;
      case HOUR:
      case MINUTE:
      case SECOND:
        throw new RFWCriticalException("A resolução '" + resolution + "' não é compatível com o  um DateResolution!");
    }
    return null;
  }

  /**
   * Retorna um clone da Hash com os FieldVOs.
   *
   * @return Retorna um clone da Hash com os FieldVOs.
   */
  @SuppressWarnings("unchecked")
  public LinkedHashMap<String, HasValue<?>> getVOFields() {
    return (LinkedHashMap<String, HasValue<?>>) this.voFieldHash.clone();
  }

  /**
   * Adiciona um listener diretamente no provider do Grid MO. Este listener é notificado toda vez que alguma alteração ocorrer no conjuntos de dados do Provider.
   *
   * @param listener Listener para escutar as alterações do Provider do MOGrid.
   */
  public void addGridForMOProviderListener(DataProviderListener<GVO<VO>> listener) {
    this.moGrid.getDataProvider().addDataProviderListener(listener);
  }

  /**
   * Auxilia a selecionar um botão em uma barra de botões RFWButtonToggle.
   *
   * @param toggleBar {@link RFWAssociativeComponent} em que foi montado o {@link RFWButtonToggle}.
   * @param value Valor do Botão que deve ser selecionado.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Object> void selectRFWButtonToggle(RFWAssociativeComponent toggleBar, Object value) {
    for (Component component : toggleBar.getComponents()) {
      RFWButtonToggle<T> btg = (RFWButtonToggle<T>) component;
      if (btg.getSelectedValue() != null && btg.getSelectedValue().equals(value)) {
        btg.setSelected(true);
        break;
      }
    }
  }

  /**
   * Auxilia a desselecionar um botão em uma barra de botões RFWButtonToggle.
   *
   * @param toggleBar {@link RFWAssociativeComponent} em que foi montado o {@link RFWButtonToggle}.
   * @param value Valor do Botão que deve ser selecionado.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Object> void desselectRFWButtonToggle(RFWAssociativeComponent toggleBar, Object value) {
    for (Component component : toggleBar.getComponents()) {
      RFWButtonToggle<T> btg = (RFWButtonToggle<T>) component;
      if (btg.getSelectedValue() != null && btg.getSelectedValue().equals(value)) {
        btg.setSelected(false);
        break;
      }
    }
  }

}
