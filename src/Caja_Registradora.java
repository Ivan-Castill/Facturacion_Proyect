import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Caja_Registradora extends JFrame {
    private JPanel panel1;
    private JButton logoutButton;
    private JTabbedPane tabbedPane1;
    private JTextField textField1; // Nombre cliente
    private JTextField textField2; // Correo
    private JTextField textField3; // Dirección
    private JTextField textField5; // Teléfono
    private JTextField textField6; // Cédula/RUC
    private JTextField textField7; // ID producto
    private JTextField textField8; // Cantidad
    private JButton agregarButton;
    private JTable table1;
    private JButton guardarCobrarButton;
    private JButton cancelarButton;
    private JLabel PrecioUnitario;
    private JLabel TotalProducto;
    private JButton validarClienteButton;
    private JLabel Subtotal;
    private JLabel IVA;
    private JLabel Total;

    public Caja_Registradora(Connection connRol) {
        setTitle("Caja Registradora");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1700, 1000);
        setLocationRelativeTo(null);
        setVisible(true);

        // ---------------------- ESTÉTICA ----------------------
        table1.setFillsViewportHeight(true);
        table1.setRowHeight(30);
        table1.setShowGrid(true);
        table1.setGridColor(new Color(200, 200, 200));
        table1.setSelectionBackground(new Color(70, 130, 180));
        table1.setSelectionForeground(Color.WHITE);
        table1.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table1.getTableHeader().setBackground(new Color(240, 240, 240));
        table1.getTableHeader().setForeground(Color.DARK_GRAY);

        Color botonColor = Color.BLUE;
        JButton[] botones = {agregarButton, guardarCobrarButton, cancelarButton, logoutButton, validarClienteButton};
        for (JButton btn : botones) {
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setBackground(botonColor);
            btn.setForeground(Color.WHITE);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        JTextField[] campos = {textField1, textField2, textField3, textField5, textField6, textField7, textField8};
        for (JTextField tf : campos) {
            tf.setFont(new Font("Arial", Font.PLAIN, 16));
            tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            tf.setBackground(new Color(255, 255, 255, 230));
            tf.setToolTipText("Ingrese valor correspondiente");
        }

        Subtotal.setFont(new Font("Arial", Font.BOLD, 16));
        Subtotal.setForeground(new Color(50, 50, 50));
        IVA.setFont(new Font("Arial", Font.BOLD, 16));
        IVA.setForeground(new Color(50, 50, 50));
        Total.setFont(new Font("Arial", Font.BOLD, 18));
        Total.setForeground(new Color(70, 130, 180));

        // ---------------------- TABLA ----------------------
        String[] columnas = {"ID Producto", "Nombre Producto", "Precio Unitario", "Total Producto"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        table1.setModel(model);

        // ---------------------- ACCIONES ----------------------
        validarClienteButton.addActionListener(e -> validarORegistrarCliente());
        agregarButton.addActionListener(e -> agregarProducto());
        guardarCobrarButton.addActionListener(e -> guardarFactura());
        cancelarButton.addActionListener(e -> limpiarFormulario());
        logoutButton.addActionListener(e -> cerrarSesion());
    }

    private void validarORegistrarCliente() {
        String nombrecliente = textField1.getText().trim();
        String correo = textField2.getText().trim();
        String direccion = textField3.getText().trim();
        String telefono = textField5.getText().trim();
        String rucCedula = textField6.getText().trim();

        if (nombrecliente.isEmpty() || rucCedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar al menos el nombre y cédula/RUC del cliente.", "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = ConexionDB.conectar("cajerolocal", "cajero2025")) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "No se pudo conectar con la base de datos", "Error de conexión", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectSQL = "SELECT nombres, cedula FROM cliente WHERE cedula = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
            selectStmt.setString(1, rucCedula);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Cliente encontrado: " + rs.getString("nombres"), "Cliente existente", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String insertSQL = "INSERT INTO cliente (nombres, cedula, direccion, telefono, correo) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
                insertStmt.setString(1, nombrecliente);
                insertStmt.setString(2, rucCedula);
                insertStmt.setString(3, direccion);
                insertStmt.setString(4, telefono);
                insertStmt.setString(5, correo);

                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo registrar al cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al acceder a la base de datos: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarProducto() {
        String idproducto = textField7.getText().trim();
        String cantidadStr = textField8.getText().trim();

        if (idproducto.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese ID del producto y cantidad.", "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!cantidadStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Ingrese solo números enteros positivos.", "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.", "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = ConexionDB.conectar("cajerolocal", "cajero2025")) {
            String selectSQL = "SELECT producto, precio FROM productos WHERE id_producto = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
            selectStmt.setString(1, idproducto);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String nombreProducto = rs.getString("producto");
                double precioUnitario = rs.getDouble("precio");
                double totalProducto = precioUnitario * cantidad;

                PrecioUnitario.setText(String.format(Locale.US, "%.2f", precioUnitario));
                TotalProducto.setText(String.format(Locale.US, "%.2f", totalProducto));

                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                model.addRow(new Object[]{
                        idproducto, nombreProducto,
                        String.format(Locale.US, "%.2f", precioUnitario),
                        String.format(Locale.US, "%.2f", totalProducto)
                });

                recalcularTotales();

            } else {
                JOptionPane.showMessageDialog(this, "Producto no encontrado con el ID: " + idproducto, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error en base de datos: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarFactura() {
        String cedulaCliente = textField6.getText().trim();
        if (cedulaCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero valide o registre al cliente.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = ConexionDB.conectar("cajerolocal", "cajero2025")) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "No se pudo conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener cliente completo
            PreparedStatement psCliente = conn.prepareStatement("SELECT id_cliente, nombres, direccion, telefono, correo FROM cliente WHERE cedula = ?");
            psCliente.setString(1, cedulaCliente);
            ResultSet rsCliente = psCliente.executeQuery();

            if (!rsCliente.next()) {
                JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ClienteJSON clienteJSON = new ClienteJSON(
                    rsCliente.getInt("id_cliente"),
                    rsCliente.getString("nombres"),
                    cedulaCliente,
                    rsCliente.getString("direccion"),
                    rsCliente.getString("telefono"),
                    rsCliente.getString("correo")
            );

            double subtotal = parseDoubleFromString(Subtotal.getText());
            double iva = parseDoubleFromString(IVA.getText());
            double total = parseDoubleFromString(Total.getText());

            // Guardar factura en DB
            PreparedStatement psFactura = conn.prepareStatement(
                    "INSERT INTO facturas (id_cliente, subtotal, IVA, total) VALUES (?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            psFactura.setInt(1, clienteJSON.idCliente);
            psFactura.setDouble(2, subtotal);
            psFactura.setDouble(3, iva);
            psFactura.setDouble(4, total);
            psFactura.executeUpdate();

            ResultSet rsFactura = psFactura.getGeneratedKeys();
            int idFactura = 0;
            if (rsFactura.next()) idFactura = rsFactura.getInt(1);

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            PreparedStatement psDetalle = conn.prepareStatement(
                    "INSERT INTO detalle_factura (id_factura, id_producto, cantidad, precio_unitario, total_producto) VALUES (?, ?, ?, ?, ?)"
            );

            List<DetalleProductoJSON> detallesJSON = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                int idProducto = Integer.parseInt(model.getValueAt(i, 0).toString());
                String nombreProducto = model.getValueAt(i, 1).toString();
                double precioUnitario = Double.parseDouble(model.getValueAt(i, 2).toString());
                double totalProducto = Double.parseDouble(model.getValueAt(i, 3).toString());
                int cantidad = (int) (totalProducto / precioUnitario);

                psDetalle.setInt(1, idFactura);
                psDetalle.setInt(2, idProducto);
                psDetalle.setInt(3, cantidad);
                psDetalle.setDouble(4, precioUnitario);
                psDetalle.setDouble(5, totalProducto);
                psDetalle.addBatch();

                detallesJSON.add(new DetalleProductoJSON(idProducto, nombreProducto, cantidad, precioUnitario, totalProducto));
            }
            psDetalle.executeBatch();

            // Crear objeto factura JSON
            FacturaJSON facturaJSON = new FacturaJSON(idFactura, clienteJSON, detallesJSON, subtotal, iva, total);

            // Guardar JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter("factura_" + idFactura + ".json")) {
                writer.write(gson.toJson(facturaJSON));
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(this, "Error al crear archivo JSON: " + ioException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(this, "Factura guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la factura: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recalcularTotales() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        double subtotal = 0.0;
        double ivaRate = 0.12;

        for (int i = 0; i < model.getRowCount(); i++) {
            double totalProducto = Double.parseDouble(model.getValueAt(i, 3).toString());
            subtotal += totalProducto;
        }

        double iva = subtotal * ivaRate;
        double totalGeneral = subtotal + iva;

        Subtotal.setText(String.format(Locale.US, "%.2f", subtotal));
        IVA.setText(String.format(Locale.US, "%.2f", iva));
        Total.setText(String.format(Locale.US, "%.2f", totalGeneral));
    }

    private void limpiarFormulario() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);

        PrecioUnitario.setText("");
        TotalProducto.setText("");
        Subtotal.setText("0.00");
        IVA.setText("0.00");
        Total.setText("0.00");

        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
        textField8.setText("");
    }

    private void cerrarSesion() {
        dispose();
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }

    private double parseDoubleFromString(String text) throws NumberFormatException {
        return Double.parseDouble(text.replace(",", ".").trim());
    }

    // ----------------- CLASES JSON -----------------
    class FacturaJSON {
        int idFactura;
        ClienteJSON cliente;
        List<DetalleProductoJSON> productos;
        double subtotal;
        double iva;
        double total;

        public FacturaJSON(int idFactura, ClienteJSON cliente, List<DetalleProductoJSON> productos, double subtotal, double iva, double total) {
            this.idFactura = idFactura;
            this.cliente = cliente;
            this.productos = productos;
            this.subtotal = subtotal;
            this.iva = iva;
            this.total = total;
        }
    }

    class ClienteJSON {
        int idCliente;
        String nombre;
        String cedula;
        String direccion;
        String telefono;
        String correo;

        public ClienteJSON(int idCliente, String nombre, String cedula, String direccion, String telefono, String correo) {
            this.idCliente = idCliente;
            this.nombre = nombre;
            this.cedula = cedula;
            this.direccion = direccion;
            this.telefono = telefono;
            this.correo = correo;
        }
    }

    class DetalleProductoJSON {
        int idProducto;
        String nombreProducto;
        int cantidad;
        double precioUnitario;
        double totalProducto;

        public DetalleProductoJSON(int idProducto, String nombreProducto, int cantidad, double precioUnitario, double totalProducto) {
            this.idProducto = idProducto;
            this.nombreProducto = nombreProducto;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.totalProducto = totalProducto;
        }
    }
}
