package fr.esisar.view;

import fr.esisar.controller.AlbumDAO;
import fr.esisar.controller.ClientDAO;
import fr.esisar.model.Client;
import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
	private static Scanner sc;

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		System.out.println("*****Bienvenue sur la plateforme Esiphoto*****by CS441 group3");
		sc = new Scanner(System.in);
		Main.menuEntree();

	}

	public static void menuEntree() throws IOException, ClassNotFoundException, SQLException {
		System.out.println("\n\n*****ESIPHOTO MENU D'ACCUEIL*****");
		// Scanner sc = new Scanner(System.in);
		String firstAction = null;

		do {
			System.out.println("\nVeuillez taper le numero de l'action a effectuer");

			System.out.println("0 : Souscription");
			System.out.println("1 : Connexion");
			System.out.println("2 : Quitter");
			System.out.print("Votre action : ");
			try {
				firstAction = lire();
			} catch (NoSuchElementException e) {
				System.out.println("Erreur de saisie !");
				firstAction = "";
			}

			System.out.print("\n");

			if (firstAction.equals("0")) {
				if (!Main.Subscribe())
					firstAction = "";
			} else if (firstAction.equals("1")) {
				if (!Main.Connexion())
					firstAction = "";
			} else if (firstAction.equals("2")) {
				// sc.close();
				System.out.print("\n\nAurevoir & A bientot ! ");
			} else {
				System.out.print("Votre choix est incorrect.");
			}
		} while (!(firstAction.equals("0") | firstAction.equals("1") | firstAction.equals("2")));

	}

	public static boolean Subscribe() throws IOException, ClassNotFoundException, SQLException {
		boolean res = false;
		try {
			ClientDAO userDAO = null;
			System.out
					.println("Veuillez entrer les informations suivantes necessaires a la creation de votre compte: ");
			System.out.println("Nom : ");
			String Nom = lire();
			System.out.println("Prenom : ");
			String Prenom = lire();
			System.out.println("Mail : ");
			String mail = lire();
			System.out.println("Password : ");
			String Password = lire();

			System.out.println("Rue de livraison");
			String ruel = lire();
			System.out.println("codePostal de livraison : ");
			String codePostall = lire();
			System.out.println("ville de livraison : ");
			String villel = lire();
			System.out.println("Rue de facturation");
			String ruef = lire();
			System.out.println("codePostal de facturation : ");
			String codePostalf = lire();
			System.out.println("ville de facturation : ");
			String villef = lire();
			System.out.println("Login : " + mail + "\npassword : " + Password);
			System.out.println("Creation de votre compte...");
			userDAO = new ClientDAO(mail, Nom, Prenom, Password);

			if (userDAO.EnregistrerClient(userDAO.getFile(), ruel, codePostall, villel, ruef, codePostalf, villef)) {
				System.out.println(
						"Felicitations ! Votre compte a bien ete enregistre." + " \nVous etes maintenant connecte");
				Main.MenuPrincipal(userDAO);
				res = true;
			} else {
				System.out.println("Nous sommes desoles. Un compte existe deja pour cette adresse mail");
				res = false;
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			res = false;
			return res;
		} catch (NoSuchElementException e) {
			System.out.println("Erreur saisie!");
			res = false;
			return res;
		}
		return res;
	}

	public static boolean Connexion() throws IOException, ClassNotFoundException, SQLException {
		boolean res = false;
		try {

			System.out.println("Mail : ");
			String mail = lire();
			System.out.println("Password : ");
			String password = lire();
			System.out.println("Connexion a votre compte...");
			ClientDAO userDAO = new ClientDAO(mail, password);

			if (userDAO.ConnexionClient(mail, password)) {
				System.out.println("Connexion reussie.");
				Main.MenuPrincipal(userDAO);
				res = true;
			} else {
				System.out.println("La connexion n'a pas aboutie, votre identifiant ou password est incorrect.");
				res = false;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			res = false;
		} catch (NoSuchElementException e) {
			System.out.println("Erreur de saisie !");
			res = false;
		}
		return res;
	}

	public static void MenuPrincipal(ClientDAO userDAO) throws IOException, ClassNotFoundException, SQLException {

		System.out.println("\n\n*****Esiphoto Menu Principal*****");
		String action = null;

		do {
			System.out.println("\nVeuillez taper le numero de l'action a effectuer");

			System.out.println("0 : Afficher la liste des Albums");
			System.out.println("1 : Creer un Album");
			System.out.println("2 : Supprimer un Album");
			System.out.println("3 : Afficher la liste d'images dans un album");
			System.out.println("4 : Deposer un fichier image");
			System.out.println("5 : Supprimer un fichier image");
			System.out.println("6 : Ajouter un fichier image (existant) a l'album");
			System.out.println("7 : Retirer un fichier image a l'album...");
			System.out.println("8 : Passer une commande...");
			System.out.println("9 : Deconnexion");
			System.out.print("Votre action : ");
			try {
				action = lire();
			} catch (NoSuchElementException e) {
				System.out.println("Erreur de saisie !");
				action = "boucle";
			} catch (NullPointerException n) {
				System.out.println("Erreur de saisie !");
				action = "boucle";
			}
			switch (action) {
			case "0":
				userDAO.AfficherListeAlbum();
				break;

			case "1":

				try {
					String chemin = null;
					System.out.println("Titre de l'album a creer : ");
					String titre = lire();
					System.out.println("Sous titre : ");
					String sous_titre = lire();

					System.out.println(
							"Votre Album doit comporter au moins une image. Si vous souhaitez ajouter une image contenue dans un autre"
									+ " album, entrer son chemin, sinon entrer new pour inserer une image");
					String rep = lire();
					if (rep.equals("new")) {
						System.out.println(
								"Veuillez nous preciser les informations suivantes concernant votre premier fichier image :");
						System.out.println("Chemin d'acces sur le serveur : ");
						chemin = lire();

						System.out.println("Appareil photo utilise : ");

						String apphoto = lire();

						System.out.println("Objectif utilise : ");
						String objectif = lire();
						System.out.println("Distance focale : ");
						String focale;
						try {
							focale = lire();
						} catch (NumberFormatException e) {
							System.out.println("Erreur de saisie !");
							break;
						}
						System.out.println("Sensibilite ISO : ");
						String iso = lire();
						System.out.println("Ouverture : ");
						String ouverture = lire();
						System.out.println("Vitesse d'obturation : ");
						String obturation;
						try {
							obturation = lire();
						} catch (NumberFormatException e) {
							System.out.println("Erreur de saisie !");
							break;
						}
						if (!userDAO.InsererFichierImage(chemin, apphoto, objectif, focale, iso, ouverture,
								obturation)) {
							break;
						}
					} else {
						chemin = rep;
					}
					if (userDAO.VerifierPresenceImage(chemin)) {
						int idAlb = userDAO.AjouterAlbum(titre, sous_titre);
						System.out.println("Titre de la photo dans l'album : ");
						String titrephoto = lire();
						System.out.println("Commentaire sur la photo dans l'album : ");
						String commentaire = lire();
						if (!userDAO.AjouterFichierImagetoAlbum(chemin, idAlb, titrephoto, commentaire)) {
							break;
						}
					} else {
						System.out.println("Nous sommes dsl, la creation de votre album n'a pas aboutie");
					}
				} catch (NoSuchElementException e) {
					System.out.println("Erreur de saisie !");
					break;
				}
				break;

			case "2":
				System.out.println(
						"Attention : La suppression d'un album entrainera la suppression de tous les fichiers images qui ne sont"
								+ " contenus dans aucun autre album. Continuer ? [tapez o pour continuer ou n pour annuler]");
				String lire = lire();
				if (lire.equals("n")) {
					System.out.println("Action annulee");
					break;
				} else if (lire.equals("o")) {
					System.out.println("Identifiant de l'album a supprimer : ");
					int idAlbum;
					try {
						idAlbum = lireInt();
					} catch (InputMismatchException e) {
						System.out.println("Erreur tu as tap un String");
						break;
					} catch (NoSuchElementException e) {
						System.out.println("Erreur de saisie !");
						break;
					} catch (NumberFormatException e) {
						System.out.println("Erreur de saisie");
						break;
					}
					userDAO.SupprimerAlbum(idAlbum);
				} else {
					System.out.println("Erreur de saisie");
					break;
				}

				break;

			case "3":
				System.out.println("Identifiant de l'album a consulter : ");
				int identAlbum;
				try {
					identAlbum = lireInt();

				} catch (InputMismatchException e) {
					System.out.println("Erreur tu as tap un String");
					break;
				} catch (NoSuchElementException e) {
					System.out.println("Erreur de saisie !");

					break;
				} catch (NumberFormatException e) {
					System.out.println("Erreur de saisie");
					break;
				}
				userDAO.AfficherListeImagesfromAlbum(identAlbum);
				break;

			case "4":

				try {
					System.out.println(
							"Veuillez nous preciser les informations suivantes concernant votre fichier image :");

					System.out.println("Chemin d'acces sur le serveur : ");
					String chemn = lire();
					System.out.println("Appareil photo utilise : ");
					String aphoto = lire();
					System.out.println("Objectif utilise : ");
					String objecti = lire();
					System.out.println("Distance focale : ");
					String focal = lire();
					System.out.println("Sensibilite ISO : ");
					String is = lire();
					System.out.println("Ouverture : ");
					String overture = lire();
					System.out.println("Vitesse d'obturation : ");
					String obturatio = lire();

					System.out.println(
							"Votre fichier image doit etre contenu dans au moins un album. Voici la liste de vos albums : ");
					userDAO.AfficherListeAlbum();
					System.out.println("Vous pourrez ensuite ajouter cette "
							+ "meme image dans d'autres albums depuis le menu principal. [entrez o pour continuer et n pour annuler]");
					String decision = lire();
					if (decision.equals("n")) {
						userDAO.SupprimerFichierImage(chemn);
						break;
					} else if (decision.equals("o")) {
						System.out.println("Entrer l'idAlbum pour lequel vous voulez mettre votre image");
						String idal = lire();
						int idalb = Integer.parseInt(idal);
						System.out.println("Titre de la photo dans l'album : ");
						String titrphoto = lire();
						System.out.println("Commentaire sur la photo dans l'album : ");
						String commentair = lire();
						if(userDAO.VerificationAlbum(idalb)){
							userDAO.InsererFichierImage(chemn, aphoto, objecti, focal, is, overture, obturatio);
							userDAO.AjouterFichierImagetoAlbum(chemn, idalb, titrphoto, commentair);
						}
						else{
							System.out.println("Erreur : votre fichier image n'a pas pu etre insere !");
						}
					} else {
						System.out.println("Erreur de saisie !");
						break;
					}
				} catch (NoSuchElementException e) {
					System.out.println("Erreur de saisie !");
					break;
				}
				break;

			case "5":
				try {
					System.out.println("Veuillez entrer le chemin d'acces du fichier que vous desirer supprimer :");
					String cheminacces = lire();
					userDAO.SupprimerFichierImage(cheminacces);
				} catch (NoSuchElementException e) {
					System.out.println("Erreur de saisie !");
					break;
				}
				break;

			case "6":
				System.out.println("***Ajouter un fichier image existant a l'album...***");
				System.out.println("Veuillez entrer les informations suivantes :");
				System.out.println("Chemin d'acces du fichier image : ");
				String cheminAcces = lire();
				System.out.println("Identifiant de l'album");

				int id_Album;
				try {
					id_Album = lireInt();
				} catch (NumberFormatException e) {
					System.out.println("Erreur de saisie");
					break;
				} catch (InputMismatchException e) {
					System.out.println("Erreur tu as tap un String");
					break;
				} catch (NoSuchElementException e) {
					System.out.println("Erreur de saisie !");
					break;
				}
				System.out.println("Titre de la photo dans l'album : ");
				String titrphoto1 = lire();
				System.out.println("Commentaire sur la photo dans l'album : ");
				String commentair1 = lire();
				userDAO.AjouterFichierImagetoAlbum(cheminAcces, id_Album, titrphoto1, commentair1);

				break;

			case "7":
				System.out.println("***Retirer un fichier image a l'album...***");
				System.out.println(
						"Attention : Le retrait d'un fichier image depuis un album entrainera sa suppression si celui-ci n'est"
								+ " contenu dans aucun autre album. Continuer ? [tapez o pour continuer ou n pour annuler]");
				String lir = lire();
				if (lir.equals("n"))
					break;
				else if (lir.equals("o")) {
					System.out.println("Veuillez entrer les informations suivantes :");
					System.out.println("Chemin d'acces du fichier image : ");
					String chemin_Acces = lire();
					System.out.println("Identifiant de l'album");
					int idalbum;
					try {
						idalbum = lireInt();
					} catch (InputMismatchException e) {
						System.out.println("Erreur tu as tap un String");
						break;
					} catch (NoSuchElementException e) {
						System.out.println("Erreur de saisie !");
						break;
					} catch (NumberFormatException e) {
						System.out.println("Erreur de saisie");
						break;
					}
					userDAO.RetirerFichierImagefromAlbum(chemin_Acces, idalbum);
					break;
				} else {
					System.out.println("Erreur de saisie");
					break;
				}

			case "8":
				break;

			case "9":
				System.out.println("Deconnex0ion... ");
				break;

			default:
				System.out.println("Votre choix est incorrect.");
				action = "0";
				break;
			}

		} while (action.equals("0") | action.equals("1") | action.equals("2") | action.equals("3") | action.equals("4")
				| action.equals("5") | action.equals("6") | action.equals("7") | action.equals("boucle"));

		if (action.equals("8")) {
			Main.ServiceCommande(userDAO);
		}
		if (action.equals("9")) {
			Main.menuEntree();
		}
	}

	public static void ServiceCommande(ClientDAO userDAO) throws ClassNotFoundException, SQLException, IOException {

		int idCommande = 0;
		String idcommande = null;
		;
		boolean res = false;
		while (res == false) {
			System.out.println("\n\n*****Esiphoto Menu Commande*****");

			System.out.println("La liste de vos commande est la suivante : \n");
			userDAO.AfficherListeCommande();

			System.out.println(
					"Si vous souhaitez continuer une commande toujours en cours, veuillez entrer son identifiant."
							+ "\nSinon entrez simplement 'new'");

			try {
				idcommande = lire();

				if (idcommande.equals("new")) {
					System.out.println("Creation de votre premiere ligne commande");
					System.out.println("Selectionner votre format parmi la liste suivante : ");
					userDAO.AfficherListeFormat();
					String format = lire();
					if (userDAO.ChoixFormat(format)) {
						System.out.println(
								"Entrer l'identifiant de votre album a commander parmi les albums suivants : ");
						userDAO.AfficherListeAlbum();
						System.out.println("\nVotre choix : ");
						int idAlbum = -1;
						try {
							idAlbum = lireInt();
						} catch (NumberFormatException e) {
							System.out.println("Erreur de saisie");
						} catch (InputMismatchException e) {
							System.out.println("Erreur tu as tape un String");

						}
						System.out.println("Entrer la quantite desiree : ");
						String quantite = lire();
						if (!userDAO.CreerLigneCommande(userDAO.getFile(), quantite, userDAO.getidCommande(), idAlbum,
								format)) {
							System.out.println("Erreur dans la creation de la commande");

						} else {
							idCommande = userDAO
									.CreerCommande(userDAO.getidLigneCommande(quantite, idCommande, idAlbum, format));
							System.out.println("L'id de votre nouvelle commande est : " + idCommande);
							res = true;
						}

					} else {
						System.out.println("Erreur : format invalide.");
					}
				} else {
					idCommande = Integer.parseInt(idcommande);
					if (!userDAO.VerifierValiditeCommande(idCommande)) {
						System.out.println("Desole, l'id commande renseigne n'est pas valide");

					} else {
						res = true;
					}

				}
			} catch (NoSuchElementException e) {
				Main.MenuPrincipal(userDAO);
				return;
			} catch (NumberFormatException e) {
				System.out.println("Desole, l'id commande renseigne n'est pas valide");
			}
		}
		// } //catch (NumberFormatException e) {
		// System.out.println("Erreur de saisie !");
		// Main.ServiceCommande(userDAO);
		// return;
		// }

		String action = null;
		do {
			try {
				System.out.println("\n\nVeuillez taper le numero de l'action a effectuer");
				System.out.println("0 : Creer une nouvelle ligne commande");
				System.out.println("1 : Afficher les lignes commandes de votre commande");
				System.out.println("2 : Supprimer une ligne commande");
				System.out.println("3 : Valider votre commande");
				System.out.println("4 : Quitter");
				System.out.print("Votre action : ");
				action = lire();

				switch (action) {
				case "0":
					System.out.println("Selectionner votre format parmi la liste suivante : ");
					userDAO.AfficherListeFormat();
					String format = lire();
					if (!userDAO.ChoixFormat(format)) {
						System.out.println("Erreur : format invalide.");
						break;
					}
					System.out.println("Entrer l'identifiant de votre album a commander : ");
					userDAO.AfficherListeAlbum();

					int idAlbum = -1;
					try {
						idAlbum = lireInt();
					} catch (NumberFormatException e) {
						System.out.println("Erreur de saisie");
						break;
					} catch (InputMismatchException e) {
						System.out.println("Erreur vous avez entre un String");
						break;
					}
					System.out.println("Entrez la quantite desiree : ");
					String quantite = lire();
					if (!userDAO.CreerLigneCommande(userDAO.getFile(), quantite, idCommande, idAlbum, format)) {
						System.out.println("Erreur dans la creation de la ligne de commande");
					}
					else{
						System.out.println("\nCreation de votre nouvelle ligne commande reussie");
					}

					break;
					
				case "1" :
					userDAO.AfficherListeLigneCommande(idCommande);
					break;

				case "2":
					System.out.println("Entrez l'identifiant de la ligne commande a supprimer : ");
					userDAO.AfficherListeLigneCommande(idCommande);
					System.out.println("\nVotre choix : ");
					int idLigneCommande = -1;
					try {
						idLigneCommande = lireInt();
						userDAO.RetirerLigneCommande(idLigneCommande, idCommande);
					} catch (NumberFormatException e) {
						System.out.println("Erreur de saisie");
						break;
					} catch (InputMismatchException e) {
						System.out.println("Erreur vous avez entre un String");
						break;
					}
					break;

				case "3":
					int prix = userDAO.ValiderCommande(userDAO.getFile(), idCommande);
					System.out.println("Votre commande est validee, pour un prix total de " + prix + " euros");
					break;

				case "4":
					break;

				default:
					System.out.println("Votre choix est incorrect");
					action = "0";
				}
			} catch (NoSuchElementException e) {
				System.out.println("Erreur saisie!");
				action = "0";
			}
		} while (action.equals("0") | action.equals("1"));

		Main.MenuPrincipal(userDAO);

	}

	private static String lire() throws NoSuchElementException {
		String aux = null;
		aux = sc.nextLine();
		if (aux.length() == 0) {
			throw new NoSuchElementException("longueur egale 0");

		}
		return aux;
	}

	private static int lireInt() {
		String lit = lire();
		int li = -1;
		if (lit != null) {
			li = Integer.parseInt(lit);
		}
		return li;

	}
}
