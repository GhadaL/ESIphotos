package fr.esisar.controller;

import fr.esisar.model.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.management.Query;

import fr.esisar.controller.*;

public class FichierImageDAO extends FichierImage {

	public FichierImageDAO(String cheminAcces, String appareilPhoto, String objectif, String distanceFocal,
			String sensibiliteISO, String ouverture, String vitesseOburation, String client) {

		super(cheminAcces, appareilPhoto, objectif, distanceFocal, sensibiliteISO, ouverture, vitesseOburation, client);
	}

	public FichierImageDAO() {
		super();
	}

	public static boolean InsererFichierImage(Connection Connection, String cheminAcces, String appareilPhoto,
			String objectif, String distanceFocal, String sensibiliteISO, String ouverture, String vitesseOburation,
			String adressMail) {
		Statement statement;
		ResultSet result = null;
		try {
			statement = Connection.createStatement();
			String quer1 = "INSERT INTO FichierImage (cheminAcces, appareilPhoto, objectif, distanceFocale, sensibiliteISO, ouverture, vitesseObturation,adresseMail) ";
			String quer2 = "VALUES ('" + cheminAcces + "', '" + appareilPhoto + "', '" + objectif + "', '"
					+ distanceFocal + "', '" + sensibiliteISO + "', '" + ouverture + "', '" + vitesseOburation + "', '"
					+ adressMail + "')";
			statement.executeUpdate(quer1 + quer2);
			System.out.println("fichier Image est bien ajoute!");
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Erreur : Fichier n'a pas pu etre insere");
			return false;
		}

	}

	public static boolean SupprimerFichierImage(Connection connect, String url) throws ClassNotFoundException {
		Statement statement;
		try {
			statement = connect.createStatement();

			// delete from contient
			// statement.executeUpdate("DELETE FROM CONTIENT WHERE cheminAcces =
			// '"+url+"'");
			String sql = "DELETE FROM FichierImage WHERE cheminAcces = '" + url + "'";

			int r = statement.executeUpdate(sql);
			System.out.println("Le fichierImage " + url + " est supprime.");
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public static FichierImage findByURL(Connection connect, String url) throws SQLException {
		Statement statement;
		ResultSet result = null;

		statement = connect.createStatement();
		String sql = "SELECT * FROM FichierImage WHERE cheminAcces = \'" + url + "\' ";
		result = statement.executeQuery(sql);
		FichierImage image = new FichierImage();
		while (result.next()) {
			image.setCheminAcces(url);
			image.setAppareilPhoto(result.getString("appareilPhoto"));
			image.setObjectif(result.getString("objectif"));
			image.setDistanceFocal(result.getString("distanceFocale"));
			image.setSensibiliteISO(result.getString("sensibiliteISO"));
			image.setOuverture(result.getString("ouverture"));
			image.setVitesseOburation(result.getString("VitesseObturation"));
		}
		return image;
	}

	public void afficherFichierImage(Connection connect, String idClient) throws SQLException {
		Statement statement;
		FichierImage image;
		ResultSet result = null;
		statement = connect.createStatement();
		String sql = "SELECT * FROM FichierImage WHERE adresseMail = '" + idClient + "'";
		result = statement.executeQuery(sql);
		image = new FichierImage(idClient);
		while (result.next()) {
			image.setCheminAcces(result.getString("cheminAcces"));
			image.setAppareilPhoto(result.getString("appareilPhoto"));
			image.setObjectif(result.getString("objectif"));
			image.setDistanceFocal(result.getString("distanceFocale"));
			image.setSensibiliteISO(result.getString("sensibiliteISO"));
			image.setOuverture(result.getString("ouverture"));
			image.setVitesseOburation(result.getString("VitesseObturation"));
			image.toString();
		}

	}

	public static boolean VerifierPresenceImage(Connection connect, String chemin) {
		Statement statement;
		boolean res = false;
		ResultSet result = null;
		try {
			statement = connect.createStatement();
			String sentence = "SELECT cheminAcces FROM FichierImage";
			result = statement.executeQuery(sentence);
			while (result.next()) {
				if (chemin.equals(result.getString("cheminAcces"))) {
					res = true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public static boolean SupprimerFichierImageAvecVerif(Connection connect, String url, String email)
			throws ClassNotFoundException, SQLException {
		Statement statement;
		int idalbum;
		ResultSet result = null;
		ResultSet result2 = null;
		boolean del = true;
		// Verification existence de l'album a supprimer
		if (!VerificationFichierImage(connect, url, email)) {
			return false;
		}
		// Suppression de l'album
		System.out.println("Suppression du fichier image " + url + "...");
		statement = connect.createStatement();
		String sentence = "SELECT idAlbum FROM Contient WHERE cheminAcces = '" + url + "'";
		result = statement.executeQuery(sentence);
		while (result.next()) {
			idalbum = result.getInt("idAlbum");
			ContientDAO.RetirerFichierImagefromAlbum(connect, url, idalbum);
		}
		return true;
	}

	public static boolean VerificationFichierImage(Connection connect, String url, String email) throws SQLException {
		Statement statement;
		ResultSet result = null;
		ResultSet result2 = null;
		boolean del = true;
		// Verification existence de l'album a supprimer
		statement = connect.createStatement();
		String sentence = "SELECT * FROM FichierImage WHERE adresseMail = '" + email + "'";
		result = statement.executeQuery(sentence);
		boolean valide = false;
		while (result.next()) {
			if (url.equals(result.getString("cheminAcces")))
				valide = true;
		}
		if (!valide) {
			System.out.println("Numero de fichierImage invalide : le client " + email
					+ " n'a pas de fichier image dont le lien est " + url);
		}
		return valide;
	}

}
