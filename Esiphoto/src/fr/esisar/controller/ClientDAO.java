package fr.esisar.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import fr.esisar.model.Client;
import fr.esisar.model.PasswordAuthentication;

public class ClientDAO extends Client {
	private Connection connexion;

	public ClientDAO(String mail, String password) throws ClassNotFoundException, SQLException {
		super(mail, password);
		connexion = creerConnection();
	}

	public ClientDAO(String mail, String nom, String prenom, String mdp) throws ClassNotFoundException, SQLException {
		super(mail, nom, prenom, mdp);
		connexion = creerConnection();
	}

	public boolean EnregistrerClient(File fichier, String ruel, String codePostall, String villel, String ruef,
			String codePostalf, String villef) throws ClassNotFoundException, SQLException, IOException {
		// si le client ne existe pas dans la basse de donnees, cette methode
		// cree une instance dans la basse de donnees par ce client.
		System.out.println("le client" + super.getPrenom() + "veux de connecter");
		boolean res = true;
		int idaddliv, idaddfac;
		Statement statement;
		ResultSet result = null;
		statement = connexion.createStatement();
		String sentence = "SELECT adresseMail FROM Client";
		result = statement.executeQuery(sentence);
		while (result.next()) {
			if (super.getAdressMail().equals(result.getString("adresseMail"))) {
				res = false;
			}
		}
		if (res == true) {
			result = statement.executeQuery("SELECT MAX(idAdresse) FROM Adresse");
			result.next();
			if (result.getString(1) == null) {
				idaddliv = 0;
				idaddfac = 1;
			} else {
				idaddliv = Integer.parseInt(result.getString(1)) + 1;
				idaddfac = Integer.parseInt(result.getString(1)) + 2;
			}

			AdresseDAO adresseliv = new AdresseDAO(idaddliv, ruel, codePostall, villel);
			AdresseDAO adressefac = new AdresseDAO(idaddfac, ruef, codePostalf, villef);
			adresseliv.addAddresse(connexion);
			adressefac.addAddresse(connexion);
			String quer1 = "INSERT INTO Client (adresseMail, nom, prenom, password,idadresseL,idadresseF) ";
			String quer2 = "VALUES ('" + super.getAdressMail() + "', '" + super.getNom() + "', '" + super.getPrenom()
					+ "', '" + super.getPassword() + "', '" + idaddliv + "', '" + idaddfac + "')";
			result = statement.executeQuery(quer1 + quer2);
		}
		// Ecriture fichier log
		System.out.println(fichier);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fichier, true));
		bufferedWriter.write("Enregistrer Nom du client : " + super.getNom() + " Prenom: " + super.getPrenom()
				+ " Mail: " + super.getAdressMail() + "Mot de passe:" + super.getPassword() + "\n");
		bufferedWriter.newLine();
		bufferedWriter.write("Effectue : " + res);
		bufferedWriter.close();
		return res;
	}

	public boolean ConnexionClient(String mail, String mdp) throws ClassNotFoundException, SQLException {
		// si le client exist, cette methode returns true
		boolean res = false;
		Statement statement;
		ResultSet result = null;
		statement = connexion.createStatement();
		String sentence = "SELECT adresseMail FROM Client";
		result = statement.executeQuery(sentence);
		ResultSet resu;
		while (result.next()) {
			if (mail.equals(result.getString("adresseMail"))) {
				sentence = "SELECT password FROM Client";
				resu = statement.executeQuery(sentence);
				while (resu.next()) {
					PasswordAuthentication psw = new PasswordAuthentication(5);
					if (psw.authenticate(mdp.toCharArray(), resu.getString("password"))) {
						res = true;
						break;
					}
				}
			}
		}
		return res;
	}

	private static Connection creerConnection() throws ClassNotFoundException, SQLException {
		String server = "tp-oracle.esisar.grenoble-inp.fr";
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@//" + server + "/xe";
		String login = "royang";
		String mdp = "royang";

		try {
			Class.forName(driver);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection connection = DriverManager.getConnection(url, login, mdp);
		// System.out.println(connection);
		return connection;

	}

	public int AjouterAlbum(String titre, String sousTitre) throws ClassNotFoundException, SQLException {
		AlbumDAO album = new AlbumDAO(titre, sousTitre);
		return album.CreationAlbum(connexion, super.getAdressMail(), titre, sousTitre);
	}

	public void AfficherListeAlbum() throws SQLException {
		AlbumDAO.afficherListeAlbum(connexion, super.getAdressMail());
	}

	public void modifierAlbum(int idAlbum, String titre, String sousTitre) throws SQLException {
		AlbumDAO.modifierAlbum(connexion, idAlbum, titre, sousTitre, super.getAdressMail());
	}

	public void SupprimerAlbum(int idAlbum) throws SQLException, ClassNotFoundException {
		AlbumDAO.supprimerAlbum(connexion, idAlbum, super.getAdressMail());
	}

	/*
	 * public void ajouterNouveauFichierImage(String cheminAcces, String
	 * appareilPhoto, String objectif, double distanceFocal, String
	 * sensibiliteISO, String ouverture, double vitesseOburation){
	 * FichierImageDAO image = new FichierImageDAO(String cheminAcces, String
	 * appareilPhoto, String objectif, double distanceFocal, String
	 * sensibiliteISO, String ouverture, double vitesseOburation);
	 * image.insererFichierImage(connect); album.ajouterFichierImage(image); }
	 * 
	 * public void copierFichierImage(String cheminAcces){ FichierImageDAO image
	 * = new FichierImageDAO(); image.findByURL(cheminAcces);
	 * album.ajouterFichierImage(image); }
	 * 
	 */

	// AlbumDAO : creer la methode : public String getidAlbum(String titreAlbum,
	// String soustitreAlbum)
	// qui sera utilisee pour creerLigneCommande

	public boolean InsererFichierImage(String cheminAcces, String appareilPhoto, String objectif, String distanceFocal,
			String sensibiliteISO, String ouverture, String vitesseOburation)
			throws ClassNotFoundException, SQLException {
		return FichierImageDAO.InsererFichierImage(connexion, cheminAcces, appareilPhoto, objectif, distanceFocal,
				sensibiliteISO, ouverture, vitesseOburation, super.getAdressMail());
	}

	public boolean SupprimerFichierImage(String cheminAcces) throws ClassNotFoundException, SQLException {
		return FichierImageDAO.SupprimerFichierImageAvecVerif(connexion, cheminAcces, super.getAdressMail());
	}

	public boolean AjouterFichierImagetoAlbum(String cheminAcces, int idAlbum, String titre, String commentaire)
			throws ClassNotFoundException, SQLException {
		return ContientDAO.AjouterFichierImagetoAlbum(connexion, cheminAcces, idAlbum, titre, commentaire, super.getAdressMail());
	}

	public boolean RetirerFichierImagefromAlbum(String cheminAcces, int idAlbum)
			throws ClassNotFoundException, SQLException {
		return ContientDAO.RetirerFichierImagefromAlbum(connexion, cheminAcces, idAlbum);
	}

	public int CreerCommande(int idLigneCommande) throws ClassNotFoundException, SQLException {
		CommandeDAO comm = new CommandeDAO();
		return comm.addCommande(connexion, super.getAdressMail(), idLigneCommande);
		// Retourne l'idCommande de la commande creee
		// commande temporaire de prix 0. A supprimer + tard si le client ne la
		// valide pas.
		// Dans le main, demander au client de memoriser son idCommande
	}

	public boolean ChoixFormat(String idFormat) throws ClassNotFoundException, SQLException {
		// verifier dans le main que le format idFormat est entre A1 et A6
		// Attention : idFormat correspond a A1, A2, A3... A6 (un format n'est
		// pas propre a une commande)
		return FormatDAO.verifierPresenceFormat(connexion, idFormat);
		// retourne le prix du format
	}

	public boolean CreerLigneCommande(File fichier, String quantite, int idCommande, int idAlbum, String idFormat)
			throws ClassNotFoundException, SQLException, IOException {
		return LigneCommandeDAO.addLigneCommande(fichier, connexion, quantite, idCommande, idFormat, idAlbum,
				super.getAdressMail());
	}

	public int ValiderCommande(File file, int idCommande) throws ClassNotFoundException, SQLException, IOException {
		return CommandeDAO.ValiderCommande(file, connexion, idCommande, super.getAdressMail());
		// Pour valider la commande, calculer prixTotal a partir des
		// prixUnitaires
		// Retourner le prixTotal
		// Dans le main, demander au client de memoriser son idCommande
	}

	public void AfficherListeFormat() throws ClassNotFoundException, SQLException {
		FormatDAO.AfficherListFormat(connexion);
	}

	public boolean VerifierValiditeCommande(int idCommande) throws SQLException {
		// A partir d'un idCommande, verifie que la commande existe et qu'elle
		// est toujours "en cours" (que son prix est bien a 0)
		return CommandeDAO.VerifIdCommande(connexion, idCommande, super.getAdressMail());
	}

	public void AfficherListeCommande() throws SQLException {
		// Affiche liste des commandes (les idCommandes) d'un utilisateur (grace
		// a son adresse mail), ainsi que l'etat de chaque commande :
		// si elle est en cours (prix=0) ou validee (prix>0)
		CommandeDAO.AffListCommande(connexion, super.getAdressMail());
	}

	public void AfficherListeImagesfromAlbum(int idAlbum) throws SQLException {
		// Affiche la liste des fichier images (numordre, titre & commentaire de
		// la classe contient) contenus dans l'album idAlbum.
		AlbumDAO.AfficherFichierImageDeLalbum(connexion, idAlbum, super.getAdressMail());
	}

	public boolean VerifierPresenceImage(String chemin) {

		return FichierImageDAO.VerifierPresenceImage(connexion, chemin);
	}

	public int getidLigneCommande(String quantite, int idCommande, int idAlbum, String format) throws SQLException {
		return LigneCommandeDAO.getidLigneCommande(connexion, quantite, idCommande, idAlbum, format,
				super.getAdressMail());
	}

	public int getidCommande() throws SQLException {
		return CommandeDAO.getidCommande(connexion);
	}

	public boolean RetirerLigneCommande(int idLigneCommande, int idCommande)
			throws ClassNotFoundException, SQLException {
		return LigneCommandeDAO.RetirerLigneCommande(super.getFile(), connexion, super.getAdressMail(), idLigneCommande,
				idCommande);
	}

	public void AfficherListeLigneCommande(int idCommande) {
		LigneCommandeDAO.AfficherListeLigneCommande(connexion, idCommande);
	}
	
	public boolean VerificationAlbum(int idAlbum) throws SQLException {
		return AlbumDAO.VerificationAlbum(connexion, idAlbum, super.getAdressMail());
	}
}
