
package ar.gob.ambiente.sacvefor.controlverificacion.mb;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.Parametrica;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.TipoParam;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.ParametricaFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.TipoParamFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.util.EntidadCombo;
import ar.gob.ambiente.sacvefor.controlverificacion.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

/**
 * Bean de respaldo para la gestión de entidades paramétricas
 * TipoParametro: Caracterización de cada una de las enitdades paramétricas registradas
 * Parametrica: Entidad paramétrica a utilizar por las entidades principales del modelo
 * @author rincostante
 */
public class MbParametrica implements Serializable{

    private TipoParam tipoParam;
    private List<TipoParam> lstTipoParam;
    private Parametrica parametrica;
    private List<Parametrica> lstParam;
    private List<EntidadCombo> lstTipoParamEC;
    private int idTipoParam;
    private boolean view;
    private boolean edit;
    
    @EJB
    private ParametricaFacade paramFacade;
    @EJB
    private TipoParamFacade tipoParamFacade;    
    
    public MbParametrica() {
    }

    public int getIdTipoParam() {
        return idTipoParam;
    }

    public void setIdTipoParam(int idTipoParam) {
        this.idTipoParam = idTipoParam;
    }

    public List<TipoParam> getLstTipoParam() {
        lstTipoParam = tipoParamFacade.findAll();
        return lstTipoParam;
    }

    public void setLstTipoParam(List<TipoParam> lstTipoParam) {
        this.lstTipoParam = lstTipoParam;
    }

    public List<Parametrica> getLstParam() {
        lstParam = paramFacade.findAll();
        return lstParam;
    }

    public void setLstParam(List<Parametrica> lstParam) {
        this.lstParam = lstParam;
    }

    public List<EntidadCombo> getLstTipoParamEC() {
        cargarTipoParam();
        return lstTipoParamEC;
    }

    public void setLstTipoParamEC(List<EntidadCombo> lstTipoParamEC) {
        this.lstTipoParamEC = lstTipoParamEC;
    }

    public TipoParam getTipoParam() {
        return tipoParam;
    }

    public void setTipoParam(TipoParam tipoParam) {
        this.tipoParam = tipoParam;
    }

    public Parametrica getParametrica() {
        return parametrica;
    }

    public void setParametrica(Parametrica parametrica) {
        this.parametrica = parametrica;
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
        tipoParam = new TipoParam();
        parametrica = new Parametrica();
    }    
    
    /***********************
     * Mátodos operativos **
     ***********************/

    /**
     * Método para guardar la Parametrica, sea inserción o edición.
     * Previa validación
     */
    public void saveParametrica(){
        boolean valida = true;
        if(parametrica.getNombre() != null && idTipoParam > 0){
            try{
                Parametrica paramExitente = paramFacade.getExistente(parametrica.getNombre().toUpperCase(), parametrica.getTipo());
                if(paramExitente != null){
                    if(parametrica.getId() != null){
                        // si edita, no habilito si no es el mismo
                        if(!paramExitente.equals(parametrica)) valida = false;
                    }else{
                        // si no edita no habilito de ninguna manera
                        valida = false;
                    }
                }
                if(valida){
                    // seteo el Tipo
                    TipoParam tp = tipoParamFacade.find(Long.valueOf(idTipoParam));
                    parametrica.setTipo(tp);
                    String tempNombre = parametrica.getNombre();
                    parametrica.setNombre(tempNombre.toUpperCase());
                    if(parametrica.getId() != null){
                        paramFacade.edit(parametrica);
                        JsfUtil.addSuccessMessage("El Parámetro fue guardado con exito");
                    }else{
                        parametrica.setHabilitado(true);
                        paramFacade.create(parametrica);
                        JsfUtil.addSuccessMessage("El Parámetro fue registrado con exito");
                    }  
                    edit = false;
                }else{
                    JsfUtil.addErrorMessage("El Parámetro que está tratando de persisitir ya existe, por favor verifique los datos ingresados.");
                }
                limpiarFormParam();
            }catch(Exception ex){
                JsfUtil.addErrorMessage("Hubo un error procesando el Parámetro: " + ex.getMessage());
            }   
        }else{
            if(parametrica.getNombre() == null) JsfUtil.addErrorMessage("Debe ingresar el nombre");
            if(idTipoParam <= 0) JsfUtil.addErrorMessage("Debe ingresar el Tipo");
        }
    }    
    
