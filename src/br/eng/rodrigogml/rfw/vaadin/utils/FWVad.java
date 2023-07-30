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
 * Description: Classe est�tica com diversos m�todos para otimizar e facilitar a cria��o e manipula��o da interface do Vaadin no padr�o do BIS.<br>
 *
 * @author Rodrigo GML
 * @since 10.0 (30 de out de 2020)
 */
public class FWVad {

  /**
   * Deixa o cursor do mouse como o "pointer" (como chamado pela documenta��o HTML) que na verdade � a "m�ozinha com o dedo", mesmo cursor de link.
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
   * Define a font com uma linha tra�ada por cima do texto, como se o texto tivesse sido exclu�do.
   */
  public static final String STYLE_FONT_LINETHROUGH = "fontLineThrough";
  /**
   * Define o alinhamento de algum conte�do que suporte ao centro.
   */
  public static final String STYLE_ALIGN_CENTER = "alignmentCenter";

  /**
   * Define o alinhamento de algum conte�do que suporte � direita.
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
   * Estilo para deixar a linha de um grid na cor verde claro. Utilizado por exemplo para indicar valores positivos (cr�dito) nas contas.
   */
  public static final String STYLE_GRID_BACKGROUND_LIGHTGREEN = "gridBackgroundLightGreen";
  /**
   * Estilo para deixar a linha de um grid na cor vermelho claro. Utilizado por exemplo para indicar valores positivos (cr�dito) nas contas.
   */
  public static final String STYLE_GRID_BACKGROUND_LIGHTRED = "gridBackgroundLightRed";
  /**
   * Define um style para a c�lula ou linha de um Grid deixando a font com uma linha cortando o texto, representando uma informa��o que foi cortada/ignorada/exclu�da.
   */
  public static final String STYLE_GRID_FONT_LINETHROUGH = "gridFontLineThrough";
  /**
   * Define o style na coluna para remover as margins internas padr�es. Recomendado o uso quando o conte�do da c�lula � personalizado com um layout pr�prio.
   */
  public static final String STYLE_GRID_CELL_BORDLESS = "bisGridCellNoMargins";
  /**
   * Define o Style a ser aplicado no label da barra de navega��o / Pagina��o para configura-lo.
   */
  public static final String STYLE_NAVIGATOR_LABEL = "navigatorLabel";
  /**
   * Define um Style no Label para deixar o texto HTML melhor formatado para exibi��o na tela
   */
  public static final String STYLE_LABEL_RICHTEXTFORMAT = "bisRichTextFormat";
  /**
   * Deixa o Painel sem borda. Utilizado, por exemplo, como um container invis�vel.
   */
  public static final String STYLE_PANEL_BORDERLESS = ValoTheme.PANEL_BORDERLESS;

  /**
   * Estilo para deixar os componentes Unidos. Pode ser aplicado em:
   * <li>CustomField
   * <li>HorizontalLayout
   */
  public static final String STYLE_COMPONENT_ASSOCIATIVE = "rfwAssociativeComponent";

  /**
   * Diferente do ButtonType que cria alguns bot�es padronizados usados por todo o sistema, o ButtonStyle tem a finalidade de modificar a apar�ncia do bot�o para que ele se encaixe em v�rias partes do sistema.
   */
  public enum ButtonStyle {
    /**
     * Define o tipo de Bot�o com apar�ncia comum, mais utilizado no sistema.
     */
    NONE,
    /**
     * Cria um bot�o com as caracter�sticas necess�rias apra ser incluido na barra de bot�es de atalhos.<br>
     * <B>Aten��o: </b> Embora o tipo do bot�o seja criado, esse bot�es t�m apenas �cones distitintos, assim, diferente dos outros estilos que j� criam o bot�o completo, este deve ter o Tip e o �cone redefeinido!
     */
    SHORTCUT_BUTTON,
    /**
     * Cria um bot�o com a cara de "default", sendo o bot�o que chama a aten��o entre os outros, indicando o caminho que o usu�rio provavelmente deve seguir.
     */
    PRIMARY,
    /**
     * COnfigura o bot�o com uma apar�ncia amigavel, usado por exemplo em bot�es de OK e confirma��es em geral.
     */
    FRIENDLY,
    /**
     * Configura o bot�o com uma apar�ncia de perigo, usado em bot�es de cancelar ou de confirma��o de exclus�o/perda de dados.
     */
    DANGER,
    /**
     * Configura o bot�o para parecer com um link comum. Util por exemplo para mostrar bot�es (ou mesmo links) em uma lista menos polu�da de "gr�ficos UI".<br>
     * Este modelo � usado por exemplo pra exibir os nomes dos relat�rios das p�ginas.
     */
    LINK,
  }

