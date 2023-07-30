package br.eng.rodrigogml.rfw.vaadin.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.vaadin.hene.popupbutton.PopupButton;

import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.UserError;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.util.CurrentInstance;

import br.eng.rodrigogml.rfw.kernel.bundle.RFWBundle;
import br.eng.rodrigogml.rfw.kernel.logger.RFWLogger;
import br.eng.rodrigogml.rfw.kernel.utils.RUWiki;
import br.eng.rodrigogml.rfw.vaadin.components.RFWButtonToggle;
import br.eng.rodrigogml.rfw.vaadin.interfaces.RFWDialogButtonClickListener;
import br.eng.rodrigogml.rfw.vaadin.interfaces.RFWDialogButtonClickListener.DialogButton;
import br.eng.rodrigogml.rfw.vaadin.interfaces.RFWUI;

/**
 * Description: Classe estática com diversos métodos para otimizar e facilitar a criação e manipulação da interface do Vaadin no padrão do BIS.<br>
 *
 * @author Rodrigo GML
 * @since 10.0 (30 de out de 2020)
 */
public class FWVad {

  /**
   * Deixa o cursor do mouse como o "pointer" (como chamado pela documentação HTML) que na verdade é a "mãozinha com o dedo", mesmo cursor de link.
   */
  public static final String STYLE_CURSOR_POINTER = "biscursor-pointer";
  /**
   * Deixa o componente que suportar com uma borda simples de 1px preta.
   */
  public static final String STYLE_BORDER_SOLID_THIN_BLACK = "bisBoderStyle1";
  /**
   * Deixa o componente que suportar com uma borda simples de 5px preta.
   */
  public static final String STYLE_BORDER_DASHED_5PX_BLACK = "bisBoderStyle2";
  /**
   * Deixa o componente que suportar com o texto em negrito.
   */
  public static final String STYLE_FONT_BOLD = "fontBold";
  /**
   * Deixa o componente que suportar com o texto na na cor "red".
   */
  public static final String STYLE_FONT_RED = "fontRed";
  /**
   * Define a font com uma linha traçada por cima do texto, como se o texto tivesse sido excluído.
   */
  public static final String STYLE_FONT_LINETHROUGH = "fontLineThrough";
  /**
   * Define o alinhamento de algum conteúdo que suporte ao centro.
   */
  public static final String STYLE_ALIGN_CENTER = "alignmentCenter";

  /**
   * Define o alinhamento de algum conteúdo que suporte á direita.
   */
  public static final String STYLE_ALIGN_RIGHT = "alignmentRight";

  /**
   * Deixa o componente que suportar com o background na cor "Gainsboro".
   */
  public static final String STYLE_BACKGROUND_GAINSBORO = "backgroundGainsboro";
  /**
   * Deixa o componente que suportar com o background na cor "black".
   */
  public static final String STYLE_BACKGROUND_BLACK = "backgroundBlack";
  /**
   * Deixa o componente que suportar com o background na cor "red".
   */
  public static final String STYLE_BACKGROUND_RED = "backgroundRed";
  /**
   * Deixa o componente que suportar com o background na cor "blue".
   */
  public static final String STYLE_BACKGROUND_BLUE = "backgroundBlue";
  /**
   * Deixa o componente que suportar com o background na cor "green".
   */
  public static final String STYLE_BACKGROUND_GREEN = "backgroundGreen";
  /**
   * Deixa o componente que suportar com o background na cor "light green".
   */
  public static final String STYLE_BACKGROUND_LIGHTGREEN = "backgroundLightGreen";
  /**
   * Deixa o componente que suportar com o background na cor "light red".
   */
  public static final String STYLE_BACKGROUND_LIGHTRED = "backgroundLightRed";
  /**
   * Estilo para deixar a linha de um grid na cor verde claro. Utilizado por exemplo para indicar valores positivos (crédito) nas contas.
   */
  public static final String STYLE_GRID_BACKGROUND_LIGHTGREEN = "gridBackgroundLightGreen";
  /**
   * Estilo para deixar a linha de um grid na cor vermelho claro. Utilizado por exemplo para indicar valores positivos (crédito) nas contas.
   */
  public static final String STYLE_GRID_BACKGROUND_LIGHTRED = "gridBackgroundLightRed";
  /**
   * Define um style para a célula ou linha de um Grid deixando a font com uma linha cortando o texto, representando uma informação que foi cortada/ignorada/excluída.
   */
  public static final String STYLE_GRID_FONT_LINETHROUGH = "gridFontLineThrough";
  /**
   * Define o style na coluna para remover as margins internas padrões. Recomendado o uso quando o conteúdo da célula é personalizado com um layout próprio.
   */
  public static final String STYLE_GRID_CELL_BORDLESS = "bisGridCellNoMargins";
  /**
   * Define o Style a ser aplicado no label da barra de navegação / Paginação para configura-lo.
   */
  public static final String STYLE_NAVIGATOR_LABEL = "navigatorLabel";
  /**
   * Define um Style no Label para deixar o texto HTML melhor formatado para exibição na tela
   */
  public static final String STYLE_LABEL_RICHTEXTFORMAT = "bisRichTextFormat";
  /**
   * Deixa o Painel sem borda. Utilizado, por exemplo, como um container invisível.
   */
  public static final String STYLE_PANEL_BORDERLESS = ValoTheme.PANEL_BORDERLESS;

  /**
   * Estilo para deixar os componentes Unidos. Pode ser aplicado em:
   * <li>CustomField
   * <li>HorizontalLayout
   */
  public static final String STYLE_COMPONENT_ASSOCIATIVE = "rfwAssociativeComponent";

  /**
   * Diferente do ButtonType que cria alguns botões padronizados usados por todo o sistema, o ButtonStyle tem a finalidade de modificar a aparência do botão para que ele se encaixe em várias partes do sistema.
   */
  public enum ButtonStyle {
    /**
     * Define o tipo de Botão com aparência comum, mais utilizado no sistema.
     */
    NONE,
    /**
     * Cria um botão com as características necessárias apra ser incluido na barra de botões de atalhos.<br>
     * <B>Atenção: </b> Embora o tipo do botão seja criado, esse botões têm apenas ícones distitintos, assim, diferente dos outros estilos que já criam o botão completo, este deve ter o Tip e o Ícone redefeinido!
     */
    SHORTCUT_BUTTON,
    /**
     * Cria um botão com a cara de "default", sendo o botão que chama a atenção entre os outros, indicando o caminho que o usuário provavelmente deve seguir.
     */
    PRIMARY,
    /**
     * COnfigura o botão com uma aparência amigavel, usado por exemplo em botões de OK e confirmações em geral.
     */
    FRIENDLY,
    /**
     * Configura o botão com uma aparência de perigo, usado em botões de cancelar ou de confirmação de exclusão/perda de dados.
     */
    DANGER,
    /**
     * Configura o botão para parecer com um link comum. Util por exemplo para mostrar botões (ou mesmo links) em uma lista menos poluída de "gráficos UI".<br>
     * Este modelo é usado por exemplo pra exibir os nomes dos relatórios das páginas.
     */
    LINK,
  }

