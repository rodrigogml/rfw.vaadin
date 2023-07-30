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
 * Description: Classe utilizada para prover os dados para o Grid (encapsulando o VO no GVO) da UI atrav�s de lazy load.<br>
 *
 * @author Rodrigo Leit�o
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
   * Define a lista de atributos que precisam devem ser recuperados nos objetos recuperados. Por padr�o mantemos null que recupera s� os atributos do objeto, sem nenhum relacionamento.
   */
  private String[] attributes = null;

  /**
   * Refer�ncia da Classe do VO que a inst�ncia do UIDataProvider esta operando.
   */
  private final Class<VO> voClass;

  /**
   * Armazena os IDs da lista que est� sendo exibida no Data Provider. Essa lista � atualizada sempre que o Sort ou o MO for alterado.
   */
  private List<Long> ids = null;

  /**
   * Se definido, este atributo ser� utilizado para realizar um filtro (al�m do RFWMO j� definido) nos elementos do Provider.
   */
  private String filterAttribute = null;

  /**
   * Salva a refer�ncia do �ltimo filtro utilizado, evitando que, caso o filtro n�o seja alterado n�o sejam feitas novas consultas sem necessidade.
   */
  private String lastFilter = null;

  /**
   * Se definido, limita a quantidade de resultados que � retornado ao realizar a busca no banco de dados.<br>
   * �til quando a tabela cont�m muitos dados e n�o queremos sobrecarregar os resultados, for�anco a aplica��o de um filtro melhor.
   */
  private Integer limitResults = null;

  /**
   * Quando {@link #limitResults} estiver definido este atrinuto indica se o retorno do banco de dados atingiu a quantidade limite.<br>
   * <li>True quando a consulta retornou a quantidade limite (podendo ou n�o haver mais objetos no banco - caso o banco tenha exatamente a quantidade limite este atributo ter� true pq chegou no valor limite, mas o banco n�o tem mais nenhum para exibir)
   * <li>False Caso a quantidade de objetos retornados seja menor que o limite.
   * <li>False caso {@link #limitResults} n�o esteja definido.
   */
  private boolean limitedResults = false;

  /**
   * Inst�ncia do DataProvider fornecido pela aplica��o para busca das informa��es.
   */
  private RFWDBProvider dataProvider = null;

  /**
   * Cria um provider sem orderBy inicial. Neste caso a orderna��o � definida pelo pr�prio Core quando retornar os objetos.
   *
   * @throws RFWException
   */
  public UIGridDataProvider(Class<VO> voClass, RFWDBProvider dataProvider) throws RFWException {
    this.voClass = voClass;
    this.dataProvider = dataProvider;
  }

  /**
   * Cria um data provider com um orderBy inicial (ou para quando o Grid n�o definir nenhum)
   *
   * @param orderBy Defini��o de Ordema��o inicial.
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
      // Verificamos se recebemos um OrderBy do componente, e se houve altera��o no orderBy, se houve temos que atualizar a lista
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

      // Se mudou orderBy, Filtro, ou se ainda n�o temos os objetos atualizamos a lista antes de retornar os dados
      if (updatePending) updateDataIDs(tmpMO);

      final int offset = query.getOffset();
      final int limit = query.getLimit();

      // Retornamos a lista vazia caso n�o tenhamos dado
      if (this.ids.size() == 0 || offset >= this.ids.size()) return new LinkedList<GVO<VO>>().stream();

      // Convertemos a sublista em uma LinkedList pq o objeto retornado pelo SubList n�o � serializavel e n�o passa pela fachada.
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
      // Lan�amos uma exception de RunTime pq retornar uma lista vazia deixa o Vaadin em loop infinito j� que a quantidade j� pode ter sido retornada em outro me�todo
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
    // S� vamos atualizar o VO se tivermos um atributo de filtro definido.
    if (this.filterAttribute != null) {
      if (filter.isPresent()) {
        if (this.lastFilter == null || !this.lastFilter.equals(filter.get())) {
          // Se temos um filtro do componente e ele � diferente do �ltimo filtro, criamos um novo MO com o filtro adicional
          if (this.rfwMO != null) {
            mo = this.rfwMO.cloneRecursive();
          } else {
            mo = new RFWMO();
          }
          mo.like(this.filterAttribute, "%" + filter.get() + "%");
          this.lastFilter = filter.get();
        }
      } else {
        // Se n�o temos mais filtro, mas antes tinhamos, retornamos o MO padr�o para realizar a nova busca "sem filtros"
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
      // Verificamos se recebemos um OrderBy do componente, e se houve altera��o no orderBy, se houve temos que atualizar a lista
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

      // Se mudou orderBy, Filtro, ou se ainda n�o temos os objetos atualizamos a lista antes de retornar os dados
      if (updatePending || this.ids == null) updateDataIDs(tmpMO);

      return this.ids.size();
    } catch (Throwable t) {
      throw new RFWRunTimeException("Fala ao Carregar dados no UIDataProvider! '" + this.voClass.getCanonicalName() + "'.", t);
    }
  }

  /**
   * M�todo utilizado para attualizar a lista de IDs dos objetos que o DataProvider tem.
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
   * # refer�ncia da Classe do VO que a inst�ncia do UIDataProvider esta operando.
   *
   * @return the refer�ncia da Classe do VO que a inst�ncia do UIDataProvider esta operando
   */
  public Class<VO> getvoClass() {
    return voClass;
  }

  /**
   * # define a lista de atributos que precisam devem ser recuperados nos objetos recuperados. Por padr�o mantemos null que recupera s� os atributos do objeto, sem nenhum relacionamento.
   *
   * @return the define a lista de atributos que precisam devem ser recuperados nos objetos recuperados
   */
  public String[] getAttributes() {
    return attributes;
  }

  /**
   * # define a lista de atributos que precisam devem ser recuperados nos objetos recuperados. Por padr�o mantemos null que recupera s� os atributos do objeto, sem nenhum relacionamento.
   *
   * @param attributes the new define a lista de atributos que precisam devem ser recuperados nos objetos recuperados
   */
  public void setAttributes(String[] attributes) throws RFWException {
    this.attributes = attributes;
    updateDataIDs(this.rfwMO);
  }

  /**
   * M�todo que permite saber se um determineo objeto (pelo seu ID) est� presente na atual cole��o do DataProvider. <B>ATEN��O:</B> Note que este m�todo s� retornar� true caso o objeto j� esteja carregado no cache, caso contr�rio o valor retornado ser� false.
   *
   * @param id ID do objeto a ser verificado.
   * @return retorna true caso esteja, retorna false caso contr�rio.<br>
   *         <b>N�o retorna erro ou null pointer. Mesmo que o conjunto de dados ainda esteja nulo, � considerado que o objeto n�o est� presente nesses casos.</b>
   */
  public boolean contains(Long id) {
    return (this.ids != null && this.ids.contains(id));
  }

  /**
   * # se definido, este atributo ser� utilizado para realizar um filtro (al�m do RFWMO j� definido) nos elementos do Provider.
   *
   * @return the se definido, este atributo ser� utilizado para realizar um filtro (al�m do RFWMO j� definido) nos elementos do Provider
   */
  public String getFilterAttribute() {
    return filterAttribute;
  }

  /**
   * # se definido, este atributo ser� utilizado para realizar um filtro (al�m do RFWMO j� definido) nos elementos do Provider.
   *
   * @param filterAttribute the new se definido, este atributo ser� utilizado para realizar um filtro (al�m do RFWMO j� definido) nos elementos do Provider
   */
  public void setFilterAttribute(String filterAttribute) {
    this.filterAttribute = filterAttribute;
  }

  @Override
  public void refreshAll() {
    // Caso seja feita uma solicita��o de atualizar todos os itens zeramos a listagem para garantir que o provider n�o usar� o cache de IDs
    this.ids = null;
    super.refreshAll();
  }

  /**
   * Retorna a quantidade atual de itens no Provider. Note que retorna conforme a quantidade de IDs que foram obtidos desde o �ltimo refresh() (o que comp�e os dados do provider atualmente) e que podem ser afetados pelo {@link #limitedResults}. Pode ou n�o representar o total de itens existente no banco de dados se {@link #limitedResults} for definido.
   *
   * @return Quantidade atual de Itens no Provider.
   */
  public int size() {
    if (this.ids == null) return 0;
    return this.ids.size();
  }

  /**
   * # se definido, limita a quantidade de resultados que � retornado ao realizar a busca no banco de dados.<br>
   * �til quando a tabela cont�m muitos dados e n�o queremos sobrecarregar os resultados, for�anco a aplica��o de um filtro melhor.
   *
   * @return the se definido, limita a quantidade de resultados que � retornado ao realizar a busca no banco de dados
   */
  public Integer getLimitResults() {
    return limitResults;
  }

  /**
   * # se definido, limita a quantidade de resultados que � retornado ao realizar a busca no banco de dados.<br>
   * �til quando a tabela cont�m muitos dados e n�o queremos sobrecarregar os resultados, for�anco a aplica��o de um filtro melhor.
   *
   * @param limitResults the new se definido, limita a quantidade de resultados que � retornado ao realizar a busca no banco de dados
   */
  public void setLimitResults(Integer limitResults) {
    this.limitResults = limitResults;
  }

  /**
   * # quando {@link #limitResults} estiver definido este atrinuto indica se o retorno do banco de dados atingiu a quantidade limite.<br>
   * <li>True quando a consulta retornou a quantidade limite (podendo ou n�o haver mais objetos no banco - caso o banco tenha exatamente a quantidade limite este atributo ter� true pq chegou no valor limite, mas o banco n�o tem mais nenhum para exibir)
   * <li>False Caso a quantidade de objetos retornados seja menor que o limite.
   * <li>False caso {@link #limitResults} n�o esteja definido.
   *
   * @return the quando {@link #limitResults} estiver definido este atrinuto indica se o retorno do banco de dados atingiu a quantidade limite
   */
  public boolean isLimitedResults() {
    return limitedResults;
  }

  /**
   * # inst�ncia do DataProvider fornecido pela aplica��o para busca das informa��es.
   *
   * @return # inst�ncia do DataProvider fornecido pela aplica��o para busca das informa��es
   */
  public RFWDBProvider getDataProvider() {
    return dataProvider;
  }

  /**
   * # inst�ncia do DataProvider fornecido pela aplica��o para busca das informa��es.
   *
   * @param dataProvider # inst�ncia do DataProvider fornecido pela aplica��o para busca das informa��es
   */
  public void setDataProvider(RFWDBProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

}
