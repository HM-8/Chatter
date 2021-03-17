package backend.controller;

import backend.models.Chat;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.geometry.Pos.CENTER;

public class ChatListCell extends ListCell<Chat>{
//    @FXML
    public Label chat_title = new Label() ;
//    @FXML
    private Label chat_message_badge =  new Label() ;

    private HBox hBox = new HBox();

    public ChatListCell() {
        Region region = new Region();
        region.setMaxWidth(120);
        hBox.setHgrow(region, Priority.ALWAYS);
        hBox.getChildren().addAll(chat_title, region, chat_message_badge);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        chat_message_badge.setMaxWidth(70);
        chat_message_badge.setPrefHeight(25);
        chat_message_badge.setPrefWidth(25);
        chat_message_badge.setStyle("-fx-background-color: #1D3557; -fx-background-radius: 25; -fx-font-weight: 500;");
        chat_message_badge.setTextFill(Color.valueOf("#e4e4e4"));

        chat_title.setMaxWidth(100.0);
        chat_title.setPrefHeight(35);
        chat_title.setPrefWidth(70);

//        chat_title.setStyle("-fx-background-color: #1D3557; -fx-background-radius: 25; -fx-font-weight: 500;");

//        loadFXML();
    }
//
//    private void loadFXML (){
//        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("frontend/ChatListCell.fxml"));
//        loader.setController(this);
//////        loader.setRoot(this);
//        try {
//            loader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    protected void updateItem(Chat chat, boolean empty) {
        super.updateItem(chat, empty);
        if(empty || chat == null){
            setText(null);
            setGraphic(null);
        }else{
            this.chat_title.setText(String.valueOf(chat.getTitle()));
            this.chat_message_badge.setText(String.valueOf(Math.random() * 100));
            setGraphic(this.hBox);
        }
    }
//
//    @FXML
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        this.chat_message_badge.setText("");
//    }
}
