package fr.esisar.model;

public class Album {
	protected int idAlbum;
	protected String titre;
	protected String sousTitre;

	public Album(String titre, String sousTitre) {
		this.idAlbum = 0;
		this.titre = titre;
		this.sousTitre = sousTitre;
	}

	public int getIdAlbum() {
		return idAlbum;
	}

	public String getTitre() {
		return titre;
	}

	public String getSousTitre() {
		return sousTitre;
	}
}
