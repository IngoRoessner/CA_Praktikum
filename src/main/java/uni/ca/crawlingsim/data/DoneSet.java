package uni.ca.crawlingsim.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
		this.createIndex();
		data.commit();
	}
	
	private void createIndex() throws SQLException {
		Statement statement = data.createStatement();
		statement.execute("CREATE INDEX "+tableName+"_index ON "+tableName+"(url)");
		statement.close();
	}

	public void add(String url) throws SQLException {
		this.add(Arrays.asList(url));
	}
	
	public void add(List<String> urls) throws SQLException{
		this.splitList(urls, Data.insertBufferSize).forEach(urlSublist -> {
			try {
				data.flushInsertBuffer(tableName, urlSublist, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		data.commit();
	}
	
	private List<List<List<String>>> splitList(List<String> list, int at){
		List<List<List<String>>> result = new ArrayList<List<List<String>>>();
		List<List<String>> sublist = new ArrayList<List<String>>();
		for(int i=0; i<list.size(); i++){
			sublist.add(Arrays.asList(list.get(i)));
			if(i%Data.insertBufferSize == 0){
				result.add(sublist);
			}
		}
		if(sublist.size() > 0){
			result.add(sublist);
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
		//System.out.println(new Date().toString() + ": build sb...");
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
		//System.out.println(new Date().toString() + ": sb to string...");
		String sbString = sb.toString();
		HashSet<String> filter = new HashSet<String>();
		Statement statement = data.createStatement();
		//System.out.println(new Date().toString() + ": execute statement...");
		ResultSet resultSet = statement.executeQuery("SELECT url FROM "+tableName+" WHERE url IN ("+sbString+")");
		//System.out.println(new Date().toString() + ": read statement results...");
		while (resultSet.next()) {
			filter.add(resultSet.getString("url"));
		}
		//System.out.println(new Date().toString() + ": close...");
		resultSet.close();
		statement.close();
		//System.out.println(new Date().toString() + ": remove used links...");
		return links.stream().filter(link -> !filter.contains(link)).collect(Collectors.toList());
	}
	
	
}
