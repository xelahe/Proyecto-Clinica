package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * Define una especialidad médica que puede asignarse al personal y usarse para
 * organizar los servicios y profesionales de la clínica.
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
