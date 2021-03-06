package fr.esisar.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FichierImage {
	private String cheminAcces;
	private String appareilPhoto;
	private String objectif;
	private String distanceFocal;
	private String sensibiliteISO;
	private String ouverture;
	private String vitesseOburation;
	private String client;

	public FichierImage() {
	}

	public FichierImage(String cheminAcces, String appareilPhoto, String objectif, String distanceFocal,
			String sensibiliteISO, String ouverture, String vitesseOburation, String client) {
		super();
		this.cheminAcces = cheminAcces;
		this.appareilPhoto = appareilPhoto;
		this.objectif = objectif;
		this.distanceFocal = distanceFocal;
		this.sensibiliteISO = sensibiliteISO;
		this.ouverture = ouverture;
		this.vitesseOburation = vitesseOburation;
		this.client = client;
	}

	public FichierImage(String idClient) {
		this.client = idClient;
	}

	public String getCheminAcces() {
		return cheminAcces;
	}

	public void setCheminAcces(String cheminAcces) {
		this.cheminAcces = cheminAcces;
	}

	public String getAppareilPhoto() {
		return appareilPhoto;
	}

	public void setAppareilPhoto(String appareilPhoto) {
		this.appareilPhoto = appareilPhoto;
	}

	public String getObjectif() {
		return objectif;
	}

	public void setObjectif(String objectif) {
		this.objectif = objectif;
	}

	public String getDistanceFocal() {
		return distanceFocal;
	}

	public void setDistanceFocal(String distanceFocal) {
		this.distanceFocal = distanceFocal;
	}

	public String getSensibiliteISO() {
		return sensibiliteISO;
	}

	public void setSensibiliteISO(String sensibiliteISO) {
		this.sensibiliteISO = sensibiliteISO;
	}

	public String getOuverture() {
		return ouverture;
	}

	public void setOuverture(String ouverture) {
		this.ouverture = ouverture;
	}

	public String getVitesseOburation() {
		return vitesseOburation;
	}

	public void setVitesseOburation(String vitesseOburation) {
		this.vitesseOburation = vitesseOburation;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "FichierImage [cheminAcces=" + cheminAcces + ", appareilPhoto=" + appareilPhoto + ", objectif="
				+ objectif + ", distanceFocal=" + distanceFocal + ", sensibiliteISO=" + sensibiliteISO + ", ouverture="
				+ ouverture + ", vitesseOburation=" + vitesseOburation + ", client=" + client + "]";
	}

}
