package fr.esisar.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LigneCommandeDAO {

	public static boolean addLigneCommande(File fichier, Connection connect, String quantite, int idCommande,
			String idFormat, int idAlbum, String email) throws SQLException, IOException {
		Statement statement;
		boolean res = true;
		boolean res2 = false;
		ResultSet result = null;
		statement = connect.createStatement();
		String sentence = "SELECT * FROM LigneCommande";
		result = statement.executeQuery(sentence);
		while (result.next()) {
			if (idCommande == result.getInt("idCommande") && idFormat.equals(result.getString("idFormat"))
					&& idAlbum == result.getInt("idAlbum")) {
				res = false;
			}
		}
		sentence = "SELECT idAlbum FROM Album WHERE adresseMail = '" + email + "'";
		result = statement.executeQuery(sentence);
		while (result.next()) {
			if (idAlbum == Integer.parseInt((result.getString("idAlbum")))) {
				res2 = true;
			}
		}
		if (res && res2) {
			result = statement.executeQuery("SELECT MAX(idLigneCommande) FROM LigneCommande");
			result.next();
			int idLigneCommande = result.getInt(1) + 1;
			String quer1 = "INSERT INTO LigneCommande (idLigneCommande, quantite, idCommande, idFormat, idAlbum) ";
			String quer2 = "VALUES ('" + idLigneCommande + "', '" + quantite + "', '" + idCommande + "', '" + idFormat
					+ "', '" + idAlbum + "')";
			result = statement.executeQuery(quer1 + quer2);

			// Ecriture fichier log
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fichier, true));
			bufferedWriter.write("Ajout Ligne de commande : " + idLigneCommande + " quantite : " + quantite + "\n");
			bufferedWriter.newLine();
			bufferedWriter.write("Effectue : " + res);
			bufferedWriter.close();
		}
		return res & res2;
	}

	public int PrixCommande(File fichier, Connection connect, int idCommande, String email)
			throws SQLException, IOException {
		int prix = 0;
		Statement statement;
		FormatDAO form = new FormatDAO();
		ResultSet result = null;
		statement = connect.createStatement();
		String sentence = "SELECT idCommande, idFormat, quantite FROM LigneCommande";
		result = statement.executeQuery(sentence);
		while (result.next()) {
			if (idCommande == result.getInt("idCommande")) {
				String format = result.getString("idFormat");
				int qte = Integer.parseInt(result.getString("quantite"));
				prix = prix + form.getPrix(connect, format) * qte;
			}
		}
		// Ecriture fichier log
		/*
		 * BufferedWriter bufferedWriter = new BufferedWriter(new
		 * FileWriter(fichier, true));
		 * bufferedWriter.write("Prix de la lignecommande : " + idCommande +
		 * " est de : " + prix +"\n"); bufferedWriter.close();
		 */
		return prix;
	}

	public static int getidLigneCommande(Connection connect, String quantite, int idCommande, int idAlbum,
			String format, String email) throws SQLException {
		Statement statement;
		int idLigne = -1;
		ResultSet result = null;
		statement = connect.createStatement();
		String sentence = "SELECT * FROM LigneCommande";
		result = statement.executeQuery(sentence);
		while (result.next()) {
			if (idCommande == result.getInt("idCommande") && quantite.equals(result.getString("quantite"))
					&& idAlbum == result.getInt("idAlbum")) {
				idLigne = result.getInt("idLigneCommande");
			}
		}
		return idLigne;
	}

	public static boolean RetirerLigneCommande(File fichier, Connection connect, String email, int idLigneCommande,
			int idCommande) {
		boolean res = false;
		boolean res2 = false;
		ResultSet result = null;
		Statement st;

		try {
			if (CommandeDAO.VerifClient(connect, email, idCommande)
					&& LigneCommandeDAO.VerifPresenceLigneCommande(connect, idLigneCommande)) {
				st = connect.createStatement();

				// insert the data
				st.executeUpdate("DELETE FROM LigneCommande WHERE idLigneCommande = '" + idLigneCommande + "' "
						+ " AND idCommande = '" + idCommande + "'");
				System.out.println(
						"Ligne commande d'id = " + idLigneCommande + " supprime de la commande id=" + idCommande);

				result = st.executeQuery("SELECT idCommande FROM LigneCommande");
				while (result.next()) {
					if (idCommande == result.getInt("idCommande")) {
						res2 = true;
					}
				}
				if (!res2) {
					CommandeDAO.SupprCommande(connect, email, idCommande);
					System.out.println("Commande supprimee !");
				}
				System.out.println("Ligne de commande supprimee !");
				res = true;

			} else {
				System.out.println("Cette commande n'existe pas !");
				res = false;
			}
		} catch (SQLException e) {
			System.out.println("Erreur dans le retrait de la ligne de commande");
			res = false;
		}

		// Ecriture fichier log
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fichier, true));
			bufferedWriter
					.write("Retrait Ligne de commande : " + idLigneCommande + " de la commande : " + idCommande + "\n");
			bufferedWriter.newLine();
			bufferedWriter.write("Effectue : " + res);
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.println("Erreur fichier de log");
		}
		return res;
	}

	public static boolean RetirerLigneCommandeAlbum(Connection connect, String email, int idAlbum) {
		boolean res = false;
		boolean res2 = false;
		ResultSet result = null;
		ResultSet result2 = null;
		int com;
		Statement st;

		try {
			st = connect.createStatement();

			result = st.executeQuery(
					"SELECT idLigneCommande, idCommande FROM LigneCommande WHERE idAlbum = '" + idAlbum + "' ");
			while (result.next()) {
				res2 = false;
				com = result.getInt("idCommande");
				result2 = st.executeQuery("SELECT idCommande, idLigneCommande FROM Commande WHERE idLigneCommande = '"
						+ result.getInt("idLigneCommande") + "' ");
				while (result2.next()) {
					if (com != result2.getInt("idCommande")) {
						res2 = true;
					}
				}
				if (!res2) {
					CommandeDAO.SupprCommande(connect, email, com);
				}
			}
			// insert the data
			st.executeUpdate("DELETE FROM LigneCommande WHERE idAlbum = '" + idAlbum + "' ");
			System.out.println("Ligne commande contenant  " + idAlbum + " supprime");

			res = true;
		} catch (SQLException e) {
			System.out.println("Erreur dans le retrait de la ligne de commande");
			res = false;
		}

		return res;
	}

	public static void AfficherListeLigneCommande(Connection connect, int idCommande) {
		Statement statement;
		ResultSet result = null;

		try {
			statement = connect.createStatement();
			String sentence = "SELECT idLigneCommande, idFormat, quantite FROM LigneCommande WHERE idCommande = '"
					+ idCommande + "'";
			result = statement.executeQuery(sentence);
			while (result.next()) {
				System.out.println("Ligne commande : " + result.getInt("idLigneCommande") + " au format : "
						+ result.getString("idFormat") + " quantite : " + result.getString("quantite"));

			}
		} catch (SQLException e) {
			System.out.println("Erreur !");
		}
	}

	public static boolean VerifPresenceLigneCommande(Connection connect, int idLigneCommande) {
		Statement statement;
		boolean res = false;
		ResultSet result = null;
		try {
			statement = connect.createStatement();
			String sentence = "SELECT idLigneCommande FROM LigneCommande";
			result = statement.executeQuery(sentence);
			while (result.next()) {
				if (idLigneCommande == result.getInt("idLigneCommande")) {
					res = true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Erreur acces ligne");
		}
		return res;
	}
}
