
package ar.gob.ambiente.sacvefor.controlverificacion.mb;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.PuestoControl;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.PuestoControlFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.territ.client.DepartamentoClient;
import ar.gob.ambiente.sacvefor.controlverificacion.territ.client.LocalidadClient;
import ar.gob.ambiente.sacvefor.controlverificacion.territ.client.ProvinciaClient;
import ar.gob.ambiente.sacvefor.controlverificacion.util.EntidadCombo;
import ar.gob.ambiente.sacvefor.controlverificacion.util.JsfUtil;
import ar.gob.ambiente.sacvefor.servicios.territorial.CentroPoblado;
import ar.gob.ambiente.sacvefor.servicios.territorial.Departamento;
import ar.gob.ambiente.sacvefor.servicios.territorial.Provincia;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Bean de respaldo para la gestión de Puestos de control del Sistema
 * @author rincostante
 */
public class MbPuestoControl {

    private PuestoControl puestoControl;
    private List<PuestoControl> lstPuestos;
    private List<EntidadCombo> lstProv;
    private List<EntidadCombo> lstDeptos;
    private List<EntidadCombo> lstLocalidades;
    private int idLocGt;
    private int idDeptoGt;
    private int idProvGt;
    private boolean view;
    private boolean edit;
    static final Logger LOG = Logger.getLogger(MbPuestoControl.class);
    
    // Clientes REST para la gestión del API Territorial
    private ProvinciaClient provClient;  
    private DepartamentoClient deptoClient;
    private LocalidadClient localClient;
    
    @EJB
    private PuestoControlFacade puestoFacade;
    
    public MbPuestoControl() {
    }

    public int getIdDeptoGt() {
        return idDeptoGt;
    }

    public void setIdDeptoGt(int idDeptoGt) {
        this.idDeptoGt = idDeptoGt;
    }

    public int getIdProvGt() {
        return idProvGt;
    }

    public void setIdProvGt(int idProvGt) {
        this.idProvGt = idProvGt;
    }

    public PuestoControl getPuestoControl() {
        return puestoControl;
    }

    public void setPuestoControl(PuestoControl puestoControl) {
        this.puestoControl = puestoControl;
    }

    public List<PuestoControl> getLstPuestos() {
        lstPuestos = puestoFacade.findAll();
        return lstPuestos;
    }

    public void setLstPuestos(List<PuestoControl> lstPuestos) {
        this.lstPuestos = lstPuestos;
    }

    public List<EntidadCombo> getLstProv() {
        return lstProv;
    }

    public void setLstProv(List<EntidadCombo> lstProv) {
        this.lstProv = lstProv;
    }

    public List<EntidadCombo> getLstDeptos() {
        return lstDeptos;
    }

    public void setLstDeptos(List<EntidadCombo> lstDeptos) {
        this.lstDeptos = lstDeptos;
    }

    public List<EntidadCombo> getLstLocalidades() {
        return lstLocalidades;
    }

    public void setLstLocalidades(List<EntidadCombo> lstLocalidades) {
        this.lstLocalidades = lstLocalidades;
    }

    public int getIdLocGt() {
        return idLocGt;
    }

    public void setIdLocGt(int idLocGt) {
        this.idLocGt = idLocGt;
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
        puestoControl = new PuestoControl();
        cargarProvincias();
    }
    
    /**
     * Método para actualizar el listado de Departamentos al seleccionar una Provincia
     * para la inserción de Personas en el RUP
     */
    public void provinciaChangeListener(){
        getDepartamentosSrv(idProvGt);
    }   
    
    /**
     * Método para actualizar el listado de Localidades al seleccionar un Departamento
     */    
    public void deptoChangeListener(){
        getLocalidadesSrv(idDeptoGt);
    }         
    
    /***********************
     * Mátodos operativos **
     ***********************/

