package br.eng.rodrigogml.rfw.vaadin.interfaces;

import com.vaadin.ui.Grid.ItemClick;

import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;

/**
 * Description: Interface de Evento de Duplo click (já tratado) no GRID de dados criado pelo UIFactory.<br>
 *
 * @author Rodrigo Leitão
 * @since 10.0.0 (31 de ago de 2018)
 */
public interface RFWGridDoubleClickListener<BEAN> {

  public void eventDoubleClick(ItemClick<BEAN> event) throws RFWException;

}
