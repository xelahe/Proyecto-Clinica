package co.edu.poli.atencionmedica.servicios;

import co.edu.poli.atencionmedica.modelo.CitaMedica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD de CitaMedica (HU03 y HU04: solicitud de visita médica o asistencia).
 * Al crear, la cita siempre queda en estado "Pendiente" y sin personal asignado.
 */
public class CitaMedicaDAO extends BaseDAO implements CRUD {

    private static final String SELECT =
        "SELECT id_cita, tipoServicio, motivo, estado, fechaHoraDeseada, fechaHoraProgramada, "
      + "fechaHoraInicioReal, fechaHoraFinReal, observaciones, id_paciente, id_personal FROM CitaMedica";

    public CitaMedicaDAO() { this(Conexion.mysql()); }
    public CitaMedicaDAO(ProveedorConexion proveedor) { super(proveedor); }

    @Override
    public boolean create(Object entidad) {
        CitaMedica c = tipo(entidad, CitaMedica.class);
        c.setEstado("Pendiente");
        c.setIdPersonal(null);
        Validador.citaNueva(c);
        return consulta(cn -> {
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO CitaMedica(tipoServicio, motivo, estado, fechaHoraDeseada, observaciones, id_paciente) "
                  + "VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, c.getTipoServicio());
                ps.setString(2, c.getMotivo().trim());
                ps.setString(3, c.getEstado());
                ps.setTimestamp(4, ts(c.getFechaHoraDeseada()));
                ps.setString(5, c.getObservaciones());
                ps.setInt(6, c.getIdPaciente());
                boolean ok = ps.executeUpdate() == 1;
                try (ResultSet k = ps.getGeneratedKeys()) {
                    if (k.next()) c.setId(k.getInt(1));
                }
                return ok;
            }
        });
    }

    @Override
    public CitaMedica read(int id) {
        return consulta(cn -> {
            try (PreparedStatement ps = cn.prepareStatement(SELECT + " WHERE id_cita=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? mapear(rs) : null;
                }
            }
        });
    }

    @Override
    public CitaMedica[] readAll() {
        return listar(SELECT + " ORDER BY id_cita", null);
    }

    /** Historial de solicitudes de un paciente (HU05/HU10: el paciente ve el estado de sus solicitudes). */
    public CitaMedica[] readPorPaciente(int idPaciente) {
        return listar(SELECT + " WHERE id_paciente=? ORDER BY id_cita", idPaciente);
    }

    /** Solicitudes en un estado dado, p. ej. "Pendiente" para la vista del administrador. */
    public CitaMedica[] readPorEstado(String estado) {
        List<CitaMedica> lista = consulta(cn -> {
            List<CitaMedica> r = new ArrayList<>();
            try (PreparedStatement ps = cn.prepareStatement(SELECT + " WHERE estado=? ORDER BY id_cita")) {
                ps.setString(1, estado);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) r.add(mapear(rs));
                }
            }
            return r;
        });
        return lista.toArray(new CitaMedica[0]);
    }

    /** Actualiza los datos de la cita. El paciente dueño de la cita no se puede cambiar. */
    @Override
    public boolean update(int id, Object entidad) {
        CitaMedica c = tipo(entidad, CitaMedica.class);
        Validador.cita(c);
        return consulta(cn -> {
            if (!UsuarioSql.existe(cn, "CitaMedica", "id_cita", id)) return false;
            try (PreparedStatement ps = cn.prepareStatement(
                    "UPDATE CitaMedica SET tipoServicio=?, motivo=?, estado=?, fechaHoraDeseada=?, fechaHoraProgramada=?, "
                  + "fechaHoraInicioReal=?, fechaHoraFinReal=?, observaciones=?, id_personal=? WHERE id_cita=?")) {
                ps.setString(1, c.getTipoServicio());
                ps.setString(2, c.getMotivo().trim());
                ps.setString(3, c.getEstado() == null ? "Pendiente" : c.getEstado());
                ps.setTimestamp(4, ts(c.getFechaHoraDeseada()));
                ps.setTimestamp(5, ts(c.getFechaHoraProgramada()));
                ps.setTimestamp(6, ts(c.getFechaHoraInicioReal()));
                ps.setTimestamp(7, ts(c.getFechaHoraFinReal()));
                ps.setString(8, c.getObservaciones());
                if (c.getIdPersonal() == null) ps.setNull(9, Types.INTEGER); else ps.setInt(9, c.getIdPersonal());
                ps.setInt(10, id);
                ps.executeUpdate();
            }
            c.setId(id);
            return true;
        });
    }

    @Override
    public boolean delete(int id) {
        return consulta(cn -> UsuarioSql.borrarPor(cn, "CitaMedica", "id_cita", id) > 0);
    }

    private CitaMedica[] listar(String sql, Integer parametro) {
        List<CitaMedica> lista = consulta(cn -> {
            List<CitaMedica> r = new ArrayList<>();
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                if (parametro != null) ps.setInt(1, parametro);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) r.add(mapear(rs));
                }
            }
            return r;
        });
        return lista.toArray(new CitaMedica[0]);
    }

    private CitaMedica mapear(ResultSet rs) throws SQLException {
        CitaMedica c = new CitaMedica();
        c.setId(rs.getInt("id_cita"));
        c.setTipoServicio(rs.getString("tipoServicio"));
        c.setMotivo(rs.getString("motivo"));
        c.setEstado(rs.getString("estado"));
        c.setFechaHoraDeseada(ldt(rs.getTimestamp("fechaHoraDeseada")));
        c.setFechaHoraProgramada(ldt(rs.getTimestamp("fechaHoraProgramada")));
        c.setFechaHoraInicioReal(ldt(rs.getTimestamp("fechaHoraInicioReal")));
        c.setFechaHoraFinReal(ldt(rs.getTimestamp("fechaHoraFinReal")));
        c.setObservaciones(rs.getString("observaciones"));
        c.setIdPaciente(rs.getInt("id_paciente"));
        int personal = rs.getInt("id_personal");
        c.setIdPersonal(rs.wasNull() ? null : personal);
        return c;
    }
}
