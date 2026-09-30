package co.edu.poli.atencionmedica.servicios;

import co.edu.poli.atencionmedica.modelo.Paciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD de Paciente (HU01). Un paciente ocupa tres tablas:
 * Usuario + Paciente + HistorialClinico, que se manejan en una sola transacción.
 */
public class PacienteDAO extends BaseDAO implements CRUD {

    private static final String SELECT =
        "SELECT " + UsuarioSql.COLUMNAS + ", p.condicionSalud, p.necesidadesCuidado "
      + "FROM Usuario u JOIN Paciente p ON p.id_paciente = u.id_usuario";

    public PacienteDAO() { this(Conexion.mysql()); }
    public PacienteDAO(ProveedorConexion proveedor) { super(proveedor); }

    @Override
    public boolean create(Object entidad) {
        Paciente p = tipo(entidad, Paciente.class);
        Validador.paciente(p);
        return transaccion(cn -> {
            UsuarioSql.insertar(cn, p, "Paciente");
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO Paciente(id_paciente, condicionSalud, necesidadesCuidado) VALUES (?,?,?)")) {
                ps.setInt(1, p.getId());
                ps.setString(2, p.getCondicionSalud());
                ps.setString(3, p.getNecesidadesCuidado());
                ps.executeUpdate();
            }
            // Cada paciente nace con su historial clínico (relación 1 a 1).
            try (PreparedStatement ps = cn.prepareStatement("INSERT INTO HistorialClinico(id_paciente) VALUES (?)")) {
                ps.setInt(1, p.getId());
                ps.executeUpdate();
            }
            return true;
        });
    }

    @Override
    public Paciente read(int id) {
        return consulta(cn -> {
            try (PreparedStatement ps = cn.prepareStatement(SELECT + " WHERE u.id_usuario=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? mapear(rs) : null;
                }
            }
        });
    }

    /** Búsqueda por documento (HU01: no se permiten documentos duplicados). */
    public Paciente readPorDocumento(String documento) {
        return consulta(cn -> {
            try (PreparedStatement ps = cn.prepareStatement(SELECT + " WHERE u.documento=?")) {
                ps.setString(1, documento);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? mapear(rs) : null;
                }
            }
        });
    }

    @Override
    public Paciente[] readAll() {
        List<Paciente> lista = consulta(cn -> {
            List<Paciente> r = new ArrayList<>();
            try (Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(SELECT + " ORDER BY u.nombre")) {
                while (rs.next()) r.add(mapear(rs));
            }
            return r;
        });
        return lista.toArray(new Paciente[0]);
    }

    @Override
    public boolean update(int id, Object entidad) {
        Paciente p = tipo(entidad, Paciente.class);
        Validador.paciente(p);
        return transaccion(cn -> {
            if (!UsuarioSql.existe(cn, "Paciente", "id_paciente", id)) return false;
            UsuarioSql.actualizar(cn, id, p);
            try (PreparedStatement ps = cn.prepareStatement(
                    "UPDATE Paciente SET condicionSalud=?, necesidadesCuidado=? WHERE id_paciente=?")) {
                ps.setString(1, p.getCondicionSalud());
                ps.setString(2, p.getNecesidadesCuidado());
                ps.setInt(3, id);
                ps.executeUpdate();
            }
            return true;
        });
    }

    @Override
    public boolean delete(int id) {
        return transaccion(cn -> {
            UsuarioSql.borrarPor(cn, "HistorialClinico", "id_paciente", id);
            UsuarioSql.borrarPor(cn, "Paciente", "id_paciente", id);
            return UsuarioSql.eliminar(cn, id);
        });
    }

    private Paciente mapear(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        UsuarioSql.llenar(rs, p);
        p.setCondicionSalud(rs.getString("condicionSalud"));
        p.setNecesidadesCuidado(rs.getString("necesidadesCuidado"));
        return p;
    }
}
