package backend.controller;

import backend.Client;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChatController {
    private static DataInputStream in=Client.getIncomingStream();
    private static DataOutputStream out=Client.getOutgoingStream();

    @FXML
    private static TextField chat_text_field;
    @FXML
    private static Label username_label;
    @FXML
    private static ListView person_info;
    @FXML
    private static ListView chat_list;
    @FXML
    private static Label username_text;
    @FXML
    private static ScrollPane chat_display_panel;

    private static Label msg=null;
    private static Label sent=null;
    public static void main(String[] args) throws IOException {

        String text=in.readUTF();
        msg.setText(text);
        msg.setAlignment(Pos.BASELINE_LEFT);
        chat_display_panel.setContent(msg);
//        chatTextArea.setText(chatTextArea.getText()+ "\n Server:" + text);
    }
    public void TextHandler() throws IOException {
        out.writeUTF(String.valueOf(chat_text_field));
        String sent_msg=String.valueOf(chat_text_field);
        sent.setText(sent_msg);
        sent.setAlignment(Pos.BASELINE_RIGHT);
        chat_display_panel.setContent(msg);
    }

}
