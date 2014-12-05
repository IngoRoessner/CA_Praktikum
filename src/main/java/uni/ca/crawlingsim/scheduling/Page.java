package uni.ca.crawlingsim.scheduling;

public class Page {
	private int rank;
	private String url;
	
	public Page(String url){
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
