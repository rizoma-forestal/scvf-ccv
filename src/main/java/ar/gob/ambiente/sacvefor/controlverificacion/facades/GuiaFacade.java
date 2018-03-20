
package ar.gob.ambiente.sacvefor.controlverificacion.facades;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.Guia;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Acceso a datos para la entidad Guía
 * @author rincostante
 */
@Stateless
public class GuiaFacade extends AbstractFacade<Guia> {

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
    public GuiaFacade() {
        super(Guia.class);
    }
    
    /**
     * Metodo para validar una Guía existente según el codigo
     * @param codigo String identificador único de la guía
     * @return Guia guía correspondiente al código
     */
    public Guia getExistente(String codigo) {
        List<Guia> lstGuias;
        em = getEntityManager();
        
        String queryString = "SELECT guia FROM Guia guia "
                + "WHERE guia.codigo = :codigo";
        Query q = em.createQuery(queryString)
                .setParameter("codigo", codigo);
        lstGuias = q.getResultList();
        if(lstGuias.isEmpty()){
            return null;
        }else{
            return lstGuias.get(0);
        }
    } 

    /**
     * Método para obetener las Guías cuyo remitente tiene el CUIT recibido
     * @param cuitOrigen Long cuit del titular que remite la guía
     * @return List<Guia> listado de las guías remitidas por el orgen remitido
     */
    public List<Guia> getByOrigen(Long cuitOrigen){
        em = getEntityManager();
        String queryString = "SELECT guia FROM Guia guia "
                + "WHERE guia.cuitOrigen = :cuitOrigen";
        Query q = em.createQuery(queryString)
                .setParameter("cuitOrigen", cuitOrigen);
        return q.getResultList();
    }   
    
    /**
     * Método para obetener las Guías vigentes cuyo remitente tiene el CUIT recibido
     * @param cuitOrigen Long cuit del titular que remite la guía
     * @return List<Guia> listado de las guías vigentes remitidas por el orgen remitido
     */
    public List<Guia> getVigByOrigen(Long cuitOrigen){
        String vigente = ResourceBundle.getBundle("/Config").getString("Vigente");
        String validada = ResourceBundle.getBundle("/Config").getString("Validada");
        em = getEntityManager();
        String queryString = "SELECT guia FROM Guia guia "
                + "WHERE guia.cuitOrigen = :cuitOrigen AND "
                + "(guia.estado.nombre = :vigente "
                + "OR guia.estado.nombre = :validada)";
        Query q = em.createQuery(queryString)
                .setParameter("cuitOrigen", cuitOrigen)
                .setParameter("vigente", vigente)
                .setParameter("validada", validada);
        return q.getResultList();
    }    
    
    /**
     * Método para obetener las Guías cuyo destino tiene el CUIT recibido
     * @param cuitDestino Long cuit del destinatario al cual se le envía la guía
     * @return List<Guia> listado de las guías enviadas al destinatario
     */
    public List<Guia> getByDestino(Long cuitDestino){
        em = getEntityManager();
        String queryString = "SELECT guia FROM Guia guia "
                + "WHERE guia.cuitDestino = :cuitDestino";
        Query q = em.createQuery(queryString)
                .setParameter("cuitDestino", cuitDestino);
        return q.getResultList();
    }   
    
    /**
     * Método para obetener las Guías vigentes cuyo destino tiene el CUIT recibido
     * @param cuitDestino Long cuit del destinatario al cual se le envía la guía
     * @return List<Guia> listado de las guías vigentes enviadas al destinatario
     */
    public List<Guia> getVigByDestino(Long cuitDestino){
        String vigente = ResourceBundle.getBundle("/Config").getString("Vigente");
        String validada = ResourceBundle.getBundle("/Config").getString("Validada");
        em = getEntityManager();
        String queryString = "SELECT guia FROM Guia guia "
                + "WHERE guia.cuitDestino = :cuitDestino AND "
                + "(guia.estado.nombre = :vigente "
                + "OR guia.estado.nombre = :validada)";
        Query q = em.createQuery(queryString)
                .setParameter("cuitDestino", cuitDestino)
                .setParameter("vigente", vigente)
                .setParameter("validada", validada);
        return q.getResultList();
    }      

    /**
     * Método que devuelve las Guías vinculadas a un vehículo determinado
     * identificado por su matrícula
     * @param matVehiculo String matrícula del Vehículo cuyas Guías se quiere obtener.
     * @return List<Guia> listado de las guías vinculadas al vehículo
     */
    public List<Guia> getByVehiculo(String matVehiculo){
        em = getEntityManager();
        String queryString = "SELECT guia FROM Guia guia "
                + "WHERE guia.matVehiculo = :matVehiculo";
        Query q = em.createQuery(queryString)
                .setParameter("matVehiculo", matVehiculo);
        return q.getResultList();
    }     
    
