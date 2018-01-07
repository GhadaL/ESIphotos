package fr.esisar.model;

public class Adresse {
	protected int idAdresse;
	private String codePostal;
	private String rue, Ville;

	public Adresse(int idAdresse, String rue, String codePostal, String Ville) {
		this.idAdresse = idAdresse;
		this.rue = rue;
		this.codePostal = codePostal;
		this.Ville = Ville;
	}

	public int getIdAdresse() {
		return idAdresse;
	}

	public String getRue() {
		return rue;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public String getVille() {
		return Ville;
	}
}
