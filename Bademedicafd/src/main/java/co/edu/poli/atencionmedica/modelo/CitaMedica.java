package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * 
 */
public class CitaMedica {

    /**
     * Default constructor
     */
    public CitaMedica() {
    }

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private String tipoServicio;

    /**
     * 
     */
    private String motivo;

    /**
     * 
     */
    private String estado;

    /**
     * 
     */
    private LocalDateTime fechaHoraDeseada;

    /**
     * 
     */
    private LocalDateTime fechaHoraProgramada;

    /**
     * 
     */
    private LocalDateTime fechaHoraInicioReal;

    /**
     * 
     */
    private LocalDateTime fechaHoraFinReal;

    /**
     * 
     */
    private String observaciones;







    /**
     * 
     */
    public void crear() {
        // TODO implement here
    }

    /**
     * 
     */
    public void asignar() {
        // TODO implement here
    }

    /**
     * 
     */
    public void iniciar() {
        // TODO implement here
    }

    /**
     * 
     */
    public void completar() {
        // TODO implement here
    }

    /**
     * 
     */
    public void cancelar() {
        // TODO implement here
    }

    // Claves foráneas del modelo relacional (columnas id_* de la base de datos)
    private int idPaciente;
    private Integer idPersonal;

    // ---- Getters y setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaHoraDeseada() { return fechaHoraDeseada; }
    public void setFechaHoraDeseada(LocalDateTime fechaHoraDeseada) { this.fechaHoraDeseada = fechaHoraDeseada; }
    public LocalDateTime getFechaHoraProgramada() { return fechaHoraProgramada; }
    public void setFechaHoraProgramada(LocalDateTime fechaHoraProgramada) { this.fechaHoraProgramada = fechaHoraProgramada; }
    public LocalDateTime getFechaHoraInicioReal() { return fechaHoraInicioReal; }
    public void setFechaHoraInicioReal(LocalDateTime fechaHoraInicioReal) { this.fechaHoraInicioReal = fechaHoraInicioReal; }
    public LocalDateTime getFechaHoraFinReal() { return fechaHoraFinReal; }
    public void setFechaHoraFinReal(LocalDateTime fechaHoraFinReal) { this.fechaHoraFinReal = fechaHoraFinReal; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }
    public Integer getIdPersonal() { return idPersonal; }
    public void setIdPersonal(Integer idPersonal) { this.idPersonal = idPersonal; }

}
