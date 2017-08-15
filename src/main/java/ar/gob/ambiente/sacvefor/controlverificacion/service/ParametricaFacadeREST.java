
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.Parametrica;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.TipoParam;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.ParametricaFacade;
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
 * Servicio que implementa los métodos expuestos por la API REST para la entidad Paramétrica
 * @author rincostante
 */
@Stateless
@Path("parametricas")
public class ParametricaFacadeREST {

    @EJB
    private ParametricaFacade paramFacade;
    @EJB
    private TipoParamFacade tipoParamFacade;

    /**
     * Método para obtener la Parametrica correspondiente al id recibido
     * Ej: [PATH]/parametricas/1
     * @param id
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Parametrica find(@PathParam("id") Long id) {
        return paramFacade.find(id);
    }

    /**
     * Método que retorna todos las Parametricas registradas
     * Ej: [PATH]/parametricas
     * @return 
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Parametrica> findAll() {
        return paramFacade.findAll();
    }
    
    /**
     * Método que devuelve las Pramétricas según el tipo y el nombre recibidos como parámetros
     * Deberán ingresarse ambos parámetros
     * @param tipoParam
     * @param nombre
     * @return 
     */
    @GET
    @Path("/query")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Parametrica> findByQuery(@QueryParam("tipoParam") String tipoParam, @QueryParam("nombre") String nombre) {
        List<Parametrica> result = new ArrayList<>();
        if(nombre != null){
            Parametrica parmEstado = obtenerParametro(tipoParam, nombre);
            result.add(parmEstado);
        }
        return result;
    }       

    /**
     * Método que obtiene un listado de Paramétricas cuyos id se encuentran entre los parámetros de inicio y fin recibidos
     * Ej: [PATH]/parametricas/1/10
     * @param from
     * @param to
     * @return 
     */
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Parametrica> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return paramFacade.findRange(new int[]{from, to});
    }

    /**
     * Método que devuelve un entero con la totalidad de las Paramétricas registradas
     * Ej: [PATH]/parametricas/count
     * @return 
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(paramFacade.count());
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
