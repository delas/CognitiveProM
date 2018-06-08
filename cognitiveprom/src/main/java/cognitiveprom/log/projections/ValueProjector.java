package cognitiveprom.log.projections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Triple;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.processmining.dataawarecnetminer.model.EventRelationStorage;
import org.processmining.framework.util.Pair;

import cognitiveprom.log.utils.XCognitiveLogHelper;

/**
 * 
 * @author Andrea Burattin
 */
public class ValueProjector implements Comparable<ValueProjector> {

	public static ValueProjector FREQUENCY = new ValueProjector("Frequency in trace", true) {
		@Override
		public List<Double> getValuesForCaching(XTrace trace, String AOIName) {
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
		
		@Override
		public List<Double> getValuesForCaching(XTrace trace, String AOISource, String AOITarget) {
			System.out.println("counting");
			List<Double> values = new ArrayList<Double>();
			// start activity case
			if (AOISource.equals(EventRelationStorage.ARTIFICIAL_START)) {
				if (AOITarget.equals(XCognitiveLogHelper.getAOIName(trace.get(0)))) {
					values.add(1d);
				} else {
					values.add(0d);
				}
				return values;
			}
			
			// end activity case
			if (AOITarget.equals(EventRelationStorage.ARTIFICIAL_END)) {
				if (AOISource.equals(XCognitiveLogHelper.getAOIName(trace.get(trace.size() - 1)))) {
					values.add(1d);
				} else {
					values.add(0d);
				}
				return values;
			}
			
			// general case
			double val = 0d;
			for (int i = 0; i < trace.size() - 1; i++) {
				if (XCognitiveLogHelper.getAOIName(trace.get(i)).equals(AOISource) &&
						XCognitiveLogHelper.getAOIName(trace.get(i + 1)).equals(AOITarget)) {
					val++;
				}
			}
			values.add(val);
			return values;
		}
	};
	
	public static ValueProjector NONE = new ValueProjector("None", true) {
		@Override
		public List<Double> getValuesForCaching(XTrace trace, String AOIName) {
			return new ArrayList<Double>();
		}
	};

	protected String attributeName = null;
	protected boolean isSpecialFunction;
	private Map<Triple<String, String, String>, List<Double>> cachedTraceValues = new HashMap<Triple<String, String, String>, List<Double>>();
	private Map<Pair<String, String>, List<Double>> cachedActivityValues = new HashMap<Pair<String, String>, List<Double>>();
	
	protected ValueProjector(String attributeName, boolean isSpecialFunction) {
		this.attributeName = attributeName;
		this.isSpecialFunction = isSpecialFunction;
	}
	
	public ValueProjector(String attributeName) {
		this(attributeName, false);
	}
	
	public String getProjectionName() {
		return attributeName;
	}
	
	public final List<Double> getValues(XTrace trace, String AOIName) {
		Pair<String, String> key = new Pair<String, String>(XCognitiveLogHelper.getSubjectName(trace), AOIName);
		if (!cachedActivityValues.containsKey(key)) {
			cachedActivityValues.put(key, getValuesForCaching(trace, AOIName));
		}
		return cachedActivityValues.get(key);
	}
	
	protected List<Double> getValuesForCaching(XTrace trace, String AOIName) {
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

	public final List<Double> getValues(XTrace trace, String AOISource, String AOITarget) {
		Triple<String, String, String> key = Triple.of(XCognitiveLogHelper.getSubjectName(trace), AOISource, AOITarget);
		if (!cachedTraceValues.containsKey(key)) {
			cachedTraceValues.put(key, getValuesForCaching(trace, AOISource, AOITarget));
		}
		return cachedTraceValues.get(key);
	}
	
	protected List<Double> getValuesForCaching(XTrace trace, String AOISource, String AOITarget) {
		return new ArrayList<Double>();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ValueProjector)) {
			return false;
		}
		ValueProjector rhs = (ValueProjector) obj;
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

	public int compareTo(ValueProjector o) {
		if (isSpecialFunction == o.isSpecialFunction) {
			return attributeName.compareTo(o.attributeName);
		} else {
			return (isSpecialFunction == true)? 1 : -1;
		}
	}
}
