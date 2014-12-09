package uni.ca.crawlingsim.crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import uni.ca.crawlingsim.data.Link;
/**
 * Class StepQualityOut, Filewriter class which writes the stepquality in a file
 * @author Ingo Rößner, Daniel Michalke
 * 
 */
public class StepQualityOut {
	private FileWriter writer;
	private Path stepQualityOutPath;
	private double goodQualityCount;
	private double totalCount;
	/**
	 * Constructor, gets the Path for the file
	 * @param stepQualityOutPath Path where the File will be safed
	 */
	public StepQualityOut(Path stepQualityOutPath) {
		this.goodQualityCount = 0;
		this.totalCount = 0;
		this.stepQualityOutPath = stepQualityOutPath;
	}
	/**
	 * Method count, counts the good sides with good qualitys
	 * @param good
	 */
	public void count(boolean good){
		if(good){
			this.goodQualityCount++;			
		}
		this.totalCount++;
	}
	/**
	 * Method Open, creates a new Filewriter and opens it
	 * @throws IOException
	 */
	public void open() throws IOException {
		writer = new FileWriter(this.stepQualityOutPath.toFile());
	}
	/**
	 * Method close, closes the Filewriter, opened by Method open
	 * @throws IOException
	 */
	public void close() throws IOException {
		writer.close();
	}
	/**
	 * Method printStepQuality, the actual writing prozess in the file
	 * @throws IOException
	 * @throws SQLException
	 */
	public void printStepQuality() throws IOException, SQLException {
		double qualityresult = this.goodQualityCount / this.totalCount;
		writer.write(Double.toString(qualityresult) + System.lineSeparator());
	}

}
