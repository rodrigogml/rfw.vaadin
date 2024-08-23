package br.eng.rodrigogml.rfw.vaadin.providers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import br.eng.rodrigogml.rfw.kernel.exceptions.RFWCriticalException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWRunTimeException;
import br.eng.rodrigogml.rfw.kernel.interfaces.RFWDBProvider;
import br.eng.rodrigogml.rfw.kernel.preprocess.PreProcess;
import br.eng.rodrigogml.rfw.kernel.vo.RFWMO;
import br.eng.rodrigogml.rfw.kernel.vo.RFWMO.AppendMethod;
import br.eng.rodrigogml.rfw.kernel.vo.RFWOrderBy;
import br.eng.rodrigogml.rfw.kernel.vo.RFWVO;
import br.eng.rodrigogml.rfw.vaadin.utils.TreatException;

/**
 * Description: Classe utilizada para prover os dados apra os componentes da UI através de lazy load.<br>
 *
 * @author Rodrigo Leitão
 * @since 10.0.0 (9 de ago de 2018)
 */
public class UIDataProvider<VO extends RFWVO> extends AbstractBackEndDataProvider<VO, String> {

  private static final long serialVersionUID = -1475465951771876197L;

  /**
   * RFWMO usado atualmente para filtrar os objetos.
   */
  private RFWMO rfwMO = null;

  /**
   * RFWOrderBy sendo utilizado no momento.
   */
  private RFWOrderBy orderBy = null;

  /**
   * Define a lista de atributos que precisam devem ser recuperados nos objetos recuperados. Por padrão mantemos null que recupera só os atributos do objeto, sem nenhum relacionamento.
   */
  private String[] attributes = null;

  /**
   * Referência da Classe do VO que a instância do UIDataProvider esta operando.
   */
  private final Class<VO> voClass;

  /**
   * Armazena os IDs da lista que está sendo exibida no Data Provider. Essa lista é atualizada sempre que o Sort ou o MO for alterado.
   */
  private List<Long> ids = null;

  /**
   * Se definido, este atributo será utilizado para realizar um filtro (além do RFWMO já definido) nos elementos do Provider.<br>
   * Quando há mais de um objeto na lista, o texto do filtro é procurado em todos os atributos aqui definidos em um MO com a condições (OR) definida.
   */
  private List<String> filterAttributes = new LinkedList<>();

  /**
   * Salva a referência do último filtro utilizado, evitando que, caso o filtro não seja alterado não sejam feitas novas consultas sem necessidade.
   */
  private String lastFilter = null;

  /**
   * Instância do DataProvider fornecido pela aplicação para busca das informações.
   */
  private RFWDBProvider dataProvider = null;

  /**
   * Cria um provider sem orderBy inicial. Neste caso a ordernação é definida pelo próprio Core quando retornar os objetos.
   *
   * @throws RFWException
   */
  public UIDataProvider(Class<VO> voClass, RFWDBProvider dataProvider) throws RFWException {
    this.voClass = voClass;
    this.dataProvider = dataProvider;
    // Não força atualizar os IDs no construtor pois podemos ter outros filtros sendo alterado em seguida. A atualização é realizada sempre que this.ids == null.
  }

  /**
   * Cria um data provider com um orderBy inicial (ou para quando o Grid não definir nenhum)
   *
   * @param orderBy Definição de Ordemação inicial.
   * @throws RFWException
   */
  public UIDataProvider(Class<VO> voClass, RFWOrderBy orderBy, String[] attributes, RFWDBProvider dataProvider) throws RFWException {
    this(voClass, null, orderBy, attributes, dataProvider);
  }

  public UIDataProvider(Class<VO> voClass, RFWMO bisMO, RFWOrderBy orderBy, String[] attributes, RFWDBProvider dataProvider) throws RFWException {
    PreProcess.requiredNonNullCritical(dataProvider, "O UIDataProvider requer um dataProvider para funcionar corretamente!");

    this.voClass = voClass;
    this.orderBy = orderBy;
    this.attributes = attributes;
    this.rfwMO = bisMO;
    this.dataProvider = dataProvider;
    // Não força atualizar os IDs no construtor pois podemos ter outros filtros sendo alterado em seguida. A atualização é realizada sempre que this.ids == null.
  }

