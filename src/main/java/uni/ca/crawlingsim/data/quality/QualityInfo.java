package uni.ca.crawlingsim.data.quality;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uni.ca.crawlingsim.data.Data;

public class QualityInfo{
	
	private static String tableName = "QualityInfo";
	Data data;
	private List<String> insertBuffer;
	
	public QualityInfo(Path qualityFilePath) throws Exception {
		this.insertBuffer = new ArrayList<String>();
		this.data = new Data();
		this.createTable();
		final long[] count = new long[] {0L};
		//parse file and add entrys to map
		Files.lines(qualityFilePath).forEach(line -> {
			count[0]++; 
			if(count[0] % 100000 == 0){
				System.out.println("QualityInfo parsed: "+count[0]+" lines");
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
	}
	
	private void addToTable(String url, boolean quality) throws SQLException{
		this.insertBuffer.add(new StringBuilder().append("('").append(url).append("',").append(quality ? "TRUE" : "FALSE").append(")").toString());
		if(this.insertBuffer.size() == Data.insertBufferSize){
			this.flushInsertBuffer();
		}
	}

	private void flushInsertBuffer() throws SQLException {
		this.data.flushInsertBuffer(tableName, insertBuffer);
	}
	
	public boolean get(String url) throws SQLException{
		boolean result = false;
		PreparedStatement preparedStatement = data.prepareStatement("SELECT quality FROM "+tableName+" WHERE url = ?");
		preparedStatement.setString(1, url);
		preparedStatement.execute();
		ResultSet resultSet = preparedStatement.getResultSet();
		while (resultSet.next()) {
			result = resultSet.getBoolean(1);
		}
		resultSet.close();
		preparedStatement.close();
		return result;		
	}
	

	public void close() throws SQLException {
		this.data.close();
	}
}
