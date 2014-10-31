package unileipzig.ca.praktikum.crawlingsim.quality;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class QualityInfo extends HashMap<String, Boolean> {
	
	public QualityInfo(Path qualityFilePath) throws IOException {
		super();
		Files.lines(qualityFilePath).forEach(line -> {
			String[] pair = line.split("\t");
			if(pair.length != 2 || !(pair[1].equals("1") || pair[1].equals("0"))){
				System.err.println("wrong syntax, ignore line: \""+line+"\"");
			}else{
				if(pair[1].equals("1")){
					this.put(pair[0], true);
				}else{
					this.put(pair[0], false);
				}
			}
		});
	}

}
