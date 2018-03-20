
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
    public PuestoControlFacade() {
        super(PuestoControl.class);
    }
    
    /**
     * Método para validar la existencia de un Puesto de control según su nombre y idLocalidad
     * @param nombre String nombre del Puesto de control buscado
     * @param idLocGt Long id de la Localidad en la API Territorial
     * @return PuestoControl Puesto de control correspondiente
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
     * @return List<PuestoControl> listado de los puestos de control ordenados por nombre
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
     * @return List<PuestoControl> listado de los puestos de control habilitados
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
     * @param localidad String nombre de la localidad de la cual se solicitan sus puestos
     * @return List<PuestoControl> listado de los puestos correspondientes
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
     * @param departamento String nombre del departamento del cual se solicitan los puestos de control
     * @return List<PuestoControl> listado de los puestos correspondientes
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
     * @param provincia String nombre de la provincia de la cual se solicitan sus puestos de control
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
