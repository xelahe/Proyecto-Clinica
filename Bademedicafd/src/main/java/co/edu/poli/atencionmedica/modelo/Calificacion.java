package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * 
 */
public class Calificacion {

    /**
     * Default constructor
     */
    public Calificacion() {
    }

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private int estrellas;

    /**
     * 
     */
    private String comentario;

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
    private int idCita;

    // ---- Getters y setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getEstrellas() { return estrellas; }
    public void setEstrellas(int estrellas) { this.estrellas = estrellas; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

}
