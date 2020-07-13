package io.github.DaniFascio;

import io.github.DaniFascio.gui.controllers.Lavorazione;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class LavorazioneDao implements Dao<Lavorazione> {

	private final Auto auto;

	public LavorazioneDao(Auto auto) {
		this.auto = auto;
	}

	@Nullable
	@Override
	public Lavorazione get(Object key) {

		Lavorazione lavorazione = null;

		try {

			String targa = auto.getTarga();
			DatabaseManager dbm = DatabaseManager.fromConfig(true);
			ResultSet rs = dbm.executePreparedQuery("SELECT id_tipo_lavorazione id, descrizione, spesa FROM tipi_lavorazioni WHERE id_tipo_lavorazione = ? AND targa_auto = ?", key, targa);

			if(rs.next())
				lavorazione = new Lavorazione.Builder().setAuto(auto)
						.setDescrizione(rs.getString("descrizione"))
						.setId(rs.getInt("id"))
						.setSpesa(rs.getFloat("spesa"))
						.build();

		} catch(Exception e) {
			e.printStackTrace();
		}

		return lavorazione;
	}

	@Override
	public @NotNull List<Lavorazione> getAll() {
		List<Lavorazione> list = new LinkedList<>();

		// TODO: LavorazioneDao::getAll()

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
		int result = 0;

		// TODO: LavorazioneDao.save(lavorazione)

		return result;
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

}
