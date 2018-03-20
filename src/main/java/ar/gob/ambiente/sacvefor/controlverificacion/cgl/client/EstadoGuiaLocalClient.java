
package ar.gob.ambiente.sacvefor.controlverificacion.cgl.client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Cliente REST Jersey generado para el recurso EstadoGuiaFacadeREST de la API de los componentes de gestión loca (CGL)
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

    /**
     * Variable privada: WebTarget path de acceso a la API de Gestión local
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
     * Recibe como parámetro la url de acceso a la API del componente local que corresponda
     */
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

    /**
     * Método para obtener un Estado de Guía según su nombre. En formato XML
     * GET /estadosguia/query?nombre=:nombre
     * @param <T> Tipo genérico
     * @param responseType Tipo que en el que se setearán los datos serializados obtenidos, en este caso será EstadoGuia
     * @param nombre String nombre del Estado de la Guía
     * @return EstadoGuia Estado de la guía obtenido según el id remitido
     * @throws ClientErrorException Excepcion a ejecutar
     */
    public <T> T findByQuery_XML(Class<T> responseType, String nombre) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (nombre != null) {
            resource = resource.queryParam("nombre", nombre);
        }
        resource = resource.path("query");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Método para obtener un Estado de Guía según su nombre. En formato JSON
     * GET /estadosguia/query?nombre=:nombre
     * @param <T> Tipo genérico
     * @param responseType Tipo que en el que se setearán los datos serializados obtenidos, en este caso será EstadoGuia
     * @param nombre String nombre del Estado de la Guía
     * @return EstadoGuia Estado de la guía obtenido según el id remitido
     * @throws ClientErrorException Excepcion a ejecutar
     */
    public <T> T findByQuery_JSON(Class<T> responseType, String nombre) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (nombre != null) {
            resource = resource.queryParam("nombre", nombre);
        }
        resource = resource.path("query");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }
    
    /**
     * Método que obtiene un Estado de guía registrado habilitado según su id en formato XML
     * GET /estadosguia/:id
     * @param <T> Tipo genérico
     * @param responseType Tipo que en el que se setearán los datos serializados obtenidos, en este caso será EstadoGuia
     * @param id String id del EstadoGuia a obtener
     * @return <T> EstadoGuia Estado de la guía obtenido según el id remitido
     * @throws ClientErrorException Excepcion a ejecutar
     */
    public <T> T find_XML(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Método que obtiene un Estado de guía registrado habilitado según su id en formato JSON
     * GET /estadosguia/:id
     * @param <T> Tipo genérico
     * @param responseType Tipo que en el que se setearán los datos serializados obtenidos, en este caso será EstadoGuia
     * @param id String id del EstadoGuia a obtener
     * @return <T> EstadoGuia Estado de la guía obtenido según el id remitido
     * @throws ClientErrorException Excepcion a ejecutar
     */
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

    /**
     * Método que obtiene todos los Estados de guías registrados habilitados en formato XML
     * GET /estadosguia
     * @param <T> Tipo genérico
     * @param responseType javax.ws.rs.core.Response
     * @return javax.ws.rs.core.Response resultados de la consulta
     * @throws ClientErrorException Excepcion a ejecutar
     */
    public <T> T findAll_XML(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Método que obtiene todos los Estados de guías registrados habilitados en formato JSON
     * GET /estadosguia
     * @param <T> Tipo genérico
     * @param responseType javax.ws.rs.core.Response
     * @return javax.ws.rs.core.Response resultados de la consulta
     * @throws ClientErrorException Excepcion a ejecutar
     */
    public <T> T findAll_JSON(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Método para cerrar el cliente
     */
    public void close() {
        client.close();
    }
    
}
