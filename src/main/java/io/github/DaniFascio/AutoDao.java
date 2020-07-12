package io.github.DaniFascio;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AutoDao implements Dao<Auto> {

	@Override
	public Auto get(Object key) {

		Auto auto = null;

		try(DatabaseManager dm = DatabaseManager.fromConfig(true)) {
			ResultSet rs = dm.executePreparedQuery("SELECT targa, modello, km, misuraGomme, note FROM auto WHERE targa = ?", key);

			if(rs.next())
				auto = new Auto(rs.getString("targa"), rs.getString("modello"), null, null, null, null);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return auto;
	}

	@Override
	public @NotNull List<Auto> getAll() {
		List<Auto> list = new LinkedList<>();


		try(DatabaseManager dm = DatabaseManager.fromConfig(true)) {
			ResultSet rs = dm.executeQuery("SELECT targa, modello, km, misura_gomme, note, g.descrizione tipo_gomme FROM auto a LEFT JOIN tipi_gomme g on a.id_tipo_gomme = g.id_tipo_gomme");

			while(rs.next())
				list.add(new Auto(rs.getString("targa"), rs.getString("modello"), rs
						.getInt("km"), rs.getString("misura_gomme"), rs.getString("note"), rs
						.getString("tipo_gomme")rs
						.getInt("tipo_gomme")));

		} catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public @NotNull List<Auto> getAll(int page, int limit) {
		List<Auto> list = new LinkedList<>();

		// TODO: Select per tutte (con max)

		return list;
	}

	@Override
	public int save(Auto auto) {

		int res = 0;

		try(DatabaseManager dm = DatabaseManager.fromConfig(true)) {

			ResultSet rs = dm.executePreparedQuery("SELECT id_tipo_gomme from tipi_gomme WHERE descrizione = ?", auto
					.getTipoGomme());

			if(rs.next())
				res = dm.executeUpdate("INSERT INTO auto (targa, modello, km, note, id_tipo_gomme, misura_gomme) VALUES (?, ?, ?, ?, ?, ?)", auto
						.getTarga(), auto.getModello(), auto.getKm(), auto.getNote(), rs
						.getInt("id_tipo_gomme"), auto.getMisuraGomme());

		} catch(Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	@Override
	public int update(Auto auto, Object[] params) {

		// TODO: UPDATE Auto

		try {
			DatabaseManager databaseManager = DatabaseManager.fromConfig(true);
			databaseManager.executeUpdate("UPDATE TABLE auto (num_targa,modello,km,note,id_tipo_gomme,misura_gomme) VALUES ()")
		}
		catch(SQLException throwables){
			throwables.printStackTrace();
		}


		return 0;
	}

	@Override
	public int delete(Auto auto) {

		// TODO: DELETE Auto

		return 0;
	}

}
