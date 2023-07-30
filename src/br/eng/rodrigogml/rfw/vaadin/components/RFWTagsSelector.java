package br.eng.rodrigogml.rfw.vaadin.components;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityEvent;

import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.DropTargetExtension;

import br.eng.rodrigogml.rfw.kernel.bundle.RFWBundle;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWRunTimeException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWValidationException;
import br.eng.rodrigogml.rfw.kernel.utils.RUReflex;
import br.eng.rodrigogml.rfw.vaadin.utils.FWVad;
import br.eng.rodrigogml.rfw.vaadin.utils.FWVad.ButtonStyle;
import br.eng.rodrigogml.rfw.vaadin.utils.FWVad.ButtonType;
import br.eng.rodrigogml.rfw.vaadin.utils.TreatException;

/**
 * Description: Componente que cria um campo para permitir a sele��o m�ltiplas escolhas / Tags.<br>
 *
 * @author Rodrigo Leit�o
 * @since 7.1.0 (18/03/2016)
 */
public class RFWTagsSelector<T> extends CustomField<List<T>> {

  private static final long serialVersionUID = 7438576547382937715L;

  private final Panel p;
  private final HorizontalLayout hl = new HorizontalLayout();

  private final PopupButton addbt = FWVad.createPopupButton(ButtonType.ADD_LISTITEM);
  private final VerticalLayout addOptionsVL = new VerticalLayout();

  private List<T> optionValues;
  private boolean repeatValues;
  private boolean sortable;
  private String captionPropoperty;

  private List<T> values = new ArrayList<>();

  /**
   * Use esse construtor quando for uma lista de enums.
   *
   * @param optionValues
   * @param repeatValues
   * @param sortable
   * @throws RFWException
   */
  public RFWTagsSelector(String caption, List<T> optionValues, boolean required, boolean repeatValues, boolean sortable) throws RFWException {
    this(caption, optionValues, null, required, repeatValues, sortable);
  }

  public RFWTagsSelector(String caption, List<T> optionValues, String captionProperty, boolean required, boolean repeatValues, boolean sortable) throws RFWException {
    super.setCaption(caption);
    super.setRequiredIndicatorVisible(required);

    this.p = new Panel();
    this.p.addStyleName("rfwTagsSelector");
    this.optionValues = optionValues;
    this.repeatValues = repeatValues;
    this.sortable = sortable;
    this.captionPropoperty = captionProperty;

    addOptionsVL.setMargin(false);
    addOptionsVL.setSpacing(false);

    addbt.setCaption(null);
    addbt.setContent(this.addOptionsVL);
    addbt.addPopupVisibilityListener(e -> changedAddButtonPopupVisibility(e));
    this.hl.addComponent(addbt);

    // Configura o Layout principal
    this.hl.setSpacing(true);

    // Configura o layout do popup do bot�o add
    addOptionsVL.setSizeFull();
    updateAddOptionsValues();
  }

  /**
   * M�todo chamado sempre que o bot�o de adicionar tiver seu popup "exibido" ou "escondido"
   */
  private void changedAddButtonPopupVisibility(PopupVisibilityEvent e) {
    if (e.isPopupVisible()) {
      // Se o popup est� vis�vel agora, vamos colocar o foco no primeiro item da lista de objetos do popup
      ((Button) this.addOptionsVL.getComponent(0)).focus();
    } else {
      // Se fechou, definimos o foco no pr�prio bot�o add
      this.addbt.focus();
    }
  }

  /**
   * M�todo chamado sempre que algum bot�o de op��o tiver seu popup "exibido" ou "escondido"
   */
  private void changedOptionsButtonPopupVisibility(PopupButton optButton, PopupVisibilityEvent e) {
    if (e.isPopupVisible()) {
      // Se o popup est� vis�vel agora, vamos colocar o foco no primeiro item da lista de objetos do popup
      ((Button) ((VerticalLayout) optButton.getContent()).getComponent(0)).focus();
    } else {
      // Se fechou, definimos o foco no pr�prio bot�o add
      optButton.focus();
    }
  }

