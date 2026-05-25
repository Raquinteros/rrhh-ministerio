package rrhh.vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import rrhh.datos.RepositorioMemoria;
import rrhh.modelo.Agente;

/**
 * Ventana de gestión de licencias.
 *
 * Permite buscar un agente por DNI y registrar licencias en MariaDB,
 * respetando la estructura aprobada:
 *
 * licencias.id_agente
 * licencias.id_tipo_licencia
 */
public class LicenciaView extends JFrame {

    private MenuPrincipalView menuPrincipal;

    private JTextField txtDniAgente;
    private JTextField txtApellidoNombre;
    private JComboBox<String> cmbTipoLicencia;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTextField txtObservaciones;

    private JTable tablaLicencias;
    private DefaultTableModel modeloTabla;

    private DateTimeFormatter formatter;

    /**
     * Constructor de la pantalla de licencias.
     *
     * @param menuPrincipal referencia al menú principal para poder volver.
     */
    public LicenciaView(MenuPrincipalView menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
        this.formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        configurarVentana();
        inicializarComponentes();
        cargarLicencias();
    }

    /**
     * Configura la ventana.
     */
    private void configurarVentana() {
        setTitle("Gestión de Licencias");
        setSize(980, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
    }

    /**
     * Inicializa formulario, botones y tabla.
     */
    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de la licencia"));

        txtDniAgente = new JTextField();
        txtApellidoNombre = new JTextField();
        txtApellidoNombre.setEditable(false);

        cmbTipoLicencia = new JComboBox<>();
        cargarTiposLicencia();

        txtFechaInicio = new JTextField();
        txtFechaFin = new JTextField();
        txtObservaciones = new JTextField();

        JButton btnBuscarAgente = new JButton("Buscar agente");

        JPanel panelBusqueda = new JPanel(new BorderLayout(5, 5));
        panelBusqueda.add(txtDniAgente, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscarAgente, BorderLayout.EAST);

        panelFormulario.add(new JLabel("DNI del agente:"));
        panelFormulario.add(panelBusqueda);

        panelFormulario.add(new JLabel("Agente encontrado:"));
        panelFormulario.add(txtApellidoNombre);

        panelFormulario.add(new JLabel("Tipo de licencia:"));
        panelFormulario.add(cmbTipoLicencia);

        panelFormulario.add(new JLabel("Fecha inicio (dd/MM/yyyy):"));
        panelFormulario.add(txtFechaInicio);

        panelFormulario.add(new JLabel("Fecha fin (dd/MM/yyyy):"));
        panelFormulario.add(txtFechaFin);

        panelFormulario.add(new JLabel("Observaciones:"));
        panelFormulario.add(txtObservaciones);

        JPanel panelBotones = new JPanel();

        JButton btnRegistrar = new JButton("Registrar licencia");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnVolver = new JButton("Volver");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnVolver);

        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("DNI");
        modeloTabla.addColumn("Apellido");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Inicio");
        modeloTabla.addColumn("Fin");
        modeloTabla.addColumn("Días");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Observaciones");

