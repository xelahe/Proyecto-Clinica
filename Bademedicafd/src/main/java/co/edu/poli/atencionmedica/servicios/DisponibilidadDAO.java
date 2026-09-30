package co.edu.poli.atencionmedica.servicios;

import co.edu.poli.atencionmedica.modelo.Disponibilidad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** CRUD de Disponibilidad: días y horarios del personal de salud (HU02). */
public class DisponibilidadDAO extends BaseDAO implements CRUD {

    private static final String SELECT =
        "SELECT id_disponibilidad, diaSemana, horaInicio, horaFin, id_personal FROM Disponibilidad";

    public DisponibilidadDAO() { this(Conexion.mysql()); }
    public DisponibilidadDAO(ProveedorConexion proveedor) { super(proveedor); }

    @Override
    public boolean create(Object entidad) {
        Disponibilidad d = tipo(entidad, Disponibilidad.class);
        Validador.disponibilidad(d);
        return consulta(cn -> {
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO Disponibilidad(diaSemana, horaInicio, horaFin, id_personal) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, d.getDiaSemana());
                ps.setTime(2, Time.valueOf(d.getHoraInicio()));
                ps.setTime(3, Time.valueOf(d.getHoraFin()));
                ps.setInt(4, d.getIdPersonal());
                boolean ok = ps.executeUpdate() == 1;
                try (ResultSet k = ps.getGeneratedKeys()) {
                    if (k.next()) d.setId(k.getInt(1));
                }
                return ok;
            }
        });
    }

    @Override
    public Disponibilidad read(int id) {
        return consulta(cn -> {
            try (PreparedStatement ps = cn.prepareStatement(SELECT + " WHERE id_disponibilidad=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? mapear(rs) : null;
                }
            }
        });
    }

    @Override
    public Disponibilidad[] readAll() {
        List<Disponibilidad> lista = consulta(cn -> {
            List<Disponibilidad> r = new ArrayList<>();
            try (Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(SELECT + " ORDER BY id_personal, id_disponibilidad")) {
                while (rs.next()) r.add(mapear(rs));
            }
            return r;
        });
        return lista.toArray(new Disponibilidad[0]);
    }

    /** Todos los horarios de un miembro del personal. */
    public Disponibilidad[] readPorPersonal(int idPersonal) {
        List<Disponibilidad> lista = consulta(cn -> {
            List<Disponibilidad> r = new ArrayList<>();
            try (PreparedStatement ps = cn.prepareStatement(SELECT + " WHERE id_personal=? ORDER BY id_disponibilidad")) {
                ps.setInt(1, idPersonal);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) r.add(mapear(rs));
                }
            }
            return r;
        });
        return lista.toArray(new Disponibilidad[0]);
    }

    @Override
    public boolean update(int id, Object entidad) {
        Disponibilidad d = tipo(entidad, Disponibilidad.class);
        Validador.disponibilidad(d);
        return consulta(cn -> {
            if (!UsuarioSql.existe(cn, "Disponibilidad", "id_disponibilidad", id)) return false;
            try (PreparedStatement ps = cn.prepareStatement(
                    "UPDATE Disponibilidad SET diaSemana=?, horaInicio=?, horaFin=?, id_personal=? WHERE id_disponibilidad=?")) {
                ps.setString(1, d.getDiaSemana());
                ps.setTime(2, Time.valueOf(d.getHoraInicio()));
                ps.setTime(3, Time.valueOf(d.getHoraFin()));
                ps.setInt(4, d.getIdPersonal());
                ps.setInt(5, id);
                ps.executeUpdate();
            }
            d.setId(id);
            return true;
        });
    }

    @Override
    public boolean delete(int id) {
        return consulta(cn -> UsuarioSql.borrarPor(cn, "Disponibilidad", "id_disponibilidad", id) > 0);
    }

    private Disponibilidad mapear(ResultSet rs) throws SQLException {
        Disponibilidad d = new Disponibilidad();
        d.setId(rs.getInt("id_disponibilidad"));
        d.setDiaSemana(rs.getString("diaSemana"));
        d.setHoraInicio(rs.getTime("horaInicio").toLocalTime());
        d.setHoraFin(rs.getTime("horaFin").toLocalTime());
        d.setIdPersonal(rs.getInt("id_personal"));
        return d;
    }
}
