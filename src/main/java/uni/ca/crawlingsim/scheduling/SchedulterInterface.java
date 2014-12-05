package uni.ca.crawlingsim.scheduling;

import java.util.List;
import java.util.Map;

import uni.ca.crawlingsim.data.Link;

public interface SchedulterInterface {
	public Map<String, Site> getDone();
	public List<Site> getQueue();
	public void addAll(List<Link> links);
	public String poll();
	public boolean isEmpty();
}
