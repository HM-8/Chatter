package backend;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class Client {
    private static Client client=new Client();

    private int id;
    public String username;
    public String fullName;
    private Socket socket;
    private static DataInputStream incomingStream;
    private static DataOutputStream outgoingStream;

    public static Client getClient() {
        return client;
    }

    public static DataInputStream getIncomingStream() {
        return incomingStream;
    }

    public static DataOutputStream getOutgoingStream() {
        return outgoingStream;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Client() {
        try {
            this.socket = new Socket("localhost", 5000);
            this.incomingStream = new DataInputStream(socket.getInputStream());
            this.outgoingStream = new DataOutputStream(socket.getOutputStream());
        }catch (ConnectException e){
            System.out.println("Unable to connect to server");
            System.exit(1);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