  /**
   * Define alguns tipos de botões que são usados pelo sistema todo, mantendo assim a mesma característica e pedrão visual em todas as telas.
   */
  public enum ButtonType {
    /**
     * Botão de desfazer
     */
    UNDO,
    /**
     * Botão de Salvar alterações
     */
    SAVE,
    /**
     * Cria um botão utilizado para inticar um livro de endereços de URLs;Internet.
     */
    BOOKMARK,
    /**
     * Botão de navegação/paginação usado para enviar o registro para o primeiro item da lista.
     */
    MOVE_FIRST,
    /**
     * Botão de navegação/paginação usado para enviar o registro para o item anterior da lista.
     */
    MOVE_PREVIOUS,
    /**
     * Botão de navegação/paginação usado para enviar o registro para o próximo item da lista.
     */
    MOVE_NEXT,
    /**
     * Botão de navegação/paginação usado para enviar o registro para o último item da lista.
     */
    MOVE_LAST,
    /**
     * Botão de navegação/paginação usado para enviar o registro para cima.
     */
    MOVE_UP,
    /**
     * Botão de navegação/paginação usado para enviar o registro para baixo.
     */
    MOVE_DOWN,
    /**
     * Botão utilizado para adicionar Itens a uma lista/tabela.
     */
    ADD_LISTITEM,
    /**
     * Botão utilizado apra remover itens de uma lista/tabela.
     */
    REMOVE_LISTITEM,
    /**
     * Cria um botão utilizado para abrir/alterar prestações de parcelamento.
     */
    INSTALLMENTS,
    /**
     * Botão utilizado para alterar/editar itens de uma lista/tabela.
     */
    EDIT_LISTITEM,
    /**
     * Botão utilizado para executar uma ação de busca de dados no sistema, como nos formulários de filtros dos dados.
     */
    SEARCH,
    /**
     * Botão utilizado para procurar um item para referência, como abrir uma janela de picker e encontrar um objeto. Utilizado quando queremos encontrar um objeto para copia-lo ou edita-lo, quando utilizar o picker para "associar" os objetos, utilizar o botão {@link ButtonType#LINK}.
     */
    SEARCH_ITEM,
    /**
     * Botão utilizado nos formulários para indicar a limpeza dos campos dos formulários, deixando todos os campos em branco novamente.
     */
    CLEARFIELDS,
    /**
     * Botão de "Fechar". Utilizado para fechar caixas de diálogo ou janelas "popup" de informação.
     */
    CLOSE,
    /**
     * Cria o botão de "Inserir" Utilizado para iniciar as operações de realizar um novo cadastro de objeto.
     */
    INSERTITEM,
    /**
     * Botão utiliado para "Enviar alguma informação" para servidor remoto (nuvem).
     */
    CLOUNDSEND,
    /**
     * Botão utiliado para "Recuperar alguma informação" de servidor remoto (nuvem).
     */
    CLOUDGET,
    /**
     * Botão utiliado para "Sincronizar alguma informação" com um servidor remoto (nuvem).
     */
    CLOUNDSYNC,
    /**
     * Botão de "Transmitir Dados", no sentido de enviar dados para outro sistema.
     */
    TRANSMIT,
    /**
     * Cria o botão de "Auditar". Utilizado para marcar/desmarcar um item como auditado. De forma geral, um item auditado tem a finalidade de impedir que alterações sejam feitas por qualquer usuário.
     */
    AUDITITEM,
    /**
     * Cria o botão de "Ignorar" um item da listagem. Esse comando é utilizado em items que o usuário pode solicitar por ignorar (não excluir), similar a desabilitar, mas para outros contextos.<br>
     * Utilizado a primeira vez para "ignorar" lançamentos de extratos importados por arquivos OFX.
     */
    IGNOREITEM,
    /**
     * Cria o botão de "Transferência de Fundos (Valores)" Utilizado para simbolizar transferência de valores em dinheiro.
     */
    TRANSFERFUNDS,
    /**
     * Cria o botão de "Duplicar" Utilizado para iniciar as operações de realizar um novo cadastro de objeto baseado em outro que já existe.
     */
    DUPLICATEITEM,
    /**
     * Cria o botão de "Nova Versão". Utilizado quando os objetos são imutáveis, ou seja, não podem ser alterados mas podem ser desabilitados. Esta operação pommite que o usuário crie um novo objeto baseando nos dados do objeto original. Mas ao confirmar a inclusão o objeto anterior é automaticamente desativado ou excluído.
     */
    VERSIONITEM,
    /**
     * Cria o botão de "Alterar" Utilizado para iniciar as operações de alterar cadastro de objeto.
     */
    EDITITEM,
    /**
     * Cria o botão de "Excluir". Utilizado para excluir algum objeto de cadastro do banco de dados.
     */
    DELETEITEM,
    /**
     * Cria o botão de "Visualizar". Utilizado para visualizar/detalhar um item.
     */
    VIEWITEM,
    /**
     * Cria o botão de "Ativar". Utilizado para a função contrária do {@link #DEACTIVATEITEM}.
     */
    ACTIVATEITEM,
    /**
     * Cria o botão de "Desativar". Utilizado para desativar algum objeto de cadastro do banco de dados. Objetos são desativados quando não podem ser excluídos do sistema.
     */
    DEACTIVATEITEM,
    /**
     * Cria o botão de "Importar Dados". Utilizado nas funções em que o sistema deve importar alguma informação externa.
     */
    IMPORTDATA,
    /**
     * Cria o botão de "Confirmar". Usado para confirmar as operações de confirmação de operações.
     */
    CONFIRM,
    /**
     * Cria o botão de "Confirmar Todos". Usado para confirmar diversas operações pendentes.
     */
    CONFIRMALL,
    /**
     * Cria o botão de "Cancelar", Usado geralmente de forma a representar a ação contrária do botão {@link #CONFIRM}
     */
    CANCEL,
    /**
     * Cria o botão de "Cancelar Todos", Usado em situações que temos várias ações ou situação que podem ser canceladas, apresentando uma ação única para cancelar todos ao mesmo tempo.
     */
    CANCELALL,
    /**
     * Cria o botão de "Help", Usado em algumas telas para exibir uma ajuda maior e mais detalhada que os "tooltips" dos campos.
     */
    HELP,
    /**
     * Cria o botão de "Picker". O botão de picker é utilizado quando desejamos "buscar" uma informação para associação em um campo. Por exemplo no campo CEP, colocamos o botão de picker para abrir uma tela e permitir que o usuário encontre um CEP, e ao confirmar ele já traga os dados para o campo.
     */
    PICKER,
    /**
     * Cria o botão da "varinha mágica". Esse botão é utilizado quando desejamos que o BIS faça alguma tarefa automatizada, como se fosse "mágica", agilizando os processos e tarefas do usuário.
     */
    MAGIC,
    /**
     * Cria o botão para associar informações, usado quando queremos associar um cadastro (objeto do BIS) à outro.
     */
    LINK,
    /**
     * Cria o botão para ação contrária do botão {@link ButtonType#LINK}.
     */
    UNLINK,
    /**
     * Cria um botão para a ação de recuperar mensagens no servidor de e-mail.
     */
    FETCHMAIL,
    /**
     * Cria um botão para a ação de recuperar senha.
     */
    PASSWORDRECOVERY,
    /**
     * Cria um botão para a ação de incluir uma pessoa (jurídica ou física) no sistema de forma rápida).
     */
    QUICKADD_PERSON,
    /**
     * Botão com ícone de repetir valor do campo anterior.
     */
    REPEATVALUE,
    /**
     * Botão para realizar a ação de pagar alguma conta.
     */
    PAY,
    /**
     * Botão criato para exibir um tipo de relatório. Este botão deve ser agrupado dentro do botão {@link #REPORTACTIONS}.
     */
    REPORTITEM,
    /**
     * Estilo do botão utilizado para o "Mais Ações"
     */
    MOREACTIONS,
    /**
     * Estilo do botão utilizado para agrupar os Relatórios, deixa o botão no estilo do "Mais Ações" mas com a cara do botão de relatórios.
     */
    REPORTACTIONS,
    /**
     * Cria um botão de "Swap", utilizado para inverter conteúdo de alguns campos, pasando o valor de A para B, e de B para A.
     */
    SWAPCONTENT,
    /**
     * Cria o botão com ícone de Download, utilizado para baixar qualquer tipo de arquivo do servidor para a máquina do cliente.
     */
    DOWNLOAD,
    /**
     * Cria o botão com ícone de Download de arquivo ZIP, utilizado para baixar qualquer tipo de arquivo do servidor para a máquina do cliente que seja compactado no formato ZIP.
     */
    DOWNLOAD_ZIP,
    /**
     * Cria um botão de envio de E-mail
     */
    SENDMAIL,
    /**
     * Cria o botão com ícone de Upload, utilizado para enviar qualquer tipo de arquivo da máquina do cliente para o servidor.
     */
    UPLOAD,
    /**
     * Cria um botão para "atualizar" no sentido de recarregar, ou refazer alguma tarefa.
     */
    REFRESH,
    /**
     * Botão utilizado na geração dos DashIt. Indica a ação de pegar os filtros e dados fornecidos pelo usuário para gerar o relatório.
     */
    DASHIT_CREATE,
    /**
     * Botão utilizado para abrir um DashIt. Utilizado nas telas de cadastro que abrem diretamente um relatório, ou mesmo entre os relatórios quando migramos de um para outro.
     */
    DASHIT,
    /**
     * Botão utilizado para abrir uma tela de configurações/definições.
     */
    CONFIG,
    /**
     * Cria um botão com o íncone do excel, utilizado normalmente para exportar dados em formato de planilha do excel
     */
    EXCEL,
    /**
     * Cria um botão para organizar dados em ordem
     */
    SORT,
    /**
     * Cria um botão com uma calculadora, normalmente utilizado associado à campos de valores para que o BIS preencha o valor baseado em algum cálculo predefinido.
     */
    CALCULATOR,
    /**
     * Cria o botão de 'play', utilizado para iniciar/resumir alguma ação/tarefa.
     */
    PLAY,
    /**
     * Cria o botão de 'pause', utilizado para pausar alguma ação/tarefa.
     */
    PAUSE,
    /**
     * Cria o botão de 'play', utilizado para finalizar alguma ação/tarefa.
     */
    STOP,
    /**
     * Cria o botão para executar uma rotina/tarefa de teste etc.
     */
    EXECUTE,
    /**
     * Botão de avançar "next", botão utilizado para navegar entre páginas ou etapas, como as de um Wizard. Não confundir com os botões de barra de navegação de itens {@link #MOVE_NEXT}.
     */
    NEXT,
    /**
     * Botão de recuar "previous", botão utilizado para navegar entre páginas ou etapas, como as de um Wizard. Não confundir com os botões de barra de navegação de itens {@link #MOVE_PREVIOUS}.
     */
    PREVIOUS,
    /**
     * Botão utilizado apra representar a ação de executar rotinas de manutenção do sistema.
     */
    MAINTENANCE,
  }

