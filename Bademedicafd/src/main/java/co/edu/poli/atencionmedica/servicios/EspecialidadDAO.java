package co.edu.poli.atencionmedica.servicios;

import co.edu.poli.atencionmedica.modelo.Especialidad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de la persistencia de las especialidades médicas disponibles en
 * la clínica y de su uso en la asignación de personal de salud.
 */
public class EspecialidadDAO extends BaseDAO implements CRUD {

    public EspecialidadDAO() { this(Conexion.mysql()); }
    public EspecialidadDAO(ProveedorConexion proveedor) { super(proveedor); }

    @Override
    public boolean create(Object entidad) {
        Especialidad e = tipo(entidad, Especialidad.class);
        Validador.especialidad(e);
        return consulta(cn -> {
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO Especialidad(nombre) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, e.getNombre().trim());
                boolean ok = ps.executeUpdate() == 1;
                try (ResultSet k = ps.getGeneratedKeys()) {
                    if (k.next()) e.setId(k.getInt(1));
                }
                return ok;
            }
        });
    }

    @Override
    public Especialidad read(int id) {
        return consulta(cn -> {
            try (PreparedStatement ps = cn.prepareStatement(
                    "SELECT id_especialidad, nombre FROM Especialidad WHERE id_especialidad=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? mapear(rs) : null;
                }
            }
        });
    }

    @Override
    public Especialidad[] readAll() {
        List<Especialidad> lista = consulta(cn -> {
            List<Especialidad> r = new ArrayList<>();
            try (Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery("SELECT id_especialidad, nombre FROM Especialidad ORDER BY nombre")) {
                while (rs.next()) r.add(mapear(rs));
            }
            return r;
        });
        return lista.toArray(new Especialidad[0]);
    }

    @Override
    public boolean update(int id, Object entidad) {
        Especialidad e = tipo(entidad, Especialidad.class);
        Validador.especialidad(e);
        return consulta(cn -> {
            if (!UsuarioSql.existe(cn, "Especialidad", "id_especialidad", id)) return false;
            try (PreparedStatement ps = cn.prepareStatement("UPDATE Especialidad SET nombre=? WHERE id_especialidad=?")) {
                ps.setString(1, e.getNombre().trim());
                ps.setInt(2, id);
                ps.executeUpdate();
            }
            e.setId(id);
            return true;
        });
    }

    @Override
    public boolean delete(int id) {
        return consulta(cn -> UsuarioSql.borrarPor(cn, "Especialidad", "id_especialidad", id) > 0);
    }

    private Especialidad mapear(ResultSet rs) throws SQLException {
        Especialidad e = new Especialidad();
        e.setId(rs.getInt("id_especialidad"));
        e.setNombre(rs.getString("nombre"));
        return e;
    }
}
