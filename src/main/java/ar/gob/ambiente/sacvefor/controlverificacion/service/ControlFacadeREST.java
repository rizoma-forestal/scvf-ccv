
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.Control;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Parametrica;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.TipoParam;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.ControlFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.ParametricaFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.PuestoControlFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.TipoParamFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Servicio que implementa los métodos expuestos por la API REST para la entidad Control
 * @author rincostante
 */
@Stateless
@Path("controles")
public class ControlFacadeREST {
    
    @EJB
    private ControlFacade controlFacade;
    @EJB
    private TipoParamFacade tipoParamFacade;
    @EJB
    private ParametricaFacade paramFacade;

    /**
     * Método para obtener el Control correspondiente al id recibido
     * Ej: [PATH]/controles/1
     * @param id
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Control find(@PathParam("id") Long id) {
        return controlFacade.find(id);
    }

    /**
     * Método que retorna todos los Controles registrados
     * Ej: [PATH]/controles
     * @return 
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Control> findAll() {
        return controlFacade.findAll();
    }
    
    /**
     * Método que retorna los Controles según el parámetro de consulta
     * Solo podrá ser uno a la vez
     * @param codGuia : Código de la Guía cuyos Controles se buscan
     * @param resultado : Resultado correspondiente a los Controles buscados
     * @param idLoc : Id de la Localidad (En la API de Gestion Territorial) dentro de la cual se realizaron los Controles buscados
     * Ej: [PATH]/controles/query?codGuia=TT-1238-2017
     * @return 
     */
    @GET
    @Path("/query")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Control> findByQuery(@QueryParam("codGuia") String codGuia, @QueryParam("resultado") String resultado, @QueryParam("idLoc") Long idLoc) {
        List<Control> result = new ArrayList<>();
        if(codGuia != null){
            result = controlFacade.getByCodGuia(codGuia);
        }else if(resultado != null){
            // obtengo la Paramétrica correspondiente al Resultado
            Parametrica paramResult = obtenerParametro(ResourceBundle.getBundle("/Config").getString("TipoResult"), resultado);
            result = controlFacade.getByResultado(paramResult);
        }else if(idLoc != null){
            result = controlFacade.getByIdLocGt(idLoc);
        }
        return result;
    }      

    /**
     * Método que obtiene un listado de Controles cuyos id se encuentran entre los parámetros de inicio y fin recibidos
     * Ej: [PATH]/controles/1/10
     * @param from
     * @param to
     * @return 
     */
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Control> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return controlFacade.findRange(new int[]{from, to});
    }

    /**
     * Método que devuelve un entero con la totalidad de los Controles realizados
     * Ej: [PATH]/controles/count
     * @return 
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(controlFacade.count());
    }
    
    /**
     * Método para obtener una Paramétrica según su nombre y nombre del Tipo
     * @param nomTipo : nombre del Tipo de Paramétrica
     * @param nomParam : nombre de la Paramétrica
     * @return 
     */
    private Parametrica obtenerParametro(String nomTipo, String nomParam) {
        TipoParam tipo = tipoParamFacade.getExistente(nomTipo);
        return paramFacade.getExistente(nomParam, tipo);
    }       
}
