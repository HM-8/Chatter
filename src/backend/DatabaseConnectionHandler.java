package backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void runMigration() {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_app?allowMultiQueries=true", "root", "rootroot");
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to load Connector");
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            System.out.println("Unable to connect to db");
            e.printStackTrace();
            System.exit(1);
        }
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            Statement finalStatement = statement;
            Files.list(new File("src/migrations").toPath())
                    .forEach(pathToMigration -> {
                        try{
                            String migration = Files.readString(pathToMigration);
                            finalStatement.executeUpdate(migration);
                        }catch (IOException e) {
                            e.printStackTrace();
                        }catch (SQLException ex){
                            System.out.println(ex.getMessage());
                            System.out.println(ex.getSQLState());
                        }
                    });
        } catch (IOException e){
            System.out.println("No migrations");
            e.printStackTrace();
        }
    }
}

