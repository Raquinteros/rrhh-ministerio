package rrhh;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import rrhh.datos.ConexionBD;
import rrhh.vista.LoginView;

/**
 * Clase principal del sistema.
 *
 * Responsabilidades:
 * - Verificar la conexión con MariaDB.
 * - Iniciar la interfaz gráfica.
 * - Abrir la ventana de inicio de sesión.
 */
public class Main {

    /**
     * Punto de entrada del programa.
     *
     * @param args argumentos recibidos por consola.
     */
    public static void main(String[] args) {

        /*
         * Antes de abrir el sistema se prueba la conexión con MariaDB.
         * Si la base no está disponible, se informa al usuario y no se continúa.
         */
        if (!ConexionBD.probarConexion()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo conectar con la base de datos MariaDB.\n"
                    + "Verifique que XAMPP/MySQL esté iniciado y que exista la base rrhh_ministerio.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        /*
         * La interfaz gráfica de Swing debe iniciarse en el hilo de eventos.
         */
        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setVisible(true);
        });
    }
}