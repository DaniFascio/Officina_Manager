package io.github.danifascio.dao;

import io.github.danifascio.DatabaseManager;
import io.github.danifascio.beans.Auto;
import io.github.danifascio.beans.TipoGomme;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AutoDao implements Dao<Auto> {

	private String errorMessage;

	public AutoDao() {
		errorMessage = "";
	}

	@Override
	public Auto get(Object key) {

		Auto auto = null;
		errorMessage = "";

		try(DatabaseManager dm = DatabaseManager.fromConfig(true)) {
			ResultSet rs = dm.executePreparedQuery(
					"SELECT targa, modello, km, misura_gomme, note, a.id_tipo_gomme, g.descrizione tipo_gomme " + "FROM auto a LEFT JOIN tipi_gomme ON a.id_tipo_gomme = g.id_tipo_gomme WHERE targa = ?",
					key);

			if(rs.next())
				auto = new Auto.Builder().setTarga(rs.getString("auto.details.targa"))
						.setModello(rs.getString("auto.details.modello"))
						.setKm(rs.getInt("km"))
						.setMisuraGomme(rs.getString("auto.details.gomme.misura"))
						.setNote(rs.getString("auto.details.note"))
						.setTipoGomme(TipoGomme.get("tipo_gomme"))
						.build();

		} catch(Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}

		return auto;
	}

	@Override
	public @NotNull List<Auto> getAll() {
		List<Auto> list = new LinkedList<>();
		errorMessage = "";

		try(DatabaseManager dm = DatabaseManager.fromConfig(true)) {
			ResultSet rs = dm.executeQuery(
					"SELECT targa, modello, km, misura_gomme, note, a.id_tipo_gomme, g.descrizione tipo_gomme FROM auto a LEFT JOIN tipi_gomme g on a.id_tipo_gomme = g.id_tipo_gomme ORDER BY targa ASC");

			while(rs.next())
				list.add(new Auto.Builder().setTarga(rs.getString("targa"))
						.setModello(rs.getString("modello"))
						.setKm(rs.getInt("km"))
						.setMisuraGomme(rs.getString("misura_gomme"))
						.setNote(rs.getString("note"))
						.setTipoGomme(TipoGomme.get(rs.getString("tipo_gomme")))
						.build());

		} catch(Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}

		return list;
	}

	@Override
	public @NotNull List<Auto> getAll(int page, int limit) {
		List<Auto> list = new LinkedList<>();
		errorMessage = "";

		// TODO: Select per tutte (con max)

		return list;
	}

	@Override
	public int save(Auto auto) {

		errorMessage = "";
		int res = 0;

		try(DatabaseManager dm = DatabaseManager.fromConfig(true)) {

			res = dm.executeUpdate("INSERT INTO auto (targa, modello, km, note, id_tipo_gomme, misura_gomme) VALUES (?, ?, ?, ?, ?, ?)",
					auto.getTarga(),
					auto.getModello(),
					auto.getKm(),
					auto.getNote(),
					auto.getTipoGomme().getId(),
					auto.getMisuraGomme());

		} catch(Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}

		return res;
	}

	@Override
	public int update(Auto auto, Object[] params) {

		errorMessage = "";
		int res = 0;

		try {

			DatabaseManager databaseManager = DatabaseManager.fromConfig(true);
			Object[] objects = new Object[params.length + 1];
			System.arraycopy(params, 0, objects, 0, params.length);
			objects[objects.length - 1] = auto.getTarga();
			res = databaseManager.executeUpdate(
					"UPDATE auto SET targa = ?, modello = ?, km = ?, note = ?, id_tipo_gomme = ?, misura_gomme = ? WHERE targa = ?",
					objects);

		} catch(SQLException e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}


		return res;
	}

	@Override
	public int delete(Auto auto) {

		errorMessage = "";
		int res = 0;

		try {
			DatabaseManager databaseManager = DatabaseManager.fromConfig(true);
			res = databaseManager.executeUpdate("DELETE FROM auto WHERE targa = ?", auto.getTarga());
		} catch(SQLException e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}

		return res;
	}

	public @NotNull String errorMessage() {
		return errorMessage;
	}

	public boolean error() {
		return errorMessage.length() != 0;
	}

}
