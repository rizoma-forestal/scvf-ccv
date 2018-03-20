

package ar.gob.ambiente.sacvefor.controlverificacion.util;

import java.io.Serializable;

/**
 * Este objeto sencillo se crea con el fin de gestionar los listados mostrados en un combo
 * @author rincostante
 */
public class EntidadCombo implements Serializable{
    
    /**
     * Variable privada: Long identificador de la entidad
     */
    private int id;
    
    /**
     * Variable privada: String nombre del objeto
     */
    private String nombre;
    
    /**
     * Constructor con parámetros
     * @param id int identificador del objeto
     * @param nombre String nombre del objeto
     */
    public EntidadCombo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }    
    
    /**
     * Cosntructor por defecto
     */
    public EntidadCombo() {
        
    }       

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return String.format("EntidadServicio[%d, %s]", id, nombre);
    }
}
