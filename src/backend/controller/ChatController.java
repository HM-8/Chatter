package backend.controller;

import backend.Client;
import backend.models.Chat;
import backend.models.JSONizable;
import backend.models.Message;
import backend.models.Request;
import frontend.MessageListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ChatController implements Initializable, EventHandler<ActionEvent> {
    private static DataInputStream in = Client.getIncomingStream();
    private static DataOutputStream out = Client.getOutgoingStream();

    public Chat currentChat;
    public ObservableList<Message> currentChatMessages = FXCollections.observableArrayList(new ArrayList<Message>());
    public ListView<Chat> listView;
    @FXML
    public ListView<Message> message_list_view;

    @FXML
    private TextField chat_text_field;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.chat_text_field.setText("");
        listView.setCellFactory(list -> new ChatListCell()
        );
        listView.getItems().setAll(getUserChatsList());
        listView.refresh();
        System.out.println(listView.getItems());
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Chat chat = listView.getSelectionModel().getSelectedItems().get(0);
                setCurrentChat(chat);
            }
        });
        message_list_view.setCellFactory(messageListView -> {
            return new MessageListCell();
        });
//        Thread thread = new Thread(new IncomingMessageListener());
//        thread.start();
    }

    private void setCurrentChat(Chat chat) {
        currentChatMessages.clear();
        this.currentChat = chat;
        Request request = new Request("getMessages", new String[]{String.valueOf(chat.getId())});
        DataOutputStream outputStream = Client.getOutgoingStream();
        DataInputStream inputStream = Client.getIncomingStream();
        try {
            outputStream.writeUTF(request.toJSON());
            JSONizable[] array =  JSONizable.fromJSONArray(inputStream.readUTF(), Message[].class);
            Message[] messages = (Message[])array;
//            currentChatMessages.addAll(messages);
            currentChatMessages.addAll(Arrays.asList(messages));
//            message_list_view.getItems().setAll(currentChatMessages);
        message_list_view.setItems(currentChatMessages);
            message_list_view.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handle(ActionEvent actionEvent) {
        try {
            Client client = Client.getClient();
            int clientId = client.getId();

            //send request
            Request request = new Request("send", new String[]{String.valueOf(clientId), chat_text_field.getText()});
            out.writeUTF(request.toJSON());
            out.writeUTF(new Message(client.getId(), chat_text_field.getText()).toJSON());
            chat_text_field.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class IncomingMessageListener implements Runnable {
        @Override
        public void run() {
            DataInputStream inputStream = Client.getIncomingStream();
            while (true) {
                try {
                    Message message = (Message) JSONizable.fromJSON(inputStream.readUTF());
                    System.out.println(message);
                    //chatTextArea.appendText(message.message+ "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Chat[] getUserChatsList() {
        Client client = Client.getClient();
        int clientId = client.getId();

        Request getAllChatRequest = new Request("myChats", new String[]{String.valueOf(clientId)});

        try {
            DataOutputStream outputStream = Client.getOutgoingStream();
            outputStream.writeUTF(getAllChatRequest.toJSON());
            DataInputStream inputStream = Client.getIncomingStream();
            String response = inputStream.readUTF();
            Chat[] chats = (Chat[]) JSONizable.fromJSONArray(response, Chat[].class);
            return chats;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Chat[0];
    }
}
