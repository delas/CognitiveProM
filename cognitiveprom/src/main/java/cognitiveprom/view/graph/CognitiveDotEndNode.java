package cognitiveprom.view.graph;

import org.processmining.plugins.graphviz.dot.DotNode;

public class CognitiveDotEndNode extends DotNode {
	public CognitiveDotEndNode() {
		super("", null);
		
		setSelectable(true);
		
		setOption("shape", "circle");
		setOption("style", "filled");
		setOption("fillcolor", "#D8BBB9:#BC9F9D");
		setOption("gradientangle", "270");
		setOption("color", "#614847");
		setOption("width", ".3");
	}
}
