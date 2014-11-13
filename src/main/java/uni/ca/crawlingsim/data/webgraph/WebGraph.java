package uni.ca.crawlingsim.data.webgraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import uni.ca.crawlingsim.data.Data;

public class WebGraph {
	private Data data;
	private static String tableName = "WebGraph";

	public WebGraph(Path graphFilePath) throws Exception {
		this.data = new Data();
		this.createTable();
		// parse file and adds entrys to the edges map
		Files.lines(graphFilePath).forEach( line -> {
			String[] pair = line.split("\t");
			if (pair.length != 2) {
				System.err.println("wrong syntax, ignore line: \"" + line + "\"");
			} else {
				try {
					this.addToTable(pair[0], pair[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void createTable() throws Exception {
		if (data.containsTable(tableName)){
			Statement statement = data.createStatement();
			statement.execute("drop table "+tableName);
			statement.close();
		}
		Statement statement = data.createStatement();
		statement.execute("create table "+tableName+" (from_url varchar(32) not null, to_url varchar(32))");
		statement.close();
	}
	
	private void addToTable(String from, String to) throws SQLException{
		PreparedStatement preparedStatement = data.prepareStatement("INSERT INTO "+tableName+" VALUES (?,?)");
		preparedStatement.setString(1, from);
		preparedStatement.setString(2, to);
		preparedStatement.execute();
		preparedStatement.close();
	}

	public List<String> linksFrom(String url) throws SQLException {
		List<String> result = new ArrayList<String>();
		PreparedStatement preparedStatement = data.prepareStatement("SELECT to_url FROM "+tableName+" WHERE from_url = ?");
		preparedStatement.setString(1, url);
		preparedStatement.execute();
		ResultSet resultSet = preparedStatement.getResultSet();
		while (resultSet.next()) {
			result.add(resultSet.getString(1));
		}
		resultSet.close();
		preparedStatement.close();
		return result;
	}
	
	public void close() throws SQLException {
		this.data.close();
	}

}
