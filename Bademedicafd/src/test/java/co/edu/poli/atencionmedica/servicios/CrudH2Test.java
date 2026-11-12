package co.edu.poli.atencionmedica.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.poli.atencionmedica.modelo.CitaMedica;
import co.edu.poli.atencionmedica.modelo.Disponibilidad;
import co.edu.poli.atencionmedica.modelo.Especialidad;
import co.edu.poli.atencionmedica.modelo.Medicamento;
import co.edu.poli.atencionmedica.modelo.Medico;
import co.edu.poli.atencionmedica.modelo.Paciente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @Test
    void creaPersonalDisponibilidadYCitaConEsquemaH2Completo() {
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre("Cardiología");
        EspecialidadDAO especialidades = new EspecialidadDAO(proveedor);
        assertTrue(especialidades.create(especialidad));

        Medico medico = new Medico();
        medico.setNombre("Dr. Ruiz");
        medico.setDocumento("MED-001");
        medico.setContacto("555-1111");
        medico.setDireccion("Calle 10");
        medico.setTipoPersonal("Medico");
        medico.setCertificaciones("Certificado de cardiología");
        medico.setVigenciaCertificacion(LocalDate.now().plusYears(1));
        medico.setIdEspecialidad(especialidad.getId());
        medico.setNumeroRegistroMedico("REG-2001");

        PersonalDAO personal = new PersonalDAO(proveedor);
        assertTrue(personal.create(medico));
        medico.setNumeroRegistroMedico("REG-2002");
        assertTrue(personal.update(medico.getId(), medico));
        assertEquals("REG-2002", ((Medico) personal.read(medico.getId())).getNumeroRegistroMedico());

        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setDiaSemana("Lunes");
        disponibilidad.setHoraInicio(LocalTime.of(8, 0));
        disponibilidad.setHoraFin(LocalTime.of(12, 0));
        disponibilidad.setIdPersonal(medico.getId());
        DisponibilidadDAO disponibilidades = new DisponibilidadDAO(proveedor);
        assertTrue(disponibilidades.create(disponibilidad));

        Paciente paciente = new Paciente();
        paciente.setNombre("Luis Gomez");
        paciente.setDocumento("PAC-001");
        paciente.setContacto("555-2222");
        paciente.setDireccion("Av. Siempre Viva");
        assertTrue(pacientes.create(paciente));

        CitaMedica cita = new CitaMedica();
        cita.setTipoServicio("Asistencia");
        cita.setMotivo("Consulta de seguimiento");
        cita.setIdPaciente(paciente.getId());
        CitaMedicaDAO citas = new CitaMedicaDAO(proveedor);
        assertTrue(citas.create(cita));
        assertEquals("Pendiente", citas.read(cita.getId()).getEstado());
    }
}
