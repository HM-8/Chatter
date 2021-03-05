package backend.repository;

import backend.DatabaseConnectionHandler;
import backend.models.Message;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository {
    private static Statement statement;

    static {
        try {
            statement = DatabaseConnectionHandler.getHandler().getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertSingleMessage(String[] message) {
        String query = MessageFormat.format("INSERT INTO messages (sent_from, message) VALUES ({0}, ''{1}'')", message);

        try {
            int queryResult = statement.executeUpdate(query);

            if(queryResult == 1)
            {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Message> readMessages()
    {
        String query = "SELECT * FROM messages";
        List<Message> messages = new ArrayList<>();

        try {
            var queryResult = statement.executeQuery(query);

            try {
                while(queryResult.next()) {
                    Message message = new Message();
                    message.id = queryResult.getInt("id");
                    message.sentFrom = queryResult.getInt("sent_from");
                    message.message = queryResult.getString("message");

                    messages.add(message);
                }
            } catch (Exception e) { e.printStackTrace(); }

            return messages;
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
