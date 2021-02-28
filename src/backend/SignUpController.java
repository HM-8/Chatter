package backend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
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
    public void validation(){
        if (signup_password.getLength()<8){
            sign_error_message.setText("password must be more than 8 strings");
        }else if (signup_password.equals(signup_confirm_password)){
            sign_error_message.setText("the password is correct");
        }else sign_error_message.setText("the password must be the same as the first one you typed");
    }

    @FXML
    public void signUpSubmitButton() throws IOException {
        if (signup_firstname.equals(null)){
            sign_error_message.setText("x-error first name can't be left Empty" );
            //else send to the database
        }
        else if (signup_lastname.equals(null)){
            sign_error_message.setText("x-error Last name can't be left Empty");
            //else send to the database
        }
        else if (signup_username.equals(null)|| signup_username.getLength()<4){
            sign_error_message.setText("x-error username can't be left Empty and must be more than Four character");
        }
        else if (signup_password.equals(null)|| signup_password.getLength()<8){
            sign_error_message.setText("x-error password can't be left Empty and must be more than Eight character");
        }
        Stage stage;
        stage= FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage.show();
    }

    @FXML
    public void loginInSubmitButton() throws IOException {
        if (signup_username.equals(null)|| signup_username.getLength()<4){
            sign_error_message.setText("x-error username can't be left Empty and must be more than Four character");
        }
        else if (signup_password.equals(null)|| signup_password.getLength()<8){
            sign_error_message.setText("x-error password can't be left Empty and must be more than Eight character");
        }
    }
}
