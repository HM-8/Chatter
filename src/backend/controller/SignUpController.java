package backend.controller;

import backend.Client;
import backend.models.ErrorMessage;
import backend.models.JSONizable;
import backend.models.Request;
import backend.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SignUpController {
    private DataInputStream incomingStream = Client.getIncomingStream();
    private DataOutputStream outgoingStream = Client.getOutgoingStream();
    @FXML
    private TextField signup_firstname;
    @FXML
    private TextField signup_lastname;
    @FXML
    private TextField signup_username;
    @FXML
    private PasswordField signup_password;
    @FXML
    private PasswordField signup_confirm_password;
    @FXML
    private Label sign_error_message;
    @FXML
    private Button signup_submit_button;
    @FXML
    private Label login_page_label;

    @FXML
    public boolean validate() {
        boolean b = false;
//        System.out.println(signup_password.getText() + "x" + signup_confirm_password.getText());
        if (signup_firstname.getLength() == 0|| signup_lastname.getLength() == 0 || signup_username.getLength() == 0) {
            sign_error_message.setText("Empty fields aren't allowed");
            return b;
        } else if (signup_password.getLength() < 8) {
            sign_error_message.setText("password must be more than 8 strings");
            return b;
//        } else if (signup_confirm_password.getLength() <8 || !signup_password.getText().equals(signup_confirm_password.getText())) {
//            sign_error_message.setText("passwords don't match");
//            return b;
//
        } else {
            return true;
        }
    }

    @FXML
    public void signUpSubmitButton() throws IOException {
        if (!validate()) {
            return;
        }
        Request request = new Request("signup",new String[]{signup_firstname.getText(), signup_lastname.getText(), signup_username.getText(), signup_password.getText()});
        outgoingStream.writeUTF(request.toJSON());

        JSONizable parsedInput = JSONizable.fromJSON(incomingStream.readUTF());
        if(parsedInput instanceof ErrorMessage){
            ErrorMessage e = (ErrorMessage) parsedInput;
            sign_error_message.setText(e.message);
        }else{
            User user = (User) parsedInput;
            Client client = Client.getClient();
            client.setId(user.id);
            client.setUsername(user.username);
            client.setFullName(user.getFullName());
            loadChatPage();
        }
    }
    private void loadChatPage() {
        try {
            Stage loginStage = (Stage) login_page_label.getScene().getWindow();
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
    public void loadLoginPage() {
        try {
            Stage loginStage = (Stage) login_page_label.getScene().getWindow();
            loginStage.close();
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("frontend/Login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Chatter");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Can't load window!");
        }
    }
}
