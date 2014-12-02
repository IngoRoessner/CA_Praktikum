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
import java.util.Set;
import java.util.stream.Collectors;

public class DoneSet {
	Set<String> done;
	
	public DoneSet() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		this.done = new HashSet<String>();
	}

	public void add(String url) throws SQLException {
		this.add(Arrays.asList(url));
	}
	
	public void add(List<String> urls) throws SQLException{
		done.addAll(urls);
	}
	
	public boolean contains(String url) throws SQLException {
		return done.contains(url);
	}

	public void close() throws SQLException {

	}

	public List<String> filter(List<String> links) throws SQLException {
		return links.stream().filter(link -> !this.done.contains(link)).collect(Collectors.toList());
	}
	
	
}
