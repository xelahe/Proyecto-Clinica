package co.edu.poli.atencionmedica.modelo;

import java.time.*;

/**
 * Clase base del sistema que centraliza los datos comunes de identificación,
 * contacto y rol de cualquier usuario del módulo de atención médica.
 */
public class Usuario {

    /**
     * Default constructor
     */
    public Usuario() {
    }

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private String nombre;

    /**
     * 
     */
    private String documento;

    /**
     * 
     */
    private String contacto;

    /**
     * 
     */
    private String direccion;

    /**
     * 
     */
    private String rol;


    /**
     * 
     */
    public void actualizarDatos() {
        // TODO implement here
    }

    // ---- Getters y setters ----
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

}
