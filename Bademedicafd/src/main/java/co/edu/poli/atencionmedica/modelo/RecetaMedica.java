package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * 
 */
public class RecetaMedica {

    /**
     * Default constructor
     */
    public RecetaMedica() {
    }

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private LocalDateTime fechaEmision;




    /**
     * 
     */
    public void emitir() {
        // TODO implement here
    }

    /**
     * 
     */
    public void agregarDetalle() {
        // TODO implement here
    }

    // Claves foráneas del modelo relacional (columnas id_* de la base de datos)
    private int idTratamiento;
    private int idMedico;

    // ---- Getters y setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
    public int getIdTratamiento() { return idTratamiento; }
    public void setIdTratamiento(int idTratamiento) { this.idTratamiento = idTratamiento; }
    public int getIdMedico() { return idMedico; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }

}
