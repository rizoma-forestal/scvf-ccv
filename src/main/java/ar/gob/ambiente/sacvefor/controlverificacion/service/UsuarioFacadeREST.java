
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import ar.gob.ambiente.sacvefor.controlverificacion.annotation.Secured;
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
     * @api {get} /usuarios/:id Ver un Usuario
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/usuarios/2 -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetUsuario
     * @apiGroup Usuario
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiParam {Long} id Identificador único del usuario
     * @apiDescription Método para obtener un usuarios existente según el id remitido.
     * Obtiene el usuario mediante el método local find(Long id)
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.Usuario} Usuario Detalle del usuario registrado.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": "2",
     *          "fechaalta": "2017-06-12 14:31:39.84",
     *          "fechaultimologin": "2017-08-15 16:20:25.212",
     *          "habilitado": "true",
     *          "login": "21633987",
     *          "nombrecompleto": "USUARIO VERIFICADOR",
     *          "puesto": {
     *                  "id": "2",
     *                  "departamento": "SUSQUES",
     *                  "descripcion": "Puesto de prueba",
     *                  "habilitado": "true",
     *                  "idlocgt": "9967",
     *                  "localidad": "MINA PROVIDENCIA",
     *                  "nombre": "Segundo",
     *                  "provincia": "JUJUY",
     *                  "altura": "Km 27",
     *                  "ruta": "128"
     *              },
     *          "rol":{
     *              "id": "1",
     *              "nombre": "ADMINISTRADOR",
     *              "habilitado": "true",
     *              "tipo": {
     *                  "id": "1",
     *                  "nombre": "ROL_USUARIOS"
     *                  }
     *              },
     *          "primeravez": "false",
     *          "correoelectronico": "usario2@correo.com"
     *     }
     * @apiError UsuarioNotFound No existe usuario registrado con ese id.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay usuario registrado con el id recibido"
     *     }
     */     
    @GET
    @Path("{id}")
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Usuario find(@PathParam("id") Long id) {
        return usFacade.find(id);
    }

    /**
     * @api {get} /usuarios Ver todos los usuarios
     * @apiExample {curl} Ejemplo de uso:
     *     curl -X GET -d [PATH_SERVER]/ctrlVerif/rest/usuarios -H "authorization: xXyYvWzZ"
     * @apiVersion 1.0.0
     * @apiName GetUsuarios
     * @apiGroup Usuario
     * @apiHeader {String} Authorization Token recibido al autenticar el usuario
     * @apiHeaderExample {json} Ejemplo de header:
     *     {
     *       "Authorization": "xXyYvWzZ"
     *     } 
     * @apiDescription Método para obtener un listado de los usuarios existentes.
     * Obtiene los tipos de paramétricas mediante el método local findAll()
     * @apiSuccess {ar.gob.ambiente.sacvefor.servicios.ctrlverif.Usuario} Usuario Listado con todas los usuarios registrados.
     * @apiSuccessExample Respuesta exitosa:
     *     HTTP/1.1 200 OK
     *     {
     *      "Usuarios": [
     *          {"id": "2",
     *          "fechaalta": "2017-06-12 14:31:39.84",
     *          "fechaultimologin": "2017-08-15 16:20:25.212",
     *          "habilitado": "true",
     *          "login": "21633987",
     *          "nombrecompleto": "USUARIO VERIFICADOR",
     *          "puesto": {
     *                  "id": "2",
     *                  "departamento": "SUSQUES",
     *                  "descripcion": "Puesto de prueba",
     *                  "habilitado": "true",
     *                  "idlocgt": "9967",
     *                  "localidad": "MINA PROVIDENCIA",
     *                  "nombre": "Segundo",
     *                  "provincia": "JUJUY",
     *                  "altura": "Km 27",
     *                  "ruta": "128"
     *              },
     *          "rol":{
     *              "id": "1",
     *              "nombre": "ADMINISTRADOR",
     *              "habilitado": "true",
     *              "tipo": {
     *                  "id": "1",
     *                  "nombre": "ROL_USUARIOS"
     *                  }
     *              },
     *          "primeravez": "false",
     *          "correoelectronico": "usario2@correo.com"},
     *          {"id": "1",
     *          "fechaalta": "2017-08-07 14:31:39.84",
     *          "fechaultimologin": "2017-12-01 11:50:02.785",
     *          "habilitado": "true",
     *          "login": "14011378",
     *          "nombrecompleto": "Usuario Controlador",
     *          "puesto": {
     *                  "id": "1",
     *                  "departamento": "COMUNA 1",
     *                  "descripcion": "Puesto de prueba",
     *                  "habilitado": "true",
     *                  "idlocgt": "10809",
     *                  "localidad": "RETIRO",
     *                  "nombre": "Puesto 1",
     *                  "provincia": "CIUDAD AUTONOMA DE BUENOS AIRES",
     *                  "altura": "Km 1",
     *                  "ruta": "1"
     *              },
     *          "rol":{
     *              "id": "1",
     *              "nombre": "ADMINISTRADOR",
     *              "habilitado": "true",
     *              "tipo": {
     *                  "id": "1",
     *                  "nombre": "ROL_USUARIOS"
     *                  }
     *              },
     *          "primeravez": "false",
     *          "correoelectronico": "usario1@correo.com"}
     *          ]
     *     }
     * @apiError UsuariosNotFound No existen usuarios registrados.
     * @apiErrorExample Respuesta de error:
     *     HTTP/1.1 400 Not Found
     *     {
     *       "error": "No hay tipos de usuarios registrados"
     *     }
     */     
    @GET
    @Secured
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findAll() {
        return usFacade.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return usFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(usFacade.count());
    }
}
