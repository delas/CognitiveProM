package cognitiveprom.view.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.deckfour.xes.model.XTrace;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.controllers.ApplicationController;
import cognitiveprom.log.projections.AggregationFunctions;
import cognitiveprom.log.projections.AggregationValues;
import cognitiveprom.log.utils.XCognitiveLogHelper;
import cognitiveprom.utils.GridBagLayoutHelper;
import cognitiveprom.view.collections.ImageIcons;
import cognitiveprom.view.graph.ColorPalette;

/**
 * 
 * @author Andrea Burattin
 */
public class AdvancedConfiguration extends ConfigurablePanel {

	private static final long serialVersionUID = 1564244018050763210L;
	protected static final int WIDTH = 200;
	
	private JComboBox<AggregationValues> comboAttributes;
	private JComboBox<AggregationFunctions> comboAttributesFunctions;
	private JList<XTrace> tracesSelector;
	private DefaultListModel<XTrace> listModelSelectedTraces;
	private JComboBox<ColorPalette.Colors> comboColors;

	/**
	 * Basic class constructor
	 * 
	 * @param conf
	 */
	public AdvancedConfiguration(ConfigurationSet conf) {
		super(conf);
		
		setPreferredSize(new Dimension(WIDTH, 0));
		setMinimumSize(new Dimension(WIDTH, 0));
		
		setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, getBackground()));
		setBackground(Color.white);
		
		placeComponents();
		registerListeners();
	}
	
	public AggregationValues getSelectedAggregationValue() {
		return (AggregationValues) comboAttributes.getSelectedItem();
	}
	
	public AggregationFunctions getSelectedAggregationFunction() {
		return (AggregationFunctions) comboAttributesFunctions.getSelectedItem();
	}
	
	public Collection<XTrace> getSelectedTraces() {
		return tracesSelector.getSelectedValuesList();
	}
	
	public ColorPalette.Colors getSelectedNodeColor() {
		return (ColorPalette.Colors) comboColors.getSelectedItem();
	}

	public void populateComponents() {
		comboAttributes.removeAllItems();
		for (AggregationValues av : ApplicationController.instance().logController().log().getProjectableAttributes()) {
			comboAttributes.addItem(av);
		}
		comboAttributes.addItem(AggregationValues.FREQUENCY);
		comboAttributes.setSelectedItem(AggregationValues.FREQUENCY);
		
		listModelSelectedTraces.removeAllElements();
		for (XTrace trace : ApplicationController.instance().logController().log().getLog()) {
			listModelSelectedTraces.addElement(trace);
		}
		tracesSelector.setSelectionInterval(0, listModelSelectedTraces.getSize() - 1);
	}
	
	private void placeComponents() {
		// construct all elements
		// the combo with aggregation attributes
		comboAttributes = new JComboBox<AggregationValues>();
		comboAttributes.addItem(AggregationValues.FREQUENCY);
		comboAttributes.setSelectedItem(AggregationValues.FREQUENCY);
		
		// the combo with aggregation functions
		comboAttributesFunctions = new JComboBox<AggregationFunctions>();
		for (AggregationFunctions f : AggregationFunctions.values()) {
			comboAttributesFunctions.addItem(f);
		}
		comboAttributesFunctions.setSelectedItem(AggregationFunctions.SUM);
		
		// the list of active traces
		listModelSelectedTraces = new DefaultListModel<XTrace>();
		tracesSelector = new JList<XTrace>(listModelSelectedTraces);
		tracesSelector.setCellRenderer(new ListCellRenderer<XTrace>() {
			public Component getListCellRendererComponent(JList<? extends XTrace> list, XTrace value, int index, boolean isSelected, boolean cellHasFocus) {
				String traceName = XCognitiveLogHelper.getSubjectName(value);
				JLabel l = new JLabel(traceName);
				l.setOpaque(true);
				if (isSelected) {
					l.setBackground(tracesSelector.getSelectionBackground());
					l.setForeground(tracesSelector.getSelectionForeground());
				} else {
					l.setBackground(tracesSelector.getBackground());
					l.setForeground(tracesSelector.getForeground());
				}
				return l;
			}
		});
		
		// the list of colors
		comboColors = new JComboBox<ColorPalette.Colors>();
		for (ColorPalette.Colors c : ColorPalette.Colors.values()) {
			comboColors.addItem(c);
		}
		comboColors.setSelectedItem(ColorPalette.Colors.BLUE);
		
		// add all elements to the panel
		setLayout(new GridBagLayout());
		
		int row = 0;
		add(new JLabel("Attribute to project", ImageIcons.ICON_ATTRIBUTE, JLabel.LEFT), GridBagLayoutHelper.createHorizontalTitleConstraint(0, row++));
		add(comboAttributes, GridBagLayoutHelper.createHorizontalComponentConstraint(0, row++));
		
		add(new JLabel("Aggregation function", ImageIcons.ICON_AGGREGATION, JLabel.LEFT), GridBagLayoutHelper.createHorizontalTitleConstraint(0, row++));
		add(comboAttributesFunctions, GridBagLayoutHelper.createHorizontalComponentConstraint(0, row++));
		
		add(new JLabel("Traces to project", ImageIcons.ICON_TRACES, JLabel.LEFT), GridBagLayoutHelper.createHorizontalTitleConstraint(0, row++));
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 0;
		c.gridy = row++;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(new JScrollPane(tracesSelector), c);

		add(new JLabel("Activities color", ImageIcons.ICON_COLORS, JLabel.LEFT), GridBagLayoutHelper.createHorizontalTitleConstraint(0, row++));
		add(comboColors, GridBagLayoutHelper.createHorizontalComponentConstraint(0, row++));
	}
	
	private void registerListeners() {
		comboAttributes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ApplicationController.instance().processController().updateVisualization();
			}
		});
		comboAttributesFunctions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ApplicationController.instance().processController().updateVisualization();
			}
		});
		tracesSelector.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ApplicationController.instance().processController().updateVisualization();
			}
		});
		comboColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ApplicationController.instance().processController().updateVisualization();
			}
		});
	}
}
