package unileipzig.ca.praktikum.crawlingsim.crawler;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import unileipzig.ca.praktikum.crawlingsim.quality.QualityInfo;
import unileipzig.ca.praktikum.crawlingsim.webgraph.WebGraph;

public class Crawler {
	private WebGraph webGraph;
	private QualityInfo quality;
	private StepQualityOut stepQualityOut;
	
	static void commandLineCall(String[] args){
		
	}
	
	public Crawler(WebGraph webGraph, QualityInfo qualityInfo, StepQualityOut stepQualityOut){
		this.init(webGraph, qualityInfo, stepQualityOut);
	}
	
	public Crawler(Path graphFilePath, Path qualityFilePath, Path stepQualityOutPath){
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
	
	public void runParallel(List<String> seed, int takesPerStep){
		this.stepQualityOut.open();
		ConcurrentHashMap<String, Boolean> done = new ConcurrentHashMap<String, Boolean>();
		PriorityQueue<String> urlQueue = new PriorityQueue<String>(seed);	
		while(!urlQueue.isEmpty()){
			urlQueue.parallelStream()
				.limit(takesPerStep)
				.forEachOrdered(url->{		
					PriorityQueue<String> urlTakes = new PriorityQueue<String>();
					webGraph.get(url).stream()
						.filter(urlElement -> !done.containsKey(urlElement))
						.forEach(urlElement -> {
							done.put(urlElement, true);
							urlTakes.add(urlElement);						
						});
					this.addUrlTakesToQueue(urlQueue, urlTakes);
				});
			this.stepQualityOut.printStepQuality(done);
		}
		this.stepQualityOut.close();
	}
	
	public void run(List<String> seed, int takesPerStep){
		this.stepQualityOut.open();
		Set<String> done = new HashSet<String>();
		PriorityQueue<String> urlQueue = new PriorityQueue<String>(seed);	
		while(!urlQueue.isEmpty()){
			urlQueue.stream()
				.limit(takesPerStep)
				.forEach(url->{		
					PriorityQueue<String> urlTakes = new PriorityQueue<String>();
					webGraph.get(url).stream()
						.filter(urlElement -> !done.contains(urlElement))
						.forEach(urlElement -> {
							done.add(urlElement);
							urlTakes.add(urlElement);						
						});
					this.addUrlTakesToQueue(urlQueue, urlTakes);
				});
			this.stepQualityOut.printStepQuality(done);
		}
		this.stepQualityOut.close();
	}

	private void addUrlTakesToQueue(PriorityQueue<String> urlQueue, PriorityQueue<String> urlTakes) {
		urlQueue.addAll(urlTakes);
	}
}
