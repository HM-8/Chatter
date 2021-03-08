package backend;

import backend.models.ErrorMessage;
import backend.models.JSONizable;
import backend.models.Message;
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
                    switch (request.route){
                        case "login":{
                            JSONizable result = UserRoutes.login((ArrayList) request.data);
                            if(!(result instanceof ErrorMessage) ){
                                Server.addToConnections(this.socket);
                            }
                            this.outgoingStream.writeUTF(result.toJSON());
                            break;
                        }
                        case "signup": {
                            JSONizable result = UserRoutes.signup((ArrayList) request.data);
                            if(!(result instanceof ErrorMessage) ){
                                Server.addToConnections(this.socket);
                            }
                            this.outgoingStream.writeUTF(result.toJSON());
                            break;
                        }
                    }
                }else if (parsedInput instanceof Message){
                    Message message = (Message) parsedInput;
                    System.out.println(message.toJSON());
                    Server.broadcast(message);
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
