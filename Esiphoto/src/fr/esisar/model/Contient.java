package fr.esisar.model;

public class Contient {
	private int num_ordre;
	private String title;
	private String commentaire;

	public Contient(int num_ordre, String title, String commentaire) {
		this.num_ordre = num_ordre;
		this.title = title;
		this.commentaire = commentaire;
	}

	public int getNumordre() {
		return num_ordre;
	}

	public String getTitle() {

		return title;
	}

	public String commentaire() {
		return commentaire;
	}

}
