package fr.esisar.controller;

import fr.esisar.model.Commande;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CommandeDAO extends Commande {

	public CommandeDAO() {
		super();
	}

	public int addCommande(Connection connect, String adresseMail, int idLigneCommande)
			throws ClassNotFoundException, SQLException {
		Statement statement;
		int idCommande;
		boolean res = true;
		ResultSet resu = null;
		statement = connect.createStatement();
		resu = statement.executeQuery("SELECT MAX(idCommande) FROM Commande");
		resu.next();
		if (resu.getString(1) == null) {
			idCommande = 0;
		} else {
			idCommande = resu.getInt(1) + 1;
		}
		if (res == true) {
			String quer1 = "INSERT INTO Commande (idCommande, dateCommande, prixTotal, adresseMail, idLigneCommande) ";
			String quer2 = "VALUES ('" + idCommande + "', '" + super.getDate() + "', '" + super.getPrixTotal() + "', '"
					+ adresseMail + "', '" + idLigneCommande + "')";
			resu = statement.executeQuery(quer1 + quer2);
		}
		return idCommande;
	}

	public static int ValiderCommande(File fichier, Connection connect, int idCommande, String adressMail)
			throws SQLException, IOException {
		Statement statement;
		boolean res = false;
		ResultSet result = null;
		statement = connect.createStatement();
		String sentence = "SELECT idCommande FROM Commande WHERE adresseMail = '" + adressMail + "'";
		result = statement.executeQuery(sentence);
		LigneCommandeDAO lComm = new LigneCommandeDAO();
		int prix = lComm.PrixCommande(fichier, connect, idCommande, adressMail);

		String quer1 = "UPDATE Commande SET prixTotal ='" + prix + "' WHERE idCommande='" + idCommande + "'";
		result = statement.executeQuery(quer1);

		// Ecriture fichier log
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fichier, true));
		bufferedWriter.write("Prix de la commande : " + idCommande + " est de : " + prix + "\n");
		bufferedWriter.close();
		return prix;
	}

	public static boolean VerifIdCommande(Connection connect, int idCommande, String email) throws SQLException {
		Statement statement;
		boolean res = false;
		ResultSet result = null;
		statement = connect.createStatement();
		String sentence = "SELECT idCommande,prixTotal FROM Commande WHERE adresseMail = '" + email + "'";
		result = statement.executeQuery(sentence);
		while (result.next()) {
			if (idCommande == result.getInt("idCommande")) {
				if (Integer.parseInt(result.getString("prixTotal")) == 0) {
					res = true;
				}
			}
		}
		return res;
	}

	public static boolean VerifClient(Connection connect, String email, int idCommande) throws SQLException {
		Statement statement;
		boolean res = false;
		ResultSet result = null;
		statement = connect.createStatement();
		String sentence = "SELECT idCommande FROM Commande WHERE adresseMail = '" + email + "'";
		result = statement.executeQuery(sentence);
		while (result.next()) {
			if (idCommande == result.getInt("idCommande")) {
				res = true;
			}
		}
		return res;
	}

	public static boolean SupprCommande(Connection connect, String email, int idCommande) throws SQLException {
		boolean res = false;
		Statement st;
		try {
			st = connect.createStatement();
			// insert the data
			st.executeUpdate("DELETE FROM Commande WHERE idCommande = '" + idCommande + "' " + " AND adresseMail = '"
					+ email + "'");
			System.out.println("Commande d'id = " + idCommande + " supprime !");
			res = true;
		} catch (SQLException e) {
			System.out.println("erreur dans la suppresion de l'album");
		}
		return res;
	}

	public static boolean AffListCommande(Connection connect, String adressMail) throws SQLException {
		Statement statement;
		boolean res = false;
		String etat = "en cours";
		ResultSet result = null;
		statement = connect.createStatement();
		String sentence = "SELECT adresseMail,prixTotal,idCommande FROM Commande";
		result = statement.executeQuery(sentence);
		while (result.next()) {
			if (adressMail.equals(result.getString("adresseMail"))) {
				if (Integer.parseInt(result.getString("prixTotal")) == 0) {
					etat = "en cours";
				} else {
					etat = "terminee";
				}
				System.out.println("Commande " + result.getInt("idCommande") + " est dans l'etat " + etat + "\n");
			}
		}
		return res;
	}

	public static int getidCommande(Connection connect) throws SQLException {
		int idCommande = -1;

		Statement statement;
		ResultSet resu = null;
		statement = connect.createStatement();
		resu = statement.executeQuery("SELECT MAX(idCommande) FROM Commande");
		resu.next();
		if (resu.getString(1) == null) {
			idCommande = 0;
		} else {
			idCommande = resu.getInt(1) + 1;
		}

		return idCommande;
	}

}
