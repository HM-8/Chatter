package backend;

import backend.models.*;
import backend.routes.MessageRoutes;
import backend.routes.UserRoutes;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
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
        while (true) {
            try {
                JSONizable parsedInput = JSONizable.fromJSON(incomingStream.readUTF());
                if (parsedInput instanceof Request) {
                    Request request = (Request) parsedInput;
                    switch (request.route) {
                        case "login": {
                            JSONizable result = UserRoutes.login((ArrayList) request.data);
                            if (!(result instanceof ErrorMessage)) {
                                Server.addToConnections(this.socket);
                            }
                            this.outgoingStream.writeUTF(result.toJSON());
                            break;
                        }
                        case "signup": {
                            JSONizable result = UserRoutes.signup((ArrayList) request.data);
                            if (!(result instanceof ErrorMessage)) {
                                Server.addToConnections(this.socket);
                            }
                            this.outgoingStream.writeUTF(result.toJSON());
                            break;
                        }
                        case "send": {
                            System.out.println("Data: " + request.data.toString());
                            MessageRoutes.send((ArrayList) request.data);
                            break;
                        }
                        case "myChats": {
                            Chat[] userChatsArraylist = UserRoutes.getChats((ArrayList) request.data);
                            this.outgoingStream.writeUTF(mapper.writeValueAsString(userChatsArraylist));
                        }
                    }
                } else if (parsedInput instanceof Message) {
                    Message message = (Message) parsedInput;
                    System.out.println(message.toJSON());
                    Server.broadcast(message);
                }
            } catch (SocketException se) {
                System.out.println(se.getMessage());
                closeThread();
                return;
            } catch (IOException e) {
                //When client disconnects
                System.out.println("Client disconnected");
                closeThread();
            }
        }
    }

    private void closeThread() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Thread.currentThread().interrupt();
        }
    }


    private void init() {
        //initialize the connection
    }
}
