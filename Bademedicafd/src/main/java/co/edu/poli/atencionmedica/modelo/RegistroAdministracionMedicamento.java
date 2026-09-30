package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * 
 */
public class RegistroAdministracionMedicamento {

    /**
     * Default constructor
     */
    public RegistroAdministracionMedicamento() {
    }

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private LocalDateTime horaAdministracion;

    /**
     * 
     */
    private String dosis;

    /**
     * 
     */
    private String via;

    /**
     * 
     */
    private String observacion;




    /**
     * 
     */
    public void registrar() {
        // TODO implement here
    }

    // Claves foráneas del modelo relacional (columnas id_* de la base de datos)
    private int idDetalle;
    private int idCita;
    private int idPersonal;

    // ---- Getters y setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getHoraAdministracion() { return horaAdministracion; }
    public void setHoraAdministracion(LocalDateTime horaAdministracion) { this.horaAdministracion = horaAdministracion; }
    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }
    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }
    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }
    public int getIdPersonal() { return idPersonal; }
    public void setIdPersonal(int idPersonal) { this.idPersonal = idPersonal; }

}
