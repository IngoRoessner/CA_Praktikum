package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import uni.ca.crawlingsim.precompile.AvgBackLinkFinder;

public class AvgBackLinkFinderTest {

	@Test
	public void test() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		AvgBackLinkFinder.main(null);
	}

}
