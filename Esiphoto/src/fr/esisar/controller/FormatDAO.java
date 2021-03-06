package fr.esisar.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.esisar.model.Format;

public class FormatDAO extends Format {

	public static boolean verifierPresenceFormat(Connection connect, String idFormat) {
		Statement statement;
		boolean res = false;
		ResultSet result = null;
		try {
			statement = connect.createStatement();
			String sentence = "SELECT idFormat FROM Format";
			result = statement.executeQuery(sentence);
			while (result.next()) {
				if (idFormat.equals(result.getString("idFormat"))) {
					res = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public static void AfficherListFormat(Connection connect) throws SQLException {
		Statement statement;
		boolean res = false;
		ResultSet result = null;
		statement = connect.createStatement();
		String sentence = "SELECT prixUnitaire FROM Format";
		result = statement.executeQuery(sentence);
		int i = 1;
		while (result.next()) {
			System.out.println("A" + i + " vaut " + result.getString("prixUnitaire") + "€");
			i++;
		}
	}

	public int getPrix(Connection connect, String idFormat) throws SQLException {
		Statement statement;
		boolean res = false;
		ResultSet result = null;
		int prix = 0;
		statement = connect.createStatement();
		String sentence = "SELECT idFormat, prixUnitaire FROM Format";
		result = statement.executeQuery(sentence);
		while (result.next()) {
			if (idFormat.equals(result.getString("idFormat"))) {
				prix = Integer.parseInt(result.getString("prixUnitaire"));
			}
		}
		return prix;
	}
}
