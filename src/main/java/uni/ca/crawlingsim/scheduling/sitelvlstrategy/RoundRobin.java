package uni.ca.crawlingsim.scheduling.sitelvlstrategy;

import uni.ca.crawlingsim.scheduling.SchedulerInterface;
import uni.ca.crawlingsim.scheduling.Site;

public class RoundRobin implements SiteLevelStrategy {

	@Override
	public void setRanks(SchedulerInterface scheduler) {
		//do nothing
	}

	@Override
	public void donePollOn(SchedulerInterface scheduler, Site site) {
		if(!site.isEmpty()){
			scheduler.getQueue().remove(0);
			scheduler.getQueue().add(site);
		}
	}
	
	@Override
	public void sort(SchedulerInterface scheduler){
		//do nothing
	}

}
