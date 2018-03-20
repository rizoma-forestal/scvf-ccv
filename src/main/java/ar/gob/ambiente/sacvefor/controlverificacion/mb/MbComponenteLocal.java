
package ar.gob.ambiente.sacvefor.controlverificacion.mb;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.ComponenteLocal;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.ComponenteLocalFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.territ.client.ProvinciaClient;
import ar.gob.ambiente.sacvefor.controlverificacion.util.EntidadCombo;
import ar.gob.ambiente.sacvefor.controlverificacion.util.JsfUtil;
import ar.gob.ambiente.sacvefor.controlverificacion.util.Token;
import ar.gob.ambiente.sacvefor.servicios.territorial.Provincia;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Bean de respaldo para la gestión de Componentes locales del Sistema
 * @author rincostante
 */
public class MbComponenteLocal implements Serializable{

    /**
     * Variable privada: Entidad a gestionar por el bean
     */
    private ComponenteLocal compLocal;
    
    /**
     * Variable privada: listado de componentes locales registrados
     */
    private List<ComponenteLocal> lstCompLocales;
    
    /**
     * Variable privada: listado de Entidades para gestionar objetos en combos
     */
    private List<EntidadCombo> lstProv;
    
    /**
     * Variable privada: UsuarioClient Cliente para la API REST de Territorial
     */
    private ar.gob.ambiente.sacvefor.controlverificacion.territ.client.UsuarioClient usuarioClientTerr;
    
    /**
     * Variable privada: Token obtenido al validar el usuario de la API Territorial
     */ 
    private Token tokenTerr;
    
    /**
     * Variable privada: Token en formato String del obtenido al validar el usuario de la API Territorial
     */ 
    private String strTokenTerr;     
    
    /**
     * Variable privada: identificador de la provincia dentro de la cual se situará el componente local
     */
    private int idProv;
    
    /**
     * Variable privada: inidica si el formulario es para una vista detalle de la entidad
     */
    private boolean view;
    
    /**
     * Variable privada: inidica si el formulario es para una vista de edición de la entidad
     */
    private boolean edit;
    
    /**
     * Variable privada: Logger para escribir en el log del server
     */ 
    static final Logger LOG = Logger.getLogger(MbComponenteLocal.class);
    
    // Clientes REST para la gestión del API Territorial
    /**
     * Variable privada: Cliente para la API de servicios Territorial
     */
    private ProvinciaClient provClient;     
    
    /**
     * Variable privada: EJB inyectado para el acceso a datos de la entidad gestionada
     */      
    @EJB
    private ComponenteLocalFacade compLocalFacade;
    
    /**
     * Constructor
     */
    public MbComponenteLocal() {
    }

    public ComponenteLocal getCompLocal() {
        return compLocal;
    }

    public void setCompLocal(ComponenteLocal compLocal) {
        this.compLocal = compLocal;
    }

    public List<ComponenteLocal> getLstCompLocales() {
        lstCompLocales = compLocalFacade.findAll();
        return lstCompLocales;
    }

    public void setLstCompLocales(List<ComponenteLocal> lstCompLocales) {
        this.lstCompLocales = lstCompLocales;
    }

    public List<EntidadCombo> getLstProv() {
        if(lstProv == null) lstProv = new ArrayList<>();
        return lstProv;
    }

    public void setLstProv(List<EntidadCombo> lstProv) {
        this.lstProv = lstProv;
    }

    public int getIdProv() {
        return idProv;
    }

