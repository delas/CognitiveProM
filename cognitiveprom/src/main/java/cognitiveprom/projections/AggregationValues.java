package cognitiveprom.projections;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AggregationValues implements Comparable<AggregationValues> {

	public static AggregationValues FREQUENCY = new AggregationValues(null, "Event frequency");

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
	
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != getClass()) {
			return false;
		}
		AggregationValues rhs = (AggregationValues) obj;
		return new EqualsBuilder()
				.append(specialFunction, rhs.specialFunction)
				.append(attributeName, rhs.specialFunction)
				.isEquals();
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
