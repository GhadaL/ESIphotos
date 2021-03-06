package fr.esisar.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.esisar.model.Album;
import fr.esisar.model.FichierImage;

public class AlbumDAO extends Album {

	public AlbumDAO(String titre, String sousTitre) {
		super(titre, sousTitre);
	}

	public int CreationAlbum(Connection connect, String adresseMail, String titre, String sousTitre)
			throws ClassNotFoundException, SQLException {
		Statement statement;
		ResultSet result = null;
		statement = connect.createStatement();
		result = statement.executeQuery("SELECT MAX(idAlbum) FROM Album");
		result.next();
		if (result.getString(1) == null) {
			idAlbum = 0;
		} else {
			idAlbum = Integer.parseInt(result.getString(1)) + 1;
		}
		String quer1 = "INSERT INTO Album (idAlbum, titre, sousTitre, adresseMail) ";
		String quer2 = "VALUES ('" + idAlbum + "', '" + titre + "', '" + sousTitre + "', '" + adresseMail + "')";
		result = statement.executeQuery(quer1 + quer2);
		System.out.println("L'album : " + idAlbum + " de titre : " + titre + " a ete cree");
		return idAlbum;
	}

	public static void afficherListeAlbum(Connection connect, String email) throws SQLException {
		System.out.println("Liste des albums du client " + email + " : ");
		Statement statement;
		ResultSet result = null;
		statement = connect.createStatement();
		String sentence = "SELECT * FROM Album WHERE adresseMail = '" + email + "'";
		result = statement.executeQuery(sentence);
		while (result.next()) {
			System.out.println("idAlbum : " + result.getInt("idAlbum") + ", titre : " + result.getString("titre")
					+ ", sous-titre : " + result.getString("sousTitre"));
		}
	}

	public static void modifierAlbum(Connection connect, int idAlbum, String titre, String sousTitre, String email)
			throws SQLException {
		Statement statement;
		// Verification existence de l'album a supprimer
		if (!VerificationAlbum(connect, idAlbum, email)) {
			return;
		}
		// Suppression de l'album
		statement = connect.createStatement();
		String sentence = "UPDATE Album SET titre = '" + titre + "', sousTitre = '" + sousTitre
				+ "' WHERE customer_id = '" + idAlbum + "'";
		statement.execute(sentence);
		System.out.println("La modification de l'album +" + idAlbum + " a été enregistrée : titre = '" + titre
				+ "', sousTitre = '" + sousTitre + "'");
	}

	public static void supprimerAlbum(Connection connect, int idAlbum, String email)
			throws SQLException, ClassNotFoundException {
		Statement statement;
		String cheminAsupp = null;
		ResultSet result = null;
		ResultSet result2 = null;
		boolean del = true;
		// Verification existence de l'album a supprimer
		if (!VerificationAlbum(connect, idAlbum, email)) {
			System.out.println(
					"Numero d'album invalide : le client " + email + " n'a pas d'album dont le numero est " + idAlbum);
			return;
		}
		LigneCommandeDAO.RetirerLigneCommandeAlbum(connect, email, idAlbum);

		// Suppression de l'album
		System.out.println("Suppression de l'album " + idAlbum + "...");
		statement = connect.createStatement();
		String sentence = "SELECT cheminAcces FROM Contient WHERE idAlbum = '" + idAlbum + "'";
		result = statement.executeQuery(sentence);
		System.out.println("Suppression des images contenues uniquement dans l'album " + idAlbum + "...");
		while (result.next()) {
			cheminAsupp = result.getString("cheminAcces");
			sentence = "SELECT idAlbum FROM Contient WHERE cheminAcces = '" + cheminAsupp + "'";
			result2 = statement.executeQuery(sentence);
			del = true;
			while (result2.next()) {
				if (idAlbum != result2.getInt("idAlbum")) {
					del = false;
				}
			}
			if (del) { // On supprime les fichiers image qui n'existent que dans
						// l'album que l'on supprime
				ContientDAO.RetirerFichierImagefromAlbum(connect, cheminAsupp, idAlbum);
				// FichierImageDAO.SupprimerFichierImage(connect, cheminAsupp);
			}
		}

		sentence = "SELECT cheminAcces FROM Contient WHERE idAlbum = '" + idAlbum + "'";
		result = statement.executeQuery(sentence);
		while (result.next()) {// On supprime les contient lies à l'album que
								// l'on souhaite supprimer
			ContientDAO.RetirerFichierImagefromAlbum(connect, result.getString("cheminAcces"), idAlbum);
		}
		if (VerificationAlbum(connect, idAlbum, email)) {
			SupprimerAlbumDirect(connect, idAlbum);
		}
	}

	public static boolean SupprimerAlbumDirect(Connection connect, int idAlbum) throws ClassNotFoundException {
		Statement statement;
		try {
			statement = connect.createStatement();

			// delete from contient
			// statement.executeUpdate("DELETE FROM CONTIENT WHERE cheminAcces =
			// '"+url+"'");
			String sql = "DELETE FROM Album WHERE idAlbum = '" + idAlbum + "'";
			System.out.println("La suppression de l'album " + idAlbum);
			statement.executeUpdate(sql);
			System.out.println("La suppression de l'album " + idAlbum + " a été enregistrée.");
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public static void AfficherFichierImageDeLalbum(Connection connect, int idAlbum, String email) throws SQLException {
		String idAlbumST = "" + idAlbum;
		Statement statement;
		ResultSet result = null;
		ResultSet result2 = null;
		FichierImage image = null;
		boolean del = true;
		// Verification existence de l'album a supprimer
		if (!VerificationAlbum(connect, idAlbum, email)) {
			return;
		}
		// Afichage des images de l'album
		System.out.println("Liste des images de l'album " + idAlbum);
		statement = connect.createStatement();
		String sentence = "SELECT * FROM Contient WHERE idAlbum = '" + idAlbum + "'";
		result = statement.executeQuery(sentence);
		while (result.next()) {
			image = FichierImageDAO.findByURL(connect, result.getString("cheminAcces"));
			System.out.print("image : " + image.getCheminAcces());
			System.out.print("; appareil : " + image.getAppareilPhoto());
			System.out.print("; distancefocale : " + image.getDistanceFocal());
			System.out.print("; objectif : " + image.getObjectif());
			System.out.print("; sensibilite iso : " + image.getSensibiliteISO());
			System.out.print("; ouverture : " + image.getOuverture());
			System.out.println("; vitesse obturation : " + image.getVitesseOburation());
		}
	}

	public static boolean VerificationAlbum(Connection connect, int idAlbum, String email) throws SQLException {
		Statement statement;
		ResultSet result = null;
		ResultSet result2 = null;
		boolean del = true;
		// Verification existence de l'album a supprimer
		statement = connect.createStatement();
		String sentence = "SELECT * FROM Album WHERE adresseMail = '" + email + "'";
		result = statement.executeQuery(sentence);
		boolean valide = false;
		while (result.next()) {
			if (idAlbum == result.getInt("idAlbum"))
				valide = true;
		}
		return valide;
	}

}