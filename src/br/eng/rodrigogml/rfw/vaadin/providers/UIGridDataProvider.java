package br.eng.rodrigogml.rfw.vaadin.providers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWRunTimeException;
import br.eng.rodrigogml.rfw.kernel.interfaces.RFWDBProvider;
import br.eng.rodrigogml.rfw.kernel.preprocess.PreProcess;
import br.eng.rodrigogml.rfw.kernel.vo.GVO;
import br.eng.rodrigogml.rfw.kernel.vo.RFWMO;
import br.eng.rodrigogml.rfw.kernel.vo.RFWOrderBy;
import br.eng.rodrigogml.rfw.kernel.vo.RFWVO;
import br.eng.rodrigogml.rfw.vaadin.utils.TreatException;

/**
 * Description: Classe utilizada para prover os dados para o Grid (encapsulando o VO no GVO) da UI através de lazy load.<br>
 *
 * @author Rodrigo Leitão
 * @since 10.0.0 (9 de ago de 2018)
 */
public class UIGridDataProvider<VO extends RFWVO> extends AbstractBackEndDataProvider<GVO<VO>, String> {

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
   * Se definido, este atributo será utilizado para realizar um filtro (além do RFWMO já definido) nos elementos do Provider.
   */
  private String filterAttribute = null;

  /**
   * Salva a referência do último filtro utilizado, evitando que, caso o filtro não seja alterado não sejam feitas novas consultas sem necessidade.
   */
  private String lastFilter = null;

  /**
   * Se definido, limita a quantidade de resultados que é retornado ao realizar a busca no banco de dados.<br>
   * Útil quando a tabela contém muitos dados e não queremos sobrecarregar os resultados, forçanco a aplicação de um filtro melhor.
   */
  private Integer limitResults = null;

  /**
   * Quando {@link #limitResults} estiver definido este atrinuto indica se o retorno do banco de dados atingiu a quantidade limite.<br>
   * <li>True quando a consulta retornou a quantidade limite (podendo ou não haver mais objetos no banco - caso o banco tenha exatamente a quantidade limite este atributo terá true pq chegou no valor limite, mas o banco não tem mais nenhum para exibir)
   * <li>False Caso a quantidade de objetos retornados seja menor que o limite.
   * <li>False caso {@link #limitResults} não esteja definido.
   */
  private boolean limitedResults = false;

  /**
   * Instância do DataProvider fornecido pela aplicação para busca das informações.
   */
  private RFWDBProvider dataProvider = null;

  /**
   * Cria um provider sem orderBy inicial. Neste caso a ordernação é definida pelo próprio Core quando retornar os objetos.
   *
   * @throws RFWException
   */
  public UIGridDataProvider(Class<VO> voClass, RFWDBProvider dataProvider) throws RFWException {
    this.voClass = voClass;
    this.dataProvider = dataProvider;
  }

  /**
   * Cria um data provider com um orderBy inicial (ou para quando o Grid não definir nenhum)
   *
   * @param orderBy Definição de Ordemação inicial.
   * @throws RFWException
   */
  public UIGridDataProvider(Class<VO> voClass, RFWOrderBy orderBy, String[] attributes, RFWDBProvider dataProvider) throws RFWException {
    this(voClass, null, orderBy, attributes, dataProvider);
  }

  public UIGridDataProvider(Class<VO> voClass, RFWMO rfwMO, RFWOrderBy orderBy, String[] attributes, RFWDBProvider dataProvider) throws RFWException {
    PreProcess.requiredNonNullCritical(dataProvider, "O UIGridDataProvider requer um dataProvider para funcionar corretamente!");

    this.voClass = voClass;
    this.orderBy = orderBy;
    this.attributes = attributes;
    this.rfwMO = rfwMO;
    this.dataProvider = dataProvider;
  }

