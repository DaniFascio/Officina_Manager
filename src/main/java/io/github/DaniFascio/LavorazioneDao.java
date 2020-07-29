package io.github.DaniFascio;

import io.github.DaniFascio.gui.controllers.Lavorazione;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class LavorazioneDao implements Dao<Lavorazione> {

	private final Auto auto;
	private String errorMessage;

	public LavorazioneDao(Auto auto) {
		this.auto = auto;
		errorMessage = "";
	}

	@Nullable
	@Override
	public Lavorazione get(Object key) {

		Lavorazione lavorazione = null;
		errorMessage = "";

		try {

			String targa = auto.getTarga();
			DatabaseManager dbm = DatabaseManager.fromConfig(true);
			ResultSet rs = dbm.executePreparedQuery("SELECT id_tipo_lavorazione id, descrizione, spesa FROM lavorazioni WHERE id_tipo_lavorazione = ? AND targa_auto = ?", key, targa);

			if(rs.next())
				lavorazione = new Lavorazione.Builder().setAuto(auto)
						.setDescrizione(rs.getString("descrizione"))
						.setId(rs.getInt("id"))
						.setSpesa(rs.getFloat("spesa"))
						.build();

		} catch(Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}

		return lavorazione;
	}

	@Override
	public @NotNull List<Lavorazione> getAll() {
		List<Lavorazione> list = new LinkedList<>();

		errorMessage = "";

		try(DatabaseManager dm = DatabaseManager.fromConfig(true)) {
			ResultSet rs = dm.executePreparedQuery("SELECT id_tipo_lavorazione id, descrizione, spesa FROM lavorazioni WHERE id_tipo_lavorazione = ? AND targa_auto = ?", key, targa);

			while(rs.next())
				list.add(new Lavorazione.Builder().setAuto(auto)
						.setDescrizione(rs.getString("descrizione"))
						.setId(rs.getInt("id"))
						.setSpesa(rs.getFloat("spesa"))
						.build());

		} catch(Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}

		return list;
	}

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

			res = dm.executeUpdate("INSERT INTO lavorazioni () VALUES (?, ?, ?, ?)", lavorazione
					.getId(), lavorazione.getDescrizione(), lavorazione.getSpesa(), lavorazione.getNote());

		} catch(Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}

		return res;
	}

	@Override
	public int update(Lavorazione lavorazione, Object[] params) {
		int result = 0;

		// TODO: LavorazioneDao.update(lavorazione, args...)

		return result;
	}

	@Override
	public int delete(Lavorazione lavorazione) {
		int result = 0;

		// TODO: LavorazioneDao.delete(lavorazione)

		return result;
	}

	public @NotNull String errorMessage() {
		return errorMessage;
	}

	public boolean error() {
		return errorMessage.length() != 0;
	}

}
