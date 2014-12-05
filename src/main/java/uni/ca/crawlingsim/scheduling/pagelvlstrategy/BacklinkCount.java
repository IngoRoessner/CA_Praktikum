package uni.ca.crawlingsim.scheduling.pagelvlstrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.Page;
import uni.ca.crawlingsim.scheduling.SchedulterInterface;
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
	public void setRanks(SchedulterInterface schedulter) {
		this.setRanks(schedulter.getDone());
		this.setRanks(schedulter.getQueue());
	}

	public void setRanks(List<Site> sites) {
		sites.stream().parallel().forEach(site->{this.setRank(site);});
	}
	
	private void setRank(Site site) {
		this.setPageRanks(site.getDoneList());
		this.setPageRanks(site.getQueue());
	}


	private void setPageRanks(List<Page> pages) {
		pages.forEach(page->{
			Integer backLinks = this.backlinks.get(page.getUrl());
			int backLinksInt;
			if(backLinks == null){
				backLinksInt = 0;
			}else{
				backLinksInt = backLinks.intValue();
			}
			page.setRank(backLinksInt);
		});
	}

}
