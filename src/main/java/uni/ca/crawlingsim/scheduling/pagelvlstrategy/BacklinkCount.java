package uni.ca.crawlingsim.scheduling.pagelvlstrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.Page;
import uni.ca.crawlingsim.scheduling.SchedulerInterface;
import uni.ca.crawlingsim.scheduling.Site;

/**
 * Class BacklinkCount
 * @author Ingo R��ner, Daniel Michalke
 *
 */
public class BacklinkCount implements PageLevelStrategy {
	
	private Map<String, Integer> backlinks; 

	/**
	 * Constructor BacklinkCount, initializes a new backlinks as a HasmMap of Strings and Integer
	 */
	public BacklinkCount(){
		this.backlinks = new HashMap<String, Integer>();
	}
	
	/**
	 * Method incomingLinks, searches a matching entry in the map and increases the value +1 for every link in the recieved list of links  
	 *@param links List<Link>
	 */
	@Override
	public void incomingLinks(List<Link> links) {
		links.forEach(link->{
			Integer backlinks = this.backlinks.get(link.to);
			int backLinksCount = 0;
			if(backlinks != null){
				backLinksCount = backlinks.intValue();
			}
			this.backlinks.put(link.to, backLinksCount+1);
		});
	}
	
	/**
	 * Method setRanks, sets the ranks for every site in the recieved scheduler object
	 */
	@Override
	public void setRanks(SchedulerInterface scheduler) {
		scheduler.getSites().forEach((key, value)->{
			this.setRank(value);
		});
	}
	
	/**
	 * Method setRank, sets the rank for the site
	 * @param site Sote
	 */
	public void setRank(Site site) {
		site.getPages().forEach((key, value)->{
			this.setPageRank(value);
		});
	}
	
	/**
	 * Method setPageRank, sets the PageRank
	 * @param page Page
	 */
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
