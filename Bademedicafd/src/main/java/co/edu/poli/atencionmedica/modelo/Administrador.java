package co.edu.poli.atencionmedica.modelo;

/**
 * Representa al administrador del sistema y encapsula las acciones de gestión,
 * control de usuarios y supervisión del flujo clínico.
 */
public class Administrador extends Usuario {

    /**
     * Default constructor
     */
    public Administrador() {
        setRol("Administrador");
    }

    /** Punto previsto para registrar usuarios con perfil de personal; aún no tiene lógica. */
    public void registrarPersonal() {
        // TODO implement here
    }

    /** Punto previsto para asignar personal a las necesidades de atención; aún no tiene lógica. */
    public void asignarPersonal() {
        // TODO implement here
    }

    /** Punto previsto para consultar los historiales clínicos de forma global; aún no tiene lógica. */
    public void consultarHistorialGeneral() {
        // TODO implement here
    }

    /** Punto previsto para gestionar el inventario de medicamentos; aún no tiene lógica. */
    public void gestionarMedicamentos() {
        // TODO implement here
    }

    /** Punto previsto para generar reportes administrativos; aún no tiene lógica. */
    public void generarReportes() {
        // TODO implement here
    }

    // ---- Getters y setters ----

}
