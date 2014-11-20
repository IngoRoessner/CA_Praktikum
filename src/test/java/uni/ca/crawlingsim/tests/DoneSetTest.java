package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import uni.ca.crawlingsim.data.DoneSet;

public class DoneSetTest {

	@Test
	public void test() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		DoneSet done = new DoneSet();
		done.add("a");
		done.add("b");
		assertTrue(done.contains("a"));
		assertTrue(done.contains("b"));
		assertTrue(!done.contains("c"));
	}

}
