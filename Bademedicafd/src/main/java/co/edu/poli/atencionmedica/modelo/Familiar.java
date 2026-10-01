package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * Representa al familiar o acudiente del paciente y su relación con la atención
 * médica y la información clínica del beneficiario.
 */
public class Familiar extends Usuario {

    /**
     * Default constructor
     */
    public Familiar() {
        setRol("Familiar");
    }

    /**
     * 
     */
    private String parentesco;


    /**
     * 
     */
    public void solicitarServicio() {
        // TODO implement here
    }

    /**
     * 
     */
    public void consultarHistorial() {
        // TODO implement here
    }

    /**
     * 
     */
    public void calificarAtencion() {
        // TODO implement here
    }

    // Claves foráneas del modelo relacional (columnas id_* de la base de datos)
    private int idPaciente;

    // ---- Getters y setters ----
    public String getParentesco() { return parentesco; }
    public void setParentesco(String parentesco) { this.parentesco = parentesco; }
    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

}
