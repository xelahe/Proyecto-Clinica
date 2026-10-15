package co.edu.poli.atencionmedica.modelo;

import java.time.LocalDateTime;

/**
 * Representa un evento, recordatorio o aviso enviado al usuario sobre una cita,
 * atención o cambio relevante del proceso clínico.
 */
public class Notificacion {

    /**
     * Default constructor
     */
    public Notificacion() {
    }

    private int id;

    private String tipoEvento;

    private LocalDateTime fechaHora;

    private boolean leida;

    /** Punto previsto para enviar la notificación al usuario asociado; aún no tiene lógica. */
    public void enviar() {
        // TODO implement here
    }

    /** Punto previsto para marcar el aviso como leído; aún no tiene lógica. */
    public void marcarLeida() {
        // TODO implement here
    }

    // Claves foráneas del modelo relacional (columnas id_* de la base de datos)
    private int idUsuario;
    private int idCita;

    // ---- Getters y setters ----
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTipoEvento() {
        return tipoEvento;
    }
    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    public boolean getLeida() {
        return leida;
    }
    public void setLeida(boolean leida) {
        this.leida = leida;
    }
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public int getIdCita() {
        return idCita;
    }
    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

}
