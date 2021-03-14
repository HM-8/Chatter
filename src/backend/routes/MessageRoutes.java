package backend.routes;

import backend.DatabaseConnectionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}
