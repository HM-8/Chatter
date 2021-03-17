package backend.routes;

import backend.DatabaseConnectionHandler;
import backend.models.Chat;
import backend.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class ChatRoutes {
    private static Statement statement;
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        try {
            statement = DatabaseConnectionHandler.getHandler().getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void creatChat(Chat chat) {
        String update = String.format("insert into chats (title, type) values ('%s', %d)", chat.getTitle(), chat.typeInt());
        try {
            statement.executeUpdate(update, new int[]{1});
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int chatId = rs.getInt(1);
            chat.getMembers().stream().forEach(member -> {
                try {
                     statement.executeUpdate(String.format("insert into users_chats (user_id, chat_id) values (%s, %d);", ((User)member).id, chatId));
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }});
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }
}
