package cognitiveprom.log;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.framework.util.Pair;

import cognitiveprom.projections.AggregationFunction;
import cognitiveprom.projections.AggregationValues;
import cognitiveprom.tools.XLogHelper;

/**
 * This class represents a cognitive log. Specifically, a cognitive an XES log which can (potentially) include cognitive
 * information. Examples of such cognitive information are coming from eye-tracking.
 * 
 * Iterating over a cognitive log means to iterate over the {@link XTrace}s it wraps.
 * 
 * To import a cognitive log it is possible to use {@link CognitiveLogImporter}s
 * 
 * @author Andrea Burattin
 */
public class CognitiveLog implements Iterable<XTrace> {

	private Map<Pair<Collection<XTrace>, AggregationValues>, Map<String, AggregationFunction>> summaryCache;
	private XLog log;
	
	/**
	 * Constructor to create a new cognitive log wrapping a {@link XLog}.
	 * 
	 * To import a cognitive log it is possible to use {@link CognitiveLogImporter}s.
	 * 
	 * @param log the XES log to be wrapped
	 */
	public CognitiveLog(XLog log) {
		this.log = log;
		this.summaryCache = new HashMap<Pair<Collection<XTrace>, AggregationValues>, Map<String, AggregationFunction>>();
	}
	
	/**
	 * Returns the XES log
	 * 
	 * @return the log
	 */
	public XLog getLog() {
		return log;
	}
	
	/**
	 * Returns a summary of the attributes for the given attribute name
	 * 
	 * @param tracesToConsider the subset of traces to consider for the summary
	 * @param attribute the attribute to inspect
	 * @return a map where each activity is mapped to an aggregation function
	 */
	public Map<String, AggregationFunction> getSummary(Collection<XTrace> tracesToConsider, AggregationValues attribute) {
		Pair<Collection<XTrace>, AggregationValues> key = new Pair<Collection<XTrace>, AggregationValues>(tracesToConsider, attribute);
		if (!summaryCache.containsKey(key)) {
			summaryCache.put(key, constructSummary(tracesToConsider, attribute));
		}
		return summaryCache.get(key);
	}
	
	/**
	 * Computes a summary of the attribute, to be cached in {@link #getSummary(Collection, AggregationValues)}
	 * 
	 * @param tracesToConsider
	 * @param attribute
	 * @return
	 */
	private Map<String, AggregationFunction> constructSummary(Collection<XTrace> tracesToConsider, AggregationValues attribute) {
		Map<String, AggregationFunction> aggregators = new HashMap<String, AggregationFunction>();
		for (XTrace trace : tracesToConsider) {
			Set<String> processedActivities = new HashSet<String>();
			for (XEvent event : trace) {
				String activity = XLogHelper.getName(event);
				if (!processedActivities.contains(activity)) {
					if (!aggregators.containsKey(activity)) {
						aggregators.put(activity, new AggregationFunction());
					}
					for(Double value : attribute.getValues(trace, activity)) {
						aggregators.get(activity).addObservation(value);
					}
					processedActivities.add(activity);
				}
			}
		}
		return aggregators;
	}
	
	@Override
	public Iterator<XTrace> iterator() {
		return log.iterator();
	}
}
