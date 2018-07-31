
package ar.gob.ambiente.sacvefor.controlverificacion.mb;

import ar.gob.ambiente.sacvefor.controlverificacion.cgl.client.EstadoGuiaLocalClient;
import ar.gob.ambiente.sacvefor.controlverificacion.cgl.client.GuiaLocalClient;
import ar.gob.ambiente.sacvefor.controlverificacion.cgl.client.UsuarioApiClient;
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
import ar.gob.ambiente.sacvefor.controlverificacion.util.Token;
import java.io.IOException;
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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * Bean de gestión de las consultas para el Control y la Verificación de Guías por Usuarios registrados
 * @author rincostante
 */
public class MbControl {

    /**
     * Variable privada: Entidada para gestionar objetos de combos, en este caso Paramétrica
     */
    private EntidadCombo parmSelected;
    
    /**
     * Variable privada: Listado de entidades combo que en este caso guardarán resultados de los controles
     */
    private List<EntidadCombo> lstResultados;
    
    /**
     * Variable privada: listado de los Controles registrados
     */
    private List<Control> lstControles;
    
    /**
     * Variable privada: matrícula de un vehículo a buscar para ver o efecutar controles
     */
    private String matricula;
    
    /**
     * Variable privada: código único de guía para efectuar un control
     */
    private String codigo;
    
    /**
     * Variable privada: cuit del remitente de las guías a efectuar un control
     */
    private Long cuitRem;
    
    /**
     * Variable privada: cuit del destinatario de las guías a efectuar un control
     */
    private Long cuitDest;
    
    /**
     * Variable privada: guía a controlar
     */
    private Guia guia;
    
    /**
     * Variable privada: listado de guías a controlar
     */
    private List<Guia> lstGuias;
    
    /**
     * Variable privada: flag que indica si una guía está siendo consultada
     */
    private boolean consultado;
    
    /**
     * Variable privada: flag que indica si se está consultando guías por la matrícula del vehículo correspondiente
     */
    private boolean verPorMatricula;
    
    /**
     * Variable privada: flag que indica si una guía se está consultando por el código correspondiente
     */
    private boolean verPorCodigo;
    
    /**
     * Variable privada: flag que indica si una guía está siendo consultada según su código
     */
    private boolean verPorCuitRem;
    
    /**
     * Variable privada: flag que indica si las guías están siendo consultadas según el cuit de su destinatario
     */
    private boolean verPorCuitDest;   
    
    /**
     * Variable privada: flag que indica si se están consultando controles
     */
    private boolean verControles;
    
    /**
     * Variable privada: flag que indica si se están listando todos los controles
     */
    private boolean verTodos;
    
    /**
     * Variable privada: control gestionado
     */
    private Control control;
    
    /**
     * Variable privada: bean de sesión para gestionar el usuario logeado
     */
    private MbSesion sesion;
    
    /**
     * Variable privada: usuario logeado
     */
    private Usuario usLogueado;
    
    /**
     * Variable privada: indica el id de la parametrica que hará las veces de resultado del control
     */
    private int result;
    
