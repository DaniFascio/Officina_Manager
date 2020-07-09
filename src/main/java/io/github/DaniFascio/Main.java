package io.github.DaniFascio;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        DatabaseConnection dbconn = new DatabaseConnection();
        dbconn.connect();
        dbconn.SelectAuto();
        dbconn.SelectTipiLavorazioni();

        AutoDao ad = new AutoDao();
        Auto a = new Auto("12sdf12", "ford", 2300 , "200x300", "sos", "sos");
        ad.save(a);


        AutoDao autoDao = new AutoDao();





    }
}