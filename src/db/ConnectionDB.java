package db;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String url = "jdbc:mysql://api-bibliotech-emersonnevess80.k.aivencloud.com:11667/defaultdb"
            + "?useSSL=true&requireSSL=true&verifyServerCertificate=false"; // se estiver testando
    private static final String user = dotenv.get("DB_USER");
    private static final String password = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
