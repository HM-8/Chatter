package backend.routes;

import backend.DatabaseConnectionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

public class UserRoutes {
    private static Statement statement;

    static {
        try {
            statement = DatabaseConnectionHandler.getHandler().getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet selectSingleUser(String[] user) {
        String query = MessageFormat.format("SELECT count(1) FROM users WHERE user_name = ''{0}'' AND password = ''{1}''", user);
        try {
            ResultSet queryResult = statement.executeQuery(query);
            return queryResult;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertSingleUser(String[] user) {
        String query = MessageFormat.format("INSERT INTO users (first_name, last_name, user_name, password) Values (''{0}'', ''{1}'', ''{2}'', ''{3}'')", user);

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

    public boolean ifUserNameExists(String userName) {
        String query = MessageFormat.format("SELECT count(1) FROM users WHERE user_name = ''{0}''", userName);

        try {
            ResultSet queryResult = statement.executeQuery(query);

            if(queryResult.getInt(1) == 1)
            {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
