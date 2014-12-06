package uni.ca.crawlingsim.scheduling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uni.ca.crawlingsim.data.Link;

public class Site {
	private int rank;
	private String url;
	private Map<String, Page> pages;
	private List<Page> queue;
	public boolean done;
	
	public Site(String url){
		this.done = false;
		this.url = url;
		this.rank = 0;
		this.queue = new LinkedList<Page>();
		this.pages = new HashMap<String, Page>();
	}
	
	public int getRank(){
		return rank;
	}
	
	public void setRank(int rank){
		this.rank = rank;
	}

	public String getUrl() {
		return this.url;
	}

	public Map<String, Page> getPages() {
		return this.pages;
	}

	public String poll() {
		Page page = this.queue.get(0);
		String result = page.getUrl();
		this.pages.get(result).done = true;
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
		if(!this.pages.containsKey(pageUrl)){
			Page page = new Page(pageUrl);
			this.pages.put(pageUrl, page);
			this.queue.add(page);
		}
	}

	public List<Page> getQueue() {
		return this.queue;
	}	
}
