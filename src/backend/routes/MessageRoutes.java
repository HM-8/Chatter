package backend.routes;

import backend.DatabaseConnectionHandler;
import backend.models.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MessageRoutes {
    private static Statement statement;
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        try {
            statement = DatabaseConnectionHandler.getHandler().getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected MessageRoutes () {
    }

    public static void send(ArrayList args) {
        int clientId = Integer.valueOf((String) args.get(0));
        String query = String.format("INSERT INTO messages (sent_from, message) VALUES ('%d', '%s')", clientId, args.get(1));

        try {
            statement.executeUpdate(query);
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