  @Override
  protected Stream<GVO<VO>> fetchFromBackEnd(Query<GVO<VO>, String> query) {
    try {
      boolean updatePending = this.ids == null;

      // Converte o QuerySort em RFWOrderBy
      RFWOrderBy tmpOrderBy = writeRFWOrderBy(query.getSortOrders());
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
      if (updatePending) updateDataIDs(tmpMO);

      final int offset = query.getOffset();
      final int limit = query.getLimit();

      // Retornamos a lista vazia caso não tenhamos dado
      if (this.ids.size() == 0 || offset >= this.ids.size()) return new LinkedList<GVO<VO>>().stream();

      // Convertemos a sublista em uma LinkedList pq o objeto retornado pelo SubList não é serializavel e não passa pela fachada.
      LinkedList<Long> idsList = new LinkedList<>();
      idsList.addAll(this.ids.subList(offset, Math.min(this.ids.size(), offset + limit)));

      // Cria o MO para buscar os ids desejados
      RFWMO tmpMOIDs = new RFWMO();
      if (idsList != null && idsList.size() > 0) tmpMOIDs.in("id", idsList);

      List<VO> list = this.dataProvider.findList(this.voClass, tmpMOIDs, this.orderBy, this.attributes, null, null);
      LinkedList<GVO<VO>> encapList = new LinkedList<>();
      for (VO vo : list) {
        encapList.add(new GVO<>(vo));
      }

      return encapList.stream();
    } catch (RFWException t) {
      // Lançamos uma exception de RunTime pq retornar uma lista vazia deixa o Vaadin em loop infinito já que a quantidade já pode ter sido retornada em outro me´todo
      TreatException.treat(t);
      // throw new RFWRunTimeException(t);
      return new LinkedList<GVO<VO>>().stream(); // Retorna uma lista vazia para o GRID em caso de erro
    } catch (Throwable t) {
      TreatException.treat(t);
      // throw new RFWRunTimeException(new RFWCriticalException(t));
      return new LinkedList<GVO<VO>>().stream(); // Retorna uma lista vazia para o GRID em caso de erro
    }
  }