  /**
   * Este m�todo atualiza o conte�do do popup que � aberto quando clicamos no bot�o ADD, para exibir ou n�o mais ou menos op��es
   */
  private void updateAddOptionsValues() throws RFWException {
    addOptionsVL.removeAllComponents();
    int count = 0;
    for (T opt : optionValues) {
      // Se podemos incluir mais que uma vez o mesmo item, ou se ele ainda n�o est� nos valores selecionados, vamos inclu�-lo
      if (repeatValues || !values.contains(opt)) {
        final String caption = getOptionLabel(opt);
        Button bt = FWVad.createButton(caption, ButtonStyle.LINK, null, e -> addValue(opt, caption));
        addOptionsVL.addComponent(bt);
        count++;
      }
    }
    addbt.setEnabled(count > 0);
  }

  /**
   * Este m�todo encapsula o caption (label da op��o) no bloco do Badge de acordo com o index
   */
  private String getComponentCaption(String caption, int index) throws RFWException {
    return "<badgeIndex data=\"" + index + "\">" + caption + "</badgeIndex>";
  }

  /**
   * Este m�todo retorna o label a ser exibido da op��o.
   */
  private String getOptionLabel(T opt) throws RFWException {
    final String caption;
    if (opt instanceof Enum) {
      caption = RFWBundle.get((Enum<?>) opt);
    } else {
      caption = RUReflex.getPropertyValue(opt, this.captionPropoperty).toString();
    }
    return caption;
  }

  /**
   * Este m�todo � chamado quando o usu�rio clica em uma das op��es do bot�o Adicionar, e criar� o novo objeto e adicionar� o valor.
   */
  private void addValue(T opt, String caption) {
    try {
      this.values.add(opt);

      // Se n�o repetimos valores temos que atualizar a lista de valores "adicion�veis"
      if (!this.repeatValues) updateAddOptionsValues();

      // Fecha o popup do bot�o
      ((PopupButton) this.hl.getComponent(this.hl.getComponentCount() - 1)).setPopupVisible(false);

      // Adiciona o componente
      addComponentValue(opt, true);
    } catch (Exception e) {
      TreatException.treat(e);
    }
  }

  private void addComponentValue(T opt, boolean fireEvent) throws RFWException {
    PopupButton bt = new PopupButton();
    bt.setCaptionAsHtml(true);

    bt.addPopupVisibilityListener(e -> changedOptionsButtonPopupVisibility(bt, e));
    bt.setCaption(getComponentCaption(getOptionLabel(opt), this.hl.getComponentCount()));

    // Cria o DragSource para que seja poss�vel arrastar o bot�o
    DragSourceExtension<PopupButton> source = new DragSourceExtension<>(bt);
    source.setEffectAllowed(EffectAllowed.MOVE);

    // Cria o Drop para aceitar outro bot�o na nossa posi��o quando receber um "drop"
    DropTargetExtension<PopupButton> drop = new DropTargetExtension<>(bt);
    drop.setDropEffect(DropEffect.MOVE);
    drop.addDropListener(e -> {
      try {
        final AbstractComponent sourceBT = e.getDragSourceComponent().get();
        final PopupButton targetBT = bt;

        // �ndices antes do Drag
        int dragIndex = this.hl.getComponentIndex(sourceBT);
        int dropIndex = this.hl.getComponentIndex(targetBT);

        // Movimenta os bot�es
        changePriority(dragIndex, dropIndex);
      } catch (Throwable e1) {
        TreatException.treat(e1);
      }
    });

    // Inclui a nova op��o por �ltimo, mas antes do bot�o Adicionar
    this.hl.addComponent(bt, this.hl.getComponentCount() - 1);

    // Cria o popupContent do bot�o
    VerticalLayout vl = new VerticalLayout();
    vl.setMargin(false);
    vl.setSpacing(false);
    if (sortable) {
      Button moveUp = FWVad.createButton(ButtonType.MOVE_UP, e -> {
        changePriority(this.hl.getComponentIndex(bt), this.hl.getComponentIndex(bt) - 1);// Aumentar a prioridade � diminuir sua posi��o em 1
        bt.setPopupVisible(false);
      });
      FWVad.setButtonStyle(moveUp, ButtonStyle.LINK);
      moveUp.setCaption("Maior Prioridade");
      vl.addComponent(moveUp);

      Button moveDown = FWVad.createButton(ButtonType.MOVE_DOWN, e -> {
        changePriority(this.hl.getComponentIndex(bt), this.hl.getComponentIndex(bt) + 1); // Diminuir a prioridade � aumentar sua posi��o em 1
        bt.setPopupVisible(false);
      });
      FWVad.setButtonStyle(moveDown, ButtonStyle.LINK);
      moveDown.setCaption("Menor Prioridade");
      vl.addComponent(moveDown);
    }
    Button removebt = FWVad.createButton(ButtonType.REMOVE_LISTITEM, e -> {
      removeValue(this.hl.getComponentIndex(bt)); // pasa o index da op��o para evitar que o valor errado seja removido no "values" em caso de duplicatas
      bt.setPopupVisible(false);
    });
    FWVad.setButtonStyle(removebt, ButtonStyle.LINK);
    vl.addComponent(removebt);
    bt.setContent(vl);

    // Emite evento de altera��o no valor
    if (fireEvent) fireValueChangeEvent();
  }

