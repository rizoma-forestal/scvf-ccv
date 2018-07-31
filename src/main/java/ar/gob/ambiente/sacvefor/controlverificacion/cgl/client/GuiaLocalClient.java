
package ar.gob.ambiente.sacvefor.controlverificacion.cgl.client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * Cliente REST Jersey generado para el recurso GuiaFacadeREST de la API de Gestión Local [guias]<br>
 * USAGE:
 * <pre>
 *        GuiaLocalClient client = new GuiaLocalClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 * Cliente para la entidad Guía del API REST del Componente de Gestión Local (CGL) que corresponda
 * @author rincostante 
 */
public class GuiaLocalClient {

    /**
     * Variable privada: WebTarget path de acceso a la API específica de Centros poblados
     */
    private WebTarget webTarget;
    
    /**
     * Variable privada: Client cliente a setear a partir de webTarget
     */
    private Client client;
    
    /**
     * Variable privada: String url general de acceso al servicio según el componente local al cual se consulta.
     */
    private String base_uri;
    
    /**
     * Constructor que instancia el cliente y el webTarget.
     * @param baseUri String url de acceso a la API del componente local que corresponda
     */
    public GuiaLocalClient(String baseUri) {
        base_uri = baseUri;
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(base_uri).path("guias");
    }

