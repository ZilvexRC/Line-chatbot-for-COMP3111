package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		String s = null;
		try {
			PreparedStatement stmt = getConnection().prepareStatement("SELECT * from lab3Table where question = ?");
			stmt.setString(1, text); 
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				s = rs.getString("answer");
				System.out.println(s);
			}
			rs.close();
			stmt.close();
			getConnection().close();
		} catch (Exception e) {
			System.out.println(e);
		}
		if (s != null)
			return s;
		return null;
	}


	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);

		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
