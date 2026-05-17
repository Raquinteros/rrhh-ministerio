package rrhh.modelo;

public class Agente {

    private String dni;
    private String cuil;
    private String apellido;
    private String nombre;
    private String cargo;
    private String reparticion;
    private boolean activo;

    public Agente(String dni, String cuil, String apellido, String nombre,
                  String cargo, String reparticion, boolean activo) {
        this.dni = dni;
        this.cuil = cuil;
        this.apellido = apellido;
        this.nombre = nombre;
        this.cargo = cargo;
        this.reparticion = reparticion;
        this.activo = activo;
    }

    public String getDni() {
        return dni;
    }

    public String getCuil() {
        return cuil;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public String getReparticion() {
        return reparticion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setReparticion(String reparticion) {
        this.reparticion = reparticion;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}