  /**
   * Construtor privado para classe estática.
   */
  private FWVad() {
  }

  /**
   * Este método cria uma janela de dialogo MODAL para exibir uma mensagem de opções para o usuário, como uma tela de confirmar uma ação com botões configuráveis.<br>
   * O click em qualquer um dos botões fechará o dialog imediatamente, antes de chamar o evento no listener.
   *
   * @param title Título da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @param listener Listener para receber qual botão foi clicado.
   * @param defaultButton Define o botão que terá o foco. Podendo ser pressionado de imediato com o espaço ou ENTER. Se nulo, o primeiro botão receberá o foco. O botão passado aqui deverá estar entre os botões do parâmetro buttons, ou não terá efeito algum.
   * @param cancelButton Define o botão que será acionado com a tecla ESC. Se nulo, nenhum botão recebe esta função. O botão passado aqui deverá estar entre os botões do parâmetro buttons, ou não terá efeito algum.
   * @param buttons Botões a serem exibidos para o usuário. Se passado nulo, será exibido apenas o botão de OK. Os botões serão colocados na ordem do array.
   *
   * @return Janela no formato de caixa de diálogo pronta para se exibida.
   */
  public static Window createDialogQuestion(String title, String message, RFWDialogButtonClickListener listener, DialogButton defaultButton, DialogButton cancelButton, DialogButton... buttons) {
    return createDialog(title, message, new ThemeResource("icon/dialog_64.png"), null, listener, defaultButton, cancelButton, buttons);
  }

  /**
   * Este método cria uma janela de dialogo MODAL para exibir uma mensagem de opções para o usuário, como uma tela de confirmar uma ação com botões configuráveis.<br>
   * O click em qualquer um dos botões fechará o dialog imediatamente, antes de chamar o evento no listener.
   *
   * @param title Título da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @param icon Define o ícone a ser exibido no dialogo.
   * @param listener Listener para receber qual botão foi clicado.
   * @param defaultButton Define o botão que terá o foco. Podendo ser pressionado de imediato com o espaço ou ENTER. Se nulo, o primeiro botão receberá o foco. O botão passado aqui deverá estar entre os botões do parâmetro buttons, ou não terá efeito algum.
   * @param cancelButton Define o botão que será acionado com a tecla ESC. Se nulo, nenhum botão recebe esta função. O botão passado aqui deverá estar entre os botões do parâmetro buttons, ou não terá efeito algum.
   * @param buttons Botões a serem exibidos para o usuário. Se passado nulo, será exibido apenas o botão de OK. Os botões serão colocados na ordem do array.
   *
   * @return Janela no formato de caixa de diálogo pronta para se exibida.
   */
  private static Window createDialog(String title, String message, ThemeResource icon, String style, RFWDialogButtonClickListener listener, DialogButton defaultButton, DialogButton cancelButton, DialogButton... buttons) {
    final Window w = new Window(title);
    w.setModal(true);
    w.setClosable(false);
    w.setResizable(false);
    w.setWidth("75%");
    w.setHeightUndefined();
    w.center();
    w.setIcon(icon);
    if (style != null) w.addStyleName(style);

    // Conteúdo da Janela
    VerticalLayout vl = new VerticalLayout();
    vl.setMargin(true);

    Label lb = new Label(message, ContentMode.HTML);
    lb.setWidth("100%");
    vl.addComponent(lb);
    vl.setComponentAlignment(lb, Alignment.MIDDLE_LEFT);
    vl.setExpandRatio(lb, 1f);
    w.setContent(vl);

    if (buttons == null || buttons.length == 0) buttons = new DialogButton[] { DialogButton.CONFIRM };

    if (defaultButton == null) defaultButton = buttons[0];

    HorizontalLayout hlButtons = new HorizontalLayout();
    hlButtons.setMargin(false);
    hlButtons.setSpacing(true);
    for (int i = 0; i < buttons.length; i++) {
      final DialogButton dBut = buttons[i];

      Button button = null;
      switch (dBut) {
        case CANCEL:
          button = createButton(ButtonType.CANCEL, e -> {
            w.close();
            if (listener != null) listener.clickedButton(dBut);
          });
          break;
        case CONFIRM:
          button = createButton(ButtonType.CONFIRM, e -> {
            w.close();
            if (listener != null) listener.clickedButton(dBut);
          });
          break;
        case NO:
          button = createButton(ButtonType.CANCEL, e -> {
            w.close();
            if (listener != null) listener.clickedButton(dBut);
          });
          button.setCaption("Não");
          break;
        case YES:
          button = createButton(ButtonType.CONFIRM, e -> {
            w.close();
            if (listener != null) listener.clickedButton(dBut);
          });
          button.setCaption("Sim");
          break;
      }
      hlButtons.addComponent(button);

      if (dBut == cancelButton) button.setClickShortcut(KeyCode.ESCAPE);
      if (dBut == defaultButton) button.focus();
    }
    vl.addComponent(hlButtons);
    vl.setComponentAlignment(hlButtons, Alignment.MIDDLE_CENTER);

    return w;
  }

  /**
   * Este método cria uma janela de dialogo MODAL para exibir uma mensagem de Infomação.
   *
   * @param title Título da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @param iconPath
   * @return Janela no formato de caixa de diálogo pronta para se exibida.
   */
  public static Window createDialogInformation(String title, String message, String iconPath) {
    final Window w = new Window(title);
    w.setModal(true);
    w.setClosable(true);
    w.setWidth("80%");
    w.setHeightUndefined();
    w.center();
    w.setIcon(new ThemeResource(iconPath));

    // Conteúdo da Janela
    VerticalLayout vl = new VerticalLayout();
    vl.setMargin(true);

    Label lb = new Label(message, ContentMode.HTML);
    lb.setWidth("100%");
    vl.addComponent(lb);
    vl.setComponentAlignment(lb, Alignment.MIDDLE_LEFT);
    vl.setExpandRatio(lb, 1f);

    final Button button = createButton(ButtonType.CLOSE, e -> w.close());
    button.setClickShortcut(KeyCode.ESCAPE);
    vl.addComponent(button);
    vl.setComponentAlignment(button, Alignment.MIDDLE_CENTER);

    w.setContent(vl);

    button.focus();

    return w;
  }

  /**
   * Exibe uma mensagem de Erro com o título padrão 'Erro no Sistema'.
   *
   * @param message Mensagem a ser exibida ao usuário.
   */
  public static void showWarningMessage(String message) {
    Window dialog = createDialogWarning("Erro no Sistema :´(", message);
    addWindow(dialog);
  }

  /**
   * Exibe uma mensagem de Erro com o título padrão 'Erro no Sistema' e anexa a instrução de reiniciar a sessão se o problema persistir ao fim da mensagem.
   *
   * @param message Mensagem a ser exibida ao usuário.
   * @param ex
   */
  public static void showErrorMessage(String message, Throwable ex) {
    showErrorMessage("Erro no Sistema :´(", message + "<br><br><i>É recomendável reiniciar a sessão. Se o problema persistir, contate o suporte.</i>", ex);
  }

  /**
   * Exibe uma mensagem de erro para o usuário.
   *
   * @param title Título da Caixa de mensagem.
   * @param message Mensagem a ser exibida.
   * @param ex
   */
  public static void showErrorMessage(String title, String message, Throwable ex) {
    Window dialog = createDialogError(title, message, ex);
    addWindow(dialog);
  }

