
package ar.gob.ambiente.sacvefor.controlverificacion.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.envers.Audited;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

/**
 * Entidad que encapsula los datos correspondientes a cada control 
 * que se le pueda hacer a una Guía de transporte durante su vigencia
 * @author rincostante
 */
@Entity
@Audited
@XmlRootElement
public class Control implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /**
     * Guía que es objeto del control
     */
    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name="guia_id", nullable=false)
    @NotNull(message = "Debe existir una Guía")    
    private Guia guia;
    
    /**
     * Fecha de registro de la Guía
     */
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaHora;
    
    /**
     * Usuario que realiza el control
     */
    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name="usuario_id", nullable=false)
    @NotNull(message = "Debe existir un Usuario")        
    private Usuario usuario;
    
    /**
     * Resultado del control realizado
     */
    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name="resultado_id", nullable=false)
    @NotNull(message = "Debe existir un resultado del control")    
    private Parametrica resultado;
    
    /**
     * Observaciones que pudieran surgir del control
     */
    @Column (length=500)
    @Size(message = "El campo Observaciones no puede tener más de 500 caracteres", max = 500)      
    private String obs;
    
    /**
     * Campo que mostrará la fecha de las revisiones
     * No se persiste
     */    
    @Transient
    private Date fechaRevision;   
    
    /**
     * Campo que muestra la fecha y hora del control como string, para el listado de controles
     */
    @Transient
    private String strFechaHora;

    /**
     * Ruta o camino en el que se encuentra el Puesto que realizó el Control
     * Cacheado de la entidad Puesto de Control, del Usuario
     * No persistido, solo para la API
     */
    @Transient
    private String ruta;
    
    /**
     * Altura en número o Km de la Ruta o camino en el que se encuentra el Puesto que realizó el Control
     * Cacheado de la entidad Puesto de Control, del Usuario
     * No persistido, solo para la API
     */    
    @Transient
    private String altura;

    public String getStrFechaHora() {
        DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        strFechaHora = formatoFecha.format(fechaHora) + " " + formatoHora.format(fechaHora);
        return strFechaHora;
    }

    public void setStrFechaHora(String strFechaHora) {
        this.strFechaHora = strFechaHora;
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

    public Guia getGuia() {
        return guia;
    }

    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public void setGuia(Guia guia) {
        this.guia = guia;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    @XmlTransient
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Parametrica getResultado() {
        return resultado;
    }

    public void setResultado(Parametrica resultado) {
        this.resultado = resultado;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
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
        if (!(object instanceof Control)) {
            return false;
        }
        Control other = (Control) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.gob.ambiente.sacvefor.controlverificacion.entities.Control[ id=" + id + " ]";
    }
    
}
