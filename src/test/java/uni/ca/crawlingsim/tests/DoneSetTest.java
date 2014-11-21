package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	@Test
	public void test2() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		
		List<String> testUrls = new ArrayList<String>();
		for(int i=1; i<2000; i++)
		{
			testUrls.add(String.valueOf(i));
			if(i==998)
			{
				DoneSet done = new DoneSet();
				done.add(testUrls);
				assertTrue(done.contains(String.valueOf(1)));
				assertTrue(done.contains(String.valueOf(998)));
				assertTrue(!done.contains(String.valueOf(999)));
				assertTrue(!done.contains(String.valueOf(1000)));
				assertTrue(!done.contains(String.valueOf(2000)));
				done.close();
			}
			if(i==999)
			{
				DoneSet done = new DoneSet();
				done.add(testUrls);
				assertTrue(done.contains(String.valueOf(1)));
				assertTrue(done.contains(String.valueOf(998)));
				assertTrue(done.contains(String.valueOf(999)));
				assertTrue(!done.contains(String.valueOf(1000)));
				assertTrue(!done.contains(String.valueOf(2000)));
				done.close();
			}
			if(i==1000)
			{
				DoneSet done = new DoneSet();
				done.add(testUrls);
				assertTrue(done.contains(String.valueOf(1)));
				assertTrue(done.contains(String.valueOf(998)));
				assertTrue(done.contains(String.valueOf(999)));
				assertTrue(done.contains(String.valueOf(1000)));
				assertTrue(!done.contains(String.valueOf(1001)));
				assertTrue(!done.contains(String.valueOf(2000)));
				done.close();
			}
			if(i==2000)
			{
				DoneSet done = new DoneSet();
				done.add(testUrls);
				assertTrue(done.contains(String.valueOf(1)));
				assertTrue(done.contains(String.valueOf(998)));
				assertTrue(done.contains(String.valueOf(999)));
				assertTrue(done.contains(String.valueOf(1000)));
				assertTrue(done.contains(String.valueOf(1001)));
				assertTrue(done.contains(String.valueOf(2000)));
				done.close();
			}
		}
		
	}
	
}
