

package ar.gob.ambiente.sacvefor.controlverificacion.util;

import java.io.Serializable;

/**
 * Este objeto sencillo se crea con el fin de gestionar los listados mostrados en un combo
 * @author rincostante
 */
public class EntidadCombo implements Serializable{
    
    private int id;
    private String nombre;
    
    public EntidadCombo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }    
    
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
