package io.github.DaniFascio;

import java.sql.*;

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




    public void SelectAuto() throws SQLException {

        ResultSet rs;
        Statement stmt = conn.createStatement();

        rs = stmt.executeQuery("select * from auto");

        while(rs.next()) {
            System.out.println(rs.getString("num_targa") + " - " + rs.getString("modello") + " - " + rs.getString("km") + " - " + rs.getString("note"));
        }
    }


    public void SelectTipiLavorazioni() throws SQLException {

        ResultSet rs;
        Statement stmt = conn.createStatement();

        rs = stmt.executeQuery("select * from tipi_lavorazioni");

        while(rs.next()) {
            System.out.println(rs.getString("id_tipo_lavorazione") + " - " + rs.getString("descrizione") + " - " + rs.getString("spesa"));
        }
    }

/*
    public void SelectAutoAndLavorazioni() throws SQLException {

        ResultSet rs;
        Statement stmt = conn.createStatement();

        rs = stmt.executeQuery("select * from auto WHERE  = id_tipo_lavorazione");

        while(rs.next()) {
            System.out.println(rs.getString("num_targa") + " - " + rs.getString("modello") + " - " + rs.getString("km") + " - " + rs.getString("note"));
        }
    }
}
*/
}