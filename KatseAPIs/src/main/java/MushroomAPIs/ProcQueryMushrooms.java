package MushroomAPIs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgresql.util.PGobject;

public class ProcQueryMushrooms {

	public static void AddNewQueryResult(Connection connection, Point AddLocation, String AddDescription) {
		String sqlInsert = "INSERT INTO querymushrooms (timestamp, features) VALUES (NOW(), ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
			String GeoJSON = Utils.MakeGeoJSONString(AddLocation, AddDescription);
			preparedStatement.setObject(1, GeoJSON, Types.OTHER);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int ProcQuery(Connection connection) {
		int FoundCount = 0;

		try {
			String sqlQuery = "SELECT location, description FROM mushrooms";

			try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					Point point = null;
					String description = resultSet.getString("description");
					PGobject pgObject = (PGobject) resultSet.getObject("location");

					if (pgObject != null && pgObject.getType().equalsIgnoreCase("geometry")) {
						PGgeometry pgGeometry = new PGgeometry(pgObject.getValue());
						point = (Point) pgGeometry.getGeometry();
					}

					if (point != null) {
						AddNewQueryResult(connection, point, description);
						FoundCount++;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return FoundCount;
	}

}
