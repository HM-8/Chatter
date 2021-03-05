package backend.controller;

import backend.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private Label login_page_label;

    @FXML
    public void validation(){
        if(signup_firstname.equals(null)){
            sign_error_message.setText("First name can't be left empty");
        }else if(signup_lastname.equals(null)){
            sign_error_message.setText("Last name can't be left empty");
        }
        else if (signup_password.getLength()<8){
            sign_error_message.setText("password must be more than 8 strings");
        }else if (signup_password.getText().equals(signup_confirm_password.getText())){
            sign_error_message.setText("the password is correct");
        }else sign_error_message.setText("the password must be the same");
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


        UserRepository userRepository = new UserRepository();
        String userName = signup_username.getText();

        //Fix this
        if(userRepository.ifUserNameExists(userName)){
            sign_error_message.setText("Username already in use.please go back to login page or Enter another username");
            signup_username.clear();
        }else{
            String[] user = {
                    signup_firstname.getText(),
                    signup_lastname.getText(),
                    signup_username.getText(),
                    signup_password.getText()
            };
            var queryResult = userRepository.insertSingleUser(user);

            if(queryResult)
            {
                Stage loginStage =(Stage) login_page_label.getScene().getWindow();
                loginStage.close();
                Parent root = FXMLLoader.load(ClassLoader.getSystemResource("frontend/Chat.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Chatter");
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                System.out.println("Error: Didn't create user!");
            }


        }
    }

    @FXML
    public void loginPage() {

        try {
            System.out.println("signup page");

            Stage loginStage =(Stage) login_page_label.getScene().getWindow();
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
