package backend.controller;

import backend.Client;
import backend.models.Chat;
import backend.models.JSONizable;
import backend.models.Message;
import backend.models.Request;
import backend.routes.MessageRoutes;
import de.jensd.fx.glyphs.emojione.EmojiOne;
import frontend.MessageListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;

public class ChatController implements Initializable, EventHandler<ActionEvent> {
//    private static DataInputStream in = Client.getIncomingStream();
//    private static DataOutputStream out = Client.getOutgoingStream();

    public Chat currentChat;
    public ObservableList<Message> currentChatMessages = FXCollections.observableArrayList(new ArrayList<Message>());
    public ListView<Chat> listView;
    public String emoji_string;
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
        Thread thread = new Thread(new IncomingMessageListener());
        thread.start();
    }

    private void setCurrentChat(Chat chat) {
        currentChatMessages.clear();
        this.currentChat = chat;
        Request request = new Request("getMessages", new String[]{String.valueOf(chat.getId())});
//        Lock lock = Client.getLock();
//        lock.lock();
        DataOutputStream outputStream = Client.getOutgoingStream();
        DataInputStream inputStream = Client.getIncomingStream();
        try {
            synchronized (inputStream) {
                outputStream.writeUTF(request.toJSON());
                JSONizable[] array = JSONizable.fromJSONArray(inputStream.readUTF(), Message[].class);
                Message[] messages = (Message[]) array;
//            currentChatMessages.addAll(messages);
                currentChatMessages.addAll(Arrays.asList(messages));
//            message_list_view.getItems().setAll(currentChatMessages);
                message_list_view.setItems(currentChatMessages);
                message_list_view.refresh();
            }
        } catch (SocketTimeoutException e) {
            System.out.println("In getmessages methods canceled");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            lock.unlock();
        }

    }

    @Override
    public void handle(ActionEvent actionEvent) {
//            Lock lock = Client.getLock();
        try {
            Client client = Client.getClient();
            int clientId = client.getId();

            //send request
//            lock.lock();
            DataOutputStream out = Client.getOutgoingStream();
            out.writeUTF(new Message(client.getId(), (currentChat.getId()),chat_text_field.getText()).toJSON());
            chat_text_field.clear();

        } catch (SocketTimeoutException e) {
            System.out.println("in send method timeout");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            lock.unlock();
        }
    }

    public void submit(MouseEvent mouseEvent) {
        System.out.println("clicked");
    }

    public void pickFile(MouseEvent mouseEvent) {
        try {
            String className = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(className);
        } catch(Exception e) {
            e.printStackTrace();
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select File");
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        int length = (int) file.length();
        Client client = Client.getClient();
        int fromId = client.getId();
        int toId = currentChat.getId();
        String type = "file";

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Object[] args = new Object[]{fromId, null, toId, type, fileInputStream, length};

            MessageRoutes messageRoutes = new MessageRoutes();
            messageRoutes.send(args);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void pickImage(MouseEvent mouseEvent) {
        try {
            String className = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(className);
        } catch(Exception e) {
            e.printStackTrace();
        }
        JFileChooser picChooser = new JFileChooser();
        picChooser.setDialogTitle("Select Image");
        picChooser.showOpenDialog(null);
        File image = picChooser.getSelectedFile();
        int length = (int) image.length();
        Client client = Client.getClient();
        int fromId = client.getId();
        int toId = currentChat.getId();
        String type = "image";

        try {
            FileInputStream imageInputStream = new FileInputStream(image);
            Object[] args = new Object[]{fromId, null, toId, type, imageInputStream, length};
            MessageRoutes messageRoutes = new MessageRoutes();
            messageRoutes.send(args);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private class IncomingMessageListener implements Runnable {
        @Override
        public void run() {
//            Lock lock = Client.getLock();
            DataInputStream inputStream = Client.getIncomingStream();
            ;
            while (true) {
                try {
//                    lock.lock();
                    synchronized (inputStream) {
                        Message message = (Message) JSONizable.fromJSON(inputStream.readUTF());
//                        System.out.println(message);
                        currentChatMessages.add(message);
                        Thread.yield();
                    }
                    //chatTextArea.appendText(message.message+ "\n");
                } catch (SocketTimeoutException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
//                    lock.unlock();
                }
            }
        }
    }

    private Chat[] getUserChatsList() {
        Client client = Client.getClient();
        int clientId = client.getId();

        Request getAllChatRequest = new Request("myChats", new String[]{String.valueOf(clientId)});
//        Lock lock = Client.getLock();
        try {
//            lock.lock();
            DataOutputStream outputStream = Client.getOutgoingStream();
            DataInputStream inputStream = Client.getIncomingStream();
            synchronized (inputStream) {
                outputStream.writeUTF(getAllChatRequest.toJSON());
                String response = inputStream.readUTF();
                Chat[] chats = (Chat[]) JSONizable.fromJSONArray(response, Chat[].class);
                return chats;
            }

        } catch (SocketTimeoutException s) {
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            lock.unlock();
        }
        return new Chat[0];
    }
    public void emoji_chooser(MouseEvent event) throws IOException {
        int column = GridPane.getColumnIndex((Node) event.getSource());
        int row = GridPane.getRowIndex((Node) event.getSource());
        System.out.println(String.format("Node clicked at: column=%d, row=%d", column, row));
         if(column==0&&row==0){
             emoji_string= String.valueOf(EmojiOne.GRINNING);
         }else if(column==0&&row==1){
             emoji_string=String.valueOf(EmojiOne.GRIN);
         }else if(column==0&&row==2){
             emoji_string=String.valueOf(EmojiOne.RELAXED);
         }else if(column==0&&row==3){
             emoji_string=String.valueOf(EmojiOne.SLIGHT_SMILE);
         }else if(column==1&&row==0){
             emoji_string=String.valueOf(EmojiOne.JOY);
         }else if(column==1&&row==1){
             emoji_string=String.valueOf(EmojiOne.KISSING_CLOSED_EYES);
         }else if(column==1&&row==2){
             emoji_string=String.valueOf(EmojiOne.NERD);
         }else if(column==1&&row==3){
             emoji_string=String.valueOf(EmojiOne.ANGRY);
         }else if(column==2&&row==0){
             emoji_string=String.valueOf(EmojiOne.INNOCENT);
         }else if(column==2&&row==1){
             emoji_string=String.valueOf(EmojiOne.HEAD_BANDAGE);
         }else if(column==2&&row==2){
             emoji_string=String.valueOf(EmojiOne.HEARTS);
         }else if(column==2&&row==3){
             emoji_string=String.valueOf(EmojiOne.SCREAM_CAT);
         }else if(column==3&&row==0){
             emoji_string=String.valueOf(EmojiOne.FOX);
         }else if(column==3&&row==1){
             emoji_string=String.valueOf(EmojiOne.CRESCENT_MOON);
         }
         DataOutputStream dataOutputStream=Client.getOutgoingStream();

        Message mesg=new Message(Client.getClient().getId(),currentChat.getId(),emoji_string);
    }
}
