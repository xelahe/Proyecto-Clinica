package co.edu.poli.atencionmedica.servicios;

import co.edu.poli.atencionmedica.modelo.Medicamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** CRUD de la tabla Medicamento (HU14: inventario de medicamentos). */
public class MedicamentoDAO extends BaseDAO implements CRUD {

    private static final String SELECT =
        "SELECT id_medicamento, nombre, presentacion, concentracion, stock FROM Medicamento";

    public MedicamentoDAO() { this(Conexion.mysql()); }
    public MedicamentoDAO(ProveedorConexion proveedor) { super(proveedor); }

    @Override
    public boolean create(Object entidad) {
        Medicamento m = tipo(entidad, Medicamento.class);
        Validador.medicamento(m);
        return consulta(cn -> {
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO Medicamento(nombre, presentacion, concentracion, stock) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, m.getNombre().trim());
                ps.setString(2, m.getPresentacion());
                ps.setFloat(3, m.getConcentracion());
                ps.setInt(4, m.getStock());
                boolean ok = ps.executeUpdate() == 1;
                try (ResultSet k = ps.getGeneratedKeys()) {
                    if (k.next()) m.setId(k.getInt(1));
                }
                return ok;
            }
        });
    }

    @Override
    public Medicamento read(int id) {
        return consulta(cn -> {
            try (PreparedStatement ps = cn.prepareStatement(SELECT + " WHERE id_medicamento=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? mapear(rs) : null;
                }
            }
        });
    }

    @Override
    public Medicamento[] readAll() {
        List<Medicamento> lista = consulta(cn -> {
            List<Medicamento> r = new ArrayList<>();
            try (Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(SELECT + " ORDER BY nombre")) {
                while (rs.next()) r.add(mapear(rs));
            }
            return r;
        });
        return lista.toArray(new Medicamento[0]);
    }

    @Override
    public boolean update(int id, Object entidad) {
        Medicamento m = tipo(entidad, Medicamento.class);
        Validador.medicamento(m);
        return consulta(cn -> {
            if (!UsuarioSql.existe(cn, "Medicamento", "id_medicamento", id)) return false;
            try (PreparedStatement ps = cn.prepareStatement(
                    "UPDATE Medicamento SET nombre=?, presentacion=?, concentracion=?, stock=? WHERE id_medicamento=?")) {
                ps.setString(1, m.getNombre().trim());
                ps.setString(2, m.getPresentacion());
                ps.setFloat(3, m.getConcentracion());
                ps.setInt(4, m.getStock());
                ps.setInt(5, id);
                ps.executeUpdate();
            }
            m.setId(id);
            return true;
        });
    }

    @Override
    public boolean delete(int id) {
        return consulta(cn -> UsuarioSql.borrarPor(cn, "Medicamento", "id_medicamento", id) > 0);
    }

    private Medicamento mapear(ResultSet rs) throws SQLException {
        Medicamento m = new Medicamento();
        m.setId(rs.getInt("id_medicamento"));
        m.setNombre(rs.getString("nombre"));
        m.setPresentacion(rs.getString("presentacion"));
        m.setConcentracion(rs.getFloat("concentracion"));
        m.setStock(rs.getInt("stock"));
        return m;
    }
}
