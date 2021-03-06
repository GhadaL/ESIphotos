package fr.esisar.model;

import java.io.File;

public class Client {

	private String adresseMail;
	private String nom;
	private String prenom;
	private String password;
	private File fichier;
	private int idadresseL;
	private int idadresseF;

	public Client(String adresseMail, String nom, String prenom, String password) {
		this.adresseMail = adresseMail;
		this.nom = nom;
		this.prenom = prenom;
		PasswordAuthentication psw = new PasswordAuthentication(5);
		this.password = psw.hash(password.toCharArray());
		// creer fichier
		fichier = new File("log.txt");

	}

	public Client(String adresseMail, String password) {
		this.adresseMail = adresseMail;
		this.nom = "";
		this.prenom = "";
		this.password = password;
		// creer fichier
		fichier = new File("log.txt");
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public String getPassword() {
		return password;
	}

	public String getAdressMail() {
		return adresseMail;
	}

	public File getFile() {
		return fichier;
	}
	/*
	 * public int getAdresseL(){ return idadresseL; } public int getAdresseF(){
	 * return idadresseF; }
	 */
}
