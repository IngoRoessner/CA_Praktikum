package uni.ca.crawlingsim.scheduling;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Site {
	private int rank;
	private String url;
	private Map<String, Page> done;
	private List<Page> queue;
	
	public Site(String url){
		this.url = url;
		this.rank = 0;
		this.queue = new LinkedList<Page>();
		this.done = new HashMap<String, Page>();
	}
	
	public int getRank(){
		return rank;
	}
	
	public void setRank(int rank){
		this.rank = rank;
	}

	public Map<String, Page> getDone() {
		return this.done;
	}

	public String getUrl() {
		return this.url;
	}

	public List<Page> getQueue() {
		return queue;
	}
	
}
