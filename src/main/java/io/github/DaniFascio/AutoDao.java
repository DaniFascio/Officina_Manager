package io.github.DaniFascio;

import java.sql.ResultSet;
import java.util.List;

public class AutoDao implements Dao<Auto> {

	@Override
	public Auto get(Object key) {

		Auto auto = null;

		try {
			DatabaseManager dm = new DatabaseManager("jdbc:postgresql://gergegrege/db_officina", "danifascio", "fdm3006", true);

			ResultSet rs = dm.executePreparedQuery("SELECT targa, modello, km, misuraGomme, note FROM auto WHERE targa = ?", key);
			if(rs.next())
				// TODO: Cambiare nomi "colonne"
				auto = new Auto(rs.getString("targa"), rs.getString("modello"), null, null, null, null);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return auto;
	}

	@Override
	public List<Auto> getAll(int page, int limit) {

		// TODO: Select per tutte

		return null;
	}

	@Override
	public void save(Auto auto) {

		// TODO: INSERT Auto

	try {
		DatabaseManager dbman = new DatabaseManager("jdbc:postgresql://gergegrege/db_officina", "danifascio", "fdm3006", true);

		ResultSet rs = dbman.executePreparedQuery(" SELECT id_tipo_gomme from tipi_gomme WHERE descrizione = ? ");

	}
	catch(Exception e){
		e.printStackTrace();


	}

	}

	@Override
	public void update(Auto auto, Object[] params) {

		// TODO: UPDATE Auto

	}

	@Override
	public void delete(Auto auto) {

		// TODO: DELETE Auto

	}

}
