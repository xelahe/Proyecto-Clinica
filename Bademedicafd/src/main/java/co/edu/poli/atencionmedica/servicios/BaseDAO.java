package co.edu.poli.atencionmedica.servicios;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Utilidades comunes de los DAO: manejo de conexión, transacciones y
 * traducción de errores de MySQL a mensajes entendibles.
 */
abstract class BaseDAO {

    @FunctionalInterface
    interface Trabajo<T> {
        T ejecutar(Connection cn) throws SQLException;
    }

    protected final ProveedorConexion proveedor;

    protected BaseDAO(ProveedorConexion proveedor) {
        this.proveedor = proveedor;
    }

    /** Ejecuta una operación de solo lectura o de una sola sentencia. */
    protected <T> T consulta(Trabajo<T> trabajo) {
        try (Connection cn = proveedor.abrir()) {
            return trabajo.ejecutar(cn);
        } catch (SQLException e) {
            throw traducir(e);
        }
    }

    /** Ejecuta varias sentencias como una sola transacción (todo o nada). */
    protected <T> T transaccion(Trabajo<T> trabajo) {
        try (Connection cn = proveedor.abrir()) {
            cn.setAutoCommit(false);
            try {
                T resultado = trabajo.ejecutar(cn);
                cn.commit();
                return resultado;
            } catch (SQLException | RuntimeException e) {
                cn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw traducir(e);
        }
    }

    private RuntimeException traducir(SQLException e) {
        switch (e.getErrorCode()) {
            case 1062: return new ValidacionException("Ya existe un registro con ese valor único (documento, nombre o número de registro).");
            case 1452: return new ValidacionException("Referencia inválida: el registro relacionado (paciente, personal, especialidad, etc.) no existe.");
            case 1451: return new DaoException("No se puede eliminar: existen registros relacionados que dependen de este.", e);
            case 3819: return new ValidacionException("Un valor incumple una restricción de la base de datos (CHECK).");
            default:   return new DaoException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    protected static Timestamp ts(LocalDateTime f) {
        return f == null ? null : Timestamp.valueOf(f);
    }

    protected static LocalDateTime ldt(Timestamp t) {
        return t == null ? null : t.toLocalDateTime();
    }

    @SuppressWarnings("unchecked")
    protected static <T> T tipo(Object o, Class<T> clase) {
        if (!clase.isInstance(o)) {
            throw new IllegalArgumentException("Se esperaba un objeto " + clase.getSimpleName()
                + " pero se recibió " + (o == null ? "null" : o.getClass().getSimpleName()));
        }
        return (T) o;
    }
}
