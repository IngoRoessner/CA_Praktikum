package uni.ca.crawlingsim.scheduling;

import java.util.LinkedList;
import java.util.List;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.pagelvlstrategy.PageLevelStrategy;
import uni.ca.crawlingsim.scheduling.sitelvlstrategy.SiteLevelStrategy;

public class Scheduler implements SchedulterInterface{
	private PageLevelStrategy pageLevelStrategy;
	private SiteLevelStrategy siteLevelStrategy;
	private List<Site> done;
	private List<Site> queue;
	
	public Scheduler(PageLevelStrategy pls, SiteLevelStrategy sls){
		this.pageLevelStrategy = pls;
		this.siteLevelStrategy = sls;
		this.done = new LinkedList<Site>();
		this.queue = new LinkedList<Site>();
	}
	
	public void addAll(List<Link> links){
		
	}
	
	public String poll(){
		return null;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Site> getDone() {
		return this.done;
	}

	@Override
	public List<Site> getQueue() {
		return this.queue;
	}
}
