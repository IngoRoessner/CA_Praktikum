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
import uni.ca.crawlingsim.scheduling.pagelvlstrategy.OPIC;
import uni.ca.crawlingsim.scheduling.pagelvlstrategy.PageLevelStrategy;
import uni.ca.crawlingsim.scheduling.sitelvlstrategy.MaxPagePriority;
import uni.ca.crawlingsim.scheduling.sitelvlstrategy.RoundRobin;
import uni.ca.crawlingsim.scheduling.sitelvlstrategy.SiteLevelStrategy;
/**
 * 
 * @author Ingo R��ner, Daniel Michalke
 * Class Crawler, Including Main function
 * 
 */
public class Crawler {
	private WebGraph webGraph;
	private QualityInfo quality;
	private StepQualityOut stepQualityOut;
	private Scheduler scheduler;
	/**
	 * Method Sheduler, creates a sheduler object based on the start parameters
	 * @param plsType page level strategy type
	 * @param slsType side level strategy type
	 * @param batchSize 
	 * @return sheduler object including the page and side strategy
	 */
	public static Scheduler getScheduler(String plsType, String slsType, int batchSize){
		PageLevelStrategy pls;
		SiteLevelStrategy sls;
		switch(plsType){
			case "BacklinkCount":
				pls = new BacklinkCount();
				break;
			case "OPIC":
				pls = new OPIC();
				break;
			default:
				System.err.println(slsType+" not as Site-Level-Strategy found: chose from ['BacklinkCount', 'OPIC']. use default 'BacklinkCount'");
				pls = new BacklinkCount();
				break;
		}
		switch(slsType){
			case "MaxPagePriority":
				sls = new MaxPagePriority();
				break;
			case "RoundRobin":
				sls = new RoundRobin();
				break;
			default:
				System.err.println(slsType+" not as Site-Level-Strategy found: chose from ['MaxPagePriority', 'RoundRobin']. use default 'MaxPagePriority'");
				sls = new MaxPagePriority();
				break;
		}
		Scheduler scheduler = new Scheduler(pls, sls, batchSize);
		return scheduler;
	}
	/**
	 * can be started with one of the following parameter combinations
	 * SEED_FILE WEB_GRAPH QUALITY_MAPPING MAX_STEPS TAKES_PER_STEP PAGE_LVL_STRAT SITE_LVL_STRAT BATCH_SIZE STEP_QUALITY
	 * SEED_FILE MAX_STEPS TAKES_PER_STEP BATCH_SIZE STEP_QUALITY 
	 * WEB_GRAPH QUALITY_MAPPING
	 * depending on the number of parameters it will create a new database and start the crawling process on it
	 * or just do the crawling process on the existing database
	 * @param args
	 * @throws Exception
	 */
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
	/**
	 * Constructor Crawler, based on webgraph qualityinfo stepqualityout and sheduler objects calls the Method init with its parameters
	 * @param webGraph
	 * @param qualityInfo
	 * @param stepQualityOut
	 * @param scheduler
	 */
	public Crawler(WebGraph webGraph, QualityInfo qualityInfo, StepQualityOut stepQualityOut, Scheduler scheduler){
		this.init(webGraph, qualityInfo, stepQualityOut, scheduler);
	}
	/**
	 * Constructor Crawler, based on Path parameters, creates WegGraph, QualityInfo, StepQualityOut and sheduler objects and starts Init method with them
	 * @param graphFilePath
	 * @param qualityFilePath
	 * @param stepQualityOutPath
	 * @param scheduler
	 * @throws Exception
	 */
	public Crawler(Path graphFilePath, Path qualityFilePath, Path stepQualityOutPath, Scheduler scheduler) throws Exception{
		WebGraph graph = new WebGraph(graphFilePath);
		QualityInfo qinfo = new QualityInfo(qualityFilePath);	
		StepQualityOut out = new StepQualityOut(stepQualityOutPath);
		this.init(graph, qinfo, out, scheduler);
	}
	/**
	 * Method init, initializes the local paramters with the given parameters
	 * @param webGraph
	 * @param qualityInfo
	 * @param stepQualityOut
	 * @param scheduler
	 */
	private void init(WebGraph webGraph, QualityInfo qualityInfo, StepQualityOut stepQualityOut, Scheduler scheduler) {
		this.webGraph = webGraph;
		this.quality = qualityInfo;
		this.stepQualityOut = stepQualityOut;
		this.scheduler = scheduler;
	}
	/**
	 * Method run, gets called if maxSteps is not entered, calls run method with paramter maxSteps = -1
	 * only used by tests so far
	 * @param seed
	 * @param takesPerStep
	 * @throws IOException
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public void run(List<String> seed, int takesPerStep)throws IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		this.run(seed, takesPerStep, -1);
	}
	/**
	 * Method run, starts the crawl process and writes the stepquality
	 * @param seed
	 * @param takesPerStep
	 * @param maxSteps
	 * @throws IOException
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public void run(List<String> seed, int takesPerStep, int maxSteps) throws IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		this.scheduler.setSeed(seed);
		this.stepQualityOut.open();	
		seed.forEach(s -> this.stepQualityOut.count(true));
		for(int i = 0; i != maxSteps && !this.scheduler.isEmpty(); i++){
			List<Link> links = new ArrayList<>();
			for(int j = 0; j<takesPerStep && !this.scheduler.isEmpty(); j++){
				String url = this.scheduler.poll();		
				this.stepQualityOut.count(this.quality.setQuality(url));
				List<Link> steplinks = webGraph.linksFrom(url);
				this.scheduler.addAll(steplinks);
				links.addAll(steplinks);
			}
			this.stepQualityOut.printStepQuality();
			if(i%100 == 0){
				System.out.println(new Date().toString() + ": crawled steps: "+i);
			}
		}
		this.stepQualityOut.close();
	}
	/**
	 * Method Close, closes the open files webgraph and quality
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		this.webGraph.close();
		this.quality.close();
	}
}