    /**
     * Método para guardar el TipoParam, sea inserción o edición.
     * Previa validación
     */
    public void saveTipoParam(){
        boolean valida = true;
        if(tipoParam.getNombre() != null){
            try{
                TipoParam tipoParamExitente = tipoParamFacade.getExistente(tipoParam.getNombre().toUpperCase());
                if(tipoParamExitente != null){
                    if(tipoParam.getId() != null){
                        // si edita, no habilito si no es el mismo
                        if(!tipoParamExitente.equals(tipoParam)) valida = false;
                    }else{
                        // si no edita no habilito de ninguna manera
                        valida = false;
                    }
                }
                if(valida){                    
                    String tempNombre = tipoParam.getNombre();
                    tipoParam.setNombre(tempNombre.toUpperCase());
                    if(tipoParam.getId() != null){
                        tipoParamFacade.edit(tipoParam);
                        JsfUtil.addSuccessMessage("El Tipo de Parámetro fue guardado con exito");
                    }else{
                        tipoParam.setHabilitado(true);
                        tipoParamFacade.create(tipoParam);
                        JsfUtil.addSuccessMessage("El Tipo de Parámetro fue registrado con exito");
                    }   
                    edit = false;
                }else{
                    JsfUtil.addErrorMessage("Tipo de Parámetro que está tratando de persisitir ya existe, por favor verifique los datos ingresados.");
                }
                limpiarFormTipoParam();
            }catch(Exception ex){
                JsfUtil.addErrorMessage("Hubo un error procesando el Tipo de Parámetro: " + ex.getMessage());
            }
        }else{
            JsfUtil.addErrorMessage("Debe completar el nombre");
        }
    }  
    
    /**
     * Método para deshabilitar un Parámetro
     * No estará disponible en los listados correspondientes
     */
    public void deshabilitarParametrica(){
        view = false;
        edit = false;
        try{
            parametrica.setHabilitado(false);
            paramFacade.edit(parametrica);
            limpiarFormParam();
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error al deshabilitar el Parámetro: " + ex.getMessage());
        }
    }    
    
    /**
     * Método para deshabilitar un Tipo de parámetro
     * No estará disponible en los listados correspondientes
     */    
    public void deshabilitarTipoParam(){
        view = false;
        edit = false;
        try{
            tipoParam.setHabilitado(false);
            tipoParamFacade.edit(tipoParam);
            limpiarFormTipoParam();
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error al deshabilitar el Tipo de Parámetro: " + ex.getMessage());
        }
    }

    /**
     * Método para habilitar un Parámetro
     * Volverá a estar disponible en los listados
     */
    public void habilitarParametrica(){
        try{
            parametrica.setHabilitado(true);
            paramFacade.edit(parametrica);
            limpiarFormParam();
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error al habilitar el Parámetro: " + ex.getMessage());
        }
    }
    
    /**
     * Método para habilitar un Tipo de parámetro
     * Volverá a estar disponible en los listados
     */    
    public void habilitarTipoParam(){
        try{
            tipoParam.setHabilitado(true);
            tipoParamFacade.edit(tipoParam);
            limpiarFormTipoParam();
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error al habilitar el Tipo de Parámetro: " + ex.getMessage());
        }
    }

    /**
     * Método para limpiar el objeto Parametrica del formulario correspondiente
     */
    public void limpiarFormParam(){
        parametrica = new Parametrica();
    }    

    /**
     * Método para limpiar el objeto TipoParam del formulario correspondiente
     */
    public void limpiarFormTipoParam(){
        tipoParam = new TipoParam();
    }   
    
    /**
     * Método para habilitar el formulario de creación
     * de Tipos de Parámetros
     */
    public void prepareNewTipoParam(){
        tipoParam = new TipoParam();
        view = false;
        edit = true;
    }    
    
    /**
     * Método para habilitar el formulario de creación
     * de Entidades paramétricas
     */
    public void prepareNewEntParam(){
        parametrica = new Parametrica();
        view = false;
        edit = true;   
    }    
    
    /**
     * Método para habilitar el formulario de edición
     * para cualquier caso
     */
    public void prepareEdit(){
        if(parametrica.getId() != null){
            // si estoy editando una Paramétrica seteo el idTipoParam
            idTipoParam = parametrica.getTipo().getId().intValue();
        }
        view = false;
        edit = true;   
    }
    
    /**
     * Método para habilitar el formulario de vista detalle 
     * para cualquier caso
     */
    public void prepareView(){
        view = true;
        edit = false;  
    }    
    
    /*********************
     * Métodos privados **
     *********************/    

    /**
     * Método que devuelve todas los TipoParam y los setea en un listado de EntidadCombo para su selección
     */
    private void cargarTipoParam() {
        List<TipoParam> lstTpPrm;
        // seteo los resultados
        try{
            lstTpPrm = tipoParamFacade.getHabilitados();
            this.lstTipoParamEC = new ArrayList<>();
            for(TipoParam tp : lstTpPrm){
                EntidadCombo entSrv = new EntidadCombo(); 
                entSrv.setId(tp.getId().intValue());
                entSrv.setNombre(tp.getNombre());
                this.lstTipoParamEC.add(entSrv);
            }
        }catch(Exception ex){
            JsfUtil.addErrorMessage("Hubo un error obteniendo las Entidades Paramétricas" + ex.getMessage());
        }
    }
}
