package uni.ca.crawlingsim.data;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WebGraph {
	private Data data;
	public static String tableName = "WebGraph";
	private List<List<String>> insertBuffer;

	//use existing infos from db
	public WebGraph() throws Exception{
		this.data = new Data();
	}
	
	//create new db table and parse infos from file
	public WebGraph(Path graphFilePath) throws Exception {
		this.insertBuffer = new ArrayList<List<String>>();
		this.data = new Data();
		this.createTable();
		final long[] count = new long[] {0L};
		// parse file and adds entrys to the edges map
		Files.lines(graphFilePath).forEach( line -> {
			count[0]++; 
			if(count[0] % 1000000 == 0){
				System.out.println("WebGraph parsed: "+count[0]+" lines");
			}
			if(count[0] % 2000000 == 0){
				try {
					data.commit();
				} catch (Exception e) {
					System.err.println("Error on Commit");
				}
			}
			
			String[] pair = line.split("\t");
			if (pair.length != 2) {
				System.err.println("webgraph: wrong syntax, ignore line: \"" + line + "\"");
			} else {
				try {
					this.addToTable(pair[0], pair[1]);
				} catch (Exception e) {}
			}
		});
		this.flushInsertBuffer();
		data.commit();
		this.createIndex();
		data.commit();
	}

	private void createIndex() throws SQLException {
		System.out.println(new Date().toString()+": create WebGraph index...");
		Statement statement = data.createStatement();
		statement.execute("CREATE INDEX "+tableName+"_index ON "+tableName+"(from_url)");
		statement.close();
	}

	private void createTable() throws Exception {
		if (data.containsTable(tableName)){
			Statement statement = data.createStatement();
			statement.execute("drop table "+tableName);
			statement.close();
		}
		Statement statement = data.createStatement();
		statement.execute("create table "+tableName+" (from_url varchar(64) not null, to_url varchar(64))");
		statement.close();
		this.data.commit();
	}
	
	private void addToTable(String from, String to) throws SQLException{
		this.insertBuffer.add(Arrays.asList(from, to));
		if(this.insertBuffer.size() == Data.insertBufferSize){
			this.flushInsertBuffer();
		}
	}

	private void flushInsertBuffer() throws SQLException {
		this.data.flushInsertBuffer(tableName, insertBuffer);
	}
	
	public List<Link> linksFrom(String url) throws SQLException {
		List<String> urls = new ArrayList<String>();
		urls.add(url);
		return linksFrom(urls);
	}
	
	public List<Link> linksFrom(List<String> urls) throws SQLException {
		List<Link> result = new ArrayList<Link>();	
		StringBuilder sb = new StringBuilder("");
		boolean firstLine = true;
		for (String s : urls)
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
		Statement statement = data.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT from_url, to_url FROM "+tableName+
				" WHERE from_url IN ("+sb.toString()+")"
		);
		while (resultSet.next()) {
			Link link = new Link(resultSet.getString("from_url"), resultSet.getString("to_url"));
			result.add(link);
		}
		resultSet.close();
		statement.close();
		return result;
	}
	
	public void close() throws SQLException {
		this.data.close();
	}

}
