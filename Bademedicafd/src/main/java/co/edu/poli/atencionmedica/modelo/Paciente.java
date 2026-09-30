package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * 
 */
public class Paciente extends Usuario {

    /**
     * Default constructor
     */
    public Paciente() {
        setRol("Paciente");
    }

    /**
     * 
     */
    private String condicionSalud;

    /**
     * 
     */
    private String necesidadesCuidado;




    /**
     * 
     */
    public void registrarse() {
        // TODO implement here
    }

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

    // ---- Getters y setters ----
    public String getCondicionSalud() { return condicionSalud; }
    public void setCondicionSalud(String condicionSalud) { this.condicionSalud = condicionSalud; }
    public String getNecesidadesCuidado() { return necesidadesCuidado; }
    public void setNecesidadesCuidado(String necesidadesCuidado) { this.necesidadesCuidado = necesidadesCuidado; }

}
