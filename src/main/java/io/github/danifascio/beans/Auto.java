package io.github.danifascio.beans;

import java.util.Objects;

public class Auto {

	private final String targa;
	private final String modello;
	private final Integer km;
	private final String misuraGomme;
	private final TipoGomme tipoGomme;
	private final String note;

	private Auto(String targa, String modello, Integer km, String misuraGomme, TipoGomme tipoGomme, String note) {
		this.targa = targa;
		this.modello = modello;
		this.km = km;
		this.misuraGomme = misuraGomme;
		this.tipoGomme = tipoGomme;
		this.note = note;
	}

	public String getTarga() {
		return targa;
	}

	public String getModello() {
		return modello;
	}

	public Integer getKm() {
		return km;
	}

	public String getMisuraGomme() {
		return misuraGomme;
	}

	public TipoGomme getTipoGomme() {
		return tipoGomme;
	}

	public String getNote() {
		return note;
	}

	// num_targa, modello, km, note, id_tipo_gomme, misura_gomme
	public Object[] values() {

		return new Object[] {
				targa, modello, km, note, tipoGomme.getId(), misuraGomme
		};

	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Auto)
			return ((Auto) obj).targa.equals(targa) && ((Auto) obj).modello.equals(modello) && ((Auto) obj).km
					.equals(km) && ((Auto) obj).misuraGomme.equals(misuraGomme) && ((Auto) obj).tipoGomme
					.equals(tipoGomme) && ((Auto) obj).note.equals(note);
		return false;
	}

	public static class Builder {

		private String targa;
		private String modello;
		private String misuraGomme;
		private String note;
		private Integer km;
		private TipoGomme tipoGomme;

		public Builder() {

		}

		public Auto build() throws Exception {
			try {
				Objects.requireNonNull(targa);
				Objects.requireNonNull(modello);
				Objects.requireNonNull(misuraGomme);
				Objects.requireNonNull(note);
				Objects.requireNonNull(km);
				Objects.requireNonNull(tipoGomme);
			} catch(NullPointerException e) {
				throw new Exception("Non-initialized argument in Auto.Builder", e);
			}

			return new Auto(targa, modello, km, misuraGomme, tipoGomme, note);
		}

		public Builder setTarga(String targa) {
			this.targa = targa;
			return this;
		}

		public Builder setModello(String modello) {
			this.modello = modello;
			return this;
		}

		public Builder setMisuraGomme(String misuraGomme) {
			this.misuraGomme = misuraGomme;
			return this;
		}

		public Builder setNote(String note) {
			this.note = note;
			return this;
		}

		public Builder setKm(Integer km) {
			this.km = km;
			return this;
		}

		public Builder setTipoGomme(TipoGomme tipoGomme) {
			this.tipoGomme = tipoGomme;
			return this;
		}

	}

}
