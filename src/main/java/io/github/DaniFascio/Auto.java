package io.github.DaniFascio;

public class Auto {

    private String targa;
    private String modello;
    private Integer km;
    private String misuraGomme;
    private String note;
	private String tipoGomme;


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

	public String queryValues() {

		String qv = String.format("('%?','%?','%?', '%?','%?','%?',%?)", targa, modello, km , note, tipoGomme, misuraGomme);
		return qv;
	}

}
