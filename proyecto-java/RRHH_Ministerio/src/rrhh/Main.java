package rrhh;

import javax.swing.SwingUtilities;
import rrhh.vista.LoginView;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setVisible(true);
        });
    }
}