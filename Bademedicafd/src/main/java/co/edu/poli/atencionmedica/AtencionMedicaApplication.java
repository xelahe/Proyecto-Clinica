package co.edu.poli.atencionmedica;

import co.edu.poli.atencionmedica.servicios.Conexion;
import co.edu.poli.atencionmedica.servicios.MedicamentoDAO;
import co.edu.poli.atencionmedica.servicios.PacienteDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Punto de entrada de Spring Boot y configuración de los DAO compartidos por
 * los controladores de la aplicación.
 */
@SpringBootApplication
public class AtencionMedicaApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtencionMedicaApplication.class);

    /** Inicia el contenedor de Spring y el servidor web embebido. */
    public static void main(String[] args) {
        SpringApplication.run(AtencionMedicaApplication.class, args);
    }

    /** Verifica MySQL al arrancar y anuncia la dirección local de la aplicación. */
    @Bean
    ApplicationRunner verificarConexionMysql() {
        return args -> {
            try (Connection ignored = Conexion.abrir()) {
                LOGGER.info("Conexión a MySQL (BaseDatoMedicaFD) establecida correctamente.");
                LOGGER.info("Aplicación disponible en http://localhost:8080");
            } catch (SQLException e) {
                throw new IllegalStateException(
                    "No se pudo conectar a MySQL en localhost:3306/BaseDatoMedicaFD. "
                        + "Verifique que el servidor esté activo, que la base exista y que DB_USER/DB_PASSWORD sean correctos.",
                    e
                );
            }
        };
    }

    /** Registra el DAO de pacientes para que Spring lo inyecte en sus consumidores. */
    @Bean
    PacienteDAO pacienteDAO() {
        return new PacienteDAO();
    }

    /** Registra el DAO del inventario de medicamentos para inyección de dependencias. */
    @Bean
    MedicamentoDAO medicamentoDAO() {
        return new MedicamentoDAO();
    }
}