    /**
     * Variable privada: Logger para escribir en el log del server
     */ 
    static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MbControl.class);
    
    /**
     * Variable privada: observaciones relacionadas con el control realizado
     */
    private String obs;
    
    // campos y recursos para el envío de correos al usuario
    /**
     * Variable privada: sesión de mail del servidor
     */    
    @Resource(mappedName ="java:/mail/ambientePrueba")    
    private Session mailSesion;
    
    /**
     * Variable privada: String mensaje a enviar por correo electrónico
     */ 
    private Message mensaje;     
    
    // campos para la API CGL
    /**
     * Variable privada: cliente para consultar la API de Guías de la gestion local correspondiente 
     */
    private GuiaLocalClient guiaLocalClient;
    
    /**
     * Variable privada: cliente para consultar la API de Estados de guías de la gestion local correspondiente 
     */
    private EstadoGuiaLocalClient estadoGuiaLocClient;
    
    /**
     * Variable privada: UsuarioApiClient Cliente para la API REST de Gestión local
     */    
    private UsuarioApiClient usuarioClientCgl;
    
    /**
     * Variable privada: Token obtenido al validar el usuario de la API de Gestión local
     */     
    private Token tokenCgl;
    
    /**
     * Variable privada: Token en formato String del obtenido al validar el usuario de la API de Gestión local
     */ 
    private String strTokenCgl;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de Parametrica
     */   
    @EJB
    private ParametricaFacade paramFacade;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de TipoParam
     */   
    @EJB
    private TipoParamFacade tipoParamFacade;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de Guia
     */   
    @EJB
    private GuiaFacade guiaFacade;
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de la entidad gestionada
     */ 
    @EJB
    private ControlFacade controlFacade;
    
    /**
     * Constructor
     */
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

    /**
     * Método que obtiene un control según la Guía y el usuario que correspondan
     * @return Control control existente o nulo según corresponda
     */
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
 
    /**
     * Método que inicializa las entidades a gestionar, el bean de sesión y el usuario
     */    
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
     * Método para registrar un Control realizado a una Guía por el Usuario.
     * Consume la API de gestión local que corresponda para obtener la Guía y actualizar su estado.
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
                
                // instancio el cliente para la selección del Estado de guía, obtengo el token si no está seteado o está vencido
                if(tokenCgl == null){
                    getTokenCgl();
                }else try {
                    if(!tokenCgl.isVigente()){
                        getTokenCgl();
                    }
                } catch (IOException ex) {
                    LOG.fatal("Hubo un error obteniendo la vigencia del token de Gestión local. " + ex.getMessage());
                }
                estadoGuiaLocClient = new EstadoGuiaLocalClient(guia.getCompLocal().getUrl());
                GenericType<List<ar.gob.ambiente.sacvefor.servicios.cgl.EstadoGuia>> gTypeEstado = new GenericType<List<ar.gob.ambiente.sacvefor.servicios.cgl.EstadoGuia>>() {};
                Response response = estadoGuiaLocClient.findByQuery_JSON(Response.class, resultado.getNombre(), tokenCgl.getStrToken());
                lstEstados = response.readEntity(gTypeEstado);
                estadoGuiaLocClient.close();
                if(!lstEstados.isEmpty()){
                    // obtengo la Guía desde el CGL
                    List<ar.gob.ambiente.sacvefor.servicios.cgl.Guia> lstG;
                    
                    // instancio el cliente para la selección de la Guía, obtengo el token si no está seteado o está vencido
                    if(tokenCgl == null){
                        getTokenCgl();
                    }else try {
                        if(!tokenCgl.isVigente()){
                            getTokenCgl();
                        }
                    } catch (IOException ex) {
                        LOG.fatal("Hubo un error obteniendo la vigencia del token de Gestión local. " + ex.getMessage());
                    }
                    guiaLocalClient = new GuiaLocalClient(guia.getCompLocal().getUrl());
                    GenericType<List<ar.gob.ambiente.sacvefor.servicios.cgl.Guia>> gTypeGuia = new GenericType<List<ar.gob.ambiente.sacvefor.servicios.cgl.Guia>>() {};
                    response = guiaLocalClient.findByQuery_JSON(Response.class, guia.getCodigo(), null, null, tokenCgl.getStrToken());
                    lstG = response.readEntity(gTypeGuia);
                    if(!lstG.isEmpty()){
                        guiaLocal = lstG.get(0);
                        guiaLocal.setEstado(lstEstados.get(0));
                        // actualizo
                        // instancio el cliente para la edición de la Guía, obtengo el token si no está seteado o está vencido
                        if(tokenCgl == null){
                            getTokenCgl();
                        }else try {
                            if(!tokenCgl.isVigente()){
                                getTokenCgl();
                            }
                        } catch (IOException ex) {
                            LOG.fatal("Hubo un error obteniendo la vigencia del token de Gestión local. " + ex.getMessage());
                        }
                        response = guiaLocalClient.edit_JSON(guiaLocal, String.valueOf(guiaLocal.getId()), tokenCgl.getStrToken());
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
    
    /**
     * Método para limpiar el formulario de registro de los resultados del control
     */
    public void limpiarFrmRegControl(){
        control = new Control();
        parmSelected = new EntidadCombo();
    }
    
    /*********************
     * Métodos privados **
     *********************/
    
    /**
     * Método para obtener una Paramétrica según su id.
     * Utilizado por el converter
     * @param key Long id de la paramétrica a buscar
     * @return Object paramétrica obtenida
     */
    private Object getParametrica(Long key) {
        return paramFacade.find(key);
    }     
    
    /**
     * Método para validar cuit. Si es válido devuelve "", y si no un mensaje.
     * Utilizado por consultarGuiasPorRem() y consultarGuiasPorDest()
     * @param cuit Long cuit a validar
     * @return String cadena vacía o un mensaje de error
     */
    private String validarCuit(Long cuit) {
        String resultCuit = "";
        
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
                resultCuit = "El CUIT ingresado es inválido.";
            }         
        }    
        return resultCuit;
    }

    /**
     * Método para enviar un correo a la Autoridad local que emitió la Guía comunicando el resultado negativo del control.
     * Utilizado por saveControl()
     * @return boolean true si el envío fue exitoso y false en caso de error
     */  
    private boolean enviarCorreo() {
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        boolean resultCorreo;
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
            
            resultCorreo = true;
            
        }catch(MessagingException ex){
            resultCorreo = false;
            LOG.fatal("Hubo un error enviando el correo de notificación de resultado. " + ex.getMessage());
        }
        
        return resultCorreo;
    }
    
    /**
     * Método privado que obtiene y setea el token para autentificarse ante la API rest de Gestión local para la provincia que corresponda
     * Crea el campo de tipo Token con la clave recibida y el momento de la obtención.
     * Utilizado por saveControl()
     */
    private void getTokenCgl(){
        try{
            usuarioClientCgl = new UsuarioApiClient(guia.getCompLocal().getUrl());
            Response responseUs = usuarioClientCgl.authenticateUser_JSON(Response.class, ResourceBundle.getBundle("/Config").getString("UsRestCgl"));
            MultivaluedMap<String, Object> headers = responseUs.getHeaders();
            List<Object> lstHeaders = headers.get("Authorization");
            strTokenCgl = (String)lstHeaders.get(0); 
            tokenCgl = new Token(strTokenCgl, System.currentTimeMillis());
            usuarioClientCgl.close();
        }catch(ClientErrorException ex){
            System.out.println("Hubo un error obteniendo el token para la API Gestión local: " + ex.getMessage());
        }
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
