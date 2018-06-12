package cognitiveprom.view.graph;

import org.processmining.plugins.graphviz.dot.DotEdge;
import org.processmining.plugins.graphviz.dot.DotNode;

import cognitiveprom.view.graph.ColorPalette.Colors;

public class CognitiveDotEdge extends DotEdge {

	public CognitiveDotEdge(DotNode source, DotNode target, String edgeText, Double weight) {
		super(source, target);
		
		setSelectable(true);
		
		setOption("color", "#9F9F9F");
		setOption("size", ".1");
		setOption("arrowsize", ".5");
		setOption("fontsize", "15");
		setOption("fontname", "Arial");
		
		if (edgeText != null) {
			setLabel(" " + edgeText);
		}
		
		if (weight != null) {
			setOption("color", ColorPalette.colorToString(ColorPalette.getValue(Colors.DARK_GRAY, weight)));
			if ((source instanceof CognitiveDotStartNode) || (target instanceof CognitiveDotEndNode)) {
				setOption("penwidth", "" + (1 + (10 * weight)));
			} else {
				setOption("penwidth", "" + (1 + (8 * weight)));
			}
		} else {
			if ((source instanceof CognitiveDotStartNode) || (target instanceof CognitiveDotEndNode)) {
				setOption("penwidth", "2");
			} else {
				setOption("penwidth", "3");
			}
		}
		
		if (source instanceof CognitiveDotStartNode) {
			setOption("style", "dashed");
			setOption("color", "#ACB89C");
		}
		
		if (target instanceof CognitiveDotEndNode) {
			setOption("style", "dashed");
			setOption("color", "#C2B0AB");
		}
	}
}
