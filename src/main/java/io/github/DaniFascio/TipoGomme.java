package io.github.DaniFascio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TipoGomme {

	private static Map<String, TipoGomme> tipi;

	static {
		tipi = new HashMap<>();
		tipi.put("Sconosciuto", new TipoGomme(0, "Sconosciuto"));
	}

	private final int id;
	private final String descrizione;

	private TipoGomme(int id, String descrizione) {
		this.id = id;
		this.descrizione = descrizione;
	}

	public static void reload() throws SQLException {

		DatabaseManager dbm = DatabaseManager.fromConfig(true);
		ResultSet rs = dbm.executeQuery("SELECT id_tipo_gomme id, descrizione FROM tipi_gomme");

		if(rs.next()) {
			tipi = new HashMap<>();

			do {
				int id = rs.getInt("id");
				String descrizione = rs.getString("descrizione");
				tipi.put(descrizione, new TipoGomme(id, descrizione));
			} while(rs.next());
		}
	}

	public static TipoGomme get(String descrizione) {
		TipoGomme tipo = tipi.get(descrizione);
		return (tipo != null) ? tipo : new TipoGomme(0, "Sconosciuto");
	}

	public static boolean contains(String descrizione) {
		return tipi.containsKey(descrizione);
	}

	public static void add(String descrizione) throws SQLException {

		DatabaseManager dbm = DatabaseManager.fromConfig(true);
		dbm.executeUpdate("INSERT INTO tipi_gomme (descrizione) VALUES (?)", descrizione);

	}

	public static Set<Map.Entry<String, TipoGomme>> entrySet() {
		return tipi.entrySet();
	}

	public static Collection<TipoGomme> values() {
		return tipi.values();
	}

	public int getId() {
		return id;
	}

	public String getDescrizione() {
		return descrizione;
	}

}