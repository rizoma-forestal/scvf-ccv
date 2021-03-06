
package ar.gob.ambiente.sacvefor.controlverificacion.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author rincostante
 */
@javax.ws.rs.ApplicationPath("rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ar.gob.ambiente.sacvefor.controlverificacion.service.ComponenteLocalFacadeREST.class);
        resources.add(ar.gob.ambiente.sacvefor.controlverificacion.service.ControlFacadeREST.class);
        resources.add(ar.gob.ambiente.sacvefor.controlverificacion.service.GuiaFacadeREST.class);
        resources.add(ar.gob.ambiente.sacvefor.controlverificacion.service.ParametricaFacadeREST.class);
        resources.add(ar.gob.ambiente.sacvefor.controlverificacion.service.PuestoControlFacadeREST.class);
        resources.add(ar.gob.ambiente.sacvefor.controlverificacion.service.RestSecurityFilter.class);
        resources.add(ar.gob.ambiente.sacvefor.controlverificacion.service.TipoParamFacadeREST.class);
        resources.add(ar.gob.ambiente.sacvefor.controlverificacion.service.UsuarioApiResource.class);
        resources.add(ar.gob.ambiente.sacvefor.controlverificacion.service.UsuarioFacadeREST.class);
    }
    
}
