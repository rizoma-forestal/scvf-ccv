
package ar.gob.ambiente.sacvefor.controlverificacion.facades;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.Control;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Guia;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Parametrica;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.PuestoControl;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

/**
 * Acceso a datos para la entidad Control
 * @author rincostante
 */
@Stateless
public class ControlFacade extends AbstractFacade<Control> {

    @PersistenceContext(unitName = "sacvefor-controlVerificacionPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ControlFacade() {
        super(Control.class);
    }
    
    /**
     * Método que devuelve el Control realizado a una Guía por un Usuario.
     * Se asume que un mismo usuario (desde su puesto de control) 
     * no puede hacer más de un Control a una misma Guía
     * @param guia
     * @param usuario
     * @return 
     */
    public Control getExistente(Guia guia, Usuario usuario) {
        List<Control> lstControl;
        em = getEntityManager();
        String queryString = "SELECT control FROM Control control "
                + "WHERE control.guia = :guia "
                + "AND control.usuario = :usuario";
        Query q = em.createQuery(queryString)
                .setParameter("guia", guia)
                .setParameter("usuario", usuario);
        lstControl = q.getResultList();
        if(lstControl.isEmpty()){
            return null;
        }else{
            return lstControl.get(0);
        }
    }     
    
    @Override
    public List<Control> findAll(){
        em = getEntityManager();
        String queryString = "SELECT control FROM Control control "
                + "ORDER BY control.fechaHora";
        Query q = em.createQuery(queryString);
        return q.getResultList();
    }  
    
    /**
     * Método que devuelve los controles realizados a una Guía determinada
     * @param guia
     * @return 
     */
    public List<Control> getByGuia(Guia guia){
        em = getEntityManager();
        String queryString = "SELECT control FROM Control control "
                + "WHERE control.guia = :guia";
        Query q = em.createQuery(queryString)
                .setParameter("guia", guia);
        return q.getResultList();
    }    
    
    /**
     * Método que devuelve los controles realizados a una Guía según su código
     * para ser consumido desde la API
     * @param codGuia
     * @return 
     */
    public List<Control> getByCodGuia(String codGuia){
        em = getEntityManager();
        String queryString = "SELECT control FROM Control control "
                + "WHERE control.guia.codigo = :codGuia";
        Query q = em.createQuery(queryString)
                .setParameter("codGuia", codGuia);
        return q.getResultList();
    }        
    
    /**
     * Método que devuelve los controles realizados por un Usuario determinado
     * @param usuario
     * @return 
     */
    public List<Control> getByUsuario(Usuario usuario){
        em = getEntityManager();
        String queryString = "SELECT control FROM Control control "
                + "WHERE control.usuario = :usuario";
        Query q = em.createQuery(queryString)
                .setParameter("usuario", usuario);
        return q.getResultList();
    }      
    
    /**
     * Método que devuelve los controles realizados con un resultado determinado
     * @param resultado
     * @return 
     */
    public List<Control> getByResultado(Parametrica resultado){
        em = getEntityManager();
        String queryString = "SELECT control FROM Control control "
                + "WHERE control.resultado = :resultado";
        Query q = em.createQuery(queryString)
                .setParameter("resultado", resultado);
        return q.getResultList();
    }   
    
    /**
     * Método que devuelve los Controles realizados en una Localidad determinada
     * @param idLocGt
     * @return 
     */
    public List<Control> getByIdLocGt(Long idLocGt){
        em = getEntityManager();
        String queryString = "SELECT control FROM Control control "
                + "INNER JOIN Usuario us "
                + "INNER JOIN PuestoControl puesto "
                + "WHERE puesto.idLocGt = :idLocGt";
        Query q = em.createQuery(queryString)
                .setParameter("idLocGt", idLocGt);
        return q.getResultList();
    }       
    
    /**
     * Método para obtener todas las revisiones de la entidad
     * @param id : Id de la Guía cuyas revisiones se solicitan
     * @return 
     */
    public List<Control> findRevisions(Long id){  
        List<Control> lstControles = new ArrayList<>();
        Control control = this.find(id);
        if(control != null){
            AuditReader reader = AuditReaderFactory.get(getEntityManager());
            List<Number> revisions = reader.getRevisions(Control.class, id);
            for (Number n : revisions) {
                Control cli = reader.find(Control.class, id, n);
                cli.setFechaRevision(reader.getRevisionDate(n));
                Hibernate.initialize(cli.getGuia());
                Hibernate.initialize(cli.getResultado());
                Hibernate.initialize(cli.getUsuario());
                lstControles.add(cli);
            }
        }
        return lstControles;
    }          
}