  @Override
  protected Stream<VO> fetchFromBackEnd(Query<VO, String> query) {
    try {
      boolean updatePending = false;

      // Converte o QuerySort em RFWOrderBy
      RFWOrderBy tmpOrderBy = writeBISorderBy(query.getSortOrders());
      // Verificamos se recebemos um OrderBy do componente, e se houve alteração no orderBy, se houve temos que atualizar a lista
      if (tmpOrderBy != null && !tmpOrderBy.equals(this.orderBy)) {
        this.orderBy = tmpOrderBy;
        updatePending = true;
      }

      // Converte o QueryFilter em um RFWMO
      RFWMO tmpMO = writeRFWMO(query.getFilter());
      if (tmpMO != null) {
        updatePending = true;
      } else {
        tmpMO = this.rfwMO; // Deixamos definido caso o updatePending esteja true por conta do orderBy
      }

      // Se mudou orderBy, Filtro, ou se ainda não temos os objetos atualizamos a lista antes de retornar os dados
      if (updatePending || this.ids == null) updateDataIDs(tmpMO);

      // Retornamos a lista vazia caso não tenhamos dado
      if (this.ids.size() == 0) return new LinkedList<VO>().stream();

      final int offset = query.getOffset();
      final int limit = query.getLimit();

      // Convertemos a sublista em uma LinkedList pq o objeto retornado pelo SubList não é serializavel e não passa pela fachada.
      LinkedList<Long> idsList = new LinkedList<>();
      idsList.addAll(this.ids.subList(offset, Math.min(this.ids.size(), offset + limit)));

      // Cria o MO para buscar os ids desejados
      RFWMO tmpMOIDs = new RFWMO();
      tmpMOIDs.in("id", idsList);

      // List<VO> list = BISSystem.getBISFacade().<VO> findList(BISUI.getSessionVO().getUUID(), this.voClass, tmpMOIDs, this.orderBy, this.attributes);
      List<VO> list = this.dataProvider.findList(this.voClass, tmpMOIDs, this.orderBy, this.attributes, null, null);
      LinkedList<VO> encapList = new LinkedList<>();
      for (VO vo : list) {
        encapList.add(vo);
      }

      return encapList.stream();
    } catch (RFWException t) {
      // Lançamos uma exception de RunTime pq retornar uma lista vazia deixa o Vaadin em loop infinito já que a quantidade já pode ter sido retornada em outro me´todo
      // return new LinkedList<VO>().stream(); // Retorna uma lista vazia para o GRID em caso de erro
      TreatException.treat(t);
      throw new RFWRunTimeException(t);
    } catch (Throwable t) {
      TreatException.treat(t);
      throw new RFWRunTimeException(new RFWCriticalException(t));
    }
  }

  private RFWMO writeRFWMO(Optional<String> filter) throws RFWException {
    RFWMO mo = null; // MO a ser utilizado na busca
    RFWMO subMO = null; // MO onde as condições do filtro devem ser colocadas. Utilizado quando temos busca combinada em mais de um atributo, já que as condições são colocadas em um SUB-MO com condição OR
    // Só vamos atualizar o VO se tivermos um atributo de filtro definido.
    if (this.filterAttributes != null && this.filterAttributes.size() > 0) {
      if (filter.isPresent()) {
        if (this.lastFilter == null || !this.lastFilter.equals(filter.get())) {
          // Se temos um filtro do componente e ele é diferente do último filtro, criamos um novo MO com o filtro adicional
          if (this.rfwMO != null) {
            if (this.filterAttributes.size() > 1) {
              // Se temos mais de um atributo para encontrar, criamos um SUBMO com a condições OR para procurar o conteudm "em qualquer um dos atributos".
              mo = this.rfwMO.cloneRecursive();
              subMO = new RFWMO(AppendMethod.OR);
              mo.getSubmo().add(subMO);
            } else {
              // Se só temos 1, colocamos a busca direta sem a necessidade de criar um SUBMO.5
              mo = this.rfwMO.cloneRecursive();
              subMO = mo;
            }
          } else {
            subMO = new RFWMO(AppendMethod.OR);
            mo = subMO;
          }
          for (String filterAttribute : this.filterAttributes) {
            subMO.like(filterAttribute, "%" + filter.get() + "%");
          }
          this.lastFilter = filter.get();
        }
      } else {
        // Se não temos mais filtro, mas antes tinhamos, retornamos o MO padrão para realizar a nova busca "sem filtros"
        if (this.lastFilter != null) {
          if (this.rfwMO != null) {
            mo = this.rfwMO;
          } else {
            mo = new RFWMO();
          }
        }
        this.lastFilter = null;
      }
    }
    return mo;
  }

