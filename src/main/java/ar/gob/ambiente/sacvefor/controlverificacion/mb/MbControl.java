
package ar.gob.ambiente.sacvefor.controlverificacion.mb;

import ar.gob.ambiente.sacvefor.controlverificacion.cgl.client.EstadoGuiaLocalClient;
import ar.gob.ambiente.sacvefor.controlverificacion.cgl.client.GuiaLocalClient;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Control;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Guia;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Parametrica;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.TipoParam;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Usuario;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.ControlFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.GuiaFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.ParametricaFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.TipoParamFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.util.EntidadCombo;
import ar.gob.ambiente.sacvefor.controlverificacion.util.JsfUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Bean de gestión de las consultas para el Control y la Verificación de Guías por Usuarios registrados
 * @author rincostante
 */
public class MbControl {

    private EntidadCombo parmSelected;
    private List<EntidadCombo> lstResultados;
    private List<Control> lstControles;
    private String matricula;
    private String codigo;
    private Long cuitRem;
    private Long cuitDest;
    private Guia guia;
    private List<Guia> lstGuias;
    private boolean consultado;
    private boolean verPorMatricula;
    private boolean verPorCodigo;
    private boolean verPorCuitRem;
    private boolean verPorCuitDest;    
    private boolean verControles;
    private boolean verTodos;
    private Control control;
    private MbSesion sesion;
    private Usuario usLogueado;
    private int result;
    static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MbControl.class);
    private String obs;
    
    // campos y recursos para el envío de correos al usuario
    @Resource(mappedName ="java:/mail/ambientePrueba")    
    private Session mailSesion;
    private Message mensaje;     
    
    // campos para la API CGL
    private GuiaLocalClient guiaLocalClient;
    private EstadoGuiaLocalClient estadoGuiaLocClient;

    @EJB
    private ParametricaFacade paramFacade;
    @EJB
    private TipoParamFacade tipoParamFacade;
    @EJB
    private GuiaFacade guiaFacade;
    @EJB
    private ControlFacade controlFacade;
    
    public MbControl() {
    }

    public boolean isVerTodos() {
        return verTodos;
    }

    public void setVerTodos(boolean verTodos) {
        this.verTodos = verTodos;
    }

    public int getResult() {
        return result;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public List<Control> getLstControles() {
        return lstControles;
    }

    public void setLstControles(List<Control> lstControles) {
        this.lstControles = lstControles;
    }

    public boolean isVerControles() {
        return verControles;
    }

    public void setVerControles(boolean verControles) {
        this.verControles = verControles;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<EntidadCombo> getLstResultados() {
        return lstResultados;
    }

    public void setLstResultados(List<EntidadCombo> lstResultados) {
        this.lstResultados = lstResultados;
    }

    public Control getControl() {
        // chequeo la existencia de un control existente
        control = controlFacade.getExistente(guia, usLogueado);
        if(control == null){
            control = new Control();
        }
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public List<Guia> getLstGuias() {
        return lstGuias;
    }

    public void setLstGuias(List<Guia> lstGuias) {
        this.lstGuias = lstGuias;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getCuitRem() {
        return cuitRem;
    }

    public void setCuitRem(Long cuitRem) {
        this.cuitRem = cuitRem;
    }

    public Long getCuitDest() {
        return cuitDest;
    }

    public void setCuitDest(Long cuitDest) {
        this.cuitDest = cuitDest;
    }

    public boolean isVerPorMatricula() {
        return verPorMatricula;
    }

    public void setVerPorMatricula(boolean verPorMatricula) {
        this.verPorMatricula = verPorMatricula;
    }

    public boolean isVerPorCodigo() {
        return verPorCodigo;
    }

    public void setVerPorCodigo(boolean verPorCodigo) {
        this.verPorCodigo = verPorCodigo;
    }

    public boolean isVerPorCuitRem() {
        return verPorCuitRem;
    }

    public void setVerPorCuitRem(boolean verPorCuitRem) {
        this.verPorCuitRem = verPorCuitRem;
    }

    public boolean isVerPorCuitDest() {
        return verPorCuitDest;
    }

    public void setVerPorCuitDest(boolean verPorCuitDest) {
        this.verPorCuitDest = verPorCuitDest;
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

    public EntidadCombo getParmSelected() {
        return parmSelected;
    }

    public void setParmSelected(EntidadCombo parmSelected) {
        this.parmSelected = parmSelected;
    }
    
    
    
    /******************************
     * Métodos de inicialización **
     ******************************/
 
    @PostConstruct
    public void init(){
        List<Parametrica> lstParam;
        // seteo los resultados
        TipoParam tipo = tipoParamFacade.getExistente(ResourceBundle.getBundle("/Config").getString("EstadosGuia"));
        lstParam = paramFacade.getHabilitadas(tipo);
        lstResultados = new ArrayList<>();
        for(Parametrica param : lstParam){
            EntidadCombo entSrv = new EntidadCombo(); 
            entSrv.setId(param.getId().intValue());
            entSrv.setNombre(param.getNombre());
            lstResultados.add(entSrv);
        }
        parmSelected = new EntidadCombo();
        // obtento el usuario
        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        sesion = (MbSesion)ctx.getSessionMap().get("mbSesion");
        usLogueado = sesion.getUsuario();
    }  
    
    /**
     * Método para limpiar los resultados de las búsquedas
     */
    public void limpiarResult(){
        lstGuias = null;
        guia = null;
        matricula = null;
        codigo = null;
        cuitRem = null;
        cuitDest = null;
        consultado = false;
        verTodos = false;
    }
    
    /**
     * Método para preparar la consulta por Matrícula
     */
    public void prepareFrmMat(){
        limpiarResult();
        verPorMatricula = true;
        verPorCodigo = false;
        verPorCuitRem = false;
        verPorCuitDest = false;
        verControles = false;
    }
    
    /**
     * Método para preparar la consulta por Código de Guía
     */
    public void prepareFrmCodigo(){
        limpiarResult();
        verPorMatricula = false;
        verPorCodigo = true;
        verPorCuitRem = false;
        verPorCuitDest = false;
        verControles = false;
    }
    
    /**
     * Método para preparar la consulta por Cuit del Remitente
     */
    public void prepareFrmCuitRem(){
        limpiarResult();
        verPorMatricula = false;
        verPorCodigo = false;
        verPorCuitRem = true;
        verPorCuitDest = false;
        verControles = false;
    }    
    
    /**
     * Método para preparar la consulta por Cuit del Destinatario
     */
    public void prepareFrmCuitDest(){
        limpiarResult();
        verPorMatricula = false;
        verPorCodigo = false;
        verPorCuitRem = false;
        verPorCuitDest = true;
        verControles = false;
    }       
    
    /**
     * Método para preparar el listado de todos los controles realizados por el usuario
     */
    public void prepareFrmMisControles(){
        limpiarResult();
        verPorMatricula = false;
        verPorCodigo = false;
        verPorCuitRem = false;
        verPorCuitDest = false;
        verControles = true;
        lstControles = controlFacade.getByUsuario(usLogueado);
    }   
    
    /**
     * Método para mostrar todos los controles registrados
     */
    public void prepareFrmControlesTotales(){
        limpiarResult();
        verPorMatricula = false;
        verPorCodigo = false;
        verPorCuitRem = false;
        verPorCuitDest = false;
        verControles = true;
        lstControles = controlFacade.findAll();
        verTodos = true;
    }
    
    /***********************
     * Métodos operativos **
     ***********************/
   
    /**
     * Método para consultar la situación de una Guía a partir de la matrícula del vehículo de transporte
     */
    public void consultarGuiaMat(){
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
     * Método para consutar la situación de una Guía a partir de su Código
     */
    public void consultarGuiaCod(){
        Date hoy = new Date(System.currentTimeMillis());
        int i = 1;
        try{
            Guia g = guiaFacade.getVigByCodigo(codigo);
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
     * Método para consultar las Guías vigentes de un Remitente
     */
    public void consultarGuiasPorRem(){
        String valid = validarCuit(cuitRem);
        List<Guia> guiasLeidas;
        Date hoy = new Date(System.currentTimeMillis());
        int i = 1;
        if(valid.equals("")){
            try{
                guiasLeidas = guiaFacade.getVigByOrigen(cuitRem);
                lstGuias = new ArrayList<>();
                for(Guia g : guiasLeidas){
                    if(g != null){
                        i = hoy.compareTo(g.getFechaVencimiento());
                    }
                    if(i <= 0){
                        lstGuias.add(g);
                    }
                }
                consultado = true;
            }catch(Exception ex){
                JsfUtil.addErrorMessage("Hubo un error consultando las Guías remitidas por el CUIT ingresado." + ex.getMessage());
            }
        }else{
            JsfUtil.addErrorMessage(valid);
        } 
    }
    
    /**
     * Método para consultar las Guías vigentes destinadas al cuit ingresado
     */
    public void consultarGuiasPorDest(){
        String valid = validarCuit(cuitDest);
        List<Guia> guiasLeidas;
        Date hoy = new Date(System.currentTimeMillis());
        int i = 1;
        if(valid.equals("")){
            try{
                guiasLeidas = guiaFacade.getVigByDestino(cuitDest);
                lstGuias = new ArrayList<>();
                for(Guia g : guiasLeidas){
                    if(g != null){
                        i = hoy.compareTo(g.getFechaVencimiento());
                    }
                    if(i <= 0){
                        lstGuias.add(g);
                    }
                }
                consultado = true;
            }catch(Exception ex){
                JsfUtil.addErrorMessage("Hubo un error consultando las Guías destinadas al CUIT ingresado." + ex.getMessage());
            }
        }else{
            JsfUtil.addErrorMessage(valid);
        } 
    }

    /**
     * Método para registrar un Control realizado a una Guía por el Usuario
     */
    public void saveControl(){
        control.setObs(obs);
        control.setGuia(guia);
        control.setUsuario(usLogueado);
        Date fecha = new Date(System.currentTimeMillis());
        control.setFechaHora(fecha);
        try{
            Parametrica resultado = paramFacade.find(Long.valueOf(result));
            control.setResultado(resultado);
            // registro el control
            controlFacade.create(control);
            // actualizo el estado de la Guía
            guia.setEstado(resultado);
            guiaFacade.edit(guia);
            // Si se intervino o se secuestró la Guía, lo comunico al Componente local para conocimiento de las autoridades locales
            if(!control.getResultado().getNombre().equals(ResourceBundle.getBundle("/Config").getString("Validada"))){
                // actualizo el estado en el CGL
                List<ar.gob.ambiente.sacvefor.servicios.cgl.EstadoGuia> lstEstados;
                ar.gob.ambiente.sacvefor.servicios.cgl.Guia guiaLocal;
                estadoGuiaLocClient = new EstadoGuiaLocalClient(guia.getCompLocal().getUrl());
                GenericType<List<ar.gob.ambiente.sacvefor.servicios.cgl.EstadoGuia>> gTypeEstado = new GenericType<List<ar.gob.ambiente.sacvefor.servicios.cgl.EstadoGuia>>() {};
                Response response = estadoGuiaLocClient.findByQuery_JSON(Response.class, resultado.getNombre());
                lstEstados = response.readEntity(gTypeEstado);
                estadoGuiaLocClient.close();
                if(!lstEstados.isEmpty()){
                    // obtengo la Guía desde el CGL
                    List<ar.gob.ambiente.sacvefor.servicios.cgl.Guia> lstG;
                    guiaLocalClient = new GuiaLocalClient(guia.getCompLocal().getUrl());
                    GenericType<List<ar.gob.ambiente.sacvefor.servicios.cgl.Guia>> gTypeGuia = new GenericType<List<ar.gob.ambiente.sacvefor.servicios.cgl.Guia>>() {};
                    response = guiaLocalClient.findByQuery_JSON(Response.class, guia.getCodigo(), null, null);
                    lstG = response.readEntity(gTypeGuia);
                    if(!lstG.isEmpty()){
                        guiaLocal = lstG.get(0);
                        guiaLocal.setEstado(lstEstados.get(0));
                        // actualizo
                        response = guiaLocalClient.edit_JSON(guiaLocal, String.valueOf(guiaLocal.getId()));
                        guiaLocalClient.close();
                        // continúo según el mensaje recibido
                        if(response.getStatus() == 200){
                            // notifico la intervención al Componente local
                            if(!enviarCorreo()){
                                JsfUtil.addErrorMessage("El Control se registró correctamente pero no se pudo enviar el correo a la Autoridad local comunicando el resultado correspondiente.");
                                LOG.warn("No se envió el correo de notificación de la intervención desde el CCV al CGL.");
                            }
                            JsfUtil.addSuccessMessage("El Control se registró correctamente y se notificó a la Autoridad local.");
                        }else{
                            JsfUtil.addErrorMessage("El Control se registró correctamente pero no se pudo actualizar la Guía en el Componente Local.");
                            LOG.warn("Hubo un error al actualizar el estado de la Guía en el Componente Local mediante la API CGL.");
                        }
                    }else{
                        JsfUtil.addErrorMessage("No se encontró la Guía en el respectivo Componente Local.");
                    }
                }else{
                    JsfUtil.addErrorMessage("No se encontró el estado de la Guía en el Componente Local.");
                }
            }else{
                JsfUtil.addSuccessMessage("El Control se registró correctamente.");
            }
        }catch(ClientErrorException ex){
            JsfUtil.addErrorMessage("Hubo un error registrando el control realizado.");
            LOG.fatal("Hubo un error registrando el control realizado. " + ex.getMessage());
        }
    }
    
    public void limpiarFrmRegControl(){
        control = new Control();
        parmSelected = new EntidadCombo();
    }
    
    /*********************
     * Métodos privados **
     *********************/
    
    /**
     * Método para obtener una Paramétrica según su id
     * @param key
     * @return 
     */
    private Object getParametrica(Long key) {
        return paramFacade.find(key);
    }     
    
    /**
     * Método para validar cuit. Si es válido devuelve "", y si no un mensaje.
     * @param cuit
     * @return 
     */
    private String validarCuit(Long cuit) {
        String result = "";
        
        String strCuit = String.valueOf(cuit);
        // dejo solo números
        strCuit = strCuit.replaceAll("[^\\d]", "");
        // valido la cantidad de números
        if(strCuit.length() == 11){
            // armo un array de caracteres
            char[] cuitArray = strCuit.toCharArray();
            // inicializo un array de enterios por cada uno de los cuales se multiplicará cada uno de los dígitos del rueCuit a validar
            Integer[] serie = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
            // instancio una variable auxiliar para guardar los resultados del calculo del número validador
            Integer aux = 0;
            // recorro el ambos arrays y opero
            for (int i=0; i<10; i++){
                aux += Character.getNumericValue(cuitArray[i]) * serie[i];
            }
            // ejecuto la especificación (Módulo 11): 11 menos el resto de de la división de la suma de productos anterior por 11
            aux = 11 - (aux % 11);
            //Si el resultado anterior es 11 el código es 0
            if (aux == 11){
                aux = 0;
            }
            //Si el resultado anterior es 10 el código no tiene que validar, cosa que de todas formas pasa
            //en la siguiente comparación.
            //Devuelve verdadero si son iguales, falso si no lo son
            //(Paso todo a Object para prevenir errores, se puede usar: Integer.valueOf(cuitArray[10]) == aux;)
            Object oUltimo = (Integer)Character.getNumericValue(cuitArray[10]);
            Object oAux = aux;
            
            if(!oUltimo.equals(oAux)){
                result = "El CUIT ingresado es inválido.";
            }         
        }    
        return result;
    }

    /**
     * Método para enviar un correo a la Autoridad local que emitió la Guía comunicando el resultado negativo del control
     * @return 
     */  
    private boolean enviarCorreo() {
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        boolean result;
        String bodyMessage;
        mensaje = new MimeMessage(mailSesion);
        bodyMessage = "<p>A la Autoridad Local de Bosques de la Provinica de " + guia.getCompLocal().getProvincia() + "</p> "
                + "<p>Estimado/a</p> "
                + "<p>La Guía " + guia.getCodigo() + " emitida el " + formateador.format(guia.getFechaEmision()) + " "
                + "remitida por " + guia.getNombreOrigen() + ", CUIT: " + guia.getCuitOrigen() + " "
                + "destinada a " + guia.getNombreDestino() + ", CUIT: " + guia.getCuitDestino() + " se ha "
                + control.getResultado().getNombre() + " con fecha " + formateador.format(control.getFechaHora()) + " "
                + "en el Puesto de control ubicado en la localidad de: " + control.getUsuario().getPuestoControl().getLocalidad() + ", "
                + "Ruta " + control.getUsuario().getPuestoControl().getRuta() + ", altura " + control.getUsuario().getPuestoControl().getAltura() + ", "
                + "con las siguientes observaciones: " + obs + ". "
                + "<p>Queda Ud. notificado/a.</p>"
                + "<p>Para mayor información, por favor diríjase a los datos remitidos al pié del presente mensaje.</p> "
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
            mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(guia.getCompLocal().getCorreoElectronico()));
            mensaje.setSubject("SACVeFor - Control y Verificación - Notificación de Intervención de Guía" );
            mensaje.setContent(bodyMessage, "text/html; charset=utf-8");
            
            Date timeStamp = new Date();
            mensaje.setSentDate(timeStamp);
            
            Transport.send(mensaje);
            
            result = true;
            
        }catch(MessagingException ex){
            result = false;
            LOG.fatal("Hubo un error enviando el correo de notificación de resultado. " + ex.getMessage());
        }
        
        return result;
    }

    
    /***************************
    ** Converter Parametrica  **
    ****************************/ 
    @FacesConverter(forClass = Parametrica.class)
    public static class ParametricaConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MbControl controller = (MbControl) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mbControl");
            return controller.getParametrica(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }
        
        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Parametrica) {
                Parametrica o = (Parametrica) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Parametrica.class.getName());
            }
        }
    }       
}
