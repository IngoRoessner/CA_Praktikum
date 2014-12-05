package uni.ca.crawlingsim.scheduling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.pagelvlstrategy.PageLevelStrategy;
import uni.ca.crawlingsim.scheduling.sitelvlstrategy.SiteLevelStrategy;

public class Scheduler implements SchedulterInterface{
	private PageLevelStrategy pageLevelStrategy;
	private SiteLevelStrategy siteLevelStrategy;
	private Map<String, Site> done;
	private List<Site> queue;
	
	public Scheduler(PageLevelStrategy pls, SiteLevelStrategy sls){
		this.pageLevelStrategy = pls;
		this.siteLevelStrategy = sls;
		this.done = new HashMap<String, Site>();
		this.queue = new LinkedList<Site>();
	}
	
	public void addAll(List<Link> links){
		this.pageLevelStrategy.incommingLinks(links);
		links.forEach(link->{this.addLink(link);});
	}
	

	public String poll(){
		return null;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Site> getDone() {
		return this.done;
	}

	@Override
	public List<Site> getQueue() {
		return this.queue;
	}
	
	private void addLink(Link link) {
		String siteUrl = this.getSiteUrl(link.to);
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
}
