package br.eng.rodrigogml.rfw.vaadin.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;

import br.eng.rodrigogml.rfw.kernel.exceptions.RFWException;
import br.eng.rodrigogml.rfw.kernel.exceptions.RFWRunTimeException;
import br.eng.rodrigogml.rfw.kernel.interfaces.RFWDBProvider;
import br.eng.rodrigogml.rfw.kernel.utils.RUReflex;
import br.eng.rodrigogml.rfw.kernel.vo.GVO;
import br.eng.rodrigogml.rfw.kernel.vo.RFWMO;
import br.eng.rodrigogml.rfw.kernel.vo.RFWOrderBy;
import br.eng.rodrigogml.rfw.kernel.vo.RFWVO;

/**
 * Description: Classe que monta um Data provider buscando os objetos diretamente no banco de dados.<br>
 *
 * @author Rodrigo Leit�o
 * @since 10.0.0 (13 de nov de 2018)
 */
public class UITreeDataProvider<VO extends RFWVO> extends AbstractBackEndHierarchicalDataProvider<GVO<VO>, Object> {

  private static final long serialVersionUID = -4329580118360902093L;

  private final Class<VO> voClass;
  private final String parentAttribute;
  private RFWOrderBy orderBy = null;
  private Long[] ignoreIDs = null;
  private Long[] rootSubSetIDs = null;

  /**
   * Esta hash armazena a Lista de IDs dos objetos filhos, para cada objeto.<br>
   * Chave ID do objeto pai, conte�do lista de IDs dos objetos filhos.
   */
  private final HashMap<Long, List<Long>> childrenHash = new HashMap<>();

  /**
   * Hash com os IDs do objeto pai. Esta Hash permite que possamamos navegar no sentido da "raiz" da �rvore. Descobrindo o objeto pai de um objeto.<br>
   * Chave: ID do objeto, Valor: ID do objeto pai, ou NULL caso seja um objeto ra�z.<br>
   * Note que o valor nulo indica que o objeto � raiz, a "n�o presen�a" da chave na hash indica que o objeto � desconhecido.
   */
  private final HashMap<Long, Long> parentHash = new HashMap<>();

  /**
   * Inst�ncia do DataProvider fornecido pela aplica��o para busca das informa��es.
   */
  private RFWDBProvider dataProvider = null;

  /**
   * Cria um TreeDataProvider que busca os objetos automaticamente no banco de dados a medida que s�o solicitados pelos componentes. Note que esta implementa��o N�O far� cache DOS OBJETOS
   *
   * @param voClass Classe do Objeto da Hierarquia
   * @param parentAttribute Atributo do objeto onde encontramos o objeto pai.
   * @param orderBy utilizado para ordernar os itens
   */
  public UITreeDataProvider(Class<VO> voClass, String parentAttribute, RFWOrderBy orderBy, RFWDBProvider dataProvider) {
    this(voClass, parentAttribute, orderBy, null, null, dataProvider);
  }

  /**
   * Cria um TreeDataProvider que busca os objetos automaticamente no banco de dados a medida que s�o solicitados pelos componentes. Note que esta implementa��o N�O far� cache DOS OBJETOS
   *
   * @param voClass Classe do Objeto da Hierarquia
   * @param parentAttribute Atributo do objeto onde encontramos o objeto pai.
   * @param orderBy utilizado para ordernar os itens
   * @param ignoreIDs Array com os IDs que devem "desaparecer" do provider. Util quando estamos editando algum objeto da hierarquia e n�o queremos que ele (ou seus filhos) sejam exibidos para serem selecionados como pai. Note que ao definir que um ID n�o deve aparecer, todo o n� abaixo dele desaparecer� tamb�m.
   */
  public UITreeDataProvider(Class<VO> voClass, String parentAttribute, RFWOrderBy orderBy, Long[] ignoreIDs, RFWDBProvider dataProvider) {
    this(voClass, parentAttribute, orderBy, ignoreIDs, null, dataProvider);
  }

