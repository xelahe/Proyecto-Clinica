package co.edu.poli.atencionmedica.servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexión a la base de datos MySQL BaseDatoMedicaFD.
 * Los valores se pueden cambiar sin tocar el código con variables de entorno:
 * DB_URL, DB_USER y DB_PASSWORD.
 */
public final class Conexion {

    private static final String URL_POR_DEFECTO =
        "jdbc:mysql://localhost:3306/BaseDatoMedicaFD?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota";

    private Conexion() { }

    /** Proveedor estándar que usan los DAO por defecto. */
    public static ProveedorConexion mysql() {
        return Conexion::abrir;
    }

    public static Connection abrir() throws SQLException {
        String url  = env("DB_URL", URL_POR_DEFECTO);
        String user = env("DB_USER", "root");
        String pass = env("DB_PASSWORD", "Poli123*");
        return DriverManager.getConnection(url, user, pass);
    }

    private static String env(String clave, String defecto) {
        String v = System.getenv(clave);
        return (v == null || v.isBlank()) ? defecto : v;
    }
}
