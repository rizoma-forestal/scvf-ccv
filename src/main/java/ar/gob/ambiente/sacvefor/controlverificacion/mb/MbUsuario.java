
package ar.gob.ambiente.sacvefor.controlverificacion.mb;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.Parametrica;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.PuestoControl;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.TipoParam;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Usuario;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.ParametricaFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.PuestoControlFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.TipoParamFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.UsuarioFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.util.CriptPass;
import ar.gob.ambiente.sacvefor.controlverificacion.util.EntidadCombo;
import ar.gob.ambiente.sacvefor.controlverificacion.util.JsfUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;

/**
 * Bean de respaldo para la gestión de Usuarios de la aplicación
 * @author rincostante
 */
public class MbUsuario {

    private Usuario usuario;
    private List<Usuario> lstUsuarios;
    private List<EntidadCombo> lstPuestos;
    private List<EntidadCombo> lstRoles;
    private int idPuesto;
    private int idRol;
    private boolean view;
    private boolean edit;
    static final Logger LOG = Logger.getLogger(MbUsuario.class);
    private String clave;
    
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private PuestoControlFacade puestoFacade;
    @EJB
    private ParametricaFacade paramFacade; 
    @EJB
    private TipoParamFacade tipoParamFacade;
    
    // campos y recursos para el envío de correos al usuario
    @Resource(mappedName ="java:/mail/ambientePrueba")    
    private Session mailSesion;
    private Message mensaje; 
    
