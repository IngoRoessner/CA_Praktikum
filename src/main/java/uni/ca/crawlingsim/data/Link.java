package uni.ca.crawlingsim.data;

public class Link{
	public String from;
	public String to;
	public boolean quality;
	
	public Link(String from, String to, boolean quality){
		this.from = from;
		this.to = to;
		this.quality = quality;
	}
	
	public Link(String from, String to){
		this.from = from;
		this.to = to;
		this.quality = false;
	}
}
