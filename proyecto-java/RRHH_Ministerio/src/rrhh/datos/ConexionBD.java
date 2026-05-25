package rrhh.datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase responsable de administrar la conexión con la base de datos MariaDB.
 *
 * Esta clase centraliza la configuración de conexión para que el resto del
 * sistema no repita la URL, el usuario ni la clave de acceso.
 *
 * Base utilizada:
 * - Motor: MariaDB / MySQL
 * - Base de datos: rrhh_ministerio
 * - Host: localhost
 * - Puerto: 3306
 *
 * La estructura de tablas utilizada corresponde al archivo SQL ya definido
 * para el proyecto:
 *
 * Z:\Desarrollo\database\rrhh_ministerio.sql
 */
public class ConexionBD {

    private static final String URL = "jdbc:mariadb://localhost:3306/rrhh_ministerio";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    /**
     * Constructor privado.
     *
     * La clase solo expone métodos estáticos y no necesita ser instanciada.
     */
    private ConexionBD() {
    }

    /**
     * Obtiene una conexión activa contra MariaDB.
     *
     * @return conexión JDBC hacia la base rrhh_ministerio.
     * @throws SQLException si ocurre un error de conexión.
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    /**
     * Verifica si la conexión con la base de datos se puede establecer.
     *
     * Este método se usa al iniciar el sistema para evitar abrir la interfaz
     * si MariaDB no está disponible.
     *
     * @return true si la conexión es correcta; false si ocurre un error.
     */
    public static boolean probarConexion() {
        try (Connection conexion = obtenerConexion()) {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al conectar con MariaDB: " + e.getMessage());
            return false;
        }
    }
}