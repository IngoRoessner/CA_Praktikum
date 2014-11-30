package uni.ca.crawlingsim.crawler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import uni.ca.crawlingsim.data.DoneSet;
import uni.ca.crawlingsim.data.QualityInfo;
import uni.ca.crawlingsim.data.WebGraph;
import uni.ca.crawlingsim.data.QualityInfo.QualityResultElement;

public class Crawler {
	private WebGraph webGraph;
	private QualityInfo quality;
	private StepQualityOut stepQualityOut;
	
	public static void main(String[] args) throws Exception{
		if(args.length != 6){
			System.out.println("SEED_FILE WEB_GRAPH QUALITY_MAPPING MAX_STEPS TAKES_PER_STEP STEP_QUALITY");
		}else{
			List<String> seed = Files.lines(Paths.get(args[0])).collect(Collectors.toList());
			Path graphFilePath = Paths.get(args[1]);
			Path qualityFilePath = Paths.get(args[2]);
			int maxSteps = Integer.parseInt(args[3]);
			int takesPerStep = Integer.parseInt(args[4]);
			Path stepQualityOutPath = Paths.get(args[5]);
			System.out.println(new Date().toString() + ": parse files...");
			Crawler crawler = new Crawler(graphFilePath, qualityFilePath, stepQualityOutPath);
			System.out.println(new Date().toString() + ": crawl graph...");
			crawler.run(seed, takesPerStep, maxSteps);
			System.out.println(new Date().toString() + ": finished");
			crawler.close();
		}		
	}
	

	public Crawler(WebGraph webGraph, QualityInfo qualityInfo, StepQualityOut stepQualityOut){
		this.init(webGraph, qualityInfo, stepQualityOut);
	}
	
	public Crawler(Path graphFilePath, Path qualityFilePath, Path stepQualityOutPath) throws Exception{
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		//Future<WebGraph> future_graph = executorService.submit(()-> );
		WebGraph graph = new WebGraph(graphFilePath);
		//Future<QualityInfo> future_quality = executorService.submit(()-> );
		QualityInfo qinfo = new QualityInfo(qualityFilePath);	
		StepQualityOut out = new StepQualityOut(stepQualityOutPath);
		this.init(graph, qinfo, out);
	}
	
	private void init(WebGraph webGraph, QualityInfo qualityInfo, StepQualityOut stepQualityOut) {
		this.webGraph = webGraph;
		this.quality = qualityInfo;
		this.stepQualityOut = stepQualityOut;
	}
	
	public void run(List<String> seed, int takesPerStep)throws IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		this.run(seed, takesPerStep, -1);
	}
	
	public void run(List<String> seed, int takesPerStep, int maxSteps) throws IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		this.stepQualityOut.open();
		PriorityQueue<String> urlQueue = new PriorityQueue<String>(seed);	
		seed.forEach(s -> this.stepQualityOut.count(true));
		DoneSet done = new DoneSet();
		done.add(seed);
		//if maxSteps = -1: run until  urlQueue.isEmpty()
		for(int i = 0; i != maxSteps && !urlQueue.isEmpty(); i++){
			List<String> urls = new ArrayList<String>();
			for(int j = 0; j<takesPerStep && !urlQueue.isEmpty(); j++){
				String url = urlQueue.poll();
				urls.add(url);
			}
			//System.out.println(new Date().toString() + ": getting linkls...");
			List<String> links = webGraph.linksFrom(urls);
			//System.out.println(new Date().toString() + ": filter linkls by done...");
			links = done.filter(links);
			done.add(links);
			//System.out.println(new Date().toString() + ": getting quality...");
			Map<String, QualityResultElement> quality = this.quality.get(links);
				
			//no direct add to urlQueue as preparation for additional strategies
			this.addUrlTakesToQueue(urlQueue, quality);
			
			this.stepQualityOut.count(quality);
			this.stepQualityOut.printStepQuality();

			if(i%100 == 0){
				System.out.println(new Date().toString() + ": crawled steps: "+i);
			}
		}
		this.stepQualityOut.close();
		done.close();
	}

	private void addUrlTakesToQueue(PriorityQueue<String> urlQueue, Map<String, QualityResultElement> urlTakes) {
		urlQueue.addAll(urlTakes.values().stream().map(element -> element.url).collect(Collectors.toList()));
	}
	
	public void close() throws SQLException {
		this.webGraph.close();
		this.quality.close();
	}
}
