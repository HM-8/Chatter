package backend;

import backend.models.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
    public static int port = 5000;
    private ServerSocket serverSocket;
    private static ArrayList<Socket> connections = new ArrayList<>();
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(port);
        DatabaseConnectionHandler.getHandler().runMigration();
    }

    public static void broadcast(Message message) {
        for(Socket socket : connections){
            try {
                DataOutputStream outputStream =  new DataOutputStream(socket.getOutputStream());
                outputStream.writeUTF(message.toJSON());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void addToConnections(Socket clientSocket){
        connections.add(clientSocket);
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
