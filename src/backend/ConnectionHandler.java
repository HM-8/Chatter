package backend;

import backend.models.JSONizable;
import backend.models.Request;
import backend.routes.UserRoutes;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionHandler implements Runnable {
    private Socket socket;
    private DataInputStream incomingStream;
    private DataOutputStream outgoingStream;
    private static ObjectMapper mapper = new ObjectMapper();
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
                JSONizable parsedInput = JSONizable.fromJSON(incomingStream.readUTF());
                if (parsedInput instanceof Request){
                    Request request = (Request) parsedInput;
                    System.out.println(request.data.getClass());
                    switch (request.route){
                        case "login":{
                            this.outgoingStream.writeUTF(UserRoutes.login((ArrayList) request.data).toJSON());
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                //When client disconnects
                System.out.println("Client disconnected");
                e.printStackTrace();
                break;
            }
        }
    }

    private void init() {
        //initialize the connection
    }
}