  /**
   * Cria um TreeDataProvider que busca os objetos automaticamente no banco de dados a medida que s�o solicitados pelos componentes. Note que esta implementa��o N�O far� cache DOS OBJETOS
   *
   * @param voClass Classe do Objeto da Hierarquia
   * @param parentAttribute Atributo do objeto onde encontramos o objeto pai.
   * @param orderBy utilizado para ordernar os itens
   * @param ignoreIDs Array com os IDs que devem "desaparecer" do provider. Util quando estamos editando algum objeto da hierarquia e n�o queremos que ele (ou seus filhos) sejam exibidos para serem selecionados como pai. Note que ao definir que um ID n�o deve aparecer, todo o n� abaixo dele desaparecer� tamb�m.
   * @param rootSubSetIDs Array com o conjunto de IDs que devem ser recuperados do banco de dados. Note que se este atributo for informado, apenas esses objetos ser�o exibidos como objetos raiz, mesmo que eles tenham hierarquia acima deles. <B>NOTE: QUE MESMO QUE O ID FOR COLOCADO COMO OBJETO RAIZ, MAS ELE CONSTAR NO ATRIBUTO DE ignoreIDs, O OBJETO SER� IGNORADO!</B>
   */
  public UITreeDataProvider(Class<VO> voClass, String parentAttribute, RFWOrderBy orderBy, Long[] ignoreIDs, Long[] rootSubSetIDs, RFWDBProvider dataProvider) {
    this.voClass = voClass;
    this.parentAttribute = parentAttribute;
    this.orderBy = orderBy;
    this.ignoreIDs = ignoreIDs;
    this.rootSubSetIDs = rootSubSetIDs;
    this.dataProvider = dataProvider;
  }

  @Override
  public int getChildCount(HierarchicalQuery<GVO<VO>, Object> query) {
    try {
      if (Optional.empty().equals(query.getFilter())) {
        // Se n�o temos filtros, retornamos o total de objetos ra�z
        return getChildrenList(null).size();
      }
      // Verificamos se j� temos esse objeto carregado na Hash
      // return getChildrenList(item.getId()).size() > 0;
      System.out.println("getChildCount!");
      return 10;
    } catch (RFWException e) {
      throw new RFWRunTimeException(e);
    }
  }

  @Override
  public boolean hasChildren(GVO<VO> item) {
    try {
      // Verificamos se j� temos esse objeto carregado na Hash
      return getChildrenList(item.getVO().getId()).size() > 0;
    } catch (RFWException e) {
      throw new RFWRunTimeException(e);
    }
  }

  private List<Long> getChildrenList(Long itemID) throws RFWException {
    List<Long> children = childrenHash.get(itemID);
    if (children == null) {
      RFWMO mo = new RFWMO();
      if (itemID != null) {
        mo.equal(this.parentAttribute + ".id", itemID);
      } else {
        // Se n�o recebemos um ID de filtro, � pq devemos retornar os objetos raiz
        if (this.rootSubSetIDs == null) {
          // Se n�o temos um subconjunto de objetos raiz, retornamos todos os objetos raiz existentes no banco de dados
          mo.isNull(this.parentAttribute + ".id");
        } else {
          // Se temos um novo subconjunto de "raiz" definido, vamos recuperar esse conjunto de objetos
          mo.in("id", rootSubSetIDs);
        }
      }

      // N�o permite selecionar os objetos indesejados
      if (this.ignoreIDs != null) mo.notIn("id", this.ignoreIDs);

      children = this.dataProvider.findIDs(this.voClass, mo, this.orderBy);
      childrenHash.put(itemID, children);
      for (Long id : children) {
        parentHash.put(id, itemID);
      }
    }
    return children;
  }

  @Override
  protected Stream<GVO<VO>> fetchChildrenFromBackEnd(HierarchicalQuery<GVO<VO>, Object> query) {
    try {
      final List<Long> childrenList;
      if (query.getParent() == null) {
        // Se n�o temos filtros, retornamos o total de objetos ra�z
        childrenList = getChildrenList(null);
      } else {
        childrenList = getChildrenList(query.getParent().getVO().getId());
      }

      // Se n�o temos filhos, retornamos o Stream de uma lista vazia. N�o deixamos ir para o banco.
      if (childrenList.size() == 0) {
        return new ArrayList<GVO<VO>>().stream();
      } else {

        // Recuperamos todos os objetos que est�o na lista
        RFWMO mo = new RFWMO();
        // mo.in("id", childrenList.subList(query.getOffset(), query.getOffset() + query.getLimit()).toArray(new Long[0]));
        mo.in("id", childrenList.toArray(new Long[0]));

        final List<VO> list = this.dataProvider.findList(this.voClass, mo, this.orderBy, null, null, null);
        final LinkedList<GVO<VO>> encapList = new LinkedList<>();
        for (VO vo : list) {
          encapList.add(new GVO<>(vo));
        }
        return encapList.stream();
      }
    } catch (RFWException e) {
      throw new RFWRunTimeException(e);
    }
  }

