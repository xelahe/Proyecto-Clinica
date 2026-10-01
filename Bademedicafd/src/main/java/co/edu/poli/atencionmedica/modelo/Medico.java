package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * Representa al profesional médico del sistema, incluyendo su número de
 * registro profesional y las acciones de atención médica asociadas.
 */
public class Medico extends Personal {

    /**
     * Default constructor
     */
    public Medico() {
        setTipoPersonal("Medico");
    }

    /**
     * 
     */
    private String numeroRegistroMedico;


    /**
     * 
     */
    public void consultarHistorial() {
        // TODO implement here
    }

    /**
     * 
     */
    public void diagnosticar() {
        // TODO implement here
    }

    /**
     * 
     */
    public void emitirReceta() {
        // TODO implement here
    }

    // ---- Getters y setters ----
    public String getNumeroRegistroMedico() { return numeroRegistroMedico; }
    public void setNumeroRegistroMedico(String numeroRegistroMedico) { this.numeroRegistroMedico = numeroRegistroMedico; }

}
