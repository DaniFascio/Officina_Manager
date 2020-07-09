package io.github.DaniFascio;

public class Auto {

	private final String targa;
	private final String modello;
	private final Integer km;
	private final String misuraGomme;
	private final String tipoGomme;
	private final String note;

	public Auto(String targa, String modello, Integer km, String misuraGomme, String note, String tipoGomme) {
		this.targa = targa;
		this.modello = modello;
		this.km = km;
		this.misuraGomme = misuraGomme;
		this.note = note;
		this.tipoGomme = tipoGomme;
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

	public String getNote() {
		return note;
	}

	public String getTipoGomme() {
		return tipoGomme;
	}



}