  /**
   * Este m�todo remove o valor de uma dada prioridade. N�o remove pelo "value" porque em caso de duplicatas podemos remover o errado. Remover pelo �ndice � mais seguro.
   */
  private void removeValue(int priority) {
    try {
      this.values.remove(priority);
      this.hl.removeComponent(this.hl.getComponent(priority));

      // Se n�o repetimos valores temos que atualizar a lista de valores "adicion�veis"
      if (!this.repeatValues) updateAddOptionsValues();

      // Reorganiza os nomes
      reloadComponentCaptions();

      // Emite evento de altera��o no valor
      fireValueChangeEvent();
    } catch (Exception e) {
      TreatException.treat(e);
    }
  }

  private void changePriority(int originalPriority, int finalPriority) {
    try {
      if (finalPriority < 0) {
        throw new RFWValidationException("BISERP_000431");
      } else if (finalPriority >= this.values.size()) {
        throw new RFWValidationException("BISERP_000432");
      }
      // Salva o valor e o componente que ser� movido
      final T value = this.values.get(originalPriority);
      final Component comp = this.hl.getComponent(originalPriority);
      // Remove ambos de suas listas
      this.values.remove(originalPriority);
      this.hl.removeComponent(comp);
      // Re adiciona os componentes na posi��o correta
      this.values.add(finalPriority, value);
      this.hl.addComponent(comp, finalPriority);
      // Relemos os captions dos bot�es
      reloadComponentCaptions();
      // Emite evento de altera��o no valor
      fireValueChangeEvent();
    } catch (Exception e) {
      TreatException.treat(e);
    }
  }

  @Override
  protected Component initContent() {
    p.setWidth("100%");

    p.setContent(this.hl);
    return p;
  }

  /**
   * Este m�todo for�a a apar�ncia do componente ficar igual ao conte�do da lista {@link #values}.
   *
   * @throws RFWException
   */
  private void reloadComponentValue() throws RFWException {
    // Primeiro limpamos o conte�do do componente, menos o �ltimo que � o bot�o de adicionar
    while (this.hl.getComponentCount() > 1)
      this.hl.removeComponent(this.hl.getComponent(0));
    // Agora iteramos os valores para ir criando os componentes
    for (T value : this.values) {
      addComponentValue(value, false);
    }
    // Emite evento de altera��o no valor
    fireValueChangeEvent();
  }

  /**
   * Este m�todo redefine os captions dos bot�es de op��es. N�o remove os bot�es, s� redefine todos os captions.
   */
  private void reloadComponentCaptions() throws RFWException {
    int index = 0;
    for (T value : this.values) {
      final PopupButton comp = (PopupButton) this.hl.getComponent(index);
      comp.setCaption(getComponentCaption(getOptionLabel(value), index + 1));
      index++;
    }
  }

  private void fireValueChangeEvent() {
    super.fireEvent(new ValueChangeEvent<>(this, null, true));
  }

  @Override
  public List<T> getValue() {
    return this.values;
  }

  @Override
  protected void doSetValue(List<T> value) {
    try {
      if (this.isReadOnly()) throw new RFWRunTimeException("O RFWTagsSelector est� em modo ReadOnly ;)");
      this.values.clear();
      if (value != null && value.size() > 0) {
        this.values.addAll(value);
      }
      reloadComponentValue();
    } catch (RFWException e) {
      new RFWRunTimeException(e);
    }
  }
}
