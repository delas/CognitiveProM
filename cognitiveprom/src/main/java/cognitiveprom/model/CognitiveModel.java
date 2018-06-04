package cognitiveprom.model;

import org.processmining.dataawarecnetminer.model.EventRelationStorage;

public class CognitiveModel {

	private EventRelationStorage eventRelationStorage;
	private Long maxAllowedToCut;
	
	public CognitiveModel(EventRelationStorage eventRelationStorage, long maxAllowedToCut) {
		this.eventRelationStorage = eventRelationStorage;
		this.maxAllowedToCut = maxAllowedToCut;
	}

	public Long getMaxAllowedToCut() {
		return maxAllowedToCut;
	}
	
	public EventRelationStorage getEventRelationStorage() {
		return eventRelationStorage;
	}
}
