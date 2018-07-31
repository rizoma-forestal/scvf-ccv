
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import ar.gob.ambiente.sacvefor.controlverificacion.annotation.Secured;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.ComponenteLocal;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Guia;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Item;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Parametrica;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.TipoParam;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.ComponenteLocalFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.GuiaFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.ParametricaFacade;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.TipoParamFacade;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;

/**
 * Servicio que implementa los métodos expuestos por la API REST para la entidad Guia
 * @author rincostante
 */
@Stateless
@Path("guias")
public class GuiaFacadeREST {
    
    static final Logger LOG = Logger.getLogger(GuiaFacadeREST.class);
    
    @EJB
    private GuiaFacade guiaFacade;
    @EJB
    private ComponenteLocalFacade compLocalFacade;
    @EJB
    private TipoParamFacade tipoParamFacade;
    @EJB
    private ParametricaFacade paramFacade;    
    
    @Context
    UriInfo uriInfo;      

    /**
     * @api {post} /guias Registrar una Guia
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X POST -d [PATH_SERVER]/ctrlVerif/rest/guias -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PostGuia
     * @apiGroup Guia
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.ctrlverif.Guia} entity Objeto java del paquete paqControlVerif.jar con los datos de la Persona a registrar
     * @apiParamExample {java} Ejemplo de Guia
     *      {"entity":{
     *              "id": "0",   
     *              "codigo": "TT-0-00016-2017",
     *              "cuitdestino": "27451140609",
     *              "cuitorigen": "23181106199",
     *              "dniconductor": "19611874",
     *              "fechaalta": "2017-08-08 13:45:58.839",
     *              "fechaemision": "2017-08-08 13:45:55.34",
     *              "fechavencimiento": "2017-09-20 14:47:52.133",
     *              "matacoplado": "",
     *              "matvehiculo": "SEJ-659",
     *              "nombreconductor": "ORLANDO GOÑI",
     *              "nombredestino": "GONZALEZ MAIZ SA",
     *              "nombreorigen": "LAS CAPRICHOSAS SA",
     *              "numfuente": "EE-0-00022-2017",
     *              "tipo": "TRANSPORTE",
     *              "tipofuente": "GUIA MADRE",
     *              "codQr": "sdjkfnññNhbSdsf16666b8we",
     *              "componentelocal":{
     *                  "id": "3",
     *                  "correoelectronico": "componente@[provincia].gob.ar",
     *                  "habilitado": "true",
     *                  "provincia": "SANTIAGO DEL ESTERO",
     *                  "url": "[server]/cgl-santiago/rest",
     *                  "idprovgt": "22"
     *              }
     *          }
     *      }
     * @apiDescription Método para registrar una nueva Guia. Instancia una entidad a persistir Guia local y la crea mediante el método local create(Guia guia) 
     * @apiSuccess {String} Location url de acceso mediante GET al Guia registrada.
     * @apiSuccessExample Response exitosa:
     *     HTTP/1.1 201 OK
     *     {
     *       {
     *          "Location": "[PATH_SERVER]/ctrlVerif/rest/guias/:id"
     *       }
     *     }
     *
     * @apiError GuiaNoRegistrada No se registró la Guia.
     * @apiErrorExample Respuesta de Error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "Hubo un error procesando la inserción en el Componente de Trazabilidad"
     *     }
     */     
    @POST
    @Secured
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response create(ar.gob.ambiente.sacvefor.servicios.ctrlverif.Guia entity) {
        boolean valida = true;
        try{
            // instancio el listado de items
            List<Item> lstItemLocal = new ArrayList<>();
            Item itemLocal;
            for(ar.gob.ambiente.sacvefor.servicios.ctrlverif.Item itemApi : entity.getItems()){
                itemLocal = new Item();
                itemLocal.setCodigoOrigen(itemApi.getCodigoOrigen());
                itemLocal.setNombreCientifico(itemApi.getNombreCientifico());
                itemLocal.setNombreVulgar(itemApi.getNombreVulgar());
                itemLocal.setClase(itemApi.getClase());
                itemLocal.setUnidad(itemApi.getUnidad());
                itemLocal.setTotal(itemApi.getTotal());
                itemLocal.setTotalKg(itemApi.getTotalKg());
                lstItemLocal.add(itemLocal);
            }
            // instancio la Guía
            Guia guia = new Guia();
            // obtengo el componente local si es que existe
            if(entity.getCompLocal() != null){
                ComponenteLocal compLocal = compLocalFacade.getExistente(entity.getProvincia().toUpperCase());
                if(compLocal == null){
                    valida = false;
                }else{
                    guia.setCompLocal(compLocal);
                }
            }
            // obtengo el estado (VIGENTE)
            Parametrica paramEstado = obtenerParametro(ResourceBundle.getBundle("/Config").getString("EstadosGuia"), "VIGENTE");
            if(paramEstado == null){
                valida = false;
            }else{
                guia.setEstado(paramEstado);
            }
            
            if(valida){
                Date fechaAlta = new Date(System.currentTimeMillis());
                guia.setFechaAlta(fechaAlta);
                guia.setCodigo(entity.getCodigo());
                guia.setTipo(entity.getTipo());
                guia.setTipoFuente(entity.getTipoFuente());
                guia.setNumFuente(entity.getNumFuente());
                guia.setItems(lstItemLocal);
                guia.setNombreOrigen(entity.getNombreOrigen());
                guia.setCuitOrigen(entity.getCuitOrigen());
                guia.setLocOrigen(entity.getLocOrigen());
                guia.setNombreDestino(entity.getNombreDestino());
                guia.setCuitDestino(entity.getCuitDestino());
                guia.setLocDestino(entity.getLocDestino());
                guia.setMatVehiculo(entity.getMatVehiculo());
                guia.setMatAcoplado(entity.getMatAcoplado());
                guia.setNombreConductor(entity.getNombreConductor());
                guia.setDniConductor(entity.getDniConductor());
                guia.setFechaEmision(entity.getFechaEmision());
                guia.setFechaVencimiento(entity.getFechaVencimiento());
                // persisto
                guiaFacade.create(guia);
                // armo la respuesta exitosa
                UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
                URI uri = uriBuilder.path(guia.getId().toString()).build();
                return Response.created(uri).build();
            }else{
                // armo la respuesta de error
                LOG.fatal("Hubo un error procesando el método 'create' de la API CCV para la Guía " + entity.getCodigo() + ". No se pudo validar el Componente Local de la Guía");
                return Response.status(400).entity("Hubo un error procesado la inserción de la Guía. No se pudo validar el Componente Local.").build();
            }
        }catch(IllegalArgumentException | UriBuilderException ex){
            // armo la respuesta de error
            LOG.fatal("Hubo un error procesando el método 'create' de la API CCV para la Guía " + entity.getCodigo() + ". " + ex.getMessage());
            return Response.status(400).entity("Hubo un error procesado la inserción de la Guía. " + ex.getMessage()).build();
        }
    }

