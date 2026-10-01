package co.edu.poli.atencionmedica.servicios;
/**
 * Contrato base para las operaciones CRUD de cada entidad del sistema.
 * Define el ciclo mínimo de persistencia para crear, consultar, actualizar
 * y eliminar registros desde la capa de acceso a datos.
 */
public interface CRUD {

    /**
     * @param entidad 
     * @return
     */
    public boolean create(Object entidad);

    /**
     * @param id 
     * @return
     */
    public Object read(int id);

    /**
     * @return
     */
    public Object[] readAll();

    /**
     * @param id 
     * @param entidad 
     * @return
     */
    public boolean update(int id, Object entidad);

    /**
     * @param id 
     * @return
     */
    public boolean delete(int id);

}