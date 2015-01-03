package uni.ca.crawlingsim.scheduling.sitelvlstrategy;

import java.util.List;

import uni.ca.crawlingsim.scheduling.Page;
import uni.ca.crawlingsim.scheduling.SchedulerInterface;
import uni.ca.crawlingsim.scheduling.Site;

public class AvgPagePriority implements SiteLevelStrategy {
	/**
	 * method setRanks, sets the rank for the scheduler
	 * @param scheduler SchedulerInterface
	 */
	@Override
	public void setRanks(SchedulerInterface scheduler) {
		scheduler.getSites().forEach((key, value)->{
			this.setRank(value);
		});
	}
	/**
	 * Method setRanks, based on sites, sets the ranks for each element in sites
	 * @param sites
	 */
	public void setRanks(List<Site> sites){
		sites.stream().parallel().forEach(site->{this.setRank(site);});
	}
	/**
	 * method setRank based on one site, sets the rank for the site
	 * @param site
	 */
	private void setRank(Site site){
		int sum = 0;
		int count = 0;
		List<Page> pages = site.getQueue();
		for(Page page : pages){
			int pageRank = page.getRank();
			sum += pageRank;
			count++;
		}
		site.setRank(sum/count);
	}
	/**
	 * Method donePollOn do nothing
	 * @param scheduler
	 * @param site
	 */
	@Override
	public void donePollOn(SchedulerInterface scheduler, Site site) {
		//do nothing
	}

}
