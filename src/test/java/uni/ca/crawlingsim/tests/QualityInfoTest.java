package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import uni.ca.crawlingsim.quality.QualityInfo;

public class QualityInfoTest {

	@Test
	public void constructBySmalFile() throws IOException {
		String workingDirectory = System.getProperty("user.dir");
		String location = "/test_resources/quality_small.txt";
		assertTrue("no such file", Paths.get(workingDirectory, location).toFile().exists());
		
		QualityInfo quality = new QualityInfo(Paths.get(workingDirectory, location));
		
		assertTrue(quality.get("http://example.com/a"));
		assertTrue(quality.get("http://example.com/c"));
		assertTrue(quality.get("http://example.com/e"));
		
		assertTrue(!quality.get("http://example.com/b"));
		assertTrue(!quality.get("http://example.com/d"));
	}

}
