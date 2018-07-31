
package ar.gob.ambiente.sacvefor.controlverificacion.facades;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.ComponenteLocal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Acceso a datos para la gestión de la Entidad ComponenteLocal
 * @author rincostante
 */
@Stateless
public class ComponenteLocalFacade extends AbstractFacade<ComponenteLocal> {

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
    public ComponenteLocalFacade() {
        super(ComponenteLocal.class);
    }
    
    /**
     * Método de AbstractFacade sobreescrito para que devuelva todos los componentes locales ordenados alfabeticamente por su Provincia
     * @return List<ComponenteLocal> listado de todos los componentes locales ordenados
     */
    @Override
    public List<ComponenteLocal> findAll(){
        em = getEntityManager();
        String queryString = "SELECT comp FROM ComponenteLocal comp "
                + "ORDER BY comp.provincia";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }

    /**
     * Metodo para validar un Componente existente según el nombre de su Provincia
     * @param provincia String Nombre de la Provincia del componente local
     * @return ComponenteLocal Componente local obtenido
     */
    public ComponenteLocal getExistente(String provincia){
        List<ComponenteLocal> lstCuentas;
        em = getEntityManager();
        
        String queryString = "SELECT comp FROM ComponenteLocal comp  "
                + "WHERE comp.provincia = :provincia";
        Query q = em.createQuery(queryString)
                .setParameter("provincia", provincia);
        lstCuentas = q.getResultList();
        if(lstCuentas.isEmpty()){
            return null;
        }else{
            return lstCuentas.get(0);
        }
    }

    /**
     * Método para obtener todos los Componentes habilitados
     * @return List<ComponenteLocal> listado de los componentes locales habilitados
     */
    public List<ComponenteLocal> getHabilitados(){
        em = getEntityManager();
        String queryString = "SELECT comp FROM ComponenteLocal comp "
                + "WHERE comp.habilitado = true "
                + "ORDER BY comp.provincia";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }    
}
