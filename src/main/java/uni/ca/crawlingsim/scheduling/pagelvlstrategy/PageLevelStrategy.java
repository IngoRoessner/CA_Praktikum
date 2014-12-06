package uni.ca.crawlingsim.scheduling.pagelvlstrategy;

import java.util.List;

import uni.ca.crawlingsim.data.Link;
import uni.ca.crawlingsim.scheduling.SchedulterInterface;

public interface PageLevelStrategy {

	void setRanks(SchedulterInterface schedulter);
	void incommingLinks(List<Link> links);

}
