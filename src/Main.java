import backend.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server;
        Thread serverThread;
        try {
            server = new Server();
            serverThread = new Thread(server);
            serverThread.start();
        } catch (IOException e) {
            System.out.println("Unable to Initialize Server");
            e.printStackTrace();
        }
    }
}
