package uni.ca.crawlingsim.crawler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import uni.ca.crawlingsim.data.Data;
import uni.ca.crawlingsim.data.quality.QualityInfo.QualityResultElement;

public class DoneSet {
	private Data data;
	public static String tableName = "DoneSetTable";
	public DoneSet() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		this.data = new Data();
		this.createTable();
	}

	private void createTable() throws SQLException {
		if (data.containsTable(tableName)){
			Statement statement = data.createStatement();
			statement.execute("drop table "+tableName);
			statement.close();
		}
		Statement statement = data.createStatement();
		statement.execute("create table "+tableName+" (url varchar(64))");
		statement.close();
		data.commit();
	}

	public void add(String url) throws SQLException {
		this.add(Arrays.asList(url));
	}
	
	public void add(List<String> urls) throws SQLException {
		StringBuilder sb = new StringBuilder("");
		boolean firstLine = true;
		for (String s : urls)
		{
			if(!firstLine){
				sb.append(", ");
			}else{
				firstLine = false;
			}
			sb.append("('"); 
		    sb.append(s); 
		    sb.append("')"); 
		}
		Statement statement = data.createStatement();
		statement.execute("INSERT INTO "+tableName+" VALUES "+sb.toString());
		statement.close();
		data.commit();
	}

	public boolean contains(String url) throws SQLException {
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

	public List<String> filter(List<String> links) throws SQLException {
		if(links.size() == 0){
			return links;
		}
		StringBuilder sb = new StringBuilder("");
		boolean firstLine = true;
		for (String s : links)
		{
			if(!firstLine){
				sb.append(", ");
			}else{
				firstLine = false;
			}
			sb.append("'"); 
		    sb.append(s); 
		    sb.append("'"); 
		}
		HashSet<String> filter = new HashSet<String>();
		Statement statement = data.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT url FROM "+tableName+" WHERE url IN ("+sb.toString()+")");
		while (resultSet.next()) {
			filter.add(resultSet.getString("url"));
		}
		resultSet.close();
		statement.close();
		return links.stream().filter(link -> !filter.contains(link)).collect(Collectors.toList());
	}
	
	
}
