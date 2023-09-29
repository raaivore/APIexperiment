package MushroomAPIs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Clean")
public class Clean extends HttpServlet implements java.io.Serializable {
	private static final String DB_URL = "jdbc:postgresql://localhost:5432/mushroom_database";
	private static final String USER = "mushroomer";
	private static final String PASSWORD = "";

	private static void deleteAllRecordsFromTable(Connection connection, String tableName) {
		try {
			// Loome SQL päringu kõikide kirjete kustutamiseks tabelist
			String deleteQuery = "DELETE FROM " + tableName;

			// Loome PreparedStatement objekti
			PreparedStatement statement = connection.prepareStatement(deleteQuery);

			// Käivitame kustutamise päringu
			statement.executeUpdate(deleteQuery);

			System.out.println("Kõik kirjed on kustutatud tabelist: " + tableName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void ProcClean(Connection connection) {
		deleteAllRecordsFromTable(connection, "addmushrooms");
		deleteAllRecordsFromTable(connection, "deletemushrooms");
		deleteAllRecordsFromTable(connection, "updatemushrooms");
		deleteAllRecordsFromTable(connection, "querymushrooms");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		boolean Ok = false;

		try {
			Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			ProcClean(connection);
			Ok = true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (Ok)
			response.getWriter().println("Ok");
	}
}