    public String countREST() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    /**
     * Método para editar una Guía de un Componente local según su id en formato XML
     * PUT /guias/:id
     * @param requestEntity ar.gob.ambiente.sacvefor.servicios.cgl.Guia Entidad Guía para encapsular los datos de la Guía que se quiere editar
     * @param id String id de la Guía que se quier editar
     * @param token String token recibido previamente al validar el usuario en la API. Irá en el header.
     * @return javax.ws.rs.core.Response con el resultado de la operación
     * @throws ClientErrorException Excepcion a ejecutar
     */
    public Response edit_XML(Object requestEntity, String id, String token) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                .request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .header(HttpHeaders.AUTHORIZATION, token)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Response.class);
    }

    /**
     * Método para editar una Guía de un Componente local según su id en formato JSON
     * PUT /guias/:id
     * @param requestEntity ar.gob.ambiente.sacvefor.servicios.cgl.Guia Entidad Guía para encapsular los datos de la Guía que se quiere editar
     * @param id String id de la Guía que se quier editar
     * @param token String token recibido previamente al validar el usuario en la API. Irá en el header.
     * @return javax.ws.rs.core.Response con el resultado de la operación
     * @throws ClientErrorException Excepcion a ejecutar
     */
    public Response edit_JSON(Object requestEntity, String id, String token) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id}))
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token)
                .put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Response.class);
    }

    /**
     * Método para obtener una Guía local según un parámetro que podrá ser el código, 
     * la matrícula del transporte o el destino. Solo se podrá pasar el valor de un parámetro, los restantes deberán ser nulos.
     * En formato XML
     * GET /guias/query?codigo=:codigo
     * GET /guias/query?matricula=:matricula
     * GET /guias/query?destino=:destino
     * @param <T> Tipo genérico
     * @param responseType Tipo que en el que se setearán los datos serializados obtenidos, en este caso será Guia
     * @param codigo String Código único de la Guía
     * @param matricula String Matrícula del vehículo de transporte de la Guía (si corresponde)
     * @param destino String cuit del destinatario de la Guía (si corresponde)
     * @param token String token recibido previamente al validar el usuario en la API. Irá en el header.
     * @return Guia Guía o guías obtenida/s según los parámetros enviados
     * @throws ClientErrorException Excepcion a ejecutar
     */
    public <T> T findByQuery_XML(Class<T> responseType, String codigo, String matricula, String destino, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (codigo != null) {
            resource = resource.queryParam("codigo", codigo);
        }
        if (matricula != null) {
            resource = resource.queryParam("matricula", matricula);
        }
        if (destino != null) {
            resource = resource.queryParam("destino", destino);
        }
        resource = resource.path("query");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(responseType);
    }

    /**
     * Método para obtener una Guía según un parámetro que podrá ser el código, 
     * la matrícula del transporte o el destino. Solo se podrá pasar el valor de un parámetro, los restantes deberán ser nulos.
     * En formato JSON
     * GET /guias/query?codigo=:codigo
     * GET /guias/query?matricula=:matricula
     * GET /guias/query?destino=:destino
     * @param <T> Tipo genérico
     * @param responseType Tipo que en el que se setearán los datos serializados obtenidos, en este caso será Guia
     * @param codigo String Código único de la Guía
     * @param matricula String Matrícula del vehículo de transporte de la Guía (si corresponde)
     * @param destino String cuit del destinatario de la Guía (si corresponde)
     * @param token String token recibido previamente al validar el usuario en la API. Irá en el header.
     * @return Guia Guía o guías obtenida/s según los parámetros enviados
     * @throws ClientErrorException Excepcion a ejecutar
     */
    public <T> T findByQuery_JSON(Class<T> responseType, String codigo, String matricula, String destino, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (codigo != null) {
            resource = resource.queryParam("codigo", codigo);
        }
        if (matricula != null) {
            resource = resource.queryParam("matricula", matricula);
        }
        if (destino != null) {
            resource = resource.queryParam("destino", destino);
        }
        resource = resource.path("query");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(responseType);
    }

    /**
     * Método que obtiene una Guía registrada habilitada según su id en formato XML
     * GET /guias/:id
     * @param <T> Tipo genérico
     * @param responseType Tipo en el que se setearán los datos serializados obtenidos, en este caso será Guía
     * @param id String id de la Guia a obtener
     * @param token String token recibido previamente al validar el usuario en la API. Irá en el header.
     * @return <T> Guia guía obtenida según el id remitido
     * @throws ClientErrorException Excepcion a ejecutar
     */
    public <T> T find_XML(Class<T> responseType, String id, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(responseType);
    }

    /**
     * Método que obtiene una Guía registrada habilitada según su id en formato JSON
     * GET /guias/:id
     * @param <T> Tipo genérico
     * @param responseType Tipo en el que se setearán los datos serializados obtenidos, en este caso será Guía
     * @param id String id de la Guia a obtener
     * @param token String token recibido previamente al validar el usuario en la API. Irá en el header.
     * @return <T> Guia guía obtenida según el id remitido
     * @throws ClientErrorException Excepcion a ejecutar
     */
    public <T> T find_JSON(Class<T> responseType, String id, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findRange_XML(Class<T> responseType, String from, String to) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findRange_JSON(Class<T> responseType, String from, String to) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Método que obtiene todas las guías registradas habilitadas en formato XML
     * GET /guias
     * @param <T> Tipo genérico
     * @param responseType javax.ws.rs.core.Response
     * @param token String token recibido previamente al validar el usuario en la API. Irá en el header.
     * @return javax.ws.rs.core.Response resultados de la consulta
     * @throws ClientErrorException Excepcion a ejecutar
     */
    public <T> T findAll_XML(Class<T> responseType, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(responseType);
    }
    
    /**
     * Método que obtiene todas las guías registradas habilitadas en formato JSON
     * GET /guias
     * @param <T> Tipo genérico
     * @param responseType javax.ws.rs.core.Response
     * @param token String token recibido previamente al validar el usuario en la API. Irá en el header.
     * @return javax.ws.rs.core.Response resultados de la consulta
     * @throws ClientErrorException Excepcion a ejecutar
     */
    public <T> T findAll_JSON(Class<T> responseType, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(responseType);
    }

    /**
     * Método para obtener los Items de una Guía según el id de la misma. Formato XML
     * Cada ítem se compone del Producto, su cantidad y la unidad de medida
     * @param <T> Tipo genérico
     * @param responseType javax.ws.rs.core.Response
     * @param id String 
     * @param token String token recibido previamente al validar el usuario en la API. Irá en el header.
     * @return javax.ws.rs.core.Response resultados de la consulta
     * @throws ClientErrorException 
     */
    public <T> T findItemsByGuia_XML(Class<T> responseType, String id, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/items", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(responseType);
    }

    /**
     * Método para obtener los Items de una Guía según el id de la misma. Formato JSON
     * Cada ítem se compone del Producto, su cantidad y la unidad de medida
     * @param <T> Tipo genérico
     * @param responseType javax.ws.rs.core.Response
     * @param id String 
     * @param token String token recibido previamente al validar el usuario en la API. Irá en el header.
     * @return javax.ws.rs.core.Response resultados de la consulta
     * @throws ClientErrorException 
     */
    public <T> T findItemsByGuia_JSON(Class<T> responseType, String id, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/items", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(responseType);
    }

    /**
     * Método para cerrar el cliente
     */
    public void close() {
        client.close();
    }
    
}
