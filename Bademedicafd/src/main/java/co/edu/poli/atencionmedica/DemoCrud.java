package co.edu.poli.atencionmedica;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.BooleanSupplier;

import co.edu.poli.atencionmedica.modelo.CitaMedica;
import co.edu.poli.atencionmedica.modelo.Disponibilidad;
import co.edu.poli.atencionmedica.modelo.Especialidad;
import co.edu.poli.atencionmedica.modelo.Medicamento;
import co.edu.poli.atencionmedica.modelo.Medico;
import co.edu.poli.atencionmedica.modelo.Paciente;
import co.edu.poli.atencionmedica.modelo.Personal;
import co.edu.poli.atencionmedica.servicios.CitaMedicaDAO;
import co.edu.poli.atencionmedica.servicios.Conexion;
import co.edu.poli.atencionmedica.servicios.DaoException;
import co.edu.poli.atencionmedica.servicios.DisponibilidadDAO;
import co.edu.poli.atencionmedica.servicios.EspecialidadDAO;
import co.edu.poli.atencionmedica.servicios.MedicamentoDAO;
import co.edu.poli.atencionmedica.servicios.PacienteDAO;
import co.edu.poli.atencionmedica.servicios.PersonalDAO;
import co.edu.poli.atencionmedica.servicios.ValidacionException;

/**
 * Consola interactiva del CRUD contra MySQL.
 *
 * Todo el manejo se hace con menús "switch": el menú principal elige la entidad
 * (Paciente, Personal, Disponibilidad, Especialidad, Cita médica, Medicamento) y
 * cada submenú elige la operación (Create, Read, Update, Delete). Los datos se
 * ingresan por teclado y se validan antes de enviarlos a la base de datos.
 * La opción 7 conserva la demo automática original (crea, lee, actualiza y borra
 * datos de prueba y deja la base como estaba).
 *
 * Ejecutar:  mvn compile exec:java   (con MySQL encendido y el script sql/Basemedicafd2.sql cargado)
 */
public class DemoCrud {

    // ------------------------------------------------------------------
    // Recursos compartidos
    // ------------------------------------------------------------------
    private static final Scanner in = new Scanner(System.in);

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final EspecialidadDAO espDao = new EspecialidadDAO();
    private static final PacienteDAO pacDao = new PacienteDAO();
    private static final PersonalDAO perDao = new PersonalDAO();
    private static final DisponibilidadDAO disDao = new DisponibilidadDAO();
    private static final MedicamentoDAO medDao = new MedicamentoDAO();
    private static final CitaMedicaDAO citaDao = new CitaMedicaDAO();

    // ==================================================================
    // MENÚ PRINCIPAL
    // ==================================================================
    public static void main(String[] args) {
        System.out.println("=======================================================");
        System.out.println("  PLATAFORMA DE ATENCIÓN MÉDICA EN CASA - CRUD");
        System.out.println("=======================================================");
        probarConexion();

        boolean salir = false;
        try {
            while (!salir) {
                System.out.println("\n================ MENÚ PRINCIPAL ================");
                System.out.println("1. Pacientes            (HU01)");
                System.out.println("2. Personal de salud    (HU02)");
                System.out.println("3. Disponibilidad       (HU02)");
                System.out.println("4. Especialidades");
                System.out.println("5. Citas médicas        (HU03 / HU04)");
                System.out.println("6. Medicamentos         (HU14)");
                System.out.println("7. Demo automática (recorre el CRUD con datos de prueba)");
                System.out.println("0. Salir");

                switch (leerOpcion()) {
                    case 1 -> menuPacientes();
                    case 2 -> menuPersonal();
                    case 3 -> menuDisponibilidad();
                    case 4 -> menuEspecialidades();
                    case 5 -> menuCitas();
                    case 6 -> menuMedicamentos();
                    case 7 -> ejecutar(DemoCrud::demoAutomatica);
                    case 0 -> salir = true;
                    default -> opcionInvalida();
                }
            }
            System.out.println("\nHasta pronto.");
        } catch (NoSuchElementException e) {
            System.out.println("\nSe cerró la entrada de datos. Fin del programa.");
        }
    }

    
    /** Avisa al inicio si MySQL no responde, para no descubrirlo en medio de un registro. */
    private static void probarConexion() {
        try (Connection cn = Conexion.abrir()) {
            System.out.println("Conexión con MySQL: OK");
        } catch (SQLException e) {
            System.out.println("AVISO: no se pudo conectar con MySQL (" + e.getMessage() + ").");
            System.out.println("Verifique que MySQL esté encendido, que el script sql/Basemedicafd2.sql esté cargado");
            System.out.println("y, si es necesario, defina DB_URL, DB_USER y DB_PASSWORD.");
        }
    }

