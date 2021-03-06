package uni.ca.crawlingsim.data;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Class WebGraph, creates the webgraph based on the file
 * @author Ingo R��ner, Daniel Michalke
 *	
 */
public class WebGraph {
	private Data data;
	public static String tableName = "WebGraph";
	private List<List<String>> insertBuffer;

	/**
	 * Constructor WebGraph, using the existing values from the database
	 * @throws Exception
	 */
	public WebGraph() throws Exception{
		this.data = new Data();
	}

	/**
	 * Constructor Webgraph, creates a new database table and parses all values from the file
	 * @param graphFilePath
	 * @throws Exception
	 */
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
	
	/**
	 * Method createIndex, creates index for the database insertion
	 * @throws SQLException
	 */
	private void createIndex() throws SQLException {
		System.out.println(new Date().toString()+": create WebGraph index...");
		Statement statement = data.createStatement();
		statement.execute("CREATE INDEX "+tableName+"_index ON "+tableName+"(from_url)");
		statement.close();
	}
	
	/**
	 * Method createTable, creates database table
	 * @throws Exception
	 */
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
	
	/**
	 * Method addToTable, adds every string between the from and to paramter in the List to the insertBuffer
	 * @param from
	 * @param to
	 * @throws SQLException
	 */
	private void addToTable(String from, String to) throws SQLException{
		this.insertBuffer.add(Arrays.asList(from, to));
		if(this.insertBuffer.size() == Data.insertBufferSize){
			this.flushInsertBuffer();
		}
	}
	
	/**
	 * Method flushInsertBuffer, calls the flushInsertBuffer method in the data class
	 * @throws SQLException
	 */
	private void flushInsertBuffer() throws SQLException {
		this.data.flushInsertBuffer(tableName, insertBuffer);
	}
	
	/**
	 * Method linksFrom, gets url and looks in the database for links from the base url to others. gives back a list of links
	 * @param url
	 * @return list of links (to_urls) that lead from the base url to others
	 * @throws SQLException
	 */
	public List<Link> linksFrom(String url) throws SQLException {
		List<Link> result = new ArrayList<Link>();	
		PreparedStatement statement = data.prepareStatement(
				"SELECT from_url, to_url FROM "+tableName+
				" WHERE from_url = ?"
		);
		statement.setString(1, url);
		statement.execute();
		ResultSet resultSet = statement.getResultSet();
		while (resultSet.next()) {
			Link link = new Link(resultSet.getString("from_url"), resultSet.getString("to_url"));
			result.add(link);
		}
		resultSet.close();
		statement.close();
		return result;
	}
	
	/**
	 * Method Close, closes the database connection
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		this.data.close();
	}

}
