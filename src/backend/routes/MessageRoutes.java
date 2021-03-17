package backend.routes;

import backend.DatabaseConnectionHandler;
import backend.models.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MessageRoutes {
    private static Statement statement;
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        try {
            statement = DatabaseConnectionHandler.getHandler().getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MessageRoutes () {
    }

    public static void send(Message m) {
        String query = String.format("INSERT INTO messages (`from`,`to` , content) VALUES (%d, %d, '%s')", m.from, m.to,m.message);

        try {
            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void send(Object[] args) {
        String query = "INSERT INTO messages (from_id, content, to_id, type, file) VALUES (?, ?, ? , ?, ?); ";

        try {
            PreparedStatement send = DatabaseConnectionHandler.getHandler().getConnection().prepareStatement(query);
            send.setInt(1, (int)args[0]);
            send.setString(2, (String)args[1]);
            send.setInt(3, (int)args[2]);
            send.setString(4, (String)args[3]);
            send.setBinaryStream(5, (FileInputStream)args[4], (int)args[5]);
            send.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Message[] getMessages(ArrayList<String> data) {
        String query = String.format("select * from messages where `to`=%s;", data.get(0));
        ArrayList<Message> messagesArrayList = new ArrayList<>();
        try {
            Statement statement = DatabaseConnectionHandler.getHandler().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                messagesArrayList.add(new Message(rs.getInt("id"), rs.getInt("from"), rs.getString("content"),rs.getInt("to")));
            }
            Message[] messagesList= new Message[messagesArrayList.size()];
            return messagesArrayList.toArray(messagesList);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return new Message[0];
    }
}
