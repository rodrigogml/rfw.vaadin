package br.eng.rodrigogml.rfw.vaadin.providers;

import com.vaadin.server.Resource;

import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;

/**
 * Description: Define uma interface para classes proverem �cones para as enumerations (Utilizado na UIFactory para recuperar os �cones para os componentes baseado em Enumerations).<br>
 *
 * <b>ATEN��O:</B> Recomendamos que a implementa��o tenha os m�todos <b>est�ticos como:</b> getIconPath24() e getIconPath64(), e o m�todo {@link #getIconPath(int, Object)} redirecione para eles. Assim quando necess�rio pode-se utiliza diretamente os m�todos est�ticos evitando a instancia��o da classe.
 *
 * @author Rodrigo Leit�o
 * @since 7.1.0 (9 de mai de 2019)
 */
public interface IconProvider<FIELDTYPE> {

  /**
   * Retorna um resource com o �cone do tamanho solicitado para a enumeration.
   *
   * @param size Tamanho do �cone desejado. Fique atento aos tamahos suportados pela classe. O Padr�o do RFW � 24 e 64.
   * @param value Valor a de enumera��o para recupera��o do �cone.
   * @return Resource contendo o caminho para o �cone dentro do tema.
   * @throws RFWException Lan�ado caso o tamanho solicitado n�o seja suportado.
   */
  public Resource getIcon(int size, FIELDTYPE value) throws RFWException;

  /**
   * Retorna o caminho para o �cone do tamanho solicitado para a enumeration.
   *
   * @param size Tamanho do �cone desejado. Fique atento aos tamahos suportados pela classe. O Padr�o do RFW � 24 e 64.
   * @param value Valor a de enumera��o para recupera��o do �cone.
   * @return String contendo o caminho para o �cone dentro do tema.
   * @throws RFWException Lan�ado caso o tamanho solicitado n�o seja suportado.
   */
  public String getIconPath(int size, FIELDTYPE value) throws RFWException;

}
