package co.edu.poli.atencionmedica;

import co.edu.poli.atencionmedica.servicios.Conexion;
import co.edu.poli.atencionmedica.servicios.EsquemaH2;
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

    /** Inicializa la base de datos configurada y anuncia la dirección local. */
    @Bean
    ApplicationRunner verificarConexionMysql() {
        return args -> {
            try (Connection conexion = Conexion.abrir()) {
                if (Conexion.usaH2()) {
                    EsquemaH2.inicializar(conexion);
                    LOGGER.info("Conexión a H2 en memoria establecida; sus datos se perderán al cerrar la aplicación.");
                } else {
                    LOGGER.info("Conexión a MySQL (BaseDatoMedicaFD) establecida correctamente.");
                }
                LOGGER.info("Aplicación disponible en http://localhost:8080");
            } catch (SQLException e) {
                throw new IllegalStateException(
                    "No se pudo inicializar o conectar con la base de datos configurada: " + e.getMessage(),
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