    /**
     * @api {put} /guias/:id Actualizar una Guía existente
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X PUT -d [PATH_SERVER]ctrlVerif/rest/guias/20 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName PutGuia
     * @apiGroup Guia
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {ar.gob.ambiente.sacvefor.servicios.ctrlverif.Guia} entity Objeto java del paquete paqControlVerif.jar con los datos de la Guia a actualizar
     * @apiParam {Long} Id Identificador único de la Guía a actualizar
     * @apiParamExample {java} Ejemplo de Guia
     *      {"entity":{
     *              "id": "20",   
     *              "items": [
     *                  {"clase": "ROLLO",
     *                  "codigoorigen": "36|Prosopis.nigra|ALGARROBO NEGRO|ROLLO|UNIDAD|005/0650/AFDA|[Provincia]",
     *                  "nombrecientifico": "Prosopis.nigra",
     *                  "nombrevulgar": "ALGARROBO NEGRO",
     *                  "total": "21",
     *                  "totalkg": "7507.5",
     *                  "unidad": "UNIDAD"},
     *                  {"clase": "POSTE",
     *                  "codigoorigen": "35|Anadenanthera.colubrina|HUILCO|POSTE|UNIDAD|005/0650/AFDA|[Provincia]",
     *                  "nombrecientifico": "Anadenanthera.colubrina",
     *                  "nombrevulgar": "HUILCO",
     *                  "total": "3",
     *                  "totalkg": "1275",
     *                  "unidad": "UNIDAD"},
     *                  {"clase": "ROLLO",
     *                  "codigoorigen": "41|Lonchocarpus lilloí|QUINA BLANCA|ROLLO|METRO CUBICO|214/2017/SAMA|[Provincia]",
     *                  "nombrecientifico": "Lonchocarpus lilloí",
     *                  "nombrevulgar": "QUINA BLANCA",
     *                  "total": "17",
     *                  "totalkg": "12750",
     *                  "unidad": "METRO CUBICO"}
     *              ],
     *              "codigo": "TT-10-00003-2017",
     *              "cuitdestino": "20141173612",
     *              "cuitorigen": "27031104663",
     *              "dniconductor": "15911856",
     *              "fechaalta": "2017-11-27 12:22:42.548",
     *              "fechaemision": "2017-11-27 12:22:29.734",
     *              "fechavencimiento": "2017-12-27 12:22:29.734",
     *              "matacoplado": "100-RTU-697",
     *              "matvehiculo": "LMG-987",
     *              "nombreconductor": "JACOPO BELBO",
     *              "nombredestino": "HERNANDEZ, PABLO ENRIQUE",
     *              "nombreorigen": "SILVESTRE, SUSANA ERNESTINA",
     *              "numfuente": "EE-10-00001-2017",
     *              "tipo": "REMITO",
     *              "tipofuente": "GUIA MADRE",
     *              "codQr": "sdjkfnññNhbSdsf16666b8we",
     *              "componentelocal":{
     *                  "id": "3",
     *                  "correoelectronico": "componente@[provincia].gob.ar",
     *                  "habilitado": "true",
     *                  "provincia": "SANTIAGO DEL ESTERO",
     *                  "url": "[server]/cgl-santiago/rest",
     *                  "idprovgt": "22"
     *              }
     *          }
     *      }
     * @apiParamExample {json} Emplo de id
     *      {
     *          "id": "20"
     *      }
     * @apiDescription Método para actualizar una Guía existente. Obtiene la Guía correspondiente al id recibido 
     * mediante el método local find(Long id), actualiza sus datos según los de la entidad recibida y la edita mediante 
     * el método local edit(Guia guia).
     * @apiSuccessExample Response exitosa:
     *     HTTP/1.1 200 OK
     *     {}
     * @apiError GuiaNoActualizada No se actualizó la Guía.
     * @apiErrorExample Respuesta de Error:
     *     HTTP/1.1 400 Not Modified
     *     {
     *       "error": "Hubo un error procesado la actualización en el Componente de Trazabilidad."
     *     }
     */       
    @PUT
    @Path("{id}")
    @Secured
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(@PathParam("id") Long id, ar.gob.ambiente.sacvefor.servicios.ctrlverif.Guia entity) {
        try{
            // obtengo la Guía
            Guia guia = guiaFacade.find(id);
            // verifico los cambios a actualizar
            if(!Objects.equals(guia.getEstado().getId(), entity.getEstado().getId())){
                // si hay cambio de estado obtengo y seteo el estado
                Parametrica paramEstado = paramFacade.find(entity.getEstado().getId());
                // actualizo la Guía con el Estado
                guia.setEstado(paramEstado);
                // si está cerrando la Guía, seteo la fecha de cierre.
                if(paramEstado.getNombre().equals(ResourceBundle.getBundle("/Config").getString("Cerrada"))){
                    // seteo la fecha de cierre
                    guia.setFechaCierre(entity.getFechaCierre());
                }
            }else if(!Objects.equals(guia.getCuitDestino(), entity.getCuitDestino())){
                // hay cambio de destino
                guia.setCuitDestino(entity.getCuitDestino());
                guia.setNombreDestino(entity.getNombreDestino());
                guia.setLocDestino(entity.getLocDestino());
                // verifico si hubo cambio de fecha de vencimiento
                if(guia.getFechaVencimiento().compareTo(entity.getFechaVencimiento()) != 0){
                    // actualizo
                    guia.setFechaVencimiento(entity.getFechaVencimiento());
                }
            }else if(entity.getFechaVencimiento().after(guia.getFechaVencimiento())){
                // si hay extensión de vigencia, actualizo el qr y la fecha de vencimiento
                guia.setFechaVencimiento(entity.getFechaVencimiento());
            }
            // actualizo
            guiaFacade.edit(guia);
            // armo la respuesta exitosa
            return Response.ok().build();
        }catch(IllegalArgumentException | UriBuilderException ex){
            // armo la respuesta de error
            return Response.notModified().entity("Hubo un error procesado la actualización en el Componente de Trazabilidad. " + ex.getMessage()).build();
        }             
    }    

