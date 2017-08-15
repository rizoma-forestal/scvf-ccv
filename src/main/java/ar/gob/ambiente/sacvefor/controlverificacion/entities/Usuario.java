
package ar.gob.ambiente.sacvefor.controlverificacion.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * Entidad para gestionar los Usuarios de la aplicación
 * @author rincostante
 */
@Entity
@XmlRootElement
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Guarda el rol al que pertenece el usuario
     */
    @ManyToOne
    @JoinColumn(name="rol_id", nullable=false)
    @NotNull(message = "Debe existir un Rol")    
    private Parametrica rol;
    
    /**
     * Será el DNI del usuario que oficiará como nombre de usuario
     */
    private Long login;
    
    /**
     * Nombre y apellido del usuario
     */
    @Column (length=50)
    @Size(message = "El campo nombreCompleto no puede tener más de 50 caracteres", max = 50)       
    private String nombreCompleto; 
    
    /**
     * Correo electrónico del Usuario, en el que recibirá las credenciales de acceso a la aplicación
     */
    @Column (length=50)
    @Size(message = "El campo correoElectronico no puede tener más de 50 caracteres", max = 50)   
    private String correoElectronico;
    
    /**
     * Clave encriptada que generará el sistema automáticamente la primera vez y 
     * solicitará al usuario su cambio cuando realice la primera sesión.
     */
    @Column (length=100)
    @Size(message = "el campo clave no puede tener más de 100 caracteres", max = 100)   
    private String clave;
    
    /**
     * Puesto de control en el que se desempeña el Usuario
     */
    @ManyToOne
    @JoinColumn(name="puesto_id")
    @NotNull(message = "Debe existir un puestoControl")
    private PuestoControl puestoControl;
    
    /**
     * Controles realizados por el Usuario
     */
    @OneToMany (mappedBy="usuario", orphanRemoval = true)
    @Basic(fetch = FetchType.LAZY)
    private List<Control> controles;
    
    /**
     * Fecha de alta del usuario
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaAlta;
    
    /**
     * Fecha de la última vez que el usuario registra una sesión en la aplicación
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaUltimoLogin;
    
    /**
     * indica si el usuario solo se logueó una vez, en cuyo caso no cambió la contraseña
     */
    private boolean primeraVez;
    
    /**
     * Estado de habilitado
     */
    private boolean habilitado;
    
    public Usuario(){
        controles = new ArrayList<>();
    }

    @XmlTransient
    public List<Control> getControles() {
        return controles;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public boolean isPrimeraVez() {
        return primeraVez;
    }

    public void setPrimeraVez(boolean primeraVez) {
        this.primeraVez = primeraVez;
    }

    public void setControles(List<Control> controles) {
        this.controles = controles;
    }

    public PuestoControl getPuestoControl() {
        return puestoControl;
    }

    public void setPuestoControl(PuestoControl puestoControl) {
        this.puestoControl = puestoControl;
    }

    public Parametrica getRol() {
        return rol;
    }

    public void setRol(Parametrica rol) {
        this.rol = rol;
    }

    public Long getLogin() {
        return login;
    }

    public void setLogin(Long login) {
        this.login = login;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaUltimoLogin() {
        return fechaUltimoLogin;
    }

    public void setFechaUltimoLogin(Date fechaUltimoLogin) {
        this.fechaUltimoLogin = fechaUltimoLogin;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.gob.ambiente.sacvefor.controlverificacion.entities.Usuario[ id=" + id + " ]";
    }
    
}
