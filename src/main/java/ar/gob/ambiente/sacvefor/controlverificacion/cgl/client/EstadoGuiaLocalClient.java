
package ar.gob.ambiente.sacvefor.controlverificacion.cgl.client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:EstadoGuiaFacadeREST
 * [estadosguia]<br>
 * USAGE:
 * <pre>
 *        EstadoGuiaLocalClient client = new EstadoGuiaLocalClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 * Cliente para la entidad EstadoGuía del API REST del Componente de Gestión Local (CGL) que corresponda
 * @author rincostante
 */
public class EstadoGuiaLocalClient {

    private WebTarget webTarget;
    private Client client;
    private String base_uri;

    public EstadoGuiaLocalClient(String baseUri) {
        base_uri = baseUri;
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(base_uri).path("estadosguia");
    }

    public String countREST() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public <T> T findByQuery_XML(Class<T> responseType, String nombre) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (nombre != null) {
            resource = resource.queryParam("nombre", nombre);
        }
        resource = resource.path("query");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findByQuery_JSON(Class<T> responseType, String nombre) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (nombre != null) {
            resource = resource.queryParam("nombre", nombre);
        }
        resource = resource.path("query");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T find_XML(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T find_JSON(Class<T> responseType, String id) throws ClientErrorException {
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

    public <T> T findAll_XML(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findAll_JSON(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        client.close();
    }
    
}
