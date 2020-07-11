package io.github.DaniFascio;

public class Main {

	public static void main(String[] args) {
		DatabaseManager.setUsername("user");
		DatabaseManager.setPassword("pass");

		try(DatabaseManager dbm = DatabaseManager.fromConfig(true)) {

			System.out.println(dbm.isClosed());
			dbm.close();
			System.out.println(dbm.isClosed());

		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}