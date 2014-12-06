package uni.ca.crawlingsim.scheduling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uni.ca.crawlingsim.data.Link;

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

	public String poll() {
		Page page = this.queue.get(0);
		String result = page.getUrl();
		this.done.put(result, page);
		this.queue.remove(0);
		return result;
	}

	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

	public void sort() {
		this.queue.sort((e1, e2)->{return e2.getRank() - e1.getRank();});
	}

	public void add(Link link) {
		String pageUrl = link.to;
		if(
			!this.done.containsKey(pageUrl) 
			&& !this.queue.stream().anyMatch((element)->{return element.getUrl().equals(pageUrl);})
		){
			this.queue.add(new Page(pageUrl));
		}
	}
	
}
