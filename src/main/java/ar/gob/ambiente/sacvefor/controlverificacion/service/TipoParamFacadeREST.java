
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import ar.gob.ambiente.sacvefor.controlverificacion.annotation.Secured;
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
     * @api {get} /tiposparam/:id Ver un TipoParam
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/tiposparam/2 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetTipoParam
     * @apiGroup TipoParam
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador único del tipo de paramétrica
     * @apiDescription Método para obtener un TipoParam existente según el id remitido.
     * Obtiene el tipo de paramétrica mediante el método local find(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.TipoParam} TipoParam Detalle del tipo de paramétrica registrado.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *      {
     *          "id": "2",
     *          "habilitado": "true",
     *          "nombre": "EST_GUIAS",
     *      }
     *     }
     * @apiError TipoParamNotFound No existe tipo de paramétrica registrada con ese id.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay tipo de paramétrica registrada con el id recibido"
     *     }
     */     
    @GET
    @Path("{id}")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TipoParam find(@PathParam("id") Long id) {
        return tipoFacade.find(id);
    }

    /**
     * @api {get} /tiposparam Ver todos los tipos de paramétricas
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/tiposparam -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetTipoParams
     * @apiGroup TipoParam
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiDescription Método para obtener un listado de los tipos de paramétricas existentes.
     * Obtiene los tipos de paramétricas mediante el método local findAll()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.TipoParam} TipoParam Listado con todas los tipos de paramétricas registrados.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *      {"tipoParams": [
     *          {"id": "2",
     *          "habilitado": "true",
     *          "nombre": "EST_GUIAS"},
     *          {"id": "3",
     *          "nombre": "PARAM_CONTROL"}
     *      ]
     *     }
     * @apiError TipoParamsNotFound No existen tipos de paramétricas registrados.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay tipos de paramétricas registrados"
     *     }
     */       
    @GET
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TipoParam> findAll() {
        return tipoFacade.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<TipoParam> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return tipoFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(tipoFacade.count());
    }
}
