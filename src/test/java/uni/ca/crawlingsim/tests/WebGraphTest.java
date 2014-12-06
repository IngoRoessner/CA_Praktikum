package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import uni.ca.crawlingsim.data.WebGraph;

public class WebGraphTest {

	@Test
	public void constructBySmallFile() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, Exception {
		String workingDirectory = System.getProperty("user.dir");
		String location = "/test_resources/webgraph_small.txt";
		assertTrue("no such file", Paths.get(workingDirectory, location).toFile().exists());
		WebGraph graph = new WebGraph(Paths.get(workingDirectory, location));
		
		List<String> fromA = graph.linksFrom("http://example.com/a").stream().map((link)->{return link.to;}).collect(Collectors.toList());
		assertTrue(fromA.size() == 3);
		assertTrue(fromA.containsAll(Arrays.asList(
				"http://example.com/a", 
				"http://example.com/b", 
				"http://example.com/e"
		))); 

		List<String> fromB = graph.linksFrom("http://example.com/b").stream().map((link)->{return link.to;}).collect(Collectors.toList());
		assertTrue(fromB.size() == 2);
		assertTrue(fromB.containsAll(Arrays.asList(
				"http://example.com/a", 
				"http://example.com/c" 
		))); 

		List<String> fromC = graph.linksFrom("http://example.com/c").stream().map((link)->{return link.to;}).collect(Collectors.toList());
		assertTrue(fromC.size() == 2);
		assertTrue(fromC.containsAll(Arrays.asList(
				"http://example.com/a", 
				"http://example.com/d" 
		)));
		
		List<String> fromD = graph.linksFrom("http://example.com/d").stream().map((link)->{return link.to;}).collect(Collectors.toList());
		assertTrue(fromD.size() == 1);
		assertTrue(fromD.containsAll(Arrays.asList(
				"http://example.com/e"
		)));
		
		List<String> fromE = graph.linksFrom("http://example.com/e").stream().map((link)->{return link.to;}).collect(Collectors.toList());
		assertTrue(fromE.size() == 0);
	}
	
}
