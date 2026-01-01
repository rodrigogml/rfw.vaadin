package br.eng.rodrigogml.rfw.vaadin.providers;

import com.vaadin.server.Resource;

import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;

/**
 * Description: Define uma interface para classes proverem ícones para as enumerations (Utilizado na UIFactory para recuperar os ícones para os componentes baseado em Enumerations).<br>
 *
 * <b>ATENÇÃO:</B> Recomendamos que a implementação tenha os métodos <b>estáticos como:</b> getIconPath24() e getIconPath64(), e o método {@link #getIconPath(int, Object)} redirecione para eles. Assim quando necessário pode-se utiliza diretamente os métodos estáticos evitando a instanciação da classe.
 *
 * @author Rodrigo Leitão
 * @since 7.1.0 (9 de mai de 2019)
 */
public interface IconProvider<FIELDTYPE> {

  /**
   * Retorna um resource com o ícone do tamanho solicitado para a enumeration.
   *
   * @param size Tamanho do ícone desejado. Fique atento aos tamahos suportados pela classe. O Padrão do RFW é 24 e 64.
   * @param value Valor a de enumeração para recuperação do ícone.
   * @return Resource contendo o caminho para o ícone dentro do tema.
   * @throws RFWException Lançado caso o tamanho solicitado não seja suportado.
   */
  public Resource getIcon(int size, FIELDTYPE value) throws RFWException;

  /**
   * Retorna o caminho para o ícone do tamanho solicitado para a enumeration.
   *
   * @param size Tamanho do ícone desejado. Fique atento aos tamahos suportados pela classe. O Padrão do RFW é 24 e 64.
   * @param value Valor a de enumeração para recuperação do ícone.
   * @return String contendo o caminho para o ícone dentro do tema.
   * @throws RFWException Lançado caso o tamanho solicitado não seja suportado.
   */
  public String getIconPath(int size, FIELDTYPE value) throws RFWException;

}
