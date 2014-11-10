package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import uni.ca.crawlingsim.crawler.Crawler;

public class CrawlerTest {

	private void test(Crawler crawler, Path path, int i) throws IOException {
		List<String> seed = Files.lines(path).collect(Collectors.toList());
		crawler.run(seed, i);
	}

	@Test
	public void smallFile() throws IOException {
		String workingDirectory = System.getProperty("user.dir");
		String location;
		
		Path graphFilePath = Paths.get(workingDirectory, "/test_resources/webgraph_small.txt");
		Path qualityFilePath = Paths.get(workingDirectory, "/test_resources/quality_small.txt");
		Path stepQualityOutPath = Paths.get(workingDirectory, "/test_resources/quality_small_out.txt");
		
		Crawler crawler = new Crawler(graphFilePath, qualityFilePath, stepQualityOutPath);

		location = "/test_resources/seed_small_1.txt";
		assertTrue("no such file: "+location, Paths.get(workingDirectory, location).toFile().exists());
		for(int i=1; i<2; i++){
			this.test(crawler, Paths.get(workingDirectory, location), i);
		}
		
		location = "/test_resources/seed_small_2.txt";
		assertTrue("no such file: "+location, Paths.get(workingDirectory, location).toFile().exists());
		for(int i=1; i<3; i++){
			this.test(crawler, Paths.get(workingDirectory, location), i);
		}
		
		location = "/test_resources/seed_small_3.txt";
		assertTrue("no such file: "+location, Paths.get(workingDirectory, location).toFile().exists());
		for(int i=1; i<4; i++){
			this.test(crawler, Paths.get(workingDirectory, location), i);
		}
		
		location = "/test_resources/seed_small_4.txt";
		assertTrue("no such file: "+location, Paths.get(workingDirectory, location).toFile().exists());
		for(int i=1; i<5; i++){
			this.test(crawler, Paths.get(workingDirectory, location), i);
		}
		
		location = "/test_resources/seed_small_5.txt";
		assertTrue("no such file: "+location, Paths.get(workingDirectory, location).toFile().exists());
		for(int i=1; i<6; i++){
			this.test(crawler, Paths.get(workingDirectory, location), i);
		}
	}


}
