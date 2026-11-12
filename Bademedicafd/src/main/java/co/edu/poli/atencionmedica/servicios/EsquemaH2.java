package co.edu.poli.atencionmedica.servicios;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Crea las tablas requeridas por el CRUD web cuando se usa H2 en memoria. */
public final class EsquemaH2 {

    private static final String[] SENTENCIAS = {
        "CREATE TABLE IF NOT EXISTS Usuario ("
            + "id_usuario INT AUTO_INCREMENT PRIMARY KEY, "
            + "nombre VARCHAR(150) NOT NULL, "
            + "documento VARCHAR(20) NOT NULL UNIQUE, "
            + "contacto VARCHAR(100) NOT NULL, "
            + "direccion VARCHAR(200) NOT NULL, "
            + "rol VARCHAR(20) NOT NULL "
            + "CHECK (rol IN ('Paciente','Familiar','Administrador','Personal')))",
        "CREATE TABLE IF NOT EXISTS Paciente ("
            + "id_paciente INT PRIMARY KEY, "
            + "condicionSalud VARCHAR(200), "
            + "necesidadesCuidado VARCHAR(200), "
            + "FOREIGN KEY (id_paciente) REFERENCES Usuario(id_usuario))",
        "CREATE TABLE IF NOT EXISTS HistorialClinico ("
            + "id_historial INT AUTO_INCREMENT PRIMARY KEY, "
            + "fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
            + "id_paciente INT NOT NULL UNIQUE, "
            + "FOREIGN KEY (id_paciente) REFERENCES Paciente(id_paciente))",
        "CREATE TABLE IF NOT EXISTS Medicamento ("
            + "id_medicamento INT AUTO_INCREMENT PRIMARY KEY, "
            + "nombre VARCHAR(100) NOT NULL, "
            + "presentacion VARCHAR(100), "
            + "concentracion FLOAT, "
            + "stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0))"
    };

    private EsquemaH2() { }

    public static void inicializar(Connection conexion) throws SQLException {
        try (Statement sentencia = conexion.createStatement()) {
            for (String sql : SENTENCIAS) {
                sentencia.execute(sql);
            }
        }
    }
}
