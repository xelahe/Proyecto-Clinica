package co.edu.poli.atencionmedica.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.poli.atencionmedica.modelo.Medicamento;
import co.edu.poli.atencionmedica.modelo.Paciente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CrudH2Test {

    private ProveedorConexion proveedor;
    private PacienteDAO pacientes;
    private MedicamentoDAO medicamentos;

    @BeforeEach
    void inicializarBaseEnMemoria() throws Exception {
        String url = "jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL;DB_CLOSE_DELAY=-1";
        proveedor = () -> DriverManager.getConnection(url, "sa", "");
        try (Connection conexion = proveedor.abrir()) {
            EsquemaH2.inicializar(conexion);
        }
        pacientes = new PacienteDAO(proveedor);
        medicamentos = new MedicamentoDAO(proveedor);
    }

    @Test
    void permiteCrearConsultarActualizarYEliminarPaciente() {
        Paciente paciente = new Paciente();
        paciente.setNombre("Ana Perez");
        paciente.setDocumento("DOC-123");
        paciente.setContacto("555-0101");
        paciente.setDireccion("Calle 1");
        paciente.setCondicionSalud("Estable");
        paciente.setNecesidadesCuidado("Ninguna");

        assertTrue(pacientes.create(paciente));
        assertNotEquals(0, paciente.getId());
        assertEquals("Ana Perez", pacientes.read(paciente.getId()).getNombre());

        Paciente duplicado = new Paciente();
        duplicado.setNombre("Otra persona");
        duplicado.setDocumento("DOC-123");
        duplicado.setContacto("555-0102");
        duplicado.setDireccion("Calle 2");
        assertThrows(ValidacionException.class, () -> pacientes.create(duplicado));

        paciente.setNombre("Ana Gomez");
        assertTrue(pacientes.update(paciente.getId(), paciente));
        assertEquals("Ana Gomez", pacientes.read(paciente.getId()).getNombre());

        assertTrue(pacientes.delete(paciente.getId()));
        assertNull(pacientes.read(paciente.getId()));
    }

    @Test
    void permiteCrearYConsultarMedicamento() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNombre("Analgesico");
        medicamento.setPresentacion("Tableta");
        medicamento.setConcentracion(500);
        medicamento.setStock(12);

        assertTrue(medicamentos.create(medicamento));
        assertNotEquals(0, medicamento.getId());
        assertNotNull(medicamentos.read(medicamento.getId()));
        assertEquals(1, medicamentos.readAll().length);

        assertTrue(medicamentos.delete(medicamento.getId()));
        assertFalse(medicamentos.delete(medicamento.getId()));
    }
}
