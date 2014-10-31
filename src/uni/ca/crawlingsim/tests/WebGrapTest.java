package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import uni.ca.crawlingsim.webgraph.WebGraph;

public class WebGrapTest {

	@Test
	public void constructBySmalFile() throws IOException {
		String workingDirectory = System.getProperty("user.dir");
		String location = "/test_resources/webgraph_smal.txt";
		assertTrue("no such file", Paths.get(workingDirectory, location).toFile().exists());
		WebGraph graph = new WebGraph(Paths.get(workingDirectory, location));
		
		List<String> fromA = graph.linksFrom("http://example.com/a");
		assertTrue(fromA.size() == 3);
		assertTrue(fromA.containsAll(Arrays.asList(
				"http://example.com/a", 
				"http://example.com/b", 
				"http://example.com/e"
		))); 

		List<String> fromB = graph.linksFrom("http://example.com/b");
		assertTrue(fromB.size() == 2);
		assertTrue(fromB.containsAll(Arrays.asList(
				"http://example.com/a", 
				"http://example.com/c" 
		))); 

		List<String> fromC = graph.linksFrom("http://example.com/c");
		assertTrue(fromC.size() == 2);
		assertTrue(fromC.containsAll(Arrays.asList(
				"http://example.com/a", 
				"http://example.com/d" 
		)));
		
		List<String> fromD = graph.linksFrom("http://example.com/d");
		assertTrue(fromD.size() == 1);
		assertTrue(fromD.containsAll(Arrays.asList(
				"http://example.com/e"
		)));
		
		List<String> fromE = graph.linksFrom("http://example.com/e");
		assertTrue(fromE.size() == 0);
	}


}
