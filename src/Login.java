import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
    private JTextField usernameTextField;
    private JPasswordField passwordPasswordField;
    private JButton logInButton;
    private JPanel panel1;

    public Login() {
        setTitle("Login");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        panel1 = new JPanel() {
            private Image backgroundImage = new ImageIcon("src/img/fondo.jpg").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel1.setLayout(new GridBagLayout());
        panel1.setOpaque(false);

        usernameTextField = new JTextField(20);
        usernameTextField.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameTextField.setBorder(BorderFactory.createTitledBorder("Usuario"));
        usernameTextField.setBackground(new Color(255, 255, 255, 200));

        passwordPasswordField = new JPasswordField(20);
        passwordPasswordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordPasswordField.setBorder(BorderFactory.createTitledBorder("Contraseña"));
        passwordPasswordField.setBackground(new Color(255, 255, 255, 200));

        JLabel userIcon = new JLabel(resizeIcon("src/img/user_icon.png", 48, 48));
        JLabel passIcon = new JLabel(resizeIcon("src/img/pass_icon.png", 48, 48));

        logInButton = new JButton("Ingresar");
        logInButton.setFont(new Font("Arial", Font.BOLD, 14));
        logInButton.setBackground(new Color(70, 130, 180));
        logInButton.setForeground(Color.WHITE);
        logInButton.setFocusPainted(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridx = 0; gbc.gridy = 0;
        panel1.add(userIcon, gbc);

        gbc.gridx = 1;
        panel1.add(usernameTextField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel1.add(passIcon, gbc);

        gbc.gridx = 1;
        panel1.add(passwordPasswordField, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 2;
        panel1.add(logInButton, gbc);

        add(panel1);

        logInButton.addActionListener((ActionEvent e) -> login());
    }

    private void login() {
        String userApp = usernameTextField.getText().trim();
        String passApp = new String(passwordPasswordField.getPassword()).trim();

        if(userApp.isEmpty() || passApp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese usuario y contraseña");
            return;
        }

        try (Connection connValidador = ConexionDB.conectar("validadorlocal", "validador2025")) {
            if (connValidador == null) {
                JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos");
                return;
            }

            String query = "SELECT rol FROM usuarios WHERE usuario = ? AND pass = ?";
            PreparedStatement stmt = connValidador.prepareStatement(query);
            stmt.setString(1, userApp);
            stmt.setString(2, passApp);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                String rol = rs.getString("rol");
                JOptionPane.showMessageDialog(this, "Bienvenido " + userApp);

                Connection connRol = null;
                switch(rol) {
                    case "Administrador":
                        connRol = ConexionDB.conectar("adminlocal", "admin2025");
                        Administrador admin = new Administrador(connRol);
                        admin.setVisible(true);
                        break;
                    case "Cajero":
                        connRol = ConexionDB.conectar("cajerolocal", "cajero2025");
                        Caja_Registradora cajero = new Caja_Registradora(connRol);
                        cajero.setVisible(true);
                        break;
                    case "Servidor bodega":
                        connRol = ConexionDB.conectar("servidor_bodegalocal", "bodega2025");
                        Servidor_bodega bodega = new Servidor_bodega(connRol);
                        bodega.setVisible(true);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Rol desconocido");
                        return;
                }

                dispose(); // cerrar login actual
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    private ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }
}
