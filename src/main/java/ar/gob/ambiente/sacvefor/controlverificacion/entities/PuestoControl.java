
package ar.gob.ambiente.sacvefor.controlverificacion.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que encapsula los datos correspondientes a un Puesto donde se realiza el Control de las Guías
 * @author rincostante
 */
@Entity
@XmlRootElement
public class PuestoControl implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Variable privada: Identificador único
     */  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Variable privada: Nombre identificatorio del Puesto de control
     */
    @Column (length=50)
    @Size(message = "El campo nombre no puede tener más de 50 caracteres", max = 50)    
    private String nombre;
    
    /**
     * Variable privada: Ruta o camino en el que se encuentra el Puesto de Control
     */
    @Column (length=50)
    @Size(message = "El campo ruta no puede tener más de 50 caracteres", max = 50)        
    private String ruta;
    
    /**
     * Variable privada: Altura en número o Km de la Ruta o Camino en el que se encuentra el Pusto de Control 
     */
    @Column (length=20)
    @Size(message = "El campo altura no puede tener más de 20 caracteres", max = 20)        
    private String altura;
    
    /**
     * Variable privada: Id de la Localidad en el Servicio de Gestión Territorial
     */
    private Long idLocGt;
    
    /**
     * Variable privada: Nombre de la Localidad donde está ubicado el Puesto de control
     * cacheado del servicio de Gestión Terrorial
     */
    @Column (nullable=false, length=50)
    @NotNull(message = "El campo localidad no puede ser nulo")
    @Size(message = "El campo localidad no puede tener más de 50 caracteres", min = 1, max = 50) 
    private String localidad;
    
    /**
     * Variable privada: Nombre del Departamento donde está ubicado el Puesto de control
     * cacheado del servicio de Gestión Terrorial
     */    
    @Column (nullable=false, length=50)
    @NotNull(message = "El campo departamento no puede ser nulo")
    @Size(message = "El campo departamento no puede tener más de 50 caracteres", min = 1, max = 50)    
    private String departamento;
    
    /**
     * Variable privada: Nombre de la Provincia donde está ubicado el Puesto de control
     * cacheado del servicio de Gestión Terrorial
     */     
    @Column (nullable=false, length=50)
    @NotNull(message = "El campo provincia no puede ser nulo")
    @Size(message = "El campo provincia no puede tener más de 50 caracteres", min = 1, max = 50) 
    private String provincia;
    
    /**
     * Variable privada: Descripción del Puesto de control
     */
    @Column (length=50)
    @Size(message = "El campo descripcion no puede tener más de 100 caracteres", max = 100) 
    private String descripcion;

    /**
     * Variable privada: Listado de Usuarios que se desempeñan en el Puesto de control
     */
    @OneToMany (mappedBy="puestoControl", orphanRemoval = true)
    @Basic(fetch = FetchType.LAZY)
    private List<Usuario> usuarios;
    
    /**
     * Variable privada: Condición de habilitado del Puesto de control
     */
    private boolean habilitado;
    
    public PuestoControl(){
        usuarios = new ArrayList<>();
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdLocGt() {
        return idLocGt;
    }

    public void setIdLocGt(Long idLocGt) {
        this.idLocGt = idLocGt;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PuestoControl)) {
            return false;
        }
        PuestoControl other = (PuestoControl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.gob.ambiente.sacvefor.controlverificacion.entities.PuestoControl[ id=" + id + " ]";
    }
}
