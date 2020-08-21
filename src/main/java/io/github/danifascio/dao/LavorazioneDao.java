package io.github.danifascio.dao;

import io.github.danifascio.DatabaseManager;
import io.github.danifascio.beans.Auto;
import io.github.danifascio.beans.Lavorazione;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class LavorazioneDao implements Dao<Lavorazione> {

	private final Auto auto;
	private String errorMessage;
	private static Logger logger = LoggerFactory.getLogger(LavorazioneDao.class);


	public LavorazioneDao(Auto auto) {
		this.auto = auto;
		errorMessage = "";
	}

	@Override
	public @Nullable Lavorazione get(Object key) {

		Lavorazione lavorazione = null;
		errorMessage = "";

		try(DatabaseManager dbm = DatabaseManager.fromConfig(true)) {
			String targa = auto.getTarga();
			ResultSet rs = dbm.executePreparedQuery(
					"SELECT id_tipo_lavorazione id, descrizione, spesa, data FROM lavorazioni WHERE id_tipo_lavorazione = ? AND targa_auto = ?",
					key,
					targa);

			if(rs.next())
				lavorazione = new Lavorazione.Builder().setAuto(auto)
						.setDescrizione(rs.getString("descrizione"))
						.setId(rs.getInt("id"))
						.setSpesa(rs.getFloat("spesa"))
						.setData(rs.getDate("data").toLocalDate())
						.build();

		}catch(SQLException e){
			logger.error("Error during LavorazioneDao.get - Error Code " + e.getSQLState(), e);
		} catch(IOException e) {
			logger.error("Error during LavorazioneDao.get",e);
		}

		return lavorazione;
	}

	@Override
	public @NotNull List<Lavorazione> getAll() {

		List<Lavorazione> list = new LinkedList<>();
		errorMessage = "";

		try(DatabaseManager dbm = DatabaseManager.fromConfig(true)) {

			String targa = auto.getTarga();
			ResultSet rs = dbm.executePreparedQuery(
					"SELECT id_tipo_lavorazione id, descrizione, spesa, data FROM lavorazioni WHERE targa_auto = ?",
					targa);

			while(rs.next())
				list.add(new Lavorazione.Builder().setAuto(auto)
						.setDescrizione(rs.getString("descrizione"))
						.setId(rs.getInt("id"))
						.setSpesa(rs.getFloat("spesa"))
						.setData(rs.getDate("data").toLocalDate())
						.build());

		}catch(SQLException e){
			logger.error("Error during LavorazioneDao.getAll - Error Code " + e.getSQLState(), e);
		} catch(IOException e) {
			logger.error("Error during LavorazioneDao.getAll",e);
		}

		return list;
	}

	@SuppressWarnings("UnnecessaryLocalVariable")
	@Override
	public @NotNull List<Lavorazione> getAll(int page, int limit) {
		List<Lavorazione> list = new LinkedList<>();

		// TODO: LavorazioneDao::getAll(page, limit)

		return list;
	}

	@Override
	public int save(Lavorazione lavorazione) {

		errorMessage = "";
		int res = 0;

		try(DatabaseManager dm = DatabaseManager.fromConfig(true)) {

			res = dm.executeUpdate("INSERT INTO lavorazioni (descrizione, spesa, data, targa_auto) VALUES (?, ?, ?, ?)",
					lavorazione.getDescrizione(),
					lavorazione.getSpesa(),
					lavorazione.getData(),
					auto.getTarga());

		}catch(SQLException e) {
			logger.error("Error during LavorazioneDao.save - Error Code " + e.getSQLState(), e);
		}catch(IOException e) {
			logger.error("Error during LavorazioneDao.save",e);
		}

		return res;
	}

	/**
	 * @param lavorazione Lavorazione da modificare
	 * @param params      Parametri da modificare. In ordine: descrizione, spesa, data
	 * @return 1 se modificato, altrimenti 0
	 */
	@Override
	public int update(Lavorazione lavorazione, Object[] params) {

		errorMessage = "";
		int res = 0;

		try(DatabaseManager dbm = DatabaseManager.fromConfig(true)) {

			int id = lavorazione.getId();
			res = dbm.executeUpdate(
					"UPDATE lavorazioni SET descrizione = ?, spesa = ?, data = ?, targa_auto = ? WHERE id_tipo_lavorazione = ?",
					params[0],
					params[1],
					params[2],
					params[3],
					id);

		} catch(SQLException e) {
			logger.error("Error during LavorazioneDao.update - Error Code " + e.getSQLState(), e);
		} catch(IOException e) {
			logger.error("Error during LavorazioneDao.update", e);
		}

		return res;
	}

	@Override
	public int delete(Lavorazione lavorazione) {

		errorMessage = "";
		int res = 0;

		try {

			DatabaseManager databaseManager = DatabaseManager.fromConfig(true);
			res = databaseManager.executeUpdate("DELETE FROM lavorazioni WHERE id_tipo_lavorazione = ?", lavorazione.getId());

		} catch(SQLException e) {
			logger.error("Error during LavorazioneDao.delete - Error Code " + e.getSQLState(), e);
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
