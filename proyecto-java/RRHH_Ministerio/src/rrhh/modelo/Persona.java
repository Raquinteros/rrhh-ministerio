package rrhh.modelo;

/**
 * Clase abstracta que representa una persona dentro del sistema.
 * 
 * Se utiliza como clase base para concentrar atributos comunes como DNI,
 * CUIL, apellido y nombre. Permite aplicar herencia y abstracción dentro
 * del prototipo.
 */
public abstract class Persona {

    private String dni;
    private String cuil;
    private String apellido;
    private String nombre;

    public Persona(String dni, String cuil, String apellido, String nombre) {
        this.dni = dni;
        this.cuil = cuil;
        this.apellido = apellido;
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método abstracto que obliga a las clases hijas a definir
     * cómo se muestran sus datos principales.
     */
    public abstract String mostrarDatos();
}