    /**
     * Método para guardar el Puesto de control, sea inserción o edición.
     * Previa validación
     */
    public void save(){
        boolean valida = true;
        String msgValid = validarDatos();
        if(msgValid.equals("")){
            try{
                // verifico si ya hay un Puesto de Control registrado con el mismo nombre para la misma localidad
                PuestoControl pst = puestoFacade.getExistente(puestoControl.getNombre(), Long.valueOf(idLocGt));
                if(pst != null){
                    if(puestoControl.getId() != null){
                        // si edita, no habilito si no es el mismo
                        if(!puestoControl.equals(pst)) valida = false;
                    }else{
                        // si no edita no habilito de ninguna manera
                        valida = false;
                    }
                }
                if(valida){
                    // seteo la id localidad
                    puestoControl.setIdLocGt(Long.valueOf(idLocGt));
                    // seteo la Provincia
                    for(EntidadCombo pr : lstProv){
                        if(idProvGt == pr.getId()){
                            puestoControl.setProvincia(pr.getNombre());
                        }
                    }
                    // seteo el Departamento
                    for(EntidadCombo de : lstDeptos){
                        if(idDeptoGt == de.getId()){
                            puestoControl.setDepartamento(de.getNombre());
                        }
                    }
                    // seteo la Localidad
                    for(EntidadCombo lo : lstLocalidades){
                        if(idLocGt == lo.getId()){
                            puestoControl.setLocalidad(lo.getNombre());
                        }
                    }
                    // persisto
                    if(puestoControl.getId() != null){
                        puestoFacade.edit(puestoControl);
                        JsfUtil.addSuccessMessage("El Puesto de Control se guardó correctamente");
                    }else{
                        puestoControl.setHabilitado(true);
                        puestoFacade.create(puestoControl);
                        JsfUtil.addSuccessMessage("El Puesto de Control se registró correctamente");
                    }
                    edit = false;
                }else{
                    JsfUtil.addErrorMessage("Ya existe un Puesto de Control con el mismo nombre para la localidad seleccionada.");
                }
                limpiarForm();
            }catch(Exception ex){
                JsfUtil.addErrorMessage("Hubo un error guardando el Puesto de Control: " + ex.getMessage());
            }
        }else{
            JsfUtil.addErrorMessage(msgValid);
        }
    }    
    
    /**
     * Método para deshabilitar un Puesto de control
     * No estará disponible en los listados correspondientes
     */
    public void deshabilitar(){
        view = false;
        edit = false;
        try{
            puestoControl.setHabilitado(false);
            puestoFacade.edit(puestoControl);
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error al deshabilitar el Puesto de control: " + ex.getMessage());
        }
    }    
    
    /**
     * Método para habilitar un Puesto de control
     * No estará disponible en los listados correspondientes
     */
    public void habilitar(){
        view = false;
        edit = false;
        try{
            puestoControl.setHabilitado(true);
            puestoFacade.edit(puestoControl);
            limpiarForm();
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error al deshabilitar el Puesto de control: " + ex.getMessage());
        }
    }     
    
    /**
     * Método para habilitar el formulario de creación
     */
    public void prepareNew(){
        puestoControl = new PuestoControl();
        view = false;
        edit = true;
    }        
    
    /**
     * Método para habilitar el formulario de edición
     * para cualquier caso
     */
    public void prepareEdit(){
        // actualizar el idLocGt. Guardo el 0
        idLocGt = puestoControl.getIdLocGt().intValue();
        cargarEntidadesTerr(idLocGt);
        view = false;
        edit = true;   
    }       
    
