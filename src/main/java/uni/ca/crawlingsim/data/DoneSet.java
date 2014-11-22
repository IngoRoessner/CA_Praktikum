package uni.ca.crawlingsim.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
	
	public void add(List<String> urls){
		this.splitList(urls, 999).forEach(urlSublist -> {
			StringBuilder sb = new StringBuilder("");
			boolean firstLine = true;
			for (String s : urlSublist)
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
			try {
				Statement statement;
				statement = data.createStatement();
				statement.execute("INSERT INTO "+tableName+" VALUES "+sb.toString());
				statement.close();
				data.commit();			
			} catch (Exception e) {
				System.err.println("error at DoneSet::add()");
			}
		});
	}
	
	private List<List<String>> splitList(List<String> list, int at){
		List<List<String>> result = new ArrayList<List<String>>();
		int start = 0;
		for(int i=0; i<list.size(); i++){
			int end = i+1;
			if(end % at == 0 || end == list.size()){
				result.add(list.subList(start, end));
				start = end;
			}
		}
		return result;
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
