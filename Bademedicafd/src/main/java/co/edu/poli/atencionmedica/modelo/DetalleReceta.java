package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * Define cada línea de una receta médica, indicando el medicamento, dosis,
 * vía de administración, frecuencia y duración del tratamiento.
 */
public class DetalleReceta {

    /**
     * Default constructor
     */
    public DetalleReceta() {
    }

    /**
     * 
     */
    private int id;

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
    private int frecuencia;

    /**
     * 
     */
    private int duracionDias;

    // Claves foráneas del modelo relacional (columnas id_* de la base de datos)
    private int idReceta;
    private int idMedicamento;

    // ---- Getters y setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }
    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }
    public int getFrecuencia() { return frecuencia; }
    public void setFrecuencia(int frecuencia) { this.frecuencia = frecuencia; }
    public int getDuracionDias() { return duracionDias; }
    public void setDuracionDias(int duracionDias) { this.duracionDias = duracionDias; }
    public int getIdReceta() { return idReceta; }
    public void setIdReceta(int idReceta) { this.idReceta = idReceta; }
    public int getIdMedicamento() { return idMedicamento; }
    public void setIdMedicamento(int idMedicamento) { this.idMedicamento = idMedicamento; }

}
