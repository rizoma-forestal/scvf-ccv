
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import ar.gob.ambiente.sacvefor.controlverificacion.annotation.Secured;
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
     * @api {get} /puestoscontrol/:id Ver un Componente local según su id
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/puestoscontrol/2 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetPuestoControl
     * @apiGroup PuestoControl
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador único del PuestoControl
     * @apiDescription Método para obtener un Puesto de Control existente según el id remitido.
     * Obtiene el puesto de control mediante el método local find(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.PuestoControl} PuestoControl Detalle del puesto de control registrado.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": "2",
     *          "departamento": "SUSQUES",
     *          "descripcion": "Puesto de prueba",
     *          "habilitado": "true",
     *          "idlocgt": "9967",
     *          "localidad": "MINA PROVIDENCIA",
     *          "nombre": "Segundo",
     *          "provincia": "JUJUY",
     *          "altura": "Km 27",
     *          "ruta": "128"
     *     }
     * @apiError PuestoControlNotFound No existe Puesto de control registrado con ese id.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay Puesto de control registrado con el id recibido"
     *     }
     */   
    @GET
    @Path("{id}")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public PuestoControl find(@PathParam("id") Long id) {
        return puestoFacade.find(id);
    }

    /**
     * @api {get} /puestoscontrol Ver todas los Puestos de control
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/puestoscontrol -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetPuestosControl
     * @apiGroup PuestoControl
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiDescription Método para obtener un listado de los Puestos de control existentes.
     * Obtiene los Puestos de control mediante el método local findAll()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.PuestoControl} PuestoControl Listado con todas los puestos de control registrados.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "puestosControl": [
     *          {"id": "1",
     *          "departamento": "COMUNA 1",
     *          "descripcion": "Puesto de prueba",
     *          "habilitado": "true",
     *          "idlocgt": "10809",
     *          "localidad": "RETIRO",
     *          "nombre": "Puesto 1",
     *          "provincia": "CIUDAD AUTONOMA DE BUENOS AIRES",
     *          "altura": "Km 1",
     *          "ruta": "1"},
     *          {"id": "2",
     *          "departamento": "SUSQUES",
     *          "descripcion": "Puesto de prueba",
     *          "habilitado": "true",
     *          "idlocgt": "9967",
     *          "localidad": "MINA PROVIDENCIA",
     *          "nombre": "Segundo",
     *          "provincia": "JUJUY",
     *          "altura": "Km 27",
     *          "ruta": "128"}
     *       ]
     *     }
     * @apiError PuestosConrolNotFound No existen puestos de control registrados.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay puestos de control registrados"
     *     }
     */ 
    @GET
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<PuestoControl> findAll() {
        return puestoFacade.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<PuestoControl> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return puestoFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(puestoFacade.count());
    }
}
