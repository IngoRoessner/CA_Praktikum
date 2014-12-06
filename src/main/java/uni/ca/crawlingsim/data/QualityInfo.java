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

public class QualityInfo{
	
	public static String tableName = "QualityInfo";
	Data data;
	private List<List<String>> insertBuffer;
	
	//use existing quality info in db
	public QualityInfo() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		this.data = new Data();
	}
	
	//create new db table and parse quality info
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
	
	private void createIndex() throws SQLException {
		System.out.println(new Date().toString()+": create QualityInfo index...");
		Statement statement = data.createStatement();
		statement.execute("CREATE INDEX "+tableName+"_index ON "+tableName+"(url)");
		statement.close();
	}

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
	
	private void addToTable(String url, boolean quality) throws SQLException{
		this.insertBuffer.add(Arrays.asList(url, quality ? "TRUE" : "FALSE"));
		if(this.insertBuffer.size() == Data.insertBufferSize){
			this.flushInsertBuffer();
		}
	}

	private void flushInsertBuffer() throws SQLException {
		this.data.flushInsertBuffer(tableName, insertBuffer);
	}
	
	public void setQuality(List<Link> links) throws SQLException{
		if(!links.isEmpty()){			
			StringBuilder sb = new StringBuilder("");
			boolean firstLine = true;
			for (Link link : links)
			{
				if(!firstLine){
					sb.append(", ");
				}else{
					firstLine = false;
				}
				sb.append("'"); 
				sb.append(link.to); 
				sb.append("'"); 
			}
			
			Statement statement = data.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT url FROM "+tableName+" WHERE quality = TRUE AND url IN ("+sb.toString()+")");
			while (resultSet.next()) {
				final String url = resultSet.getString("url");
				links.forEach((link)->{
					if(link.to.equals(url)){
						link.quality = true;
					}
				});
			}
			resultSet.close();
			statement.close();
		}
	}

	public void close() throws SQLException {
		this.data.close();
	}
}
