package MushroomAPIs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProcDeleteMushrooms {

    private static boolean deleteRecordFromMushrooms(Connection connection, int MushroomId ) {
    	boolean Deleted = false;
        try {
            String deleteQuery = "DELETE FROM mushrooms WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, MushroomId);

            if( statement.executeUpdate() > 0)
            	Deleted = true;;
            
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return Deleted;
    }
    
	public static int GetDeleteMushrooms(Connection connection, Timestamp LastModifiedTS ) {
		
		int DeleteCount = 0;
        
         String sqlQuery = "SELECT mushroomid FROM deletemushrooms WHERE timestamp > ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setTimestamp(1, LastModifiedTS); 

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int mushroomId = resultSet.getInt("mushroomid");
                System.out.println("Kusttame Mushroom ID: " + mushroomId);
                if( deleteRecordFromMushrooms( connection, mushroomId ) )
                	DeleteCount++;
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return DeleteCount;
	}
}
