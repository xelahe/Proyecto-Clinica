package co.edu.poli.atencionmedica.servicios;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstrae la creación de conexiones JDBC para que los DAO puedan utilizar
 * distintas implementaciones de acceso a datos, especialmente MySQL en
 * producción o conexiones alternativas en pruebas.
 */
@FunctionalInterface
public interface ProveedorConexion {
    /** Abre una conexión que el DAO cerrará al terminar la operación. */
    Connection abrir() throws SQLException;
}
