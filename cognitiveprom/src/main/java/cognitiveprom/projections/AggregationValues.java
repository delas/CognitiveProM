package cognitiveprom.projections;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeContinuous;
import org.deckfour.xes.model.XAttributeDiscrete;
import org.deckfour.xes.model.XEvent;

public class AggregationValues implements Comparable<AggregationValues> {

	public static AggregationValues FREQUENCY = new AggregationValues(null, "Event frequency") {
		@Override
		public Number getValue(XEvent event) {
			return 1;
		}
	};


	private String specialFunction = null;
	private String attributeName = null;
	
	public AggregationValues(String attributeName) {
		this(attributeName, null);
	}
	
	private AggregationValues(String attributeName, String specialFunction) {
		this.attributeName = attributeName;
		this.specialFunction = specialFunction;
	}
	
	public String getAttributeName() {
		return attributeName;
	}
	
	public Number getValue(XEvent event) {
		XAttribute a = event.getAttributes().get(attributeName);
		if (a instanceof XAttributeDiscrete) {
			return ((XAttributeDiscrete) a).getValue();
		} else if (a instanceof XAttributeContinuous) {
			return ((XAttributeContinuous) a).getValue();
		}
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AggregationValues)) {
			return false;
		}
		AggregationValues rhs = (AggregationValues) obj;
		boolean att1 = (specialFunction == null)? (rhs.specialFunction == null) : specialFunction.equals(rhs.specialFunction);
		boolean att2 = (attributeName == null)? (rhs.attributeName == null) : attributeName.equals(rhs.attributeName);
		return att1 && att2;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(specialFunction)
				.append(attributeName)
				.toHashCode();
	}
	
	@Override
	public String toString() {
		if (specialFunction != null) {
			return "Function - " + specialFunction;
		} else if (attributeName != null) {
			return "Attribute - " + attributeName;
		} else {
			return "";
		}
	}

	@Override
	public int compareTo(AggregationValues o) {
		if (specialFunction != null && o.specialFunction != null) {
			return specialFunction.compareTo(o.specialFunction);
		} else if (attributeName != null && o.attributeName != null) {
			return attributeName.compareTo(o.attributeName);
		} else {
			return (specialFunction != null && o.attributeName == null)? -1 : 1;
		}
	}
}
