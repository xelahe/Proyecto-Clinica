package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * 
 */
public class Disponibilidad {

    /**
     * Default constructor
     */
    public Disponibilidad() {
    }

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private String diaSemana;

    /**
     * 
     */
    private LocalTime horaInicio;

    /**
     * 
     */
    private LocalTime horaFin;

    // Claves foráneas del modelo relacional (columnas id_* de la base de datos)
    private int idPersonal;

    // ---- Getters y setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public int getIdPersonal() { return idPersonal; }
    public void setIdPersonal(int idPersonal) { this.idPersonal = idPersonal; }

}
