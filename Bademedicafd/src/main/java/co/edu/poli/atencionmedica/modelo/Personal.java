package co.edu.poli.atencionmedica.modelo;

import java.time.LocalDate;

/**
 * Representa al personal de salud o apoyo de la clínica con datos de
 * especialidad, certificaciones y disponibilidad para la atención.
 */
public class Personal extends Usuario {

    /**
     * Default constructor
     */
    public Personal() {
        setRol("Personal");
    }

    private String tipoPersonal;

    private String certificaciones;

    private LocalDate vigenciaCertificacion;

    /** Punto previsto para consultar los horarios disponibles del profesional; aún no tiene lógica. */
    public void consultarDisponibilidad() {
        // TODO implement here
    }

    /** Punto previsto para iniciar una cita asignada; aún no tiene lógica. */
    public void iniciarCita() {
        // TODO implement here
    }

    /** Punto previsto para completar una cita en curso; aún no tiene lógica. */
    public void completarCita() {
        // TODO implement here
    }

    /** Punto previsto para registrar la administración de un medicamento; aún no tiene lógica. */
    public void registrarAdministracion() {
        // TODO implement here
    }

    // Claves foráneas del modelo relacional (columnas id_* de la base de datos)
    private int idEspecialidad;

    // ---- Getters y setters ----
    public String getTipoPersonal() {
        return tipoPersonal;
    }
    public void setTipoPersonal(String tipoPersonal) {
        this.tipoPersonal = tipoPersonal;
    }
    public String getCertificaciones() {
        return certificaciones;
    }
    public void setCertificaciones(String certificaciones) {
        this.certificaciones = certificaciones;
    }
    public LocalDate getVigenciaCertificacion() {
        return vigenciaCertificacion;
    }
    public void setVigenciaCertificacion(LocalDate vigenciaCertificacion) {
        this.vigenciaCertificacion = vigenciaCertificacion;
    }
    public int getIdEspecialidad() {
        return idEspecialidad;
    }
    public void setIdEspecialidad(int idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

}
