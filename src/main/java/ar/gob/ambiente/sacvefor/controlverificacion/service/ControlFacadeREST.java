
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import ar.gob.ambiente.sacvefor.controlverificacion.annotation.Secured;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Control;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.Parametrica;
import ar.gob.ambiente.sacvefor.controlverificacion.entities.TipoParam;
import ar.gob.ambiente.sacvefor.controlverificacion.facades.ControlFacade;
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
 * Servicio que implementa los métodos expuestos por la API REST para la entidad Control
 * @author rincostante
 */
@Stateless
@Path("controles")
public class ControlFacadeREST {
    
    @EJB
    private ControlFacade controlFacade;
    @EJB
    private TipoParamFacade tipoParamFacade;
    @EJB
    private ParametricaFacade paramFacade;

    /**
     * @api {get} /controles/:id Ver un Control según su id
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/controles/3 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetControl
     * @apiGroup Control
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador único del Control
     * @apiDescription Método para obtener un Control existente según el id remitido.
     * Obtiene el componente local mediante el método local find(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.Control} Control Detalle del control registrado.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": "3",
     *          "fechahora": "componente@[provincia].gob.ar",
     *          "obs": "true",
     *          "guia": {
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
     *          },
     *          "resultado": {
     *              "id": "15",
     *              "habilitado": "true",
     *              "nombre": "VALIDADA",
     *              "tipo": {
     *                  "id": "2",
     *                  "habilitado": "true",
     *                  "nombre": "EST_GUIAS",
     *              },
     *          },
     *          "ruta": "RP. 26",
     *          "altura": "Km 29,300"
     *     }
     * @apiError ControlNotFound No existe control registrado con ese id.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay control registrado con el id recibido"
     *     }
     */      
    @GET
    @Path("{id}")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Control find(@PathParam("id") Long id) {
        return controlFacade.find(id);
    }

