package cognitiveprom.map;

import java.util.HashSet;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.dataawarecnetminer.model.EventRelationStorage;
import org.processmining.models.causalgraph.Relation;

import com.google.common.collect.Multiset.Entry;

import cognitiveprom.log.CognitiveLog;

/**
 * This class represents a process mined out of a {@link CognitiveLog}
 * 
 * @author Andrea Burattin
 */
public class ProcessMap {

	private EventRelationStorage eventRelationStorage;
	private Long maxAllowedToCut;
	
	// cached values
	private Long mostFrequentActivity = null;
	
	public ProcessMap(EventRelationStorage eventRelationStorage, long maxAllowedToCut) {
		this.eventRelationStorage = eventRelationStorage;
		this.maxAllowedToCut = maxAllowedToCut;
	}

	public Long getMaxAllowedToCut() {
		return maxAllowedToCut;
	}
	
	public Set<XEventClass> getActivities() {
		Set<XEventClass> acts = new HashSet<XEventClass>();
		for (Entry<Relation> relation : eventRelationStorage.getDirectlyFollowsRelations().entrySet()) {
			acts.add(relation.getElement().getSource());
			acts.add(relation.getElement().getTarget());
		}
		return acts;
	}
	
	public EventRelationStorage getEventRelationStorage() {
		return eventRelationStorage;
	}
	
	public Long getMostFrequencActivity() {
		if (mostFrequentActivity == null) {
			mostFrequentActivity = 0l;
			for (Relation r : eventRelationStorage.getDirectlyFollowsRelations()) {
				mostFrequentActivity = Math.max(mostFrequentActivity,
						Math.max(eventRelationStorage.countOccurence(r.getSource()), eventRelationStorage.countOccurence(r.getTarget())));
			}
		}
		return mostFrequentActivity;
	}
}
