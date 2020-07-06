package io.github.DaniFascio;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static final String URL = "jdbc:postgresql://localhost:5432/db_officina";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "fdm3006";


    private Connection conn;

    public void connect() {

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            if (conn != null)
                System.out.println("connected to the database");
            else
                System.out.println("failed to make connection");

        }

        catch (SQLException e) {
            System.err.format("SQL state: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
