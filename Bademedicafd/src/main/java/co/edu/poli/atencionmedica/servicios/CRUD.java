package co.edu.poli.atencionmedica.servicios;

import java.io.*;
import java.util.*;

/**
 * 
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