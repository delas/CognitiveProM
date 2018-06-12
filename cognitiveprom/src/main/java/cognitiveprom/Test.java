package cognitiveprom;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.swing.JFrame;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryNaiveImpl;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.out.XesXmlSerializer;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotElement;
import org.processmining.plugins.graphviz.dot.DotNode;
import org.processmining.plugins.graphviz.visualisation.DotPanel;
import org.processmining.plugins.graphviz.visualisation.listeners.DotElementSelectionListener;

import com.kitfox.svg.SVGDiagram;

import cognitiveprom.exceptions.LogIOException;
import cognitiveprom.log.CognitiveLog;
import cognitiveprom.log.extension.XCognitiveEventClassifier;
import cognitiveprom.log.extension.XCognitiveExtension;
import cognitiveprom.log.io.TSVCognitiveImporter;
import cognitiveprom.log.utils.XCognitiveLogHelper;

class CustomDotNode extends DotNode {
	public CustomDotNode(String label) {
		super(label, null);
		
		setOption("shape", "box");
		setOption("style", "rounded,filled");
		
		addSelectionListener(new DotElementSelectionListener() {
			public void selected(DotElement element, SVGDiagram image) {
				System.out.println("Selected " + getLabel());
			}
			public void deselected(DotElement element, SVGDiagram image) {
				System.out.println("Deselected " + getLabel());
			}
		});
	}
}

public class Test {

	public static void main(String[] args) {
		
		Dot dot = new Dot();
		
		dot.addNode(new CustomDotNode("n1"));
		dot.addNode(new CustomDotNode("n2"));
		
		DotPanel p = new DotPanel(dot);
		p.setPreferredSize(new Dimension(800, 600));
		
		JFrame f = new JFrame("test");
		f.add(p);
		f.pack();
		
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}


//public class Test {
//
//	public static void main(String[] args) throws LogIOException, FileNotFoundException, IOException {
//		
//		TSVCognitiveImporter i = new TSVCognitiveImporter();
//		i.addAOIs("legend", "graph", "title");
//		
//		CognitiveLog l = i.load("C:\\Users\\andbur\\Desktop\\q1.1fixations.tsv");
//		XLog log = l.getLog();
//		new XesXmlSerializer().serialize(log, new FileOutputStream("C:\\Users\\andbur\\Desktop\\tmp.xes"));
//		
//		
//		
//		XCognitiveExtension ce = XCognitiveExtension.instance();
//		XFactory factory = new XFactoryNaiveImpl();
//		XLog log = XCognitiveLogHelper.prepareLog();
//		
//		XEvent e11 = factory.createEvent();
//		XTimeExtension.instance().assignTimestamp(e11, 1234);
//		ce.assignAOI(e11, "a1");
//		ce.addMetric(e11, "m1", new Date(), 12d);
//		ce.addMetric(e11, "m1", new Date(), 24d);
//		ce.addMetric(e11, "m3", new Date(), 36d);
//		ce.assignDuration(e11, 123);
//		ce.assignIsStimulus(e11, true);
//		
//		System.out.println(ce.extractMetricNames(e11));
//		System.out.println(ce.extractMetricValues(e11, "m1"));
//		
//		
//		XEvent s11 = factory.createEvent();
//		XCognitiveExtension.instance().assignIsStimulus(s11, true);
//		XConceptExtension.instance().assignName(s11, "some kind of stimulus");
//		XTimeExtension.instance().assignTimestamp(s11, 2);
//		XCognitiveExtension.instance().assignDuration(s11, 123);
//		
////		XEvent e12 = factory.createEvent();
////		XCognitiveExtension.instance().assignAOI(e12, "a2");
////		XTimeExtension.instance().assignTimestamp(e12, 3);
////		
////		XEvent e21 = factory.createEvent();
////		XCognitiveExtension.instance().assignAOI(e21, "a1");
////		XTimeExtension.instance().assignTimestamp(e21, 5);
////		XCognitiveExtension.instance().assignDuration(e21, 456);
////		
////		XEvent e22 = factory.createEvent();
////		XCognitiveExtension.instance().assignAOI(e22, "a2");
////		XTimeExtension.instance().assignTimestamp(e22, 6);
////		
//		XTrace t1 = factory.createTrace();
//		XCognitiveExtension.instance().assignSubjectName(t1, "Andrea");
//		t1.insertOrdered(e11);
////		t1.insertOrdered(s11);
////		t1.insertOrdered(e12);
//		log.add(t1);
////		
////		XTrace t2 = factory.createTrace();
////		log.add(t2);
////		XCognitiveExtension.instance().assignSubjectName(t2, "Enrico");
////		t2.insertOrdered(e21);
////		t2.insertOrdered(e22);
//	}
//
//}
