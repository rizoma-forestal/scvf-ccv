
package ar.gob.ambiente.sacvefor.controlverificacion.mb;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.Guia;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.GuiaFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.util.JsfUtil;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * Bean para la gestión de los controles públicos
 * @author rincostante
 */
public class MbPublico implements Serializable{

    /**
     * Variable privada: guía para setear con el resultado de la búsqueda por matrícula del vehículo de transporte
     */
    private Guia guia;
    
    /**
     * Variable privada: matrícula del vehículo de transporte de la guía a buscar
     */
    private String matricula;
    
    /**
     * Variable privada: Logger para escribir en el log del server
     */ 
    static final Logger LOG = Logger.getLogger(MbPublico.class);
    
    /**
     * Variable privada: flag que indica si se está consultando una guía
     */
    public boolean consultado;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de guía
     */
    @EJB
    private GuiaFacade guiaFacade;
    
    /**
     * Constructor
     */
    public MbPublico() {
    }

    public boolean isConsultado() {
        return consultado;
    }

    public void setConsultado(boolean consultado) {
        this.consultado = consultado;
    }

    public Guia getGuia() {
        return guia;
    }

    public void setGuia(Guia guia) {
        this.guia = guia;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    /**
     * Método que inicializa el bean y remueve el bean de sesión
     */        
    @PostConstruct
    public void init(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.removeAttribute("mbSesion");
    }        

    
    /***********************
     * Métodos operativos **
     ***********************/
    
    /**
     * Método para consultar la situación de una Guía a partir de la matrícula del vehículo de transporte
     */
    public void consultarGuia(){
        Date hoy = new Date(System.currentTimeMillis());
        int i = 1;
        try{
            Guia g = guiaFacade.getVigByMatricula(matricula.toUpperCase());
            if(g != null){
                i = hoy.compareTo(g.getFechaVencimiento());
            }
            if(i <= 0){
                guia = g;
            }
            consultado = true;
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error consultando la Guía." + ex.getMessage());
        } 
    }
    
    /**
     * Método para limpiar los resultados de la búsqueda por Matrícula
     */
    public void limpiarResultMat(){
        guia = null;
        matricula = null;
    }
}
