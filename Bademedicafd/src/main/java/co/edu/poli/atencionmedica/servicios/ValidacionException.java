package co.edu.poli.atencionmedica.servicios;

/**
 * Excepción de validación utilizada cuando un dato incumple una regla de negocio,
 * una restricción del dominio o un criterio de aceptación del sistema.
 */
public class ValidacionException extends RuntimeException {
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
