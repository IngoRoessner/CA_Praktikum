package uni.ca.crawlingsim.scheduling.pagelvlstrategy;

import java.util.List;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.Page;
import uni.ca.crawlingsim.scheduling.SchedulerInterface;
import uni.ca.crawlingsim.scheduling.Site;
/**
 * Class Optimal, implements the optimal combination of both, Backlinkcount and OPIC
 */
public class Optimal implements PageLevelStrategy {
	private OPIC opic;
	private BacklinkCount blc;
	private int seed_size = 0;
	private int poll_count = 0;
	private int size_of_queue;
	static private double avgBlc = 15.9;
	/**
	 * Constructor, initializes Opic and Backlinkcount 
	 */
	public Optimal(){
		this.opic = new OPIC();
		this.blc = new BacklinkCount();
	}
	/**
	 * Method getOpicRank. returns the opic rank for the page
	 * @param page Page
	 * @return int rank
	 */
	private int getOpicRank(Page page){
		return this.opic.getCash(page.getUrl());
	}
	/**
	 * Method getBlcRank, returns backlinkcountrank for the page
	 * @param page Page
	 * @return int rank
	 */
	private int getBlcRank(Page page){
		return this.blc.getPageRank(page);
	}
	/**
	 * Method getOptimalRank implements the algorithm
	 * @param blcRank backlinkcountrank
	 * @param opicRank OPIC rank
	 * @param size_of_queue Size of the Queue
	 * @return
	 */
	private int getOptimalRank(double blcRank, double opicRank, final int size_of_queue){
		// normalization of the backlinkcount
		double blc_normal = blcRank / avgBlc;
		
		double maxOpic = OPIC.startCash * this.seed_size;
		double currentAvgOpic = maxOpic / size_of_queue;
		double opic_normal = opicRank / currentAvgOpic;
		// weighting of 1 for  backlinkcount and opic, backlinkcount 0 if number of takes smaller than seedsize*2
		int blc_gewicht = (poll_count < (2 * this.seed_size)) ? 0 : 1;
		int opic_gewicht = 1;
		
		double optimal = opic_gewicht * opic_normal + blc_gewicht * blc_normal;
		
		int optimalInt = (int) (optimal*1000);
		
		return optimalInt;
	}
	/**
	 * Method setRanks
	 * @param scheduler SchedulerInterface
	 */
	@Override
	public void setRanks(SchedulerInterface scheduler) {
		this.size_of_queue = 0;
		//calculates the size of the total queue
		for(Site site : scheduler.getQueue()){
			this.size_of_queue += site.getQueue().size();
		}
		//for every page in every site gets the opic and backlinkcount rank, and sets the optimal rank
		scheduler.getQueue().forEach(site -> {
			site.getQueue().forEach(page -> {
				int opicRank = this.getOpicRank(page);
				int blcRank = this.getBlcRank(page);
				page.setRank(this.getOptimalRank(blcRank, opicRank, this.size_of_queue));
			});
		});
	}
	/**
	 * Method incommingLinks  gets a list of links, counts the polls and starts incomingLinks method in classes OPIC.java and BacklinkCount.java 
	 * @param links List of links
	 */
	@Override
	public void incomingLinks(List<Link> links) {
		this.poll_count++;
		this.opic.incomingLinks(links);
		this.blc.incomingLinks(links);
	}
	/**
	 * Method setSeedSize sets the seedsize for the optimal algorithm with the given parameter
	 * @param size Int
	 */
	public void setSeedSize(int size){
		this.seed_size = size;
	}

}