  /**
   * Este método cria uma janela de dialogo MODAL para exibir uma mensagem de erro.
   *
   * @param title Título da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @return Janela no formato de caixa de diálogo pronta para se exibida.
   */
  public static Window createDialogError(String title, String message, Throwable ex) {
    final Window w = new Window(title);
    w.setModal(true);
    w.setClosable(true);
    w.setWidth("80%");
    w.setHeightUndefined();
    w.center();
    w.setIcon(new ThemeResource("icon/error_64.png"));
    w.addStyleName("bisErrorDialog");

    // Conteúdo da Janela
    VerticalLayout vl = new VerticalLayout();
    vl.setMargin(true);

    Label lb = new Label(message, ContentMode.HTML);
    lb.setWidth("100%");
    vl.addComponent(lb);
    vl.setComponentAlignment(lb, Alignment.MIDDLE_LEFT);
    vl.setExpandRatio(lb, 1f);

    GridLayout gl = new GridLayout(3, 1);
    gl.setWidth("100%");
    gl.setMargin(false);
    gl.setSpacing(false);
    gl.setColumnExpandRatio(1, 1F);

    final Button button = createButton(ButtonType.CLOSE, e -> w.close());
    button.setClickShortcut(KeyCode.ESCAPE);
    gl.addComponent(button, 1, 0);
    gl.setComponentAlignment(button, Alignment.MIDDLE_CENTER);

    if (ex != null) {
      Button showLog = new Button("Registro Técnico");
      showLog.addClickListener(evt -> addWindow(createDialogException(ex)));
      setButtonStyle(showLog, ButtonStyle.LINK);
      showLog.setWidth("140px");
      gl.addComponent(showLog, 0, 0);
      gl.setComponentAlignment(showLog, Alignment.BOTTOM_LEFT);
      Label label = new Label();
      label.setWidth("140px");
      gl.addComponent(label, 2, 0);
    }
    vl.addComponent(gl);

    w.setContent(vl);

    button.focus();

    return w;
  }

  /**
   * Este método cria uma janela de dialogo MODAL para exibir uma mensagem de Warnings.
   *
   * @param title Título da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @return Janela no formato de caixa de diálogo pronta para se exibida.
   */
  public static Window createDialogWarning(String title, String message) {
    final Window w = new Window(title);
    w.setModal(true);
    w.setClosable(true);
    w.setWidth("80%");
    w.setHeightUndefined();
    w.center();
    w.setIcon(new ThemeResource("icon/warning_64.png"));
    w.addStyleName("bisWarningDialog");

    // Conteúdo da Janela
    VerticalLayout vl = new VerticalLayout();
    vl.setMargin(true);

    Label lb = new Label(message, ContentMode.HTML);
    lb.setWidth("100%");
    vl.addComponent(lb);
    vl.setComponentAlignment(lb, Alignment.MIDDLE_LEFT);
    vl.setExpandRatio(lb, 1f);

    GridLayout gl = new GridLayout(3, 1);
    gl.setWidth("100%");
    gl.setMargin(false);
    gl.setSpacing(false);
    gl.setColumnExpandRatio(1, 1F);

    final Button button = createButton(ButtonType.CLOSE, e -> w.close());
    button.setClickShortcut(KeyCode.ESCAPE);
    vl.addComponent(button);
    vl.setComponentAlignment(button, Alignment.MIDDLE_CENTER);

    w.setContent(vl);

    button.focus();

    return w;
  }

  /**
   * Mosta um dialog paca exibir uma pilha de uma exception.
   */
  public static Window createDialogException(Throwable ex) {
    final Window w = new Window("Detalhes Técnicos do Erro");
    w.setModal(true);
    w.addStyleName("bisErrorDialog");
    w.setClosable(true);
    w.setWidth("90%");
    w.setHeight("90%");
    w.center();
    w.setIcon(new ThemeResource("icon/bug_64.png"));

    // Conteúdo da Janela
    VerticalLayout vl = new VerticalLayout();
    vl.setMargin(true);
    vl.setSizeFull();

    StringWriter sw = new StringWriter();
    ex.printStackTrace(new PrintWriter(sw));

    Label lb = new Label(sw.toString(), ContentMode.PREFORMATTED);
    lb.setSizeUndefined();

    Panel scrollPanel = createScrollPanel(lb);
    vl.addComponent(scrollPanel);
    vl.setExpandRatio(scrollPanel, 1f);

    final Button button = createButton(ButtonType.CLOSE, e -> w.close());
    button.setClickShortcut(KeyCode.ESCAPE);
    vl.addComponent(button);
    vl.setComponentAlignment(button, Alignment.MIDDLE_CENTER);

    w.setContent(vl);

    button.focus();

    return w;
  }

  /**
   * Cria um botão padrão totalmente configurado.
   *
   * @param buttonType Definição do botão a ser criado.
   * @param listener
   * @return Botão criado já com as carecteristicas do padrão visual.
   */
  public static Button createButton(ButtonType buttonType, ClickListener listener) {
    Button button = new Button();
    if (buttonType != null) setButtonType(buttonType, button);
    if (listener != null) button.addClickListener(listener);
    return button;
  }

