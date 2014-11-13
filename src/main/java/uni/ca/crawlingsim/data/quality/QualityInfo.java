package uni.ca.crawlingsim.data.quality;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.stream.Collectors;

import uni.ca.crawlingsim.data.Data;

public class QualityInfo{
	
	private static String tableName = "QualityInfo";
	Data data;
	
	public QualityInfo(Path qualityFilePath) throws Exception {
		this.data = new Data();
		this.createTable();
		//parse file and add entrys to map
		Files.lines(qualityFilePath).forEach(line -> {
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
	}
	
	private void createTable() throws Exception {
		if (data.containsTable(tableName)){
			Statement statement = data.createStatement();
			statement.execute("drop table "+tableName);
			statement.close();
		}
		Statement statement = data.createStatement();
		statement.execute("create table "+tableName+" (url varchar(32) not null, quality BOOLEAN)");
		statement.close();
	}
	
	private void addToTable(String url, boolean quality) throws SQLException{
		PreparedStatement preparedStatement = data.prepareStatement("INSERT INTO "+tableName+" VALUES (?,?)");
		preparedStatement.setString(1, url);
		preparedStatement.setBoolean(2, quality);
		preparedStatement.execute();
		preparedStatement.close();
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
	
	//filters given url set: returns a set where the given quality maches the quality mapping
	public Set<String> filterSet(Set<String> set, boolean quality) throws SQLException{
		return set.stream().filter(element -> {
			boolean result = false;
			try{
				result = this.get(element)==quality;
			}catch(Exception e){}
			return result;
		}).collect(Collectors.toSet());
	}

	public void close() throws SQLException {
		this.data.close();
	}
}
