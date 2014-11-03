package uni.ca.crawlingsim.crawler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import uni.ca.crawlingsim.quality.QualityInfo;
import uni.ca.crawlingsim.webgraph.WebGraph;

public class Crawler {
	private WebGraph webGraph;
	private QualityInfo quality;
	private StepQualityOut stepQualityOut;
	
	public static void main(String[] args) throws IOException{
		if(args.length!=5) System.out.println("Seed, Graph, Qualit�tsmapping, Crawlingschritte, Qualityausgabe");
		else{
			List<String> seed = Files.lines(Paths.get(args[0])).collect(Collectors.toList());
			Path graphFilePath = Paths.get(args[1]);
			Path qualityFilePath = Paths.get(args[2]);
			int takesPerStep = Integer.parseInt(args[3]);
			Path stepQualityOutPath = Paths.get(args[4]);
			Crawler crawler = new Crawler(graphFilePath, qualityFilePath, stepQualityOutPath);
			crawler.run(seed, takesPerStep);
		}
		
		
	}
	
	
	public Crawler(WebGraph webGraph, QualityInfo qualityInfo, StepQualityOut stepQualityOut){
		this.init(webGraph, qualityInfo, stepQualityOut);
	}
	
	public Crawler(Path graphFilePath, Path qualityFilePath, Path stepQualityOutPath) throws IOException{
		QualityInfo qinfo = new QualityInfo(qualityFilePath);	
		WebGraph graph = new WebGraph(graphFilePath);
		StepQualityOut out = new StepQualityOut(stepQualityOutPath, qinfo);
		this.init(graph, qinfo, out);
	}
	
	private void init(WebGraph webGraph, QualityInfo qualityInfo, StepQualityOut stepQualityOut) {
		this.webGraph = webGraph;
		this.quality = qualityInfo;
		this.stepQualityOut = stepQualityOut;
	}
	
	public void run(List<String> seed, int takesPerStep) throws IOException{
		this.stepQualityOut.open();
		PriorityQueue<String> urlQueue = new PriorityQueue<String>(seed);	
		Set<String> done = new HashSet<String>();
		while(!urlQueue.isEmpty()){
			for(int i = 0; i<takesPerStep && !urlQueue.isEmpty(); i++){
				//no direct add to urlQueue as preparation for additional strategies
				PriorityQueue<String> urlTakes = new PriorityQueue<String>();
				String url = urlQueue.poll();
				done.add(url);
				webGraph.linksFrom(url).stream()
					.filter(urlElement -> !done.contains(urlElement))
					.forEach(urlElement -> urlTakes.add(urlElement));						
				this.addUrlTakesToQueue(urlQueue, urlTakes);
			}
			this.stepQualityOut.printStepQuality(done);
		}
		this.stepQualityOut.close();
	}

	private void addUrlTakesToQueue(PriorityQueue<String> urlQueue, PriorityQueue<String> urlTakes) {
		urlQueue.addAll(urlTakes);
	}
}