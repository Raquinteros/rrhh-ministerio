package rrhh.vista;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuPrincipalView extends JFrame {

    private String usuarioActual;

    public MenuPrincipalView(String usuarioActual) {
        this.usuarioActual = usuarioActual;
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("Sistema de Gestión Integral para RRHH - Menú principal");
        setSize(620, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel lblTitulo = new JLabel("Menú principal");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel lblUsuario = new JLabel("Usuario activo: " + usuarioActual);

        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));
        panelSuperior.add(lblTitulo);
        panelSuperior.add(lblUsuario);

        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 15, 15));

        JButton btnAgentes = new JButton("Gestión de Agentes");
        JButton btnLegajos = new JButton("Gestión de Legajos");
        JButton btnLicencias = new JButton("Gestión de Licencias");
        JButton btnInformes = new JButton("Informes");
        JButton btnAcerca = new JButton("Acerca del sistema");
        JButton btnSalir = new JButton("Salir");

        panelBotones.add(btnAgentes);
        panelBotones.add(btnLegajos);
        panelBotones.add(btnLicencias);
        panelBotones.add(btnInformes);
        panelBotones.add(btnAcerca);
        panelBotones.add(btnSalir);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        add(panelPrincipal);

        btnAgentes.addActionListener(e -> {
            AgenteView agenteView = new AgenteView(this);
            agenteView.setVisible(true);
            setVisible(false);
        });

        btnLegajos.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Módulo de legajos previsto para consulta del prototipo.",
                    "Gestión de Legajos",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        btnLicencias.addActionListener(e -> {
            LicenciaView licenciaView = new LicenciaView(this);
            licenciaView.setVisible(true);
            setVisible(false);
        });

        btnInformes.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Módulo previsto para futuras iteraciones.",
                    "Informes",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        btnAcerca.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Sistema de Gestión Integral para RRHH\n"
                    + "Prototipo AP2\n"
                    + "Módulos incluidos: Agentes, Legajos y Licencias.",
                    "Acerca del sistema",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        btnSalir.addActionListener(e -> System.exit(0));
    }
}