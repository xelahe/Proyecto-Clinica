package co.edu.poli.atencionmedica.servicios;
/**
 * Contrato base para las operaciones CRUD de cada entidad del sistema.
 * Define el ciclo mínimo de persistencia para crear, consultar, actualizar
 * y eliminar registros desde la capa de acceso a datos.
 */
public interface CRUD {

    /** Persiste una entidad del tipo admitido por la implementación del contrato. */
    public boolean create(Object entidad);

    /** Busca por identificador; devuelve {@code null} cuando no existe el registro. */
    public Object read(int id);

    /** Devuelve todos los registros de la entidad gestionada por el DAO. */
    public Object[] readAll();

    /** Actualiza el registro identificado y devuelve {@code false} si no existe. */
    public boolean update(int id, Object entidad);

    /** Elimina el registro identificado y devuelve si se encontró para eliminarlo. */
    public boolean delete(int id);

}