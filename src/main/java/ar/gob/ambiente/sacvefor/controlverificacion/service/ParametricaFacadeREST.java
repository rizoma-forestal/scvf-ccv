
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import ar.gob.ambiente.sacvefor.controlverificacion.annotation.Secured;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Parametrica;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.TipoParam;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.ParametricaFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.TipoParamFacade;
import java.util.ArrayList;
import java.util.List;
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
     * @api {get} /parametricas/:id Ver una Parametrica
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/parametricas/2 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetParametrica
     * @apiGroup Parametrica
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador único de la Parametrica
     * @apiDescription Método para obtener una Parametrica existente según el id remitido.
     * Obtiene la paramétrica mediante el método local find(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.Parametrica} Parametrica Detalle de la paramétrica registrada.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *      {
     *          "id": "2",
     *          "nombre": "OPERATIVO",
     *          "tipo": {
     *              "id": "1",
     *              "nombre": "ROL_USUARIOS"
     *          }
     *      }
     *     }
     * @apiError ParametricaNotFound No existe paramétrica registrada con ese id.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay paramétrica registrada con el id recibido"
     *     }
     */         
    @GET
    @Path("{id}")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Parametrica find(@PathParam("id") Long id) {
        return paramFacade.find(id);
    }

    /**
     * @api {get} /parametricas Ver todas las Parametricas
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/parametricas -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetParametricas
     * @apiGroup Parametrica
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiDescription Método para obtener un listado de las Parametricas existentes.
     * Obtiene las paramétricas mediante el método local findAll()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.Parametrica} Parametricas Listado con todas las Parametricas registradas.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "parametricas": [
     *          {"id": "2",
     *          "nombre": "OPERATIVO",
     *          "tipo": {
     *              "id": "1",
     *              "nombre": "ROL_USUARIOS"
     *          },
     *          {"id": "3",
     *          "nombre": "VIGENTE",
     *          "tipo": {
     *              "id": "1",
     *              "nombre": "EST_GUIAS",
     *          }
     *       ]
     *     }
     * @apiError ParametricasNotFound No existen paramétricas registradas.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay paramétricas registradas"
     *     }
     */ 
    @GET
    @Secured
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
    
    /**
     * @api {get} /parametricas/query?tipoParam=:tipoParam,nombre=:nombre Ver Paramétricas según su nombre y tipo.
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/parametricas/query?tipoParam=EST_GUIAS -H "authorization: xXyYvWzZ"
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/parametricas/query?nombre=VALIDADA "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetParametricaQuery
     * @apiGroup Parametrica
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     }
     * @apiParam {String} tipoParam tipo de la Parametrica solicitada
     * @apiParam {String} nombre nombre de la Parametrica solicitada
     * @apiDescription Método para obtener la  paramétrica según su tipo y nombre.
     * Los dos parámetros deberán tener un valor asignado.
     * Obtiene la paramétrica con el método local obtenerParametro(String tipoParam, String nombre)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.Parametrica} Parametrica Paramétrica obtenida.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "parametrica": 
     *          {"id": "3",
     *          "nombre": "VIGENTE",
     *          "tipo": {
     *              "id": "1",
     *              "nombre": "EST_GUIAS",
     *          }
     *     }
     * @apiError ParametricaNotFound No existe paramétrica registrada con ese nombre y tipo.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay paramétrica registrada con con ese nombre y tipo"
     *     }
     */ 
    @GET
    @Path("/query")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Parametrica> findByQuery(@QueryParam("tipoParam") String tipoParam, @QueryParam("nombre") String nombre) {
        List<Parametrica> result = new ArrayList<>();
        if(nombre != null){
            Parametrica parmEstado = obtenerParametro(tipoParam, nombre);
            result.add(parmEstado);
        }
        return result;
    }       

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Parametrica> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return paramFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(paramFacade.count());
    }
    
    /**
     * Método para obtener una Paramétrica según su nombre y nombre del Tipo
     * @param nomTipo String nombre del Tipo de Paramétrica
     * @param nomParam String nombre de la Paramétrica
     * @return Parametrica paramétrica obtenida según el tipo y nombre remitidos
     */
    private Parametrica obtenerParametro(String nomTipo, String nomParam) {
        TipoParam tipo = tipoParamFacade.getExistente(nomTipo);
        return paramFacade.getExistente(nomParam, tipo);
    }      
}
