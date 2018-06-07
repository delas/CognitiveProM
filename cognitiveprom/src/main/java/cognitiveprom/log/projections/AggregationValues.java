package cognitiveprom.log.projections;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;

import cognitiveprom.log.utils.XCognitiveLogHelper;

public class AggregationValues implements Comparable<AggregationValues> {

	public static AggregationValues FREQUENCY = new AggregationValues("Frequency in trace", true) {
		@Override
		public List<Double> getValues(XTrace trace, String AOIName) {
			List<Double> values = new ArrayList<Double>();
			double counter = 0;
			for (XEvent event : trace) {
				if (AOIName.equals(XCognitiveLogHelper.getAOIName(event))) {
					counter++;
				}
			}
			values.add(counter);
			return values;
		}
	};
	
	public static AggregationValues NONE = new AggregationValues("None", true) {
		@Override
		public List<Double> getValues(XTrace trace, String AOIName) {
			return new ArrayList<Double>();
		}
	};

	protected String attributeName = null;
	protected boolean isSpecialFunction;
	
	protected AggregationValues(String attributeName, boolean isSpecialFunction) {
		this.attributeName = attributeName;
		this.isSpecialFunction = isSpecialFunction;
	}
	
	public AggregationValues(String attributeName) {
		this(attributeName, false);
	}
	
	public String getAttributeName() {
		return attributeName;
	}
	
	public List<Double> getValues(XTrace trace, String AOIName) {
		List<Double> values = new ArrayList<Double>();
		for (XEvent event : trace) {
			if (AOIName.equals(XCognitiveLogHelper.getAOIName(event))) {
				if (XCognitiveLogHelper.hasDoubleAttribute(event, attributeName)) {
					values.add(XCognitiveLogHelper.getDoubleAttribute(event, attributeName));
				}
				if (XCognitiveLogHelper.hasLongAttribute(event, attributeName)) {
					values.add(XCognitiveLogHelper.getLongAttribute(event, attributeName).doubleValue());
				}
			}
		}
		return values;
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
		boolean attName = (attributeName == null)? (rhs.attributeName == null) : attributeName.equals(rhs.attributeName);
		return (isSpecialFunction == rhs.isSpecialFunction) && attName;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(isSpecialFunction)
				.append(attributeName)
				.toHashCode();
	}
	
	@Override
	public String toString() {
		if (isSpecialFunction) {
			return "Function - " + attributeName;
		} else {
			return "Attribute - " + attributeName;
		}
	}

	public int compareTo(AggregationValues o) {
		if (isSpecialFunction == o.isSpecialFunction) {
			return attributeName.compareTo(o.attributeName);
		} else {
			return (isSpecialFunction == true)? 1 : -1;
		}
	}
}
