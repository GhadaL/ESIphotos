package fr.esisar.model;

public class LigneCommande {
	private Format format;
	private Album album;
	private int quantite;

	public LigneCommande(Format format, Album album, int quantite) {
		this.format = format;
		this.album = album;
		this.quantite = quantite;
	}

	public Format getFormat() {
		return this.format;
	}

	public Album getAlbum() {
		return this.album;
	}

	public int getQuantite() {
		return quantite;
	}
}
