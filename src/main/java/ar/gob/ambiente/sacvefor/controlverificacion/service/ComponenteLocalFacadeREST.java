
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.ComponenteLocal;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.ComponenteLocalFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Servicio que implementa los métodos expuestos por la API REST para la entidad ComponenteLocal
 * @author rincostante
 */
@Stateless
@Path("componenteslocales")
public class ComponenteLocalFacadeREST {
    
    @EJB
    private ComponenteLocalFacade compLocFacade;

    /**
     * Método para obtener el Componente Local correspondiente al id recibido
     * Ej: [PATH]/componenteslocales/1
     * @param id
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ComponenteLocal find(@PathParam("id") Long id) {
        return compLocFacade.find(id);
    }

    /**
     * Método que retorna todos los Componentes Locales registrados
     * Ej: [PATH]/componenteslocales
     * @return 
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ComponenteLocal> findAll() {
        return compLocFacade.findAll();
    }

    /**
     * Método que obtiene un listado de Componentes locales cuyos id se encuentran entre los parámetros de inicio y fin recibidos
     * Ej: [PATH]/componenteslocales/1/10
     * @param from
     * @param to
     * @return 
     */
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ComponenteLocal> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return compLocFacade.findRange(new int[]{from, to});
    }

    /**
     * Método que devuelve un entero con la totalidad de los Componentes locales registradas
     * Ej: [PATH]/componenteslocales/count
     * @return 
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(compLocFacade.count());
    }
}
