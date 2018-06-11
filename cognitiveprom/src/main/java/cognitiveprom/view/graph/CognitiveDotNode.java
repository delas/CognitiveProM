package cognitiveprom.view.graph;

import java.awt.Color;

import org.processmining.plugins.graphviz.dot.DotNode;

public class CognitiveDotNode extends DotNode {

	public CognitiveDotNode(String label, String secondLine, Double weight, ColorPalette.Colors activityColor) {
		super(label, null);
		
		setSelectable(true);
		
		setOption("shape", "box");
		setOption("style", "rounded,filled");
//		setOption("height", ".4");
//		setOption("width", "1");
		setOption("fontsize", "12");
		setOption("fontname", "Arial");
		setOption("gradientangle", "270");
		setOption("penwidth", ".75");
		setOption("margin", ".5,.2");
		
		if (secondLine != null) {
			setLabel("<" + label + " <br/><font point-size='10'>" + secondLine + "</font>>");
		}
		
		if (weight == null) {
			setOption("fillcolor", "#FDEFD8:#E1D3BC");
		} else {
			Color backgroundColor = ColorPalette.getValue(activityColor, weight);
			Color fontColor = ColorPalette.getFontColor(backgroundColor);
			setOption("fillcolor", ColorPalette.colorToString(backgroundColor) + ":" + ColorPalette.colorToString(backgroundColor.darker()));
			setOption("fontcolor", ColorPalette.colorToString(fontColor));
			setOption("fixedsize", "false");
		}
		
		setSelectable(true);
	}
	
	public void setMovedIn() {
		setOption("fillcolor", "white");
	}
	
	public void setMovedOut() {
		setOption("fillcolor", "black");
	}
}
