package cognitiveprom.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotEdge;
import org.processmining.plugins.graphviz.dot.DotElement;
import org.processmining.plugins.graphviz.dot.DotNode;
import org.processmining.plugins.graphviz.visualisation.DotPanel;
import org.processmining.plugins.graphviz.visualisation.listeners.SelectionChangedListener;

import com.kitfox.svg.Group;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGElement;

import cognitiveprom.config.ConfigurationSet;

/**
 * 
 * @author Andrea Burattin
 */
public class ProcessVisualizer extends ConfigurablePanel {

	private static final long serialVersionUID = -1749483831141989186L;
	
	private DotPanel graphVisualizer;
	private AdvancedConfiguration advancedConfiguration;
	private TracesVisualizer tracesVisualizer;
	private AbstractionSlider abstractionSlider;

	public ProcessVisualizer(ConfigurationSet conf) {
		super(conf);
		
		// place the components of the window
		placeComponents();
		
		registerListener();
	}
	
	public DotPanel getGraphVisualizer() {
		return graphVisualizer;
	}
	
	public AdvancedConfiguration getAdvancedConfigurationPanel() {
		return advancedConfiguration;
	}
	
	public TracesVisualizer getShowTracesPanel() {
		return tracesVisualizer;
	}
	
	public double getAbstractionValue() {
		return abstractionSlider.getAbstractionValue();
	}

	private void placeComponents() {
		// set the slider
		abstractionSlider = new AbstractionSlider(conf.getChild(AbstractionSlider.class.getCanonicalName()));
		
		// sets the panels on the left
		advancedConfiguration = new AdvancedConfiguration(conf.getChild(AdvancedConfiguration.class.getCanonicalName()));
		tracesVisualizer = new TracesVisualizer(conf.getChild(TracesVisualizer.class.getCanonicalName()));
		JPanel leftContainer = new JPanel();
		leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.X_AXIS));
		leftContainer.add(advancedConfiguration);
		leftContainer.add(tracesVisualizer);
		
		// set the graph
		graphVisualizer = new DotPanel(new Dot());
		graphVisualizer.setOpaque(true);
		graphVisualizer.setBackground(Color.white);
		
		// displace all elements
		setLayout(new BorderLayout());
		add(leftContainer, BorderLayout.WEST);
		add(graphVisualizer, BorderLayout.CENTER);
		add(abstractionSlider, BorderLayout.EAST);
	}
	
	public void registerListener() {
		graphVisualizer.addSelectionChangedListener(new SelectionChangedListener<DotElement>() {
			public void selectionChanged(Set<DotElement> selectedElements) {
				if (selectedElements.isEmpty()) {
					for (DotEdge edge : graphVisualizer.getEdges()) {
						styleNode(edge, graphVisualizer.getSVG(), false);
					}
					for (DotNode node : graphVisualizer.getNodes()) {
						styleNode(node, graphVisualizer.getSVG(), false);
					}
				} else {
					for (DotEdge edge : graphVisualizer.getEdges()) {
						styleNode(edge, graphVisualizer.getSVG(), true);
					}
					for (DotNode node : graphVisualizer.getNodes()) {
						styleNode(node, graphVisualizer.getSVG(), true);
					}
					for (DotNode node : graphVisualizer.getNodes()) {
						if (selectedElements.contains(node)) {
							styleNode(node, graphVisualizer.getSVG(), false);
							for (DotEdge edge : graphVisualizer.getEdges()) {
								if (edge.getSource().getId().equals(node.getId()) && !"invisible".equals(edge.getOption("style"))) {
									styleNode(edge, graphVisualizer.getSVG(), false);
									styleNode(edge.getTarget(), graphVisualizer.getSVG(), false);
								}
								if (edge.getTarget().getId().equals(node.getId()) && !"invisible".equals(edge.getOption("style"))) {
									styleNode(edge, graphVisualizer.getSVG(), false);
									styleNode(edge.getSource(), graphVisualizer.getSVG(), false);
								}
							}
						}
					}
				}
				graphVisualizer.repaint();
			}
			private void styleNode(DotElement element, SVGDiagram svg, boolean hide) {
				//prepare parts of the rendered dot element
				Group group = DotPanel.getSVGElementOf(svg, element);
				//transparency
				if (hide) {
					DotPanel.setCSSAttributeOf(group, "opacity", "0.25");
				} else {
					DotPanel.setCSSAttributeOf(group, "opacity", "1.0");
				}
			}
		});
	}
}