    public void setIdProv(int idProv) {
        this.idProv = idProv;
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
    
    /**
     * Método que se ejecuta luego de instanciada la clase e inicializa las entidades a gestionar 
     * y carga las provincias registradas en el API territorial en el combo correspondinte
     */
    @PostConstruct
    public void init(){
        compLocal = new ComponenteLocal();
        cargarProvincias();
    } 

    /***********************
     * Mátodos operativos **
     ***********************/

    /**
     * Método para guardar el Componente Local, sea inserción o edición.
     * Previa validación
     */
    public void save(){
        boolean valida = true;
        if(validarMail()){
            try{
                // verifico si ya hay un Componente local registrado para la pronvincia 
                ComponenteLocal comLoc = compLocalFacade.getExistente(compLocal.getProvincia());
                if(comLoc != null){
                    if(compLocal.getId() != null){
                        // si edita, no habilito si no es el mismo
                        if(!compLocal.equals(comLoc)) valida = false;
                    }else{
                        // si no edita no habilito de ninguna manera
                        valida = false;
                    }
                }
                if(valida){
                    // seteo la provincia
                    for(EntidadCombo ec : lstProv){
                        if(idProv == ec.getId()){
                            compLocal.setProvincia(ec.getNombre());
                            compLocal.setIdProvGt(Long.valueOf(idProv));
                        }
                    }
                    // persisto
                    if(compLocal.getId() != null){
                        compLocalFacade.edit(compLocal);
                        JsfUtil.addSuccessMessage("El Componente Local se guardó correctamente");
                    }else{
                        compLocal.setHabilitado(true);
                        compLocalFacade.create(compLocal);
                        JsfUtil.addSuccessMessage("El Componente Local se registró correctamente");
                    }
                    edit = false;
                }else{
                    JsfUtil.addErrorMessage("Hubo un error guardando el Componente Local.");
                }
                limpiarForm();
            }catch(Exception ex){
                JsfUtil.addErrorMessage("Hubo un error guardando el Componente Local: " + ex.getMessage());
            }
        }else{
            JsfUtil.addErrorMessage("El correo electrónico ingresado es inválido.");
        }
    }   
    
    /**
     * Método para deshabilitar un Componente local
     * No estará disponible en los listados correspondientes
     */
    public void deshabilitar(){
        view = false;
        edit = false;
        try{
            compLocal.setHabilitado(false);
            compLocalFacade.edit(compLocal);
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error al deshabilitar el Componente local: " + ex.getMessage());
        }
    }        
    
    /**
     * Método para habilitar un Componente local
     * No estará disponible en los listados correspondientes
     */    
    public void habilitar(){
        view = false;
        edit = false;
        try{
            compLocal.setHabilitado(true);
            compLocalFacade.edit(compLocal);
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error al habilitar el Componente local: " + ex.getMessage());
        }
    }    

    /**
     * Método para limpiar el objeto ComponenteLocal del formulario correspondiente
     */
    public void limpiarForm(){
        compLocal = new ComponenteLocal();
        idProv = 0;
    }    
    
    /**
     * Método para habilitar el formulario de creación
     */
    public void prepareNew(){
        compLocal = new ComponenteLocal();
        view = false;
        edit = true;
    }    

    /**
     * Método para habilitar el formulario de edición
     * para cualquier caso
     */
    public void prepareEdit(){
        // actualizar el idProv. Guardo el 0 para la Provincia de prueba
        if(compLocal.getIdProvGt() == 0){
            idProv = 0;
        }else{
            idProv = compLocal.getIdProvGt().intValue();
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
    
    /*********************
     * Métodos privados **
     *********************/
    
    /**
     * Método para cargar el listado de Provincias para su selección.
     * Se comunica con la API Territorial mediante el cliente respectivo
     * Utilizado por init()
     */
    private void cargarProvincias() {
        EntidadCombo provincia;
        List<Provincia> listSrv;
        
        try{
            // instancio el cliente para la selección del Departamento TERR, obtengo el token si no está seteado o está vencido
            if(tokenTerr == null){
                getTokenTerr();
            }else try {
                if(!tokenTerr.isVigente()){
                    getTokenTerr();
                }
            } catch (IOException ex) {
                LOG.fatal("Hubo un error obteniendo la vigencia del token Territorial. " + ex.getMessage());
            }
            // instancio el cliente para la selección de las provincias
            provClient = new ProvinciaClient();
            // obtengo el listado de provincias 
            GenericType<List<Provincia>> gType = new GenericType<List<Provincia>>() {};
            Response response = provClient.findAll_JSON(Response.class, tokenTerr.getStrToken());
            listSrv = response.readEntity(gType);
            // lleno el list con las provincias como un objeto Entidad Servicio
            lstProv = new ArrayList<>();
            for(Provincia prov : listSrv){
                provincia = new EntidadCombo(prov.getId().intValue(), prov.getNombre());
                lstProv.add(provincia);
            }
            // cierro el cliente
            provClient.close();
            // agrego la provincia de prueba
            provincia = new EntidadCombo(0, "[Provincia]");
            lstProv.add(provincia);
        }catch(ClientErrorException ex){
            // inicializo las propiedades del legger
            BasicConfigurator.configure();
            // muestro un mensaje al usuario
            JsfUtil.addErrorMessage("Hubo un error cargando el listado de Provincias para su selección.");
            // lo escribo en el log del server
            LOG.fatal("Hubo un error cargando las Provincias desde el servicio REST de Centros poblados. " + ex.getMessage());
        }
    }    

    /**
     * Método para validar el correo electrónico ingresado
     * @return boolean true o false según el correo esté o no validado
     */
    private boolean validarMail() {
        // defino el pattern para comparar
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        // comparo y retorno
        Matcher mather = pattern.matcher(compLocal.getCorreoElectronico());
        return mather.find() == true;
    }
    
    /**
     * Método privado que obtiene y setea el token para autentificarse ante la API rest de Territorial
     * Crea el campo de tipo Token con la clave recibida y el momento de la obtención.
     * Utilizado por cargarProvincias()
     */
    private void getTokenTerr(){
        try{
            usuarioClientTerr = new ar.gob.ambiente.sacvefor.controlverificacion.territ.client.UsuarioClient();
            Response responseUs = usuarioClientTerr.authenticateUser_JSON(Response.class, ResourceBundle.getBundle("/Config").getString("UsRestTerr"));
            MultivaluedMap<String, Object> headers = responseUs.getHeaders();
            List<Object> lstHeaders = headers.get("Authorization");
            strTokenTerr = (String)lstHeaders.get(0); 
            tokenTerr = new Token(strTokenTerr, System.currentTimeMillis());
            usuarioClientTerr.close();
        }catch(ClientErrorException ex){
            System.out.println("Hubo un error obteniendo el token para la API RUE: " + ex.getMessage());
        }
    }         
}
