package co.edu.poli.atencionmedica;

import co.edu.poli.atencionmedica.servicios.PacienteDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AtencionMedicaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtencionMedicaApplication.class, args);
    }

    @Bean
    PacienteDAO pacienteDAO() {
        return new PacienteDAO();
    }
}