    // ==================================================================
    // SUBMENÚS (uno por entidad)
    // ==================================================================
    private static void menuPacientes() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- PACIENTES ---");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Listar todos");
            System.out.println("3. Buscar por id");
            System.out.println("4. Buscar por documento");
            System.out.println("5. Actualizar");
            System.out.println("6. Eliminar");
            System.out.println("0. Volver");

            switch (leerOpcion()) {
                case 1 -> ejecutar(DemoCrud::crearPaciente);
                case 2 -> ejecutar(DemoCrud::listarPacientes);
                case 3 -> ejecutar(DemoCrud::buscarPacientePorId);
                case 4 -> ejecutar(DemoCrud::buscarPacientePorDocumento);
                case 5 -> ejecutar(DemoCrud::actualizarPaciente);
                case 6 -> ejecutar(DemoCrud::eliminarPaciente);
                case 0 -> volver = true;
                default -> opcionInvalida();
            }
        }
    }

    private static void menuPersonal() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- PERSONAL DE SALUD ---");
            System.out.println("1. Registrar personal (Médico, Enfermero o Cuidador)");
            System.out.println("2. Listar todo el personal");
            System.out.println("3. Listar por tipo");
            System.out.println("4. Buscar por id");
            System.out.println("5. Actualizar");
            System.out.println("6. Eliminar");
            System.out.println("0. Volver");

            switch (leerOpcion()) {
                case 1 -> ejecutar(DemoCrud::crearPersonal);
                case 2 -> ejecutar(DemoCrud::listarPersonal);
                case 3 -> ejecutar(DemoCrud::listarPersonalPorTipo);
                case 4 -> ejecutar(DemoCrud::buscarPersonalPorId);
                case 5 -> ejecutar(DemoCrud::actualizarPersonal);
                case 6 -> ejecutar(DemoCrud::eliminarPersonal);
                case 0 -> volver = true;
                default -> opcionInvalida();
            }
        }
    }

    private static void menuDisponibilidad() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- DISPONIBILIDAD DEL PERSONAL ---");
            System.out.println("1. Registrar horario");
            System.out.println("2. Listar todos los horarios");
            System.out.println("3. Listar horarios de un personal");
            System.out.println("4. Buscar por id");
            System.out.println("5. Actualizar");
            System.out.println("6. Eliminar");
            System.out.println("0. Volver");

            switch (leerOpcion()) {
                case 1 -> ejecutar(DemoCrud::crearDisponibilidad);
                case 2 -> ejecutar(DemoCrud::listarDisponibilidades);
                case 3 -> ejecutar(DemoCrud::listarDisponibilidadPorPersonal);
                case 4 -> ejecutar(DemoCrud::buscarDisponibilidadPorId);
                case 5 -> ejecutar(DemoCrud::actualizarDisponibilidad);
                case 6 -> ejecutar(DemoCrud::eliminarDisponibilidad);
                case 0 -> volver = true;
                default -> opcionInvalida();
            }
        }
    }

    private static void menuEspecialidades() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- ESPECIALIDADES ---");
            System.out.println("1. Registrar especialidad");
            System.out.println("2. Listar todas");
            System.out.println("3. Buscar por id");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");

            switch (leerOpcion()) {
                case 1 -> ejecutar(DemoCrud::crearEspecialidad);
                case 2 -> ejecutar(DemoCrud::listarEspecialidades);
                case 3 -> ejecutar(DemoCrud::buscarEspecialidadPorId);
                case 4 -> ejecutar(DemoCrud::actualizarEspecialidad);
                case 5 -> ejecutar(DemoCrud::eliminarEspecialidad);
                case 0 -> volver = true;
                default -> opcionInvalida();
            }
        }
    }

    private static void menuCitas() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- CITAS MÉDICAS ---");
            System.out.println("1. Solicitar cita (queda en estado Pendiente)");
            System.out.println("2. Listar todas");
            System.out.println("3. Buscar por id");
            System.out.println("4. Listar citas de un paciente");
            System.out.println("5. Listar citas por estado");
            System.out.println("6. Asignar personal y fecha (estado Asignada)");
            System.out.println("7. Actualizar todos los datos");
            System.out.println("8. Cancelar cita");
            System.out.println("9. Eliminar");
            System.out.println("0. Volver");

            switch (leerOpcion()) {
                case 1 -> ejecutar(DemoCrud::crearCita);
                case 2 -> ejecutar(DemoCrud::listarCitas);
                case 3 -> ejecutar(DemoCrud::buscarCitaPorId);
                case 4 -> ejecutar(DemoCrud::listarCitasPorPaciente);
                case 5 -> ejecutar(DemoCrud::listarCitasPorEstado);
                case 6 -> ejecutar(DemoCrud::asignarCita);
                case 7 -> ejecutar(DemoCrud::actualizarCita);
                case 8 -> ejecutar(DemoCrud::cancelarCita);
                case 9 -> ejecutar(DemoCrud::eliminarCita);
                case 0 -> volver = true;
                default -> opcionInvalida();
            }
        }
    }

    private static void menuMedicamentos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- MEDICAMENTOS (INVENTARIO) ---");
            System.out.println("1. Registrar medicamento");
            System.out.println("2. Listar todos");
            System.out.println("3. Buscar por id");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");

            switch (leerOpcion()) {
                case 1 -> ejecutar(DemoCrud::crearMedicamento);
                case 2 -> ejecutar(DemoCrud::listarMedicamentos);
                case 3 -> ejecutar(DemoCrud::buscarMedicamentoPorId);
                case 4 -> ejecutar(DemoCrud::actualizarMedicamento);
                case 5 -> ejecutar(DemoCrud::eliminarMedicamento);
                case 0 -> volver = true;
                default -> opcionInvalida();
            }
        }
    }

    // ==================================================================
    // PACIENTES
    // ==================================================================
    private static void crearPaciente() {
        System.out.println("\n== Registrar paciente ==");
        Paciente p = new Paciente();
        p.setNombre(leerTexto("Nombre completo", 150));
        p.setDocumento(leerTexto("Documento", 20));
        p.setContacto(leerTexto("Contacto (teléfono o correo)", 100));
        p.setDireccion(leerTexto("Dirección", 200));
        p.setCondicionSalud(leerTextoOpcional("Condición de salud (Enter para omitir)", 200));
        p.setNecesidadesCuidado(leerTextoOpcional("Necesidades de cuidado (Enter para omitir)", 200));
        if (pacDao.create(p)) {
            System.out.println("Paciente registrado con id " + p.getId() + " (se creó también su historial clínico).");
        }
    }

    private static void listarPacientes() {
        Paciente[] lista = pacDao.readAll();
        if (lista.length == 0) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        System.out.println("\n== Pacientes (" + lista.length + ") ==");
        for (Paciente p : lista) mostrar(p);
    }

    private static void buscarPacientePorId() {
        int id = leerId("ID del paciente");
        Paciente p = pacDao.read(id);
        if (p == null) noExiste("paciente", id); else mostrar(p);
    }

    private static void buscarPacientePorDocumento() {
        String doc = leerTexto("Documento", 20);
        Paciente p = pacDao.readPorDocumento(doc);
        if (p == null) System.out.println("No existe un paciente con documento " + doc + "."); else mostrar(p);
    }

    private static void actualizarPaciente() {
        int id = leerId("ID del paciente a actualizar");
        Paciente p = pacDao.read(id);
        if (p == null) { noExiste("paciente", id); return; }
        mostrar(p);
        avisoEdicion();
        p.setNombre(editarTexto("Nombre", 150, p.getNombre(), true));
        p.setDocumento(editarTexto("Documento", 20, p.getDocumento(), true));
        p.setContacto(editarTexto("Contacto", 100, p.getContacto(), true));
        p.setDireccion(editarTexto("Dirección", 200, p.getDireccion(), true));
        p.setCondicionSalud(editarTexto("Condición de salud", 200, p.getCondicionSalud(), false));
        p.setNecesidadesCuidado(editarTexto("Necesidades de cuidado", 200, p.getNecesidadesCuidado(), false));
        if (pacDao.update(id, p)) System.out.println("Paciente actualizado."); else noExiste("paciente", id);
    }

    private static void eliminarPaciente() {
        int id = leerId("ID del paciente a eliminar");
        Paciente p = pacDao.read(id);
        if (p == null) { noExiste("paciente", id); return; }
        mostrar(p);
        if (confirmar("Se eliminará también su historial clínico. ¿Confirma?")) {
            resultadoEliminar("Paciente", id, pacDao.delete(id));
        }
    }

    // ==================================================================
    // PERSONAL DE SALUD
    // ==================================================================
    private static void crearPersonal() {
        System.out.println("\n== Registrar personal de salud ==");
        String tipo = elegirTipoPersonal();
        Personal p = "Medico".equals(tipo) ? new Medico() : new Personal();
        p.setTipoPersonal(tipo);
        p.setNombre(leerTexto("Nombre completo", 150));
        p.setDocumento(leerTexto("Documento", 20));
        p.setContacto(leerTexto("Contacto (teléfono o correo)", 100));
        p.setDireccion(leerTexto("Dirección", 200));
        p.setCertificaciones(leerTextoOpcional("Certificaciones (Enter para omitir)", 200));
        p.setVigenciaCertificacion(leerFecha("Vigencia de la certificación (yyyy-MM-dd)"));
        if (!hayEspecialidades()) return;
        p.setIdEspecialidad(leerId("ID de la especialidad"));
        if (p instanceof Medico m) {
            m.setNumeroRegistroMedico(leerTexto("Número de registro médico", 30));
        }
        if (perDao.create(p)) {
            System.out.println("Personal registrado con id " + p.getId() + " (" + tipo + ").");
        }
    }

    private static void listarPersonal() {
        mostrarLista(perDao.readAll(), "personal");
    }

    private static void listarPersonalPorTipo() {
        mostrarLista(perDao.readPorTipo(elegirTipoPersonal()), "personal de ese tipo");
    }

    private static void buscarPersonalPorId() {
        int id = leerId("ID del personal");
        Personal p = perDao.read(id);
        if (p == null) noExiste("personal", id); else mostrar(p);
    }

    private static void actualizarPersonal() {
        int id = leerId("ID del personal a actualizar");
        Personal p = perDao.read(id);
        if (p == null) { noExiste("personal", id); return; }
        mostrar(p);
        System.out.println("(El tipo de personal no se puede cambiar: " + p.getTipoPersonal() + ")");
        avisoEdicion();
        p.setNombre(editarTexto("Nombre", 150, p.getNombre(), true));
        p.setDocumento(editarTexto("Documento", 20, p.getDocumento(), true));
        p.setContacto(editarTexto("Contacto", 100, p.getContacto(), true));
        p.setDireccion(editarTexto("Dirección", 200, p.getDireccion(), true));
        p.setCertificaciones(editarTexto("Certificaciones", 200, p.getCertificaciones(), false));
        p.setVigenciaCertificacion(editarFecha("Vigencia de la certificación (yyyy-MM-dd)", p.getVigenciaCertificacion()));
        if (hayEspecialidades()) {
            p.setIdEspecialidad(editarId("ID de la especialidad", p.getIdEspecialidad()));
        }
        if (p instanceof Medico m) {
            m.setNumeroRegistroMedico(editarTexto("Número de registro médico", 30, m.getNumeroRegistroMedico(), true));
        }
        if (perDao.update(id, p)) System.out.println("Personal actualizado."); else noExiste("personal", id);
    }

    private static void eliminarPersonal() {
        int id = leerId("ID del personal a eliminar");
        Personal p = perDao.read(id);
        if (p == null) { noExiste("personal", id); return; }
        mostrar(p);
        if (confirmar("Se eliminarán también sus horarios de disponibilidad. ¿Confirma?")) {
            resultadoEliminar("Personal", id, perDao.delete(id));
        }
    }

    // ==================================================================
    // DISPONIBILIDAD
    // ==================================================================
    private static void crearDisponibilidad() {
        System.out.println("\n== Registrar horario de disponibilidad ==");
        if (!hayPersonal()) return;
        Disponibilidad d = new Disponibilidad();
        d.setIdPersonal(leerId("ID del personal"));
        d.setDiaSemana(elegirDia(null));
        d.setHoraInicio(leerHora("Hora de inicio (HH:mm)"));
        d.setHoraFin(leerHora("Hora de fin (HH:mm)"));
        if (disDao.create(d)) System.out.println("Horario registrado con id " + d.getId() + ".");
    }

    private static void listarDisponibilidades() {
        mostrarLista(disDao.readAll(), "horarios");
    }

    private static void listarDisponibilidadPorPersonal() {
        int idPersonal = leerId("ID del personal");
        mostrarLista(disDao.readPorPersonal(idPersonal), "horarios para ese personal");
    }

    private static void buscarDisponibilidadPorId() {
        int id = leerId("ID del horario");
        Disponibilidad d = disDao.read(id);
        if (d == null) noExiste("horario", id); else mostrar(d);
    }

    private static void actualizarDisponibilidad() {
        int id = leerId("ID del horario a actualizar");
        Disponibilidad d = disDao.read(id);
        if (d == null) { noExiste("horario", id); return; }
        mostrar(d);
        avisoEdicion();
        d.setIdPersonal(editarId("ID del personal", d.getIdPersonal()));
        d.setDiaSemana(elegirDia(d.getDiaSemana()));
        d.setHoraInicio(editarHora("Hora de inicio (HH:mm)", d.getHoraInicio()));
        d.setHoraFin(editarHora("Hora de fin (HH:mm)", d.getHoraFin()));
        if (disDao.update(id, d)) System.out.println("Horario actualizado."); else noExiste("horario", id);
    }

    private static void eliminarDisponibilidad() {
        int id = leerId("ID del horario a eliminar");
        Disponibilidad d = disDao.read(id);
        if (d == null) { noExiste("horario", id); return; }
        mostrar(d);
        if (confirmar("¿Confirma la eliminación?")) resultadoEliminar("Horario", id, disDao.delete(id));
    }

    // ==================================================================
    // ESPECIALIDADES
    // ==================================================================
    private static void crearEspecialidad() {
        System.out.println("\n== Registrar especialidad ==");
        Especialidad e = new Especialidad();
        e.setNombre(leerTexto("Nombre de la especialidad", 100));
        if (espDao.create(e)) System.out.println("Especialidad registrada con id " + e.getId() + ".");
    }

    private static void listarEspecialidades() {
        mostrarLista(espDao.readAll(), "especialidades");
    }

    private static void buscarEspecialidadPorId() {
        int id = leerId("ID de la especialidad");
        Especialidad e = espDao.read(id);
        if (e == null) noExiste("especialidad", id); else mostrar(e);
    }

    private static void actualizarEspecialidad() {
        int id = leerId("ID de la especialidad a actualizar");
        Especialidad e = espDao.read(id);
        if (e == null) { noExiste("especialidad", id); return; }
        mostrar(e);
        avisoEdicion();
        e.setNombre(editarTexto("Nombre", 100, e.getNombre(), true));
        if (espDao.update(id, e)) System.out.println("Especialidad actualizada."); else noExiste("especialidad", id);
    }

    private static void eliminarEspecialidad() {
        int id = leerId("ID de la especialidad a eliminar");
        Especialidad e = espDao.read(id);
        if (e == null) { noExiste("especialidad", id); return; }
        mostrar(e);
        if (confirmar("¿Confirma la eliminación?")) resultadoEliminar("Especialidad", id, espDao.delete(id));
    }

    // ==================================================================
    // CITAS MÉDICAS
    // ==================================================================
    private static void crearCita() {
        System.out.println("\n== Solicitar cita ==");
        if (!hayPacientes()) return;
        CitaMedica c = new CitaMedica();
        c.setIdPaciente(leerId("ID del paciente"));
        c.setTipoServicio(elegirTipoServicio(null));
        c.setMotivo(leerTexto("Motivo", 200));
        c.setFechaHoraDeseada(leerFechaHora("Fecha y hora deseada (yyyy-MM-dd HH:mm, Enter para omitir)", true));
        c.setObservaciones(leerTextoOpcional("Observaciones (Enter para omitir)", 300));
        if (citaDao.create(c)) {
            System.out.println("Cita registrada con id " + c.getId() + " en estado " + c.getEstado() + ".");
        }
    }

    private static void listarCitas() {
        mostrarLista(citaDao.readAll(), "citas");
    }

    private static void buscarCitaPorId() {
        int id = leerId("ID de la cita");
        CitaMedica c = citaDao.read(id);
        if (c == null) noExiste("cita", id); else mostrar(c);
    }

    private static void listarCitasPorPaciente() {
        int idPaciente = leerId("ID del paciente");
        mostrarLista(citaDao.readPorPaciente(idPaciente), "citas para ese paciente");
    }

    private static void listarCitasPorEstado() {
        mostrarLista(citaDao.readPorEstado(elegirEstado(null)), "citas en ese estado");
    }

    /** Flujo del administrador: toma una cita pendiente y le asigna personal y fecha programada. */
    private static void asignarCita() {
        int id = leerId("ID de la cita a asignar");
        CitaMedica c = citaDao.read(id);
        if (c == null) { noExiste("cita", id); return; }
        mostrar(c);
        if ("Cancelada".equals(c.getEstado()) || "Completada".equals(c.getEstado())) {
            System.out.println("No se puede asignar una cita en estado " + c.getEstado() + ".");
            return;
        }
        if (!hayPersonal()) return;
        c.setIdPersonal(leerId("ID del personal a asignar"));
        c.setFechaHoraProgramada(leerFechaHora("Fecha y hora programada (yyyy-MM-dd HH:mm)", false));
        c.setEstado("Asignada");
        if (citaDao.update(id, c)) System.out.println("Cita asignada."); else noExiste("cita", id);
    }

    private static void actualizarCita() {
        int id = leerId("ID de la cita a actualizar");
        CitaMedica c = citaDao.read(id);
        if (c == null) { noExiste("cita", id); return; }
        mostrar(c);
        avisoEdicion();
        System.out.println("(En fechas y en el personal, escriba - para borrar el valor.)");
        c.setTipoServicio(elegirTipoServicio(c.getTipoServicio()));
        c.setMotivo(editarTexto("Motivo", 200, c.getMotivo(), true));
        c.setEstado(elegirEstado(c.getEstado()));
        c.setIdPersonal(editarIdOpcional("ID del personal", c.getIdPersonal()));
        c.setFechaHoraDeseada(editarFechaHora("Fecha y hora deseada (yyyy-MM-dd HH:mm)", c.getFechaHoraDeseada()));
        c.setFechaHoraProgramada(editarFechaHora("Fecha y hora programada (yyyy-MM-dd HH:mm)", c.getFechaHoraProgramada()));
        c.setFechaHoraInicioReal(editarFechaHora("Inicio real (yyyy-MM-dd HH:mm)", c.getFechaHoraInicioReal()));
        c.setFechaHoraFinReal(editarFechaHora("Fin real (yyyy-MM-dd HH:mm)", c.getFechaHoraFinReal()));
        c.setObservaciones(editarTexto("Observaciones", 300, c.getObservaciones(), false));
        if (citaDao.update(id, c)) System.out.println("Cita actualizada."); else noExiste("cita", id);
    }

    private static void cancelarCita() {
        int id = leerId("ID de la cita a cancelar");
        CitaMedica c = citaDao.read(id);
        if (c == null) { noExiste("cita", id); return; }
        mostrar(c);
        if ("Cancelada".equals(c.getEstado()) || "Completada".equals(c.getEstado())) {
            System.out.println("La cita ya está en estado " + c.getEstado() + "; no se puede cancelar.");
            return;
        }
        if (confirmar("¿Confirma la cancelación?")) {
            c.setEstado("Cancelada");
            if (citaDao.update(id, c)) System.out.println("Cita cancelada.");
        }
    }

    private static void eliminarCita() {
        int id = leerId("ID de la cita a eliminar");
        CitaMedica c = citaDao.read(id);
        if (c == null) { noExiste("cita", id); return; }
        mostrar(c);
        if (confirmar("¿Confirma la eliminación?")) resultadoEliminar("Cita", id, citaDao.delete(id));
    }

    // ==================================================================
    // MEDICAMENTOS
    // ==================================================================
    private static void crearMedicamento() {
        System.out.println("\n== Registrar medicamento ==");
        Medicamento m = new Medicamento();
        m.setNombre(leerTexto("Nombre", 100));
        m.setPresentacion(leerTextoOpcional("Presentación (tabletas, jarabe... Enter para omitir)", 100));
        m.setConcentracion(leerDecimal("Concentración (número, 0 si no aplica)"));
        m.setStock(leerEnteroNoNegativo("Stock inicial"));
        if (medDao.create(m)) System.out.println("Medicamento registrado con id " + m.getId() + ".");
    }

    private static void listarMedicamentos() {
        mostrarLista(medDao.readAll(), "medicamentos");
    }

    private static void buscarMedicamentoPorId() {
        int id = leerId("ID del medicamento");
        Medicamento m = medDao.read(id);
        if (m == null) noExiste("medicamento", id); else mostrar(m);
    }

    private static void actualizarMedicamento() {
        int id = leerId("ID del medicamento a actualizar");
        Medicamento m = medDao.read(id);
        if (m == null) { noExiste("medicamento", id); return; }
        mostrar(m);
        avisoEdicion();
        m.setNombre(editarTexto("Nombre", 100, m.getNombre(), true));
        m.setPresentacion(editarTexto("Presentación", 100, m.getPresentacion(), false));
        m.setConcentracion(editarDecimal("Concentración", m.getConcentracion()));
        m.setStock(editarEnteroNoNegativo("Stock", m.getStock()));
        if (medDao.update(id, m)) System.out.println("Medicamento actualizado."); else noExiste("medicamento", id);
    }

    private static void eliminarMedicamento() {
        int id = leerId("ID del medicamento a eliminar");
        Medicamento m = medDao.read(id);
        if (m == null) { noExiste("medicamento", id); return; }
        mostrar(m);
        if (confirmar("¿Confirma la eliminación?")) resultadoEliminar("Medicamento", id, medDao.delete(id));
    }

    // ==================================================================
    // SELECTORES CON SWITCH (opciones fijas que acepta la base de datos)
    // ==================================================================
    /** Tipos válidos de personal: Medico, Enfermero o Cuidador. */
    private static String elegirTipoPersonal() {
        while (true) {
            System.out.println("Tipo de personal:  1. Medico   2. Enfermero   3. Cuidador");
            String tipo = switch (leerEntero("Elija el tipo")) {
                case 1 -> "Medico";
                case 2 -> "Enfermero";
                case 3 -> "Cuidador";
                default -> null;
            };
            if (tipo != null) return tipo;
            opcionInvalida();
        }
    }

    /** Tipo de servicio de la cita. Si {@code actual} no es null, con 0 se deja el valor actual. */
    private static String elegirTipoServicio(String actual) {
        while (true) {
            System.out.println("Tipo de servicio:  1. VisitaMedica   2. Asistencia" + sufijoActual(actual));
            int op = leerEntero("Elija el tipo de servicio");
            if (op == 0 && actual != null) return actual;
            String tipo = switch (op) {
                case 1 -> "VisitaMedica";
                case 2 -> "Asistencia";
                default -> null;
            };
            if (tipo != null) return tipo;
            opcionInvalida();
        }
    }

    /** Estado de la cita. Si {@code actual} no es null, con 0 se deja el valor actual. */
    private static String elegirEstado(String actual) {
        while (true) {
            System.out.println("Estado:  1. Pendiente   2. Asignada   3. En curso   4. Completada   5. Cancelada"
                + sufijoActual(actual));
            int op = leerEntero("Elija el estado");
            if (op == 0 && actual != null) return actual;
            String estado = switch (op) {
                case 1 -> "Pendiente";
                case 2 -> "Asignada";
                case 3 -> "En curso";
                case 4 -> "Completada";
                case 5 -> "Cancelada";
                default -> null;
            };
            if (estado != null) return estado;
            opcionInvalida();
        }
    }

    /** Día de la semana (sin tildes, como lo exige la base de datos). Con 0 se deja el actual si existe. */
    private static String elegirDia(String actual) {
        while (true) {
            System.out.println("Día:  1. Lunes  2. Martes  3. Miercoles  4. Jueves  5. Viernes  6. Sabado  7. Domingo"
                + sufijoActual(actual));
            int op = leerEntero("Elija el día");
            if (op == 0 && actual != null) return actual;
            String dia = switch (op) {
                case 1 -> "Lunes";
                case 2 -> "Martes";
                case 3 -> "Miercoles";
                case 4 -> "Jueves";
                case 5 -> "Viernes";
                case 6 -> "Sabado";
                case 7 -> "Domingo";
                default -> null;
            };
            if (dia != null) return dia;
            opcionInvalida();
        }
    }

    private static String sufijoActual(String actual) {
        return actual == null ? "" : "   0. Dejar igual (" + actual + ")";
    }

    // ==================================================================
    // AYUDAS PARA ELEGIR IDs (muestran lo que existe antes de pedir el id)
    // ==================================================================
    private static boolean hayEspecialidades() {
        Especialidad[] lista = espDao.readAll();
        if (lista.length == 0) {
            System.out.println("Primero registre una especialidad (menú Especialidades).");
            return false;
        }
        System.out.println("Especialidades disponibles:");
        for (Especialidad e : lista) System.out.println("   " + resumen(e));
        return true;
    }

    private static boolean hayPersonal() {
        Personal[] lista = perDao.readAll();
        if (lista.length == 0) {
            System.out.println("Primero registre personal de salud (menú Personal).");
            return false;
        }
        System.out.println("Personal disponible:");
        for (Personal p : lista) System.out.println("   " + resumen(p));
        return true;
    }

    private static boolean hayPacientes() {
        Paciente[] lista = pacDao.readAll();
        if (lista.length == 0) {
            System.out.println("Primero registre un paciente (menú Pacientes).");
            return false;
        }
        System.out.println("Pacientes registrados:");
        for (Paciente p : lista) System.out.println("   [" + p.getId() + "] " + p.getNombre() + " (doc " + p.getDocumento() + ")");
        return true;
    }

    // ==================================================================
    // PRESENTACIÓN DE RESULTADOS
    // ==================================================================
    private static void mostrar(Paciente p) {
        System.out.println("  [" + p.getId() + "] " + p.getNombre() + " | doc: " + p.getDocumento()
            + " | contacto: " + p.getContacto() + " | dirección: " + p.getDireccion()
            + "\n       condición de salud: " + dato(p.getCondicionSalud())
            + " | necesidades de cuidado: " + dato(p.getNecesidadesCuidado()));
    }

    private static void mostrar(Personal p) {
        System.out.println("  [" + p.getId() + "] " + p.getNombre() + " | " + p.getTipoPersonal()
            + " | doc: " + p.getDocumento() + " | contacto: " + p.getContacto() + " | dirección: " + p.getDireccion()
            + "\n       certificaciones: " + dato(p.getCertificaciones())
            + " | vigencia: " + (p.getVigenciaCertificacion() == null ? "-" : p.getVigenciaCertificacion().format(FECHA))
            + " | id especialidad: " + p.getIdEspecialidad()
            + (p instanceof Medico m ? " | registro médico: " + m.getNumeroRegistroMedico() : ""));
    }

    private static void mostrar(Disponibilidad d) {
        System.out.println("  [" + d.getId() + "] personal " + d.getIdPersonal() + " | " + d.getDiaSemana()
            + " " + d.getHoraInicio().format(HORA) + " - " + d.getHoraFin().format(HORA));
    }

    private static void mostrar(Especialidad e) {
        System.out.println("  " + resumen(e));
    }

    private static void mostrar(CitaMedica c) {
        System.out.println("  [" + c.getId() + "] " + c.getTipoServicio() + " | estado: " + c.getEstado()
            + " | paciente: " + c.getIdPaciente() + " | personal: " + (c.getIdPersonal() == null ? "-" : c.getIdPersonal())
            + "\n       motivo: " + c.getMotivo()
            + "\n       deseada: " + fecha(c.getFechaHoraDeseada()) + " | programada: " + fecha(c.getFechaHoraProgramada())
            + " | inicio real: " + fecha(c.getFechaHoraInicioReal()) + " | fin real: " + fecha(c.getFechaHoraFinReal())
            + "\n       observaciones: " + dato(c.getObservaciones()));
    }

    private static void mostrar(Medicamento m) {
        System.out.println("  [" + m.getId() + "] " + m.getNombre() + " | presentación: " + dato(m.getPresentacion())
            + " | concentración: " + m.getConcentracion() + " | stock: " + m.getStock());
    }

    private static String resumen(Especialidad e) {
        return "[" + e.getId() + "] " + e.getNombre();
    }

    private static String resumen(Personal p) {
        return "[" + p.getId() + "] " + p.getNombre() + " (" + p.getTipoPersonal() + ")";
    }

    /** Muestra cualquier arreglo devuelto por un DAO usando el método mostrar() de su tipo. */
    private static void mostrarLista(Object[] lista, String nombre) {
        if (lista.length == 0) {
            System.out.println("No hay " + nombre + ".");
            return;
        }
        System.out.println("\n== " + lista.length + " " + nombre + " ==");
        for (Object o : lista) {
            if (o instanceof Personal p) mostrar(p);
            else if (o instanceof Paciente p) mostrar(p);
            else if (o instanceof Disponibilidad d) mostrar(d);
            else if (o instanceof Especialidad e) mostrar(e);
            else if (o instanceof CitaMedica c) mostrar(c);
            else if (o instanceof Medicamento m) mostrar(m);
        }
    }

    private static String dato(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }

    private static String fecha(LocalDateTime f) {
        return f == null ? "-" : f.format(FECHA_HORA);
    }

    private static void noExiste(String entidad, int id) {
        System.out.println("No existe " + entidad + " con id " + id + ".");
    }

    private static void resultadoEliminar(String entidad, int id, boolean ok) {
        System.out.println(ok ? entidad + " " + id + " eliminado." : "No se eliminó nada: no existe registro con id " + id + ".");
    }

    private static void opcionInvalida() {
        System.out.println("Opción no válida, intente de nuevo.");
    }

    private static void avisoEdicion() {
        System.out.println("Escriba el nuevo valor o pulse Enter para dejar el actual (en campos opcionales, - para borrarlo).");
    }

    /** Ejecuta una operación y muestra los errores de validación o de base de datos sin cerrar el programa. */
    private static void ejecutar(Runnable operacion) {
        try {
            operacion.run();
        } catch (ValidacionException e) {
            System.out.println("\n[DATO NO VÁLIDO] " + e.getMessage());
        } catch (DaoException e) {
            System.out.println("\n[ERROR DE BASE DE DATOS] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    // ==================================================================
    // LECTURA POR CONSOLA (repite la pregunta hasta que el dato sea correcto)
    // ==================================================================
    private static int leerOpcion() {
        System.out.print("\nElija una opción: ");
        String s = in.nextLine().trim();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1; // cae en "default" del switch
        }
    }

    /** Texto obligatorio con largo máximo. */
    private static String leerTexto(String etiqueta, int max) {
        while (true) {
            System.out.print(etiqueta + ": ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) {
                System.out.println("  Este campo es obligatorio.");
            } else if (s.length() > max) {
                System.out.println("  Máximo " + max + " caracteres (escribió " + s.length() + ").");
            } else {
                return s;
            }
        }
    }

    /** Texto opcional: si se pulsa Enter devuelve null. */
    private static String leerTextoOpcional(String etiqueta, int max) {
        while (true) {
            System.out.print(etiqueta + ": ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) return null;
            if (s.length() <= max) return s;
            System.out.println("  Máximo " + max + " caracteres (escribió " + s.length() + ").");
        }
    }

    private static int leerEntero(String etiqueta) {
        while (true) {
            System.out.print(etiqueta + ": ");
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Escriba un número entero.");
            }
        }
    }

    private static int leerId(String etiqueta) {
        while (true) {
            int n = leerEntero(etiqueta);
            if (n > 0) return n;
            System.out.println("  El id debe ser un número mayor que 0.");
        }
    }

    private static int leerEnteroNoNegativo(String etiqueta) {
        while (true) {
            int n = leerEntero(etiqueta);
            if (n >= 0) return n;
            System.out.println("  No puede ser negativo.");
        }
    }

    /** Acepta punto o coma como separador decimal. */
    private static float leerDecimal(String etiqueta) {
        while (true) {
            System.out.print(etiqueta + ": ");
            try {
                float n = Float.parseFloat(in.nextLine().trim().replace(',', '.'));
                if (n >= 0) return n;
                System.out.println("  No puede ser negativo.");
            } catch (NumberFormatException e) {
                System.out.println("  Escriba un número (por ejemplo 500 o 2.5).");
            }
        }
    }

    private static LocalDate leerFecha(String etiqueta) {
        while (true) {
            System.out.print(etiqueta + ": ");
            try {
                return LocalDate.parse(in.nextLine().trim(), FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("  Formato inválido. Use yyyy-MM-dd, por ejemplo 2027-03-15.");
            }
        }
    }

    private static LocalTime leerHora(String etiqueta) {
        while (true) {
            System.out.print(etiqueta + ": ");
            try {
                return LocalTime.parse(in.nextLine().trim(), HORA);
            } catch (DateTimeParseException e) {
                System.out.println("  Formato inválido. Use HH:mm, por ejemplo 08:30.");
            }
        }
    }

    /** Fecha y hora; si {@code opcional} es true, Enter devuelve null. */
    private static LocalDateTime leerFechaHora(String etiqueta, boolean opcional) {
        while (true) {
            System.out.print(etiqueta + ": ");
            String s = in.nextLine().trim();
            if (s.isEmpty() && opcional) return null;
            try {
                return LocalDateTime.parse(s, FECHA_HORA);
            } catch (DateTimeParseException e) {
                System.out.println("  Formato inválido. Use yyyy-MM-dd HH:mm, por ejemplo 2027-03-15 09:30.");
            }
        }
    }

    private static boolean confirmar(String pregunta) {
        while (true) {
            System.out.print(pregunta + " (s/n): ");
            String s = in.nextLine().trim().toLowerCase();
            switch (s) {
                case "s", "si", "sí" -> { return true; }
                case "n", "no" -> { return false; }
                default -> System.out.println("  Responda s o n.");
            }
        }
    }

    // ------------------------------------------------------------------
    // Lectura para ACTUALIZAR: Enter deja el valor actual
    // ------------------------------------------------------------------
    private static String editarTexto(String etiqueta, int max, String actual, boolean obligatorio) {
        while (true) {
            System.out.print(etiqueta + " [" + dato(actual) + "]: ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) return actual;
            if (!obligatorio && s.equals("-")) return null;
            if (s.length() <= max) return s;
            System.out.println("  Máximo " + max + " caracteres (escribió " + s.length() + ").");
        }
    }

    private static int editarId(String etiqueta, int actual) {
        while (true) {
            System.out.print(etiqueta + " [" + actual + "]: ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) return actual;
            try {
                int n = Integer.parseInt(s);
                if (n > 0) return n;
                System.out.println("  El id debe ser mayor que 0.");
            } catch (NumberFormatException e) {
                System.out.println("  Escriba un número entero.");
            }
        }
    }

    /** Id opcional (por ejemplo el personal de una cita): Enter = igual, - = quitar. */
    private static Integer editarIdOpcional(String etiqueta, Integer actual) {
        while (true) {
            System.out.print(etiqueta + " [" + (actual == null ? "-" : actual) + "]: ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) return actual;
            if (s.equals("-")) return null;
            try {
                int n = Integer.parseInt(s);
                if (n > 0) return n;
                System.out.println("  El id debe ser mayor que 0.");
            } catch (NumberFormatException e) {
                System.out.println("  Escriba un número entero o - para quitarlo.");
            }
        }
    }

    private static int editarEnteroNoNegativo(String etiqueta, int actual) {
        while (true) {
            System.out.print(etiqueta + " [" + actual + "]: ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) return actual;
            try {
                int n = Integer.parseInt(s);
                if (n >= 0) return n;
                System.out.println("  No puede ser negativo.");
            } catch (NumberFormatException e) {
                System.out.println("  Escriba un número entero.");
            }
        }
    }

    private static float editarDecimal(String etiqueta, float actual) {
        while (true) {
            System.out.print(etiqueta + " [" + actual + "]: ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) return actual;
            try {
                float n = Float.parseFloat(s.replace(',', '.'));
                if (n >= 0) return n;
                System.out.println("  No puede ser negativo.");
            } catch (NumberFormatException e) {
                System.out.println("  Escriba un número (por ejemplo 500 o 2.5).");
            }
        }
    }

    private static LocalDate editarFecha(String etiqueta, LocalDate actual) {
        while (true) {
            System.out.print(etiqueta + " [" + (actual == null ? "-" : actual.format(FECHA)) + "]: ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) return actual;
            try {
                return LocalDate.parse(s, FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("  Formato inválido. Use yyyy-MM-dd.");
            }
        }
    }

    private static LocalTime editarHora(String etiqueta, LocalTime actual) {
        while (true) {
            System.out.print(etiqueta + " [" + actual.format(HORA) + "]: ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) return actual;
            try {
                return LocalTime.parse(s, HORA);
            } catch (DateTimeParseException e) {
                System.out.println("  Formato inválido. Use HH:mm.");
            }
        }
    }

    /** Fecha y hora opcional: Enter = igual, - = borrar. */
    private static LocalDateTime editarFechaHora(String etiqueta, LocalDateTime actual) {
        while (true) {
            System.out.print(etiqueta + " [" + fecha(actual) + "]: ");
            String s = in.nextLine().trim();
            if (s.isEmpty()) return actual;
            if (s.equals("-")) return null;
            try {
                return LocalDateTime.parse(s, FECHA_HORA);
            } catch (DateTimeParseException e) {
                System.out.println("  Formato inválido. Use yyyy-MM-dd HH:mm.");
            }
        }
    }

    // ==================================================================
    // DEMO AUTOMÁTICA (la versión original, ahora como opción 7 del menú)
    // ==================================================================
    /**
     * Recorre Create, Read, Update y Delete de cada entidad con datos de prueba
     * y deja la base de datos como la encontró. Sirve como guion para la "Demo de producto".
     */
    private static void demoAutomatica() {
        String sufijo = String.valueOf(System.currentTimeMillis() % 1_000_000); // evita documentos duplicados al repetir la demo

        Especialidad esp = new Especialidad();
        Paciente pac = new Paciente();
        Medico med = new Medico();
        Disponibilidad disp = new Disponibilidad();
        Medicamento fam = new Medicamento();
        CitaMedica cita = new CitaMedica();

        try {
            titulo("ESPECIALIDAD");
            esp.setNombre("Geriatría " + sufijo);
            System.out.println("CREATE  -> " + espDao.create(esp) + "  (id=" + esp.getId() + ")");
            System.out.println("READ    -> " + espDao.read(esp.getId()).getNombre());

            titulo("HU01 - PACIENTE");
            pac.setNombre("Ana María Ruiz");
            pac.setDocumento("DEMO" + sufijo);
            pac.setContacto("3001112233");
            pac.setDireccion("Cra 7 # 45-10, Bogotá");
            pac.setCondicionSalud("Movilidad reducida");
            pac.setNecesidadesCuidado("Apoyo para caminar");
            System.out.println("CREATE  -> " + pacDao.create(pac) + "  (id=" + pac.getId() + ")");
            System.out.println("READ    -> " + pacDao.read(pac.getId()).getNombre());
            pac.setCondicionSalud("Movilidad reducida y diabetes");
            System.out.println("UPDATE  -> " + pacDao.update(pac.getId(), pac));
            System.out.println("READ    -> " + pacDao.read(pac.getId()).getCondicionSalud());
            try {
                Paciente duplicado = new Paciente();
                duplicado.setNombre("Otro");
                duplicado.setDocumento(pac.getDocumento());
                duplicado.setContacto("1");
                duplicado.setDireccion("x");
                pacDao.create(duplicado);
            } catch (ValidacionException e) {
                System.out.println("DUPLICADO rechazado -> " + e.getMessage());
            }

            titulo("HU02 - PERSONAL (MÉDICO) Y DISPONIBILIDAD");
            med.setNombre("Dr. Luis Herrera");
            med.setDocumento("MED" + sufijo);
            med.setContacto("3109998877");
            med.setDireccion("Cl 80 # 10-20");
            med.setCertificaciones("Medicina general");
            med.setVigenciaCertificacion(LocalDate.now().plusYears(1));
            med.setIdEspecialidad(esp.getId());
            med.setNumeroRegistroMedico("RM-" + sufijo);
            System.out.println("CREATE  -> " + perDao.create(med) + "  (id=" + med.getId() + ")");
            Personal leido = perDao.read(med.getId());
            System.out.println("READ    -> " + leido.getNombre() + " (" + leido.getClass().getSimpleName() + ")");
            disp.setDiaSemana("Lunes");
            disp.setHoraInicio(LocalTime.of(8, 0));
            disp.setHoraFin(LocalTime.of(12, 0));
            disp.setIdPersonal(med.getId());
            System.out.println("DISPONIBILIDAD CREATE -> " + disDao.create(disp));

            titulo("HU14 - MEDICAMENTO");
            fam.setNombre("Paracetamol " + sufijo);
            fam.setPresentacion("Tabletas");
            fam.setConcentracion(500f);
            fam.setStock(100);
            System.out.println("CREATE  -> " + medDao.create(fam) + "  (id=" + fam.getId() + ")");
            fam.setStock(80);
            System.out.println("UPDATE  -> " + medDao.update(fam.getId(), fam));
            System.out.println("READ    -> stock=" + medDao.read(fam.getId()).getStock());

            titulo("HU03 - CITA MÉDICA");
            cita.setTipoServicio("VisitaMedica");
            cita.setMotivo("Control mensual");
            cita.setFechaHoraDeseada(LocalDateTime.now().plusDays(2));
            cita.setIdPaciente(pac.getId());
            System.out.println("CREATE  -> " + citaDao.create(cita) + "  (id=" + cita.getId() + ", estado=" + cita.getEstado() + ")");
            cita.setEstado("Asignada");
            cita.setIdPersonal(med.getId());
            cita.setFechaHoraProgramada(LocalDateTime.now().plusDays(2).withHour(9).withMinute(0).withSecond(0).withNano(0));
            System.out.println("UPDATE  -> " + citaDao.update(cita.getId(), cita));
            System.out.println("READ    -> estado=" + citaDao.read(cita.getId()).getEstado());
            System.out.println("Citas del paciente: " + citaDao.readPorPaciente(pac.getId()).length);

        } finally {
            titulo("LIMPIEZA (DELETE)");
            intentar("cita", () -> citaDao.delete(cita.getId()));
            intentar("disponibilidad", () -> disDao.delete(disp.getId()));
            intentar("médico", () -> perDao.delete(med.getId()));
            intentar("especialidad", () -> espDao.delete(esp.getId()));
            intentar("medicamento", () -> medDao.delete(fam.getId()));
            intentar("paciente", () -> pacDao.delete(pac.getId()));
        }
    }

    private static void titulo(String t) {
        System.out.println("\n=== " + t + " ===");
    }

    private static void intentar(String nombre, BooleanSupplier op) {
        try {
            System.out.println("DELETE " + nombre + " -> " + op.getAsBoolean());
        } catch (RuntimeException e) {
            System.out.println("DELETE " + nombre + " -> " + e.getMessage());
        }
    }
}