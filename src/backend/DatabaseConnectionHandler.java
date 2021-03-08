package backend;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnectionHandler {
    private static DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
    private Connection connection;
    public DatabaseConnectionHandler() {
        Dotenv dotenv = Dotenv.load();
        String dbHost = "localhost";
        String dbName = "chat_app";
        int port = 3306;

        String url = String.format("jdbc:mysql://%s:%d/%s?allowMultiQueries=true", dbHost, port, dbName);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, dotenv.get("DB_USER"), dotenv.get("DB_PASSWORD"));
        } catch (ClassNotFoundException e) {
            System.err.println("Unable to load JDBC Driver");
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException sqlException) {
            System.err.println("Unable to connect to Mysql server");
            sqlException.printStackTrace();
            System.out.println(sqlException.getSQLState());
            System.exit(1);
        }
    }

    public static DatabaseConnectionHandler getHandler() {
        return databaseConnectionHandler;
    }

    public Connection getConnection() {
        return connection;
    }

    public void runMigration() {
        try {
        Statement statement = getConnection().createStatement();
            File[] migrations =new File("src/migrations").listFiles();
            for (File migration : migrations) {
                try {
                    String migrationContent = Files.readString(migration.toPath());
                    statement.executeUpdate(migrationContent);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    System.out.println(ex.getSQLState());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}

