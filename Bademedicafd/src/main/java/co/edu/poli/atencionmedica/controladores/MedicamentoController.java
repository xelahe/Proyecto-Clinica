package co.edu.poli.atencionmedica.controladores;

import co.edu.poli.atencionmedica.modelo.Medicamento;
import co.edu.poli.atencionmedica.servicios.MedicamentoDAO;
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
@RequestMapping("/api/medicamentos")
public class MedicamentoController {

    private final MedicamentoDAO medicamentos;

    public MedicamentoController(MedicamentoDAO medicamentos) {
        this.medicamentos = medicamentos;
    }

    @GetMapping
    public Medicamento[] listar() {
        return medicamentos.readAll();
    }

    @GetMapping("/{id}")
    public Medicamento buscar(@PathVariable int id) {
        Medicamento medicamento = medicamentos.read(id);
        if (medicamento == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el medicamento.");
        }
        return medicamento;
    }

    @PostMapping
    public ResponseEntity<Medicamento> crear(@RequestBody Medicamento medicamento) {
        medicamentos.create(medicamento);
        return ResponseEntity.created(URI.create("/api/medicamentos/" + medicamento.getId()))
            .body(medicamento);
    }

    @PutMapping("/{id}")
    public Medicamento actualizar(@PathVariable int id, @RequestBody Medicamento medicamento) {
        medicamento.setId(id);
        if (!medicamentos.update(id, medicamento)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el medicamento.");
        }
        return medicamentos.read(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (!medicamentos.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el medicamento.");
        }
        return ResponseEntity.noContent().build();
    }
}
