package MushroomAPIs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgresql.util.PGobject;

public class ProcAddMushrooms {

	public static boolean AddNewMushroom(Connection connection, Point AddLocation, String AddDescription) {

		boolean AddOk = false;

		String sqlInsert = "INSERT INTO mushrooms (timestamp, location, description) VALUES (NOW(), ?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
			PGgeometry pgGeometry = new PGgeometry(
					new Point("SRID=4326;POINT(" + AddLocation.getX() + " " + AddLocation.getY() + ")"));

			preparedStatement.setObject(1, pgGeometry, Types.OTHER);
			preparedStatement.setString(2, AddDescription);

			preparedStatement.executeUpdate();
			AddOk = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return AddOk;
	}

	public static boolean CheckIsNewInMushrooms(Connection connection, Point AddLocation, String AddDescription) {
		boolean IsNew = true;

		try {
			String sqlQuery = "SELECT location, description FROM mushrooms";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					String description = resultSet.getString("description");
					PGobject pgObject = (PGobject) resultSet.getObject("location");

					if (pgObject != null && pgObject.getType().equalsIgnoreCase("geometry")) {
						PGgeometry pgGeometry = new PGgeometry(pgObject.getValue());
						Point point = (Point) pgGeometry.getGeometry();
						if (AddLocation.x == point.x && AddLocation.y == point.y && AddDescription.equals(description))
							IsNew = false;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return IsNew;
	}

	public static int GetAddMushrooms(Connection connection, Timestamp LastModifiedTS) {

		int AddedCount = 0;

		String sqlQuery = "SELECT features->'geometry'->'coordinates' AS coordinates, "
				+ "features->'properties'->'description' AS description " + "FROM addmushrooms WHERE timestamp > ?";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setTimestamp(1, LastModifiedTS);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String coordinates = resultSet.getString("coordinates");
				String description = resultSet.getString("description");

				Point locationGeometry = Utils.GetPoinFromJSONLocation(coordinates);

				if (CheckIsNewInMushrooms(connection, locationGeometry, description)) {
					if (AddNewMushroom(connection, locationGeometry, description))
						AddedCount++;
				}
			}

			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return AddedCount;
	}
}
