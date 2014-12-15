package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.Page;
import uni.ca.crawlingsim.scheduling.Site;
import uni.ca.crawlingsim.scheduling.pagelvlstrategy.BacklinkCount;
import uni.ca.crawlingsim.scheduling.pagelvlstrategy.OPIC;

public class OPICtest {

	private void addPageToSite(Site site, String pageUrl){
		Page page = new Page(pageUrl);
		site.getPages().put(pageUrl, page);
		site.getQueue().add(page);
	}
	
	@Test
	public void test() {
		OPIC opic = new OPIC();
		opic.incomingLinks(Arrays.asList(
			new Link("a", "b"),
			new Link("a", "c"),
			new Link("a", "d"),
			new Link("c", "b"),
			new Link("e", "f")
		));
		Site site = new Site("");
		addPageToSite(site, "a");
		addPageToSite(site, "b");
		addPageToSite(site, "c");
		addPageToSite(site, "d");
		addPageToSite(site, "e");
		addPageToSite(site, "f");
		opic.setRank(site);
		for(Page page : site.getQueue()){
			String url = page.getUrl();
			switch(url){
				case "a":
					assertEquals(page.getRank(), 0);
					break;
				case "b":
					assertEquals(page.getRank(), (50000/3)*2);
					break;
				case "c":
					assertEquals(page.getRank(), 0);
					break;
				case "d":
					assertEquals(page.getRank(), (50000/3));
					break;
				case "e":
					assertEquals(page.getRank(), 0);
					break;
				case "f":
					assertEquals(page.getRank(), 50000);
					break;
			}
		}
		
		opic.incomingLinks(Arrays.asList(
			new Link("a", "b"),
			new Link("a", "c"),
			new Link("a", "d"),
			new Link("c", "b"),
			new Link("e", "f")
		));
		
		opic.setRank(site);
		for(Page page : site.getQueue()){
			String url = page.getUrl();
			switch(url){
				case "a":
					assertEquals(page.getRank(), 0);
					break;
				case "b":
					assertEquals(page.getRank(), (50000/3)*2);
					break;
				case "c":
					assertEquals(page.getRank(), 0);
					break;
				case "d":
					assertEquals(page.getRank(), (50000/3));
					break;
				case "e":
					assertEquals(page.getRank(), 0);
					break;
				case "f":
					assertEquals(page.getRank(), 50000);
					break;
			}
		}
	}


}
