package cognitiveprom.view.graph;

import org.processmining.plugins.graphviz.dot.DotEdge;
import org.processmining.plugins.graphviz.dot.DotNode;

import cognitiveprom.tools.ColorPalette;
import cognitiveprom.tools.ColorPalette.Colors;

public class CognitiveDotEdge extends DotEdge {

	public CognitiveDotEdge(DotNode source, DotNode target, String edgeText, Double weight) {
		super(source, target);
		
		setSelectable(true);
		
		setOption("color", "#9F9F9F");
		setOption("size", ".1");
		setOption("arrowsize", ".5");
		setOption("fontsize", "7");
		
		if (edgeText != null) {
			setLabel("  " + edgeText);
		}
		
		if (weight != null) {
			setOption("color", ColorPalette.colorToString(ColorPalette.getValue(Colors.DARK_GRAY, weight)));
			setOption("penwidth", "" + (1 + (4 * weight)));
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
