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

public class Scheduler implements SchedulerInterface{
	private PageLevelStrategy pageLevelStrategy;
	private SiteLevelStrategy siteLevelStrategy;
	private Map<String, Site> sites;
	private List<Site> queue;
	private int batch;
	private int batchCounter;
	
	public Scheduler(PageLevelStrategy pls, SiteLevelStrategy sls, int batch){
		this.pageLevelStrategy = pls;
		this.siteLevelStrategy = sls;
		this.sites = new HashMap<String, Site>();
		this.queue = new LinkedList<Site>();
		this.batch = batch;
		this.batchCounter = 0;
	}
	
	public void addAll(List<Link> links){
		links.forEach(link->{this.addLink(link);});
		this.pageLevelStrategy.incommingLinks(links);
	}

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

	private void setRanksAndSort() {
		if(this.batchCounter++ % this.batch == 0){
			this.pageLevelStrategy.setRanks(this);
			this.pageLevelStrategy.sort(this);
			this.siteLevelStrategy.setRanks(this);
			this.siteLevelStrategy.sort(this);
		}
	}

	public boolean isEmpty() {
		return this.queue.isEmpty();
	}
	
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

	public void setSeed(List<String> seed) {
		for(String url : seed){
			Link link = new Link("", url);
			this.addLink(link);
		}
	}

	@Override
	public Map<String, Site> getSites() {
		return this.sites;
	}

	@Override
	public List<Site> getQueue() {
		return this.queue;
	}
}
