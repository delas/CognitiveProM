package cognitiveprom;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotElement;
import org.processmining.plugins.graphviz.visualisation.DotPanel;
import org.processmining.plugins.graphviz.visualisation.listeners.DotElementSelectionListener;

import com.kitfox.svg.SVGDiagram;

import cognitiveprom.graphical.CognitiveDotEdge;
import cognitiveprom.graphical.CognitiveDotEndNode;
import cognitiveprom.graphical.CognitiveDotNode;
import cognitiveprom.graphical.CognitiveDotStartNode;
import cognitiveprom.tools.ColorPalette;

public class Test2 {
	public static void main(String a[]) {
		
		Dot dot = new Dot();
		dot.setOption("outputorder", "edgesfirst");
		
		CognitiveDotStartNode start = new CognitiveDotStartNode(); dot.addNode(start);
		CognitiveDotEndNode end = new CognitiveDotEndNode(); dot.addNode(end);
		CognitiveDotNode n61 = new CognitiveDotNode("6.1 Decision trees", "100", 1d, ColorPalette.Colors.BLUE); dot.addNode(n61);
		CognitiveDotNode n66 = new CognitiveDotNode("6.6 Clustering", "80", 0.8, ColorPalette.Colors.BLUE); dot.addNode(n66);
		CognitiveDotNode n65 = new CognitiveDotNode("6.5 Numeric prediction", "50", 0.5, ColorPalette.Colors.BLUE); dot.addNode(n65);
		CognitiveDotNode n92 = new CognitiveDotNode("9.2 How do you use it?", "70", 0.7, ColorPalette.Colors.BLUE); dot.addNode(n92);
		CognitiveDotNode n101 = new CognitiveDotNode("10.1 Getting started", "60", 0.6, ColorPalette.Colors.BLUE); dot.addNode(n101);
		
		dot.addEdge(new CognitiveDotEdge(start, n61, "45", null));
		dot.addEdge(new CognitiveDotEdge(start, n66, "5", null));
		
		dot.addEdge(new CognitiveDotEdge(n61, n66, "", 0.7));
		dot.addEdge(new CognitiveDotEdge(n66, n61, "", 0.7));
		
		dot.addEdge(new CognitiveDotEdge(n61, n92, "", 0.6));
		dot.addEdge(new CognitiveDotEdge(n92, n61, "", 0.3));
		
		dot.addEdge(new CognitiveDotEdge(n92, n101, "", 0.2));
		dot.addEdge(new CognitiveDotEdge(n101, n92, "", 0.2));
		
		dot.addEdge(new CognitiveDotEdge(n61, n65, "", 0.3));
		dot.addEdge(new CognitiveDotEdge(n65, n61, "", 0.3));
		
		dot.addEdge(new CognitiveDotEdge(n66, end, "5", null));
		dot.addEdge(new CognitiveDotEdge(n61, end, "30", null));
		dot.addEdge(new CognitiveDotEdge(n92, end, "15", null));
		
		dot.addSelectionListener(new DotElementSelectionListener() {
			
			@Override
			public void selected(DotElement element, SVGDiagram image) {
				System.out.println(element);
			}
			
			@Override
			public void deselected(DotElement element, SVGDiagram image) {
				System.out.println(element);
			}
		});
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new DotPanel(dot));
		f.setPreferredSize(new Dimension(1600, 900));
		f.pack();
		f.setVisible(true);
	}
}
