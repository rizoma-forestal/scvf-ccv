
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import ar.gob.ambiente.sacvefor.controlverificacion.annotation.Secured;
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
 * Se denomína así a los componentes de gestión de cada provincia
 * @author rincostante
 */
@Stateless
@Path("componenteslocales")
public class ComponenteLocalFacadeREST {
    
    @EJB
    private ComponenteLocalFacade compLocFacade;

    /**
     * @api {get} /componenteslocales/:id Ver un Componente local según su id
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/componenteslocales/3 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetComponenteLocal
     * @apiGroup ComponenteLocal
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador único del ComponenteLocal
     * @apiDescription Método para obtener un ComponenteLocal existente según el id remitido.
     * Obtiene el componente local mediante el método local find(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.ComponenteLocal} ComponenteLocal Detalle del componente local registrado.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *          {"id": "3",
     *          "correoelectronico": "componente@[provincia].gob.ar",
     *          "habilitado": "true",
     *          "provincia": "SANTIAGO DEL ESTERO",
     *          "url": "[server]/cgl-santiago/rest",
     *          "idprovgt": "22"}
     *     }
     * @apiError ComponenteLocalNotFound No existe componente local registrado con ese id.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay componente local registrado con el id recibido"
     *     }
     */        
    @GET
    @Path("{id}")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ComponenteLocal find(@PathParam("id") Long id) {
        return compLocFacade.find(id);
    }

    /**
     * @api {get} /componenteslocales Ver todos los Componentes locales
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/componenteslocales -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetComponentesLocales
     * @apiGroup ComponenteLocal
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiDescription Método para obtener un listado de los componentes locales existentes.
     * Obtiene los componentes locales mediante el método local findAll()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.ComponenteLocal} ComponenteLocal Listado con todas los componentes locales registrados.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "parametricas": [
     *          {"id": "3",
     *          "correoelectronico": "componente@[provincia].gob.ar",
     *          "habilitado": "true",
     *          "provincia": "SANTIAGO DEL ESTERO",
     *          "url": "[server]/cgl-santiago/rest",
     *          "idprovgt": "22"},
     *          {"id": "2",
     *          "correoelectronico": "componente@[provincia].gob.ar",
     *          "habilitado": "true",
     *          "provincia": "JUJUY",
     *          "url": "[server]/cgl-jujuy/rest",
     *          "idprovgt": "10"},
     *          {"id": "4",
     *          "correoelectronico": "componente@[provincia].gob.ar",
     *          "habilitado": "true",
     *          "provincia": "SALTA",
     *          "url": "[server]/cgl-salta/rest",
     *          "idprovgt": "17"}
     *       ]
     *     }
     * @apiError ComponentesLocalesNotFound No existen componentes locales registrados.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay componentes locales registrados"
     *     }
     */      
    @GET
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ComponenteLocal> findAll() {
        return compLocFacade.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ComponenteLocal> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return compLocFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(compLocFacade.count());
    }
}
