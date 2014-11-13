package uni.ca.crawlingsim.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Data {
	private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private static String database = "jdbc:derby:crawlingsim";
	private static int openCount = 0;

	private Connection connection;
	
	public Data() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		openCount++;
		Class.forName(driver).newInstance();
		this.connection = DriverManager.getConnection(database + ";create=true");
	}
	
	public void close() throws SQLException{
		connection.close();
		openCount--;
		if(openCount==0){
			try {
				DriverManager.getConnection(database + ";drop=true");
			} catch (SQLException e) {}
			try {
				DriverManager.getConnection(database + ";shutdown=true");
			} catch (SQLException e) {}
		}
	}
	
	public boolean containsTable(String table) throws SQLException{
		ResultSet resultSet = connection.getMetaData().getTables(null, null, null,
				new String[] { "TABLE" });
		boolean result = false;
		while (resultSet.next() && !result) {
			if (resultSet.getString("TABLE_NAME").equalsIgnoreCase(table)) {
				result = true;
			}
		}
		resultSet.close();
		return result;
	}

	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}

	public PreparedStatement prepareStatement(String string) throws SQLException {
		return connection.prepareStatement(string);
	}
}