  private RFWMO writeRFWMO(Optional<String> filter) throws RFWException {
    RFWMO mo = null;
    // Só vamos atualizar o VO se tivermos um atributo de filtro definido.
    if (this.filterAttribute != null) {
      if (filter.isPresent()) {
        if (this.lastFilter == null || !this.lastFilter.equals(filter.get())) {
          // Se temos um filtro do componente e ele é diferente do último filtro, criamos um novo MO com o filtro adicional
          if (this.rfwMO != null) {
            mo = this.rfwMO.cloneRecursive();
          } else {
            mo = new RFWMO();
          }
          mo.like(this.filterAttribute, "%" + filter.get() + "%");
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

  private RFWOrderBy writeRFWOrderBy(final List<QuerySortOrder> sortOrders) {
    RFWOrderBy tmpOrderBy = null;
    for (QuerySortOrder qSO : sortOrders) {
      final String sorted = qSO.getSorted().substring(3); // Remove o prefixo "VO." criado por termos encapsulado os objetos dentro do GVO
      if (tmpOrderBy == null) {
        tmpOrderBy = new RFWOrderBy(sorted, qSO.getDirection() == SortDirection.ASCENDING);
      } else {
        tmpOrderBy.addOrderbyItem(sorted, qSO.getDirection() == SortDirection.ASCENDING);
      }
    }
    return tmpOrderBy;
  }

  @Override
  protected int sizeInBackEnd(Query<GVO<VO>, String> query) {
    try {
      boolean updatePending = this.ids == null;

      // Converte o QuerySort em RFWOrderBy
      RFWOrderBy tmpOrderBy = writeRFWOrderBy(query.getSortOrders());
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
   * @param mo Atualiza o Provider com o MO recebido. Se for igual a NULL utiliza o rfwMO definido no escopo da classe.
   *
   * @throws RFWException
   */
  private void updateDataIDs(RFWMO mo) throws RFWException {
    this.ids = this.dataProvider.findIDs(this.voClass, mo, this.orderBy, null, this.limitResults);

    this.limitedResults = false;
    if (this.limitResults != null) {
      this.limitedResults = this.limitResults.compareTo(this.ids.size()) <= 0;
    }
    super.refreshAll(); // Notifica que mudamos todos os itens do Provider
  }

  /**
   * # rFWMO usado atualmente para filtrar os objetos.
   *
   * @return # rFWMO usado atualmente para filtrar os objetos
   */
  public RFWMO getRfwMO() {
    return rfwMO;
  }

  /**
   * # rFWMO usado atualmente para filtrar os objetos.
   *
   * @param mo # rFWMO usado atualmente para filtrar os objetos
   */
  public void setRfwMO(RFWMO mo) throws RFWException {
    this.rfwMO = mo;
    updateDataIDs(mo);
  }

  /**
   * # rfwOrderBy sendo utilizado no momento.
   *
   * @return the rfwOrderBy sendo utilizado no momento
   */
  public RFWOrderBy getOrderBy() {
    return orderBy;
  }

  /**
   * # rfwOrderBy sendo utilizado no momento.
   *
   * @param orderBy the new rfwOrderBy sendo utilizado no momento
   */
  public void setOrderBy(RFWOrderBy orderBy) throws RFWException {
    this.orderBy = orderBy;
    updateDataIDs(this.rfwMO);
  }

  /**
   * # referência da Classe do VO que a instância do UIDataProvider esta operando.
   *
   * @return the referência da Classe do VO que a instância do UIDataProvider esta operando
   */
  public Class<VO> getvoClass() {
    return voClass;
  }

  /**
   * # define a lista de atributos que precisam devem ser recuperados nos objetos recuperados. Por padrão mantemos null que recupera só os atributos do objeto, sem nenhum relacionamento.
   *
   * @return the define a lista de atributos que precisam devem ser recuperados nos objetos recuperados
   */
  public String[] getAttributes() {
    return attributes;
  }

  /**
   * # define a lista de atributos que precisam devem ser recuperados nos objetos recuperados. Por padrão mantemos null que recupera só os atributos do objeto, sem nenhum relacionamento.
   *
   * @param attributes the new define a lista de atributos que precisam devem ser recuperados nos objetos recuperados
   */
  public void setAttributes(String[] attributes) throws RFWException {
    this.attributes = attributes;
    updateDataIDs(this.rfwMO);
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

  /**
   * # se definido, este atributo será utilizado para realizar um filtro (além do RFWMO já definido) nos elementos do Provider.
   *
   * @return the se definido, este atributo será utilizado para realizar um filtro (além do RFWMO já definido) nos elementos do Provider
   */
  public String getFilterAttribute() {
    return filterAttribute;
  }

  /**
   * # se definido, este atributo será utilizado para realizar um filtro (além do RFWMO já definido) nos elementos do Provider.
   *
   * @param filterAttribute the new se definido, este atributo será utilizado para realizar um filtro (além do RFWMO já definido) nos elementos do Provider
   */
  public void setFilterAttribute(String filterAttribute) {
    this.filterAttribute = filterAttribute;
  }

  @Override
  public void refreshAll() {
    // Caso seja feita uma solicitação de atualizar todos os itens zeramos a listagem para garantir que o provider não usará o cache de IDs
    this.ids = null;
    super.refreshAll();
  }

  /**
   * Retorna a quantidade atual de itens no Provider. Note que retorna conforme a quantidade de IDs que foram obtidos desde o último refresh() (o que compõe os dados do provider atualmente) e que podem ser afetados pelo {@link #limitedResults}. Pode ou não representar o total de itens existente no banco de dados se {@link #limitedResults} for definido.
   *
   * @return Quantidade atual de Itens no Provider.
   */
  public int size() {
    if (this.ids == null) return 0;
    return this.ids.size();
  }

  /**
   * # se definido, limita a quantidade de resultados que é retornado ao realizar a busca no banco de dados.<br>
   * Útil quando a tabela contém muitos dados e não queremos sobrecarregar os resultados, forçanco a aplicação de um filtro melhor.
   *
   * @return the se definido, limita a quantidade de resultados que é retornado ao realizar a busca no banco de dados
   */
  public Integer getLimitResults() {
    return limitResults;
  }

  /**
   * # se definido, limita a quantidade de resultados que é retornado ao realizar a busca no banco de dados.<br>
   * Útil quando a tabela contém muitos dados e não queremos sobrecarregar os resultados, forçanco a aplicação de um filtro melhor.
   *
   * @param limitResults the new se definido, limita a quantidade de resultados que é retornado ao realizar a busca no banco de dados
   */
  public void setLimitResults(Integer limitResults) {
    this.limitResults = limitResults;
  }

  /**
   * # quando {@link #limitResults} estiver definido este atrinuto indica se o retorno do banco de dados atingiu a quantidade limite.<br>
   * <li>True quando a consulta retornou a quantidade limite (podendo ou não haver mais objetos no banco - caso o banco tenha exatamente a quantidade limite este atributo terá true pq chegou no valor limite, mas o banco não tem mais nenhum para exibir)
   * <li>False Caso a quantidade de objetos retornados seja menor que o limite.
   * <li>False caso {@link #limitResults} não esteja definido.
   *
   * @return the quando {@link #limitResults} estiver definido este atrinuto indica se o retorno do banco de dados atingiu a quantidade limite
   */
  public boolean isLimitedResults() {
    return limitedResults;
  }

  /**
   * # instância do DataProvider fornecido pela aplicação para busca das informações.
   *
   * @return # instância do DataProvider fornecido pela aplicação para busca das informações
   */
  public RFWDBProvider getDataProvider() {
    return dataProvider;
  }

  /**
   * # instância do DataProvider fornecido pela aplicação para busca das informações.
   *
   * @param dataProvider # instância do DataProvider fornecido pela aplicação para busca das informações
   */
  public void setDataProvider(RFWDBProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

}
