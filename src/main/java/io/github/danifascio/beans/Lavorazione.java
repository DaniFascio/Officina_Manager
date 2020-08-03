package io.github.danifascio.beans;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Objects;

public class Lavorazione {

	private final Integer id;
	private final Float spesa;
	private final LocalDate data;
	private final String descrizione;
	private final Auto auto;

	private Lavorazione(int id, float spesa, LocalDate data, String descrizione, Auto auto) {
		this.id = id;
		this.data = data;
		this.descrizione = descrizione;
		this.spesa = spesa;
		this.auto = Objects.requireNonNull(auto);
	}

	public Integer getId() {
		return id;
	}

	public Float getSpesa() {
		return spesa;
	}

	public LocalDate getData() {
		return data;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public @NotNull Auto getAuto() {
		return auto;
	}

	// descrizione, spesa, data, targa_auto
	public Object[] values() {
		return new Object[] {
				descrizione, spesa, data, auto.getTarga()
		};
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Lavorazione)
			return ((Lavorazione) obj).id.equals(id) && ((Lavorazione) obj).spesa
					.equals(spesa) && ((Lavorazione) obj).descrizione.equals(descrizione) && ((Lavorazione) obj).auto
					.equals(auto);
		return false;
	}

	public static class Builder {

		private Integer id;
		private Float spesa;
		private String descrizione;
		private Auto auto;
		private LocalDate data;

		public Builder() {

		}

		public Lavorazione build() throws Exception {
			try {
				Objects.requireNonNull(spesa);
				Objects.requireNonNull(descrizione);
				Objects.requireNonNull(auto);
			} catch(NullPointerException e) {
				throw new Exception("Non-initialized argument in Lavorazione.Builder", e);
			}

			return new Lavorazione(id != null ? id : -1, spesa, data, descrizione, auto);
		}

		public Builder setAuto(Auto auto) {
			this.auto = auto;
			return this;
		}

		public Builder setData(LocalDate data) {
			this.data = data;
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
