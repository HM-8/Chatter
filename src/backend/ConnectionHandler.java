package backend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private Socket socket;
    private DataInputStream incomingStream;
    private DataOutputStream outgoingStream;

    public ConnectionHandler(Socket clientSocket) {
        this.socket = clientSocket;
        try {
            this.incomingStream = new DataInputStream(this.socket.getInputStream());
            this.outgoingStream = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        init();
        while(true){
            try {
                String incomingMessage = this.incomingStream.readUTF();
                System.out.println(incomingMessage);
            } catch (IOException e) {
                //When client disconnects
                System.out.println("Client disconnected");
                break;
            }
        }
    }

    private void init() {
        //initialize the connection
    }
}
