package backend;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.*;
import java.util.Scanner;

public class Server implements Runnable{
    public static int port = 5000;
    private ServerSocket serverSocket;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(port);
        //Run migrations
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
                    String migration = Files.readString(pathToMigration);;
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
    public void run(){
        System.out.println("Server Running at port 5000");
        while(true){
            try {
                Socket clientSocket = serverSocket.accept();
                Thread connectionHandlerThread = new Thread(new ConnectionHandler(clientSocket));
                connectionHandlerThread.start();
                //TODO add connection thread to connections list
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
