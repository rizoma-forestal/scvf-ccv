
package ar.gob.ambiente.sacvefor.controlverificacion.mb;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.Usuario;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.UsuarioFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.util.CriptPass;
import ar.gob.ambiente.sacvefor.controlverificacion.util.JsfUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.Session;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * Bean para la gestión de la sesión del usuario
 * @author rincostante 
 */
public class MbSesion implements Serializable{

    private Long dni;
    private String clave;
    private String claveEncript;
    private String newClave;
    private String newClave2;
    private Usuario usuario;
    private boolean iniciando;
    private boolean logeado = false;
    static final Logger LOG = Logger.getLogger(MbSesion.class);
    
    // campos para la notificación al Usuario
    @Resource(mappedName ="java:/mail/ambientePrueba")    
    private Session mailSesion;
    private Message mensaje;   

    @EJB
    private UsuarioFacade usuarioFacade;    
    
    public MbSesion() {
    }

    public boolean isIniciando() {
        return iniciando;
    }

    public void setIniciando(boolean iniciando) {
        this.iniciando = iniciando;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getClaveEncript() {
        return claveEncript;
    }

    public void setClaveEncript(String claveEncript) {
        this.claveEncript = claveEncript;
    }

    public String getNewClave() {
        return newClave;
    }

    public void setNewClave(String newClave) {
        this.newClave = newClave;
    }

    public String getNewClave2() {
        return newClave2;
    }

    public void setNewClave2(String newClave2) {
        this.newClave2 = newClave2;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Session getMailSesion() {
        return mailSesion;
    }

    public void setMailSesion(Session mailSesion) {
        this.mailSesion = mailSesion;
    }

    public Message getMensaje() {
        return mensaje;
    }

    public void setMensaje(Message mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isLogeado() {
        return logeado;
    }

    public void setLogeado(boolean logeado) {
        this.logeado = logeado;
    }
    
    /******************************
     * Métodos de inicialización **
     ******************************/ 
    
    @PostConstruct
    public void init(){
        iniciando = true;
    }       
    
    /***********************
     * Métodos operativos **
     ***********************/ 

    /**
     * Método para validar los datos del usuario
     */
    public void login(){
        ExternalContext contextoExterno = FacesContext.getCurrentInstance().getExternalContext();
        // encripto la contraseña recibida
        claveEncript = "";
        claveEncript = CriptPass.encriptar(clave);
        
        try{
            // valdo el usuario
            usuario = usuarioFacade.validar(dni, claveEncript);
            if(usuario != null){
                logeado = true;
                JsfUtil.addSuccessMessage("Bienvenid@ " + usuario.getNombreCompleto());
                
                // verifico si es la primera vez que inicia sesión
                String ctxPath = ((ServletContext) contextoExterno.getContext()).getContextPath();
                if(!usuario.isPrimeraVez()){
                    contextoExterno.redirect(ctxPath + "/faces/sesion/control.xhtml");
                }else{
                    contextoExterno.redirect(ctxPath + "/faces/sesion/primeraVez.xhtml");
                }
                Date fecha = new Date(System.currentTimeMillis());
                usuario.setFechaUltimoLogin(fecha);
                usuarioFacade.edit(usuario);
                iniciando = false;
            }else{
                logeado = false;
                JsfUtil.addErrorMessage("No se han validado los datos ingresados, alguno de los dos o ambos son incorrectos.");
            }
        }catch(IOException ex){
            LOG.fatal("Hubo un error validando las credenciales del Usuario." + ex.getMessage());
            JsfUtil.addErrorMessage("Hubo un error validando las credenciales del Usuario.");
        }
    }    
    
    /**
     * Método para limpiar el formulario de inicio de sesión
     */
    public void limpiarFormLogin(){
        dni = null;
        clave = null;
    }
    
    /**
     * Método para limpiar el formulario de cambio de contraseña
     */
    public void limpiarFormNewClave(){
        newClave2 = null;
        newClave = null;
    }
    
    /**
     * Método para redireccionar al formulario de cambio de clave
     */
    public void cambiarClave(){
        newClave2 = null;
        newClave = null;
        try{
            ExternalContext contextoExterno = FacesContext.getCurrentInstance().getExternalContext();
            String ctxPath = ((ServletContext) contextoExterno.getContext()).getContextPath();
            contextoExterno.redirect(ctxPath + "/faces/sesion/primeraVez.xhtml");
        }catch(IOException ex){
            LOG.fatal("Hubo un error redireccionando al cambio de clave." + ex.getMessage());
            JsfUtil.addErrorMessage("Hubo un error redireccionando al cambio de clave.");
        }
    }
    
    /**
     * Método que actualiza la clave del Usuario
     */
    public void actualizarClave(){
        ExternalContext contextoExterno = FacesContext.getCurrentInstance().getExternalContext();
        String ctxPath = ((ServletContext) contextoExterno.getContext()).getContextPath();
        // solo proceso si ambas claves son iguales
        if(newClave.equals(newClave2)){
            // seteo primeraVez false y actualizo la clave
            usuario.setPrimeraVez(false);
            claveEncript = CriptPass.encriptar(newClave);
            usuario.setClave(claveEncript);
            // actualico el usuario
            try{
                usuarioFacade.edit(usuario);
                contextoExterno.redirect(ctxPath + "/faces/sesion/control.xhtml");
                JsfUtil.addSuccessMessage("La clave se actualizó correctamente.");
            }catch(IOException ex){
                LOG.fatal("Hubo un error actualizando la clave." + ex.getMessage());
                JsfUtil.addErrorMessage("Hubo un error actualizando la clave.");
            }
        }else{
            JsfUtil.addErrorMessage("Las claves ingresadas no coinciden.");
        }
    }
    
    /**
     * Método para cerrar la sesión del usuario
     */
    public void logout(){
        ExternalContext contextoExterno = FacesContext.getCurrentInstance().getExternalContext();
        try {
            String ctxPath = ((ServletContext) contextoExterno.getContext()).getContextPath();
            contextoExterno.redirect(ctxPath);
            HttpSession session = (HttpSession) contextoExterno.getSession(false);
            session.invalidate();
        } catch (IOException ex) {
            JsfUtil.addErrorMessage("Hubo un error cerrando la sesión.");
            LOG.fatal("Hubo un error cerrando la sesión." + ex.getMessage());
        } 
    }
}
