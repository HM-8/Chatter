package backend.controller;

import backend.Client;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChatController {
    private static DataInputStream in=Client.getIncomingStream();
    private static DataOutputStream out=Client.getOutgoingStream();

    @FXML
    private static TextField chatTextField;
    @FXML
    private static TextArea chatTextArea;

    public static void main(String[] args) throws IOException {
        String text=in.readUTF();
        chatTextArea.setText(chatTextArea.getText()+ "\n Server:" + text);
    }
    public void TextHandler() throws IOException {
        out.writeUTF(String.valueOf(chatTextField));
    }

}
