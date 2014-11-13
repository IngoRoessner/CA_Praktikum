package uni.ca.crawlingsim.crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

public class StepQualityOut {
	private FileWriter writer;
	private Path stepQualityOutPath;
	private double goodQualityCount;
	private double totalCount;
	
	public StepQualityOut(Path stepQualityOutPath) {
		this.goodQualityCount = 0;
		this.totalCount = 0;
		this.stepQualityOutPath = stepQualityOutPath;
	}
	
	public void count(boolean good){
		if(good){
			this.goodQualityCount++;			
		}
		this.totalCount++;
	}

	public void open() throws IOException {
		writer = new FileWriter(this.stepQualityOutPath.toFile());
	}

	public void close() throws IOException {
		writer.close();
	}

	public void printStepQuality() throws IOException, SQLException {
		double qualityresult = this.goodQualityCount / this.totalCount;
		writer.write(Double.toString(qualityresult) + System.lineSeparator());
	}

}