    /**
     * Método para limpiar el objeto ComponenteLocal del formulario correspondiente
     */
    public void limpiarForm(){
        puestoControl = new PuestoControl();
        idLocGt = 0;
        idDeptoGt = 0;
        idProvGt = 0;
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
     * Método para cargar el listado de Provincias para su selección
     */
    private void cargarProvincias() {
        EntidadCombo provincia;
        List<Provincia> listSrv;
        
        try{
            // instancio el cliente para la selección de las provincias
            provClient = new ProvinciaClient();
            // obtengo el listado de provincias 
            GenericType<List<Provincia>> gType = new GenericType<List<Provincia>>() {};
            Response response = provClient.findAll_JSON(Response.class);
            listSrv = response.readEntity(gType);
            // lleno el list con las provincias como un objeto Entidad Servicio
            lstProv = new ArrayList<>();
            for(Provincia prov : listSrv){
                provincia = new EntidadCombo(prov.getId().intValue(), prov.getNombre());
                lstProv.add(provincia);
            }
            // cierro el cliente
            provClient.close();
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
     * Método que carga el listado de Departamentos según la Provincia seleccionada
     * @param id 
     */
    private void getDepartamentosSrv(int idProv) {
        EntidadCombo depto;
        List<Departamento> listSrv;
        
        try{
            // instancio el cliente para la selección de los Departamentos
            provClient = new ProvinciaClient();
            // obtngo el listado
            GenericType<List<Departamento>> gType = new GenericType<List<Departamento>>() {};
            Response response = provClient.findByProvincia_JSON(Response.class, String.valueOf(idProv));
            listSrv = response.readEntity(gType);
            // lleno el listado de los combos
            lstDeptos = new ArrayList<>();
            for(Departamento dpt : listSrv){
                depto = new EntidadCombo(dpt.getId().intValue(), dpt.getNombre());
                lstDeptos.add(depto);
            }
            
            provClient.close();
        }catch(ClientErrorException ex){
            // muestro un mensaje al usuario
            JsfUtil.addErrorMessage("Hubo un error obteniendo los Departamentos de la Provincia seleccionada. " + ex.getMessage());
            LOG.fatal("Hubo un error obteniendo los Departamentos por Provincia del servicio REST de centros poblados. " + ex.getMessage());
        }
    }    
    
    /**
     * Método que carga el listado de Localidades según el Departamento seleccionado
     * @param id 
     */    
    private void getLocalidadesSrv(int idDepto) {
        EntidadCombo local;
        List<CentroPoblado> listSrv;
        
        try{
            // instancio el cliente para la selección de las Localidades
            deptoClient = new DepartamentoClient();
            // obtngo el listado
            GenericType<List<CentroPoblado>> gType = new GenericType<List<CentroPoblado>>() {};
            Response response = deptoClient.findByDepto_JSON(Response.class, String.valueOf(idDepto));
            listSrv = response.readEntity(gType);
            // lleno el listado de los combos
            lstLocalidades = new ArrayList<>();
            for(CentroPoblado loc : listSrv){
                local = new EntidadCombo(loc.getId().intValue(), loc.getNombre());
                lstLocalidades.add(local);
            }
            
            deptoClient.close();
        }catch(ClientErrorException ex){
            // muestro un mensaje al usuario
            JsfUtil.addErrorMessage("Hubo un error obteniendo los Centros poblados del Departamento seleccionado. " + ex.getMessage());
            LOG.fatal("Hubo un error obteniendo las Localidades por Departamento del servicio REST de centros poblados. " + ex.getMessage());
        }
    }  
    
    /**
     * Método para cargar los listados, para actualizar la Localidad del Puesto de control
     */
    private void cargarEntidadesTerr(int idLocalidad){
        CentroPoblado cp;
        
        try{
            // instancio el cliente para la selección de las provincias
            localClient = new LocalidadClient();
            cp = localClient.find_JSON(CentroPoblado.class, String.valueOf(idLocalidad));
            // cierro el cliente
            localClient.close();
            // instancio los id territoriales
            idDeptoGt = cp.getDepartamento().getId().intValue();
            idProvGt = cp.getDepartamento().getProvincia().getId().intValue();
            // cargo el listado de provincias
            cargarProvincias();
            // cargo el listado de Departamentos
            getDepartamentosSrv(idProvGt);
            // cargo el listado de Localidades
            getLocalidadesSrv(idDeptoGt);

        }catch(ClientErrorException ex){
            // muestro un mensaje al usuario
            JsfUtil.addErrorMessage("Hubo un error obteniendo los datos territoriales de la Localidad de ubicación del Puesto de control. " + ex.getMessage());
            LOG.fatal("Hubo un error obteniendo el Centro poblado por id desde el servicio REST de centros poblados. " + ex.getMessage());
        }
    }     

    /**
     * Método para validar los datos obligatorios
     * @return 
     */
    private String validarDatos() {
        String mensaje = "";
        if(puestoControl.getNombre() == null){
            mensaje = "Debe ingresar un Nombre.";
        }
        if(puestoControl.getRuta() == null){
            if(mensaje.equals("")){
                mensaje = "Debe ingresar una Ruta.";
            }else{
                mensaje = mensaje + " " + "Debe ingresar una Ruta.";
            }
        }
        if(puestoControl.getAltura() == null){
            if(mensaje.equals("")){
                mensaje = "Debe ingresar una Altura.";
            }else{
                mensaje = mensaje + " " + "Debe ingresar una Altura.";
            } 
        }
        if(idLocGt == 0){
            if(mensaje.equals("")){
                mensaje = "Debe seleccionar una Localidad.";
            }else{
                mensaje = mensaje + " " + "Debe seleccionar una Localidad.";
            } 
        }
        
        return mensaje;
    }
}
