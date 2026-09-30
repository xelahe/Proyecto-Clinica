package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * 
 */
public class Especialidad {

    /**
     * Default constructor
     */
    public Especialidad() {
    }

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private String nombre;

    // ---- Getters y setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

}
