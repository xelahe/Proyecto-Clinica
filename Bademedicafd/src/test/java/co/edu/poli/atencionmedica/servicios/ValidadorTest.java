package co.edu.poli.atencionmedica.servicios;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import co.edu.poli.atencionmedica.modelo.CitaMedica;
import co.edu.poli.atencionmedica.modelo.Medicamento;

/**
 * Pruebas unitarias de las reglas de validación aplicadas a las citas médicas.
 * Comprueba los casos válidos y las restricciones que deben producir errores.
 */
class ValidadorTest {

    /** Verifica que una cita con tipo de servicio, motivo y paciente sea aceptada. */
    @Test
    void aceptaUnaCitaConDatosValidos() {
        CitaMedica cita = new CitaMedica();
        cita.setTipoServicio("Asistencia");
        cita.setMotivo("Acompanamiento en casa");
        cita.setIdPaciente(1);

        assertDoesNotThrow(() -> Validador.cita(cita));
    }

    /** Verifica que el validador rechace una cita cuyo motivo está vacío. */
    @Test
    void rechazaUnaCitaSinMotivo() {
        CitaMedica cita = new CitaMedica();
        cita.setTipoServicio("Asistencia");
        cita.setMotivo(" ");
        cita.setIdPaciente(1);
        ValidacionException excepcion = assertThrows(
            ValidacionException.class,
            () -> Validador.cita(cita)
        );
        assertEquals("El campo 'motivo' es obligatorio.", excepcion.getMessage());
    }

    /** Verifica que una cita asignada no pueda quedar sin personal responsable. */
    @Test
    void rechazaUnaCitaAsignadaSinPersonal() {
        CitaMedica cita = new CitaMedica();
        cita.setTipoServicio("VisitaMedica");
        cita.setMotivo("Consulta medica");
        cita.setEstado("Asignada");
        cita.setIdPaciente(1);
        ValidacionException excepcion = assertThrows(
            ValidacionException.class,
            () -> Validador.cita(cita)
        );
        assertEquals("Una cita en estado Asignada debe tener personal asignado.", excepcion.getMessage());
    }

    @Test
    void aceptaTelefonoDeSoloDigitosYRechazaOtrosCaracteres() {
        assertDoesNotThrow(() -> Validador.telefono("3001234567"));
        assertThrows(ValidacionException.class, () -> Validador.telefono("+573001234567"));
        assertThrows(ValidacionException.class, () -> Validador.telefono("300-123-4567"));
        assertThrows(ValidacionException.class, () -> Validador.telefono("1234567890123456"));
    }

    @Test
    void rechazaConcentracionNoFinita() {
        Medicamento medicamento = new Medicamento();
        medicamento.setNombre("Medicamento");
        medicamento.setConcentracion(Float.POSITIVE_INFINITY);
        assertThrows(ValidacionException.class, () -> Validador.medicamento(medicamento));
    }
}