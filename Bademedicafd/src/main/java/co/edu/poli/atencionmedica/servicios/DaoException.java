package co.edu.poli.atencionmedica.servicios;

/**
 * Excepción de acceso a datos que indica un fallo técnico de la base de datos,
 * como problemas de conexión, restricciones de integridad o errores de SQL.
 */
public class DaoException extends RuntimeException {
    public DaoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
