package uni.ca.crawlingsim.data.quality;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class QualityInfo extends HashMap<String, Boolean> {
	
	public QualityInfo(Path qualityFilePath) throws IOException {
		super();
		//parse file and add entrys to map
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
	
	//filters given url set: returns a set where the given quality maches the quality mapping
	public Set<String> filterSet(Set<String> set, boolean quality){
		return set.stream().filter(element -> this.get(element)==quality).collect(Collectors.toSet());
	}

}
