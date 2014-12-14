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
 * Class QualityInfo, calculates the Quality Informations for every site/page
 * @author Ingo Rößner, Daniel Michalke
 *
 */
public class QualityInfo{
	
	public static String tableName = "QualityInfo";
	Data data;
	private List<List<String>> insertBuffer;
	
	/**
	 * Method QualityInfo, based on the existing qualityinfo in the database
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public QualityInfo() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		this.data = new Data();
	}
	
	
	/**
	 * Constructor QualityInfo, creates a new Database table and parses the qualityinfo from the given path file to the database
	 * @param qualityFilePath
	 * @throws Exception
	 */
	public QualityInfo(Path qualityFilePath) throws Exception {
		this.insertBuffer = new ArrayList<List<String>>();
		this.data = new Data();
		this.createTable();
		final long[] count = new long[] {0L};
		//parse file and add entrys to map
		Files.lines(qualityFilePath).forEach(line -> {
			count[0]++; 
			if(count[0] % 1000000 == 0){
				System.out.println("QualityInfo parsed: "+count[0]+" lines");
			}
			if(count[0] % 2000000 == 0){
				try {
					data.commit();
				} catch (Exception e) {
					System.err.println("Error on Commit");
				}
			}
			String[] pair = line.split(" ");
			if(pair.length != 2 || !(pair[1].equals("1") || pair[1].equals("0"))){
				System.err.println("quality: wrong syntax, ignore line: \""+line+"\"");
			}else{
				try {
					if(pair[1].equals("1")){
						this.addToTable(pair[0], true);
					}else{
						this.addToTable(pair[0], false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		this.flushInsertBuffer();
		data.commit();
		this.createIndex();
		data.commit();
	}
	/**
	 * Method createIndex, indexes the database tables
	 * @throws SQLException
	 */
	private void createIndex() throws SQLException {
		System.out.println(new Date().toString()+": create QualityInfo index...");
		Statement statement = data.createStatement();
		statement.execute("CREATE INDEX "+tableName+"_index ON "+tableName+"(url)");
		statement.close();
	}
	/**
	 * Method createTable, forms the database statement for the creationprozess
	 * @throws Exception
	 */
	private void createTable() throws Exception {
		if (data.containsTable(tableName)){
			Statement statement = data.createStatement();
			statement.execute("drop table "+tableName);
			statement.close();
		}
		Statement statement = data.createStatement();
		statement.execute("create table "+tableName+" (url varchar(64) not null, quality BOOLEAN)");
		statement.close();
		this.data.commit();
	}
	/**
	 * Method addtoTable, adds the Url and its quality to the table
	 * @param url
	 * @param quality
	 * @throws SQLException
	 */
	private void addToTable(String url, boolean quality) throws SQLException{
		this.insertBuffer.add(Arrays.asList(url, quality ? "TRUE" : "FALSE"));
		if(this.insertBuffer.size() == Data.insertBufferSize){
			this.flushInsertBuffer();
		}
	}
	/**
	 * Method flushinsertBuffer, calls flushinsertbuffer method from the data class
	 * @throws SQLException
	 */
	private void flushInsertBuffer() throws SQLException {
		this.data.flushInsertBuffer(tableName, insertBuffer);
	}
	/**
	 * Method setQuality, retrieves the quality for the url from the database
	 * @param url
	 * @return qualityvalue for the url(true or false)
	 * @throws SQLException
	 */
	public boolean setQuality(String url) throws SQLException{			
		PreparedStatement statement = data.prepareStatement("SELECT quality FROM "+tableName+" WHERE url = ?");
		statement.setString(1, url);
		statement.execute();
		ResultSet resultSet = statement.getResultSet();
		boolean result = false;
		if(resultSet.next()){
			result = resultSet.getBoolean("quality");		
		}
		resultSet.close();
		statement.close();
		return result;
	}
	/**
	 * Method close, calls the method close in Data
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		this.data.close();
	}
}