    /**
     * @api {get} /controles Ver todos los controles
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/controles -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetControles
     * @apiGroup Control
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiDescription Método para obtener un listado de los controles existentes.
     * Obtiene los controles mediante el método local findAll()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.Control} Control Listado con todas los controles registrados.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "controles": [
     *          {"id": "3",
     *          "fechahora": "2017-08-09 12:52:27.447",
     *          "obs": "Todo salió muy bien",
     *          "guia": {
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
     *          },
     *          "resultado": {
     *              "id": "15",
     *              "habilitado": "true",
     *              "nombre": "VALIDADA",
     *              "tipo": {
     *                  "id": "2",
     *                  "habilitado": "true",
     *                  "nombre": "EST_GUIAS",
     *              },
     *          },
     *          "ruta": "RP. 26",
     *          "altura": "Km 29,300"},
     *          {"id": "17",
     *          "fechahora": "2017-10-17 16:37:58.743",
     *          "obs": "Se validó correctamente la Guía",
     *          "guia": {
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
     *              "componentelocal":{
     *                  "id": "3",
     *                  "correoelectronico": "componente@[provincia].gob.ar",
     *                  "habilitado": "true",
     *                  "provincia": "SANTIAGO DEL ESTERO",
     *                  "url": "[server]/cgl-santiago/rest",
     *                  "idprovgt": "22"
     *              },
     *              "fechacierre": "2017-11-27 12:32:35.813",
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
     *              "locdestino": "SAN SALVADOR DE JUJUY - JUJUY",
     *              "locorigen": "ALTO BELLO - PUEBLO - SANTIAGO DEL ESTERO"
     *          },
     *          "resultado": {
     *              "id": "15",
     *              "habilitado": "true",
     *              "nombre": "VALIDADA",
     *              "tipo": {
     *                  "id": "2",
     *                  "habilitado": "true",
     *                  "nombre": "EST_GUIAS",
     *              },
     *          },
     *          "ruta": "RP. 31",
     *          "altura": "Km 15,900"}
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
    public List<Control> findAll() {
        return controlFacade.findAll();
    }

    /**
     * @api {get} /controles/query?codGuia=:codGuia,resultado=:resultado,idLoc=:idLoc Ver Controles según parámetros.
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/controles/query?codGuia=TT-10-00003-2017 -H "authorization: xXyYvWzZ"
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/controles/query?resultado=VALIDADA -H "authorization: xXyYvWzZ"
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/controles/query?idLoc=10 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetControlQuery
     * @apiGroup Control
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     }
     * @apiParam {String} codGuia Código de la Guía cuyos controles se solicita
     * @apiParam {String} resultado Resultado del control de las Guías solicitadas
     * @apiParam {String} idLoc Identificación de la Provincia de cuyas Guías se solicitan los controles
     * @apiDescription Método para obtener los controles según una Guía determinada, un resultado determinado o una Provincia emisora.
     * Solo uno de los parámetros tendrá un valor y los restantes nulos.
     * Según el caso, obtiene los controles en cuestión con los métodos locales getByCodGuia(String codGuia), getByResultado(String resultado) o getByIdLocGt(Long idLoc)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.Control} Control Listado de los Controles obtenidos.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *       "controles": [
     *          {"id": "3",
     *          "fechahora": "2017-08-09 12:52:27.447",
     *          "obs": "Todo salió muy bien",
     *          "guia": {
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
     *          },
     *          "resultado": {
     *              "id": "15",
     *              "habilitado": "true",
     *              "nombre": "VALIDADA",
     *              "tipo": {
     *                  "id": "2",
     *                  "habilitado": "true",
     *                  "nombre": "EST_GUIAS",
     *              },
     *          },
     *          "ruta": "RP. 26",
     *          "altura": "Km 29,300"},
     *          {"id": "17",
     *          "fechahora": "2017-10-17 16:37:58.743",
     *          "obs": "Se validó correctamente la Guía",
     *          "guia": {
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
     *              "componentelocal":{
     *                  "id": "3",
     *                  "correoelectronico": "componente@[provincia].gob.ar",
     *                  "habilitado": "true",
     *                  "provincia": "SANTIAGO DEL ESTERO",
     *                  "url": "[server]/cgl-santiago/rest",
     *                  "idprovgt": "22"
     *              },
     *              "fechacierre": "2017-11-27 12:32:35.813",
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
     *              "locdestino": "SAN SALVADOR DE JUJUY - JUJUY",
     *              "locorigen": "ALTO BELLO - PUEBLO - SANTIAGO DEL ESTERO"
     *          },
     *          "resultado": {
     *              "id": "15",
     *              "habilitado": "true",
     *              "nombre": "VALIDADA",
     *              "tipo": {
     *                  "id": "2",
     *                  "habilitado": "true",
     *                  "nombre": "EST_GUIAS",
     *              },
     *          },
     *          "ruta": "RP. 31",
     *          "altura": "Km 15,900"}
     *       ]
     *     }
     * @apiError ControlesNotFound No existen controles registrados con esos parámetros.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay controles registrados con los parámetros recibidos"
     *     }
     */        
    @GET
    @Path("/query")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Control> findByQuery(@QueryParam("codGuia") String codGuia, @QueryParam("resultado") String resultado, @QueryParam("idLoc") Long idLoc) {
        List<Control> result = new ArrayList<>();
        if(codGuia != null){
            result = controlFacade.getByCodGuia(codGuia);
        }else if(resultado != null){
            // obtengo la Paramétrica correspondiente al Resultado
            Parametrica paramResult = obtenerParametro(ResourceBundle.getBundle("/Config").getString("TipoResult"), resultado);
            result = controlFacade.getByResultado(paramResult);
        }else if(idLoc != null){
            result = controlFacade.getByIdLocGt(idLoc);
        }
        return result;
    }      

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Control> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return controlFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(controlFacade.count());
    }
    
    /**
     * Método para obtener una Paramétrica según su nombre y nombre del Tipo
     * @param nomTipo String nombre del Tipo de Paramétrica
     * @param nomParam String nombre de la Paramétrica
     * @return Parametrica Paramétrica obtenida según el tipo y nombre
     */
    private Parametrica obtenerParametro(String nomTipo, String nomParam) {
        TipoParam tipo = tipoParamFacade.getExistente(nomTipo);
        return paramFacade.getExistente(nomParam, tipo);
    }       
}
