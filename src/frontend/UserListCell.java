package frontend;

import backend.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Arrays;

public class UserListCell extends ListCell<User> {
    private  Label fullName = new Label();
    private  Label userName = new Label();
    private VBox vBox = new VBox();
    public UserListCell() {
        fullName.setMinHeight(20);
        fullName.setFont(new Font("Arial", 20));
        userName.setFont(new Font(14));
        vBox.getChildren().addAll(fullName, userName);
    }

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if(empty || user == null){
            setText(null);
            setGraphic(null);
        }else{
            System.out.println(user.username);
            fullName.setText(String.valueOf(user.getFullName()));
            userName.setText(String.valueOf(user.username));
            setGraphic(this.vBox);
        }
    }
}
