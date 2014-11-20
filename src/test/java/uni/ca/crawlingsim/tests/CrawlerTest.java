package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import uni.ca.crawlingsim.crawler.Crawler;

public class CrawlerTest {
	private static String workingDirectory = System.getProperty("user.dir");
	
	private void test(Path path, int i) throws Exception {
		Path graphFilePath = Paths.get(workingDirectory, "/test_resources/webgraph_small.txt");
		Path qualityFilePath = Paths.get(workingDirectory, "/test_resources/quality_small.txt");
		Path stepQualityOutPath = Paths.get(workingDirectory, "/test_resources/small_out.txt");
		
		Crawler crawler = new Crawler(graphFilePath, qualityFilePath, stepQualityOutPath);
		
		List<String> seed = Files.lines(path).collect(Collectors.toList());
		crawler.run(seed, i);
		
		Files.lines(stepQualityOutPath).forEach(line -> assertTrue(!line.contains("NaN")));
	}

	@Test
	public void smallFile() throws Exception {
		String location;
		location = "/test_resources/seed_small_1.txt";
		assertTrue("no such file: "+location, Paths.get(workingDirectory, location).toFile().exists());
		for(int i=1; i<2; i++){
			this.test(Paths.get(workingDirectory, location), i);
		}
		
		location = "/test_resources/seed_small_2.txt";
		assertTrue("no such file: "+location, Paths.get(workingDirectory, location).toFile().exists());
		for(int i=1; i<3; i++){
			this.test(Paths.get(workingDirectory, location), i);
		}
		
		location = "/test_resources/seed_small_3.txt";
		assertTrue("no such file: "+location, Paths.get(workingDirectory, location).toFile().exists());
		for(int i=1; i<4; i++){
			this.test(Paths.get(workingDirectory, location), i);
		}
		
		location = "/test_resources/seed_small_4.txt";
		assertTrue("no such file: "+location, Paths.get(workingDirectory, location).toFile().exists());
		for(int i=1; i<5; i++){
			this.test(Paths.get(workingDirectory, location), i);
		}
		
		location = "/test_resources/seed_small_5.txt";
		assertTrue("no such file: "+location, Paths.get(workingDirectory, location).toFile().exists());
		for(int i=1; i<6; i++){
		//	this.test(Paths.get(workingDirectory, location), i);
		}
	}


}
