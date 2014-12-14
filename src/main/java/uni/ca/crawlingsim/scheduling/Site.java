package uni.ca.crawlingsim.scheduling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uni.ca.crawlingsim.data.Link;
/**
 * Class Site, defines the properties of the Site object used in the crawling process
 * @author Daniel
 *
 */
public class Site {
	private int rank;
	private String url;
	private Map<String, Page> pages;
	private List<Page> queue;
	public boolean done;
	/**
	 * Constructor Site, initializes the site with the url parameter, rank = 0 and a queue and pages
	 * @param url String
	 */
	public Site(String url){
		this.done = false;
		this.url = url;
		this.rank = 0;
		this.queue = new LinkedList<Page>();
		this.pages = new HashMap<String, Page>();
	}
	/**
	 * Method getRank, returns the rank of the Site
	 * @return rank, integer
	 */
	public int getRank(){
		return rank;
	}
	/**
	 * Method setRank, defines the rank of the page with its paramter
	 * @param rank integer
	 */
	public void setRank(int rank){
		this.rank = rank;
	}
	/**
	 * Method getUrl, returns the Url of the site
	 * @return url string
	 */
	public String getUrl() {
		return this.url;
	}
	/**
	 * Method getPages, returns a Map consisting of Strings and the page
	 * @return Map<String, Page>
	 */
	public Map<String, Page> getPages() {
		return this.pages;
	}
	/**
	 * Method poll,searches for the url with the highest priority removes it from the queue and gives it back as the result String
	 * @return
	 */
	public String poll() {
		Page page = this.queue.get(0);
		String result = page.getUrl();
		this.pages.get(result).done = true;
		this.queue.remove(0);
		return result;
	}
	/**
	 * Method isEmpty, checks if the queue is empty and returns the boolean
	 * @return boolean if the queue is empty
	 */
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}
	/**
	 * Method sort,  
	 */
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
