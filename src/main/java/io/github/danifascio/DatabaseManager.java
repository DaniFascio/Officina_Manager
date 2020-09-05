package io.github.danifascio;

import org.apache.commons.lang3.SystemUtils;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.Properties;

@SuppressWarnings("unused")
public class DatabaseManager implements AutoCloseable {

	private static final String filePath;
	private static final Properties errorCodes;
	private static final Properties dbProperties;
	private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

	static {
		dbProperties = new Properties();
		errorCodes = new Properties();

		String os = SystemUtils.OS_NAME.toLowerCase();
		if(os.contains("windows"))
			os = System.getenv("APPDATA");
		else
			os = System.getenv("HOME");

		filePath = os + "/OfficinaManager";

		try(InputStream input = new FileInputStream(filePath)) {

			dbProperties.load(input);

		} catch(FileNotFoundException ignored) {
		} catch(IOException e) {
			logger.error("Error while loading resources", e);
		}

		try(InputStream input = DatabaseManager.class.getResourceAsStream("/SQLErrorCodes.properties")) {

			errorCodes.load(input);

		} catch(Exception e) {
			logger.error("Error while loading resources", e);
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

		// TODO: default port 54321
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

			String username = dbProperties.getProperty("db.username");
			String password = dbProperties.getProperty("db.password");
			dbProperties.remove("db.username");
			dbProperties.remove("db.password");

			dbProperties.store(printWriter, null);

			if(username != null)
				dbProperties.put("db.username", username);
			if(password != null)
				dbProperties.put("db.password", password);

		} catch(IOException e) {
			logger.error("[Error Code -1]", e);

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