  /**
   * Define alguns tipos de bot�es que s�o usados pelo sistema todo, mantendo assim a mesma caracter�stica e pedr�o visual em todas as telas.
   */
  public enum ButtonType {
    /**
     * Bot�o de desfazer
     */
    UNDO,
    /**
     * Bot�o de Salvar altera��es
     */
    SAVE,
    /**
     * Cria um bot�o utilizado para inticar um livro de endere�os de URLs;Internet.
     */
    BOOKMARK,
    /**
     * Bot�o de navega��o/pagina��o usado para enviar o registro para o primeiro item da lista.
     */
    MOVE_FIRST,
    /**
     * Bot�o de navega��o/pagina��o usado para enviar o registro para o item anterior da lista.
     */
    MOVE_PREVIOUS,
    /**
     * Bot�o de navega��o/pagina��o usado para enviar o registro para o pr�ximo item da lista.
     */
    MOVE_NEXT,
    /**
     * Bot�o de navega��o/pagina��o usado para enviar o registro para o �ltimo item da lista.
     */
    MOVE_LAST,
    /**
     * Bot�o de navega��o/pagina��o usado para enviar o registro para cima.
     */
    MOVE_UP,
    /**
     * Bot�o de navega��o/pagina��o usado para enviar o registro para baixo.
     */
    MOVE_DOWN,
    /**
     * Bot�o utilizado para adicionar Itens a uma lista/tabela.
     */
    ADD_LISTITEM,
    /**
     * Bot�o utilizado apra remover itens de uma lista/tabela.
     */
    REMOVE_LISTITEM,
    /**
     * Cria um bot�o utilizado para abrir/alterar presta��es de parcelamento.
     */
    INSTALLMENTS,
    /**
     * Bot�o utilizado para alterar/editar itens de uma lista/tabela.
     */
    EDIT_LISTITEM,
    /**
     * Bot�o utilizado para executar uma a��o de busca de dados no sistema, como nos formul�rios de filtros dos dados.
     */
    SEARCH,
    /**
     * Bot�o utilizado para procurar um item para refer�ncia, como abrir uma janela de picker e encontrar um objeto. Utilizado quando queremos encontrar um objeto para copia-lo ou edita-lo, quando utilizar o picker para "associar" os objetos, utilizar o bot�o {@link ButtonType#LINK}.
     */
    SEARCH_ITEM,
    /**
     * Bot�o utilizado nos formul�rios para indicar a limpeza dos campos dos formul�rios, deixando todos os campos em branco novamente.
     */
    CLEARFIELDS,
    /**
     * Bot�o de "Fechar". Utilizado para fechar caixas de di�logo ou janelas "popup" de informa��o.
     */
    CLOSE,
    /**
     * Cria o bot�o de "Inserir" Utilizado para iniciar as opera��es de realizar um novo cadastro de objeto.
     */
    INSERTITEM,
    /**
     * Bot�o utiliado para "Enviar alguma informa��o" para servidor remoto (nuvem).
     */
    CLOUNDSEND,
    /**
     * Bot�o utiliado para "Recuperar alguma informa��o" de servidor remoto (nuvem).
     */
    CLOUDGET,
    /**
     * Bot�o utiliado para "Sincronizar alguma informa��o" com um servidor remoto (nuvem).
     */
    CLOUNDSYNC,
    /**
     * Bot�o de "Transmitir Dados", no sentido de enviar dados para outro sistema.
     */
    TRANSMIT,
    /**
     * Cria o bot�o de "Auditar". Utilizado para marcar/desmarcar um item como auditado. De forma geral, um item auditado tem a finalidade de impedir que altera��es sejam feitas por qualquer usu�rio.
     */
    AUDITITEM,
    /**
     * Cria o bot�o de "Ignorar" um item da listagem. Esse comando � utilizado em items que o usu�rio pode solicitar por ignorar (n�o excluir), similar a desabilitar, mas para outros contextos.<br>
     * Utilizado a primeira vez para "ignorar" lan�amentos de extratos importados por arquivos OFX.
     */
    IGNOREITEM,
    /**
     * Cria o bot�o de "Transfer�ncia de Fundos (Valores)" Utilizado para simbolizar transfer�ncia de valores em dinheiro.
     */
    TRANSFERFUNDS,
    /**
     * Cria o bot�o de "Duplicar" Utilizado para iniciar as opera��es de realizar um novo cadastro de objeto baseado em outro que j� existe.
     */
    DUPLICATEITEM,
    /**
     * Cria o bot�o de "Nova Vers�o". Utilizado quando os objetos s�o imut�veis, ou seja, n�o podem ser alterados mas podem ser desabilitados. Esta opera��o pommite que o usu�rio crie um novo objeto baseando nos dados do objeto original. Mas ao confirmar a inclus�o o objeto anterior � automaticamente desativado ou exclu�do.
     */
    VERSIONITEM,
    /**
     * Cria o bot�o de "Alterar" Utilizado para iniciar as opera��es de alterar cadastro de objeto.
     */
    EDITITEM,
    /**
     * Cria o bot�o de "Excluir". Utilizado para excluir algum objeto de cadastro do banco de dados.
     */
    DELETEITEM,
    /**
     * Cria o bot�o de "Visualizar". Utilizado para visualizar/detalhar um item.
     */
    VIEWITEM,
    /**
     * Cria o bot�o de "Ativar". Utilizado para a fun��o contr�ria do {@link #DEACTIVATEITEM}.
     */
    ACTIVATEITEM,
    /**
     * Cria o bot�o de "Desativar". Utilizado para desativar algum objeto de cadastro do banco de dados. Objetos s�o desativados quando n�o podem ser exclu�dos do sistema.
     */
    DEACTIVATEITEM,
    /**
     * Cria o bot�o de "Importar Dados". Utilizado nas fun��es em que o sistema deve importar alguma informa��o externa.
     */
    IMPORTDATA,
    /**
     * Cria o bot�o de "Confirmar". Usado para confirmar as opera��es de confirma��o de opera��es.
     */
    CONFIRM,
    /**
     * Cria o bot�o de "Confirmar Todos". Usado para confirmar diversas opera��es pendentes.
     */
    CONFIRMALL,
    /**
     * Cria o bot�o de "Cancelar", Usado geralmente de forma a representar a a��o contr�ria do bot�o {@link #CONFIRM}
     */
    CANCEL,
    /**
     * Cria o bot�o de "Cancelar Todos", Usado em situa��es que temos v�rias a��es ou situa��o que podem ser canceladas, apresentando uma a��o �nica para cancelar todos ao mesmo tempo.
     */
    CANCELALL,
    /**
     * Cria o bot�o de "Help", Usado em algumas telas para exibir uma ajuda maior e mais detalhada que os "tooltips" dos campos.
     */
    HELP,
    /**
     * Cria o bot�o de "Picker". O bot�o de picker � utilizado quando desejamos "buscar" uma informa��o para associa��o em um campo. Por exemplo no campo CEP, colocamos o bot�o de picker para abrir uma tela e permitir que o usu�rio encontre um CEP, e ao confirmar ele j� traga os dados para o campo.
     */
    PICKER,
    /**
     * Cria o bot�o da "varinha m�gica". Esse bot�o � utilizado quando desejamos que o BIS fa�a alguma tarefa automatizada, como se fosse "m�gica", agilizando os processos e tarefas do usu�rio.
     */
    MAGIC,
    /**
     * Cria o bot�o para associar informa��es, usado quando queremos associar um cadastro (objeto do BIS) � outro.
     */
    LINK,
    /**
     * Cria o bot�o para a��o contr�ria do bot�o {@link ButtonType#LINK}.
     */
    UNLINK,
    /**
     * Cria um bot�o para a a��o de recuperar mensagens no servidor de e-mail.
     */
    FETCHMAIL,
    /**
     * Cria um bot�o para a a��o de recuperar senha.
     */
    PASSWORDRECOVERY,
    /**
     * Cria um bot�o para a a��o de incluir uma pessoa (jur�dica ou f�sica) no sistema de forma r�pida).
     */
    QUICKADD_PERSON,
    /**
     * Bot�o com �cone de repetir valor do campo anterior.
     */
    REPEATVALUE,
    /**
     * Bot�o para realizar a a��o de pagar alguma conta.
     */
    PAY,
    /**
     * Bot�o criato para exibir um tipo de relat�rio. Este bot�o deve ser agrupado dentro do bot�o {@link #REPORTACTIONS}.
     */
    REPORTITEM,
    /**
     * Estilo do bot�o utilizado para o "Mais A��es"
     */
    MOREACTIONS,
    /**
     * Estilo do bot�o utilizado para agrupar os Relat�rios, deixa o bot�o no estilo do "Mais A��es" mas com a cara do bot�o de relat�rios.
     */
    REPORTACTIONS,
    /**
     * Cria um bot�o de "Swap", utilizado para inverter conte�do de alguns campos, pasando o valor de A para B, e de B para A.
     */
    SWAPCONTENT,
    /**
     * Cria o bot�o com �cone de Download, utilizado para baixar qualquer tipo de arquivo do servidor para a m�quina do cliente.
     */
    DOWNLOAD,
    /**
     * Cria o bot�o com �cone de Download de arquivo ZIP, utilizado para baixar qualquer tipo de arquivo do servidor para a m�quina do cliente que seja compactado no formato ZIP.
     */
    DOWNLOAD_ZIP,
    /**
     * Cria um bot�o de envio de E-mail
     */
    SENDMAIL,
    /**
     * Cria o bot�o com �cone de Upload, utilizado para enviar qualquer tipo de arquivo da m�quina do cliente para o servidor.
     */
    UPLOAD,
    /**
     * Cria um bot�o para "atualizar" no sentido de recarregar, ou refazer alguma tarefa.
     */
    REFRESH,
    /**
     * Bot�o utilizado na gera��o dos DashIt. Indica a a��o de pegar os filtros e dados fornecidos pelo usu�rio para gerar o relat�rio.
     */
    DASHIT_CREATE,
    /**
     * Bot�o utilizado para abrir um DashIt. Utilizado nas telas de cadastro que abrem diretamente um relat�rio, ou mesmo entre os relat�rios quando migramos de um para outro.
     */
    DASHIT,
    /**
     * Bot�o utilizado para abrir uma tela de configura��es/defini��es.
     */
    CONFIG,
    /**
     * Cria um bot�o com o �ncone do excel, utilizado normalmente para exportar dados em formato de planilha do excel
     */
    EXCEL,
    /**
     * Cria um bot�o para organizar dados em ordem
     */
    SORT,
    /**
     * Cria um bot�o com uma calculadora, normalmente utilizado associado � campos de valores para que o BIS preencha o valor baseado em algum c�lculo predefinido.
     */
    CALCULATOR,
    /**
     * Cria o bot�o de 'play', utilizado para iniciar/resumir alguma a��o/tarefa.
     */
    PLAY,
    /**
     * Cria o bot�o de 'pause', utilizado para pausar alguma a��o/tarefa.
     */
    PAUSE,
    /**
     * Cria o bot�o de 'play', utilizado para finalizar alguma a��o/tarefa.
     */
    STOP,
    /**
     * Cria o bot�o para executar uma rotina/tarefa de teste etc.
     */
    EXECUTE,
    /**
     * Bot�o de avan�ar "next", bot�o utilizado para navegar entre p�ginas ou etapas, como as de um Wizard. N�o confundir com os bot�es de barra de navega��o de itens {@link #MOVE_NEXT}.
     */
    NEXT,
    /**
     * Bot�o de recuar "previous", bot�o utilizado para navegar entre p�ginas ou etapas, como as de um Wizard. N�o confundir com os bot�es de barra de navega��o de itens {@link #MOVE_PREVIOUS}.
     */
    PREVIOUS,
    /**
     * Bot�o utilizado apra representar a a��o de executar rotinas de manuten��o do sistema.
     */
    MAINTENANCE,
  }

