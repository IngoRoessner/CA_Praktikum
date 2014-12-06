package uni.ca.crawlingsim.crawler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.data.QualityInfo;
import uni.ca.crawlingsim.data.WebGraph;
import uni.ca.crawlingsim.scheduling.Scheduler;
import uni.ca.crawlingsim.scheduling.pagelvlstrategy.BacklinkCount;
import uni.ca.crawlingsim.scheduling.pagelvlstrategy.PageLevelStrategy;
import uni.ca.crawlingsim.scheduling.sitelvlstrategy.MaxPagePriority;
import uni.ca.crawlingsim.scheduling.sitelvlstrategy.SiteLevelStrategy;

public class Crawler {
	private WebGraph webGraph;
	private QualityInfo quality;
	private StepQualityOut stepQualityOut;
	private Scheduler scheduler;
	
	public static Scheduler getScheduler(String plsType, String slsType, int batchSize){
		PageLevelStrategy pls;
		SiteLevelStrategy sls;
		switch(plsType){
			case "BacklinkCount":
				pls = new BacklinkCount();
				break;
			default:
				System.err.println(slsType+" not as Site-Level-Strategy found: chose from ['BacklinkCount']. use default 'BacklinkCount'");
				pls = new BacklinkCount();
				break;
		}
		switch(slsType){
			case "MaxPagePriority":
				sls = new MaxPagePriority();
				break;
			default:
				System.err.println(slsType+" not as Site-Level-Strategy found: chose from ['MaxPagePriority']. use default 'MaxPagePriority'");
				sls = new MaxPagePriority();
				break;
		}
		Scheduler scheduler = new Scheduler(pls, sls, batchSize);
		return scheduler;
	}
	
	public static void main(String[] args) throws Exception{
		if(args.length == 9){
			List<String> seed = Files.lines(Paths.get(args[0])).collect(Collectors.toList());
			Path graphFilePath = Paths.get(args[1]);
			Path qualityFilePath = Paths.get(args[2]);
			int maxSteps = Integer.parseInt(args[3]);
			int takesPerStep = Integer.parseInt(args[4]);
			Scheduler scheduler = getScheduler(args[5], args[6], Integer.parseInt(args[7]));
			Path stepQualityOutPath = Paths.get(args[8]);
			System.out.println(new Date().toString() + ": parse files...");
			Crawler crawler = new Crawler(graphFilePath, qualityFilePath, stepQualityOutPath, scheduler);
			System.out.println(new Date().toString() + ": crawl graph...");
			crawler.run(seed, takesPerStep, maxSteps);
			System.out.println(new Date().toString() + ": finished");
			crawler.close();			
		}else if(args.length == 7){
			List<String> seed = Files.lines(Paths.get(args[0])).collect(Collectors.toList());
			int maxSteps = Integer.parseInt(args[1]);
			int takesPerStep = Integer.parseInt(args[2]);
			Scheduler scheduler = getScheduler(args[3], args[4], Integer.parseInt(args[5]));
			Path stepQualityOutPath = Paths.get(args[6]);
			Crawler crawler = new Crawler(new WebGraph(), new QualityInfo(), new StepQualityOut(stepQualityOutPath), scheduler);
			System.out.println(new Date().toString() + ": crawl graph...");
			crawler.run(seed, takesPerStep, maxSteps);
			System.out.println(new Date().toString() + ": finished");
			crawler.close();
		}else if(args.length == 2){
			System.out.println(new Date().toString() + ": parse files...");
			Path graphFilePath = Paths.get(args[0]);
			Path qualityFilePath = Paths.get(args[1]);
			WebGraph wgraph = new WebGraph(graphFilePath);
			QualityInfo qinfo = new QualityInfo(qualityFilePath);
			wgraph.close();
			qinfo.close();
		}else{
			System.out.println("SEED_FILE WEB_GRAPH QUALITY_MAPPING MAX_STEPS TAKES_PER_STEP PAGE_LVL_STRAT SITE_LVL_STRAT BATCH_SIZE STEP_QUALITY");
			System.out.println("SEED_FILE MAX_STEPS TAKES_PER_STEP BATCH_SIZE STEP_QUALITY");
			System.out.println("WEB_GRAPH QUALITY_MAPPING");
		}	
	}
	
	public Crawler(WebGraph webGraph, QualityInfo qualityInfo, StepQualityOut stepQualityOut, Scheduler scheduler){
		this.init(webGraph, qualityInfo, stepQualityOut, scheduler);
	}
	
	public Crawler(Path graphFilePath, Path qualityFilePath, Path stepQualityOutPath, Scheduler scheduler) throws Exception{
		WebGraph graph = new WebGraph(graphFilePath);
		QualityInfo qinfo = new QualityInfo(qualityFilePath);	
		StepQualityOut out = new StepQualityOut(stepQualityOutPath);
		this.init(graph, qinfo, out, scheduler);
	}
	
	private void init(WebGraph webGraph, QualityInfo qualityInfo, StepQualityOut stepQualityOut, Scheduler scheduler) {
		this.webGraph = webGraph;
		this.quality = qualityInfo;
		this.stepQualityOut = stepQualityOut;
		this.scheduler = scheduler;
	}
	
	public void run(List<String> seed, int takesPerStep)throws IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		this.run(seed, takesPerStep, -1);
	}
	
	public void run(List<String> seed, int takesPerStep, int maxSteps) throws IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		this.scheduler.setSeed(seed);
		this.stepQualityOut.open();	
		seed.forEach(s -> this.stepQualityOut.count(true));
		for(int i = 0; i != maxSteps && !this.scheduler.isEmpty(); i++){
			List<Link> links = new ArrayList<>();
			for(int j = 0; j<takesPerStep && !this.scheduler.isEmpty(); j++){
				String url = this.scheduler.poll();
				List<Link> steplinks = webGraph.linksFrom(url);
				this.scheduler.addAll(steplinks);
				this.quality.setQuality(steplinks);		
				links.addAll(steplinks);
			}
			this.stepQualityOut.count(links);
			this.stepQualityOut.printStepQuality();

			if(i%1 == 0){
				System.out.println(new Date().toString() + ": crawled steps: "+i);
			}
		}
		this.stepQualityOut.close();
	}
	
	public void close() throws SQLException {
		this.webGraph.close();
		this.quality.close();
	}
}
