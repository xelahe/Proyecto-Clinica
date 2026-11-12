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
        "jdbc:h2:mem:bademedicafd;MODE=MySQL;DB_CLOSE_DELAY=-1";

    private Conexion() { }

    /** Proveedor configurado que usan los DAO por defecto. */
    public static ProveedorConexion configurada() {
        return Conexion::abrir;
    }

    /** Indica si la conexión configurada usa la base de datos embebida H2. */
    public static boolean usaH2() {
        return env("DB_URL", URL_POR_DEFECTO).startsWith("jdbc:h2:");
    }

    /** Abre una conexión JDBC usando H2 por defecto o la URL configurada. */
    public static Connection abrir() throws SQLException {
        String url  = env("DB_URL", URL_POR_DEFECTO);
        boolean h2 = url.startsWith("jdbc:h2:");
        String user = env("DB_USER", h2 ? "sa" : "root");
        String pass = System.getenv("DB_PASSWORD");
        if (pass == null && h2) {
            pass = "";
        } else if (pass == null || pass.isBlank()) {
            throw new SQLException("Debe definir la variable de entorno DB_PASSWORD para conectarse a MySQL.");
        }
        return DriverManager.getConnection(url, user, pass);
    }

    /** Lee una variable de entorno y usa el valor alternativo si no está definida o está vacía. */
    private static String env(String clave, String defecto) {
        String v = System.getenv(clave);
        return (v == null || v.isBlank()) ? defecto : v;
    }
}
