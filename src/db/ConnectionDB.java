package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String url = "jdbc:mysql://localhost:3306/acervoinclusivo";
    private static final String user = "root";
    private static final String password = "74569";

//    private static final String url = System.getenv("DB_URL");
//    private static final String user = System.getenv("DB_USER");
//    private static final String password = System.getenv("DB_PASSWORD");
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
