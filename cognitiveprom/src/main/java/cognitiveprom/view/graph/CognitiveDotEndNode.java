package cognitiveprom.view.graph;

import org.processmining.plugins.graphviz.dot.DotNode;

public class CognitiveDotEndNode extends DotNode {
	public CognitiveDotEndNode() {
		super("", null);
		
		setSelectable(true);
		
		setOption("shape", "circle");
		setOption("style", "filled");
		setOption("fillcolor", "#D8BBB9"); // #D8BBB9:#BC9F9D
		setOption("gradientangle", "270");
		setOption("color", "#614847");
		setOption("height", "0.13");
		setOption("width", "0.13");
	}
	
	@Override
	public String toString() {
		return "{ rank = \"sink\"; " + super.toString() + "}";
	}
}
