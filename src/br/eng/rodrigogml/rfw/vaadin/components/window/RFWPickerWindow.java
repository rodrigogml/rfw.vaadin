package br.eng.rodrigogml.rfw.vaadin.components.window;

import java.util.List;
import java.util.Set;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWValidationException;
import br.eng.rodrigogml.rfw.kernel.interfaces.RFWDBProvider;
import br.eng.rodrigogml.rfw.kernel.preprocess.PreProcess;
import br.eng.rodrigogml.rfw.kernel.utils.RUReflex;
import br.eng.rodrigogml.rfw.kernel.vo.GVO;
import br.eng.rodrigogml.rfw.kernel.vo.RFWVO;
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
   * Referência para o botão de cancelar da janela.
   */
  final Button buttonCancel;

  /**
   * Referência para o botão de confirmar
   */
  final Button buttonConfirm;

  /**
   * DBProvider Fornecido na construção da classe.
   */
  private final RFWDBProvider dbProvider;

  /**
   * Lista dos itens selecionados antes da tela ser fechada.<br>
   * Estará nula caso a tela não tenha sido confirmada (seja fechada ou cancelada).
   */
  private List<VO> selectedItems = null;

  public RFWPickerWindow(Class<VO> voClass, String caption, ThemeResource icon, final RFWDBProvider dbProvider) throws RFWException {
    super(caption);

    PreProcess.requiredNonNull(dbProvider, "O RFWPickerWindow necessita um DBProvider válido para funcionar corretamente!");

    this.setIcon(icon);
    this.setModal(true);
    this.setSizeFull();
    this.setWidth("92%");
    this.setHeight("95%");
    this.center();
    this.setClosable(true);

    this.dbProvider = dbProvider;

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
    buttonCancel = FWVad.createButton(ButtonType.CANCEL, e -> this.close());
    buttonBar.addComponent(buttonCancel);
    buttonConfirm = FWVad.createButton(ButtonType.CONFIRM, e -> clickedButtonConfirm());
    buttonBar.addComponent(buttonConfirm);

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

    this.setContent(this.mainView);
  }

  /**
   * Sets the grid.
   *
   * @param grid the new grid
   * @throws RFWException the RFW exception
   */
  protected void setGrid(Grid<GVO<VO>> grid) throws RFWException {
    // Remove os componentes iniciais até só só sobrar o último (que é a barra de botões cancelar a confirmar.
    while (listLayout.getComponentCount() > 1) {
      listLayout.removeComponent(listLayout.getComponent(0));
    }

    listLayout.addComponent(grid, 0);
    listLayout.setExpandRatio(grid, 1f);

    // Adiciona o double click para selecionar e confirmar o Picker
    this.getUiFac().addDoubleClickListenerToMOGrid(e -> {
      grid.getSelectionModel().deselectAll();
      grid.getSelectionModel().select(e.getItem());
      clickedButtonConfirm();
    });
  }

  /**
   * Permite a troca do componente da Barra de botões de confirmar a cancelar.
   *
   * @param component
   * @throws RFWException
   */
  protected void setButtonBar(Component component) throws RFWException {
    // Garante que o componente não é nulo para não perdermos a conta dos componentes no layout, já que a substituição é feita com base na posição dele no verticallayout
    PreProcess.requiredNonNullCritical(component, "O componente da barra de botões não pode ser nulo e deve incluir ao menos o botão de confirmar!");
    // A barra de botões sempre está no componente, mas o grid pode não ter sido alocado ainda. Assim a barra pode estar na posição 0 ou 1.
    int index = listLayout.getComponentCount() - 1;
    listLayout.removeComponent(listLayout.getComponent(index));
    listLayout.addComponent(component, index);
  }

  /**
   * Força a atualização do painel de busca de acordo com os campos criados no UIFactory para o MO da listagem.
   */
  protected void updateSearchPanel() {
    // Atualiza o conteúdo do searchPanel
    searchPanel.setContent(this.uiFac.createSearchPanel(1, null, this.dbProvider));
  }

  private void clickedButtonConfirm() {
    try {
      // Verificamos se há 1 objeto selecionado no Grid
      final Set<GVO<VO>> sels = getUiFac().getMOGrid().getSelectedItems();
      if (sels.size() == 0) throw new RFWValidationException("Selecione o item desejado antes de confirmar.");
      this.selectedItems = RUReflex.collectGVOToVOList(sels);
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
   * # lista dos itens selecionados antes da tela ser fechada.<br>
   * Estará nula caso a tela não tenha sido confirmada (seja fechada ou cancelada).
   *
   * @return # lista dos itens selecionados antes da tela ser fechada
   */
  public List<VO> getSelectedItems() {
    return selectedItems;
  }

  /**
   * # dBProvider Fornecido na construção da classe.
   *
   * @return # dBProvider Fornecido na construção da classe
   */
  public RFWDBProvider getDbProvider() {
    return dbProvider;
  }

  /**
   * # referência para o botão de cancelar da janela.
   *
   * @return # referência para o botão de cancelar da janela
   */
  public Button getButtonCancel() {
    return buttonCancel;
  }

  /**
   * # referência para o botão de confirmar.
   *
   * @return # referência para o botão de confirmar
   */
  public Button getButtonConfirm() {
    return buttonConfirm;
  }

  /**
   * Este método retorna o primeiro item da lista dos itens retornados por {@link #getSelectedItems()}, não é necessaraimente o primeiro item que o usuário selecionou. O objectivo deste método é evitar ter que lhe dar com a lista quando temos apenas um item selecionado.
   *
   * @return null se {@link #getSelectedItems()} for nulo, o primeiro item da lista de {@link #getSelectedItems()}.
   */
  public VO getSelectedFirstItem() {
    if (getSelectedItems() != null) {
      return getSelectedItems().iterator().next();
    }
    return null;
  }

}