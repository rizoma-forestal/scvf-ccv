
package ar.gob.ambiente.sacvefor.controlverificacion.mb;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.Guia;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.GuiaFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.util.JsfUtil;
import java.io.Serializable;
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

    private Guia guia;
    private String matricula;
    static final Logger LOG = Logger.getLogger(MbPublico.class);
    public boolean consultado;
    
    @EJB
    private GuiaFacade guiaFacade;
    
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
        try{
            guia = guiaFacade.getVigByMatricula(matricula.toUpperCase());
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
