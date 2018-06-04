package cognitiveprom.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JSlider;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.visualisation.DotPanel;

import cognitiveprom.config.ConfigurationSet;

public class ProcessVisualizer extends ConfigurablePanel {

	private static final long serialVersionUID = -1749483831141989186L;
	
	private DotPanel graphVisualizer;
	private JSlider abstractionSlider;

	public ProcessVisualizer(ConfigurationSet conf) {
		super(conf);
		
		// place the components of the window
		placeComponents();
	}

	private void placeComponents() {
		
		// set the slider
		abstractionSlider = new JSlider(JSlider.VERTICAL, 0, 100, 100);
		abstractionSlider.setBackground(Color.white);
		
		// set the graph
		graphVisualizer = new DotPanel(new Dot());
		graphVisualizer.setOpaque(true);
		graphVisualizer.setBackground(Color.white);
		
		// displace all elements
		setLayout(new BorderLayout());
//		add(attributePanel, BorderLayout.WEST);
		add(graphVisualizer, BorderLayout.CENTER);
		add(abstractionSlider, BorderLayout.EAST);
	}
}
