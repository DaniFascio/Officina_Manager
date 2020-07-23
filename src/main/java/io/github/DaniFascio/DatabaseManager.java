package io.github.DaniFascio;

import javafx.scene.Scene;
import org.intellij.lang.annotations.Language;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.MissingResourceException;
import java.util.Properties;

public class DatabaseManager implements AutoCloseable {

	private static final Properties properties;

	static {
		properties = new Properties();

		try(InputStream input = DatabaseManager.class.getClassLoader()
				.getResourceAsStream("conn.properties")) {

			if(input == null)
				throw new MissingResourceException("Cannot load db configs from default properties file", DatabaseManager.class
						.getCanonicalName(), "conn.properties");

			properties.load(input);

		} catch(MissingResourceException e) {
			properties.setProperty("db.host", "localhost");
			properties.setProperty("db.port", "5432");
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private final Connection connection;
	private Statement statement;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	public static DatabaseManager fromConfig(boolean autoCommit) throws SQLException {
		String dbhost, dbport, dbuser, dbpass;

		dbhost = properties.getProperty("db.host");
		dbport = properties.getProperty("db.port");
		dbuser = properties.getProperty("db.username");
		dbpass = properties.getProperty("db.password");

		return new DatabaseManager("jdbc:postgresql://" + dbhost + ":" + dbport + "/db_officina", dbuser, dbpass, autoCommit);
	}

	public DatabaseManager(String url, String user, String pass, boolean autoCommit) throws SQLException {

		try {
			Class.forName("org.postgresql.Driver");
		} catch(ClassNotFoundException e) {
			throw new SQLException(e);
		}

		resultSet = null;
		statement = null;
		preparedStatement = null;
		connection = DriverManager.getConnection(url, user, pass);
		connection.setAutoCommit(autoCommit);
	}

	public static void setHostname(String hostname) {
		properties.setProperty("db.host", hostname);
	}

	public static void setPort(String port) {
		properties.setProperty("db.port", port);
	}

	public static void setUsername(String username) {
		properties.setProperty("db.username", username);
	}

	public static void setPassword(String password) {
		properties.setProperty("db.password", password);
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
		// TODO: SQL TYPE IN OBJECT
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

	public boolean isClosed() throws SQLException {
		return connection.isClosed();
	}

	public static String getUsername() {
		return properties.getProperty("db.username");
	}

}

