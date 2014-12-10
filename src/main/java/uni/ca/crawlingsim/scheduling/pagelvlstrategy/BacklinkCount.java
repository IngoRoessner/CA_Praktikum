package uni.ca.crawlingsim.scheduling.pagelvlstrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.Page;
import uni.ca.crawlingsim.scheduling.SchedulerInterface;
import uni.ca.crawlingsim.scheduling.Site;

public class BacklinkCount implements PageLevelStrategy {
	
	private Map<String, Integer> backlinks; 

	public BacklinkCount(){
		this.backlinks = new HashMap<String, Integer>();
	}
	
	@Override
	public void incommingLinks(List<Link> links) {
		links.forEach(link->{
			Integer backlinks = this.backlinks.get(link.to);
			int backLinksCount = 0;
			if(backlinks != null){
				backLinksCount = backlinks.intValue();
			}
			this.backlinks.put(link.to, backLinksCount+1);
		});
	}

	@Override
	public void setRanks(SchedulerInterface scheduler) {
		scheduler.getSites().forEach((key, value)->{
			this.setRank(value);
		});
	}
	
	public void setRank(Site site) {
		site.getPages().forEach((key, value)->{
			this.setPageRank(value);
		});
	}
	
	private void setPageRank(Page page){
		Integer backLinks = this.backlinks.get(page.getUrl());
		int backLinksInt;
		if(backLinks == null){
			backLinksInt = 0;
		}else{
			backLinksInt = backLinks.intValue();
		}
		page.setRank(backLinksInt);
	}

}
