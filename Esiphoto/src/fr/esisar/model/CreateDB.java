package fr.esisar.model;

import java.sql.*;

public class CreateDB {
	public static void main(String[] args) throws SQLException {
		Connection connection;
		CreateDB DB = new CreateDB();
		connection = DB.connect();

		//DB.DropAllTable(connection);
		DB.createAdresse(connection);
		DB.createClient(connection);
		DB.CreateFichierImage(connection);
		DB.CreateAlbum(connection);
		DB.CreateCommande(connection);
		DB.CreateFormat(connection);
		DB.CreateLigneCommande(connection);
		DB.CreateContient(connection);
		DB.ajoutDataFormat(connection);

		// DB.showContentTable(connection);

		DB.showAll(connection);
	}

	private void ajoutDataFormat(Connection conn) throws SQLException {

		// create a Statement from the connection
		Statement statement = conn.createStatement();

		// insert the data
		statement.executeUpdate("INSERT INTO FORMAT " + "VALUES ('A1','35')");
		statement.executeUpdate("INSERT INTO FORMAT " + "VALUES ('A2','30')");
		statement.executeUpdate("INSERT INTO FORMAT " + "VALUES ('A3','25')");
		statement.executeUpdate("INSERT INTO FORMAT " + "VALUES ('A4','20')");
		statement.executeUpdate("INSERT INTO FORMAT " + "VALUES ('A5','15')");
		statement.executeUpdate("INSERT INTO FORMAT " + "VALUES ('A6','10')");

	}
	// insert into sql
	// insert into Accounts (USER_NAME, ACTIVE, PASSWORD, USER_ROLE)
	// values ('employee1', 1, '123', 'EMPLOYEE');
	//

	private void showContentTable(Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		String sql = "SELECT nom FROM Client";
		ResultSet resu = statement.executeQuery(sql);
		while (resu.next()) {
			System.out.println(resu.getString("nom"));
		}
	}

