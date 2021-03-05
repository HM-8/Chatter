package backend.repository;

import backend.DatabaseConnectionHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;

public class UserRepository {
    private static final Connection connection = DatabaseConnectionHandler.getHandler().getConnection();

    public ResultSet selectSingleUser(String[] user) {
        String query = MessageFormat.format("SELECT count(1) FROM users WHERE user_name = ''{0}'' AND password = ''{1}''", user);
        try {
            Statement statement = connection.createStatement();
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
            Statement statement = connection.createStatement();
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
            Statement statement = connection.createStatement();
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
