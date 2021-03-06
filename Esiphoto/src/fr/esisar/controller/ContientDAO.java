package fr.esisar.controller;

import fr.esisar.model.*;

import java.sql.*;

public class ContientDAO extends Contient {

	public ContientDAO(int num_ordre, String title, String commentaire) {
		super(num_ordre, title, commentaire);

	}

	public static boolean AjouterFichierImagetoAlbum(Connection connect, String url, int idAlbum, String titre,
			String commentaire, String email) {
		// si on a besion d'afficher l image
		// FichierImage image = new FichierImage();
		// image=FichierImageDAO.findByURL(connect, url);
		Statement st;
		int numOrdre;
		try {
			if(AlbumDAO.VerificationAlbum(connect, idAlbum, email)){
				st = connect.createStatement();
				ResultSet result = st.executeQuery("SELECT MAX(numOrdre) FROM Contient");
				result.next();
				// insert into contient la liaison
				if (result.getString(1) == null) {
					numOrdre = 0;
				} else {
					numOrdre = Integer.parseInt(result.getString(1)) + 1;
				}
				st = connect.createStatement();
				// insert the data
				st.executeUpdate("INSERT INTO Contient " + "VALUES ('" + numOrdre + "', '" + titre + "', '" + commentaire
						+ "', '" + url + "', '" + idAlbum + "')");
				System.out.println("Fichier image ajoute a album id=" + idAlbum);
				// album.ajouterFichierImage(image);
	
				return true;
			}else{
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Erreur : Ficher image n'a pas pu etre insere a l'album");
			return false;
		}

	}

	public static boolean RetirerFichierImagefromAlbum(Connection connect, String cheminAcces, int idAlbum)
			throws ClassNotFoundException {
		Statement st;
		try {
			st = connect.createStatement();
			// insert the data
			st.executeUpdate("DELETE FROM CONTIENT WHERE cheminAcces = '" + cheminAcces + "' " + " AND idAlbum = '"
					+ idAlbum + "'");
			System.out.println("Fichier image " + cheminAcces + " supprime de l'album id=" + idAlbum);
			// album.ajouterFichierImage(image);
			// then call method verify in Fichier image to delete the image if
			// it doesn't exist in any album
			ResultSet result = null;
			result = st.executeQuery("SELECT * FROM CONTIENT WHERE cheminAcces = '" + cheminAcces + "'");
			boolean del = true;
			while (result.next()) {

				if (cheminAcces.equals(result.getString("cheminAcces"))) {
					del = false;
				}
			}
			if (del) { // On supprime le fichier image uniquement s'il n'est pas
						// utilise dans un autre albums
				FichierImageDAO.SupprimerFichierImage(connect, cheminAcces);
			}

			result = st.executeQuery("SELECT * FROM CONTIENT WHERE idAlbum = '" + idAlbum + "'");
			boolean del2 = true;
			while (result.next()) {
				if (idAlbum == result.getInt("idAlbum")) {
					del2 = false;
				}
			}
			if (del2) {
				AlbumDAO.SupprimerAlbumDirect(connect, idAlbum);
			}

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

}
