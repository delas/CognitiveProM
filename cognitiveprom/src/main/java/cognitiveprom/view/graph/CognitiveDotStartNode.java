package cognitiveprom.view.graph;

import org.processmining.plugins.graphviz.dot.DotNode;

public class CognitiveDotStartNode extends DotNode {
	public CognitiveDotStartNode() {
		super("", null);
		
		setSelectable(true);
		
		setOption("shape", "circle");
		setOption("style", "filled");
		setOption("fillcolor", "#CED6BD:#B3BBA2");
		setOption("gradientangle", "270");
		setOption("color", "#595F45");
		setOption("width", ".3");
	}
}
