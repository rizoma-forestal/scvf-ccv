
package ar.gob.ambiente.sacvefor.controlverificacion.facades;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.ComponenteLocal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Acceso a datos para la entidad ComponenteLocal
 * @author rincostante
 */
@Stateless
public class ComponenteLocalFacade extends AbstractFacade<ComponenteLocal> {

    @PersistenceContext(unitName = "sacvefor-controlVerificacionPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComponenteLocalFacade() {
        super(ComponenteLocal.class);
    }
    
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
     * @param provincia
     * @return 
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
     * @return 
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
