package co.edu.poli.atencionmedica.servicios;

import co.edu.poli.atencionmedica.modelo.Usuario;

import java.sql.*;

/** Sentencias SQL compartidas por Paciente y Personal (ambos heredan de Usuario). */
final class UsuarioSql {

    static final String COLUMNAS = "u.id_usuario, u.nombre, u.documento, u.contacto, u.direccion, u.rol";

    private UsuarioSql() { }

    static void insertar(Connection cn, Usuario u, String rol) throws SQLException {
        String sql = "INSERT INTO Usuario(nombre, documento, contacto, direccion, rol) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombre().trim());
            ps.setString(2, u.getDocumento().trim());
            ps.setString(3, u.getContacto().trim());
            ps.setString(4, u.getDireccion().trim());
            ps.setString(5, rol);
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) {
                k.next();
                u.setId(k.getInt(1));
            }
        }
        u.setRol(rol);
    }

    static void actualizar(Connection cn, int id, Usuario u) throws SQLException {
        String sql = "UPDATE Usuario SET nombre=?, documento=?, contacto=?, direccion=? WHERE id_usuario=?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre().trim());
            ps.setString(2, u.getDocumento().trim());
            ps.setString(3, u.getContacto().trim());
            ps.setString(4, u.getDireccion().trim());
            ps.setInt(5, id);
            ps.executeUpdate();
        }
        u.setId(id);
    }

    static boolean eliminar(Connection cn, int id) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("DELETE FROM Usuario WHERE id_usuario=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    static void llenar(ResultSet rs, Usuario u) throws SQLException {
        u.setId(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setDocumento(rs.getString("documento"));
        u.setContacto(rs.getString("contacto"));
        u.setDireccion(rs.getString("direccion"));
        u.setRol(rs.getString("rol"));
    }

    /** Ejecuta un DELETE ... WHERE columna = id. */
    static int borrarPor(Connection cn, String tabla, String columna, int id) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("DELETE FROM " + tabla + " WHERE " + columna + "=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    static boolean existe(Connection cn, String tabla, String columna, int id) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("SELECT 1 FROM " + tabla + " WHERE " + columna + "=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
