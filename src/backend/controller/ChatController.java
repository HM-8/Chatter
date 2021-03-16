package backend.controller;

import backend.Client;
import backend.models.JSONizable;
import backend.models.Message;
import backend.models.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.StringValue;
import de.jensd.fx.glyphs.emojione.EmojiOne;
import de.jensd.fx.glyphs.emojione.EmojiOneView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements EventHandler<ActionEvent> {
    private static DataInputStream in = Client.getIncomingStream();
    private static DataOutputStream out = Client.getOutgoingStream();
    @FXML
    private TextField chat_text_field;
    @FXML
    private static GridPane emoji_gridPane=new GridPane();
    
    private static ArrayList<EmojiOne> emojiOnes=new ArrayList<>();

    private  BooleanProperty visible = new SimpleBooleanProperty(false);


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {


        this.chat_text_field.setText("");
        Thread thread = new Thread(new IncomingMessageListener());
        thread.start();
    }


    @Override
    public void handle(ActionEvent actionEvent) {
        try {
            Client client = Client.getClient();
            int clientId = client.getId();

            //send request
            Request request = new Request("send", new String[]{String.valueOf(clientId), chat_text_field.getText()});
            out.writeUTF(request.toJSON());
            out.writeUTF(new Message(client.getId(),chat_text_field.getText()).toJSON());
            chat_text_field.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private class IncomingMessageListener implements Runnable {
        @Override
        public void run() {
            DataInputStream inputStream = Client.getIncomingStream();
            while (true){
                try {
                    Message message = (Message) JSONizable.fromJSON(inputStream.readUTF());
                    System.out.println(message);
                    //chatTextArea.appendText(message.message+ "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
