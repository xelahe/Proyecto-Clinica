package co.edu.poli.atencionmedica.servicios;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstrae la forma de obtener una conexión JDBC. Permite que los DAO usen
 * MySQL en producción y una conexión distinta (o simulada) en las pruebas.
 */
@FunctionalInterface
public interface ProveedorConexion {
    Connection abrir() throws SQLException;
}
