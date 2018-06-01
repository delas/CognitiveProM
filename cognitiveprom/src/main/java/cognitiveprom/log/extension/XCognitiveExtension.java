package cognitiveprom.log.extension;

import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.info.XGlobalAttributeNameMap;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeBoolean;
import org.deckfour.xes.model.XAttributeContinuous;
import org.deckfour.xes.model.XAttributeDiscrete;
import org.deckfour.xes.model.XAttributeList;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;
import org.processmining.framework.util.Pair;

/**
 * This is the extension to describe cognitive data
 * 
 * @author Andrea Burattin
 */
public class XCognitiveExtension extends XExtension {

	private static final long serialVersionUID = -9164198340987494850L;
	private static XFactory factory = XFactoryRegistry.instance().currentDefault();
	
	/**
	 * Unique URI of this extension.
	 */
	public static final URI EXTENSION_URI = URI.create("https://andrea.burattin.net/public/cognitive.xesext");

	/**
	 * Keys for the attributes.
	 */
	public static final String KEY_SUBJECT_NAME = "cognitive:subjectName";
	public static final String KEY_IS_STIMULUS = "cognitive:isStimulus";
	public static final String KEY_AOI = "cognitive:aoi";
	public static final String KEY_DURATION = "cognitive:duration";
	public static final String KEY_METRICS = "cognitive:metrics";
	public static final String KEY_METRIC = "cognitive:metric";
	public static final String KEY_METRIC_VALUE = "cognitive:metricValue";
	
	/**
	 * Attribute prototypes
	 */
	public static XAttributeLiteral ATTR_SUBJECT_NAME;
	public static XAttributeBoolean ATTR_IS_STIMULUS;
	public static XAttributeLiteral ATTR_AOI;
	public static XAttributeDiscrete ATTR_DURATION;
	public static XAttributeList ATTR_METRICS;
	public static XAttributeContinuous ATTR_METRIC_VALUE;

	/**
	 * Singleton instance of this extension.
	 */
	private static XCognitiveExtension singleton = new XCognitiveExtension();

	/**
	 * Provides access to the singleton instance.
	 * 
	 * @return Singleton extension.
	 */
	public static XCognitiveExtension instance() {
		return singleton;
	}
	
	/**
	 * Private constructor. Use {@link #instance()} to access the extension
	 */
	private XCognitiveExtension() {
		super("Cognitive", "cognitive", EXTENSION_URI);
		
		ATTR_SUBJECT_NAME = factory.createAttributeLiteral(KEY_SUBJECT_NAME, "__INVALID__", this);
		ATTR_IS_STIMULUS = factory.createAttributeBoolean(KEY_IS_STIMULUS, false, this);
		ATTR_AOI = factory.createAttributeLiteral(KEY_AOI, "__INVALID__", this);
		ATTR_DURATION = factory.createAttributeDiscrete(KEY_DURATION, -1, this);
		ATTR_METRICS = factory.createAttributeList(KEY_METRICS, this);
		ATTR_METRIC_VALUE = factory.createAttributeContinuous(KEY_METRIC_VALUE, -1, this);
		
		eventAttributes.add((XAttribute) ATTR_SUBJECT_NAME.clone());
		eventAttributes.add((XAttribute) ATTR_IS_STIMULUS.clone());
		eventAttributes.add((XAttribute) ATTR_AOI.clone());
		eventAttributes.add((XAttribute) ATTR_DURATION.clone());
		eventAttributes.add((XAttribute) ATTR_METRICS.clone());
		
		XGlobalAttributeNameMap gap = XGlobalAttributeNameMap.instance();
		gap.registerMapping(XGlobalAttributeNameMap.MAPPING_ENGLISH, KEY_SUBJECT_NAME, "Subject name");
		gap.registerMapping(XGlobalAttributeNameMap.MAPPING_ENGLISH, KEY_IS_STIMULUS, "Whether the event refers to a stimulus");
		gap.registerMapping(XGlobalAttributeNameMap.MAPPING_ENGLISH, KEY_AOI, "Area of interest of the fixation");
		gap.registerMapping(XGlobalAttributeNameMap.MAPPING_ENGLISH, KEY_DURATION, "Fixation duration");
		gap.registerMapping(XGlobalAttributeNameMap.MAPPING_ENGLISH, KEY_METRICS, "List of metrics");
		gap.registerMapping(XGlobalAttributeNameMap.MAPPING_ENGLISH, KEY_METRIC_VALUE, "Value for the given metric");
	}
	
