package io.github.DaniFascio;

import java.sql.*;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DatabaseManager {

    private static final String[] arguments = { "DB_HOST", "DB_USER", "DB_PASS" };

    private final Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public DatabaseManager(String url, String user, String pass, boolean autocommit) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        statement = null;
        preparedStatement = null;
        resultSet = null;
        connection = DriverManager.getConnection(url, user, pass);
        connection.setAutoCommit(autocommit);
    }

    public void commit() throws SQLException {
        if(connection != null)
            connection.commit();
    }

    public void rollback() throws SQLException {
        if(connection != null)
            connection.rollback();
    }

    public ResultSet executeQuery(String query) throws SQLException {
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        return resultSet;
    }

    public Integer executeUpdate(String query, Object... objects) throws SQLException {
        preparedStatement = connection.prepareStatement(query);
        for(int i = 1; i <= objects.length; i++)
            preparedStatement.setObject(i, objects[i - 1]);
        int result = preparedStatement.executeUpdate();
        if(result > 0)
            resultSet = preparedStatement.getResultSet();
        return result;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

}