  private RFWOrderBy writeBISorderBy(final List<QuerySortOrder> sortOrders) {
    RFWOrderBy tmpOrderBy = null;
    for (QuerySortOrder qSO : sortOrders) {
      if (tmpOrderBy == null) {
        tmpOrderBy = new RFWOrderBy(qSO.getSorted(), qSO.getDirection() == SortDirection.ASCENDING);
      } else {
        tmpOrderBy.addOrderbyItem(qSO.getSorted(), qSO.getDirection() == SortDirection.ASCENDING);
      }
    }
    return tmpOrderBy;
  }

  @Override
  protected int sizeInBackEnd(Query<VO, String> query) {
    try {
      boolean updatePending = false;

      // Converte o QuerySort em RFWOrderBy
      RFWOrderBy tmpOrderBy = writeBISorderBy(query.getSortOrders());
      // Verificamos se recebemos um OrderBy do componente, e se houve alteração no orderBy, se houve temos que atualizar a lista
      if (tmpOrderBy != null && !tmpOrderBy.equals(this.orderBy)) {
        this.orderBy = tmpOrderBy;
        updatePending = true;
      }

      // Converte o QueryFilter em um RFWMO
      RFWMO tmpMO = writeRFWMO(query.getFilter());
      if (tmpMO != null) {
        updatePending = true;
      } else {
        tmpMO = this.rfwMO; // Deixamos definido caso o updatePending esteja true por conta do orderBy
      }

      // Se mudou orderBy, Filtro, ou se ainda não temos os objetos atualizamos a lista antes de retornar os dados
      if (updatePending || this.ids == null) updateDataIDs(tmpMO);

      return this.ids.size();
    } catch (Throwable t) {
      throw new RFWRunTimeException("Fala ao Carregar dados no UIDataProvider! '" + this.voClass.getCanonicalName() + "'.", t);
    }
  }

  /**
   * Método utilizado para attualizar a lista de IDs dos objetos que o DataProvider tem.
   *
   * @param mo Atualiza o Provider com o MO recebido. Se for igual a NULL utiliza o bisMO definido no escopo da classe.
   *
   * @throws RFWException
   */
  private void updateDataIDs(RFWMO mo) throws RFWException {
    // this.ids = BISSystem.getBISFacade().findIDs(BISUI.getSessionVO().getUUID(), this.voClass, mo, this.orderBy);
    this.ids = this.dataProvider.findIDs(this.voClass, mo, this.orderBy);
    super.refreshAll(); // Notifica que mudamos todos os itens do Provider
  }

  /**
   * Recupera o bISMO usado atualmente para filtrar os objetos.
   *
   * @return the bISMO usado atualmente para filtrar os objetos
   * @throws RFWException
   */
  public RFWMO getRfwMO() throws RFWException {
    return rfwMO.cloneRecursive();
  }

  /**
   * Define o bISMO usado atualmente para filtrar os objetos.
   *
   * @param mo the new bISMO usado atualmente para filtrar os objetos
   */
  public void setRfwMO(RFWMO mo) throws RFWException {
    this.rfwMO = mo;
    this.ids = null;
  }

  /**
   * Recupera o bISOrderBy sendo utilizado no momento.
   *
   * @return the bISOrderBy sendo utilizado no momento
   */
  public RFWOrderBy getOrderBy() {
    return orderBy;
  }

  /**
   * Define o bISOrderBy sendo utilizado no momento.
   *
   * @param orderBy the new bISOrderBy sendo utilizado no momento
   */
  public void setOrderBy(RFWOrderBy orderBy) throws RFWException {
    this.orderBy = orderBy;
    this.ids = null;
  }

  /**
   * Recupera o referência da Classe do VO que a instância do UIDataProvider esta operando.
   *
   * @return the referência da Classe do VO que a instância do UIDataProvider esta operando
   */
  public Class<VO> getvoClass() {
    return voClass;
  }

