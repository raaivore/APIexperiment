package MushroomAPIs;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@WebServlet("/AddMushrooms")
public class AddMushrooms extends HttpServlet implements java.io.Serializable {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		final String DB_URL = "jdbc:postgresql://localhost:5432/mushroom_database";
		final String USER = "mushroomer";
		final String PASSWORD = "";

		Connection connection = null;
		Timestamp LastModifiedTS = Timestamp.valueOf("1970-01-01 00:00:00");

		try {
			connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

			// SQL päring vanima timestamp-i leidmiseks
			String query = "SELECT timestamp AS oldest_timestamp FROM mushrooms";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Timestamp oldest_timestamp = resultSet.getTimestamp("oldest_timestamp");
				if (oldest_timestamp.after(LastModifiedTS))
					LastModifiedTS = oldest_timestamp;
			}
			System.out.println("Vanim timestamp: " + LastModifiedTS);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		int count = ProcAddMushrooms.GetAddMushrooms(connection, LastModifiedTS);

		response.getWriter().println("Added " + count + " mushrooms");
	}
}