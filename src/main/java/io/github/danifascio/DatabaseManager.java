package io.github.danifascio;

import org.intellij.lang.annotations.Language;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
public class DatabaseManager implements AutoCloseable {

	private static final String filePath;
	private static final Properties errorCodes;
	private static final Properties dbProperties;

	static {
		dbProperties = new Properties();
		errorCodes = new Properties();
		filePath = System.getProperty("user.home") + "\\AppData\\Roaming\\db_officina\\conn.properties";

		try(InputStream dbInput = new FileInputStream(filePath); InputStream ecInput = DatabaseManager.class.getResourceAsStream(
				"/SQLErrorCodes.properties")) {

			errorCodes.load(ecInput);
			dbProperties.load(dbInput);

		} catch(Exception e) {
			LoggerFactory.getLogger(DatabaseManager.class).error("Error while loading resources", e);
		}
	}

	private final Connection connection;
	private Statement statement;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	public static String errorCodeResponse(String errorCode) {
		return errorCodes.getProperty(errorCode, Gui.lang().getString("unknown_error") + " - Error Code " + errorCode);
	}

	public static DatabaseManager fromConfig(boolean autoCommit) throws SQLException {
		String dbhost, dbport, dbuser, dbpass;

		dbhost = dbProperties.getProperty("db.host");
		dbport = dbProperties.getProperty("db.port");
		dbuser = dbProperties.getProperty("db.username");
		dbpass = dbProperties.getProperty("db.password");

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

	public boolean isClosed() throws SQLException {
		return connection.isClosed();
	}

	public static void save() {
		try(PrintWriter printWriter = new PrintWriter(filePath)) {
			dbProperties.store(printWriter, "");
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getUsername() {
		String str = dbProperties.getProperty("db.username");
		return str != null ? str : "";
	}

	public static String getHostname() {
		String str = dbProperties.getProperty("db.host");
		return str != null ? str : "";
	}

	public static String getPort() {
		String str = dbProperties.getProperty("db.port");
		return str != null ? str : "";
	}

	public static void setHostname(String hostname) {
		dbProperties.setProperty("db.host", hostname);
	}

	public static void setPort(String port) {
		dbProperties.setProperty("db.port", port);
	}

	public static void setUsername(String username) {
		dbProperties.setProperty("db.username", username);
	}

	public static void setPassword(String password) {
		dbProperties.setProperty("db.password", password);
	}

}

