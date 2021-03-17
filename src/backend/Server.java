package backend;

import backend.models.Message;
import backend.models.User;
import backend.routes.UserRoutes;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Server implements Runnable{
    public static int port = 5000;
    private ServerSocket serverSocket;
    private static ArrayList<ConnectionHandler> connections = new ArrayList<>();
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(port);
        DatabaseConnectionHandler.getHandler().runMigration();
    }

    public static void broadcast(Message message) {
        Integer[] userIdList = UserRoutes.getUsersForChat(message.to);
        Predicate<ConnectionHandler> isInChat = connection -> Arrays.asList(userIdList).contains(connection.getClient_id());
        for(ConnectionHandler connection : connections.stream().filter(isInChat).collect(Collectors.toList())){
            try {
                DataOutputStream outputStream =  new DataOutputStream(connection.getSocket().getOutputStream());
                outputStream.writeUTF(message.toJSON());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void addToConnections(ConnectionHandler connectionHandler){
        connections.add(connectionHandler);
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
