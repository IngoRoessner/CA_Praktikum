package uni.ca.crawlingsim.webgraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WebGraph {
	
	private HashMap<String, List<String>> edges;
	
	public WebGraph(Path graphFilePath) throws IOException {
		this.edges = new HashMap<String, List<String>>();
		Files.lines(graphFilePath).forEach(line -> {
			String[] pair = line.split("\t");
			if(pair.length != 2){
				System.err.println("wrong syntax, ignore line: \""+line+"\"");
			}else{
				if(!this.edges.containsKey(pair[0])){
					this.edges.put(pair[0], new LinkedList<String>());
				}
				if(!this.edges.containsKey(pair[1])){
					this.edges.put(pair[1], new LinkedList<String>());
				}
				List<String> links = this.edges.get(pair[0]);
				links.add(pair[1]);
			}
		});
	}

	public List<String> linksFrom(String url) {
		return edges.get(url);
	}

}
