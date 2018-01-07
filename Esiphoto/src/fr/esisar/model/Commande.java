package fr.esisar.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import fr.esisar.controller.CommandeDAO;

import java.text.SimpleDateFormat;

public class Commande {

	private int idCommande;
	private String date;
	private String prixTotal;

	public Commande() {
		SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yy");
		Date aujourdhui = new Date();
		this.date = formater.format(aujourdhui);
		this.prixTotal = "0"; // temporairement
	}

	public int getIdCommande() {
		return idCommande;
	}

	public void setIdCommande(int idCommande) {
		this.idCommande = idCommande;
	}

	public String getDate() {
		return date;
	}

	public String getPrixTotal() {
		return prixTotal;
	}

	public void setPrixTotal(String prixTotal) {
		this.prixTotal = prixTotal;
	}

}
