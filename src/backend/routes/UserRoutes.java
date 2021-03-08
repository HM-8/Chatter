package backend.routes;

import backend.DatabaseConnectionHandler;
import backend.Server;
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

    public static JSONizable signup(ArrayList args) {
        String query = String.format("INSERT INTO users (first_name, last_name, user_name, password) Values ('%s', '%s', '%s', '%s')", args.get(0), args.get(1), args.get(2), args.get(3));
        try {
            int queryResult = statement.executeUpdate(query);
            if (queryResult == 1) {
                query = String.format("SELECT id, first_name, last_name, user_name FROM users WHERE user_name = '%s'", args.get(2));
                ResultSet rs = statement.executeQuery(query);
                rs.next();
                return new User(rs.getInt(1), rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            if(e.getSQLState() == "23000"){
                return new ErrorMessage("Username already exists, try again");
            }
        }
        return new ErrorMessage("Unable to signup, try again");
    }
}
