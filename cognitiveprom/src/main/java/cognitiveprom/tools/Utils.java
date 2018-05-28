package cognitiveprom.tools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.dataawarecnetminer.model.EventRelationStorage;
import org.processmining.models.causalgraph.Relation;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;

import com.google.common.collect.Multiset.Entry;

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
}
