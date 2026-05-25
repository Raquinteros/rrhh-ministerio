package rrhh.modelo;

/**
 * Clase que representa a un agente o empleado gestionado por el sistema.
 * 
 * Hereda de Persona los datos comunes: DNI, CUIL, apellido y nombre.
 * Mantiene como atributos propios los datos laborales del agente.
 */
public class Agente extends Persona {

    private String cargo;
    private String reparticion;
    private boolean activo;

    public Agente(String dni, String cuil, String apellido, String nombre, String cargo, String reparticion, boolean activo) {
        super(dni, cuil, apellido, nombre);
        this.cargo = cargo;
        this.reparticion = reparticion;
        this.activo = activo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getReparticion() {
        return reparticion;
    }

    public void setReparticion(String reparticion) {
        this.reparticion = reparticion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String mostrarDatos() {
        return getApellido() + ", " + getNombre()
                + " | DNI: " + getDni()
                + " | CUIL: " + getCuil()
                + " | Cargo: " + cargo
                + " | Repartición: " + reparticion
                + " | Estado: " + (activo ? "Activo" : "Inactivo");
    }

    @Override
    public String toString() {
        return mostrarDatos();
    }
}