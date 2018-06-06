package cognitiveprom.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.visualisation.DotPanel;

import cognitiveprom.config.ConfigurationSet;

/**
 * 
 * @author Andrea Burattin
 */
public class ProcessVisualizer extends ConfigurablePanel {

	private static final long serialVersionUID = -1749483831141989186L;
	
	private DotPanel graphVisualizer;
	private AdvancedConfiguration advancedConfiguration;
	private AbstractionSlider abstractionSlider;

	public ProcessVisualizer(ConfigurationSet conf) {
		super(conf);
		
		// place the components of the window
		placeComponents();
	}
	
	public DotPanel getGraphVisualizer() {
		return graphVisualizer;
	}
	
	public AdvancedConfiguration getAdvancedConfigurationPanel() {
		return advancedConfiguration;
	}
	
	public double getAbstractionValue() {
		return abstractionSlider.getAbstractionValue();
	}

	private void placeComponents() {
		// set the slider
		abstractionSlider = new AbstractionSlider(conf.getChild(AbstractionSlider.class.getCanonicalName()));
		
		// sets the advanced configuration
		advancedConfiguration = new AdvancedConfiguration(conf.getChild(AdvancedConfiguration.class.getCanonicalName()));
		
		// set the graph
		graphVisualizer = new DotPanel(new Dot());
		graphVisualizer.setOpaque(true);
		graphVisualizer.setBackground(Color.white);
		
		// displace all elements
		setLayout(new BorderLayout());
		add(advancedConfiguration, BorderLayout.WEST);
		add(graphVisualizer, BorderLayout.CENTER);
		add(abstractionSlider, BorderLayout.EAST);
	}
}
