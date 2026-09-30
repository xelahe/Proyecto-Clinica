package co.edu.poli.atencionmedica.servicios;

import co.edu.poli.atencionmedica.modelo.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Reglas de validación de negocio, independientes de la base de datos
 * (por eso se pueden probar con pruebas unitarias sin MySQL).
 * Cada regla corresponde a un criterio de aceptación del Sprint Backlog.
 */
public final class Validador {

    public static final List<String> TIPOS_PERSONAL = List.of("Medico", "Enfermero", "Cuidador");
    public static final List<String> TIPOS_SERVICIO = List.of("VisitaMedica", "Asistencia");
    public static final List<String> ESTADOS_CITA   = List.of("Pendiente", "Asignada", "En curso", "Completada", "Cancelada");
    public static final List<String> DIAS_SEMANA    = List.of("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");

    private Validador() { }

    // ---------- helpers ----------
    static void obligatorio(String valor, String campo, int max) {
        if (valor == null || valor.isBlank()) {
            throw new ValidacionException("El campo '" + campo + "' es obligatorio.");
        }
        opcional(valor, campo, max);
    }

    static void opcional(String valor, String campo, int max) {
        if (valor != null && valor.length() > max) {
            throw new ValidacionException("El campo '" + campo + "' no puede superar " + max + " caracteres.");
        }
    }

    // ---------- HU01: registro de pacientes ----------
    public static void usuario(Usuario u) {
        if (u == null) throw new ValidacionException("El usuario no puede ser nulo.");
        obligatorio(u.getNombre(), "nombre", 150);
        obligatorio(u.getDocumento(), "documento", 20);
        obligatorio(u.getContacto(), "contacto", 100);
        obligatorio(u.getDireccion(), "dirección", 200);
    }

    public static void paciente(Paciente p) {
        usuario(p);
        opcional(p.getCondicionSalud(), "condición de salud", 200);
        opcional(p.getNecesidadesCuidado(), "necesidades de cuidado", 200);
    }

    // ---------- HU02: registro de personal de salud ----------
    public static void personal(Personal p) {
        usuario(p);
        if (p.getTipoPersonal() == null || !TIPOS_PERSONAL.contains(p.getTipoPersonal())) {
            throw new ValidacionException("El tipo de personal debe ser Medico, Enfermero o Cuidador.");
        }
        opcional(p.getCertificaciones(), "certificaciones", 200);
        if (p.getVigenciaCertificacion() == null) {
            throw new ValidacionException("La vigencia de la certificación es obligatoria.");
        }
        if (p.getVigenciaCertificacion().isBefore(LocalDate.now())) {
            throw new ValidacionException("La certificación está vencida; no se puede registrar al personal.");
        }
        if (p.getIdEspecialidad() <= 0) {
            throw new ValidacionException("Debe seleccionar una especialidad.");
        }
        boolean esMedico = "Medico".equals(p.getTipoPersonal());
        if (esMedico && !(p instanceof Medico)) {
            throw new ValidacionException("Un personal de tipo Medico debe registrarse como objeto Medico.");
        }
        if (!esMedico && p instanceof Medico) {
            throw new ValidacionException("Un objeto Medico debe tener tipo de personal 'Medico'.");
        }
        if (p instanceof Medico m) {
            obligatorio(m.getNumeroRegistroMedico(), "número de registro médico", 30);
        }
    }

    public static void disponibilidad(Disponibilidad d) {
        if (d == null) throw new ValidacionException("La disponibilidad no puede ser nula.");
        if (d.getDiaSemana() == null || !DIAS_SEMANA.contains(d.getDiaSemana())) {
            throw new ValidacionException("El día debe ser Lunes, Martes, Miercoles, Jueves, Viernes, Sabado o Domingo.");
        }
        LocalTime ini = d.getHoraInicio(), fin = d.getHoraFin();
        if (ini == null || fin == null) {
            throw new ValidacionException("La hora de inicio y de fin son obligatorias.");
        }
        if (!fin.isAfter(ini)) {
            throw new ValidacionException("La hora de fin debe ser posterior a la hora de inicio.");
        }
        if (d.getIdPersonal() <= 0) {
            throw new ValidacionException("La disponibilidad debe estar asociada a un personal.");
        }
    }

    public static void especialidad(Especialidad e) {
        if (e == null) throw new ValidacionException("La especialidad no puede ser nula.");
        obligatorio(e.getNombre(), "nombre de la especialidad", 100);
    }

    // ---------- HU03 / HU04: solicitud de visita o asistencia ----------
    public static void cita(CitaMedica c) {
        if (c == null) throw new ValidacionException("La cita no puede ser nula.");
        if (c.getTipoServicio() == null || !TIPOS_SERVICIO.contains(c.getTipoServicio())) {
            throw new ValidacionException("El tipo de servicio debe ser VisitaMedica o Asistencia.");
        }
        obligatorio(c.getMotivo(), "motivo", 200);
        opcional(c.getObservaciones(), "observaciones", 300);
        if (c.getEstado() != null && !ESTADOS_CITA.contains(c.getEstado())) {
            throw new ValidacionException("Estado de cita no válido: " + c.getEstado());
        }
        if (c.getIdPaciente() <= 0) {
            throw new ValidacionException("La cita debe estar asociada a un paciente.");
        }
        if (c.getEstado() != null && List.of("Asignada", "En curso", "Completada").contains(c.getEstado())
                && c.getIdPersonal() == null) {
            throw new ValidacionException("Una cita en estado " + c.getEstado() + " debe tener personal asignado.");
        }
        LocalDateTime ini = c.getFechaHoraInicioReal(), fin = c.getFechaHoraFinReal();
        if (ini != null && fin != null && fin.isBefore(ini)) {
            throw new ValidacionException("La fecha de fin real no puede ser anterior a la de inicio real.");
        }
    }

    /** Regla adicional solo al crear: la fecha deseada, si se envía, no puede estar en el pasado. */
    public static void citaNueva(CitaMedica c) {
        cita(c);
        if (c.getFechaHoraDeseada() != null && c.getFechaHoraDeseada().isBefore(LocalDateTime.now())) {
            throw new ValidacionException("La fecha/hora deseada no puede estar en el pasado.");
        }
    }

    // ---------- HU14: inventario de medicamentos ----------
    public static void medicamento(Medicamento m) {
        if (m == null) throw new ValidacionException("El medicamento no puede ser nulo.");
        obligatorio(m.getNombre(), "nombre del medicamento", 100);
        opcional(m.getPresentacion(), "presentación", 100);
        if (m.getStock() < 0) {
            throw new ValidacionException("El stock no puede ser negativo.");
        }
        if (m.getConcentracion() < 0) {
            throw new ValidacionException("La concentración no puede ser negativa.");
        }
    }
}
