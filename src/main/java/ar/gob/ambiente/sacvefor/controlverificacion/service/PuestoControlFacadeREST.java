
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import ar.gob.ambiente.sacvefor.controlverificacion.entities.PuestoControl;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.PuestoControlFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Servicio que implementa los métodos expuestos por la API REST para la entidad PuestoControl
 * @author rincostante
 */
@Stateless
@Path("puestoscontrol")
public class PuestoControlFacadeREST {

    @EJB
    private PuestoControlFacade puestoFacade;

    /**
     * Método para obtener el Puesto de Control correspondiente al id recibido
     * Ej: [PATH]/puestoscontrol/1
     * @param id
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public PuestoControl find(@PathParam("id") Long id) {
        return puestoFacade.find(id);
    }

    /**
     * Método que retorna todos los Puestos de Control registrados
     * Ej: [PATH]/puestoscontrol
     * @return 
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<PuestoControl> findAll() {
        return puestoFacade.findAll();
    }

    /**
     * Método que obtiene un listado de Puestos de Control cuyos id se encuentran entre los parámetros de inicio y fin recibidos
     * Ej: [PATH]/puestoscontrol/1/10
     * @param from
     * @param to
     * @return 
     */
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<PuestoControl> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return puestoFacade.findRange(new int[]{from, to});
    }

    /**
     * Método que devuelve un entero con la totalidad de los Puestos de Control registrados
     * Ej: [PATH]/puestoscontrol/count
     * @return 
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(puestoFacade.count());
    }
}