    /**
     * @api {get} /guias/:id Ver una Guía según su id
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/guias/4 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetGuia
     * @apiGroup Guia
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador único de la Guía
     * @apiDescription Método para obtener una Guía existente según el id remitido.
     * Obtiene la Guía mediante el método local find(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.Guia} Guia Detalle de la Guía registrada.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *          {
     *              "id": "4",   
     *              "items": [
     *                  {"clase": "ROLLO",
     *                  "codigoorigen": "37|Pterogyne.nitens|TIPA COLORADA|ROLLO|METRO CUBICO|005/0650/AFDA|[Provincia]",
     *                  "nombrecientifico": "Pterogyne.nitens",
     *                  "nombrevulgar": "TIPA COLORADA",
     *                  "total": "21",
     *                  "totalkg": "17409",
     *                  "unidad": "METRO CUBICO"},
     *                  {"clase": "POSTE",
     *                  "codigoorigen": "35|Anadenanthera.colubrina|HUILCO|POSTE|UNIDAD|005/0650/AFDA|[Provincia]",
     *                  "nombrecientifico": "Anadenanthera.colubrina",
     *                  "nombrevulgar": "HUILCO",
     *                  "total": "13",
     *                  "totalkg": "5525",
     *                  "unidad": "UNIDAD"}
     *              ],
     *              "codigo": "TT-0-00016-2017",
     *              "cuitdestino": "27451140609",
     *              "cuitorigen": "23181106199",
     *              "dniconductor": "19611874",
     *              "fechaalta": "2017-08-08 13:45:58.839",
     *              "fechaemision": "2017-08-08 13:45:55.34",
     *              "fechavencimiento": "2017-09-20 14:47:52.133",
     *              "matacoplado": "",
     *              "matvehiculo": "SEJ-659",
     *              "nombreconductor": "SEBASTIAN PIANA",
     *              "nombredestino": "LA CACHILA SA",
     *              "nombreorigen": "CALLEJON SRL",
     *              "numfuente": "EE-0-00022-2017",
     *              "tipo": "TRANSPORTE",
     *              "tipofuente": "GUIA MADRE",
     *              "codQr": "sdjkfnññNhbSdsf16666b8we",
     *              "componentelocal":{
     *                  "id": "3",
     *                  "correoelectronico": "componente@[provincia].gob.ar",
     *                  "habilitado": "true",
     *                  "provincia": "SANTIAGO DEL ESTERO",
     *                  "url": "[server]/cgl-santiago/rest",
     *                  "idprovgt": "22"
     *              }
     *          }
     * @apiError GuiaNotFound No existe guía registrada con ese id.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay guía registrada con el id recibido"
     *     }
     */        
    @GET
    @Path("{id}")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Guia find(@PathParam("id") Long id) {
        return guiaFacade.find(id);
    }

