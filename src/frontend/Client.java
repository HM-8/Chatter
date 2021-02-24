package frontend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class Client {
    private Socket socket;
    private DataInputStream incomingStream;
    private DataOutputStream outgoingStream;

    public Client() {
        try {
            this.socket = new Socket("localhost", 5000);
            this.incomingStream = new DataInputStream(socket.getInputStream());
            this.outgoingStream = new DataOutputStream(socket.getOutputStream());
        }catch (ConnectException e){
            // Unable to connect to server
            System.out.println("Unable to connect to server");
            System.exit(1);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.outgoingStream.writeUTF("Hello World");
        client.incomingStream.readUTF();
    }
}
