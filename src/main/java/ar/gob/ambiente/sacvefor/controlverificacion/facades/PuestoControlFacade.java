
package ar.gob.ambiente.sacvefor.controlverificacion.facades;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.PuestoControl;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Acceso a datos para la entidad PuestoControl
 * @author rincostante
 */
@Stateless
public class PuestoControlFacade extends AbstractFacade<PuestoControl> {

    @PersistenceContext(unitName = "sacvefor-controlVerificacionPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PuestoControlFacade() {
        super(PuestoControl.class);
    }
    
    /**
     * Método para validar la existencia de un Puesto de control según su nombre y idLocalidad
     * @param nombre
     * @param idLocGt
     * @return 
     */
    public PuestoControl getExistente(String nombre, Long idLocGt) {
        List<PuestoControl> lstPuestos;
        em = getEntityManager();
        
        String queryString = "SELECT puesto FROM PuestoControl puesto "
                + "WHERE puesto.nombre = :nombre "
                + "AND puesto.idLocGt = :idLocGt";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre)
                .setParameter("idLocGt", idLocGt);
        lstPuestos = q.getResultList();
        if(lstPuestos.isEmpty()){
            return null;
        }else{
            return lstPuestos.get(0);
        }
    } 
    
    /**
     * Método sobreescrito que lista los Puestos de control ordenados por nombre
     * @return 
     */
    @Override
    public List<PuestoControl> findAll(){
        em = getEntityManager();
        String queryString = "SELECT puesto FROM PuestoControl puesto "
                + "ORDER BY puesto.nombre";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }       
    
    /**
     * Mátodo que solo devuelve los Puestos de control habilitados.
     * Para poblar combos de selección.
     * @return 
     */
    public List<PuestoControl> getHabilitados(){
        em = getEntityManager();
        String queryString = "SELECT puesto FROM PuestoControl puesto "
                + "WHERE puesto.habilitado = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }      
    
    /**
     * Método que devuelve los Puestos de Control ubicados en una Localidad
     * @param localidad
     * @return 
     */
    public List<PuestoControl> getByLocalidad(String localidad){
        em = getEntityManager();
        String queryString = "SELECT puesto FROM PuestoControl puesto "
                + "WHERE puesto.localidad = :localidad "
                + "AND puesto.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("localidad", localidad);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve los Puestos de Control ubicados en un Departamento
     * @param departamento
     * @return 
     */
    public List<PuestoControl> getByDepartamento(String departamento){
        em = getEntityManager();
        String queryString = "SELECT puesto FROM PuestoControl puesto "
                + "WHERE puesto.departamento = :departamento "
                + "AND puesto.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("departamento", departamento);
        return q.getResultList();
    }    
    
    /**
     * Método que devuelve los Puestos de Control ubicados en una Provincia
     * @param provincia
     * @return 
     */
    public List<PuestoControl> getByProvincia(String provincia){
        em = getEntityManager();
        String queryString = "SELECT puesto FROM PuestoControl puesto "
                + "WHERE puesto.provincia = :provincia "
                + "AND puesto.habilitado = true";
        Query q = em.createQuery(queryString)
                .setParameter("provincia", provincia);
        return q.getResultList();
    }    
}
