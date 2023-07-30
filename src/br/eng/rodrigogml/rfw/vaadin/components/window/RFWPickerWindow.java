package br.eng.rodrigogml.rfw.vaadin.components.window;

import java.util.Set;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWValidationException;
import br.eng.rodrigogml.rfw.kernel.interfaces.RFWDBProvider;
import br.eng.rodrigogml.rfw.kernel.preprocess.PreProcess;
import br.eng.rodrigogml.rfw.kernel.vo.GVO;
import br.eng.rodrigogml.rfw.kernel.vo.RFWVO;
import br.eng.rodrigogml.rfw.vaadin.interfaces.RFWPickerConfirmListener;
import br.eng.rodrigogml.rfw.vaadin.main.utils.UIFactory;
import br.eng.rodrigogml.rfw.vaadin.utils.FWVad;
import br.eng.rodrigogml.rfw.vaadin.utils.FWVad.ButtonType;
import br.eng.rodrigogml.rfw.vaadin.utils.TreatException;

/**
 * Description: Classe que implementa uma janela de "picker" utilizada para exibir uma lista de objetos para selação.<br>
 * Geralmente utilizada quando precisamos associar um objeto a outro.
 *
 * @author Rodrigo GML
 * @since 1.0.0 (29 de jul. de 2023)
 * @version 1.0.0 - Rodrigo GML-(...)
 */
public class RFWPickerWindow<VO extends RFWVO> extends Window {

  private static final long serialVersionUID = 2823391896172773258L;

  /**
   * Instância da {@link UIFactory} para controlar os campos desta janela.
   */
  private final UIFactory<VO> uiFac;

  // Componentes utilizados na "view" de listagem
  private final VerticalLayout mainView = new VerticalLayout();
  private final Panel searchPanel = new Panel("Filtro");
  private final Panel listPanel = new Panel("Lista");
  private final VerticalLayout listLayout;

  /**
   * Listener chamado ao confirmar a seleção no picker, passando os itens selecionados.
   */
  private RFWPickerConfirmListener<VO> confirmListener = null;

  public RFWPickerWindow(Class<VO> voClass, String caption, ThemeResource icon, final RFWDBProvider dataProvider) throws RFWException {
    super(caption);

    PreProcess.requiredNonNull(dataProvider, "O RFWPickerWindow necessita um dataProvider válido para funcionar corretamente!");

    this.setIcon(icon);
    this.setModal(true);
    this.setSizeFull();
    this.setWidth("92%");
    this.setHeight("95%");
    this.center();
    this.setClosable(true);

    this.uiFac = new UIFactory<>(voClass);

    // Teclas de Atalho da TELA
    UIFactory.addShortcut(this, (comp, s, t, key, mod) -> uiFac.focusFirstSearchField(), KeyCode.F, ModifierKey.ALT);
    UIFactory.addShortcut(this, (comp, s, t, key, mod) -> uiFac.focusMOGrid(), KeyCode.G, ModifierKey.ALT);
    // Teclas de Atalho do Painel de Busca
    UIFactory.addShortcut(searchPanel, (comp, s, t, key, mod) -> uiFac.doSearch(), KeyCode.ENTER, null);

    this.searchPanel.setIcon(VaadinIcons.FILTER);

    // ==>>> Cria os botões e coloca no layout horizontal para ficarem na hodem
    HorizontalLayout buttonBar = new HorizontalLayout();
    buttonBar.setMargin(true);
    buttonBar.setSpacing(false);
    buttonBar.addStyleName(FWVad.STYLE_COMPONENT_ASSOCIATIVE);
    buttonBar.addComponent(FWVad.createButton(ButtonType.CANCEL, e -> this.close()));
    buttonBar.addComponent(FWVad.createButton(ButtonType.CONFIRM, e -> clickedButtonConfirm()));

    // ==>>> Agora montamos os componentes nos layouts para gerar a view de Listagem
    this.mainView.setMargin(true);
    this.mainView.setSizeFull();
    this.mainView.addComponent(searchPanel);
    this.mainView.addComponent(listPanel);
    this.mainView.setExpandRatio(listPanel, 1f);

    // Configura o ListPanel
    this.listPanel.setSizeFull();
    this.listPanel.setIcon(VaadinIcons.LIST);
    listLayout = new VerticalLayout();
    listLayout.setSizeFull();
    listLayout.setMargin(false);
    listLayout.addComponent(buttonBar);
    listLayout.setComponentAlignment(buttonBar, Alignment.MIDDLE_RIGHT);
    this.listPanel.setContent(listLayout);

    // Criamos a parte dinâmica do Layout com essas informações (Que se alterará caso a página seja redimensionada)
    // Atualiza o conteúdo do searchPanel
    searchPanel.setContent(this.uiFac.createSearchPanel(1, null, dataProvider));

    this.setContent(this.mainView);
  }

  /**
   * Sets the grid.
   *
   * @param grid the new grid
   * @throws RFWException the RFW exception
   */
  protected void setGrid(Grid<GVO<VO>> grid) throws RFWException {
    listLayout.addComponent(grid);
    listLayout.setExpandRatio(grid, 1f);

    // Adiciona o double click para selecionar e confirmar o Picker
    this.getUiFac().addDoubleClickListenerToMOGrid(e -> {
      grid.getSelectionModel().deselectAll();
      grid.getSelectionModel().select(e.getItem());
      clickedButtonConfirm();
    });

  }

  @SuppressWarnings("unchecked")
  private void clickedButtonConfirm() {
    try {
      // Verificamos se há 1 objeto selecionado no Grid
      final Set<GVO<VO>> sels = getUiFac().getMOGrid().getSelectedItems();
      if (sels.size() == 0) throw new RFWValidationException("Selecione o item desejado antes de confirmar.");
      if (this.confirmListener != null) this.confirmListener.confirm(sels.iterator().next().getVO());
      this.close();
    } catch (Throwable e) {
      TreatException.treat(e);
    }
  }

  /**
   * # instância da {@link UIFactory} para controlar os campos desta janela.
   *
   * @return # instância da {@link UIFactory} para controlar os campos desta janela
   */
  public UIFactory<VO> getUiFac() {
    return uiFac;
  }

  /**
   * # listener chamado ao confirmar a seleção no picker, passando os itens selecionados.
   *
   * @return # listener chamado ao confirmar a seleção no picker, passando os itens selecionados
   */
  public RFWPickerConfirmListener<VO> getConfirmListener() {
    return confirmListener;
  }

  /**
   * # listener chamado ao confirmar a seleção no picker, passando os itens selecionados.
   *
   * @param confirmListener # listener chamado ao confirmar a seleção no picker, passando os itens selecionados
   */
  public void setConfirmListener(RFWPickerConfirmListener<VO> confirmListener) {
    this.confirmListener = confirmListener;
  }

}