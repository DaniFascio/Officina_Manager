package io.github.DaniFascio;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        DatabaseConnection dbconn = new DatabaseConnection();
        dbconn.connect();
        dbconn.SelectAuto();
        dbconn.SelectTipiLavorazioni();

        AutoDao autoDao = new AutoDao();





    }
}