  /**
   * Construtor privado para classe est�tica.
   */
  private FWVad() {
  }

  /**
   * Este m�todo cria uma janela de dialogo MODAL para exibir uma mensagem de op��es para o usu�rio, como uma tela de confirmar uma a��o com bot�es configur�veis.<br>
   * O click em qualquer um dos bot�es fechar� o dialog imediatamente, antes de chamar o evento no listener.
   *
   * @param title T�tulo da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @param listener Listener para receber qual bot�o foi clicado.
   * @param defaultButton Define o bot�o que ter� o foco. Podendo ser pressionado de imediato com o espa�o ou ENTER. Se nulo, o primeiro bot�o receber� o foco. O bot�o passado aqui dever� estar entre os bot�es do par�metro buttons, ou n�o ter� efeito algum.
   * @param cancelButton Define o bot�o que ser� acionado com a tecla ESC. Se nulo, nenhum bot�o recebe esta fun��o. O bot�o passado aqui dever� estar entre os bot�es do par�metro buttons, ou n�o ter� efeito algum.
   * @param buttons Bot�es a serem exibidos para o usu�rio. Se passado nulo, ser� exibido apenas o bot�o de OK. Os bot�es ser�o colocados na ordem do array.
   *
   * @return Janela no formato de caixa de di�logo pronta para se exibida.
   */
  public static Window createDialogQuestion(String title, String message, RFWDialogButtonClickListener listener, DialogButton defaultButton, DialogButton cancelButton, DialogButton... buttons) {
    return createDialog(title, message, new ThemeResource("icon/dialog_64.png"), null, listener, defaultButton, cancelButton, buttons);
  }

