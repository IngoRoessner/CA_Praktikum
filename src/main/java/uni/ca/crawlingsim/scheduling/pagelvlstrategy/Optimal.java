package uni.ca.crawlingsim.scheduling.pagelvlstrategy;

import java.util.List;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.Page;
import uni.ca.crawlingsim.scheduling.SchedulerInterface;
import uni.ca.crawlingsim.scheduling.Site;

public class Optimal implements PageLevelStrategy {
	private OPIC opic;
	private BacklinkCount blc;
	private int seed_size = 0;
	private int poll_count = 0;
	private int size_of_queue;
	static private double avgBlc = 15.9;

	private int getOpicRank(Page page){
		return this.opic.getCash(page.getUrl());
	}
	
	private int getBlcRank(Page page){
		return this.blc.getPageRank(page);
	}
	
	private int getOptimalRank(double blcRank, double opicRank, final int size_of_queue){
		double blc_normal = blcRank / avgBlc;

		double maxOpic = OPIC.startCash * this.seed_size;
		double currentAvgOpic = maxOpic / size_of_queue;
		double opic_normal = opicRank / currentAvgOpic;

		int blc_gewicht = (poll_count < (2 * this.seed_size)) ? 0 : 1;
		int opic_gewicht = 1;

		double optimal = opic_gewicht * opic_normal + blc_gewicht * blc_normal;
		
		int optimalInt = (int) (optimal*1000);
		
		return optimalInt;
	}
	
	@Override
	public void setRanks(SchedulerInterface scheduler) {
		this.size_of_queue = 0;
		for(Site site : scheduler.getQueue()){
			this.size_of_queue += site.getQueue().size();
		}
		
		scheduler.getQueue().forEach(site -> {
			site.getQueue().forEach(page -> {
				int opicRank = this.getOpicRank(page);
				int blcRank = this.getBlcRank(page);
				page.setRank(this.getOptimalRank(blcRank, opicRank, this.size_of_queue));
			});
		});
	}

	@Override
	public void incomingLinks(List<Link> links) {
		this.poll_count++;
		this.opic.incomingLinks(links);
		this.blc.incomingLinks(links);
	}
	
	public void setSeedSize(int size){
		this.seed_size = size;
	}

}
