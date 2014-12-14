package uni.ca.crawlingsim.scheduling;

import java.util.List;
import java.util.Map;

import uni.ca.crawlingsim.data.Link;
/**
 * Class SchedulerInterface, Interface for the ShedulerClass
 * @author Ingo Rößner, Daniel Michalke
 *
 */
public interface SchedulerInterface {
	public Map<String, Site> getSites();
	public List<Site> getQueue();
	public void addAll(List<Link> links);
	public String poll();
	public boolean isEmpty();
}
