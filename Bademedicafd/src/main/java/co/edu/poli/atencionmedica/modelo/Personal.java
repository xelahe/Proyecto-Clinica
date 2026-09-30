package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * 
 */
public class Personal extends Usuario {

    /**
     * Default constructor
     */
    public Personal() {
        setRol("Personal");
    }

    /**
     * 
     */
    private String tipoPersonal;

    /**
     * 
     */
    private String certificaciones;

    /**
     * 
     */
    private LocalDate vigenciaCertificacion;





    /**
     * 
     */
    public void consultarDisponibilidad() {
        // TODO implement here
    }

    /**
     * 
     */
    public void iniciarCita() {
        // TODO implement here
    }

    /**
     * 
     */
    public void completarCita() {
        // TODO implement here
    }

    /**
     * 
     */
    public void registrarAdministracion() {
        // TODO implement here
    }

    // Claves foráneas del modelo relacional (columnas id_* de la base de datos)
    private int idEspecialidad;

    // ---- Getters y setters ----
    public String getTipoPersonal() { return tipoPersonal; }
    public void setTipoPersonal(String tipoPersonal) { this.tipoPersonal = tipoPersonal; }
    public String getCertificaciones() { return certificaciones; }
    public void setCertificaciones(String certificaciones) { this.certificaciones = certificaciones; }
    public LocalDate getVigenciaCertificacion() { return vigenciaCertificacion; }
    public void setVigenciaCertificacion(LocalDate vigenciaCertificacion) { this.vigenciaCertificacion = vigenciaCertificacion; }
    public int getIdEspecialidad() { return idEspecialidad; }
    public void setIdEspecialidad(int idEspecialidad) { this.idEspecialidad = idEspecialidad; }

}