    public MbUsuario() {
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public List<EntidadCombo> getLstRoles() {
        cargarRoles();
        return lstRoles;
    }

    public void setLstRoles(List<EntidadCombo> lstRoles) {
        this.lstRoles = lstRoles;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getLstUsuarios() {
        lstUsuarios = usuarioFacade.findAll();
        return lstUsuarios;
    }

    public void setLstUsuarios(List<Usuario> lstUsuarios) {
        this.lstUsuarios = lstUsuarios;
    }

    public List<EntidadCombo> getLstPuestos() {
        cargarPuestos();
        return lstPuestos;
    }

    public void setLstPuestos(List<EntidadCombo> lstPuestos) {
        this.lstPuestos = lstPuestos;
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
    
    /******************************
     * Mátodos de inicialización **
     ******************************/
    @PostConstruct
    public void init(){
        usuario = new Usuario();
    }   
    
    /***********************
     * Mátodos operativos **
     ***********************/

    /**
     * Método para guardar el Usuario, sea inserción o edición.
     * Previa validación
     */
    public void save(){
        boolean valida = true;
        String msgValid = validarDatos(), msgUs;
        if(msgValid.equals("")){
            try{
                // verifico que no exista otro usuario con el mismo DNI
                Usuario us = usuarioFacade.getExistente(usuario.getLogin());
                if(us != null){
                    if(usuario.getId() != null){
                        // si edita, no habilito si no es el mismo
                        if(!usuario.equals(us)) valida = false;
                    }else{
                        // si no edita no habilito de ninguna manera
                        valida = false;
                    }
                }
                if(valida){
                    String claveEncript = "";
                    // seteo las fechas
                    Date fecha = new Date(System.currentTimeMillis());
                    usuario.setFechaUltimoLogin(fecha);
                    // persisto
                    if(usuario.getId() != null){
                        usuarioFacade.edit(usuario);
                        JsfUtil.addSuccessMessage("El Usuario se guardó correctamente.");
                    }else{
                        // genero la clave y la seteo
                        clave = CriptPass.generar();
                        claveEncript = CriptPass.encriptar(clave);
                        usuario.setClave(claveEncript);
                        usuario.setHabilitado(true);
                        usuarioFacade.create(usuario);
                        msgUs = "El Usuario se registró correctamente.";
                        // mando correo
                        if(enviarCorreo()){
                            JsfUtil.addSuccessMessage(msgUs + " Se envió un correo con las credenciales de acceso.");
                        }else{
                            JsfUtil.addSuccessMessage(msgUs + " Pero no se pudo enviar un correo con las credenciales de acceso.");
                            LOG.fatal("Hubo un error enviando el correo al usuario con las credenciales de acceso.");
                        }
                    }
                    edit = false;
                }else{
                    JsfUtil.addErrorMessage("Ya existe un Usuario con el mismo DNI registrado previamento.");
                }
            }catch(Exception ex){
                JsfUtil.addErrorMessage("Hubo un error guardando el Usuario.");
                LOG.fatal("Hubo un error guardando el Usuario: " + ex.getMessage());
            }
        }else{
            JsfUtil.addErrorMessage(msgValid);
        }
    }    
    
    /**
     * Método para deshabilitar un Usuario
     * No estará disponible en los listados correspondientes
     */
    public void deshabilitar(){
        view = false;
        edit = false;
        try{
            usuario.setHabilitado(false);
            usuarioFacade.edit(usuario);
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error al deshabilitar el Usuario.");
            LOG.fatal("Hubo un error al deshabilitar el Usuario: " + ex.getMessage());
        }
    }      
    
    /**
     * Método para deshabilitar un Usuario
     * No estará disponible en los listados correspondientes
     */
    public void habilitar(){
        view = false;
        edit = false;
        try{
            usuario.setHabilitado(true);
            usuarioFacade.edit(usuario);
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error al deshabilitar el Usuario.");
            LOG.fatal("Hubo un error al deshabilitar el Usuario: " + ex.getMessage());
        }
    }         
    
    /**
     * Método para habilitar el formulario de creación
     */
    public void prepareNew(){
        usuario = new Usuario();
        view = false;
        edit = true;
    }   
    
    /**
     * Método para habilitar el formulario de edición
     * para cualquier caso
     */
    public void prepareEdit(){
        if(usuario.getId() != null){
            // si estoy editando un Usuario seteo el idPuesto y el idRol
            idPuesto = usuario.getPuestoControl().getId().intValue();
            idRol = usuario.getRol().getId().intValue();
        }
        view = false;
        edit = true;   
    } 
    
    /**
     * Método para habilitar el formulario de vista detalle 
     */
    public void prepareView(){
        view = true;
        edit = false;  
    }         
    
    /**
     * Método para limpiar el objeto ComponenteLocal del formulario correspondiente
     */
    public void limpiarForm(){
        usuario = new Usuario();
        idPuesto = 0;
        idRol = 0;
    }       

    /*********************
     * Métodos privados **
     *********************/

    /**
     * Método para cargar el listado de Puestos de control como EntidadCombo 
     */    
    private void cargarPuestos() {
        List<PuestoControl> lstPuestosControl;
        // seteo los resultados
        try{
            lstPuestosControl = puestoFacade.getHabilitados();
            this.lstPuestos = new ArrayList<>();
            for(PuestoControl puesto : lstPuestosControl){
                EntidadCombo entSrv = new EntidadCombo(); 
                entSrv.setId(puesto.getId().intValue());
                entSrv.setNombre(puesto.getNombre());
                this.lstPuestos.add(entSrv);
            }
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error obteniendo los Puestos de Control.");
            LOG.fatal("Hubo un error obteniendo los Puestos de Control. " + ex.getMessage());
        }
    }

    /**
     * Método para validar los datos del Usuario
     * @return 
     */
    private String validarDatos() {
        String mensaje = "";
        if(Objects.equals(usuario.getLogin(), Long.valueOf(0))){
            mensaje = "Debe ingresar el DNI del Usuario.";
        }
        if(!String.valueOf(usuario.getLogin()).matches("[0-9]*")){
            if(mensaje.equals("")){
                mensaje = "El DNI solo debe contener números, sin '-' ni '.'";
            }else{
                mensaje = mensaje + " " + "El DNI solo debe contener números, sin '-' ni '.'";
            }
        }
        if(usuario.getNombreCompleto() == null){
            if(mensaje.equals("")){
                mensaje = "Debe ingresar el Nombre completo del Usuario.";
            }else{
                mensaje = mensaje + " " + "Debe ingresar Nombre completo del Usuario.";
            }
        }else{
            String nombre = usuario.getNombreCompleto().toUpperCase();
            usuario.setNombreCompleto(nombre);
        }
        if(idPuesto == 0 && usuario.getRol().getNombre().equals(ResourceBundle.getBundle("/Config").getString("Operativo"))){
            if(mensaje.equals("")){
                mensaje = "Debe seleccionar el Puesto de Control del Usuario.";
            }else{
                mensaje = mensaje + " " + "Debe selecciónar el Puesto de Control del Usuario.";
            }
        }else if(idPuesto > 0){
            // seteo el puesto
            usuario.setPuestoControl(puestoFacade.find(Long.valueOf(idPuesto)));
        }
        if(idRol == 0){
            if(mensaje.equals("")){
                mensaje = "Debe seleccionar el Rol del Usuario.";
            }else{
                mensaje = mensaje + " " + "Debe seleccionar el Rol del Usuario.";
            }
        }else{
            // seteo el rol
            usuario.setRol(paramFacade.find(Long.valueOf(idRol)));
        }
            
        return mensaje;
    }

    /**
     * Método para cargar el listado de Roles de Usuario como EntidadCombo 
     */
    private void cargarRoles() {
        TipoParam tipo = tipoParamFacade.getExistente(ResourceBundle.getBundle("/Config").getString("RolUsuarios"));
        List<Parametrica> lstParamRoles;
        // seteo los resultados
        try{
            lstParamRoles = paramFacade.getHabilitadas(tipo);
            this.lstRoles = new ArrayList<>();
            for(Parametrica paramRol : lstParamRoles){
                EntidadCombo entSrv = new EntidadCombo(); 
                entSrv.setId(paramRol.getId().intValue());
                entSrv.setNombre(paramRol.getNombre());
                this.lstRoles.add(entSrv);
            }
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error obteniendo los Roles de Usuario.");
            LOG.fatal("Hubo un error obteniendo los Roles de Usuario. " + ex.getMessage());
        }
    }

    /**
     * Método para enviar un correo al Usuario creado con las correspondientes credenciales de acceso.
     * @return 
     */
    private boolean enviarCorreo() {
        boolean result;
        String bodyMessage;
        mensaje = new MimeMessage(mailSesion);
        bodyMessage = "<p>Estimado/a</p> "
                + "<p>Se ha creado una cuenta a su nombre: " + usuario.getNombreCompleto() + ".</p>"
                + "<p>Deberá iniciar sesión en en el siguiente link: " + ResourceBundle.getBundle("/Config").getString("Server") + ResourceBundle.getBundle("/Bundle").getString("RutaAplicacion") + " </p>"
                + "<p>Las credenciales de acceso son las siguientes:</p> "
                + "<p><strong>usuario:</strong> " + usuario.getLogin() + "<br/> "
                + "<strong>contraseña:</strong> " + clave + "</p> "
                + "<p>Una vez iniciada la sesión podrá cambiar la clave generada automáticamente por una de su elección.</p>"
                + "<p>Por favor, no responda este correo. No divulgue ni comparta las credenciales de acceso.</p> "
                + "<p>Saludos cordiales</p> "
                + "<p>" + ResourceBundle.getBundle("/Config").getString("EntidadBosques") + "<br/> "
                + ResourceBundle.getBundle("/Config").getString("EntidadBosqesSup_1") + "<br/> "
                + ResourceBundle.getBundle("/Config").getString("EntidadBosqesSup_2") + "<br/> "
                + ResourceBundle.getBundle("/Config").getString("EntidadBosqesSup_3") + "<br/> "
                + ResourceBundle.getBundle("/Config").getString("EntidadBosqesSup_4") + "<br/> "
                + "Presidencia de la Nación<br/> "
                + ResourceBundle.getBundle("/Config").getString("DomicilioBosques") + "<br/> "
                + ResourceBundle.getBundle("/Config").getString("TelBosques") + "<br /> "
                + "Correo electrónico: <a href=\"mailto:" + ResourceBundle.getBundle("/Config").getString("emailBosques") + "\">" + ResourceBundle.getBundle("/Config").getString("emailBosques") + "</a></p>";     
        
        try{
            mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(usuario.getCorreoElectronico()));
            mensaje.setSubject("SACVeFor - Control y Verificación - Credenciales de acceso" );
            mensaje.setContent(bodyMessage, "text/html; charset=utf-8");
            
            Date timeStamp = new Date();
            mensaje.setSentDate(timeStamp);
            
            Transport.send(mensaje);
            
            result = true;
            
        }catch(MessagingException ex){
            result = false;
            LOG.fatal("Hubo un error enviando el correo de registro de usuario. " + ex.getMessage());
        }
        
        return result;
    }
}
