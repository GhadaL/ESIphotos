package fr.esisar.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.esisar.model.Adresse;

public class AdresseDAO extends Adresse {

	public AdresseDAO(int idAdresse, String rue, String codePostal, String ville) {
		super(idAdresse, rue, codePostal, ville);
	}

	public boolean addAddresse(Connection con) throws SQLException, ClassNotFoundException {
		boolean res = true;
		Statement statement;
		ResultSet result = null;
		statement = con.createStatement();
		String sentence = "SELECT idAdresse FROM Adresse";
		result = statement.executeQuery(sentence);
		while (result.next()) {
			if (super.getIdAdresse() == Integer.parseInt(result.getString("idAdresse"))) {
				res = false;
			}
		}
		if (res == true) {
			String quer1 = "INSERT INTO Adresse (idAdresse, rue, codePostal, ville) ";
			String quer2 = "VALUES (" + super.getIdAdresse() + ", '" + super.getRue() + "', '" + super.getCodePostal()
					+ "', '" + super.getVille() + "')";
			result = statement.executeQuery(quer1 + quer2);
		}
		return res;
	}

}
