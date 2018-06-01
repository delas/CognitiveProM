package cognitiveprom.log.decorative;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.processmining.framework.util.Pair;

public class DecorativeData {

	private Map<String, Map<String, List<Pair<Date, Double>>>> structure;
	
	public DecorativeData() {
		structure = new HashMap<String, Map<String, List<Pair<Date, Double>>>>();
	}
	
	public void addMeasure(String subject, String metric, Date timestamp, Double value) {
		if (!structure.containsKey(subject)) {
			structure.put(subject, new HashMap<String, List<Pair<Date, Double>>>());
		}
		Map<String, List<Pair<Date, Double>>> metrics = structure.get(subject);
		if (!metrics.containsKey(metric)) {
			metrics.put(metric, new LinkedList<Pair<Date, Double>>());
		}
		metrics.get(metric).add(new Pair<Date, Double>(timestamp, value));
	}
	
	public Set<String> getSubjects() {
		return structure.keySet();
	}
	
	public Set<String> getMetrics(String subject) {
		if (structure.containsKey(subject)) {
			return structure.get(subject).keySet();
		} else {
			return new HashSet<String>();
		}
	}
	
	public List<Pair<Date, Double>> getMetricValues(String subject, String metric) {
		if (structure.containsKey(subject)) {
			if (structure.get(subject).containsKey(metric)) {
				return structure.get(subject).get(metric);
			}
		}
		return new LinkedList<Pair<Date, Double>>();
	}
	
	public List<Pair<Date, Double>> getMetricValuesInRange(String subject, String metric, Date start, Date end) {
		List<Pair<Date, Double>> values = new LinkedList<Pair<Date, Double>>();
		for (Pair<Date, Double> val : getMetricValues(subject, metric)) {
			Date d = val.getFirst();
			if ((d.after(start) || d.equals(start)) && (d.before(end) || d.equals(end))) {
				values.add(val);
			}
		}
		return values;
	}
}
