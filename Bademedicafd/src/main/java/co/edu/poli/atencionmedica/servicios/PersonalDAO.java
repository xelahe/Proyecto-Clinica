package co.edu.poli.atencionmedica.servicios;

import co.edu.poli.atencionmedica.modelo.Medico;
import co.edu.poli.atencionmedica.modelo.Personal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO del personal clínico que gestiona la información compartida de Usuario,
 * la especialidad y la certificación asociada. Cuando la persona es un médico,
 * también persiste el número de registro profesional en la tabla Medico.
 */
public class PersonalDAO extends BaseDAO implements CRUD {

    private static final String SELECT =
        "SELECT " + UsuarioSql.COLUMNAS + ", pe.tipoPersonal, pe.certificaciones, pe.vigenciaCertificacion, "
    + "pe.id_especialidad, m.numeroRegistroMedico "
    + "FROM Usuario u JOIN Personal pe ON pe.id_personal = u.id_usuario "
    + "LEFT JOIN Medico m ON m.id_medico = pe.id_personal";

    public PersonalDAO() { this(Conexion.mysql()); }
    public PersonalDAO(ProveedorConexion proveedor) { super(proveedor); }

    @Override
    public boolean create(Object entidad) {
        Personal p = tipo(entidad, Personal.class);
        Validador.personal(p);
        return transaccion(cn -> {
            UsuarioSql.insertar(cn, p, "Personal");
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO Personal(id_personal, tipoPersonal, certificaciones, vigenciaCertificacion, id_especialidad) "
                + "VALUES (?,?,?,?,?)")) {
                ps.setInt(1, p.getId());
                ps.setString(2, p.getTipoPersonal());
                ps.setString(3, p.getCertificaciones());
                ps.setDate(4, java.sql.Date.valueOf(p.getVigenciaCertificacion()));
                ps.setInt(5, p.getIdEspecialidad());
                ps.executeUpdate();
            }
            if (p instanceof Medico m) {
                try (PreparedStatement ps = cn.prepareStatement(
                        "INSERT INTO Medico(id_medico, numeroRegistroMedico) VALUES (?,?)")) {
                    ps.setInt(1, m.getId());
                    ps.setString(2, m.getNumeroRegistroMedico().trim());
                    ps.executeUpdate();
                }
            }
            return true;
        });
    }

    @Override
    public Personal read(int id) {
        return consulta(cn -> {
            try (PreparedStatement ps = cn.prepareStatement(SELECT + " WHERE u.id_usuario=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? mapear(rs) : null;
                }
            }
        });
    }

    @Override
    public Personal[] readAll() {
        List<Personal> lista = consulta(cn -> {
            List<Personal> r = new ArrayList<>();
            try (Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(SELECT + " ORDER BY u.nombre")) {
                while (rs.next()) r.add(mapear(rs));
            }
            return r;
        });
        return lista.toArray(new Personal[0]);
    }

    /** Lista el personal de un tipo: Medico, Enfermero o Cuidador. */
    public Personal[] readPorTipo(String tipoPersonal) {
        List<Personal> lista = consulta(cn -> {
            List<Personal> r = new ArrayList<>();
            try (PreparedStatement ps = cn.prepareStatement(SELECT + " WHERE pe.tipoPersonal=? ORDER BY u.nombre")) {
                ps.setString(1, tipoPersonal);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) r.add(mapear(rs));
                }
            }
            return r;
        });
        return lista.toArray(new Personal[0]);
    }

    @Override
    public boolean update(int id, Object entidad) {
        Personal p = tipo(entidad, Personal.class);
        Validador.personal(p);
        return transaccion(cn -> {
            if (!UsuarioSql.existe(cn, "Personal", "id_personal", id)) return false;
            UsuarioSql.actualizar(cn, id, p);
            try (PreparedStatement ps = cn.prepareStatement(
                    "UPDATE Personal SET tipoPersonal=?, certificaciones=?, vigenciaCertificacion=?, id_especialidad=? "
                  + "WHERE id_personal=?")) {
                ps.setString(1, p.getTipoPersonal());
                ps.setString(2, p.getCertificaciones());
                ps.setDate(3, java.sql.Date.valueOf(p.getVigenciaCertificacion()));
                ps.setInt(4, p.getIdEspecialidad());
                ps.setInt(5, id);
                ps.executeUpdate();
            }
            if (p instanceof Medico m) {
                try (PreparedStatement ps = cn.prepareStatement(
                        "INSERT INTO Medico(id_medico, numeroRegistroMedico) VALUES (?,?) "
                      + "ON DUPLICATE KEY UPDATE numeroRegistroMedico = ?")) {
                    String num = m.getNumeroRegistroMedico().trim();
                    ps.setInt(1, id);
                    ps.setString(2, num);
                    ps.setString(3, num);
                    ps.executeUpdate();
                }
            } else {
                UsuarioSql.borrarPor(cn, "Medico", "id_medico", id);
            }
            return true;
        });
    }

    @Override
    public boolean delete(int id) {
        return transaccion(cn -> {
            UsuarioSql.borrarPor(cn, "Medico", "id_medico", id);
            UsuarioSql.borrarPor(cn, "Personal", "id_personal", id); // Disponibilidad se borra en cascada
            return UsuarioSql.eliminar(cn, id);
        });
    }

    private Personal mapear(ResultSet rs) throws SQLException {
        String numeroRegistro = rs.getString("numeroRegistroMedico");
        Personal p;
        if (numeroRegistro != null) {
            Medico m = new Medico();
            m.setNumeroRegistroMedico(numeroRegistro);
            p = m;
        } else {
            p = new Personal();
        }
        UsuarioSql.llenar(rs, p);
        p.setTipoPersonal(rs.getString("tipoPersonal"));
        p.setCertificaciones(rs.getString("certificaciones"));
        Date vig = rs.getDate("vigenciaCertificacion");
        p.setVigenciaCertificacion(vig == null ? null : vig.toLocalDate());
        p.setIdEspecialidad(rs.getInt("id_especialidad"));
        return p;
    }
}
