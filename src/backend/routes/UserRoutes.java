package backend.routes;

import backend.DatabaseConnectionHandler;
import backend.models.ErrorMessage;
import backend.models.JSONizable;
import backend.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
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
        String query = String.format("SELECT id, user_name, CONCAT(first_name, \" \" , last_name) FROM users WHERE user_name = '%s' AND password = '%s' Limit 1;", args.get(0), args.get(1));
        System.out.println(query);
        try {
            ResultSet qr = statement.executeQuery(query);
            qr.next();
            User user = new User(qr.getInt(1), qr.getString(2), qr.getString(3));
            return user;
        } catch (SQLException e) {
                System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            if(e.getSQLState() == "S1000") {
                return new ErrorMessage("Username or password Incorrect");
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insertSingleUser(String[] user) {
        String query = MessageFormat.format("INSERT INTO users (first_name, last_name, user_name, password) Values (''{0}'', ''{1}'', ''{2}'', ''{3}'')", user);

        try {
            int queryResult = statement.executeUpdate(query);

            if (queryResult == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean ifUserNameExists(String userName) {
        String query = MessageFormat.format("SELECT count(1) FROM users WHERE user_name = ''{0}''", userName);

        try {
            ResultSet queryResult = statement.executeQuery(query);

            if (queryResult.getInt(1) == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
