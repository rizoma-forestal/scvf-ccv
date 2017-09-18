
package ar.gob.ambiente.sacvefor.controlverificacion.service;

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
     * Método para crear una Guía a controlar, que provendrá de cualquier de los CGL o del CGT
     * @param entity
     * @return 
     */
    @POST
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
     * Método para editar una Guia existente. Solo se editará el estado y la fecha correspondiente
     * @param id : Id de la Guia a editar
     * @param entity : Guia a editar
     * @return : El código de repuesta que corresponda según se haya realizado o no la operación: 200 o 400
     */     
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(@PathParam("id") Long id, ar.gob.ambiente.sacvefor.servicios.ctrlverif.Guia entity) {
        try{
            // obtengo la Guía
            Guia guia = guiaFacade.find(id);
            // obtengo y seteo el estado
            Parametrica paramEstado = paramFacade.find(entity.getEstado().getId());
            // actualizo la Guía con el Estado
            guia.setEstado(paramEstado);
            // si está cerrando la Guía, seteo la fecha de cierre.
            if(paramEstado.getNombre().equals(ResourceBundle.getBundle("/Config").getString("Cerrada"))){
                // seteo la fecha de cierre
                guia.setFechaCierre(entity.getFechaCierre());
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
     * Método para obtener la Guía correspondiente al id recibido
     * Ej: [PATH]/guias/1
     * @param id : Id de la Guía a buscar
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Guia find(@PathParam("id") Long id) {
        return guiaFacade.find(id);
    }

    /**
     * Método que retorna todas las Guías registradas
     * Ej: [PATH]/guias
     * @return 
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Guia> findAll() {
        return guiaFacade.findAll();
    }
    
    /**
     * Método que retorna las Guías según el parámetro de consulta
     * Solo podrá ser uno a la vez
     * @param codigo : Código de la Guía, solo devolverá una
     * @param matricula : Matrícula del Vehículo de transporte, podrá devolver un listado
     * @param provincia : Para el caso de Guías locales, la Provincia que las emitió. Podrá devolver un listado
     * Ej: [PATH]/guias/query?matricula=ABC-213
     * @return 
     */
    @GET
    @Path("/query")
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

    /**
     * Método que obtiene un listado de Guías cuyos id se encuentran entre los parámetros de inicio y fin recibidos
     * Ej: [PATH]/usuarios/1/10
     * @param from
     * @param to
     * @return 
     */
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Guia> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return guiaFacade.findRange(new int[]{from, to});
    }

    /**
     * Método que devuelve un entero con la totalidad de las Guías registradas
     * Ej: [PATH]/guias/count
     * @return 
     */
    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(guiaFacade.count());
    }
    
    /**
     * Método para obtener una Paramétrica según su nombre y nombre del Tipo
     * @param nomTipo : nombre del Tipo de Paramétrica
     * @param nomParam : nombre de la Paramétrica
     * @return 
     */
    private Parametrica obtenerParametro(String nomTipo, String nomParam) {
        TipoParam tipo = tipoParamFacade.getExistente(nomTipo);
        return paramFacade.getExistente(nomParam, tipo);
    }      
}
