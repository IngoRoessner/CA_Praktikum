package uni.ca.crawlingsim.scheduling.sitelvlstrategy;
/**
 * Class RoundRobin, implements the roundRobin algorithm
 * @author Ingo Rößner, Daniel Michalke
 */
import uni.ca.crawlingsim.scheduling.SchedulerInterface;
import uni.ca.crawlingsim.scheduling.Site;

public class RoundRobin implements SiteLevelStrategy {
	/**
	 * Method setRanks 
	 * @input scheduler
	 */
	@Override
	public void setRanks(SchedulerInterface scheduler) {
		//do nothing
	}
	/**
	 * Method donePollOn 
	 * @param scheduler
	 * @param site
	 */
	@Override
	public void donePollOn(SchedulerInterface scheduler, Site site) {
		if(!site.isEmpty() && scheduler.getQueue().get(0) == site){
			scheduler.getQueue().remove(0);
			scheduler.getQueue().add(site);
		}
	}
	
	@Override
	public void sort(SchedulerInterface scheduler){
		//do nothing
	}

}
