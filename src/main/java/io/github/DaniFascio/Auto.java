package io.github.DaniFascio;

public class Auto {

    private String targa;
    private String modello;
    private Integer km;
    private String misuraGomme;
    private String note;

	public Auto(String targa, String modello, Integer km, String misuraGomme, String note) {
		this.targa = targa;
		this.modello = modello;
		this.km = km;
		this.misuraGomme = misuraGomme;
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

	public String getNote() {
		return note;
	}

}
