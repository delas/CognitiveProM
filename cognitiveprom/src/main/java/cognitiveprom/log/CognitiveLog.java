package cognitiveprom.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeContinuous;
import org.deckfour.xes.model.XAttributeDiscrete;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.framework.util.Pair;

import cognitiveprom.log.io.CognitiveLogImporter;
import cognitiveprom.log.projections.AggregationFunction;
import cognitiveprom.log.projections.ValueProjector;
import cognitiveprom.tools.XLogHelper;

/**
 * This class represents a cognitive log. Specifically, a cognitive an XES log
 * which can (potentially) include cognitive information. Examples of such
 * cognitive information are coming from eye-tracking.
 * 
 * Iterating over a cognitive log means to iterate over the {@link XTrace}s it
 * wraps.
 * 
 * To import a cognitive log it is possible to use {@link CognitiveLogImporter}s
 * 
 * @author Andrea Burattin
 */
public class CognitiveLog implements Iterable<XTrace> {

	private Map<Pair<Collection<XTrace>, ValueProjector>, Map<String, AggregationFunction>> summaryCache;
	private XLog log;
	private List<ValueProjector> projectableAttributes = null;
	
	/**
	 * Constructor to create a new cognitive log wrapping a {@link XLog}.
	 * 
	 * To import a cognitive log it is possible to use
	 * {@link CognitiveLogImporter}s.
	 * 
	 * @param log the XES log to be wrapped
	 */
	public CognitiveLog(XLog log) {
		this.log = log;
		this.summaryCache = new HashMap<Pair<Collection<XTrace>, ValueProjector>, Map<String, AggregationFunction>>();
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
	public Map<String, AggregationFunction> getSummary(Collection<XTrace> tracesToConsider, ValueProjector attribute) {
		Pair<Collection<XTrace>, ValueProjector> key = new Pair<Collection<XTrace>, ValueProjector>(tracesToConsider, attribute);
		if (!summaryCache.containsKey(key)) {
			summaryCache.put(key, constructSummary(tracesToConsider, attribute));
		}
		return summaryCache.get(key);
	}
	
	/**
	 * Computes a summary of the attribute, to be cached in
	 * {@link #getSummary(Collection, ValueProjector)}
	 * 
	 * @param tracesToConsider
	 * @param attribute
	 * @return
	 */
	private Map<String, AggregationFunction> constructSummary(Collection<XTrace> tracesToConsider, ValueProjector attribute) {
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
	
	/**
	 * Returns a cached list of attributes that can be associated with
	 * activities as they have numerical nature (i.e., they are either
	 * {@link XAttributeDiscrete} or {@link XAttributeContinuous}).
	 * 
	 * @return the cached list of attributes
	 */
	public List<ValueProjector> getProjectableAttributes() {
		if (projectableAttributes == null) {
			int MAX_TRACES_TO_CHECK = 10;
			
			// get all attributes that can be used for the timing
			Set<String> candidateAttributes = new HashSet<String>();
			for(int i = 0; i < MAX_TRACES_TO_CHECK && i <log.size(); i++) {
				XTrace trace = log.get(i);
				for (XEvent e : trace) {
					for (String attributeName : e.getAttributes().keySet()) {
						if (!candidateAttributes.contains(attributeName)) {
							XAttribute a = e.getAttributes().get(attributeName);
							if (a instanceof XAttributeDiscrete || a instanceof XAttributeContinuous) {
								candidateAttributes.add(attributeName);
							}
						}
					}
				}
			}
			for(int i = 0; i < MAX_TRACES_TO_CHECK && i <log.size(); i++) {
				XTrace trace = log.get(i);
				for (XEvent e : trace) {
					Iterator<String> iterAttributes = candidateAttributes.iterator();
					while(iterAttributes.hasNext()) {
						if (!e.getAttributes().containsKey(iterAttributes.next())) {
							iterAttributes.remove();
						}
					}
				}
			}
			
			projectableAttributes = new ArrayList<ValueProjector>(candidateAttributes.size());
			for (String attr : candidateAttributes) {
				projectableAttributes.add(new ValueProjector(attr));
			}
			Collections.sort(projectableAttributes);
		}
		return projectableAttributes;
	}
	
	@Override
	public Iterator<XTrace> iterator() {
		return log.iterator();
	}
}
