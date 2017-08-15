
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.Usuario;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.UsuarioFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Servicio que implementa los métodos expuestos por la API REST para la entidad Usuario
 * @author rincostante
 */
@Stateless
@Path("usuarios")
public class UsuarioFacadeREST {

    @EJB
    private UsuarioFacade usFacade;

    /**
     * Método para obtener el Usuario correspondiente al id recibido
     * Ej: [PATH]/usuarios/1
     * @param id
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Usuario find(@PathParam("id") Long id) {
        return usFacade.find(id);
    }

    /**
     * Método que retorna todos los Usuarios registrados
     * Ej: [PATH]/usuarios
     * @return 
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findAll() {
        return usFacade.findAll();
    }

    /**
     * Método que obtiene un listado de Usuarios cuyos id se encuentran entre los parámetros de inicio y fin recibidos
     * Ej: [PATH]/usuarios/1/10
     * @param from
     * @param to
     * @return 
     */
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return usFacade.findRange(new int[]{from, to});
    }

    /**
     * Método que devuelve un entero con la totalidad de los Usuarios registrados
     * Ej: [PATH]/usuarios/count
     * @return 
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(usFacade.count());
    }
}
