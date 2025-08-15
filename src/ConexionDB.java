import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    public static Connection conectar(String usuario, String password) {
        String url = "jdbc:mysql://localhost:3307/supermercadodb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection conexion = null;

        try {
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("Conexión exitosa con usuario: " + usuario);
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }

        return conexion;
    }
}

