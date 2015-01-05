package uni.ca.crawlingsim.scheduling.pagelvlstrategy;
/**
 * Class OPIC implements the opic algorithm as a pagelvlstrategy
 * @author Ingo R��ner, Daniel Michalke
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.SchedulerInterface;
import uni.ca.crawlingsim.scheduling.Site;

public class OPIC implements PageLevelStrategy {
	public static int startCash = 50000;
	private static boolean onlySeedStartCash = true;
	private Map<String, Integer> cashMap;
	
	/**
	 * Constructor OPUC initializes cashMap as a new HashMap of String and Integer
	 */
	public OPIC(){
		this.cashMap = new HashMap<String, Integer>();
	}
	
	/**
	 * Method setRanks sets the rank for each site of the scheduler
	 * @param scheduler SchedulerInterface
	 */
	@Override
	public void setRanks(SchedulerInterface scheduler) {
		scheduler.getSites().forEach((key, site) -> {
			this.setRank(site);
		});
	}
	
	/**
	 * Method incomingLinks shares the saved cash in link.from to the belonging link.to and saves the new cash  spread in cashMap 
	 * @param links List of Link elements
	 */
	@Override
	public void incomingLinks(List<Link> links) {
		Map<String, List<String>> linkMap = new HashMap<String, List<String>>();
		for(Link link : links){
			if(!linkMap.containsKey(link.from)){
				linkMap.put(link.from, new ArrayList<String>());
			}
			List<String> toList = linkMap.get(link.from);
			toList.add(link.to);
		}
		linkMap.forEach((from, toList)->{
			int fromCash = this.getCash(from);
			int toCash = fromCash/(toList.size());
			toList.forEach(to->{this.addCash(to, toCash);});
			this.cashMap.put(from, 0);
		});
	}
	
	/**
	 * Method addCash adds the Cash in the cashMap to the string
	 * @param to String
	 * @param toCash int
	 */
	private void addCash(String to, int toCash) {
		int startCashForNonSeed = onlySeedStartCash ? 0 : startCash;
		if(!this.cashMap.containsKey(to)){
			this.cashMap.put(to, startCashForNonSeed);
		}
		int origCash = this.cashMap.get(to);
		this.cashMap.put(to, origCash + toCash);
	}

	/**
	 * Method getCash
	 * @param from
	 * @return startCash: if url is seed url; sum of gotten casch if not
	 */
	public int getCash(String from) {
		if(!this.cashMap.containsKey(from)){
			this.cashMap.put(from, startCash);
		}
		return this.cashMap.get(from);	
	}
	
	/**
	 * Method setRank, sets the rank for the given site
	 * @param site
	 */
	public void setRank(Site site) {
		site.getQueue().forEach((page)->{
			page.setRank(this.getCash(page.getUrl()));
		});
	}

}
