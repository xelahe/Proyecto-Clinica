package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * Mantiene el historial clínico del paciente como centro de información para
 * diagnósticos, tratamientos y evolución de la atención.
 */
public class HistorialClinico {

    /**
     * Default constructor
     */
    public HistorialClinico() {
    }

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private LocalDateTime fechaCreacion;



    /**
     * 
     */
    public void consultar() {
        // TODO implement here
    }

    // Claves foráneas del modelo relacional (columnas id_* de la base de datos)
    private int idPaciente;

    // ---- Getters y setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

}
