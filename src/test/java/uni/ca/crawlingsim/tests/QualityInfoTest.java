package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;

import uni.ca.crawlingsim.data.QualityInfo;

public class QualityInfoTest {

	@Test
	public void constructBySmalFile() throws Exception {
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

	@Ignore
	@Test
	public void prodFileRunable() throws Exception{
		String workingDirectory = System.getProperty("user.dir");
		String location = "/test_resources/prod_sample/quality_mapping.txt";
		assertTrue("no such file", Paths.get(workingDirectory, location).toFile().exists());
		QualityInfo quality = new QualityInfo(Paths.get(workingDirectory, location));
		bla(quality);
	}
	
	void bla(QualityInfo a){}
}
