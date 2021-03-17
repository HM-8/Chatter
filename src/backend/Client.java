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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client {
    private static final int SOCKET_TIMEOUT = 1000;
    private static Client client=new Client();
    private static Lock lock = new ReentrantLock();
    private int id;
    public String username;
    public String fullName;
    private Socket socket;
    private static DataInputStream incomingStream;
    private static DataOutputStream outgoingStream;

    public static Client getClient() {
        return client;
    }

    public static Lock getLock() {
        return lock;
    }

    public synchronized static DataInputStream getIncomingStream() {
        return incomingStream;
    }

    public synchronized static DataOutputStream getOutgoingStream() {
        return outgoingStream;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
            this.socket.setSoTimeout(Client.SOCKET_TIMEOUT);
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
