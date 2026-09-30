package co.edu.poli.atencionmedica.servicios;

/** Se lanza cuando la base de datos falla (conexión, llaves foráneas, etc.). */
public class DaoException extends RuntimeException {
    public DaoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
