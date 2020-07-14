package io.github.DaniFascio.gui.controllers;

import io.github.DaniFascio.Auto;

import java.util.Objects;

public class Lavorazione {

	private final Integer id;
	private final Float spesa;
	private final String descrizione;
	private final Auto auto;

	private Lavorazione(int id, float spesa, String descrizione, Auto auto) {
		this.id = id;
		this.descrizione = descrizione;
		this.spesa = spesa;
		this.auto = auto;
	}

	public Integer getId() {
		return id;
	}

	public Float getSpesa() {
		return spesa;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public Auto getAuto() {
		return auto;
	}

	public static class Builder {

		private Integer id;
		private Float spesa;
		private String descrizione;
		private Auto auto;

		public Builder() {

		}

		public Lavorazione build() throws Exception {
			try {
				Objects.requireNonNull(id);
				Objects.requireNonNull(spesa);
				Objects.requireNonNull(descrizione);
				Objects.requireNonNull(auto);
			} catch(NullPointerException e) {
				throw new Exception("Non-initialized argument in Lavorazione.Builder", e);
			}

			return new Lavorazione(id, spesa, descrizione, auto);
		}

		public Builder setAuto(Auto auto) {
			this.auto = auto;
			return this;
		}

		public Builder setDescrizione(String descrizione) {
			this.descrizione = descrizione;
			return this;
		}

		public Builder setId(Integer id) {
			this.id = id;
			return this;
		}

		public Builder setSpesa(Float spesa) {
			this.spesa = spesa;
			return this;
		}

	}

}
