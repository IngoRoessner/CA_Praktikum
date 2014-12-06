package uni.ca.crawlingsim.scheduling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.pagelvlstrategy.PageLevelStrategy;
import uni.ca.crawlingsim.scheduling.sitelvlstrategy.SiteLevelStrategy;

public class Scheduler implements SchedulterInterface{
	private PageLevelStrategy pageLevelStrategy;
	private SiteLevelStrategy siteLevelStrategy;
	private Map<String, Site> done;
	private List<Site> queue;
	private int batch;
	private int batchCounter;
	
	public Scheduler(PageLevelStrategy pls, SiteLevelStrategy sls, int batch){
		this.pageLevelStrategy = pls;
		this.siteLevelStrategy = sls;
		this.done = new HashMap<String, Site>();
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
			this.done.put(site.getUrl(), site);
			this.queue.remove(0);
		}
		return result;
	}

	private void setRanksAndSort() {
		if(this.batchCounter++ % this.batch == 0){
			this.pageLevelStrategy.setRanks(this);
			this.siteLevelStrategy.setRanks(this);
			this.queue.sort((e1, e2)->{return e2.getRank() - e1.getRank();});
			this.queue.forEach(site->{site.sort();});
		}
	}

	public boolean isEmpty() {
		return this.queue.isEmpty();
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
		Site site;
		boolean siteIsDone = false;
		if(this.done.containsKey(siteUrl)){
			siteIsDone = true;
			site = this.done.get(siteUrl);
		}else{
			Optional<Site> siteOpt = this.queue.stream().filter((element)->{return element.getUrl().equals(siteUrl);}).findFirst();
			if(siteOpt.isPresent()){
				site = siteOpt.get();
			}else{
				site = new Site(siteUrl);
				this.queue.add(site);
			}
		}
		site.add(link);
		if(siteIsDone && !site.isEmpty()){
			this.queue.add(site);
			this.done.remove(siteUrl);
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
}
