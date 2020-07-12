package io.github.DaniFascio;

import java.util.MissingResourceException;
import java.util.Objects;

public class Auto {

	private final String targa;
	private final String modello;
	private final Integer km;
	private final String misuraGomme;
	private final TipoGomme tipoGomme;
	private final String note;

	public Auto(String targa, String modello, Integer km, String misuraGomme, TipoGomme tipoGomme, String note) {
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

	public static class AutoBuilder {

		private String targa;
		private String modello;
		private String misuraGomme;
		private String note;
		private Integer km;
		private TipoGomme tipoGomme;

		public AutoBuilder() {

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
				throw new Exception(e);
			}

			return new Auto(targa, modello, km, misuraGomme, tipoGomme, note);
		}

		public AutoBuilder setTarga(String targa) {
			this.targa = targa;
			return this;
		}

		public AutoBuilder setModello(String modello) {
			this.modello = modello;
			return this;
		}

		public AutoBuilder setMisuraGomme(String misuraGomme) {
			this.misuraGomme = misuraGomme;
			return this;
		}

		public AutoBuilder setNote(String note) {
			this.note = note;
			return this;
		}

		public AutoBuilder setKm(Integer km) {
			this.km = km;
			return this;
		}

		public AutoBuilder setTipoGomme(TipoGomme tipoGomme) {
			this.tipoGomme = tipoGomme;
			return this;
		}

	}

}
