package rrhh.datos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import rrhh.modelo.Agente;

/**
 * Repositorio de datos del sistema.
 *
 * El nombre RepositorioMemoria se conserva para respetar la estructura ya usada
 * en el prototipo y evitar modificar innecesariamente las pantallas.
 *
 * A partir de esta versión, la clase ya no guarda datos en memoria mediante
 * ArrayList, sino que trabaja contra MariaDB usando JDBC.
 *
 * Tablas usadas:
 * - usuarios
 * - agentes
 * - tipos_licencia
 * - licencias
 */
public class RepositorioMemoria {

    /**
     * Constructor privado.
     *
     * La clase se utiliza de manera estática desde las ventanas del sistema.
     */
    private RepositorioMemoria() {
    }

    /**
     * Valida usuario y contraseña contra la tabla usuarios.
     *
     * Respeta los usuarios cargados en el archivo SQL:
     * - admin / admin123
     * - rrhh / rrhh123
     *
     * @param usuario usuario ingresado.
     * @param clave clave ingresada.
     * @return true si el usuario existe, está activo y la clave coincide.
     */
    public static boolean validarUsuario(String usuario, String clave) {
        String sql = """
                SELECT COUNT(*) AS total
                FROM usuarios
                WHERE usuario = ?
                  AND clave = ?
                  AND activo = TRUE
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, clave);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
        }

        return false;
    }

    /**
     * Agrega un agente en la tabla agentes.
     *
     * Se respetan las columnas reales de la base aprobada:
     * dni, cuil, apellido, nombre, cargo, reparticion y activo.
     *
     * @param agente agente a registrar.
     * @return true si se guardó correctamente; false si ocurrió un error.
     */
    public static boolean agregarAgente(Agente agente) {
        String sql = """
                INSERT INTO agentes (
                    dni,
                    cuil,
                    apellido,
                    nombre,
                    cargo,
                    reparticion,
                    activo
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, agente.getDni());
            ps.setString(2, agente.getCuil());
            ps.setString(3, agente.getApellido());
            ps.setString(4, agente.getNombre());
            ps.setString(5, agente.getCargo());
            ps.setString(6, agente.getReparticion());
            ps.setBoolean(7, agente.isActivo());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al agregar agente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los agentes registrados en MariaDB.
     *
     * @return lista de agentes.
     */
    public static List<Agente> obtenerAgentes() {
        List<Agente> agentes = new ArrayList<>();

        String sql = """
                SELECT
                    id_agente,
                    dni,
                    cuil,
                    apellido,
                    nombre,
                    cargo,
                    reparticion,
                    activo
                FROM agentes
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                agentes.add(mapearAgente(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener agentes: " + e.getMessage());
        }

        return agentes;
    }

    /**
     * Obtiene los agentes ordenados por apellido y nombre.
     *
     * Este método permite demostrar el uso de ordenamiento dentro del prototipo.
     *
     * @return lista de agentes ordenada alfabéticamente.
     */
    public static List<Agente> obtenerAgentesOrdenadosPorApellido() {
        List<Agente> agentes = new ArrayList<>();

        String sql = """
                SELECT
                    id_agente,
                    dni,
                    cuil,
                    apellido,
                    nombre,
                    cargo,
                    reparticion,
                    activo
                FROM agentes
                ORDER BY apellido ASC, nombre ASC
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                agentes.add(mapearAgente(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener agentes ordenados: " + e.getMessage());
        }

        return agentes;
    }

    /**
     * Busca un agente por DNI.
     *
     * @param dni DNI del agente.
     * @return agente encontrado o null si no existe.
     */
    public static Agente buscarAgentePorDni(String dni) {
        String sql = """
                SELECT
                    id_agente,
                    dni,
                    cuil,
                    apellido,
                    nombre,
                    cargo,
                    reparticion,
                    activo
                FROM agentes
                WHERE dni = ?
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearAgente(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar agente por DNI: " + e.getMessage());
        }

        return null;
    }

    /**
     * Verifica si ya existe un DNI registrado.
     *
     * @param dni DNI a verificar.
     * @return true si ya existe; false si no existe.
     */
    public static boolean existeDni(String dni) {
        String sql = """
                SELECT COUNT(*) AS total
                FROM agentes
                WHERE dni = ?
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar DNI: " + e.getMessage());
        }

        return false;
    }

    /**
     * Obtiene el id_agente a partir del DNI.
     *
     * Es necesario porque la tabla licencias no guarda el DNI directamente,
     * sino el campo id_agente como clave foránea.
     *
     * @param dni DNI del agente.
     * @return id_agente o -1 si no existe.
     */
    private static int obtenerIdAgentePorDni(String dni) {
        String sql = """
                SELECT id_agente
                FROM agentes
                WHERE dni = ?
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_agente");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener id_agente: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Obtiene el id_tipo_licencia a partir del nombre del tipo de licencia.
     *
     * Es necesario porque la tabla licencias guarda id_tipo_licencia y no el
     * texto del tipo de licencia.
     *
     * @param nombreTipo nombre del tipo de licencia.
     * @return id_tipo_licencia o -1 si no existe.
     */
    private static int obtenerIdTipoLicenciaPorNombre(String nombreTipo) {
        String sql = """
                SELECT id_tipo_licencia
                FROM tipos_licencia
                WHERE nombre = ?
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombreTipo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_tipo_licencia");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener tipo de licencia: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Obtiene los tipos de licencia registrados en la tabla tipos_licencia.
     *
     * Esto permite que el combo de la pantalla de licencias use los datos reales
     * de la base, en vez de depender solamente de valores escritos en el código.
     *
     * @return lista de nombres de tipos de licencia.
     */
    public static List<String> obtenerTiposLicencia() {
        List<String> tipos = new ArrayList<>();

        String sql = """
                SELECT nombre
                FROM tipos_licencia
                ORDER BY id_tipo_licencia ASC
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tipos.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener tipos de licencia: " + e.getMessage());
        }

        return tipos;
    }

    /**
     * Registra una licencia asociada a un agente existente.
     *
     * La base aprobada exige guardar:
     * - id_agente
     * - id_tipo_licencia
     * - fecha_inicio
     * - fecha_fin
     * - dias_solicitados
     * - estado
     * - observaciones
     *
     * @param dni DNI del agente.
     * @param tipoLicencia nombre del tipo de licencia.
     * @param fechaInicio fecha inicial.
     * @param fechaFin fecha final.
     * @param diasSolicitados cantidad de días solicitados.
     * @param observaciones observaciones ingresadas por el usuario.
     * @return true si se registró correctamente; false si ocurrió un error.
     */
    public static boolean registrarLicencia(
            String dni,
            String tipoLicencia,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            int diasSolicitados,
            String observaciones
    ) {
        int idAgente = obtenerIdAgentePorDni(dni);
        int idTipoLicencia = obtenerIdTipoLicenciaPorNombre(tipoLicencia);

        if (idAgente == -1 || idTipoLicencia == -1) {
            return false;
        }

        String sql = """
                INSERT INTO licencias (
                    id_agente,
                    id_tipo_licencia,
                    fecha_inicio,
                    fecha_fin,
                    dias_solicitados,
                    estado,
                    observaciones
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idAgente);
            ps.setInt(2, idTipoLicencia);
            ps.setDate(3, Date.valueOf(fechaInicio));
            ps.setDate(4, Date.valueOf(fechaFin));
            ps.setInt(5, diasSolicitados);
            ps.setString(6, "Registrada");
            ps.setString(7, observaciones);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar licencia: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene las licencias registradas usando JOIN contra agentes y tipos_licencia.
     *
     * Se devuelve una lista de filas preparada para cargar la JTable de LicenciaView.
     *
     * @return lista de filas con datos de licencias.
     */
    public static List<Object[]> obtenerLicenciasParaTabla() {
        List<Object[]> licencias = new ArrayList<>();

        String sql = """
                SELECT
                    l.id_licencia,
                    a.dni,
                    a.apellido,
                    a.nombre,
                    tl.nombre AS tipo_licencia,
                    l.fecha_inicio,
                    l.fecha_fin,
                    l.dias_solicitados,
                    l.estado,
                    l.observaciones
                FROM licencias l
                INNER JOIN agentes a
                    ON l.id_agente = a.id_agente
                INNER JOIN tipos_licencia tl
                    ON l.id_tipo_licencia = tl.id_tipo_licencia
                ORDER BY l.id_licencia DESC
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                licencias.add(new Object[]{
                    rs.getString("dni"),
                    rs.getString("apellido"),
                    rs.getString("nombre"),
                    rs.getString("tipo_licencia"),
                    rs.getDate("fecha_inicio").toString(),
                    rs.getDate("fecha_fin").toString(),
                    rs.getInt("dias_solicitados"),
                    rs.getString("estado"),
                    rs.getString("observaciones")
                });
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener licencias: " + e.getMessage());
        }

        return licencias;
    }

    /**
     * Convierte una fila SQL de la tabla agentes en un objeto Agente.
     *
     * @param rs fila actual del ResultSet.
     * @return objeto Agente.
     * @throws SQLException si ocurre un error leyendo los campos.
     */
    private static Agente mapearAgente(ResultSet rs) throws SQLException {
        return new Agente(
                rs.getString("dni"),
                rs.getString("cuil"),
                rs.getString("apellido"),
                rs.getString("nombre"),
                rs.getString("cargo"),
                rs.getString("reparticion"),
                rs.getBoolean("activo")
        );
    }
}