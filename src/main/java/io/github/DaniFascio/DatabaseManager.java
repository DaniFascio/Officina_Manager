package io.github.DaniFascio;

import org.intellij.lang.annotations.Language;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;

public class DatabaseManager implements Closeable {

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

	public ResultSet executeQuery(@Language("SQL") String query) throws SQLException {
		statement = connection.createStatement();
		resultSet = statement.executeQuery(query);
		return resultSet;
	}

	public ResultSet executePreparedQuery(@Language("SQL") String query, Object... params) throws SQLException {
		preparedStatement = connection.prepareStatement(query);
		for(int i = 1; i <= params.length; i++)
			preparedStatement.setObject(i, params[i - 1]);
		resultSet = preparedStatement.executeQuery();
		return resultSet;
	}

	public Integer executeUpdate(@Language("SQL") String query, Object... params) throws SQLException {
		preparedStatement = connection.prepareStatement(query);
		for(int i = 1; i <= params.length; i++)
			preparedStatement.setObject(i, params[i - 1]);
		int result = preparedStatement.executeUpdate();
		if(result > 0)
			resultSet = preparedStatement.getResultSet();
		return result;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	@Override
	public void close() throws IOException {
		try {
			connection.close();
		} catch(SQLException e) {
			throw new IOException(e);
		}
	}

}

