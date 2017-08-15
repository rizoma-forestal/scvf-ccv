
package ar.gob.ambiente.sacvefor.controlverificacion.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que encapsula los datos correspondientes a los Componentes de Gestión Local (CGL) vinculados
 * @author rincostante
 */
@Entity
@XmlRootElement
public class ComponenteLocal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Nombre de la Provincia que gestiona el Componente local
     */
    @Column (nullable=false, length=50, unique=true)
    @NotNull(message = "El campo provincia no puede ser nulo")
    @Size(message = "El campo provincia no puede tener más de 50 caracteres", min = 1, max = 50)    
    private String provincia;
    
    /**
     * Identificación de la Provincia en el SErvicio de Gestión territorial
     */
    private Long idProvGt;
    
    /**
     * Url de acceso a la API rest del Componente local
     */
    @Column (length=50, unique=true)
    @Size(message = "El campo url no puede tener más de 50 caracteres", max = 50)     
    private String url;
    
    /**
     * Correo electrónico oficial del Componente local
     * Para notificar resultados de controles
     */
    @Column (length=50)
    @Size(message = "El campo correoElectronico no puede tener más de 50 caracteres", max = 50)   
    private String correoElectronico;
    
    /**
     * Condición de habilitado del Componente local
     */
    private boolean habilitado;

    public Long getIdProvGt() {
        return idProvGt;
    }

    public void setIdProvGt(Long idProvGt) {
        this.idProvGt = idProvGt;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    @XmlTransient
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
        if (!(object instanceof ComponenteLocal)) {
            return false;
        }
        ComponenteLocal other = (ComponenteLocal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.gob.ambiente.sacvefor.controlverificacion.entities.ComponenteLocal[ id=" + id + " ]";
    }
    
}
