
package ar.gob.ambiente.sacvefor.controlverificacion.cgl.client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:GuiaFacadeREST [guias]<br>
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

    private WebTarget webTarget;
    private Client client;
    private String base_uri;

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

    public Response edit_XML(Object requestEntity, String id) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), Response.class);
    }

    public Response edit_JSON(Object requestEntity, String id) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Response.class);
    }

    public <T> T findByQuery_XML(Class<T> responseType, String codigo, String matricula, String destino) throws ClientErrorException {
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
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findByQuery_JSON(Class<T> responseType, String codigo, String matricula, String destino) throws ClientErrorException {
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

    public <T> T findItemsByGuia_XML(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/items", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findItemsByGuia_JSON(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}/items", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        client.close();
    }
    
}
