import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Servidor_bodega extends JFrame {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton logoutButton;
    private JTabbedPane tabbedPane2;
    private JTable table1;
    private JButton refrescarTablaButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton registrarProductoButton;
    private JTextField textField4;
    private JButton actualizarProductoButton;
    private JTextField textField5;
    private JTextField textField6;
    private JButton eliminarProductoButton;

    public Servidor_bodega(Connection connRol) {
        setTitle("Panel Servidor Bodega");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);

        // ===== ESTÉTICA =====
        Color botonColor = new Color(30, 80, 140); // azul oscuro
        JButton[] botones = {registrarProductoButton, actualizarProductoButton, eliminarProductoButton, refrescarTablaButton, logoutButton};
        for (JButton btn : botones) {
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setBackground(botonColor);
            btn.setForeground(Color.WHITE);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        JTextField[] campos = {textField1, textField2, textField3, textField4, textField5, textField6};
        for (JTextField campo : campos) {
            campo.setFont(new Font("Arial", Font.PLAIN, 16));
            campo.setBackground(new Color(255, 255, 255, 220)); // semitransparente
        }

        // ===== REGISTRAR PRODUCTO =====
        registrarProductoButton.addActionListener(e -> {
            String producto = textField1.getText().trim();
            String precioStr = textField2.getText().trim();
            String stockStr = textField3.getText().trim();

            if (producto.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                return;
            }

            try {
                double precio = Double.parseDouble(precioStr.replace(",", "."));
                int stock = Integer.parseInt(stockStr);

                if (precio <= 0 || stock < 0) {
                    JOptionPane.showMessageDialog(null, "Precio debe ser positivo y stock >= 0");
                    return;
                }

                try (Connection conn = ConexionDB.conectar("servidor_bodegalocal", "bodega2025")) {
                    String sql = "INSERT INTO productos (producto, precio, stock) VALUES (?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, producto);
                    stmt.setDouble(2, precio);
                    stmt.setInt(3, stock);

                    int rows = stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, rows > 0 ? "Producto registrado" : "No se pudo registrar");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Precio debe ser número y stock número entero");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error BD: " + ex.getMessage());
            }
        });

        // ===== ACTUALIZAR PRODUCTO =====
        actualizarProductoButton.addActionListener(e -> {
            String idProducto = textField4.getText().trim();
            String stockStr = textField5.getText().trim();

            if (idProducto.isEmpty() || stockStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "ID y Stock son obligatorios");
                return;
            }

            try {
                int id = Integer.parseInt(idProducto);
                int stock = Integer.parseInt(stockStr);
                if (stock < 0) {
                    JOptionPane.showMessageDialog(null, "Stock debe ser >= 0");
                    return;
                }

                try (Connection conn = ConexionDB.conectar("servidor_bodegalocal", "bodega2025")) {
                    String sql = "UPDATE productos SET stock = ? WHERE id_producto = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, stock);
                    stmt.setInt(2, id);

                    int rows = stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, rows > 0 ? "Producto actualizado" : "No se pudo actualizar");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID y Stock deben ser números enteros");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error BD: " + ex.getMessage());
            }
        });

        // ===== ELIMINAR PRODUCTO =====
        eliminarProductoButton.addActionListener(e -> {
            String idProducto = textField6.getText().trim();
            if (idProducto.isEmpty()) {
                JOptionPane.showMessageDialog(null, "ID es obligatorio");
                return;
            }

            try {
                int id = Integer.parseInt(idProducto);
                try (Connection conn = ConexionDB.conectar("servidor_bodegalocal", "bodega2025")) {
                    String sql = "DELETE FROM productos WHERE id_producto = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, id);

                    int rows = stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, rows > 0 ? "Producto eliminado" : "No se pudo eliminar");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID debe ser número entero");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error BD: " + ex.getMessage());
            }
        });

        // ===== REFRESCAR TABLA =====
        refrescarTablaButton.addActionListener(e -> {
            try (Connection conn = ConexionDB.conectar("servidor_bodegalocal", "bodega2025")) {
                String sql = "SELECT id_producto, producto, precio, stock FROM productos";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("ID");
                model.addColumn("Producto");
                model.addColumn("Precio");
                model.addColumn("Stock");

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id_producto"),
                            rs.getString("producto"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    });
                }
                table1.setModel(model);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al cargar datos: " + ex.getMessage());
            }
        });

        // ===== LOGOUT =====
        logoutButton.addActionListener(e -> {
            dispose();
            new Login().setVisible(true); // Volver al login
        });
    }
}
