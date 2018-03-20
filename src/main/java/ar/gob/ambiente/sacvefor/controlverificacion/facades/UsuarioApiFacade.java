
package ar.gob.ambiente.sacvefor.controlverificacion.facades;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.UsuarioApi;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase que implementa la abstracta para el acceso a datos de la entidad UsuarioApi.
 * @author rincostante
 */
@Stateless
public class UsuarioApiFacade extends AbstractFacade<UsuarioApi> {

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
    public UsuarioApiFacade() {
        super(UsuarioApi.class);
    }
    
    /**
     * Método para verificar la existencia de UsuarioApi referidos por algún Usuario creado por él.
     * @param usuarioApi UsuarioApi del cual se buscan las referencias
     * @return boolean Si no existen ninguna devuelve false, si no true
     */
    public boolean esReferenciado(UsuarioApi usuarioApi){
        em = getEntityManager();
        String queryString = "SELECT us FROM Usuario us "
                + "WHERE us.usuarioApi = :usuarioApi";
        Query q = em.createQuery(queryString)
                .setParameter("usuarioApi", usuarioApi);
        return q.getResultList().isEmpty();
    }
    
    /**
     * Método que obtiene el UsuarioApi según la identificación (en la API territorial) de la Provincia
     * de la que proviene el cliente.
     * @param idProvGt Long identificación de la Provincia cliente
     * @return UsuarioApi usuario API de la Provincia
     */
    public UsuarioApi getByIdProvGt(Long idProvGt){
        List<UsuarioApi> lstUsuarios;
        em = getEntityManager();
        String queryString = "SELECT usApi FROM UsuarioApi usApi "
                + "WHERE usApi.idProvGt = :idProvGt";
        Query q = em.createQuery(queryString)
                .setParameter("idProvGt", idProvGt);
        lstUsuarios = q.getResultList();
        if(lstUsuarios.isEmpty()){
            return null;
        }else{
            return lstUsuarios.get(0);
        }
    }
    
    /**
     * Método para obtener un UsuarioApi según su nombre, si existe
     * @param nombre String nombre del Usuario
     * @return UsuarioApi el UsuarioApi buscado o null, en caso de no existir
     */    
    public UsuarioApi getExistente(String nombre) {
        List<UsuarioApi> lstUsuarios;
        em = getEntityManager();
        
        String queryString = "SELECT usApi FROM UsuarioApi usApi "
                + "WHERE usApi.nombre = :nombre";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        lstUsuarios = q.getResultList();
        if(lstUsuarios.isEmpty()){
            return null;
        }else{
            return lstUsuarios.get(0);
        }
    }
    
    /**
     * Método que valida que el usuarioApi recibido está registrado como usuario de la API
     * @param nombre String Nombre del usuarioApi recibido, enviado por le cliente.
     * @return boolean Verdadero o falso según el caso
     */
    public boolean validarUsuarioApi(String nombre){
        em = getEntityManager();
        String queryString = "SELECT usApi FROM UsuarioApi usApi "
                + "WHERE usApi.nombre = :nombre "
                + "AND (usApi.rol.nombre = 'CLIENTE_CGL' OR usApi.rol.nombre = 'CLIENTE_TRAZ')";
        Query q = em.createQuery(queryString)
                .setParameter("nombre", nombre);
        return !q.getResultList().isEmpty();
    }     
}
