package io.github.DaniFascio;

import org.intellij.lang.annotations.Language;

public class Auto {

    private String targa;
    private String modello;
    private Integer km;
    private String misuraGomme;
    private String note;
	private String tipo_gomme;


	public Auto(String targa, String modello, Integer km, String misuraGomme, String note, String tipo_gomme) {
		this.targa = targa;
		this.modello = modello;
		this.km = km;
		this.misuraGomme = misuraGomme;
		this.note = note;
		this.tipo_gomme = tipo_gomme;
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


	public String queryValues() {

		String qv = String.format("('%s','%s','%d', '%s','%d','%s',%d)", targa, modello, km , note, tipo_gomme , misuraGomme,);
		return qv;
	}

}
