package uni.ca.crawlingsim.scheduling.sitelvlstrategy;

import uni.ca.crawlingsim.scheduling.SchedulerInterface;
import uni.ca.crawlingsim.scheduling.Site;
/**
 * Class SiteleveSTrategy, interface for RoundRObin and maxpagepriority
 * @author Ingo R��ner, Daniel Michalke
 *
 */
public interface SiteLevelStrategy {

	void setRanks(SchedulerInterface scheduler);
	void donePollOn(SchedulerInterface scheduler, Site site);
	default void sort(SchedulerInterface scheduler){
		scheduler.getQueue().sort((e1, e2)->{return e2.getRank() - e1.getRank();});
	}
}
