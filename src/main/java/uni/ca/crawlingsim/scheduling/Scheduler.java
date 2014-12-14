package uni.ca.crawlingsim.scheduling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.pagelvlstrategy.PageLevelStrategy;
import uni.ca.crawlingsim.scheduling.sitelvlstrategy.SiteLevelStrategy;
/**
 * Class Sheduler, implements page and site levestrategies
 * @author Ingo Rößner, Daniel Michalke
 * 
 */
public class Scheduler implements SchedulerInterface{
	private PageLevelStrategy pageLevelStrategy;
	private SiteLevelStrategy siteLevelStrategy;
	private Map<String, Site> sites;
	private List<Site> queue;
	private int batch;
	private int batchCounter;
	/**
	 * Constructor Scheduler, sets the Page and Sitelevelstrategy based on the given parameters including the batchsize
	 * initializes batchCounter to 0, and creates sites (HashMap of strings and Sites) and queue (LinkedList of Sites) objects
	 * @param pls PageLevelStrategy
	 * @param sls SiteLevelStrategy
	 * @param batch integer Batchsize
	 */
	public Scheduler(PageLevelStrategy pls, SiteLevelStrategy sls, int batch){
		this.pageLevelStrategy = pls;
		this.siteLevelStrategy = sls;
		this.sites = new HashMap<String, Site>();
		this.queue = new LinkedList<Site>();
		this.batch = batch;
		this.batchCounter = 0;
	}
	/**
	 * Method addAll, gets a List of Links and calls the pagelevelStrategy with them
	 */
	public void addAll(List<Link> links){
		links.forEach(link->{this.addLink(link);});
		this.pageLevelStrategy.incommingLinks(links);
	}
	/**
	 * Method poll, searches for the url with the highest priority removes it from the queue and gives it back as the result String
	 */
	public String poll(){
		this.setRanksAndSort();
		Site site = this.queue.get(0);
		String result = site.poll();
		if(site.isEmpty()){
			site.done = true;
			this.queue.remove(0);
		}
		this.siteLevelStrategy.donePollOn(this, site);
		return result;
	}
	/**
	 * Method setRankAndSort, when the batchsize is reached, the ranking functions of the strategies are called and the queues according to themsorted
	 * 
	 */
	private void setRanksAndSort() {
		if(this.batchCounter++ % this.batch == 0){
			this.pageLevelStrategy.setRanks(this);
			this.pageLevelStrategy.sort(this);
			this.siteLevelStrategy.setRanks(this);
			this.siteLevelStrategy.sort(this);
		}
	}
	/**
	 * Method isEmpty, checks if the queue is empty
	 * @return isEmpty boolean 
	 */
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}
	/**
	 * Method addLink, adds the recieving Link to the queue
	 * @param link Link
	 */
	private void addLink(Link link) {
		String siteUrl = this.getSiteUrl(link.to);
		Site site;
		site = this.sites.get(siteUrl);
		if(site == null){
			site = new Site(siteUrl);
			this.sites.put(siteUrl, site);
			this.queue.add(site);
		}
		site.add(link);
		if(site.done && !site.isEmpty()){
			site.done = false;
			this.queue.add(site);
		}
	}

	/**
	 * Method getSiteUrl, returns URI of the link
	 * @param to String
	 * @return String uri Host
	 */
	private String getSiteUrl(String to) {
		URI uri;
		try {
			uri = new URI(to);
			return uri.getHost();
		} catch (URISyntaxException e) {
			System.err.println("given link ["+to+"] is not an URI");
			return "";
		}
	}
	/**
	 * Method setSeed, adds every String in the List seed to the linkobject
	 * @param seed List of Strings
	 */
	public void setSeed(List<String> seed) {
		for(String url : seed){
			Link link = new Link("", url);
			this.addLink(link);
		}
	}
	/**
	 * Method getSites, returns sites
	 * @return Map<String, Site> sites
	 */
	@Override
	public Map<String, Site> getSites() {
		return this.sites;
	}
	/**
	 * Method getQuere, returns the Queue
	 * @return List<Site> queue
	 */
	@Override
	public List<Site> getQueue() {
		return this.queue;
	}
}