  /**
   * Recupera o define a lista de atributos que precisam devem ser recuperados nos objetos recuperados. Por padrão mantemos null que recupera só os atributos do objeto, sem nenhum relacionamento.
   *
   * @return the define a lista de atributos que precisam devem ser recuperados nos objetos recuperados
   */
  public String[] getAttributes() {
    return attributes;
  }

  /**
   * Define o define a lista de atributos que precisam devem ser recuperados nos objetos recuperados. Por padrão mantemos null que recupera só os atributos do objeto, sem nenhum relacionamento.
   *
   * @param attributes the new define a lista de atributos que precisam devem ser recuperados nos objetos recuperados
   */
  public void setAttributes(String[] attributes) throws RFWException {
    this.attributes = attributes;
    this.ids = null;
  }

  /**
   * Método que permite saber se um determineo objeto (pelo seu ID) está presente na atual coleção do DataProvider. <B>ATENÇÃO:</B> Note que este método só retornará true caso o objeto já esteja carregado no cache, caso contrário o valor retornado será false.
   *
   * @param id ID do objeto a ser verificado.
   * @return retorna true caso esteja, retorna false caso contrário.<br>
   *         <b>Não retorna erro ou null pointer. Mesmo que o conjunto de dados ainda esteja nulo, é considerado que o objeto não está presente nesses casos.</b>
   */
  public boolean contains(Long id) {
    return (this.ids != null && this.ids.contains(id));
  }

  @Override
  public void refreshAll() {
    // Caso seja feita uma solicitação de atualizar todos os itens zeramos a listagem para garantir que o provider não usará o cache de IDs
    this.ids = null;
    super.refreshAll();
  }

  /**
   * Recupera o se definido, este atributo será utilizado para realizar um filtro (além do RFWMO já definido) nos elementos do Provider.<br>
   * Quando há mais de um objeto na lista, o texto do filtro é procurado em todos os atributos aqui definidos em um MO com a condições (OR) definida.
   *
   * @return the se definido, este atributo será utilizado para realizar um filtro (além do RFWMO já definido) nos elementos do Provider
   */
  public List<String> getFilterAttributes() {
    return filterAttributes;
  }

  /**
   * Define o se definido, este atributo será utilizado para realizar um filtro (além do RFWMO já definido) nos elementos do Provider.<br>
   * Quando há mais de um objeto na lista, o texto do filtro é procurado em todos os atributos aqui definidos em um MO com a condições (OR) definida.
   *
   * @param filterAttributes the new se definido, este atributo será utilizado para realizar um filtro (além do RFWMO já definido) nos elementos do Provider
   */
  public void setFilterAttributes(List<String> filterAttributes) {
    this.filterAttributes = filterAttributes;
  }

  /**
   * Método auxiliar, permite definir diretamente um único atributo (cria a lista de qualquer forma internamente à este método, por isso não há método get equivalente.
   *
   * @param attribute Atributo a ser utilizado no filtro de forma "singular" (sem busca combinada com vários atributos). Se passado nulo, deixa sem atributos e consequentemente sem filtro.
   */
  public void setFilterAttribute(String attribute) {
    if (attribute != null) {
      this.filterAttributes = new LinkedList<>();
      this.filterAttributes.add(attribute);
    } else {
      this.filterAttributes = null;
    }
  }

  /**
   * Retorna a lista de IDs dos objetos que estão atualmente no Provider. Retorna uma lista clonada para segurança contra manipulação externa dos dados.
   *
   * @throws RFWException
   */
  public List<Long> getIds() throws RFWException {
    if (ids == null) updateDataIDs(this.rfwMO);
    return new LinkedList<Long>(ids);
  }

  /**
   * Retorna um indicador se temos o objeto desejado atualmente no provider.<br>
   * Note que "ter o objeto" indica apenas se o ID faz parte da lista do provider, o objeto não fica na memória do Provider.
   *
   * @param id Identificador do objeto a ser verificado.
   * @return true caso o objeto esteja no provider, false caso contrário.
   * @throws RFWException
   */
  public boolean hasId(long id) throws RFWException {
    if (ids == null) updateDataIDs(this.rfwMO);
    return this.ids.contains(id);
  }

  public RFWDBProvider getDataProvider() {
    return dataProvider;
  }

  public void setDataProvider(RFWDBProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

}
