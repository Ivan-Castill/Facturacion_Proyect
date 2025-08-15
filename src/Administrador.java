import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Administrador extends JFrame {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton logoutButton;
    private JTabbedPane tabbedPane2;
    private JTabbedPane tabbedPane3;
    private JTabbedPane tabbedPane4;
    private JTabbedPane tabbedPane5;
    private JTextField textUserR;
    private JTextField textPassR;
    private JComboBox comboBox1;
    private JButton registrarUsuarioButton;
    private JTextField textField3;
    private JTextField textField4;
    private JButton actualizarPasswordButton;
    private JTextField textField5;
    private JComboBox comboBox2;
    private JButton actualizarRolButton;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JTextField textField9;
    private JButton registrarPersonalButton;
    private JTextField textField10;
    private JTextField textField11;
    private JTextField textField12;
    private JTextField textField13;
    private JTextField textField14;
    private JTextField textField15;
    private JButton actualizarNombreButton;
    private JButton actualizarTelefonoButton;
    private JButton actualizarCorreoButton;
    private JTextField textField1;
    private JButton eliminarUsuarioButton;
    private JTextField textField2;
    private JButton eliminarPersonalButton;
    private JTable table1;
    private JButton refrescarTablaButton;
    private JTable table2;
    private JTabbedPane tabbedPane6;
    private JTable table3;
    private JTextField textField16;
    private JButton eliminarClienteButton;

    public Administrador(Connection connRol) {
        setTitle("Panel Administrador");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        aplicarEstilo();
        setVisible(true);

        registrarUsuarioButton.addActionListener(e -> registrarUsuario());
        actualizarPasswordButton.addActionListener(e -> actualizarPassword());
        actualizarRolButton.addActionListener(e -> actualizarRol());
        eliminarUsuarioButton.addActionListener(e -> eliminarUsuario());
        registrarPersonalButton.addActionListener(e -> registrarPersonal());
        actualizarNombreButton.addActionListener(e -> actualizarNombre());
        actualizarTelefonoButton.addActionListener(e -> actualizarTelefono());
        actualizarCorreoButton.addActionListener(e -> actualizarCorreo());
        eliminarPersonalButton.addActionListener(e -> eliminarPersonal());
        eliminarClienteButton.addActionListener(e -> eliminarCliente());
        refrescarTablaButton.addActionListener(e -> refrescarTablas());
        logoutButton.addActionListener(e -> cerrarSesion());
    }

    // ---------------------
    // VALIDACIONES
    // ---------------------
    private boolean validarCampo(JTextField campo, String nombreCampo) {
        if (campo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo " + nombreCampo + " no puede estar vacío");
            campo.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validarPassword(JTextField campo) {
        String pass = campo.getText().trim();
        if (pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La contraseña no puede estar vacía");
            campo.requestFocus();
            return false;
        }
        if (pass.length() < 6) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres");
            campo.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validarCorreo(String correo) {
        return correo.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean validarTelefono(String telefono) {
        return telefono.matches("\\d{7,10}");
    }

    // ---------------------
    // ESTILO
    // ---------------------
    private void aplicarEstilo() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        JButton[] botonesVerdes = {
                registrarUsuarioButton, registrarPersonalButton,
                actualizarPasswordButton, actualizarRolButton,
                actualizarNombreButton, actualizarTelefonoButton,
                actualizarCorreoButton, refrescarTablaButton
        };
        for (JButton btn : botonesVerdes) {
            btn.setBackground(new Color(0, 153, 76));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        }

        JButton[] botonesRojos = {
                eliminarUsuarioButton, eliminarPersonalButton, eliminarClienteButton
        };
        for (JButton btn : botonesRojos) {
            btn.setBackground(new Color(204, 0, 0));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        }

        JTable[] tablas = { table1, table2, table3 };
        for (JTable tabla : tablas) {
            tabla.setRowHeight(25);
            tabla.setAutoCreateRowSorter(true);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            tabla.setFillsViewportHeight(true);
            for (int i = 0; i < tabla.getColumnCount(); i++) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    }

    // ---------------------
    // ACCIONES BOTONES
    // ---------------------
    private void registrarUsuario() {
        if (!validarCampo(textUserR, "Usuario") || !validarPassword(textPassR)) return;

        String usuario = textUserR.getText();
        String pass = textPassR.getText();
        String rol = comboBox1.getSelectedItem().toString();

        try (Connection conn = ConexionDB.conectar("adminlocal", "admin2025")) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "No se pudo conectar con la base de datos");
                return;
            }

            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM usuarios WHERE usuario = ?");
            checkStmt.setString(1, usuario);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "El usuario ya existe");
                return;
            }

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO usuarios (usuario, pass, rol) VALUES (?, ?, ?)");
            stmt.setString(1, usuario);
            stmt.setString(2, pass);
            stmt.setString(3, rol);
            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualizarPassword() {
        if (!validarCampo(textField3, "ID Usuario") || !validarPassword(textField4)) return;

        try (Connection conn = ConexionDB.conectar("adminlocal", "admin2025")) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE usuarios SET pass = ? WHERE id_user = ?");
            stmt.setString(1, textField4.getText());
            stmt.setString(2, textField3.getText());
            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Contraseña actualizada");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualizarRol() {
        if (!validarCampo(textField5, "ID Usuario")) return;

        try (Connection conn = ConexionDB.conectar("adminlocal", "admin2025")) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE usuarios SET rol = ? WHERE id_user = ?");
            stmt.setString(1, comboBox2.getSelectedItem().toString());
            stmt.setString(2, textField5.getText());
            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Rol actualizado");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminarUsuario() {
        if (!validarCampo(textField1, "ID Usuario")) return;

        try (Connection conn = ConexionDB.conectar("adminlocal", "admin2025")) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM usuarios WHERE id_user = ?");
            stmt.setString(1, textField1.getText());
            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void registrarPersonal() {
        if (!validarCampo(textField6, "Nombres") ||
                !validarCampo(textField8, "Teléfono") ||
                !validarCampo(textField9, "Correo") ||
                !validarCampo(textField7, "ID Usuario")) return;

        if (!validarTelefono(textField8.getText())) {
            JOptionPane.showMessageDialog(this, "Teléfono inválido");
            return;
        }
        if (!validarCorreo(textField9.getText())) {
            JOptionPane.showMessageDialog(this, "Correo inválido");
            return;
        }

        try (Connection conn = ConexionDB.conectar("adminlocal", "admin2025")) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO personal (nombres, telefono, correo, id_user) VALUES (?, ?, ?, ?)");
            stmt.setString(1, textField6.getText());
            stmt.setString(2, textField8.getText());
            stmt.setString(3, textField9.getText());
            stmt.setString(4, textField7.getText());
            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Personal registrado");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualizarNombre() {
        if (!validarCampo(textField10, "ID Personal") || !validarCampo(textField11, "Nuevo Nombre")) return;

        try (Connection conn = ConexionDB.conectar("adminlocal", "admin2025")) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE personal SET nombres = ? WHERE id_personal = ?");
            stmt.setString(1, textField11.getText());
            stmt.setString(2, textField10.getText());
            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Nombre actualizado");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualizarTelefono() {
        if (!validarCampo(textField12, "ID Personal") || !validarCampo(textField13, "Nuevo Teléfono")) return;
        if (!validarTelefono(textField13.getText())) {
            JOptionPane.showMessageDialog(this, "Teléfono inválido");
            return;
        }

        try (Connection conn = ConexionDB.conectar("adminlocal", "admin2025")) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE personal SET telefono = ? WHERE id_personal = ?");
            stmt.setString(1, textField13.getText());
            stmt.setString(2, textField12.getText());
            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Teléfono actualizado");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualizarCorreo() {
        if (!validarCampo(textField14, "ID Personal") || !validarCampo(textField15, "Nuevo Correo")) return;
        if (!validarCorreo(textField15.getText())) {
            JOptionPane.showMessageDialog(this, "Correo inválido");
            return;
        }

        try (Connection conn = ConexionDB.conectar("adminlocal", "admin2025")) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE personal SET correo = ? WHERE id_personal = ?");
            stmt.setString(1, textField15.getText());
            stmt.setString(2, textField14.getText());
            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Correo actualizado");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminarPersonal() {
        if (!validarCampo(textField2, "ID Personal")) return;

        try (Connection conn = ConexionDB.conectar("adminlocal", "admin2025")) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM personal WHERE id_personal = ?");
            stmt.setString(1, textField2.getText());
            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Personal eliminado");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminarCliente() {
        if (!validarCampo(textField16, "ID Cliente")) return;

        try (Connection conn = ConexionDB.conectar("adminlocal", "admin2025")) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM cliente WHERE id_cliente = ?");
            stmt.setString(1, textField16.getText());
            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void refrescarTablas() {
        try (Connection conn = ConexionDB.conectar("adminlocal", "admin2025")) {
            // Tabla usuarios
            DefaultTableModel modelUsuarios = new DefaultTableModel(new String[]{"ID", "Usuario", "Rol"}, 0);
            ResultSet rs = conn.prepareStatement("SELECT id_user, usuario, rol FROM usuarios").executeQuery();
            while (rs.next()) {
                modelUsuarios.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3)});
            }
            table2.setModel(modelUsuarios);

            // Tabla personal
            DefaultTableModel modelPersonal = new DefaultTableModel(new String[]{"ID", "Nombres", "Teléfono", "Correo", "ID Usuario"}, 0);
            ResultSet rs2 = conn.prepareStatement("SELECT id_personal, nombres, telefono, correo, id_user FROM personal").executeQuery();
            while (rs2.next()) {
                modelPersonal.addRow(new Object[]{rs2.getInt(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getInt(5)});
            }
            table1.setModel(modelPersonal);

            // Tabla clientes
            DefaultTableModel modelClientes = new DefaultTableModel(new String[]{"ID", "Nombres", "Cédula", "Dirección", "Teléfono", "Correo"}, 0);
            ResultSet rs3 = conn.prepareStatement("SELECT id_cliente, nombres, cedula, direccion, telefono, correo FROM cliente").executeQuery();
            while (rs3.next()) {
                modelClientes.addRow(new Object[]{rs3.getInt(1), rs3.getString(2), rs3.getString(3), rs3.getString(4), rs3.getString(5), rs3.getString(6)});
            }
            table3.setModel(modelClientes);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al refrescar tablas: " + ex.getMessage());
        }
    }

    private void cerrarSesion() {
        dispose(); // cierra Administrador
        SwingUtilities.invokeLater(() -> new Login().setVisible(true)); // vuelve a abrir login
    }
}
