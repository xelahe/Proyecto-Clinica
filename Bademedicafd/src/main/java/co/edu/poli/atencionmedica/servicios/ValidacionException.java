package co.edu.poli.atencionmedica.servicios;

/** Se lanza cuando un dato incumple una regla de negocio o un criterio de aceptación. */
public class ValidacionException extends RuntimeException {
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
