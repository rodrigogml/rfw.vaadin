package br.eng.rodrigogml.rfw.vaadin.components.stylegenerators;

import java.util.Collection;
import java.util.LinkedList;

import com.vaadin.ui.StyleGenerator;

import br.eng.rodrigogml.rfw.kernel.vo.GVO;
import br.eng.rodrigogml.rfw.kernel.vo.RFWVO;

/**
 * Description: Style Generator utilizado no GRID para ressaltar algumas linhas. A linha é ressaltada sempre que o ID do objeto (esperado um RFWVO) estiver dentro da coleção de IDs definidos para HighLight.<br>
 *
 * @author Rodrigo Leitão
 * @since 10.0.0 (22 de nov de 2018)
 */
public class UIGridRowHighlight<VO extends RFWVO> implements StyleGenerator<GVO<VO>> {

  private static final long serialVersionUID = 1480953217975914466L;

  private final LinkedList<Long> highLightIDs = new LinkedList<>();

  /**
   * Cria um novo StyleGenerator que identificará no Grid linhas cujo ID do objeto correponda aos IDs definidos.<br>
   * Note que este construtor cria o Style Generator mas a principio não destacará nenhum objeto.
   */
  public UIGridRowHighlight() {
  }

  /**
   * Cria um novo StyleGenerator que identificará no Grid linhas cujo ID do objeto correponda aos IDs definidos.
   *
   * @param highLightIDs IDs dos objetos que devem ter a linha destacada no Grid.
   */
  public UIGridRowHighlight(Collection<Long> highLightIDs) {
    add(highLightIDs);
  }

  /**
   * Cria um novo StyleGenerator que identificará no Grid linhas cujo ID do objeto correponda aos IDs definidos.
   *
   * @param highLightIDs IDs dos objetos que devem ter a linha destacada no Grid.
   */
  public UIGridRowHighlight(Long[] highLightIDs) {
    add(highLightIDs);
  }

  /**
   * Limpa todos os objetos destacados.
   */
  public void clear() {
    this.highLightIDs.clear();
  }

  /**
   * Inclui novos IDs para serem destacados no Grid.
   *
   * @param highLightIDs Coleção de IDs dos objetos a serem destacados
   */
  public void add(Collection<Long> highLightIDs) {
    this.highLightIDs.addAll(highLightIDs);
  }

  /**
   * Inclui novos IDs para serem destacados no Grid.
   *
   * @param highLightIDs Coleção de IDs dos objetos a serem destacados
   */
  public void add(Long[] highLightIDs) {
    for (Long id : highLightIDs)
      this.highLightIDs.add(id);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public String apply(GVO bean) {
    if (bean != null && this.highLightIDs.contains(bean.getVO().getId())) {
      return "RFWGridHighLightRow";
    }
    return null;
  }
}
