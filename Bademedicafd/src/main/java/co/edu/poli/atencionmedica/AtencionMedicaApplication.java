package co.edu.poli.atencionmedica;

import co.edu.poli.atencionmedica.servicios.MedicamentoDAO;
import co.edu.poli.atencionmedica.servicios.PacienteDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Punto de entrada de Spring Boot y configuración de los DAO compartidos por
 * los controladores de la aplicación.
 */
@SpringBootApplication
public class AtencionMedicaApplication {

    /** Inicia el contenedor de Spring y el servidor web embebido. */
    public static void main(String[] args) {
        SpringApplication.run(AtencionMedicaApplication.class, args);
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
