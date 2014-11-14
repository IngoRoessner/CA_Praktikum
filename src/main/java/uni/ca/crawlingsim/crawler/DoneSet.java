package uni.ca.crawlingsim.crawler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import uni.ca.crawlingsim.data.Data;

public class DoneSet {
	private Data data;
	private static String tableName = "DoneSetTable";
	public DoneSet() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		this.data = new Data();
		this.createTable();
	}

	private void createTable() throws SQLException {
		// TODO Auto-generated method stub
		if (data.containsTable(tableName)){
			Statement statement = data.createStatement();
			statement.execute("drop table "+tableName);
			statement.close();
		}
		Statement statement = data.createStatement();
		statement.execute("create table "+tableName+" (url varchar(32))");
		statement.close();
	}

	public void add(String url) throws SQLException {
		PreparedStatement preparedStatement = data.prepareStatement("INSERT INTO "+tableName+" VALUES (?)");
		preparedStatement.setString(1, url);
		preparedStatement.execute();
		preparedStatement.close();
		// TODO Auto-generated method stub
	}

	public boolean contains(String url) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement preparedStatement = data.prepareStatement("SELECT * FROM "+tableName+" WHERE url = ?");
		preparedStatement.setString(1, url);
		preparedStatement.execute();
		ResultSet resultSet = preparedStatement.getResultSet();
		boolean result = resultSet.next();
		resultSet.close();
		preparedStatement.close();
		return result;
	}

	public void close() throws SQLException {
		this.data.close();
	}
	
	
}
