package br.eng.rodrigogml.rfw.vaadin.interfaces;

import com.vaadin.server.Page.BrowserWindowResizeEvent;

/**
 * Description: Os Frames e janelas que implementarem essa interface receberão a notificação de alteração de tamanho do Browser, permitindo que o layout seja refeito ou reconfigurado.<br>
 *
 * Note que o único componente que "ouve" diretamente do Vaadin é o BISUI, este por sua vez tenta passar o evento para o frame ativo, que deve passar para as janelas ativas, e assim por diante.
 *
 * @author Rodrigo Leitão
 * @since 7.1.0 (5 de nov de 2016)
 */
public interface RFWBrowserResizeListener {

  public void eventBrowserResized(BrowserWindowResizeEvent e);

}
