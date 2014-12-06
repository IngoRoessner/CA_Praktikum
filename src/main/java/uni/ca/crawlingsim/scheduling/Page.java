package uni.ca.crawlingsim.scheduling;

public class Page {
	private int rank;
	private String url;
	public boolean done;
	
	public Page(String url){
		this.done = false;
		this.url = url;
		this.rank = 0;
	}

	public int getRank() {
		return this.rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getUrl() {
		return this.url;
	}
	
}
