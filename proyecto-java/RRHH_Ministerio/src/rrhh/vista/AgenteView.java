package rrhh.vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
 * Ventana de gestión de agentes.
 *
 * Permite registrar agentes y consultar los agentes persistidos en MariaDB.
 */
public class AgenteView extends JFrame {

    private MenuPrincipalView menuPrincipal;

    private JTextField txtDni;
    private JTextField txtCuil;
    private JTextField txtApellido;
    private JTextField txtNombre;
    private JTextField txtCargo;
    private JTextField txtReparticion;
    private JCheckBox chkActivo;

    private JTable tablaAgentes;
    private DefaultTableModel modeloTabla;

    /**
     * Constructor de la pantalla de gestión de agentes.
     *
     * @param menuPrincipal referencia al menú principal para poder volver.
     */
    public AgenteView(MenuPrincipalView menuPrincipal) {
        this.menuPrincipal = menuPrincipal;

        configurarVentana();
        inicializarComponentes();
        cargarTablaAgentes();
    }

    /**
     * Configura la ventana.
     */
    private void configurarVentana() {
        setTitle("Gestión de Agentes");
        setSize(850, 520);
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

        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del agente"));

        txtDni = new JTextField();
        txtCuil = new JTextField();
        txtApellido = new JTextField();
        txtNombre = new JTextField();
        txtCargo = new JTextField();
        txtReparticion = new JTextField();
        chkActivo = new JCheckBox("Activo", true);

        panelFormulario.add(new JLabel("DNI:"));
        panelFormulario.add(txtDni);
        panelFormulario.add(new JLabel("CUIL:"));
        panelFormulario.add(txtCuil);
        panelFormulario.add(new JLabel("Apellido:"));
        panelFormulario.add(txtApellido);
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Cargo:"));
        panelFormulario.add(txtCargo);
        panelFormulario.add(new JLabel("Repartición:"));
        panelFormulario.add(txtReparticion);
        panelFormulario.add(new JLabel("Estado:"));
        panelFormulario.add(chkActivo);

        JPanel panelBotones = new JPanel();

        JButton btnGuardar = new JButton("Guardar agente");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnOrdenar = new JButton("Listar ordenado");
        JButton btnVolver = new JButton("Volver");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnOrdenar);
        panelBotones.add(btnVolver);

        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("DNI");
        modeloTabla.addColumn("CUIL");
        modeloTabla.addColumn("Apellido");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Cargo");
        modeloTabla.addColumn("Repartición");
        modeloTabla.addColumn("Activo");

        tablaAgentes = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaAgentes);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Agentes registrados"));

        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        btnGuardar.addActionListener(e -> guardarAgente());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnOrdenar.addActionListener(e -> cargarTablaAgentesOrdenados());
        btnVolver.addActionListener(e -> volverAlMenu());
    }

    /**
     * Valida los datos del formulario y registra el agente en MariaDB.
     */
    private void guardarAgente() {
        String dni = txtDni.getText().trim();
        String cuil = txtCuil.getText().trim();
        String apellido = txtApellido.getText().trim();
        String nombre = txtNombre.getText().trim();
        String cargo = txtCargo.getText().trim();
        String reparticion = txtReparticion.getText().trim();
        boolean activo = chkActivo.isSelected();

        if (dni.isEmpty() || cuil.isEmpty() || apellido.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe completar DNI, CUIL, apellido y nombre.",
                    "Datos incompletos",
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

        if (RepositorioMemoria.existeDni(dni)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ya existe un agente registrado con ese DNI.",
                    "Registro duplicado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Agente agente = new Agente(dni, cuil, apellido, nombre, cargo, reparticion, activo);

        boolean guardado = RepositorioMemoria.agregarAgente(agente);

        if (!guardado) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el agente. Verifique que el CUIL no esté duplicado.",
                    "Error al guardar",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        cargarTablaAgentes();

        JOptionPane.showMessageDialog(
                this,
                "Agente registrado correctamente.",
                "Operación exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        limpiarFormulario();
    }

    /**
     * Carga la tabla con los agentes registrados en MariaDB.
     */
    private void cargarTablaAgentes() {
        modeloTabla.setRowCount(0);

        for (Agente agente : RepositorioMemoria.obtenerAgentes()) {
            modeloTabla.addRow(new Object[]{
                agente.getDni(),
                agente.getCuil(),
                agente.getApellido(),
                agente.getNombre(),
                agente.getCargo(),
                agente.getReparticion(),
                agente.isActivo() ? "Sí" : "No"
            });
        }
    }

    /**
     * Carga la tabla con agentes ordenados por apellido y nombre.
     */
    private void cargarTablaAgentesOrdenados() {
        modeloTabla.setRowCount(0);

        for (Agente agente : RepositorioMemoria.obtenerAgentesOrdenadosPorApellido()) {
            modeloTabla.addRow(new Object[]{
                agente.getDni(),
                agente.getCuil(),
                agente.getApellido(),
                agente.getNombre(),
                agente.getCargo(),
                agente.getReparticion(),
                agente.isActivo() ? "Sí" : "No"
            });
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarFormulario() {
        txtDni.setText("");
        txtCuil.setText("");
        txtApellido.setText("");
        txtNombre.setText("");
        txtCargo.setText("");
        txtReparticion.setText("");
        chkActivo.setSelected(true);
    }

    /**
     * Vuelve al menú principal.
     */
    private void volverAlMenu() {
        menuPrincipal.setVisible(true);
        dispose();
    }
}