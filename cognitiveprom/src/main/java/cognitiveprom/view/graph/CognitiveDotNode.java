package cognitiveprom.view.graph;

import java.awt.Color;

import org.processmining.plugins.graphviz.dot.DotNode;

public class CognitiveDotNode extends DotNode {

	private String label;
	
	public CognitiveDotNode(String label) {
		this(label, null, null, null);
	}
	
	public CognitiveDotNode(String label, String secondLine, Double weight, ColorPalette.Colors activityColor) {
		super(label, null);

		this.label = label;
		
		setSelectable(true);
		
		setOption("shape", "box");
		setOption("style", "rounded,filled");
		setOption("fontsize", "20");
		setOption("fontname", "Arial");
		setOption("gradientangle", "270");
		setOption("penwidth", ".75");
		setOption("margin", ".5,.2");
		
		setSecondLine(secondLine);
		setColorWeight(weight, activityColor);
		
		setSelectable(true);
	}

	public void setSecondLine(String secondLine) {
		if (secondLine != null) {
			setLabel("<<font point-size='22'>" + label + "</font> <br/><font point-size='16'>" + secondLine + "</font>>");
		}
	}

	public void setColorWeight(Double weight, ColorPalette.Colors activityColor) {
		if (weight == null) {
			setOption("fillcolor", "#FDEFD8:#E1D3BC");
		} else {
			Color backgroundColor = ColorPalette.getValue(activityColor, weight);
			Color fontColor = ColorPalette.getFontColor(backgroundColor);
			setOption("fillcolor", ColorPalette.colorToString(backgroundColor)/* + ":" + ColorPalette.colorToString(backgroundColor.darker())*/);
			setOption("fontcolor", ColorPalette.colorToString(fontColor));
			setOption("fixedsize", "false");
		}
	}

	public void setMovedIn() {
		setOption("fillcolor", "white");
	}
	
	public void setMovedOut() {
		setOption("fillcolor", "black");
	}
	
	@Override
	public int hashCode() {
		return getLabel().hashCode();
	}
	
	@Override
	public boolean equals(Object object) {
		return getLabel().equals(object);
	}
}