  /**
   * Faz todas as definições no Objeto "button" conforme a definição de UI padrão do BIS.
   *
   * @param buttonType Definição da UI padrão a ser aplicada no Botão.
   * @param button Objeto "botão" para ser formatado conforme padrão da UI do BIS.
   */
  public static void setButtonType(ButtonType buttonType, Button button) {
    switch (buttonType) {
      case SENDMAIL:
        button.setCaption("Enviar Email");
        button.setIcon(new ThemeResource("icon/sendmail_24.png"));
        break;
      case TRANSMIT:
        button.setCaption("Transmitir");
        button.setIcon(new ThemeResource("icon/transmitted_24.png"));
        break;
      case UNDO:
        button.setCaption(null);
        button.setDescription("Desfazer");
        button.setIcon(new ThemeResource("icon/undo_24.png"));
        break;
      case SAVE:
        button.setCaption("Salvar");
        button.setDescription("Salvar as Alterações");
        button.setIcon(new ThemeResource("icon/save_24.png"));
        break;
      case BOOKMARK:
        button.setCaption(null);
        button.setDescription("Endereços de Sites");
        button.setIcon(new ThemeResource("icon/bookmark_24.png"));
        break;
      case SWAPCONTENT:
        button.setCaption(null);
        button.setDescription("Inverte os valores dos campos.");
        button.setIcon(new ThemeResource("icon/swap_24.png"));
        break;
      case SEARCH:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.PRIMARY);
        button.setIcon(new ThemeResource("icon/search_24.png"));
        break;
      case SEARCH_ITEM:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/search_24.png"));
        break;
      case CLEARFIELDS:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/clean_24.png"));
        break;
      case CLOSE:
        button.setCaption("Fechar");
        setButtonStyle(button, ButtonStyle.PRIMARY);
        button.setIcon(new ThemeResource("icon/close_24.png"));
        break;
      case CLOUNDSEND:
        button.setCaption("Enviar para Servidor Remoto");
        button.setIcon(new ThemeResource("icon/cloudsend_24.png"));
        break;
      case CLOUDGET:
        button.setCaption("Download de Servidor Remoto");
        button.setIcon(new ThemeResource("icon/cloudget_24.png"));
        break;
      case CLOUNDSYNC:
        button.setCaption("Sincronizar com Servidor Remoto");
        button.setIcon(new ThemeResource("icon/cloudsync_24.png"));
        break;
      case INSERTITEM:
        button.setCaption("Inserir");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/insert_24.png"));
        break;
      case AUDITITEM:
        button.setCaption("Auditar");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/audited_24.png"));
        break;
      case IGNOREITEM:
        button.setCaption("Ignorar");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/ignore_24.png"));
        break;
      case TRANSFERFUNDS:
        button.setCaption("Transferir");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/transferFund_24.png"));
        break;
      case DUPLICATEITEM:
        button.setCaption("Duplicar");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/duplicate_24.png"));
        break;
      case VERSIONITEM:
        button.setCaption("Nova Versão");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/version_24.png"));
        break;
      case EDITITEM:
        button.setCaption("Alterar");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/edit_24.png"));
        break;
      case DELETEITEM:
        button.setCaption("Excluir");
        setButtonStyle(button, ButtonStyle.DANGER);
        button.setIcon(new ThemeResource("icon/delete_24.png"));
        break;
      case VIEWITEM:
        button.setCaption("Visualizar");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/visualize_24.png"));
        break;
      case DEACTIVATEITEM:
        button.setCaption("Desativar");
        setButtonStyle(button, ButtonStyle.DANGER);
        button.setIcon(new ThemeResource("icon/deactivate_24.png"));
        break;
      case ACTIVATEITEM:
        button.setCaption("Ativar");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/activate_24.png"));
        break;
      case CANCEL:
        button.setCaption("Cancelar");
        setButtonStyle(button, ButtonStyle.DANGER);
        button.setIcon(new ThemeResource("icon/cancel_24.png"));
        break;
      case CANCELALL:
        button.setCaption("Cancelar Todos");
        setButtonStyle(button, ButtonStyle.DANGER);
        button.setIcon(new ThemeResource("icon/cancelall_24.png"));
        break;
      case HELP:
        button.setCaption("Ajuda");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/help_24.png"));
        break;
      case IMPORTDATA:
        button.setCaption("Importar Dados");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/importdata_24.png"));
        break;
      case CONFIRM:
        button.setCaption("Confirmar");
        setButtonStyle(button, ButtonStyle.FRIENDLY);
        button.setIcon(new ThemeResource("icon/confirm_24.png"));
        break;
      case CONFIRMALL:
        button.setCaption("Confirmar Todos");
        setButtonStyle(button, ButtonStyle.FRIENDLY);
        button.setIcon(new ThemeResource("icon/confirmall_24.png"));
        break;
      case PICKER:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/search_24.png"));
        break;
      case MAGIC:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/magic_24.png"));
        break;
      case LINK:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/link_24.png"));
        break;
      case UNLINK:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/unlink_24.png"));
        break;
      case FETCHMAIL:
        button.setCaption("Verificar Emails");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/mailboxrefresh_24.png"));
        break;
      case PASSWORDRECOVERY:
        button.setCaption("Redefinir Senha");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/resetpass_24.png"));
        break;
      case ADD_LISTITEM:
        button.setCaption("Adicionar");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/add_24.png"));
        break;
      case REMOVE_LISTITEM:
        button.setCaption("Remover");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/remove_24.png"));
        break;
      case INSTALLMENTS:
        button.setCaption("Prestação/Parcelas");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/installment2_24.png"));
        break;
      case EDIT_LISTITEM:
        button.setCaption("Alterar");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/edititem_24.png"));
        break;
      case MOVE_FIRST:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/movefirst_24.png"));
        break;
      case MOVE_LAST:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/movelast_24.png"));
        break;
      case MOVE_NEXT:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/movenext_24.png"));
        break;
      case MOVE_PREVIOUS:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/moveprevious_24.png"));
        break;
      case MOVE_DOWN:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/movedown_24.png"));
        break;
      case MOVE_UP:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/moveup_24.png"));
        break;
      case QUICKADD_PERSON:
        button.setCaption(null);
        button.setDescription("Inclui uma nova Pessoa rapidamente no Sistema");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/quickAddPerson_24.png"));
        break;
      case REPEATVALUE:
        button.setCaption(null);
        button.setDescription("Repete o mesmo valor.");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/repeatvalue_24.png"));
        break;
      case PAY:
        button.setCaption("Pagar/Receber");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/pay2_24.png"));
        break;
      case MOREACTIONS:
        button.setCaption("Mais Ações");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/moreactions_24.png"));
        break;
      case REPORTACTIONS:
        button.setCaption("Relatórios");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/report_24.png"));
        break;
      case REPORTITEM:
        button.setCaption("Relatório");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/report_24.png"));
        break;
      case DOWNLOAD:
        button.setIcon(new ThemeResource("icon/download_24.png"));
        button.setCaption("Baixar Arquivo");
        break;
      case DOWNLOAD_ZIP:
        button.setIcon(new ThemeResource("icon/zip_24.png"));
        button.setCaption("Baixar Arquivo Compactado");
        break;
      case UPLOAD:
        button.setIcon(new ThemeResource("icon/upload_24.png"));
        button.setCaption("Enviar Arquivo");
        break;
      case REFRESH:
        button.setIcon(new ThemeResource("icon/refresh_24.png"));
        button.setCaption("Atualizar");
        break;
      case DASHIT_CREATE:
        button.setIcon(new ThemeResource("icon/dashitcreate_24.png"));
        button.setCaption("Gerar Relatório");
        setButtonStyle(button, ButtonStyle.FRIENDLY);
        break;
      case DASHIT:
        button.setIcon(new ThemeResource("icon/dashit_24.png"));
        button.setCaption("Abrir Relatório");
        break;
      case CONFIG:
        button.setIcon(new ThemeResource("icon/config_24.png"));
        button.setCaption("Configurar");
        break;
      case EXCEL:
        button.setIcon(new ThemeResource("icon/excel_24.png"));
        button.setCaption("Exportar para Excel");
        break;
      case CALCULATOR:
        button.setIcon(new ThemeResource("icon/calc_24.png"));
        button.setCaption(null);
        break;
      case SORT:
        button.setIcon(new ThemeResource("icon/sort_24.png"));
        button.setCaption("Ordenar");
        break;
      case PAUSE:
        button.setIcon(new ThemeResource("icon/pause_24.png"));
        button.setCaption(null);
        break;
      case PLAY:
        button.setIcon(new ThemeResource("icon/play_24.png"));
        button.setCaption(null);
        break;
      case STOP:
        button.setIcon(new ThemeResource("icon/stop_24.png"));
        button.setCaption(null);
        break;
      case EXECUTE:
        button.setIcon(new ThemeResource("icon/execute_24.png"));
        button.setCaption("Executar");
        break;
      case NEXT:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/next_24.png"));
        break;
      case PREVIOUS:
        button.setCaption(null);
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/previous_24.png"));
        break;
      case MAINTENANCE:
        button.setCaption("Manutenção");
        button.setDescription("Executar a rotina de manutenção dos dados.");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/maintenance_24.png"));
        break;
    }
  }

  /**
   * Define o estilo em algum botão.<br>
   *
   * @param buttonStyle Estilo a ser definido no botão.
   */
  public static void setButtonStyle(Button button, ButtonStyle buttonStyle) {
    // Atenção: Se não for passado o estilo None, não removemos estilos antigos, permitindo que os estilos se somem se necessário.
    switch (buttonStyle) {
      case NONE:
        // Remove os estilos conhecidos
        button.removeStyleName(ValoTheme.BUTTON_QUIET);
        button.removeStyleName("bisButtonShortcut");
        button.removeStyleName(ValoTheme.BUTTON_PRIMARY);
        button.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
        button.removeStyleName(ValoTheme.BUTTON_DANGER);
        button.removeStyleName(ValoTheme.BUTTON_LINK);
        break;
      case DANGER:
        button.addStyleName(ValoTheme.BUTTON_DANGER);
        break;
      case FRIENDLY:
        button.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        break;
      case LINK:
        button.addStyleName(ValoTheme.BUTTON_LINK);
        break;
      case PRIMARY:
        button.addStyleName(ValoTheme.BUTTON_PRIMARY);
        break;
      case SHORTCUT_BUTTON:
        button.setCaption(null);
        button.addStyleName(ValoTheme.BUTTON_QUIET);
        button.addStyleName("bisButtonShortcut");
        break;
    }
  }

  /**
   * Exibe uma mensagem de notificação que some sozinha depois de um tempo. <br>
   * <B>ATENÇÃO:</B> Nenhuma notificação faz busca da mensagem por bundle, quando necessário fazer a busca antes da chamada deste método.
   *
   * @param caption Título da Notificação
   * @param text texto/conteúdo de exibição da notificação
   * @param msec Tempo em milisegundos para exibição da mensagem. Se passado -1 exige que se clique na mensagem.
   * @param icon Ícone para ser exibido na notificação, se nulo deixa sem ícone.
   * @param type Tipo da notificação conforme definição do Vaadin. Veja em {@link Type}.
   * @param position Posição de aparência da notificação. Veja detalhes em {@link Position}.
   * @param style Estilo do CSS a ser colocado na Notificação. Permite um controle maior sobre a aparência da notificação.
   */
  public static void showNotification(String caption, String text, int msec, Resource icon, Type type, Position position, String style) {
    Notification notification = new Notification(caption, text, type);
    notification.setDelayMsec(msec);
    if (style != null) notification.setStyleName(style);
    notification.setHtmlContentAllowed(true);
    notification.setPosition(position);
    if (icon != null) notification.setIcon(icon);
    notification.show(Page.getCurrent());
  }

  /**
   * Exibe uma notificação com aspecto de sucesso.<br>
   * Utilizada para operações bem sucessidas.<br>
   * Assim informamos que tudo foi realizado com sucesso, mas não exigimos do usuário um clique para confirmar a informação. Deixando assim o uso mais rápido e simples.
   *
   * @param msg Mensagem de confirmação da operação a ser exibida.
   */
  public static void showNotificationSuccess(String msg) {
    showNotification("", msg, 1000, new ThemeResource("icon/success_64.png"), Type.HUMANIZED_MESSAGE, Position.TOP_CENTER, "bisNotificationSuccess");
  }

  /**
   * Exibe uma notificação com aspecto de sucesso.<br>
   * Utilizada para operações bem sucessidas.<br>
   * Assim informamos que tudo foi realizado com sucesso, mas não exigimos do usuário um clique para confirmar a informação. Deixando assim o uso mais rápido e simples.
   *
   * @param msg Mensagem de confirmação da operação a ser exibida.
   * @param subMsg Segunda linha da mensagem com uma fonte menor, permite exibir uma informação adicional
   */
  public static void showNotificationSuccess(String msg, String subMsg) {
    showNotification("", msg + "<br><small>" + subMsg + "</small>", 1000, new ThemeResource("icon/success_64.png"), Type.HUMANIZED_MESSAGE, Position.TOP_CENTER, "bisNotificationSuccess");
  }

  /**
   * Cria uma notificação de Bandeja de Sistema. Normalmente utilizada para emitir alertas do sistema de tarefas e eventos desrelacionados com a operação atual do usuário. Como uma mensagem que chegou, um evento que ocorreu em segundo plano, etc.
   *
   * @param title Título da notificação
   * @param msg Mensagem da Notificação
   * @param icon Ícona da notificação. Pode ser nulo para não exibir nenhum, mas é recomendado utilizar para identificação mais rápida do usuário de que se trata a notificação.
   */
  public static void showNotificationTray(String title, String msg, Resource icon) {
    showNotification(title, msg, 1000, icon, Type.TRAY_NOTIFICATION, Position.TOP_RIGHT, "bisTrayNotification");
  }

  /**
   * Recupera a instância da UI de acordo com a Thread que chama o método.
   *
   * @return Instância do UI do Vaadin em execução no momento.
   */
  public static UI getUI() {
    return CurrentInstance.get(UI.class);
  }

  /**
   * Recupera a instância da UI de acordo com a Thread que chama o método, desde que ela implemente a {@link RFWUI}.<br>
   * Caso ela não implemente ou a UI não seja encontrada é retornado nulo, sem lançamento de exceptions.
   *
   * @return Instância do {@link RFWUI} do Vaadin em execução no momento, caso a UI implemente a interface.
   */
  public static RFWUI getRFWUI() {
    UI ui = getUI();
    if (ui != null && (ui instanceof RFWUI)) return (RFWUI) ui;
    return null;
  }

  /**
   * Coloca uma nova janela na interface do usuário.
   *
   * @param window Janela a ser Exibida para o usuário
   */
  public static void addWindow(Window window) {
    getUI().addWindow(window);
  }

  /**
   * Define uma mensagem de erro em um campo da UI. Em geral utilizado para anexar mensagens de falha de validação do seu conteúdo.
   *
   * @param field Campo que implemente {@link HasValue} do Vaadin. Se o campo não suportar a notificação, nada é feito.
   * @param msg Mensagem a ser exibida.
   * @return true caso a mensagem tenha sido associada ao Field, false caso contrário.
   */
  public static boolean setComponentError(HasValue<?> field, String msg) {
    UserError error = null;
    if (msg != null) error = new UserError(msg);

    if (field instanceof AbstractComponent) {
      ((AbstractComponent) field).setComponentError(error);
      return true;
    }

    RFWLogger.logImprovement("Tipo de field desconhecido para definir a mensagem de 'ComponentError': '${0}'.", new String[] { field.getClass().getCanonicalName() });
    return false;
  }

  /**
   * Mensagens de validações são as mensagens usadas para informar o usuário que algum dado fornecido está com algum tipo de problema (errado, inconsistente, faltando, etc.) e que por isso a operação não prosseguirá. Usado também quando a informação não é fornecida pelo usuário, mas recuperada pelo sistema, como por exemplo, quando desejamos alterar um item selecionado na tela e durante sua busca
   * descobrimos que o item não está mais no banco de dados.<br>
   * <B>ATENÇÃO:</B> Nenhuma notificação faz busca da mensagem por bundle, quando necessário fazer a busca antes da chamada deste método.
   */
  public static void showValidationMessage(String msg) {
    showInformationMessage("Problema de Validação", msg, "icon/validation_64.png");
  }

  /**
   * Exibe uma mensagem de informação para o usuário.
   *
   * @param title Título da Caixa de mensagem.
   * @param message Mensagem a ser exibida.
   * @param iconPath Caminho do Ícone a ser exibido na mensagem. Padrão em 64x64px.
   */
  public static void showInformationMessage(String title, String message, String iconPath) {
    Window dialog = createDialogInformation(title, message, iconPath);
    addWindow(dialog);
  }

  /**
   * Cria um Panel invisível que cria scrolls caso o conteúdo definido dentro dele seja maior do que o próprio Panel.<br>
   * <B>ATENÇÃO:</B> Os scrolls só são gerados caso o conteúdo do panel não tenha o tamanho definido na direção que se deseja o Scroll.
   */
  public static Panel createScrollPanel() {
    Panel panel = new Panel();
    panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
    panel.setSizeFull();
    return panel;
  }

  /**
   * Cria um Panel invisível que cria scrolls caso o conteúdo definido dentro dele seja maior do que o próprio Panel.<br>
   * <B>ATENÇÃO:</B> Os scrolls só são gerados caso o conteúdo do panel não tenha o tamanho definido na direção que se deseja o Scroll.
   *
   * @param content Conteúdo a ser colocado dentro do Panel. NOTE QUE JÁ DEVE VIR COM A DEFINIÇÃO DE ALTURA "INDEFINIDA"!!!
   */
  public static Panel createScrollPanel(Component content) {
    Panel panel = createScrollPanel();
    panel.setContent(content);
    return panel;
  }

  /**
   * Exibe uma mensagem de erro a partir de uma exceção qualquer.
   *
   * @param e Exception a ser transformada em Mensagem.
   */
  public static void showUncatchExceptionMessage(Throwable e) {
    String message = "<br>Foi detectado um BUG no BIS. Reinície sua sessão, se o problema persistir, contate o suporte.<br><br><b>Erro: </b><i>";
    if (e != null) {
      if (e.getCause() != null) {
        message += e.getCause().getClass().getName();
      } else {
        message += e.getClass().getName();
      }
    }
    String msg = RFWBundle.get(e);
    if (msg != null && !"".equals(msg)) {
      message += ": " + msg;
    }
    message += "</i>";

    Window dialog = createDialogBUG("BUG Encontrado", message, e);
    addWindow(dialog);
  }

  /**
   * Este método cria uma janela de dialogo MODAL para exibir uma mensagem de BUG.
   *
   * @param title Título da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @return Janela no formato de caixa de diálogo pronta para se exibida.
   */
  public static Window createDialogBUG(String title, String message, Throwable ex) {
    final Window w = new Window(title);
    w.setModal(true);
    w.addStyleName("bisErrorDialog");
    w.setClosable(true);
    w.setWidth("80%");
    w.setHeightUndefined();
    w.center();
    w.setIcon(new ThemeResource("icon/bug_64.png"));

    // Conteúdo da Janela
    VerticalLayout vl = new VerticalLayout();
    vl.setMargin(true);

    Label lb = new Label(message, ContentMode.HTML);
    lb.setWidth("100%");
    vl.addComponent(lb);
    vl.setComponentAlignment(lb, Alignment.MIDDLE_LEFT);
    vl.setExpandRatio(lb, 1f);

    GridLayout gl = new GridLayout(3, 1);
    gl.setWidth("100%");
    gl.setMargin(false);
    gl.setSpacing(false);
    gl.setColumnExpandRatio(1, 1F);

    final Button button = createButton(ButtonType.CLOSE, e -> w.close());
    button.setClickShortcut(KeyCode.ESCAPE);
    gl.addComponent(button, 1, 0);
    gl.setComponentAlignment(button, Alignment.MIDDLE_CENTER);

    if (ex != null) {
      Button showLog = new Button("Registro Técnico");
      showLog.addClickListener(evt -> addWindow(createDialogException(ex)));
      setButtonStyle(showLog, ButtonStyle.LINK);
      showLog.setWidth("140px");
      gl.addComponent(showLog, 0, 0);
      gl.setComponentAlignment(showLog, Alignment.BOTTOM_LEFT);
      Label label = new Label();
      label.setWidth("140px");
      gl.addComponent(label, 2, 0);
    }
    vl.addComponent(gl);

    w.setContent(vl);
    button.focus();
    return w;
  }

  /**
   * Este método cria uma janela de dialogo MODAL para exibir uma mensagem para confirmação de excluir dados. O click em qualquer um dos botões fechará o dialog imediatamente, antes de chamar o evento no listener.
   *
   * @param listener Listener para receber qual botão foi clicado.
   *
   * @return Janela no formato de caixa de diálogo pronta para se exibida.
   */
  public static Window createDialogQuestionDelete(RFWDialogButtonClickListener listener) {
    final Window w = new Window("Tem Certeza Que Deseja Excluir?");
    w.setModal(true);
    w.setClosable(false);
    w.setResizable(false);
    w.setWidth("75%");
    w.setHeightUndefined();
    w.center();
    w.setIcon(new ThemeResource("icon/delete_64.png"));
    w.addStyleName("bisErrorDialog");

    // Conteúdo da Janela
    VerticalLayout vl = new VerticalLayout();
    vl.setMargin(true);

    Label lb = new Label("Caso clique em confirmar, o cadastro será excluído do sistema!<br><b>Essa operação não poderá ser desfeita!</b>", ContentMode.HTML);
    vl.addComponent(lb);
    vl.setComponentAlignment(lb, Alignment.MIDDLE_LEFT);
    vl.setExpandRatio(lb, 1f);
    w.setContent(vl);

    HorizontalLayout hlButtons = new HorizontalLayout();
    hlButtons.setMargin(false);
    hlButtons.setSpacing(true);

    Button btCancel = createButton(ButtonType.CANCEL, e -> {
      w.close();
      if (listener != null) listener.clickedButton(DialogButton.CANCEL);
    });
    btCancel.setClickShortcut(KeyCode.ESCAPE);
    btCancel.focus();
    hlButtons.addComponent(btCancel);

    Button btConfirm = createButton(ButtonType.CONFIRM, e -> {
      w.close();
      if (listener != null) listener.clickedButton(DialogButton.CONFIRM);
    });
    hlButtons.addComponent(btConfirm);

    vl.addComponent(hlButtons);
    vl.setComponentAlignment(hlButtons, Alignment.MIDDLE_CENTER);

    return w;
  }

  /**
   * Este método cria uma janela de dialogo MODAL para exibir uma mensagem para confirmação de desativar algum objeto. Desativar é o substituto do "delete" para objetos que não podem ser excluídos devido a associações ou histórico.<br>
   * O click em qualquer um dos botões fechará o dialog imediatamente, antes de chamar o evento no listener.
   *
   * @param listener Listener para receber qual botão foi clicado.
   *
   * @return Janela no formato de caixa de diálogo pronta para se exibida.
   */
  public static Window createDialogQuestionDeactivate(RFWDialogButtonClickListener listener) {
    final Window w = new Window("Tem Certeza Que Deseja Desativar e Excluir?");
    w.setModal(true);
    w.setClosable(false);
    w.setResizable(false);
    w.setWidth("75%");
    w.setHeightUndefined();
    w.center();
    w.setIcon(new ThemeResource("icon/deactivate_64.png"));
    w.addStyleName("bisErrorDialog");

    // Conteúdo da Janela
    VerticalLayout vl = new VerticalLayout();
    vl.setMargin(true);

    Label lb = new Label("Ao confirmar, o BIS desativará o cadastro impedindo seu uso. Depois da desativação, se não houver uso no histórico, o BIS excluirá o cadastro.<br><b>Caso ele seja excluído, essa operação não poderá ser desfeita!</b>", ContentMode.HTML);
    vl.addComponent(lb);
    vl.setComponentAlignment(lb, Alignment.MIDDLE_LEFT);
    vl.setExpandRatio(lb, 1f);
    w.setContent(vl);

    HorizontalLayout hlButtons = new HorizontalLayout();
    hlButtons.setMargin(false);
    hlButtons.setSpacing(true);

    Button btCancel = createButton(ButtonType.CANCEL, e -> {
      w.close();
      if (listener != null) listener.clickedButton(DialogButton.CANCEL);
    });
    btCancel.setClickShortcut(KeyCode.ESCAPE);
    btCancel.focus();
    hlButtons.addComponent(btCancel);

    Button btConfirm = createButton(ButtonType.CONFIRM, e -> {
      w.close();
      if (listener != null) listener.clickedButton(DialogButton.CONFIRM);
    });
    hlButtons.addComponent(btConfirm);

    vl.addComponent(hlButtons);
    vl.setComponentAlignment(hlButtons, Alignment.MIDDLE_CENTER);

    return w;
  }

  /**
   * Este método cria uma janela de dialogo MODAL para exibir uma mensagem de Infomação.
   *
   * @param title Título da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @param iconPath
   * @return Janela no formato de caixa de diálogo pronta para se exibida.
   */
  public static Window createDialogTip(String title, String message, String iconPath) {
    final Window w = new Window(title);
    w.setModal(true);
    w.setClosable(true);
    w.setWidth("80%");
    w.setHeightUndefined();
    w.center();
    w.setIcon(new ThemeResource(iconPath));
    w.addStyleName("bisTipDialog");

    // Conteúdo da Janela
    VerticalLayout vl = new VerticalLayout();
    vl.setMargin(true);

    Label lb = new Label(message, ContentMode.HTML);
    lb.setWidth("100%");
    vl.addComponent(lb);
    vl.setComponentAlignment(lb, Alignment.MIDDLE_LEFT);
    vl.setExpandRatio(lb, 1f);

    final Button button = createButton(ButtonType.CLOSE, e -> w.close());
    button.setClickShortcut(KeyCode.ESCAPE);
    vl.addComponent(button);
    vl.setComponentAlignment(button, Alignment.MIDDLE_CENTER);

    w.setContent(vl);

    button.focus();

    return w;
  }

  /**
   * Este método cria uma janela de dialogo MODAL para exibir uma mensagem de opções para o usuário com um aviso de perigo.<br>
   * O click em qualquer um dos botões fechará o dialog imediatamente, antes de chamar o evento no listener.
   *
   * @param title Título da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @param listener Listener para receber qual botão foi clicado.
   * @param defaultButton Define o botão que terá o foco. Podendo ser pressionado de imediato com o espaço ou ENTER. Se nulo, o primeiro botão receberá o foco. O botão passado aqui deverá estar entre os botões do parâmetro buttons, ou não terá efeito algum.
   * @param cancelButton Define o botão que será acionado com a tecla ESC. Se nulo, nenhum botão recebe esta função. O botão passado aqui deverá estar entre os botões do parâmetro buttons, ou não terá efeito algum.
   * @param buttons Botões a serem exibidos para o usuário. Se passado nulo, será exibido apenas o botão de OK. Os botões serão colocados na ordem do array.
   *
   * @return Janela no formato de caixa de diálogo pronta para se exibida.
   */
  public static Window createDialogQuestionWarning(String title, String message, RFWDialogButtonClickListener listener, DialogButton defaultButton, DialogButton cancelButton, DialogButton... buttons) {
    return FWVad.createDialog(title, message, new ThemeResource("icon/error_64.png"), "bisErrorDialog", listener, defaultButton, cancelButton, buttons);
  }

  /**
   * Cria um dialog modal com o conteúdo do {@link RUWiki#getFWWikiManual_HTML()} pronto para ser exibido como ajuda nos campos que aceitam BISWiki
   */
  public static Window createDialogBISWikiHelp() {
    Window help = new Window();
    help.setWidth("560px");
    help.setHeight("390px");
    help.setCaption("Ajuda BISWiki");
    help.setModal(true);
    help.center();
    help.setClosable(true);
    help.setIcon(new ThemeResource("icon/help_64.png"));

    VerticalLayout helpVL = new VerticalLayout();
    helpVL.setSizeFull();
    helpVL.setMargin(true);
    helpVL.setSpacing(true);
    help.setContent(helpVL);

    Label helpLB = new Label();
    helpLB.setWidth("100%");
    helpLB.setContentMode(ContentMode.HTML);
    helpLB.setValue(RUWiki.getFWWikiManual_HTML());

    helpVL.addComponent(helpLB);
    helpVL.setExpandRatio(helpLB, 1);

    // Cria barra de botões
    Button helpCloseBT = FWVad.createButton(ButtonType.CLOSE, evtClose -> {
      help.close();
    });
    helpVL.addComponent(helpCloseBT);
    helpVL.setComponentAlignment(helpCloseBT, Alignment.MIDDLE_CENTER);

    return help;
  }

  /**
   * Cria o layout utilizado como barra de botões nas telas do BIS e já aceita colocar os components.<Br>
   * NOTE QUE: este componente retorna com setMargins(true);, porem dependendo do layout em que ele for inserido, é possível definir para false para que não fique com espaços duplos ou mesmo desalinhado em relação ao restante da tela.
   *
   * @param components Normalmente botões que são colocados na tela.
   * @return HorizontalLayout no formato de barra de botões pronto para ser utilizado na tela.
   */
  public static HorizontalLayout createButtonBarLayout(Component... components) {
    HorizontalLayout buttonBar = new HorizontalLayout();
    buttonBar.setMargin(true);
    buttonBar.setSpacing(false);
    buttonBar.addStyleName(FWVad.STYLE_COMPONENT_ASSOCIATIVE);
    if (components != null) for (Component component : components) {
      buttonBar.addComponent(component);
    }
    return buttonBar;
  }

  /**
   * Cria uma barra de botões com os botões "CANCEL" e "CONFIRM" padrão do sistema.<br>
   * Mesmo efeito que criar os botões {@link ButtonType#CANCEL} e {@link ButtonType#CONFIRM} e colocados na barra criada com o {@link #createButtonBarLayout(Component...)}.
   *
   * @param cancelListener
   * @param confirmListener
   *
   * @return Barra de botões pronta.
   */
  public static HorizontalLayout createButtonBarLayoutCancelConfirm(ClickListener cancelListener, ClickListener confirmListener) {
    Button btCancel = FWVad.createButton(ButtonType.CANCEL, cancelListener);
    Button btConfirm = FWVad.createButton(ButtonType.CONFIRM, confirmListener);
    return createButtonBarLayout(btCancel, btConfirm);
  }

  /**
   * Método utilizado para criar os botões do sistema
   *
   * @return Botão com o estilo padrão do sistema.
   */
  public static Button createButton(String caption, ButtonStyle buttonStyle, String iconPath, Button.ClickListener listener) {
    final Button b = new Button(caption);
    if (buttonStyle != null) FWVad.setButtonStyle(b, buttonStyle);
    if (iconPath != null) b.setIcon(new ThemeResource(iconPath));
    if (listener != null) b.addClickListener(listener);
    return b;
  }

  /**
   * Cria um botão padrão do BIS do tipo Toggle totalmente configurado.
   *
   * @param buttonType Definição do botão a ser criado.
   * @param listener
   * @return Botão criado já com as carecteristicas do padrão visual.
   */
  public static <T extends Object> RFWButtonToggle<T> createButtonToggle(ButtonType buttonType, ClickListener listener) {
    RFWButtonToggle<T> button = new RFWButtonToggle<T>();
    if (buttonType != null) FWVad.setButtonType(buttonType, button);
    if (listener != null) button.addClickListener(listener);
    return button;
  }

  /**
   * Método utilizado para criar os botões do tipo BISButtonToggle do sistema
   *
   * @return Botão com o estilo padrão do sistema.
   */
  public static <T extends Object> RFWButtonToggle<T> createButtonToggle(String caption, ButtonStyle buttonStyle, String iconPath, Button.ClickListener listener) {
    final RFWButtonToggle<T> b = new RFWButtonToggle<T>(caption);
    if (buttonStyle != null) FWVad.setButtonStyle(b, buttonStyle);
    if (iconPath != null) b.setIcon(new ThemeResource(iconPath));
    if (listener != null) b.addClickListener(listener);
    return b;
  }

  /**
   * Mesmo que o método {@link #createPopupButton(String, ButtonStyle, String, ClickListener, Component)}, mas já cria o listener que faz com que o conteúdo popup seja exibido e ocultado a cada click.
   *
   * @return Botão Popup com o estilo padrão do sistema.
   */
  public static PopupButton createPopupButton(String caption, ButtonStyle buttonStyle, String iconPath, Component content) {
    final PopupButton b = new PopupButton(caption);
    if (buttonStyle != null) FWVad.setButtonStyle(b, buttonStyle);
    if (iconPath != null) b.setIcon(new ThemeResource(iconPath));
    if (content != null) b.setContent(content);
    return b;
  }

  /**
   * Mesmo que o método {@link #createPopupButton(String, ButtonStyle, String, ClickListener, Component)}, mas já cria o listener que faz com que o conteúdo popup seja exibido e ocultado a cada click.
   *
   * @return Botão Popup com o estilo padrão do sistema.
   */
  public static PopupButton createPopupButton(String caption, ButtonStyle buttonStyle, String iconPath, Button... buttons) {
    final PopupButton b = new PopupButton(caption);
    if (buttonStyle != null) FWVad.setButtonStyle(b, buttonStyle);
    if (iconPath != null) b.setIcon(new ThemeResource(iconPath));

    addPopupButtons(b, buttons);
    return b;
  }

  /**
   * Método utilizado para criar os botões popup do sistema
   *
   * @return Botão Popup com o estilo padrão do sistema.
   */
  public static PopupButton createPopupButton(String caption, ButtonStyle buttonStyle, String iconPath, Button.ClickListener listener, Component content) {
    final PopupButton b = new PopupButton(caption);
    if (buttonStyle != null) FWVad.setButtonStyle(b, buttonStyle);
    if (iconPath != null) b.setIcon(new ThemeResource(iconPath));
    if (listener != null) b.addClickListener(listener);
    if (content != null) b.setContent(content);
    return b;
  }

  /**
   * Cria um botão Popup do BIS totalmente configurado com o padrão visual do BIS.
   *
   * @param buttonType Definição do botão a ser criado.
   * @return Botão criado já com as carecteristicas do padrão visual.
   */
  public static PopupButton createPopupButton(ButtonType buttonType, Button.ClickListener listener, Component content) {
    PopupButton button = createPopupButton(null, null, null, listener, content);
    if (buttonType != null) FWVad.setButtonType(buttonType, button);
    return button;
  }

  /**
   * Cria um botão Popup do BIS totalmente configurado com o padrão visual do BIS.
   *
   * @param buttonType Definição do botão a ser criado.
   * @return Botão criado já com as carecteristicas do padrão visual.
   */
  public static PopupButton createPopupButton(ButtonType buttonType) {
    PopupButton button = createPopupButton(buttonType, null, null);
    return button;
  }

  /**
   * Cria um botão Popup do BIS totalmente configurado com o padrão visual do BIS.<br>
   * Criar o padrão de botão com outros botões tipo "link", como um menu de subopções que sai do menu.
   *
   * @param buttonType Definição do botão a ser criado.
   * @param buttons Lista de botões a serem adicionados no layout.
   * @return Botão criado já com as carecteristicas do padrão visual.
   */
  public static PopupButton createPopupButton(ButtonType buttonType, Button... buttons) {
    PopupButton button = new PopupButton();
    FWVad.setButtonType(buttonType, button);

    addPopupButtons(button, buttons);

    return button;
  }

  /**
   * Cria o conteúdo para o PopupButton no formato de lista de opções (criado com VerticalLayout e botões com estilo "Link").
   *
   * @param button PopupButton para receber o conteúdo
   * @param buttons Botões com as opções
   */
  public static void addPopupButtons(PopupButton button, Button... buttons) {
    // Configura os botões
    for (Button bt : buttons) {
      bt.setStyleName(ValoTheme.BUTTON_LINK);
      bt.addClickListener(e -> button.setPopupVisible(false));
    }

    VerticalLayout vl = new VerticalLayout();
    vl.setMargin(false);
    vl.setSpacing(false);
    vl.addComponents(buttons);
    button.setContent(vl);
  }

  /**
   * Retorna a largura da "área da página" no navegador.
   */
  public static int getCanvasWidth() {
    return getUI().getPage().getBrowserWindowWidth();
  }

  /**
   * Retorna a altura da "área da página" no navegador.
   */
  public static int getCanvasHeight() {
    return getUI().getPage().getBrowserWindowHeight();
  }

}
