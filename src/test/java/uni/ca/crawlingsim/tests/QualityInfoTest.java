package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.data.QualityInfo;

public class QualityInfoTest {

	@Test
	public void constructBySmalFile() throws Exception {
		String workingDirectory = System.getProperty("user.dir");
		String location = "/test_resources/quality_small.txt";
		assertTrue("no such file", Paths.get(workingDirectory, location).toFile().exists());
		
		QualityInfo quality = new QualityInfo(Paths.get(workingDirectory, location));
		List<Link> links = Arrays.asList(
				new Link("", "http://example.com/a"), 
				new Link("", "http://example.com/c"),
				new Link("", "http://example.com/e"),
				new Link("", "http://example.com/b"),
				new Link("", "http://example.com/d")
		);
		quality.setQuality(links);
		links.forEach((link)->{
			switch (link.to){
				case "http://example.com/a":
					assertTrue(link.quality);
					break;
				case "http://example.com/c":
					assertTrue(link.quality);
					break;
				case "http://example.com/e":
					assertTrue(link.quality);
					break;
				case "http://example.com/b":
					assertFalse(link.quality);
					break;
				case "http://example.com/d":
					assertFalse(link.quality);
					break;
			}
		});
	}

}
