package backend.routes;

import backend.DatabaseConnectionHandler;
import backend.models.Chat;
import backend.models.ErrorMessage;
import backend.models.JSONizable;
import backend.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserRoutes {
    private static Statement statement;
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        try {
            statement = DatabaseConnectionHandler.getHandler().getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected UserRoutes() {
    }

    public static JSONizable login(ArrayList args) throws JsonProcessingException {
        String query = String.format("SELECT id, user_name, first_name, last_name FROM users WHERE user_name = '%s' AND password = '%s' Limit 1;", args.get(0), args.get(1));
        System.out.println(query);
        try {
            ResultSet qr = statement.executeQuery(query);
            qr.next();
            User user = new User(qr.getInt(1), qr.getString(2), qr.getString(3), qr.getString(4));
            return user;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            if (e.getSQLState() == "S1000") {
                return new ErrorMessage("Username or password Incorrect");
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONizable signup(ArrayList args) {
        String query = String.format("INSERT INTO users (first_name, last_name, user_name, password) Values ('%s', '%s', '%s', '%s')", args.get(0), args.get(1), args.get(2), args.get(3));
        try {
            int queryResult = statement.executeUpdate(query);
            if (queryResult == 1) {
                query = String.format("SELECT id, user_name, first_name, last_name FROM users WHERE user_name = '%s'", args.get(2));
                ResultSet rs = statement.executeQuery(query);
                rs.next();
                return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
            }
        } catch (SQLException e) {
            if (e.getSQLState() == "23000") {
                return new ErrorMessage("Username already exists, try again");
            }
        }
        return new ErrorMessage("Unable to signup, try again");
    }

    public static Chat[] getChats(ArrayList data) {
        String user_id = String.valueOf(data.get(0));
        String query = String.format("SELECT c.id as chat_id, c.type as chat_type, u.id as user_id, u.first_name , u.last_name, u.user_name from chats c JOIN users_chats cv1 on cv1.user_id = %s and c.id = cv1.chat_id JOIN users_chats cv2 on cv2.chat_id = cv1.chat_id JOIN users u on cv2.user_id = u.id;", user_id);
        try {
            ResultSet rs = statement.executeQuery(query);
            ArrayList<Chat> chatArrayList = new ArrayList<>();
            Chat newChat;
            while (rs.next()) {
                String chatType = rs.getString("chat_type");
                newChat = new Chat(rs.getInt("chat_id"));
                //Create a newMember
                User newMember = new User();
                newMember.id = rs.getInt("user_id");
                newMember.firstName = rs.getString("first_name");
                newMember.lastName = rs.getString("last_name");
                newMember.username = rs.getString("user_name");

                //if chatList contains the chat add a member to the existing chat
                if (chatArrayList.contains(newChat)) {
                    Chat existingChat = chatArrayList.get(chatArrayList.indexOf(newChat));
                    existingChat.addMember(newMember);
                    if(chatType.equals("user") && !(String.valueOf(newMember.id).equals(user_id))){
                        existingChat.setTitle(newMember.username);
                    }
                } else {
                    //if the chat is not in the list add it to the list and add a member to it
                    newChat.addMember(newMember);
                    if (chatType.equals("user")) {
                        //if the chat is of type 'user', set its title to the user's username
                        if (newMember.id != Integer.parseInt(user_id)) {
                            newChat.setTitle(newMember.username);
                        }
                    }
                    chatArrayList.add(newChat);
                }
            }
            Chat[] chatsArray = new Chat[chatArrayList.size()];
            return chatArrayList.toArray(chatsArray);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.out.println(sqlException.getSQLState());
        }
        return null;
    }

    public static Integer[] getUsersForChat(int to) {
        String query = String.format("Select u.id from users u join users_chats uc on uc.chat_id = %d", to);
        ArrayList<Integer> idArrayList = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                idArrayList.add(rs.getInt("id"));
            }
            Integer[] idList = new Integer[idArrayList.size()];
            return idArrayList.toArray(idList);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return new Integer[0];
    }
    public User[] getAllUsers(String usernameQueryString) {
        String query;
        if(usernameQueryString == null){
            query = "Select users.id, users.user_name, users.first_name, users.last_name from users;";
        }
        else{
            query = String.format("Select users.id, users.user_name, users.first_name, users.last_name from users where users.user_name like %s;", usernameQueryString);
        }
        ArrayList<User> userArrayList = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                userArrayList.add(new User(rs.getInt("id"), rs.getString("user_name"), rs.getString("first_name"), rs.getString("last_name")));
            }
            User[] users = new User[userArrayList.size()];
            return users;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return new User[0];
    }
}