    /**
     * @api {get} /guias Ver todas las Guías
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/guias -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetGuias
     * @apiGroup Guia
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiDescription Método para obtener un listado de las guias existentes.
     * Obtiene las guias mediante el método local findAll()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.Guia} Guia Listado con todas las Guías registradas.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "guias": [
     *          {"id": "4",   
     *              "items": [
     *                  {"clase": "ROLLO",
     *                  "codigoorigen": "37|Pterogyne.nitens|TIPA COLORADA|ROLLO|METRO CUBICO|005/0650/AFDA|[Provincia]",
     *                  "nombrecientifico": "Pterogyne.nitens",
     *                  "nombrevulgar": "TIPA COLORADA",
     *                  "total": "21",
     *                  "totalkg": "17409",
     *                  "unidad": "METRO CUBICO"},
     *                  {"clase": "POSTE",
     *                  "codigoorigen": "35|Anadenanthera.colubrina|HUILCO|POSTE|UNIDAD|005/0650/AFDA|[Provincia]",
     *                  "nombrecientifico": "Anadenanthera.colubrina",
     *                  "nombrevulgar": "HUILCO",
     *                  "total": "13",
     *                  "totalkg": "5525",
     *                  "unidad": "UNIDAD"}
     *              ],
     *              "codigo": "TT-0-00016-2017",
     *              "cuitdestino": "27451140609",
     *              "cuitorigen": "23181106199",
     *              "dniconductor": "19611874",
     *              "fechaalta": "2017-08-08 13:45:58.839",
     *              "fechaemision": "2017-08-08 13:45:55.34",
     *              "fechavencimiento": "2017-09-20 14:47:52.133",
     *              "matacoplado": "",
     *              "matvehiculo": "SEJ-659",
     *              "nombreconductor": "SEBASTIAN PIANA",
     *              "nombredestino": "LA CACHILA SA",
     *              "nombreorigen": "CALLEJON SRL",
     *              "numfuente": "EE-0-00022-2017",
     *              "tipo": "TRANSPORTE",
     *              "tipofuente": "GUIA MADRE",
     *              "codQr": "sdjkfnññNhbSdsf16666b8we",
     *              "componentelocal":{
     *                  "id": "3",
     *                  "correoelectronico": "componente@[provincia].gob.ar",
     *                  "habilitado": "true",
     *                  "provincia": "SANTIAGO DEL ESTERO",
     *                  "url": "[server]/cgl-santiago/rest",
     *                  "idprovgt": "22"
     *              }
     *          },
     *          {
     *              "id": "20",   
     *              "codigo": "TT-10-00003-2017",
     *              "cuitdestino": "20141173612",
     *              "cuitorigen": "27031104663",
     *              "dniconductor": "15911856",
     *              "fechaalta": "2017-11-27 12:22:42.548",
     *              "fechaemision": "2017-11-27 12:22:29.734",
     *              "fechavencimiento": "2017-12-27 12:22:29.734",
     *              "matacoplado": "100-RTU-697",
     *              "matvehiculo": "LMG-987",
     *              "nombreconductor": "JACOPO BELBO",
     *              "nombredestino": "HERNANDEZ, PABLO ENRIQUE",
     *              "nombreorigen": "SILVESTRE, SUSANA ERNESTINA",
     *              "numfuente": "EE-10-00001-2017",
     *              "tipo": "REMITO",
     *              "tipofuente": "GUIA MADRE",
     *              "codQr": "sdjkfnññNhbSdsf16666b8we",
     *              "componentelocal":{
     *                  "id": "3",
     *                  "correoelectronico": "componente@[provincia].gob.ar",
     *                  "habilitado": "true",
     *                  "provincia": "SANTIAGO DEL ESTERO",
     *                  "url": "[server]/cgl-santiago/rest",
     *                  "idprovgt": "22"
     *              }
     *          }
     *       ]
     *     }
     * @apiError GuiasNotFound No existen Guías registradas.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay guías registradas"
     *     }
     */  
    @GET
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Guia> findAll() {
        return guiaFacade.findAll();
    }