  /**
   * Este m�todo cria uma janela de dialogo MODAL para exibir uma mensagem de op��es para o usu�rio, como uma tela de confirmar uma a��o com bot�es configur�veis.<br>
   * O click em qualquer um dos bot�es fechar� o dialog imediatamente, antes de chamar o evento no listener.
   *
   * @param title T�tulo da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @param icon Define o �cone a ser exibido no dialogo.
   * @param listener Listener para receber qual bot�o foi clicado.
   * @param defaultButton Define o bot�o que ter� o foco. Podendo ser pressionado de imediato com o espa�o ou ENTER. Se nulo, o primeiro bot�o receber� o foco. O bot�o passado aqui dever� estar entre os bot�es do par�metro buttons, ou n�o ter� efeito algum.
   * @param cancelButton Define o bot�o que ser� acionado com a tecla ESC. Se nulo, nenhum bot�o recebe esta fun��o. O bot�o passado aqui dever� estar entre os bot�es do par�metro buttons, ou n�o ter� efeito algum.
   * @param buttons Bot�es a serem exibidos para o usu�rio. Se passado nulo, ser� exibido apenas o bot�o de OK. Os bot�es ser�o colocados na ordem do array.
   *
   * @return Janela no formato de caixa de di�logo pronta para se exibida.
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

    // Conte�do da Janela
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
          button.setCaption("N�o");
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
   * Este m�todo cria uma janela de dialogo MODAL para exibir uma mensagem de Infoma��o.
   *
   * @param title T�tulo da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @param iconPath
   * @return Janela no formato de caixa de di�logo pronta para se exibida.
   */
  public static Window createDialogInformation(String title, String message, String iconPath) {
    final Window w = new Window(title);
    w.setModal(true);
    w.setClosable(true);
    w.setWidth("80%");
    w.setHeightUndefined();
    w.center();
    w.setIcon(new ThemeResource(iconPath));

    // Conte�do da Janela
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
   * Exibe uma mensagem de Erro com o t�tulo padr�o 'Erro no Sistema'.
   *
   * @param message Mensagem a ser exibida ao usu�rio.
   */
  public static void showWarningMessage(String message) {
    Window dialog = createDialogWarning("Erro no Sistema :�(", message);
    addWindow(dialog);
  }

  /**
   * Exibe uma mensagem de Erro com o t�tulo padr�o 'Erro no Sistema' e anexa a instru��o de reiniciar a sess�o se o problema persistir ao fim da mensagem.
   *
   * @param message Mensagem a ser exibida ao usu�rio.
   * @param ex
   */
  public static void showErrorMessage(String message, Throwable ex) {
    showErrorMessage("Erro no Sistema :�(", message + "<br><br><i>� recomend�vel reiniciar a sess�o. Se o problema persistir, contate o suporte.</i>", ex);
  }

  /**
   * Exibe uma mensagem de erro para o usu�rio.
   *
   * @param title T�tulo da Caixa de mensagem.
   * @param message Mensagem a ser exibida.
   * @param ex
   */
  public static void showErrorMessage(String title, String message, Throwable ex) {
    Window dialog = createDialogError(title, message, ex);
    addWindow(dialog);
  }

  /**
   * Este m�todo cria uma janela de dialogo MODAL para exibir uma mensagem de erro.
   *
   * @param title T�tulo da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @return Janela no formato de caixa de di�logo pronta para se exibida.
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

    // Conte�do da Janela
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
      Button showLog = new Button("Registro T�cnico");
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
   * Este m�todo cria uma janela de dialogo MODAL para exibir uma mensagem de Warnings.
   *
   * @param title T�tulo da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @return Janela no formato de caixa de di�logo pronta para se exibida.
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

    // Conte�do da Janela
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
    final Window w = new Window("Detalhes T�cnicos do Erro");
    w.setModal(true);
    w.addStyleName("bisErrorDialog");
    w.setClosable(true);
    w.setWidth("90%");
    w.setHeight("90%");
    w.center();
    w.setIcon(new ThemeResource("icon/bug_64.png"));

    // Conte�do da Janela
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
   * Cria um bot�o padr�o totalmente configurado.
   *
   * @param buttonType Defini��o do bot�o a ser criado.
   * @param listener
   * @return Bot�o criado j� com as carecteristicas do padr�o visual.
   */
  public static Button createButton(ButtonType buttonType, ClickListener listener) {
    Button button = new Button();
    if (buttonType != null) setButtonType(buttonType, button);
    if (listener != null) button.addClickListener(listener);
    return button;
  }

  /**
   * Faz todas as defini��es no Objeto "button" conforme a defini��o de UI padr�o do BIS.
   *
   * @param buttonType Defini��o da UI padr�o a ser aplicada no Bot�o.
   * @param button Objeto "bot�o" para ser formatado conforme padr�o da UI do BIS.
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
        button.setDescription("Salvar as Altera��es");
        button.setIcon(new ThemeResource("icon/save_24.png"));
        break;
      case BOOKMARK:
        button.setCaption(null);
        button.setDescription("Endere�os de Sites");
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
        button.setCaption("Nova Vers�o");
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
        button.setCaption("Presta��o/Parcelas");
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
        button.setCaption("Mais A��es");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/moreactions_24.png"));
        break;
      case REPORTACTIONS:
        button.setCaption("Relat�rios");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/report_24.png"));
        break;
      case REPORTITEM:
        button.setCaption("Relat�rio");
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
        button.setCaption("Gerar Relat�rio");
        setButtonStyle(button, ButtonStyle.FRIENDLY);
        break;
      case DASHIT:
        button.setIcon(new ThemeResource("icon/dashit_24.png"));
        button.setCaption("Abrir Relat�rio");
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
        button.setCaption("Manuten��o");
        button.setDescription("Executar a rotina de manuten��o dos dados.");
        setButtonStyle(button, ButtonStyle.NONE);
        button.setIcon(new ThemeResource("icon/maintenance_24.png"));
        break;
    }
  }

  /**
   * Define o estilo em algum bot�o.<br>
   *
   * @param buttonStyle Estilo a ser definido no bot�o.
   */
  public static void setButtonStyle(Button button, ButtonStyle buttonStyle) {
    // Aten��o: Se n�o for passado o estilo None, n�o removemos estilos antigos, permitindo que os estilos se somem se necess�rio.
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
   * Exibe uma mensagem de notifica��o que some sozinha depois de um tempo. <br>
   * <B>ATEN��O:</B> Nenhuma notifica��o faz busca da mensagem por bundle, quando necess�rio fazer a busca antes da chamada deste m�todo.
   *
   * @param caption T�tulo da Notifica��o
   * @param text texto/conte�do de exibi��o da notifica��o
   * @param msec Tempo em milisegundos para exibi��o da mensagem. Se passado -1 exige que se clique na mensagem.
   * @param icon �cone para ser exibido na notifica��o, se nulo deixa sem �cone.
   * @param type Tipo da notifica��o conforme defini��o do Vaadin. Veja em {@link Type}.
   * @param position Posi��o de apar�ncia da notifica��o. Veja detalhes em {@link Position}.
   * @param style Estilo do CSS a ser colocado na Notifica��o. Permite um controle maior sobre a apar�ncia da notifica��o.
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
   * Exibe uma notifica��o com aspecto de sucesso.<br>
   * Utilizada para opera��es bem sucessidas.<br>
   * Assim informamos que tudo foi realizado com sucesso, mas n�o exigimos do usu�rio um clique para confirmar a informa��o. Deixando assim o uso mais r�pido e simples.
   *
   * @param msg Mensagem de confirma��o da opera��o a ser exibida.
   */
  public static void showNotificationSuccess(String msg) {
    showNotification("", msg, 1000, new ThemeResource("icon/success_64.png"), Type.HUMANIZED_MESSAGE, Position.TOP_CENTER, "bisNotificationSuccess");
  }

  /**
   * Exibe uma notifica��o com aspecto de sucesso.<br>
   * Utilizada para opera��es bem sucessidas.<br>
   * Assim informamos que tudo foi realizado com sucesso, mas n�o exigimos do usu�rio um clique para confirmar a informa��o. Deixando assim o uso mais r�pido e simples.
   *
   * @param msg Mensagem de confirma��o da opera��o a ser exibida.
   * @param subMsg Segunda linha da mensagem com uma fonte menor, permite exibir uma informa��o adicional
   */
  public static void showNotificationSuccess(String msg, String subMsg) {
    showNotification("", msg + "<br><small>" + subMsg + "</small>", 1000, new ThemeResource("icon/success_64.png"), Type.HUMANIZED_MESSAGE, Position.TOP_CENTER, "bisNotificationSuccess");
  }

  /**
   * Cria uma notifica��o de Bandeja de Sistema. Normalmente utilizada para emitir alertas do sistema de tarefas e eventos desrelacionados com a opera��o atual do usu�rio. Como uma mensagem que chegou, um evento que ocorreu em segundo plano, etc.
   *
   * @param title T�tulo da notifica��o
   * @param msg Mensagem da Notifica��o
   * @param icon �cona da notifica��o. Pode ser nulo para n�o exibir nenhum, mas � recomendado utilizar para identifica��o mais r�pida do usu�rio de que se trata a notifica��o.
   */
  public static void showNotificationTray(String title, String msg, Resource icon) {
    showNotification(title, msg, 1000, icon, Type.TRAY_NOTIFICATION, Position.TOP_RIGHT, "bisTrayNotification");
  }

  /**
   * Recupera a inst�ncia da UI de acordo com a Thread que chama o m�todo.
   *
   * @return Inst�ncia do UI do Vaadin em execu��o no momento.
   */
  public static UI getUI() {
    return CurrentInstance.get(UI.class);
  }

  /**
   * Recupera a inst�ncia da UI de acordo com a Thread que chama o m�todo, desde que ela implemente a {@link RFWUI}.<br>
   * Caso ela n�o implemente ou a UI n�o seja encontrada � retornado nulo, sem lan�amento de exceptions.
   *
   * @return Inst�ncia do {@link RFWUI} do Vaadin em execu��o no momento, caso a UI implemente a interface.
   */
  public static RFWUI getRFWUI() {
    UI ui = getUI();
    if (ui != null && (ui instanceof RFWUI)) return (RFWUI) ui;
    return null;
  }

  /**
   * Coloca uma nova janela na interface do usu�rio.
   *
   * @param window Janela a ser Exibida para o usu�rio
   */
  public static void addWindow(Window window) {
    getUI().addWindow(window);
  }

  /**
   * Define uma mensagem de erro em um campo da UI. Em geral utilizado para anexar mensagens de falha de valida��o do seu conte�do.
   *
   * @param field Campo que implemente {@link HasValue} do Vaadin. Se o campo n�o suportar a notifica��o, nada � feito.
   * @param msg Mensagem a ser exibida.
   * @return true caso a mensagem tenha sido associada ao Field, false caso contr�rio.
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
   * Mensagens de valida��es s�o as mensagens usadas para informar o usu�rio que algum dado fornecido est� com algum tipo de problema (errado, inconsistente, faltando, etc.) e que por isso a opera��o n�o prosseguir�. Usado tamb�m quando a informa��o n�o � fornecida pelo usu�rio, mas recuperada pelo sistema, como por exemplo, quando desejamos alterar um item selecionado na tela e durante sua busca
   * descobrimos que o item n�o est� mais no banco de dados.<br>
   * <B>ATEN��O:</B> Nenhuma notifica��o faz busca da mensagem por bundle, quando necess�rio fazer a busca antes da chamada deste m�todo.
   */
  public static void showValidationMessage(String msg) {
    showInformationMessage("Problema de Valida��o", msg, "icon/validation_64.png");
  }

  /**
   * Exibe uma mensagem de informa��o para o usu�rio.
   *
   * @param title T�tulo da Caixa de mensagem.
   * @param message Mensagem a ser exibida.
   * @param iconPath Caminho do �cone a ser exibido na mensagem. Padr�o em 64x64px.
   */
  public static void showInformationMessage(String title, String message, String iconPath) {
    Window dialog = createDialogInformation(title, message, iconPath);
    addWindow(dialog);
  }

  /**
   * Cria um Panel invis�vel que cria scrolls caso o conte�do definido dentro dele seja maior do que o pr�prio Panel.<br>
   * <B>ATEN��O:</B> Os scrolls s� s�o gerados caso o conte�do do panel n�o tenha o tamanho definido na dire��o que se deseja o Scroll.
   */
  public static Panel createScrollPanel() {
    Panel panel = new Panel();
    panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
    panel.setSizeFull();
    return panel;
  }

  /**
   * Cria um Panel invis�vel que cria scrolls caso o conte�do definido dentro dele seja maior do que o pr�prio Panel.<br>
   * <B>ATEN��O:</B> Os scrolls s� s�o gerados caso o conte�do do panel n�o tenha o tamanho definido na dire��o que se deseja o Scroll.
   *
   * @param content Conte�do a ser colocado dentro do Panel. NOTE QUE J� DEVE VIR COM A DEFINI��O DE ALTURA "INDEFINIDA"!!!
   */
  public static Panel createScrollPanel(Component content) {
    Panel panel = createScrollPanel();
    panel.setContent(content);
    return panel;
  }

  /**
   * Exibe uma mensagem de erro a partir de uma exce��o qualquer.
   *
   * @param e Exception a ser transformada em Mensagem.
   */
  public static void showUncatchExceptionMessage(Throwable e) {
    String message = "<br>Foi detectado um BUG no BIS. Rein�cie sua sess�o, se o problema persistir, contate o suporte.<br><br><b>Erro: </b><i>";
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
   * Este m�todo cria uma janela de dialogo MODAL para exibir uma mensagem de BUG.
   *
   * @param title T�tulo da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @return Janela no formato de caixa de di�logo pronta para se exibida.
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

    // Conte�do da Janela
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
      Button showLog = new Button("Registro T�cnico");
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
   * Este m�todo cria uma janela de dialogo MODAL para exibir uma mensagem para confirma��o de excluir dados. O click em qualquer um dos bot�es fechar� o dialog imediatamente, antes de chamar o evento no listener.
   *
   * @param listener Listener para receber qual bot�o foi clicado.
   *
   * @return Janela no formato de caixa de di�logo pronta para se exibida.
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

    // Conte�do da Janela
    VerticalLayout vl = new VerticalLayout();
    vl.setMargin(true);

    Label lb = new Label("Caso clique em confirmar, o cadastro ser� exclu�do do sistema!<br><b>Essa opera��o n�o poder� ser desfeita!</b>", ContentMode.HTML);
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
   * Este m�todo cria uma janela de dialogo MODAL para exibir uma mensagem para confirma��o de desativar algum objeto. Desativar � o substituto do "delete" para objetos que n�o podem ser exclu�dos devido a associa��es ou hist�rico.<br>
   * O click em qualquer um dos bot�es fechar� o dialog imediatamente, antes de chamar o evento no listener.
   *
   * @param listener Listener para receber qual bot�o foi clicado.
   *
   * @return Janela no formato de caixa de di�logo pronta para se exibida.
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

    // Conte�do da Janela
    VerticalLayout vl = new VerticalLayout();
    vl.setMargin(true);

    Label lb = new Label("Ao confirmar, o BIS desativar� o cadastro impedindo seu uso. Depois da desativa��o, se n�o houver uso no hist�rico, o BIS excluir� o cadastro.<br><b>Caso ele seja exclu�do, essa opera��o n�o poder� ser desfeita!</b>", ContentMode.HTML);
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
   * Este m�todo cria uma janela de dialogo MODAL para exibir uma mensagem de Infoma��o.
   *
   * @param title T�tulo da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @param iconPath
   * @return Janela no formato de caixa de di�logo pronta para se exibida.
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

    // Conte�do da Janela
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
   * Este m�todo cria uma janela de dialogo MODAL para exibir uma mensagem de op��es para o usu�rio com um aviso de perigo.<br>
   * O click em qualquer um dos bot�es fechar� o dialog imediatamente, antes de chamar o evento no listener.
   *
   * @param title T�tulo da Janela de Dialogo.
   * @param message Mensagem a ser exibida.
   * @param listener Listener para receber qual bot�o foi clicado.
   * @param defaultButton Define o bot�o que ter� o foco. Podendo ser pressionado de imediato com o espa�o ou ENTER. Se nulo, o primeiro bot�o receber� o foco. O bot�o passado aqui dever� estar entre os bot�es do par�metro buttons, ou n�o ter� efeito algum.
   * @param cancelButton Define o bot�o que ser� acionado com a tecla ESC. Se nulo, nenhum bot�o recebe esta fun��o. O bot�o passado aqui dever� estar entre os bot�es do par�metro buttons, ou n�o ter� efeito algum.
   * @param buttons Bot�es a serem exibidos para o usu�rio. Se passado nulo, ser� exibido apenas o bot�o de OK. Os bot�es ser�o colocados na ordem do array.
   *
   * @return Janela no formato de caixa de di�logo pronta para se exibida.
   */
  public static Window createDialogQuestionWarning(String title, String message, RFWDialogButtonClickListener listener, DialogButton defaultButton, DialogButton cancelButton, DialogButton... buttons) {
    return FWVad.createDialog(title, message, new ThemeResource("icon/error_64.png"), "bisErrorDialog", listener, defaultButton, cancelButton, buttons);
  }

  /**
   * Cria um dialog modal com o conte�do do {@link RUWiki#getFWWikiManual_HTML()} pronto para ser exibido como ajuda nos campos que aceitam BISWiki
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

    // Cria barra de bot�es
    Button helpCloseBT = FWVad.createButton(ButtonType.CLOSE, evtClose -> {
      help.close();
    });
    helpVL.addComponent(helpCloseBT);
    helpVL.setComponentAlignment(helpCloseBT, Alignment.MIDDLE_CENTER);

    return help;
  }

  /**
   * Cria o layout utilizado como barra de bot�es nas telas do BIS e j� aceita colocar os components.<Br>
   * NOTE QUE: este componente retorna com setMargins(true);, porem dependendo do layout em que ele for inserido, � poss�vel definir para false para que n�o fique com espa�os duplos ou mesmo desalinhado em rela��o ao restante da tela.
   *
   * @param components Normalmente bot�es que s�o colocados na tela.
   * @return HorizontalLayout no formato de barra de bot�es pronto para ser utilizado na tela.
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
   * Cria uma barra de bot�es com os bot�es "CANCEL" e "CONFIRM" padr�o do sistema.<br>
   * Mesmo efeito que criar os bot�es {@link ButtonType#CANCEL} e {@link ButtonType#CONFIRM} e colocados na barra criada com o {@link #createButtonBarLayout(Component...)}.
   *
   * @param cancelListener
   * @param confirmListener
   *
   * @return Barra de bot�es pronta.
   */
  public static HorizontalLayout createButtonBarLayoutCancelConfirm(ClickListener cancelListener, ClickListener confirmListener) {
    Button btCancel = FWVad.createButton(ButtonType.CANCEL, cancelListener);
    Button btConfirm = FWVad.createButton(ButtonType.CONFIRM, confirmListener);
    return createButtonBarLayout(btCancel, btConfirm);
  }

  /**
   * M�todo utilizado para criar os bot�es do sistema
   *
   * @return Bot�o com o estilo padr�o do sistema.
   */
  public static Button createButton(String caption, ButtonStyle buttonStyle, String iconPath, Button.ClickListener listener) {
    final Button b = new Button(caption);
    if (buttonStyle != null) FWVad.setButtonStyle(b, buttonStyle);
    if (iconPath != null) b.setIcon(new ThemeResource(iconPath));
    if (listener != null) b.addClickListener(listener);
    return b;
  }

  /**
   * Cria um bot�o padr�o do BIS do tipo Toggle totalmente configurado.
   *
   * @param buttonType Defini��o do bot�o a ser criado.
   * @param listener
   * @return Bot�o criado j� com as carecteristicas do padr�o visual.
   */
  public static <T extends Object> RFWButtonToggle<T> createButtonToggle(ButtonType buttonType, ClickListener listener) {
    RFWButtonToggle<T> button = new RFWButtonToggle<T>();
    if (buttonType != null) FWVad.setButtonType(buttonType, button);
    if (listener != null) button.addClickListener(listener);
    return button;
  }

  /**
   * M�todo utilizado para criar os bot�es do tipo BISButtonToggle do sistema
   *
   * @return Bot�o com o estilo padr�o do sistema.
   */
  public static <T extends Object> RFWButtonToggle<T> createButtonToggle(String caption, ButtonStyle buttonStyle, String iconPath, Button.ClickListener listener) {
    final RFWButtonToggle<T> b = new RFWButtonToggle<T>(caption);
    if (buttonStyle != null) FWVad.setButtonStyle(b, buttonStyle);
    if (iconPath != null) b.setIcon(new ThemeResource(iconPath));
    if (listener != null) b.addClickListener(listener);
    return b;
  }

  /**
   * Mesmo que o m�todo {@link #createPopupButton(String, ButtonStyle, String, ClickListener, Component)}, mas j� cria o listener que faz com que o conte�do popup seja exibido e ocultado a cada click.
   *
   * @return Bot�o Popup com o estilo padr�o do sistema.
   */
  public static PopupButton createPopupButton(String caption, ButtonStyle buttonStyle, String iconPath, Component content) {
    final PopupButton b = new PopupButton(caption);
    if (buttonStyle != null) FWVad.setButtonStyle(b, buttonStyle);
    if (iconPath != null) b.setIcon(new ThemeResource(iconPath));
    if (content != null) b.setContent(content);
    return b;
  }

  /**
   * Mesmo que o m�todo {@link #createPopupButton(String, ButtonStyle, String, ClickListener, Component)}, mas j� cria o listener que faz com que o conte�do popup seja exibido e ocultado a cada click.
   *
   * @return Bot�o Popup com o estilo padr�o do sistema.
   */
  public static PopupButton createPopupButton(String caption, ButtonStyle buttonStyle, String iconPath, Button... buttons) {
    final PopupButton b = new PopupButton(caption);
    if (buttonStyle != null) FWVad.setButtonStyle(b, buttonStyle);
    if (iconPath != null) b.setIcon(new ThemeResource(iconPath));

    addPopupButtons(b, buttons);
    return b;
  }

  /**
   * M�todo utilizado para criar os bot�es popup do sistema
   *
   * @return Bot�o Popup com o estilo padr�o do sistema.
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
   * Cria um bot�o Popup do BIS totalmente configurado com o padr�o visual do BIS.
   *
   * @param buttonType Defini��o do bot�o a ser criado.
   * @return Bot�o criado j� com as carecteristicas do padr�o visual.
   */
  public static PopupButton createPopupButton(ButtonType buttonType, Button.ClickListener listener, Component content) {
    PopupButton button = createPopupButton(null, null, null, listener, content);
    if (buttonType != null) FWVad.setButtonType(buttonType, button);
    return button;
  }

  /**
   * Cria um bot�o Popup do BIS totalmente configurado com o padr�o visual do BIS.
   *
   * @param buttonType Defini��o do bot�o a ser criado.
   * @return Bot�o criado j� com as carecteristicas do padr�o visual.
   */
  public static PopupButton createPopupButton(ButtonType buttonType) {
    PopupButton button = createPopupButton(buttonType, null, null);
    return button;
  }

  /**
   * Cria um bot�o Popup do BIS totalmente configurado com o padr�o visual do BIS.<br>
   * Criar o padr�o de bot�o com outros bot�es tipo "link", como um menu de subop��es que sai do menu.
   *
   * @param buttonType Defini��o do bot�o a ser criado.
   * @param buttons Lista de bot�es a serem adicionados no layout.
   * @return Bot�o criado j� com as carecteristicas do padr�o visual.
   */
  public static PopupButton createPopupButton(ButtonType buttonType, Button... buttons) {
    PopupButton button = new PopupButton();
    FWVad.setButtonType(buttonType, button);

    addPopupButtons(button, buttons);

    return button;
  }

  /**
   * Cria o conte�do para o PopupButton no formato de lista de op��es (criado com VerticalLayout e bot�es com estilo "Link").
   *
   * @param button PopupButton para receber o conte�do
   * @param buttons Bot�es com as op��es
   */
  public static void addPopupButtons(PopupButton button, Button... buttons) {
    // Configura os bot�es
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
   * Retorna a largura da "�rea da p�gina" no navegador.
   */
  public static int getCanvasWidth() {
    return getUI().getPage().getBrowserWindowWidth();
  }

  /**
   * Retorna a altura da "�rea da p�gina" no navegador.
   */
  public static int getCanvasHeight() {
    return getUI().getPage().getBrowserWindowHeight();
  }

}
