package uni.ca.crawlingsim.crawler;

import java.sql.SQLException;

import uni.ca.crawlingsim.data.Data;

public class DoneSet {
	private Data data;
	
	public DoneSet() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		this.data = new Data();
		this.createTable();
	}

	private void createTable() {
		// TODO Auto-generated method stub
		
	}

	public void add(String url) {
		// TODO Auto-generated method stub
		
	}

	public boolean contains(String urlElement) {
		// TODO Auto-generated method stub
		return false;
	}

	public void close() throws SQLException {
		this.data.close();
	}
	
	
}
