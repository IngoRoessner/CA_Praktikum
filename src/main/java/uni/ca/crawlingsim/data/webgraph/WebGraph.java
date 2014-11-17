package uni.ca.crawlingsim.data.webgraph;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uni.ca.crawlingsim.crawler.DoneSet;
import uni.ca.crawlingsim.data.Data;
import uni.ca.crawlingsim.data.quality.QualityInfo;

public class WebGraph {
	private Data data;
	public static String tableName = "WebGraph";
	private List<String> insertBuffer;

	public WebGraph(Path graphFilePath) throws Exception {
		this.insertBuffer = new ArrayList<String>();
		this.data = new Data();
		this.createTable();
		final long[] count = new long[] {0L};
		// parse file and adds entrys to the edges map
		Files.lines(graphFilePath).forEach( line -> {
			count[0]++; 
			if(count[0] % 100000 == 0){
				System.out.println("WebGraph parsed: "+count[0]+" lines");
			}
			if(count[0] % 5000000 == 0){
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
	}
	
	private void addToTable(String from, String to) throws SQLException{
		this.insertBuffer.add(new StringBuilder().append("('").append(from).append("','").append(to).append("')").toString());
		if(this.insertBuffer.size() == Data.insertBufferSize){
			this.flushInsertBuffer();
		}
	}

	private void flushInsertBuffer() throws SQLException {
		this.data.flushInsertBuffer(tableName, insertBuffer);
	}
	
	public List<String> linksFrom(String url, DoneSet done) throws SQLException {
		List<String> urls = new ArrayList<String>();
		urls.add(url);
		return linksFrom(urls, done);
	}
	
	//Param done, to be shure that a done table exists
	public List<String> linksFrom(List<String> urls, DoneSet done) throws SQLException {
		List<String> result = new ArrayList<String>();	
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
				"SELECT to_url FROM "+tableName+
				" WHERE from_url IN ("+
				sb.toString()+
				") AND to_url NOT IN (SELECT url FROM "+DoneSet.tableName+")"
		);
		while (resultSet.next()) {
			result.add(resultSet.getString("to_url"));
		}
		resultSet.close();
		statement.close();
		return result;
	}
	
	public void close() throws SQLException {
		this.data.close();
	}

}
