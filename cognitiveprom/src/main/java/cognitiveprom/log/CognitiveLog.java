package cognitiveprom.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.deckfour.xes.extension.std.XTimeExtension;
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
import cognitiveprom.log.utils.XCognitiveLogHelper;

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

	private XLog log;
	private Map<Pair<Collection<XTrace>, ValueProjector>, Map<String, AggregationFunction>> summaryCache;
	private Map<String, XTrace> tracesCache;
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
		
		resetCaches();
	}

	/**
	 * Merge the provided log into the current one.
	 * 
	 * The merging, actually, happens as the concatenation of the traces of the
	 * old log with the traces of the new one. Therefore, if two subject names
	 * are the same, then they are merged into the same trace (where events are
	 * sorted per timestamp)
	 * 
	 * @param newLog
	 */
	public void merge(CognitiveLog newLog) {
		XLog newXLog = newLog.getLog();
		
		for (XTrace t : newXLog) {
			XTrace oldT = get(XCognitiveLogHelper.getSubjectName(t));
			
			// do we have already a trace for this subject?
			if (oldT == null) {
				// nope, just add the trace to the log
				log.add(t);
			} else {
				// yup, append the new trace to the existing one
				log.removeIf(new Predicate<XTrace>() {
					@Override
					public boolean test(XTrace tr) {
						return XCognitiveLogHelper.getSubjectName(tr).equals(XCognitiveLogHelper.getSubjectName(oldT));
					}
				});
				oldT.addAll(t);
				log.add(oldT);
				
				// sort and merge contiguous events
				XCognitiveLogHelper.sortXLog(log);
//				XCognitiveLogHelper.mergeEventsWithSameName(log);
			}
		}
		
		resetCaches();
	}
	
	/**
	 * 
	 */
	private void resetCaches() {
		this.summaryCache = new HashMap<Pair<Collection<XTrace>, ValueProjector>, Map<String, AggregationFunction>>();
		this.tracesCache = new HashMap<String, XTrace>();
		for(XTrace trace : log) {
			tracesCache.put(XCognitiveLogHelper.getSubjectName(trace), trace);
		}
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
	 * Returns the list of all subjects recorded in the log
	 * 
	 * @return
	 */
	public Collection<String> getSubjectNames() {
		return tracesCache.keySet();
	}
	
	/**
	 * Returns the {@link XTrace} associated with the given subject name
	 * 
	 * @param subjectName the name of the subject associated with the trace
	 * @return
	 */
	public XTrace get(String subjectName) {
		return tracesCache.get(subjectName);
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
				String activity = XCognitiveLogHelper.getAOIName(event);
				if (!processedActivities.contains(activity)) {
					if (!aggregators.containsKey(activity)) {
						aggregators.put(activity, new AggregationFunction(tracesToConsider.size()));
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
