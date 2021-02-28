package backend;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnectionHandler {
    public Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "chat_app";
        String databaseUser = "Hiwot";
        String databasePassword = "g69Db@23%%";
        String url = "jdbc:mysql://127.0.0.1/" + databaseName;

        try {
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return databaseLink;
    }
}

