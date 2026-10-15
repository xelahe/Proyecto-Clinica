package co.edu.poli.atencionmedica.modelo;

/**
 * Modelo del paciente, que amplía la información base del usuario con su
 * condición de salud y necesidades de cuidado.
 */
public class Paciente extends Usuario {

    /**
     * Default constructor
     */
    public Paciente() {
        setRol("Paciente");
    }

    private String condicionSalud;

    private String necesidadesCuidado;

    /** Punto previsto para iniciar el registro del paciente; aún no tiene lógica. */
    public void registrarse() {
        // TODO implement here
    }

    /** Punto previsto para solicitar un servicio de atención médica; aún no tiene lógica. */
    public void solicitarServicio() {
        // TODO implement here
    }

    /** Punto previsto para consultar el historial clínico propio; aún no tiene lógica. */
    public void consultarHistorial() {
        // TODO implement here
    }

    /** Punto previsto para calificar la atención recibida; aún no tiene lógica. */
    public void calificarAtencion() {
        // TODO implement here
    }

    // ---- Getters y setters ----
    public String getCondicionSalud() {
        return condicionSalud;
    }
    public void setCondicionSalud(String condicionSalud) {
        this.condicionSalud = condicionSalud;
    }
    public String getNecesidadesCuidado() {
        return necesidadesCuidado;
    }
    public void setNecesidadesCuidado(String necesidadesCuidado) {
        this.necesidadesCuidado = necesidadesCuidado;
    }

}
