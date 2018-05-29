package cognitiveprom.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeContinuous;
import org.deckfour.xes.model.XAttributeDiscrete;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.dataawarecnetminer.model.EventRelationStorage;
import org.processmining.models.causalgraph.Relation;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;

import com.google.common.collect.Multiset.Entry;

import cognitiveprom.projections.AggregationValues;

public class Utils {

	public static Long getMostFrequencActivity(IMLogInfo logInfo) {
		List<XEventClass> acts = logInfo.getActivities().sortByCardinality();
		return logInfo.getActivities().getCardinalityOf(acts.get(acts.size() - 1));
	}
	
	public static Long getMostFrequencActivity(EventRelationStorage eventRelations) {
		long max = 0;
		for (Relation r : eventRelations.getDirectlyFollowsRelations()) {
			max = Math.max(max, Math.max(eventRelations.countOccurence(r.getSource()), eventRelations.countOccurence(r.getTarget())));
		}
		return max;
	}

	public static Long getMostFrequentRelation(EventRelationStorage eventRelations) {
		long max = 0;
		for (Relation r : eventRelations.getDirectlyFollowsRelations()) {
			if (!r.getSource().equals(eventRelations.getStartEventClass()) && !r.getTarget().equals(eventRelations.getEndEventClass())) {
				max = Math.max(max, eventRelations.countDirectlyFollows(r));
			}
		}
		return max;
	}
	
	public static Long getMostFrequentRelationStart(EventRelationStorage eventRelations) {
		long max = 0;
		for (Relation r : eventRelations.getDirectlyFollowsRelations()) {
			if (r.getSource().equals(eventRelations.getStartEventClass())) {
				max = Math.max(max, eventRelations.countDirectlyFollows(r));
			}
		}
		return max;
	}
	
	public static Long getMostFrequentRelationEnd(EventRelationStorage eventRelations) {
		long max = 0;
		for (Relation r : eventRelations.getDirectlyFollowsRelations()) {
			if (r.getTarget().equals(eventRelations.getEndEventClass())) {
				max = Math.max(max, eventRelations.countDirectlyFollows(r));
			}
		}
		return max;
	}

	public static Set<XEventClass> getActivities(EventRelationStorage eventRelations) {
		Set<XEventClass> acts = new HashSet<XEventClass>();
		for (Entry<Relation> relation : eventRelations.getDirectlyFollowsRelations().entrySet()) {
			acts.add(relation.getElement().getSource());
			acts.add(relation.getElement().getTarget());
		}
		return acts;
	}
	
	public static List<AggregationValues> getProjectableAttributes(XLog log) {
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
		
		List<AggregationValues> sortedAttributes = new ArrayList<AggregationValues>(candidateAttributes.size());
		for (String attr : candidateAttributes) {
			sortedAttributes.add(new AggregationValues(attr));
		}
		Collections.sort(sortedAttributes);
		return sortedAttributes;
	}
}