  /**
   * M�todo que permite saber se um determinado objeto (pelo seu ID) est� presente na atual cole��o do DataProvider.<br>
   * <B>ATEN��O:</B> Note que este m�todo s� retornar� true caso o objeto j� esteja carregado no cache, caso contr�rio o valor retornado ser� false.
   *
   * @param id ID do objeto a ser verificado.
   * @return retorna true caso esteja, retorna false caso contr�rio.<br>
   *         <b>N�o retorna erro ou null pointer. Mesmo que o conjunto de dados ainda esteja nulo, � considerado que o objeto n�o est� presente nesses casos.</b>
   * @throws RFWException
   */
  public boolean contains(Long id) throws RFWException {
    // Utilizamos a parentHash ao inv�s da ChildrenHash, pois a children s� tem os objetos cujo os filhos foram carregados, a parent tem todos os objetos, pais ou filhos j� "conhecidos"
    boolean isLoaded = this.parentHash.containsKey(id);
    // Se n�o encontramos ele carregado, vamos procura-lo na base de dados
    if (!isLoaded) {
      final List<Long> parentList = getParentPath(id);
      // Se a lista de caminho retornar nula, significa que ele n�o foi encontrado nem na base. Quand o objeto � raiz, o m�todo getParentPath retorna seu ID.
      isLoaded = parentList != null;
    }
    return isLoaded;
  }

  /**
   * Este m�todo busca o caminho at� um determinado objeto na estrutura de �rvore.
   *
   * @param id ID do objeto desejado
   * @return Lista com os IDs dos objetos, sendo o primeiro ID o objeto raiz, o segundo o de segundo n�vel e assim por diante at� chegar no objeto desejado. Retorna nulo caso o objeto n�o seja encontrado
   * @throws RFWException
   */
  @SuppressWarnings("unchecked")
  public List<Long> getParentPath(Long id) throws RFWException {
    List<Long> list = null;
    // Verificamos se o objeto � conhecido do provider
    if (!this.parentHash.containsKey(id)) {
      // Se ainda n�o � conhecido, buscamos o objeto para inclui-lo na estrutura do provider
      final VO vo = (VO) this.dataProvider.findByID(this.voClass, id, new String[] { this.parentAttribute + ".id" });
      if (vo != null) {
        final VO parent = (VO) RUReflex.getPropertyValue(vo, this.parentAttribute);
        if (parent == null) {
          this.parentHash.put(id, null);
        } else {
          this.parentHash.put(id, parent.getId());
        }
      }
    }

    // Se ainda n�o encontramos o objeto, ser� retonada a lista vazia
    if (this.parentHash.containsKey(id)) {

      // Recuperamos o ID do pai do objeto
      final Long parentID = this.parentHash.get(id);
      if (parentID == null) {
        // Se n�o temos um pai, estamos no objeto ra�z, logo � o primeiro objeto a ser encontrado, come�amos a lista e retornamos
        list = new LinkedList<>();
        list.add(id);
      } else {
        // Se temos um ID do pai, solicitamos o caminho at� o pai recursivamente e nos incluimos no fim da lista
        list = getParentPath(parentID);
        list.add(id);
      }
    }
    return list;
  }

  /**
   * Retorna os IDs de todos os objetos conhecidos pelo Provider
   *
   * @return
   */
  public Collection<Long> getAllItemsIDs() {
    return this.parentHash.values();
  }

  @Override
  public void refreshAll() {
    // limpamos o cache para for�ar uma completa releitura
    this.parentHash.clear();
    this.childrenHash.clear();
    super.refreshAll();
  }

  @Override
  public void refreshItem(GVO<VO> item) {
    // Removemos apenas o objeto atual do cache de filhos e pai para garantir que este objeto seja relido
    this.childrenHash.remove(item.getVO().getId());
    this.parentHash.remove(item.getVO().getId());
    super.refreshItem(item);
  }

  /**
   * Gets the ignore I ds.
   *
   * @return the ignore I ds
   */
  public Long[] getIgnoreIDs() {
    return ignoreIDs;
  }

  /**
   * Sets the ignore I ds.
   *
   * @param ignoreIDs the new ignore I ds
   */
  public void setIgnoreIDs(Long[] ignoreIDs) {
    this.ignoreIDs = ignoreIDs;
  }

  /**
   * Gets the root sub set I ds.
   *
   * @return the root sub set I ds
   */
  public Long[] getRootSubSetIDs() {
    return rootSubSetIDs;
  }

  /**
   * Sets the root sub set I ds.
   *
   * @param rootSubSetIDs the new root sub set I ds
   */
  public void setRootSubSetIDs(Long[] rootSubSetIDs) {
    this.rootSubSetIDs = rootSubSetIDs;
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
