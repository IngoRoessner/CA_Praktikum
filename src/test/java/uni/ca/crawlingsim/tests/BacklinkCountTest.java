package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.Page;
import uni.ca.crawlingsim.scheduling.Site;
import uni.ca.crawlingsim.scheduling.pagelvlstrategy.BacklinkCount;

public class BacklinkCountTest {

	@Test
	public void test() {
		BacklinkCount blc = new BacklinkCount();
		blc.incommingLinks(Arrays.asList(
			new Link("a", "b"),
			new Link("a", "c"),
			new Link("c", "b"),
			new Link("d", "b"),
			new Link("d", "c")
		));
		Site site = new Site("");
		site.getQueue().addAll(Arrays.asList(
			new Page("a"),
			new Page("b"),
			new Page("c"),
			new Page("d")
		));
		blc.setRanks(Arrays.asList(site));
		for(Page page : site.getQueue()){
			String url = page.getUrl();
			switch(url){
				case "a":
					assertEquals(page.getRank(), 0);
					break;
				case "b":
					assertEquals(page.getRank(), 3);
					break;
				case "c":
					assertEquals(page.getRank(), 2);
					break;
				case "d":
					assertEquals(page.getRank(), 0);
					break;
			}
		}
		
		blc.incommingLinks(Arrays.asList(
			new Link("a", "b"),
			new Link("a", "c"),
			new Link("c", "b"),
			new Link("d", "b"),
			new Link("d", "c")
		));
		
		blc.setRanks(Arrays.asList(site));
		for(Page page : site.getQueue()){
			String url = page.getUrl();
			switch(url){
				case "a":
					assertEquals(page.getRank(), 0);
					break;
				case "b":
					assertEquals(page.getRank(), 6);
					break;
				case "c":
					assertEquals(page.getRank(), 4);
					break;
				case "d":
					assertEquals(page.getRank(), 0);
					break;
			}
		}
	}

}
