package uni.ca.crawlingsim.precompile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import uni.ca.crawlingsim.data.Data;

public class AvgBackLinkFinder {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Data data = new Data();
		Statement statement = data.createStatement();
		statement.execute("SELECT count(from_url) as links, count(DISTINCT to_url) as urls FROM WebGraph");
		ResultSet result =  statement.getResultSet();
		result.next();
		double links = result.getDouble("links");
		double urls = result.getDouble("urls");
		double avgBackLinks = links/urls;
		System.out.println("avg backlinks: " + avgBackLinks);
		System.out.println("links: " + links);
		System.out.println("urls: " + urls);
		result.close();
		statement.close();
		data.close();
	}

}
