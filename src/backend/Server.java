package backend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    public static int port = 5000;
    private ServerSocket serverSocket;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(port);
        DatabaseConnectionHandler.getHandler().runMigration();
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
