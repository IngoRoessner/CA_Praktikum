package uni.ca.crawlingsim.crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import uni.ca.crawlingsim.quality.QualityInfo;

public class StepQualityOut {
	private FileWriter writer;
	private QualityInfo quality;
	private Path stepQualityOutPath;

	public StepQualityOut(Path stepQualityOutPath, QualityInfo quality) {
		this.quality = quality;
		this.stepQualityOutPath = stepQualityOutPath;
	}

	public void open() throws IOException {
		writer = new FileWriter(this.stepQualityOutPath.toFile());
	}

	public void close() throws IOException {
		writer.close();
	}

	public void printStepQuality(Set<String> done) throws IOException {
		double goodCrawl = this.quality.filterSet(done, true).size();
		double totalCrawl = done.size();
		writer.write(Double.toString(goodCrawl / totalCrawl) + System.lineSeparator());
	}

}
