package frontend;

import backend.Client;
import backend.models.Message;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import org.intellij.lang.annotations.JdkConstants;

public class MessageListCell extends ListCell<Message> {
    private Label message_contents = new Label();

    private HBox  hBox  = new HBox();
    public MessageListCell() {
        hBox.getChildren().add(message_contents);
        hBox.setMaxHeight(100);
        hBox.setMaxWidth(430);
        hBox.setPadding(new Insets(10, 5,10, 5));
        hBox.setStyle("-fx-background-color: #f4f4f4; -fx-background-radius: 10; -fx-border-radius: 10");
        message_contents.setFont(new Font("Arial", 18));
//        message_contents.setMaxWidth(100);
    }

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);
        if(empty || message == null){
            setText(null);
            setGraphic(null);
        }else{
        System.out.println(message.message);
            message_contents.setText(message.message);
            if (message.from == Client.getClient().getId()) {
                hBox.setAlignment(Pos.CENTER_RIGHT);
            }
//            setText(message.message);
            setGraphic(hBox);
        }
    }
}
