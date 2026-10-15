package co.edu.poli.atencionmedica.modelo;

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

    private String numeroRegistroMedico;

    /** Punto previsto para consultar el historial clínico de un paciente; aún no tiene lógica. */
    public void consultarHistorial() {
        // TODO implement here
    }

    /** Punto previsto para registrar el diagnóstico derivado de la consulta; aún no tiene lógica. */
    public void diagnosticar() {
        // TODO implement here
    }

    /** Punto previsto para emitir una receta vinculada al tratamiento; aún no tiene lógica. */
    public void emitirReceta() {
        // TODO implement here
    }

    // ---- Getters y setters ----
    public String getNumeroRegistroMedico() {
        return numeroRegistroMedico;
    }
    public void setNumeroRegistroMedico(String numeroRegistroMedico) {
        this.numeroRegistroMedico = numeroRegistroMedico;
    }

}
