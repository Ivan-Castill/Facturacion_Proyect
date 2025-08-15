import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Esto asegura que Swing se ejecute en el hilo de eventos
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }
}
