package backend.controller;

import backend.Client;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable, EventHandler<ActionEvent> {
    private static DataInputStream in = Client.getIncomingStream();
    private static DataOutputStream out = Client.getOutgoingStream();

    @FXML
    private TextField chatTextField;
    @FXML
    private static TextArea chatTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.chatTextField.setText("");
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        try {
            out.writeUTF(chatTextField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
