package uni.ca.crawlingsim.scheduling;
/**
 * Class Page defines the properties of the page object used in the crawling process
 * @author Ingo Rößner, Daniel Michalke
 *
 */
public class Page {
	private int rank;
	private String url;
	public boolean done;
	/**
	 * constructor Page, initializes the page with the given url and rank=0
	 * @param url
	 */
	public Page(String url){
		this.done = false;
		this.url = url;
		this.rank = 0;
	}
	/**
	 * Method getRank, returns the rank value of the Page
	 * @return rank integer
	 */
	public int getRank() {
		return this.rank;
	}
	/**
	 * Method setRank, sets the rank for the page
	 * @param rank integer
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}
	/**
	 * Method getUrl returns the url of the page
	 * @return url string
	 */
	public String getUrl() {
		return this.url;
	}
	
}
