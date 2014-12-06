package uni.ca.crawlingsim.scheduling.pagelvlstrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.SchedulterInterface;

public class OPIC implements PageLevelStrategy {
	private static int startCash = 1000;
	private static boolean onlySeedStartCash = true;
	private Map<String, Integer> cashMap;
	
	public OPIC(){
		this.cashMap = new HashMap<String, Integer>();
	}
	
	@Override
	public void setRanks(SchedulterInterface schedulter) {
		
	}

	@Override
	public void incommingLinks(List<Link> links) {
		Map<String, List<String>> linkMap = new HashMap<String, List<String>>();
		for(Link link : links){
			if(!linkMap.containsKey(link.from)){
				linkMap.put(link.from, new ArrayList<String>());
			}
			List<String> toList = linkMap.get(link.from);
			toList.add(link.to);
		}
		linkMap.forEach((from, toList)->{
			int fromCash = this.getCash(from);
			int toCash = fromCash/(toList.size());
			toList.forEach(to->{this.addCash(to, toCash);});
			this.cashMap.put(from, 0);
		});
	}

	private void addCash(String to, int toCash) {
		int startCashFoNonSeed = onlySeedStartCash ? 0 : startCash;
		if(!this.cashMap.containsKey(to)){
			this.cashMap.put(to, startCashFoNonSeed);
		}
		int origCash = this.cashMap.get(to);
		this.cashMap.put(to, origCash + toCash);
	}

	private int getCash(String from) {
		if(!this.cashMap.containsKey(from)){
			this.cashMap.put(from, startCash);
		}
		return this.cashMap.get(from);	
	}

}
