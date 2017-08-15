
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
 * Indica los distintos tipos de parámetros que se manipularán con el entidad Parametrica
 * @author rincostante
 */
@Entity
@XmlRootElement
public class TipoParam implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column (length=50, unique=true)
    @Size(message = "El campo nombre no puede tener más de 50 caracteres", max = 50)    
    private String nombre;    
    
    @OneToMany (mappedBy="tipo", orphanRemoval = true)
    @Basic(fetch = FetchType.LAZY)
    private List<Parametrica> parametricas;   
    
    private boolean habilitado;
    
    public TipoParam(){
        parametricas = new ArrayList<>();
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Parametrica> getParametricas() {
        return parametricas;
    }

    public void setParametricas(List<Parametrica> parametricas) {
        this.parametricas = parametricas;
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
        if (!(object instanceof TipoParam)) {
            return false;
        }
        TipoParam other = (TipoParam) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.gob.ambiente.sacvefor.controlverificacion.entities.TipoParam[ id=" + id + " ]";
    }
    
}
