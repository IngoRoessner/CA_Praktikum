package uni.ca.crawlingsim.scheduling.sitelvlstrategy;

import java.util.List;

import uni.ca.crawlingsim.scheduling.Page;
import uni.ca.crawlingsim.scheduling.SchedulterInterface;
import uni.ca.crawlingsim.scheduling.Site;

public class MaxPagePriority implements SiteLevelStrategy{

	@Override
	public void setRanks(SchedulterInterface scheduler) {
		this.setRanks(scheduler.getDone());
		this.setRanks(scheduler.getQueue());
	}

	public void setRanks(List<Site> sites){
		sites.stream().parallel().forEach(site->{this.setRank(site);});
	}
	
	private void setRank(Site site){
		int maxRank = 0;
		List<Page> pages = site.getQueue();
		for(Page page : pages){
			int pageRank = page.getRank();
			if(maxRank < pageRank){
				maxRank = pageRank;				
			}
		}
		site.setRank(maxRank);
	}
}
