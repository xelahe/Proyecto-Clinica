package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * 
 */
public class Tratamiento {

    /**
     * Default constructor
     */
    public Tratamiento() {
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
    private LocalDateTime fechaInicio;

    /**
     * 
     */
    private LocalDateTime fechaFin;

    /**
     * 
     */
    private String estado;



    /**
     * 
     */
    public void iniciar() {
        // TODO implement here
    }

    /**
     * 
     */
    public void finalizar() {
        // TODO implement here
    }

    // Claves foráneas del modelo relacional (columnas id_* de la base de datos)
    private int idDiagnostico;

    // ---- Getters y setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getIdDiagnostico() { return idDiagnostico; }
    public void setIdDiagnostico(int idDiagnostico) { this.idDiagnostico = idDiagnostico; }

}