    /**
     * Devuelve la Guía vigente cuyo vehículo de transporte tiene la matrícula solicitada.
     * Se asume que no debería haber dos Guías vigentes vinculados a un mismo vehículo
     * @param matVehiculo String matrícula del Vehículo cuyas Guías vigentes se quiere obtener.
     * @return listado de las guías vigentes vinculadas al vehículo
     */
    public Guia getVigByMatricula(String matVehiculo){
        String vigente = ResourceBundle.getBundle("/Config").getString("Vigente");
        String validada = ResourceBundle.getBundle("/Config").getString("Validada");
        em = getEntityManager();
        List<Guia> lstGuias;
        String queryString = "SELECT guia FROM Guia guia "
                + "WHERE guia.matVehiculo = :matVehiculo AND "
                + "(guia.estado.nombre = :vigente "
                + "OR guia.estado.nombre = :validada) "
                + "ORDER BY guia.id";
        Query q = em.createQuery(queryString)
                .setParameter("matVehiculo", matVehiculo)
                .setParameter("vigente", vigente)
                .setParameter("validada", validada);
        lstGuias = q.getResultList();
        if(lstGuias.isEmpty()){
            return null;
        }else{
            return lstGuias.get(lstGuias.size() - 1);
        }
    }
    
    /**
     * Método que devuelve una Guía vigente según su código
     * @param codigo String identificador único de la guía
     * @return Guia guía vigente cuyo código se remitió
     */
    public Guia getVigByCodigo(String codigo){
        String vigente = ResourceBundle.getBundle("/Config").getString("Vigente");
        String validada = ResourceBundle.getBundle("/Config").getString("Validada");
        em = getEntityManager();
        List<Guia> lstGuias;
        String queryString = "SELECT guia FROM Guia guia "
                + "WHERE guia.codigo = :codigo AND "
                + "(guia.estado.nombre = :vigente "
                + "OR guia.estado.nombre = :validada)";
        Query q = em.createQuery(queryString)
                .setParameter("codigo", codigo)
                .setParameter("vigente", vigente)
                .setParameter("validada", validada);
        lstGuias = q.getResultList();
        if(lstGuias.isEmpty()){
            return null;
        }else{
            return lstGuias.get(0);
        }
    }
    
    /**
     * Método que devuelve las Guías que contengan items con el código de producto recibido
     * @param codigoOrigen String código de origen de los productos cuyos items conforman las guías solicitadas
     * @return List<Guia> listado de las guías correspondientes
     */
    public List<Guia> getByCodOrigen(String codigoOrigen){
        em = getEntityManager();
        String queryString = "SELECT guia FROM Guia guia "
                + "INNER JOIN Item item"
                + "WHERE item.codigoOrigen = :codigoOrigen";
        Query q = em.createQuery(queryString)
                .setParameter("codigoOrigen", codigoOrigen);
        return q.getResultList();
    }
    
    /**
     * Método que devuelve la Guías que contengan items con productos con el nombre científico recibido
     * @param nombreCientifico String nombre científico de los productos incluídos en las guías solicitadas
     * @return 
     */
    public List<Guia> getByNomCientifico(String nombreCientifico){
        em = getEntityManager();
        String queryString = "SELECT guia FROM Guia guia "
                + "INNER JOIN Item item"
                + "WHERE item.nombreCientifico = :nombreCientifico";
        Query q = em.createQuery(queryString)
                .setParameter("nombreCientifico", nombreCientifico);
        return q.getResultList();
    }    
    
    /**
     * Método que devuelve la Guías que contengan items con productos con el nombre vulgar recibido
     * @param nombreVulgar
     * @return List<Guia> listado de las guías correspondientes
     */
    public List<Guia> getByNomVulgar(String nombreVulgar){
        em = getEntityManager();
        String queryString = "SELECT guia FROM Guia guia "
                + "INNER JOIN Item item"
                + "WHERE item.nombreVulgar = :nombreVulgar";
        Query q = em.createQuery(queryString)
                .setParameter("nombreVulgar", nombreVulgar);
        return q.getResultList();
    }     
    
    /**
     * Método que devuelve las Guías que contengan items cuyo código de producto contenga el elemento recibido
     * @param elemOrigen String cadena a contener en los códigos de origen de los items incluídos en las guías buscadas
     * @return List<Guia> listado de las guías correspondientes 
     */
    public List<Guia> getByElementoOrigen(String elemOrigen){
        em = getEntityManager();
        String queryString = "SELECT guia FROM Guia guia "
                + "INNER JOIN Item item"
                + "WHERE item.codigoOrigen LIKE :codigoOrigen";
        Query q = em.createQuery(queryString)
                .setParameter("codigoOrigen", "'%" + elemOrigen + "%'");
        return q.getResultList();
    }
    
    /**
     * Método que devuleve las Guías locales registradas según la Provincia que las emitió
     * @param provincia String Provincia emisora de la Guía
     * @return List<Guia> listado de las guías correspondientes
     */
    public List<Guia> getByProvincia(String provincia){
        em = getEntityManager();
        String queryString = "SELECT guia FROM Guia guia "
                + "INNER JOIN ComponenteLocal comp"
                + "WHERE comp.provincia = :provincia";
        Query q = em.createQuery(queryString)
                .setParameter("provincia", provincia);
        return q.getResultList();
    }    
}
