package MushroomAPIs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.postgis.Point;

public class ProcQueryMushrooms {

	public static void AddNewQueryResult(Connection connection, Point AddLocation, String AddDescription) {
		// SQL insert statement with NOW()
		String sqlInsert = "INSERT INTO querymushrooms (timestamp, features) VALUES (NOW(), ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
			// Set parameters
			String GeoJSON =  Utils.MakeGeoJSONString( AddLocation, AddDescription );
			preparedStatement.setObject(1, GeoJSON, Types.OTHER);

			System.out.println("Mushroom GeoJSON - " + GeoJSON + "added to the querymushrooms.");

			// Execute the SQL statement
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int ProcQuery(Connection connection ) {
		int FoundCount = 0;;
		
		try {
			String sqlQuery = "SELECT location, description FROM mushrooms";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					Point point = (Point) resultSet.getObject("location");
			        String description = resultSet.getString("description");
					AddNewQueryResult( connection, point, description);
					FoundCount++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return FoundCount;
	}

}
