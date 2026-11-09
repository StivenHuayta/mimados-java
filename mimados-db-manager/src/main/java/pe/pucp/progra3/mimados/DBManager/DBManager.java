package pe.pucp.progra3.mimados.DBManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBManager {

    private static String host;
    private static String puerto;
    private static String esquema;
    private static String user;
    private static String password;
    private static DBManager instance;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("No se pudo cargar el driver MySQL: " + e.getMessage());
        }

        try {
            ResourceBundle rb = ResourceBundle.getBundle("db");
            host = rb.getString("host").trim();
            puerto = rb.getString("puerto").trim();
            esquema = rb.getString("esquema").trim();
            user = rb.getString("user").trim();
            password = rb.getString("password").trim();
        } catch (Exception e) {
            System.err.println("Error al cargar db.properties en DBManager: " + e.getMessage());
        }

        instance = new DBManager();
    }

    public Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + puerto + "/" + esquema +
                "?useSSL=false&allowPublicKeyRetrieval=true&sslMode=DISABLED&serverTimezone=UTC";
        return DriverManager.getConnection(url, user, password);
    }

    public static DBManager getInstance() {
        return instance;
    }
}
