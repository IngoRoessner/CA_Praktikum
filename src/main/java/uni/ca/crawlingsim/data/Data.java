package uni.ca.crawlingsim.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Data {
	private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	static {
		try {
			Class.forName(driver).newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public static int insertBufferSize = 999;
	private static String database = "jdbc:derby:crawlingsim_db";
	private static int openCount = 0;

	private Connection connection;
	
	public Data() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		openCount++;
		this.connection = DriverManager.getConnection(database + ";create=true");
		this.connection.setAutoCommit(false);
	}
	
	public void close() throws SQLException{
		this.commit();
		connection.close();
		openCount--;
		if(openCount==0){
			try {
				DriverManager.getConnection(database + ";shutdown=true");
			} catch (SQLException e) {}
			try {
				DriverManager.getConnection(database + ";drop=true");
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
	
	public void flushInsertBuffer(String tableName, List<List<String>> insertBuffer) throws SQLException {
		this.flushInsertBuffer(tableName, insertBuffer, true);
	}
	
	public void flushInsertBuffer(String tableName, List<List<String>> insertBuffer, boolean toClear) throws SQLException {
		if(insertBuffer.size() > 0){
			int valueCount = insertBuffer.get(0).size();
			String marcs = "";
			for(int i = 0; i<valueCount; i++){
				if(i>0){
					marcs = marcs + ",";
				}
				marcs = marcs + "?";
			}
			PreparedStatement statement = this.prepareStatement("INSERT INTO "+tableName+" VALUES ("+marcs+")");
			for (List<String> s : insertBuffer){
				for(int i = 0; i<valueCount && i<s.size(); i++){
					statement.setString(i+1, s.get(i));				
				}
				statement.addBatch();
			}
			statement.executeBatch();
			statement.close();
			if(toClear){
				insertBuffer.clear();				
			}
		}
	}
	
	public void commit() throws SQLException{
		this.connection.commit();
	}
}
