package uni.ca.crawlingsim.scheduling.pagelvlstrategy;

import java.util.List;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.Scheduler;
import uni.ca.crawlingsim.scheduling.SchedulerInterface;

public interface PageLevelStrategy {

	void setRanks(SchedulerInterface scheduler);
	void incomingLinks(List<Link> links);
	default void sort(SchedulerInterface scheduler){
		scheduler.getQueue().parallelStream().forEach(site->{site.sort();});
	}

}
