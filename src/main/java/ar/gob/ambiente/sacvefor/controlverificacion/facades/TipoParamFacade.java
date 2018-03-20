
package ar.gob.ambiente.sacvefor.controlverificacion.facades;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.Parametrica;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.TipoParam;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Acceso a datos para la entidad TipoParam
 * @author rincostante
 */
@Stateless
public class TipoParamFacade extends AbstractFacade<TipoParam> {

    /**
     * Variable privada: EntityManager al que se le indica la unidad de persistencia mediante la cual accederá a la base de datos
     */ 
    @PersistenceContext(unitName = "sacvefor-controlVerificacionPU")
    private EntityManager em;

    /**
     * Método que implementa el abstracto para la obtención del EntityManager
     * @return EntityManager para acceder a datos
     */ 
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Constructor
     */
    public TipoParamFacade() {
        super(TipoParam.class);
    }
    
    /**
     * Método para validar la existencia de un TipoParam según su nombre
     * @param nombre String Nombre del TipoParam a validar
     * @return TipoParam tipo de paramétrica correspondiente
     */
    public TipoParam getExistente(String nombre) {
        List<TipoParam> lstParam;
        em = getEntityManager();
        
        String queryString = "SELECT tipoParam FROM TipoParam tipoParam "
                + "WHERE tipoParam.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        lstParam = q.getResultList();
        if(lstParam.isEmpty()){
            return null;
        }else{
            return lstParam.get(0);
        }
    }    

    /**
     * Método sobreescrito que lista los TipoParam ordenados por nombre
     * @return List<TipoParam> listado de los tipos de paramétricas ordenados por nombre
     */
    @Override
    public List<TipoParam> findAll(){
        em = getEntityManager();
        String queryString = "SELECT tipoParam FROM TipoParam tipoParam "
                + "ORDER BY tipoParam.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }  

    /**
     * Mátodo que solo devuelve los Tipos de Parametricas habilitados.
     * @return List<TipoParam> listado de los tipos de paramétricas habilitados
     */
    public List<TipoParam> getHabilitados(){
        em = getEntityManager();
        String queryString = "SELECT tipoParam FROM TipoParam tipoParam "
                + "WHERE tipoParam.habilitado = true "
                + "ORDER BY tipoParam.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    } 

    /**
     * Método que devuelve las Paramétricas para un tipo determinado por su id
     * Consumido por la API REST
     * @param idTipo Long identificación del tipo buscado
     * @return List<Parametrica> listado de las Paramétricas vinculadas al tipo correspondiente
     */
    public List<Parametrica> getParamByTipo(Long idTipo){
        em = getEntityManager();
        String queryString = "SELECT param FROM Parametrica param "
                + "WHERE param.tipo.id = :idTipo "
                + "AND param.habilitado = true "
                + "ORDER BY param.nombre";
        Query q = em.createQuery(queryString)
                .setParameter("idTipo", idTipo);;
        return q.getResultList();
    }    
}