	public void assignSubjectName(XTrace element, String subjectName) {
		if (subjectName != null && subjectName.trim().length() > 0) {
			XAttributeLiteral attr = (XAttributeLiteral) ATTR_SUBJECT_NAME.clone();
			attr.setValue(subjectName);
			element.getAttributes().put(KEY_SUBJECT_NAME, attr);
		}
	}
	
	public String extractSubjectName(XTrace element) {
		XAttribute attr = element.getAttributes().get(KEY_SUBJECT_NAME);
		if (attr == null) {
			return null;
		} else {
			return ((XAttributeLiteral) attr).getValue();
		}
	}
	
	public void assignIsStimulus(XEvent element, boolean isStimulus) {
		XAttributeBoolean attr = (XAttributeBoolean) ATTR_IS_STIMULUS.clone();
		attr.setValue(isStimulus);
		element.getAttributes().put(KEY_IS_STIMULUS, attr);
	}
	
	/**
	 * 
	 * <b>Attention:</b> default value is <tt>false</tt>.
	 * 
	 * @param element
	 * @return
	 */
	public boolean extractIsStimulus(XEvent element) {
		XAttribute attr = element.getAttributes().get(KEY_IS_STIMULUS);
		if (attr == null) {
			return false;
		} else {
			return ((XAttributeBoolean) attr).getValue();
		}
	}
	
	public void assignAOI(XEvent element, String AOI) {
		if (AOI != null && AOI.trim().length() > 0) {
			XAttributeLiteral attr = (XAttributeLiteral) ATTR_AOI.clone();
			attr.setValue(AOI);
			element.getAttributes().put(KEY_AOI, attr);
		}
	}
	
	public String extractAOI(XEvent element) {
		XAttribute attr = element.getAttributes().get(KEY_AOI);
		if (attr == null) {
			return null;
		} else {
			return ((XAttributeLiteral) attr).getValue();
		}
	}
	
	public void assignDuration(XEvent element, long duration) {
		XAttributeDiscrete attr = (XAttributeDiscrete) ATTR_DURATION.clone();
		attr.setValue(duration);
		element.getAttributes().put(KEY_DURATION, attr);
	}
	
	public Long extractDuration(XEvent element) {
		XAttribute attr = element.getAttributes().get(KEY_DURATION);
		if (attr == null) {
			return 0l;
		} else {
			return ((XAttributeDiscrete) attr).getValue();
		}
	}
	
	public void addMetric(XEvent element, String metricName, Date timestamp, Double value) {
		XAttributeList list = (XAttributeList) element.getAttributes().get(KEY_METRICS);
		if (list == null) {
			list = (XAttributeList) ATTR_METRICS.clone();
			element.getAttributes().put(KEY_METRICS, list);
		}
		
		XAttributeLiteral metric = factory.createAttributeLiteral(KEY_METRIC, metricName, null);
		metric.getAttributes().put(XTimeExtension.KEY_TIMESTAMP, new XAttributeTimestampImpl(XTimeExtension.KEY_TIMESTAMP, timestamp));
		metric.getAttributes().put(KEY_METRIC_VALUE, factory.createAttributeContinuous(KEY_METRIC_VALUE, value, this));
		list.addToCollection(metric);
	}
	
	public List<Pair<Date, Double>> extractMetricValues(XEvent element, String metricName) {
		List<Pair<Date, Double>> values = new LinkedList<Pair<Date, Double>>();
		XAttributeList list = (XAttributeList) element.getAttributes().get(KEY_METRICS);
		if (list != null) {
			for(XAttribute metricValue : list.getCollection()) {
				if (metricValue.toString().equals(metricName)) {
					Date time = ((XAttributeTimestamp) metricValue.getAttributes().get(XTimeExtension.KEY_TIMESTAMP)).getValue();
					Double value = ((XAttributeContinuous) metricValue.getAttributes().get(KEY_METRIC_VALUE)).getValue();
					values.add(new Pair<Date, Double>(time, value));
				}
			}
		}
		
		Collections.sort(values, new Comparator<Pair<Date, Double>>() {
			@Override
			public int compare(Pair<Date, Double> o1, Pair<Date, Double> o2) {
				return o1.getFirst().compareTo(o2.getFirst());
			}
		});
		
		return values;
	}
	
	public Set<String> extractMetricNames(XEvent element) {
		Set<String> metricValues = new HashSet<String>();
		XAttributeList attr = (XAttributeList) element.getAttributes().get(KEY_METRICS);
		if (attr != null) {
			for(XAttribute value : attr.getCollection()) {
				metricValues.add(value.toString());
			}
		}
		return metricValues;
	}
}
