package MushroomAPIs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

	private static int deleteAllRecordsFromTable(Connection connection, String tableName) {

		int affectedRows = 0;

		try {
			String sql = "DELETE FROM " + tableName;
			Statement statement = connection.createStatement();
			affectedRows = statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return affectedRows;
	}

	public static int ProcClean(Connection connection) {
		int deletede_rows = 0;
		deletede_rows = deletede_rows + deleteAllRecordsFromTable(connection, "addmushrooms");
		deletede_rows = deletede_rows + deleteAllRecordsFromTable(connection, "deletemushrooms");
		deletede_rows = deletede_rows + deleteAllRecordsFromTable(connection, "updatemushrooms");
		deletede_rows = deletede_rows + deleteAllRecordsFromTable(connection, "querymushrooms");
		return deletede_rows;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int deleted_rows = 0;

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			deleted_rows = ProcClean(connection);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (connection == null) {
			response.getWriter().println("Ühendust andmebaasiga ei õnnestunud luua");
			return;
		}

		response.getWriter().println("Deleted " + deleted_rows + " rows");
	}
}
