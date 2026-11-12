package co.edu.poli.atencionmedica.servicios;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Crea las tablas requeridas por el CRUD web cuando se usa H2 en memoria. */
public final class EsquemaH2 {

    private static final String[] SENTENCIAS = {
        "CREATE TABLE IF NOT EXISTS Usuario ("
            + "id_usuario INT AUTO_INCREMENT PRIMARY KEY, "
            + "nombre VARCHAR(150) NOT NULL, "
            + "documento VARCHAR(20) NOT NULL UNIQUE, "
            + "contacto VARCHAR(100) NOT NULL, "
            + "direccion VARCHAR(200) NOT NULL, "
            + "rol VARCHAR(20) NOT NULL, "
            + "CHECK (rol IN ('Paciente','Familiar','Administrador','Personal'))) ",
        "CREATE TABLE IF NOT EXISTS Administrador ("
            + "id_administrador INT PRIMARY KEY, "
            + "FOREIGN KEY (id_administrador) REFERENCES Usuario(id_usuario))",
        "CREATE TABLE IF NOT EXISTS Paciente ("
            + "id_paciente INT PRIMARY KEY, "
            + "condicionSalud VARCHAR(200), "
            + "necesidadesCuidado VARCHAR(200), "
            + "FOREIGN KEY (id_paciente) REFERENCES Usuario(id_usuario))",
        "CREATE TABLE IF NOT EXISTS Familiar ("
            + "id_familiar INT PRIMARY KEY, "
            + "parentesco VARCHAR(50), "
            + "id_paciente INT NOT NULL, "
            + "FOREIGN KEY (id_familiar) REFERENCES Usuario(id_usuario), "
            + "FOREIGN KEY (id_paciente) REFERENCES Paciente(id_paciente))",
        "CREATE TABLE IF NOT EXISTS Especialidad ("
            + "id_especialidad INT AUTO_INCREMENT PRIMARY KEY, "
            + "nombre VARCHAR(100) NOT NULL UNIQUE)",
        "CREATE TABLE IF NOT EXISTS Personal ("
            + "id_personal INT PRIMARY KEY, "
            + "tipoPersonal VARCHAR(20) NOT NULL, "
            + "certificaciones VARCHAR(200), "
            + "vigenciaCertificacion DATE NOT NULL, "
            + "id_especialidad INT NOT NULL, "
            + "FOREIGN KEY (id_personal) REFERENCES Usuario(id_usuario), "
            + "FOREIGN KEY (id_especialidad) REFERENCES Especialidad(id_especialidad), "
            + "CHECK (tipoPersonal IN ('Medico','Enfermero','Cuidador'))) ",
        "CREATE TABLE IF NOT EXISTS Medico ("
            + "id_medico INT PRIMARY KEY, "
            + "numeroRegistroMedico VARCHAR(30) NOT NULL UNIQUE, "
            + "FOREIGN KEY (id_medico) REFERENCES Personal(id_personal))",
        "CREATE TABLE IF NOT EXISTS Disponibilidad ("
            + "id_disponibilidad INT AUTO_INCREMENT PRIMARY KEY, "
            + "diaSemana VARCHAR(15) NOT NULL, "
            + "horaInicio TIME NOT NULL, "
            + "horaFin TIME NOT NULL, "
            + "id_personal INT NOT NULL, "
            + "FOREIGN KEY (id_personal) REFERENCES Personal(id_personal) ON DELETE CASCADE, "
            + "CHECK (diaSemana IN ('Lunes','Martes','Miercoles','Jueves','Viernes','Sabado','Domingo')), "
            + "CHECK (horaFin > horaInicio))",
        "CREATE TABLE IF NOT EXISTS HistorialClinico ("
            + "id_historial INT AUTO_INCREMENT PRIMARY KEY, "
            + "fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
            + "id_paciente INT NOT NULL UNIQUE, "
            + "FOREIGN KEY (id_paciente) REFERENCES Paciente(id_paciente))",
        "CREATE TABLE IF NOT EXISTS CitaMedica ("
            + "id_cita INT AUTO_INCREMENT PRIMARY KEY, "
            + "tipoServicio VARCHAR(20) NOT NULL, "
            + "motivo VARCHAR(200) NOT NULL, "
            + "estado VARCHAR(20) NOT NULL DEFAULT 'Pendiente', "
            + "fechaHoraDeseada TIMESTAMP, "
            + "fechaHoraProgramada TIMESTAMP, "
            + "fechaHoraInicioReal TIMESTAMP, "
            + "fechaHoraFinReal TIMESTAMP, "
            + "observaciones VARCHAR(300), "
            + "id_paciente INT NOT NULL, "
            + "id_personal INT NULL, "
            + "FOREIGN KEY (id_paciente) REFERENCES Paciente(id_paciente), "
            + "FOREIGN KEY (id_personal) REFERENCES Personal(id_personal), "
            + "CHECK (tipoServicio IN ('VisitaMedica','Asistencia')), "
            + "CHECK (estado IN ('Pendiente','Asignada','En curso','Completada','Cancelada')), "
            + "CHECK (fechaHoraFinReal IS NULL OR fechaHoraInicioReal IS NULL OR fechaHoraFinReal >= fechaHoraInicioReal))",
        "CREATE TABLE IF NOT EXISTS Notificacion ("
            + "id_notificacion INT AUTO_INCREMENT PRIMARY KEY, "
            + "tipoEvento VARCHAR(50) NOT NULL, "
            + "fechaHora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
            + "leida BOOLEAN NOT NULL DEFAULT FALSE, "
            + "id_usuario INT NOT NULL, "
            + "id_cita INT NOT NULL, "
            + "FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario), "
            + "FOREIGN KEY (id_cita) REFERENCES CitaMedica(id_cita))",
        "CREATE TABLE IF NOT EXISTS Calificacion ("
            + "id_calificacion INT AUTO_INCREMENT PRIMARY KEY, "
            + "estrellas INT NOT NULL, "
            + "comentario VARCHAR(300), "
            + "fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
            + "id_cita INT NOT NULL UNIQUE, "
            + "FOREIGN KEY (id_cita) REFERENCES CitaMedica(id_cita), "
            + "CHECK (estrellas BETWEEN 1 AND 5))",
        "CREATE TABLE IF NOT EXISTS Diagnostico ("
            + "id_diagnostico INT AUTO_INCREMENT PRIMARY KEY, "
            + "descripcion VARCHAR(300) NOT NULL, "
            + "fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
            + "id_historial INT NOT NULL, "
            + "id_cita INT NOT NULL, "
            + "FOREIGN KEY (id_historial) REFERENCES HistorialClinico(id_historial), "
            + "FOREIGN KEY (id_cita) REFERENCES CitaMedica(id_cita))",
        "CREATE TABLE IF NOT EXISTS Tratamiento ("
            + "id_tratamiento INT AUTO_INCREMENT PRIMARY KEY, "
            + "descripcion VARCHAR(300) NOT NULL, "
            + "fechaInicio TIMESTAMP, "
            + "fechaFin TIMESTAMP, "
            + "estado VARCHAR(20) NOT NULL DEFAULT 'Activo', "
            + "id_diagnostico INT NOT NULL, "
            + "FOREIGN KEY (id_diagnostico) REFERENCES Diagnostico(id_diagnostico), "
            + "CHECK (estado IN ('Activo','Finalizado','Suspendido')))",
        "CREATE TABLE IF NOT EXISTS Medicamento ("
            + "id_medicamento INT AUTO_INCREMENT PRIMARY KEY, "
            + "nombre VARCHAR(100) NOT NULL, "
            + "presentacion VARCHAR(100), "
            + "concentracion FLOAT, "
            + "stock INT NOT NULL DEFAULT 0, "
            + "CHECK (stock >= 0))",
        "CREATE TABLE IF NOT EXISTS RecetaMedica ("
            + "id_receta INT AUTO_INCREMENT PRIMARY KEY, "
            + "fechaEmision TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
            + "id_tratamiento INT NOT NULL, "
            + "id_medico INT NOT NULL, "
            + "FOREIGN KEY (id_tratamiento) REFERENCES Tratamiento(id_tratamiento), "
            + "FOREIGN KEY (id_medico) REFERENCES Medico(id_medico))",
        "CREATE TABLE IF NOT EXISTS DetalleReceta ("
            + "id_detalle INT AUTO_INCREMENT PRIMARY KEY, "
            + "dosis VARCHAR(50) NOT NULL, "
            + "via VARCHAR(50) NOT NULL, "
            + "frecuencia INT NOT NULL, "
            + "duracionDias INT NOT NULL, "
            + "id_receta INT NOT NULL, "
            + "id_medicamento INT NOT NULL, "
            + "FOREIGN KEY (id_receta) REFERENCES RecetaMedica(id_receta) ON DELETE CASCADE, "
            + "FOREIGN KEY (id_medicamento) REFERENCES Medicamento(id_medicamento), "
            + "CHECK (frecuencia > 0 AND duracionDias > 0))",
        "CREATE TABLE IF NOT EXISTS RegistroAdministracionMedicamento ("
            + "id_registro INT AUTO_INCREMENT PRIMARY KEY, "
            + "horaAdministracion TIMESTAMP NOT NULL, "
            + "dosis VARCHAR(50) NOT NULL, "
            + "via VARCHAR(50) NOT NULL, "
            + "observacion VARCHAR(300), "
            + "id_detalle INT NOT NULL, "
            + "id_cita INT NOT NULL, "
            + "id_personal INT NOT NULL, "
            + "FOREIGN KEY (id_detalle) REFERENCES DetalleReceta(id_detalle), "
            + "FOREIGN KEY (id_cita) REFERENCES CitaMedica(id_cita), "
            + "FOREIGN KEY (id_personal) REFERENCES Personal(id_personal))"
    };

    private EsquemaH2() { }

    public static void inicializar(Connection conexion) throws SQLException {
        try (Statement sentencia = conexion.createStatement()) {
            for (String sql : SENTENCIAS) {
                sentencia.execute(sql);
            }
        }
    }
}
