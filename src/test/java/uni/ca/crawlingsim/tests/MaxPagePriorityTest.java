package uni.ca.crawlingsim.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uni.ca.crawlingsim.scheduling.Page;
import uni.ca.crawlingsim.scheduling.Site;
import uni.ca.crawlingsim.scheduling.sitelvlstrategy.MaxPagePriority;

public class MaxPagePriorityTest {
	
	private Site getSite(String url, int[] ranks){
		Site result = new Site(url);
		List<Page> pages = new ArrayList<Page>();
		int counter = 0;
		for(int rank : ranks){
			Page page = new Page(Integer.toString(counter++));
			page.setRank(rank);
			pages.add(page);
		}
		result.getQueue().addAll(pages);
		return result;
	}

	@Test
	public void test() {
		MaxPagePriority mpp = new MaxPagePriority();
		List<Site> sites = new ArrayList<Site>();
		int[] ranksA = {0, 2, 1, 0};
		sites.add(this.getSite("a", ranksA));
		
		int[] ranksB = {0, 2, 1, 5};
		sites.add(this.getSite("b", ranksB));
		
		int[] ranksC = {1, 2, 0, 3};
		sites.add(this.getSite("c", ranksC));
		
		int[] ranksD = {0, 0, 0, 0};
		sites.add(this.getSite("d", ranksD));
		
		mpp.setRanks(sites);
		for(Site site : sites){
			String url = site.getUrl();
			switch(url){
				case "a":
					assertEquals(site.getRank(), 2);
					break;
				case "b":
					assertEquals(site.getRank(), 5);
					break;
				case "c":
					assertEquals(site.getRank(), 3);
					break;
				case "d":
					assertEquals(site.getRank(), 0);
					break;
			}
		}
	}

}
