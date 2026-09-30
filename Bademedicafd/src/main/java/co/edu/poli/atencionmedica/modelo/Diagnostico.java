package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * 
 */
public class Diagnostico {

    /**
     * Default constructor
     */
    public Diagnostico() {
    }

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private String descripcion;

    /**
     * 
     */
    private LocalDateTime fecha;




    /**
     * 
     */
    public void registrar() {
        // TODO implement here
    }

    // Claves foráneas del modelo relacional (columnas id_* de la base de datos)
    private int idHistorial;
    private int idCita;

    // ---- Getters y setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public int getIdHistorial() { return idHistorial; }
    public void setIdHistorial(int idHistorial) { this.idHistorial = idHistorial; }
    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

}
