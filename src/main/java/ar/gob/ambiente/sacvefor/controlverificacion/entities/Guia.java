
package ar.gob.ambiente.sacvefor.controlverificacion.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que encapsula los datos correspondientes a una Guía.
 * Cacheados al ser emitida, desde un CGL o el CGT
 * @author rincostante
 */
@Entity
@XmlRootElement
public class Guia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Cadena que constituye el código único de la Guía,
     * el formato se configurará en el archivo de propiedades Config.
     */
    @Column (nullable=false, length=20, unique=true)
    @NotNull(message = "El campo codigo no puede ser nulo")
    @Size(message = "El campo codigo no puede tener más de 20 caracteres", min = 1, max = 20)    
    private String codigo;
    
    /**
     * Estado en el que se encuentra la Guía, podrán ser:
     * Vigente,
     * Cerrada,
     * Anulada,
     * Intervenida,
     * Suspendida
     */
    @ManyToOne
    @JoinColumn(name="estado_id", nullable=false)
    @NotNull(message = "Debe existir un estado para la Guía")    
    private Parametrica estado;    
    
    /**
     * Tipo de Guía
     * Primaria si proviene de un CGL
     * Trazabilidad si proviene del CGT
     */
    @Column (nullable=false, length=50)
    @NotNull(message = "El campo tipo no puede ser nulo")
    @Size(message = "El campo tipo no puede tener más de 50 caracteres", min = 1, max = 50)  
    private String tipo;
    
    /**
     * Tipo de Guía que dio origen a los productos
     * Solo para Guías primarias
     */
    @Column (nullable=false, length=50)
    @NotNull(message = "El campo tipo no puede ser nulo")
    @Size(message = "El campo tipo no puede tener más de 50 caracteres", min = 1, max = 50)  
    private String tipoFuente;
    
    /**
     * Cadena que constituye el número identificatorio de la Guía fuente de Productos,
     * Solo para Guías primarias
     */
    @Column (nullable=false, length=30)
    @NotNull(message = "El campo numFuente no puede ser nulo")
    @Size(message = "El campo numFuente no puede tener más de 30 caracteres", min = 1, max = 30)  
    private String numFuente;   
    
    /**
     * Listado de los items que constituyen el detalle de la Guía
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name = "guia_id", referencedColumnName = "id")
    private List<Item> items;      

    /**
     * En los casos que la Guía haya sido generada por un Componente local, lo vincula.
     */
    @ManyToOne
    @JoinColumn(name="complocal_id")
    @NotNull(message = "Debe existir un Componente local")    
    private ComponenteLocal compLocal;
    
    /**
     * Nombre completo de la Entidad origen que remite la Guía
     */
    @Column (nullable=false, length=50)
    @NotNull(message = "El campo Nombre de Origen no puede ser nulo")
    @Size(message = "El campo Nombre de Origen no puede tener más de 50 caracteres", min = 1, max = 50)  
    private String nombreOrigen;
    
    /**
     * Cuit correspondiente a la Entidad origen que remite la Guía
     */
    private Long cuitOrigen;
    
    /**
     * Ubicación del orígen de la Guía.
     * Ej: Villa Candela - Jujuy
     */
    @Column (length=100)
    @Size(message = "El campo Localidad origen no puede tener más de 100 caracteres", max = 100)  
    private String locOrigen;
    
    /**
     * Nombre completo de la Entidad destinataria de la Guía
     */
    @Column (nullable=false, length=50)
    @NotNull(message = "El campo Nombre de Origen no puede ser nulo")
    @Size(message = "El campo Nombre de Origen no puede tener más de 50 caracteres", min = 1, max = 50)  
    private String nombreDestino;
    
    /**
     * Cuit correspondiente a la Entidad destinataria de la Guía
     */
    private Long cuitDestino;
    
    /**
     * Ubicación del destino de la Guía.
     * Ej: Los cardos - Salta
     */
    @Column (length=100)
    @Size(message = "El campo Localidad destino no puede tener más de 100 caracteres", max = 100)  
    private String locDestino;    
    
    /**
     * Matrícula correspondiente al Vehículo de transporte vinculado a la Guía
     */
    @Column (nullable=false, length=15)
    @NotNull(message = "El campo Matrícula del Vehículo no puede ser nulo")
    @Size(message = "El campo Matrícula del Vehículo no puede tener más de 15 caracteres", min = 1, max = 15)  
    private String matVehiculo;    
    
    /**
     * Si lo hubiera, matrícula correspondiente al acoplado del Vehículo de transporte vinculado a la Guía
     */
    @Column (length=15)
    @Size(message = "El campo Matrícula del Vehículo no puede tener más de 15 caracteres", max = 15)  
    private String matAcoplado; 
    
    /**
     * Nombre completo del Conductor del Vehículo de transporte vinculado a la Guía
     */
    @Column (nullable=false, length=50)
    @NotNull(message = "El campo Nombre del Conductor del Vehículo no puede ser nulo")
    @Size(message = "El campo Nombre del Conductor del Vehículo no puede tener más de 50 caracteres", min = 1, max = 50)  
    private String nombreConductor;    
    
    /**
     * DNI correspondiente al Conductor del Vehículo de transporte vinculado a la Guía
     */
    private Long dniConductor;    
    
    /**
     * Fecha de registro de la Guía
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaAlta;  
    
    /**
     * Fecha de emisión de la Guía.
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaEmision;    
    
    /**
     * Fecha de emisión de vencimiento de la Guía.
     * Si correspondiera
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaVencimiento;     
    
    /**
     * Fecha de cierre de la Guía. Se seteará desde el CGT mediante la API REST
     * en el momento en que el Destinatario cierre la Guía.
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaCierre;      


    public Guia(){
        items = new ArrayList<>();
    }

    public String getLocOrigen() {
        return locOrigen;
    }

    public void setLocOrigen(String locOrigen) {
        this.locOrigen = locOrigen;
    }

    public String getLocDestino() {
        return locDestino;
    }

    public void setLocDestino(String locDestino) {
        this.locDestino = locDestino;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Parametrica getEstado() {
        return estado;
    }

    public void setEstado(Parametrica estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoFuente() {
        return tipoFuente;
    }

    public void setTipoFuente(String tipoFuente) {
        this.tipoFuente = tipoFuente;
    }

    public String getNumFuente() {
        return numFuente;
    }

    public void setNumFuente(String numFuente) {
        this.numFuente = numFuente;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public ComponenteLocal getCompLocal() {
        return compLocal;
    }

    public void setCompLocal(ComponenteLocal compLocal) {
        this.compLocal = compLocal;
    }

    public String getNombreOrigen() {
        return nombreOrigen;
    }

    public void setNombreOrigen(String nombreOrigen) {
        this.nombreOrigen = nombreOrigen;
    }

    public Long getCuitOrigen() {
        return cuitOrigen;
    }

    public void setCuitOrigen(Long cuitOrigen) {
        this.cuitOrigen = cuitOrigen;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }

    public Long getCuitDestino() {
        return cuitDestino;
    }

    public void setCuitDestino(Long cuitDestino) {
        this.cuitDestino = cuitDestino;
    }

    public String getMatVehiculo() {
        return matVehiculo;
    }

    public void setMatVehiculo(String matVehiculo) {
        this.matVehiculo = matVehiculo;
    }

    public String getMatAcoplado() {
        return matAcoplado;
    }

    public void setMatAcoplado(String matAcoplado) {
        this.matAcoplado = matAcoplado;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public Long getDniConductor() {
        return dniConductor;
    }

    public void setDniConductor(Long dniConductor) {
        this.dniConductor = dniConductor;
    }

    @XmlTransient
    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
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
        if (!(object instanceof Guia)) {
            return false;
        }
        Guia other = (Guia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.gob.ambiente.sacvefor.controlverificacion.entities.Guia[ id=" + id + " ]";
    }
    
}
