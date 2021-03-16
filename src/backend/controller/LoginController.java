package backend.controller;

import backend.Client;
import backend.models.ErrorMessage;
import backend.models.JSONizable;
import backend.models.Request;
import backend.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LoginController {
    private static ObjectMapper mapper = new ObjectMapper();
    private static DataOutputStream outgoingStream = Client.getOutgoingStream();
    private static DataInputStream incomingStream = Client.getIncomingStream();
    @FXML
    private TextField login_username;
    @FXML
    private TextField login_password;
    @FXML
    private Label login_error_message;
    @FXML
    private Label signup_page_label;
    @FXML
    private Button login_submit_button;

    @FXML
    public boolean validate() {
        if (login_username.getText().length() == 0) {
            login_error_message.setText("Username cannot be empty");
            return false;
        }
        if (login_password.getLength() < 8) {
            login_error_message.setText("password must be at least 8 characters");
            return false;
        }
        return true;
    }

    @FXML
    public void loginInSubmitButton() {
        if(!validate()){
            return;
        }
        try {
            //send request
            Request request = new Request("login", new String[]{login_username.getText(), login_password.getText()});
            outgoingStream.writeUTF(request.toJSON());
            //receive response
            JSONizable parsedInput = JSONizable.fromJSON(incomingStream.readUTF());
            if (parsedInput instanceof ErrorMessage) {
                ErrorMessage e = (ErrorMessage) parsedInput;
                System.out.println(e.message);
                login_error_message.setText(e.message);
            } else {
                User user = (User) parsedInput;
                Client client = Client.getClient();
                client.setId(user.id);
                client.setUsername(user.username);
                client.setFullName(user.getFullName());
                loadChatPage();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadChatPage() {
        try {
            Stage loginStage = (Stage) signup_page_label.getScene().getWindow();
            loginStage.close();
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("frontend/Chat.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Chatter");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadSignupPage() {
        try {
            Stage loginStage = (Stage) signup_page_label.getScene().getWindow();
            loginStage.close();
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("frontend/Signup.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Chatter");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't load window!");
        }
    }
}