    /**
     * @api {get} /guias/query?codigo=:codigo,matricula=:matricula,provincia=:provincia Ver Guías según parámetros.
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/guias/query?codigo=TT-10-00003-2017 -H "authorization: xXyYvWzZ"
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/guias/query?matricula=LMG-987 -H "authorization: xXyYvWzZ"
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/guias/query?provincia=JUJUY -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetGuiaQuery
     * @apiGroup Guia
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     }
     * @apiParam {String} codigo Código de la Guía solicitada
     * @apiParam {String} matricula Matrícula del vehículo de transporte de los productos amparados por las Guía buscadas
     * @apiParam {String} provincia Nombre de la Provincia emisora de las Guías buscadas
     * @apiDescription Método para obtener la/s  guía/s según un código, una matrícula de vehículo de tansporte o una Provincia emisora.
     * Solo uno de los parámetros tendrá un valor y los restantes nulos.
     * Según el caso, obtiene las guías en cuestión con los métodos locales getExistente(String codigo), getByVehiculo(String matricula) o getByProvincia(String provincia)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.Guia} Guia Guía o listado de las Guías obtenidas.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "guias": [
     *          {
     *              "id": "20", 
     *              "items": [
     *                  {"clase": "ROLLO",
     *                  "codigoorigen": "36|Prosopis.nigra|ALGARROBO NEGRO|ROLLO|UNIDAD|005/0650/AFDA|[Provincia]",
     *                  "nombrecientifico": "Prosopis.nigra",
     *                  "nombrevulgar": "ALGARROBO NEGRO",
     *                  "total": "21",
     *                  "totalkg": "7507.5",
     *                  "unidad": "UNIDAD"},
     *                  {"clase": "POSTE",
     *                  "codigoorigen": "35|Anadenanthera.colubrina|HUILCO|POSTE|UNIDAD|005/0650/AFDA|[Provincia]",
     *                  "nombrecientifico": "Anadenanthera.colubrina",
     *                  "nombrevulgar": "HUILCO",
     *                  "total": "3",
     *                  "totalkg": "1275",
     *                  "unidad": "UNIDAD"},
     *                  {"clase": "ROLLO",
     *                  "codigoorigen": "41|Lonchocarpus lilloí|QUINA BLANCA|ROLLO|METRO CUBICO|214/2017/SAMA|[Provincia]",
     *                  "nombrecientifico": "Lonchocarpus lilloí",
     *                  "nombrevulgar": "QUINA BLANCA",
     *                  "total": "17",
     *                  "totalkg": "12750",
     *                  "unidad": "METRO CUBICO"}
     *              ],
     *              "codigo": "TT-10-00003-2017",
     *              "cuitdestino": "20141173612",
     *              "cuitorigen": "27031104663",
     *              "dniconductor": "15911856",
     *              "fechaalta": "2017-11-27 12:22:42.548",
     *              "fechaemision": "2017-11-27 12:22:29.734",
     *              "fechavencimiento": "2017-12-27 12:22:29.734",
     *              "matacoplado": "100-RTU-697",
     *              "matvehiculo": "LMG-987",
     *              "nombreconductor": "JACOPO BELBO",
     *              "nombredestino": "HERNANDEZ, PABLO ENRIQUE",
     *              "nombreorigen": "SILVESTRE, SUSANA ERNESTINA",
     *              "numfuente": "EE-10-00001-2017",
     *              "tipo": "REMITO",
     *              "tipofuente": "GUIA MADRE",
     *              "codQr": "sdjkfnññNhbSdsf16666b8we",
     *              "componentelocal":{
     *                  "id": "3",
     *                  "correoelectronico": "componente@[provincia].gob.ar",
     *                  "habilitado": "true",
     *                  "provincia": "SANTIAGO DEL ESTERO",
     *                  "url": "[server]/cgl-santiago/rest",
     *                  "idprovgt": "22"
     *              }
     *          },
     *          {
     *              "id": "4", 
     *              "items": [
     *                  {"clase": "ROLLO",
     *                  "codigoorigen": "37|Pterogyne.nitens|TIPA COLORADA|ROLLO|METRO CUBICO|005/0650/AFDA|[Provincia]",
     *                  "nombrecientifico": "Pterogyne.nitens",
     *                  "nombrevulgar": "TIPA COLORADA",
     *                  "total": "21",
     *                  "totalkg": "17409",
     *                  "unidad": "METRO CUBICO"},
     *                  {"clase": "POSTE",
     *                  "codigoorigen": "35|Anadenanthera.colubrina|HUILCO|POSTE|UNIDAD|005/0650/AFDA|[Provincia]",
     *                  "nombrecientifico": "Anadenanthera.colubrina",
     *                  "nombrevulgar": "HUILCO",
     *                  "total": "13",
     *                  "totalkg": "5525",
     *                  "unidad": "UNIDAD"}
     *              ],
     *              "codigo": "TT-0-00016-2017",
     *              "cuitdestino": "27451140609",
     *              "cuitorigen": "23181106199",
     *              "dniconductor": "19611874",
     *              "fechaalta": "2017-08-08 13:45:58.839",
     *              "fechaemision": "2017-08-08 13:45:55.34",
     *              "fechavencimiento": "2017-09-20 14:47:52.133",
     *              "matacoplado": "",
     *              "matvehiculo": "SEJ-659",
     *              "nombreconductor": "ORLANDO GOÑI",
     *              "nombredestino": "GONZALEZ MAIZ SA",
     *              "nombreorigen": "LAS CAPRICHOSAS SA",
     *              "numfuente": "EE-0-00022-2017",
     *              "tipo": "TRANSPORTE",
     *              "tipofuente": "GUIA MADRE",
     *              "codQr": "sdjkfnññNhbSdsf16666b8we",
     *              "componentelocal":{
     *                  "id": "3",
     *                  "correoelectronico": "componente@[provincia].gob.ar",
     *                  "habilitado": "true",
     *                  "provincia": "SANTIAGO DEL ESTERO",
     *                  "url": "[server]/cgl-santiago/rest",
     *                  "idprovgt": "22"
     *              },
     *              "fechacierre": "2017-09-15 15:26:04.47",
     *              "estado":{
     *                  "id": "4",
     *                  "habilitado": "true",
     *                  "nombre": "CERRADA",
     *                  "tipo":{
     *                      "id": "2",
     *                      "habilitado": "true",
     *                      "nombre": "EST_GUIAS",
     *                  }
     *              },
     *              "locdestino": "ALMAGRO - BARRIO - CIUDAD AUTONOMA DE BUENOS AIRES",
     *              "locorigen": "ALTO BELLO - PUEBLO - SANTIAGO DEL ESTERO"
     *          }
     *        ]
     *     }
     * @apiError GuiasNotFound No existen guías registradas con esos parámetros.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay guías registradas con los parámetros recibidos"
     *     }
     */      
    @GET
    @Path("/query")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Guia> findByQuery(@QueryParam("codigo") String codigo, @QueryParam("matricula") String matricula, @QueryParam("provincia") String provincia) {
        List<Guia> result = new ArrayList<>();
        if(codigo != null){
            Guia guia = guiaFacade.getExistente(codigo);
            result.add(guia);
        }else if(matricula != null){
            result = guiaFacade.getByVehiculo(matricula);
        }else if(provincia != null){
            result = guiaFacade.getByProvincia(provincia);
        }
        return result;
    }       

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Guia> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return guiaFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(guiaFacade.count());
    }
    
    /**
     * Método para obtener una Paramétrica según su nombre y nombre del Tipo
     * @param nomTipo String nombre del Tipo de Paramétrica
     * @param nomParam String nombre de la Paramétrica
     * @return Parametrica paramétrica del tipo y con el nombre requeridos
     */
    private Parametrica obtenerParametro(String nomTipo, String nomParam) {
        TipoParam tipo = tipoParamFacade.getExistente(nomTipo);
        return paramFacade.getExistente(nomParam, tipo);
    }      
}
