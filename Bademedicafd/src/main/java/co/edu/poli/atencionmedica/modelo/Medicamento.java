package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * Modelo del medicamento disponible en el inventario, con información de
 * presentación, concentración y cantidad disponible en stock.
 */
public class Medicamento {

    /**
     * Default constructor
     */
    public Medicamento() {
    }

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private String nombre;

    /**
     * 
     */
    private String presentacion;

    /**
     * 
     */
    private float concentracion;

    /**
     * 
     */
    private int stock;


    /**
     * 
     */
    public void actualizarStock() {
        // TODO implement here
    }

    // ---- Getters y setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }
    public float getConcentracion() { return concentracion; }
    public void setConcentracion(float concentracion) { this.concentracion = concentracion; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

}
