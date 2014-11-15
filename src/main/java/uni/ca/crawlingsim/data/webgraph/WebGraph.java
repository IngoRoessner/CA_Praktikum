package uni.ca.crawlingsim.data.webgraph;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uni.ca.crawlingsim.data.Data;

public class WebGraph {
	private Data data;
	private static String tableName = "WebGraph";
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
		this.insertBuffer.add(new StringBuilder().append("('").append(from).append("','").append(to).append("')").toString());
		if(this.insertBuffer.size() == Data.insertBufferSize){
			this.flushInsertBuffer();
		}
	}

	private void flushInsertBuffer() throws SQLException {
		this.data.flushInsertBuffer(tableName, insertBuffer);
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
