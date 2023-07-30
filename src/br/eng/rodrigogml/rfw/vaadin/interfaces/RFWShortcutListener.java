package br.eng.rodrigogml.rfw.vaadin.interfaces;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.ui.AbstractComponent;

/**
 * Interface do evento de shortkey
 */
public interface RFWShortcutListener {

  /**
   * Método chamado quando a tecla de atalho é pressionada
   *
   * @param component Componente à qual o listener foi anexado.
   * @param sender Sender recebido do Handler do Vaadin (Sem documentação de explicação) - provavelmente o objeto que tinha o foco quando a tecla foi pressionada.
   * @param target Targer recebido do Handler do Vaadin (Sem documentação de explicação)
   * @param keyCode Código da Tecla pressionada (mesma passada no momento do bind do shortcut) - {@link KeyCode}
   * @param keyModifier Array com as teclas "modificadoras" (mesmas passadas no momento do bind do Shortcut) - {@link ModifierKey}
   */
  public void shortCutPressed(AbstractComponent component, Object sender, Object target, int keyCode, int... keyModifier);
}