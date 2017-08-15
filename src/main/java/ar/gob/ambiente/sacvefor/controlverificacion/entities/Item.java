
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

/**
 * Entidad que encapsula los datos correspondientes a los items que detallan los productos de las Guías
 * @author rincostante
 */
@Entity
@XmlRootElement
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * El código origen encapsula los atributos del Producto de origen separados por '|' en este orden
     * nombreCientifico: nombre científico de la Especie constituido por 'Género/Especie'
     * nombreVulgar: nombre vulgar de la Especie definido de manera local
     * clase: clase en la que se comercializa el Producto definido de manera local
     * unidad: unidad de medida en la que se comercializa el Producto/Clase definido de manera local
     * resolución: numero de la resolución (campo numero de la entidad Autorización)
     * provincia: nombre de la Provincia dentro de la cual se extraerá el Producto
     * EJ.:"1|Prosopis caldenia|Caldén|Rollo|Unidad|123-DGB-2017|Santiago del Estero"
     * Estos valores serán tomados del Item productivo acreditado que de origen a los nuevos productos
     */
    private String codigoOrigen;
    
    /**
     * Nombre Científico cacheado del Producto
     * Se incluye en el código del producto trazable
     */
    @Column (nullable=false, length=100)
    @NotNull(message = "El campo nombreCientifico no puede ser nulo")
    @Size(message = "El campo nombreCientifico no puede tener más de 100 caracteres", min = 1, max = 100)      
    private String nombreCientifico;
    
    /**
     * Nombre vulgar cacheado del Producto
     * Se incluye en el código del producto trazable
     */
    @Column (nullable=false, length=50)
    @NotNull(message = "El campo nombreVulgar no puede ser nulo")
    @Size(message = "El campo nombreVulgar no puede tener más de 50 caracteres", min = 1, max = 50)  
    private String nombreVulgar;
    
    /**
     * Clase cacheada del Producto
     * Se incluye en el código del producto trazable
     */
    @Column (nullable=false, length=30)
    @NotNull(message = "El campo clase no puede ser nulo")
    @Size(message = "El campo clase no puede tener más de 30 caracteres", min = 1, max = 30)  
    private String clase;
    
    /**
     * Unidad de medida cacheada del Producto
     * Se incluye en el código del producto trazable
     */    
    @Column (nullable=false, length=30)
    @NotNull(message = "El campo unidad no puede ser nulo")
    @Size(message = "El campo unidad no puede tener más de 30 caracteres", min = 1, max = 30) 
    private String unidad;
    
    /**
     * Cantidad del Producto incluído en el item
     * Cupo
     */
    @Column
    private float total;
    
    /**
     * Equivalencia del total por unidad del Producto, en Kg.
     */
    @Column
    private float totalKg;
 

    public String getCodigoOrigen() {
        return codigoOrigen;
    }

    public void setCodigoOrigen(String codigoOrigen) {
        this.codigoOrigen = codigoOrigen;
    }

    public String getNombreCientifico() {
        return nombreCientifico;
    }

    public void setNombreCientifico(String nombreCientifico) {
        this.nombreCientifico = nombreCientifico;
    }

    public String getNombreVulgar() {
        return nombreVulgar;
    }

    public void setNombreVulgar(String nombreVulgar) {
        this.nombreVulgar = nombreVulgar;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getTotalKg() {
        return totalKg;
    }

    /**********************
     * Métodos de acceso **
     **********************/ 
    public void setTotalKg(float totalKg) {
        this.totalKg = totalKg;
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
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.gob.ambiente.sacvefor.controlverificacion.entities.ItemProductivo[ id=" + id + " ]";
    }
    
}
