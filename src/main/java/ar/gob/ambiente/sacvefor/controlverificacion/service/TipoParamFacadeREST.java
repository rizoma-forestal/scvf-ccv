
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.TipoParam;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.TipoParamFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Servicio que implementa los métodos expuestos por la API REST para la entidad TipoParam
 * @author rincostante
 */
@Stateless
@Path("tiposparam")
public class TipoParamFacadeREST {

    @EJB
    private TipoParamFacade tipoFacade;
    
    /**
     * Método para obtener el Tipo de Paramétrica correspondiente al id recibido
     * Ej: [PATH]/tiposparam/1
     * @param id
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TipoParam find(@PathParam("id") Long id) {
        return tipoFacade.find(id);
    }

    /**
     * Método que retorna todos los Tipos de Paramétricas registrados
     * Ej: [PATH]/tiposparam
     * @return 
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TipoParam> findAll() {
        return tipoFacade.findAll();
    }

    /**
     * Método que obtiene un listado de Tipos de Paramétricas cuyos id se encuentran entre los parámetros de inicio y fin recibidos
     * Ej: [PATH]/tiposparam/1/10
     * @param from
     * @param to
     * @return 
     */
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TipoParam> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return tipoFacade.findRange(new int[]{from, to});
    }

    /**
     * Método que devuelve un entero con la totalidad de los Tipos de Paramétricas registrados
     * Ej: [PATH]/puestoscontrol/count
     * @return 
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(tipoFacade.count());
    }
}