        tablaLicencias = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaLicencias);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Licencias registradas"));

        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        btnBuscarAgente.addActionListener(e -> buscarAgente());
        btnRegistrar.addActionListener(e -> registrarLicencia());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnVolver.addActionListener(e -> volverAlMenu());
    }

    /**
     * Carga los tipos de licencia desde la tabla tipos_licencia.
     */
    private void cargarTiposLicencia() {
        List<String> tipos = RepositorioMemoria.obtenerTiposLicencia();

        cmbTipoLicencia.removeAllItems();

        for (String tipo : tipos) {
            cmbTipoLicencia.addItem(tipo);
        }

        /*
         * Respaldo defensivo.
         * Si por algún motivo la tabla tipos_licencia no devolviera datos,
         * se cargan los tipos previstos en el SQL aprobado.
         */
        if (cmbTipoLicencia.getItemCount() == 0) {
            cmbTipoLicencia.addItem("Licencia Anual Reglamentaria");
            cmbTipoLicencia.addItem("Licencia por Enfermedad");
            cmbTipoLicencia.addItem("Licencia por Estudio");
            cmbTipoLicencia.addItem("Licencia por Razones Particulares");
        }
    }

    /**
     * Busca un agente por DNI y muestra apellido y nombre.
     */
    private void buscarAgente() {
        String dni = txtDniAgente.getText().trim();

        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el DNI del agente.",
                    "Dato obligatorio",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!dni.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(
                    this,
                    "El DNI debe contener 8 dígitos numéricos.",
                    "DNI inválido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Agente agente = RepositorioMemoria.buscarAgentePorDni(dni);

        if (agente == null) {
            txtApellidoNombre.setText("");
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el agente solicitado.",
                    "Agente inexistente",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        txtApellidoNombre.setText(agente.getApellido() + ", " + agente.getNombre());
    }

    /**
     * Registra una licencia en MariaDB.
     *
     * La pantalla trabaja con DNI y nombre de tipo de licencia,
     * pero el repositorio convierte esos datos a id_agente e id_tipo_licencia
     * para respetar las claves foráneas definidas en la base.
     */
    private void registrarLicencia() {
        String dni = txtDniAgente.getText().trim();
        String tipoLicencia = cmbTipoLicencia.getSelectedItem().toString();
        String fechaInicioTexto = txtFechaInicio.getText().trim();
        String fechaFinTexto = txtFechaFin.getText().trim();
        String observaciones = txtObservaciones.getText().trim();

        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el DNI del agente.",
                    "Dato obligatorio",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!dni.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(
                    this,
                    "El DNI debe contener 8 dígitos numéricos.",
                    "DNI inválido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Agente agente = RepositorioMemoria.buscarAgentePorDni(dni);

        if (agente == null) {
            txtApellidoNombre.setText("");
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el agente solicitado. No se puede registrar una licencia sin agente asociado.",
                    "Agente inexistente",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        txtApellidoNombre.setText(agente.getApellido() + ", " + agente.getNombre());

        if (fechaInicioTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar fecha de inicio.",
                    "Dato obligatorio",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (fechaFinTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar fecha de finalización.",
                    "Dato obligatorio",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        LocalDate fechaInicio;
        LocalDate fechaFin;

        try {
            fechaInicio = LocalDate.parse(fechaInicioTexto, formatter);
            fechaFin = LocalDate.parse(fechaFinTexto, formatter);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Las fechas deben tener el formato dd/MM/yyyy.",
                    "Formato de fecha inválido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (fechaFin.isBefore(fechaInicio)) {
            JOptionPane.showMessageDialog(
                    this,
                    "La fecha de finalización no puede ser anterior a la fecha de inicio.",
                    "Fechas inválidas",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        long dias = java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;

        boolean guardado = RepositorioMemoria.registrarLicencia(
                dni,
                tipoLicencia,
                fechaInicio,
                fechaFin,
                (int) dias,
                observaciones
        );

        if (!guardado) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar la licencia en la base de datos.",
                    "Error al guardar",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        cargarLicencias();

        JOptionPane.showMessageDialog(
                this,
                "Licencia registrada correctamente para el agente "
                + agente.getApellido() + ", " + agente.getNombre() + ".",
                "Operación exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        limpiarFormulario();
    }

    /**
     * Carga en la tabla las licencias registradas en MariaDB.
     */
    private void cargarLicencias() {
        modeloTabla.setRowCount(0);

        for (Object[] fila : RepositorioMemoria.obtenerLicenciasParaTabla()) {
            modeloTabla.addRow(fila);
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarFormulario() {
        txtDniAgente.setText("");
        txtApellidoNombre.setText("");
        cmbTipoLicencia.setSelectedIndex(0);
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        txtObservaciones.setText("");
    }

    /**
     * Vuelve al menú principal.
     */
    private void volverAlMenu() {
        menuPrincipal.setVisible(true);
        dispose();
    }
}