	private void CreateContient(Connection connection) {
		Statement stmt;

		System.out.println("Creating table Contient...");
		try {
			stmt = connection.createStatement();
			String sql = "CREATE TABLE Contient " + "(numOrdre VARCHAR(255), " + "titre VARCHAR(255), "
					+ "commentaire VARCHAR(255), " + "cheminAcces VARCHAR(255), " + "idAlbum int, "
					+ "PRIMARY KEY (cheminAcces,idAlbum), "// MAAAAAAL
					+ "CONSTRAINT FK_ContientFichier FOREIGN KEY (cheminAcces) REFERENCES FICHIERIMAGE(cheminAcces))";

			stmt.executeUpdate(sql);
			System.out.println("Created table Contient...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void CreateAlbum(Connection connection) {
		Statement stmt;

		System.out.println("Creating table Album...");
		try {
			stmt = connection.createStatement();
			String sql = "CREATE TABLE Album " + "(idAlbum int not NULL, " + "titre VARCHAR(255), "
					+ "sousTitre VARCHAR(255), " + "adresseMail VARCHAR(255), " + "PRIMARY KEY (idAlbum), "
					+ "CONSTRAINT FK_ALBUMCLIENT FOREIGN KEY (adresseMail) REFERENCES CLIENT(adresseMail))";

			stmt.executeUpdate(sql);
			System.out.println("Created table Album...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void CreateFormat(Connection connection) {
		Statement stmt;

		System.out.println("Creating table Format...");
		try {
			stmt = connection.createStatement();
			String sql = "CREATE TABLE Format " + "(idFormat VARCHAR(25) not NULL, " + "prixUnitaire VARCHAR(25), "
					+ "PRIMARY KEY (idFormat))";

			stmt.executeUpdate(sql);
			System.out.println("Created table Format...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void CreateLigneCommande(Connection connection) {
		Statement stmt;

		System.out.println("Creating table LigneCommande...");
		try {
			stmt = connection.createStatement();
			String sql = "CREATE TABLE LigneCommande " + "(idLigneCommande int not NULL, " + "quantite VARCHAR(12), "
					+ "idCommande int, " + "idFormat VARCHAR(25), " + "idAlbum int, "
					+ "PRIMARY KEY (idLigneCommande), "
					// + "CONSTRAINT FK_LCommandeCommande FOREIGN KEY
					// (idCommande) REFERENCES COMMANDE(idCommande), "
					+ "CONSTRAINT FK_LCommandeFormat FOREIGN KEY (idFormat) REFERENCES FORMAT(idFormat), "
					+ "CONSTRAINT FK_LCommandeAlbum FOREIGN KEY (idAlbum) REFERENCES ALBUM(idAlbum))";

			stmt.executeUpdate(sql);
			System.out.println("Created table LigneCommande...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void CreateCommande(Connection connection) {
		Statement stmt;

		System.out.println("Creating table Commande...");
		try {
			stmt = connection.createStatement();
			String sql = "CREATE TABLE Commande " + "(idCommande int not NULL, " + "dateCommande VARCHAR(12), "
					+ "prixTotal VARCHAR(25), " + "adresseMail VARCHAR(255), " + "idLigneCommande int not NULL, "
					+ "PRIMARY KEY (idCommande), "
					+ "CONSTRAINT FK_COMMANDECLIENT FOREIGN KEY (adresseMail) REFERENCES CLIENT(adresseMail))";

			stmt.executeUpdate(sql);
			System.out.println("Created table Commande...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createClient(Connection connection) {
		Statement stmt;

		System.out.println("Creating table Client...");
		try {
			stmt = connection.createStatement();
			String sql = "CREATE TABLE Client " + "(adresseMail VARCHAR(255) not NULL, " + " nom VARCHAR(255), "
					+ " prenom VARCHAR(255), " + " password VARCHAR(255), " + "idAdresseL int ," + "idAdresseF int,"
					+ "PRIMARY KEY (adresseMail),"
					+ "CONSTRAINT FK_CLIENTADRESSEL FOREIGN KEY (idAdresseL) REFERENCES Adresse(idAdresse),"
					+ "CONSTRAINT FK_CLINETADRESSF FOREIGN KEY (idAdresseF) REFERENCES Adresse(idAdresse))";

			stmt.executeUpdate(sql);
			System.out.println("Created table Client...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createAdresse(Connection connection) {
		Statement stmt;

		System.out.println("Creating table Adresse...");
		try {
			stmt = connection.createStatement();
			String sql = "CREATE TABLE Adresse " + "(idAdresse int not NULL, " + " rue VARCHAR(255), "
					+ " codePostal VARCHAR(255), " + " ville VARCHAR(255), " + " PRIMARY KEY (idAdresse))";

			stmt.executeUpdate(sql);
			System.out.println("Created table Adresse...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// il faut creer client avant a cause de foreign key
	private void CreateFichierImage(Connection connection) {
		Statement stmt;

		System.out.println("Creating table FichierImage...");
		try {
			stmt = connection.createStatement();
			String sql = "CREATE TABLE FichierImage " + "(cheminAcces VARCHAR(255) not NULL, "
					+ "appareilPhoto VARCHAR(255), " + "objectif VARCHAR(255), " + "distanceFocale VARCHAR(255), "
					+ "sensibiliteISO VARCHAR(255), " + "ouverture VARCHAR(255), " + "vitesseObturation VARCHAR(255), "
					+ "adresseMail VARCHAR(2555), " + "PRIMARY KEY (cheminAcces), "
					+ "CONSTRAINT FK_FIMAGECLIENT FOREIGN KEY (adresseMail) REFERENCES Client(adresseMail))";

			stmt.executeUpdate(sql);
			System.out.println("Created table FichierImage...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Connection connect() throws SQLException {

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

	private void DropAllTable(Connection connection) throws SQLException {
		Statement stmt;
		int i;
		try {
			stmt = connection.createStatement();
			String sql[] = { "", "", "", "", "", "", "", "" };
			String print[] = { "", "", "", "", "", "", "", "" };
			sql[0] = "DROP TABLE Contient";
			print[0] = "Table Contient supprimee";
			sql[1] = "DROP TABLE LigneCommande";
			print[1] = "Table LigneCommande supprimee";
			sql[2] = "DROP TABLE Format";
			print[2] = "Table Format supprimee";
			sql[3] = "DROP TABLE Commande";
			print[3] = "Table Commande supprimee";
			sql[4] = "DROP TABLE Album";
			print[4] = "Table Album supprimee";
			sql[5] = "DROP TABLE FichierImage";
			print[5] = "Table FichierImage supprimee";
			sql[6] = "DROP TABLE Client";
			print[6] = "Table Client supprimee";
			sql[7] = "DROP TABLE Adresse";
			print[7] = "Table Adresse supprimee";

			for (i = 0; i < 8; i++) {
				stmt.execute(sql[i]);
				System.out.println(print[i]);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showAll(Connection connection) {
		Statement statement;
		try {
			statement = connection.createStatement();
			// statement.execute("CREATE TABLE ghada (id CHAR(4), idYx
			// CHAR(3))");
			System.out.println("Liste des tables :");
			int i = 0;
			ResultSet res;
			res = statement.executeQuery("SELECT owner , table_name FROM all_tables");
			while (res.next()) {
				i++;
				if (i > 43 && i < 52)
					System.out.println(res.getString("table_name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
