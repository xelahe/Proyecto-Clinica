package co.edu.poli.atencionmedica.controladores;

import co.edu.poli.atencionmedica.modelo.Paciente;
import co.edu.poli.atencionmedica.servicios.PacienteDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteDAO pacientes;

    public PacienteController(PacienteDAO pacientes) {
        this.pacientes = pacientes;
    }

    @GetMapping
    public Paciente[] listar() {
        return pacientes.readAll();
    }

    @GetMapping("/{id}")
    public Paciente buscar(@PathVariable int id) {
        Paciente paciente = pacientes.read(id);
        if (paciente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el paciente.");
        }
        return paciente;
    }

    @PostMapping
    public ResponseEntity<Paciente> crear(@RequestBody Paciente paciente) {
        paciente.setRol("Paciente");
        pacientes.create(paciente);
        return ResponseEntity.created(URI.create("/api/pacientes/" + paciente.getId()))
            .body(paciente);
    }

    @PutMapping("/{id}")
    public Paciente actualizar(@PathVariable int id, @RequestBody Paciente paciente) {
        paciente.setId(id);
        paciente.setRol("Paciente");
        if (!pacientes.update(id, paciente)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el paciente.");
        }
        return pacientes.read(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (!pacientes.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el paciente.");
        }
        return ResponseEntity.noContent().build();
    }
}
