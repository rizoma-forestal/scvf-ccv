
package ar.gob.ambiente.sacvefor.controlverificacion.facades;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Acceso a datos para la entidad Usuario
 * @author rincostante
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

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
    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    /**
     * Método para validar la existencia de un Usuario según su DNI, utilizado como login
     * @param login Long DNI del Usuario
     * @return Usuario usuario correspondiente
     */
    public Usuario getExistente(Long login) {
        List<Usuario> lstUsuarios;
        em = getEntityManager();
        
        String queryString = "SELECT us FROM Usuario us "
                + "WHERE us.login = :login";
        Query q = em.createQuery(queryString)
                .setParameter("login", login);
        lstUsuarios = q.getResultList();
        if(lstUsuarios.isEmpty()){
            return null;
        }else{
            return lstUsuarios.get(0);
        }
    }    

    /**
     * Mátodo que solo devuelve los Usuarios habilitados.
     * Para poblar combos de selección.
     * @return List<Usuario> listado de los usuario habilitados
     */
    public List<Usuario> getHabilitados(){
        em = getEntityManager();
        String queryString = "SELECT us FROM Usuario us "
                + "WHERE us.habilitado = true";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }  
    
    /**
     * Mátodo que solo devuelve los Usuarios habilitados.
     * Para poblar combos de selección.
     * @return List<Usuario> listado de los usuarios ordenados por nombre completo
     */
    @Override
    public List<Usuario> findAll(){
        em = getEntityManager();
        String queryString = "SELECT us FROM Usuario us "
                + "ORDER BY us.nombreCompleto";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }      

    /**
     * Método para validar la existencia de un Usuario con las credenicales recibidas
     * @param login Long DNI del Usuario dni del usuario a validar
     * @param clave String clave encriptada clave del usuario a validar
     * @return Usuario usuario validado
     */
    public Usuario validar(Long login, String clave){
        List<Usuario> lUs;
        em = getEntityManager();
        String queryString = "SELECT us FROM Usuario us "
                + "WHERE us.login = :login "
                + "AND us.clave = :clave";
        Query q = em.createQuery(queryString)
                .setParameter("login", login)
                .setParameter("clave", clave);
        lUs = q.getResultList();
        if(lUs.isEmpty()){
            return null;
        }else{
            return lUs.get(0);
        }
    }      
}
