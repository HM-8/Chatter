package backend.controller;

import backend.routes.UserRoutes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
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
    public void validation(){
        if (login_username.getText() == null) {
            login_error_message.setText("Empty");
        }
        if (login_password.getLength()<8){
            login_error_message.setText("password must be more than 8 strings");
        }
    }

    @FXML
    public void loginInSubmitButton() {
        try {
            UserRoutes userRoutes = new UserRoutes();
            String[] user = {login_username.getText(), login_password.getText()};
            var queryResult = userRoutes.selectSingleUser(user);

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    System.out.println("Logged in successfully!");

                    Stage loginStage =(Stage) signup_page_label.getScene().getWindow();
                    loginStage.close();
                    Parent root = FXMLLoader.load(ClassLoader.getSystemResource("frontend/Chat.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Chatter");
                    stage.setScene(new Scene(root));
                    stage.show();
                } else {
                    System.out.println("Invalid login! Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void signupPage() {

        try {
            System.out.println("signup page");

            Stage loginStage =(Stage) signup_page_label.getScene().getWindow();
            loginStage.close();
            Parent root = FXMLLoader.load(ClassLoader.getSystemResource("frontend/Signup.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Chatter");
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            System.out.println("Can't load window!");
        }
    }
}
