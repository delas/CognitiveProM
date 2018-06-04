package cognitiveprom.log.utils;

import java.util.Comparator;
import java.util.Date;

import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.xeslite.lite.factory.XFactoryLiteImpl;

import cognitiveprom.log.extension.XCognitiveEventClassifier;
import cognitiveprom.log.extension.XCognitiveExtension;

public class XCognitiveLogHelper {

	private static XFactory xesFactory = new XFactoryLiteImpl();
	
	public static XFactory XESFactory() {
		return xesFactory;
	}
	
	/**
	 * Prepares and returns a new log with proper extensions set
	 * 
	 * @return a new, empty, log
	 */
	public static XLog prepareLog() {
		XFactory xesFactory = XCognitiveLogHelper.XESFactory();
		XLog log = xesFactory.createLog();
		
		log.getAttributes().put("creator", new XAttributeLiteralImpl("creator", "CognitiveProM"));
		log.getExtensions().add(XTimeExtension.instance());
		log.getExtensions().add(XConceptExtension.instance());
		log.getExtensions().add(XCognitiveExtension.instance());
		
		log.getClassifiers().add(new XCognitiveEventClassifier());
		log.getClassifiers().add(new XEventNameClassifier());
		
		return log;
	}
	
	/**
	 * Returns the subject name that the given trace refers to. If the
	 * {@link XCognitiveExtension} is available, then it is used, otherwise the
	 * name comes from the {@link XConceptExtension}
	 * 
	 * @param trace
	 * @return
	 */
	public static String getSubjectName(XTrace trace) {
		String name = XCognitiveExtension.instance().extractSubjectName(trace);
		if (name == null) {
			name = XConceptExtension.instance().extractName(trace);
		}
		return name;
	}
	
	/**
	 * Returns the AOI name that the given trace refers to. If the
	 * {@link XCognitiveExtension} is available, then it is used, otherwise the
	 * name comes from the {@link XConceptExtension}
	 * 
	 * @param event
	 * @return
	 */
	public static String getAOIName(XEvent event) {
		String name = XCognitiveExtension.instance().extractAOI(event);
		if (name == null) {
			name = XConceptExtension.instance().extractName(event);
		}
		return name;
	}
	
	/**
	 * This method creates a new {@link XTrace} for the given subject (if
	 * needed), adds it to the given log object (if needed), and returns the
	 * trace itself.
	 * 
	 * @param log the log that is going to host the new trace
	 * @param subjectName the case identifier of the new trace
	 * @return the new trace created, or <tt>null</tt> if the given log is not valid
	 */
	public static XTrace insertTraceForSubject(XLog log, String subjectName) {
		if (log == null) {
			return null;
		}
		
		for (XTrace trace : log) {
			if (subjectName.equals(XConceptExtension.instance().extractName(trace))) {
				return trace;
			}
		}
		
		XTrace trace = xesFactory.createTrace();
		XConceptExtension.instance().assignName(trace, subjectName);
		XCognitiveExtension.instance().assignSubjectName(trace, subjectName);
		log.add(trace);
		
		return trace;
	}
	
	/**
	 * This method creates a new {@link XEvent} referring to the given activity
	 * name, occurred at the given time. The event is added to the given trace,
	 * and returned
	 * 
	 * @param trace the trace that is going to host the new event
	 * @param AOI the name of the activity the new event is going to
	 * refer to
	 * @param timestamp the time the new event occurred
	 * @return the new event created, or <tt>null</tt> if the given trace is
	 * not valid
	 */
	public static XEvent constructAOIFixation(String AOI, Date timestamp, Long duration) {
		XEvent e = xesFactory.createEvent();
		XConceptExtension.instance().assignName(e, AOI);
		XCognitiveExtension.instance().assignAOI(e, AOI);
		XTimeExtension.instance().assignTimestamp(e, timestamp);
		XCognitiveExtension.instance().assignDuration(e, duration);
		XCognitiveExtension.instance().assignIsStimulus(e, false);
		return e;
	}
	
	/**
	 * This method sorts all the events in the traces of the log with respect to
	 * their timestamps, in increasing order.
	 * 
	 * @param log
	 */
	public static void sortXLog(XLog log) {
		for (XTrace trace : log) {
			trace.sort(new Comparator<XEvent>() {
				@Override
				public int compare(XEvent e1, XEvent e2) {
					return XTimeExtension.instance().extractTimestamp(e1).compareTo(
							XTimeExtension.instance().extractTimestamp(e2));
				}
			});
		}
	}
}
