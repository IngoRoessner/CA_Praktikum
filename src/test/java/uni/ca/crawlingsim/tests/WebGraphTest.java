package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import uni.ca.crawlingsim.crawler.DoneSet;
import uni.ca.crawlingsim.data.webgraph.WebGraph;

public class WebGraphTest {

	@Test
	public void constructBySmallFile() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, Exception {
		String workingDirectory = System.getProperty("user.dir");
		String location = "/test_resources/webgraph_small.txt";
		assertTrue("no such file", Paths.get(workingDirectory, location).toFile().exists());
		WebGraph graph = new WebGraph(Paths.get(workingDirectory, location));
		DoneSet done = new DoneSet();
		List<String> fromA = graph.linksFrom("http://example.com/a", done);
		assertTrue(fromA.size() == 3);
		assertTrue(fromA.containsAll(Arrays.asList(
				"http://example.com/a", 
				"http://example.com/b", 
				"http://example.com/e"
		))); 

		List<String> fromB = graph.linksFrom("http://example.com/b", done);
		assertTrue(fromB.size() == 2);
		assertTrue(fromB.containsAll(Arrays.asList(
				"http://example.com/a", 
				"http://example.com/c" 
		))); 

		List<String> fromC = graph.linksFrom("http://example.com/c", done);
		assertTrue(fromC.size() == 2);
		assertTrue(fromC.containsAll(Arrays.asList(
				"http://example.com/a", 
				"http://example.com/d" 
		)));
		
		List<String> fromD = graph.linksFrom("http://example.com/d", done);
		assertTrue(fromD.size() == 1);
		assertTrue(fromD.containsAll(Arrays.asList(
				"http://example.com/e"
		)));
		
		List<String> fromE = graph.linksFrom("http://example.com/e", done);
		assertTrue(fromE.size() == 0);
	}

	@Ignore
	@Test
	public void prodFileRunable() throws Exception{
		String workingDirectory = System.getProperty("user.dir");
		String location = "/test_resources/prod_sample/linkgraph_ids.txt";
		assertTrue("no such file", Paths.get(workingDirectory, location).toFile().exists());
		WebGraph graph = new WebGraph(Paths.get(workingDirectory, location));
		foo(graph);
	}
	
	void foo(WebGraph graph){}
}
