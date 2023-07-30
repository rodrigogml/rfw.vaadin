package br.eng.rodrigogml.rfw.vaadin.interfaces;

import com.vaadin.server.Page.BrowserWindowResizeEvent;

/**
 * Description: Os Frames e janelas que implementarem essa interface receber�o a notifica��o de altera��o de tamanho do Browser, permitindo que o layout seja refeito ou reconfigurado.<br>
 *
 * Note que o �nico componente que "ouve" diretamente do Vaadin � o BISUI, este por sua vez tenta passar o evento para o frame ativo, que deve passar para as janelas ativas, e assim por diante.
 *
 * @author Rodrigo Leit�o
 * @since 7.1.0 (5 de nov de 2016)
 */
public interface RFWBrowserResizeListener {

  public void eventBrowserResized(BrowserWindowResizeEvent e);

}
