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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.controllers.ApplicationController;
import cognitiveprom.log.projections.AggregationFunctions;
import cognitiveprom.log.projections.ValueProjector;
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
	
	private JComboBox<ValueProjector> comboAttributes;
	private JComboBox<AggregationFunctions> comboAttributesFunctions;
	private JLabel labelTraces;
	private JList<String> tracesSelector;
	private DefaultListModel<String> listModelSelectedTraces;
//	private JLabel labelActivities;
//	private JList<XEventClass> activitiesSelector;
//	private DefaultListModel<XEventClass> listModelSelectedActivities;
	private JComboBox<ColorPalette.Colors> comboColors;
	private JCheckBox checkBoxPreserveAllNodesConnected;

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
	
	public ValueProjector getSelectedAggregationValue() {
		return (ValueProjector) comboAttributes.getSelectedItem();
	}
	
	public AggregationFunctions getSelectedAggregationFunction() {
		return (AggregationFunctions) comboAttributesFunctions.getSelectedItem();
	}
	
	public Collection<String> getSelectedTraces() {
		return tracesSelector.getSelectedValuesList();
	}
	
	public ColorPalette.Colors getSelectedNodeColor() {
		return (ColorPalette.Colors) comboColors.getSelectedItem();
	}

	public boolean getPreserveAllNodesConnected() {
		return checkBoxPreserveAllNodesConnected.isSelected();
	}
	
	public void reset() {
		comboAttributes.removeAllItems();
		listModelSelectedTraces.removeAllElements();
//		listModelSelectedActivities.removeAllElements();
	}

	public void populateComponents() {
		reset();
		
		for (ValueProjector av : ApplicationController.instance().logController().log().getProjectableAttributes()) {
			comboAttributes.addItem(av);
		}
		comboAttributes.addItem(ValueProjector.FREQUENCY);
		comboAttributes.addItem(ValueProjector.NONE);
		comboAttributes.setSelectedItem(ValueProjector.FREQUENCY);
		
		tracesSelector.setModel(new DefaultListModel<String>());
		for (String subjectName : ApplicationController.instance().logController().log().getSubjectNames()) {
			listModelSelectedTraces.addElement(subjectName);
		}
		tracesSelector.setModel(listModelSelectedTraces);
		tracesSelector.setSelectionInterval(0, listModelSelectedTraces.getSize() - 1);
	}
	
	private void placeComponents() {
		// construct all elements
		// the combo with aggregation attributes
		comboAttributes = new JComboBox<ValueProjector>();
		comboAttributes.addItem(ValueProjector.FREQUENCY);
		comboAttributes.setSelectedItem(ValueProjector.FREQUENCY);
		
		// the combo with aggregation functions
		comboAttributesFunctions = new JComboBox<AggregationFunctions>();
		for (AggregationFunctions f : AggregationFunctions.values()) {
			comboAttributesFunctions.addItem(f);
		}
		comboAttributesFunctions.setSelectedItem(AggregationFunctions.SUM);
		
		// the list of active traces
		listModelSelectedTraces = new DefaultListModel<String>();
		tracesSelector = new JList<String>(listModelSelectedTraces);
		tracesSelector.setCellRenderer(new ListCellRenderer<String>() {
			public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
				String traceName = value;
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
		
		// the list of activities
//		listModelSelectedActivities = new DefaultListModel<XEventClass>();
//		activitiesSelector = new JList<XEventClass>(listModelSelectedActivities);
//		activitiesSelector.setCellRenderer(new ListCellRenderer<XEventClass>() {
//			public Component getListCellRendererComponent(JList<? extends XEventClass> list, XEventClass value, int index, boolean isSelected, boolean cellHasFocus) {
//				String activityName = value.toString();
//				JLabel l = new JLabel(activityName);
//				l.setOpaque(true);
//				if (isSelected) {
//					l.setBackground(tracesSelector.getSelectionBackground());
//					l.setForeground(tracesSelector.getSelectionForeground());
//				} else {
//					l.setBackground(tracesSelector.getBackground());
//					l.setForeground(tracesSelector.getForeground());
//				}
//				return l;
//			}
//		});
		
		// the list of colors
		comboColors = new JComboBox<ColorPalette.Colors>();
		for (ColorPalette.Colors c : ColorPalette.Colors.values()) {
			comboColors.addItem(c);
		}
		comboColors.setSelectedItem(ColorPalette.Colors.BLUE);
		
		// add all elements to the panel
		setLayout(new GridBagLayout());
		this.labelTraces = new JLabel("", ImageIcons.ICON_TRACES, JLabel.LEFT);
//		this.labelActivities = new JLabel("", ImageIcons.ICON_ACTIVITIES, JLabel.LEFT);
		
		int row = 0;
		add(new JLabel("Attribute to project", ImageIcons.ICON_ATTRIBUTE, JLabel.LEFT), GridBagLayoutHelper.createHorizontalTitleConstraint(0, row++));
		add(comboAttributes, GridBagLayoutHelper.createHorizontalComponentConstraint(0, row++));
		
		add(new JLabel("Aggregation function", ImageIcons.ICON_AGGREGATION, JLabel.LEFT), GridBagLayoutHelper.createHorizontalTitleConstraint(0, row++));
		add(comboAttributesFunctions, GridBagLayoutHelper.createHorizontalComponentConstraint(0, row++));
		
		add(labelTraces, GridBagLayoutHelper.createHorizontalTitleConstraint(0, row++));
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 0;
		c.gridy = row++;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(new JScrollPane(tracesSelector), c);
		
//		add(labelActivities, GridBagLayoutHelper.createHorizontalTitleConstraint(0, row++));
//		c = new GridBagConstraints();
//		c.insets = new Insets(0, 5, 5, 5);
//		c.gridx = 0;
//		c.gridy = row++;
//		c.weightx = 1;
//		c.weighty = 1;
//		c.fill = GridBagConstraints.BOTH;
//		add(new JScrollPane(activitiesSelector), c);

		add(new JLabel("Activities color", ImageIcons.ICON_COLORS, JLabel.LEFT), GridBagLayoutHelper.createHorizontalTitleConstraint(0, row++));
		add(comboColors, GridBagLayoutHelper.createHorizontalComponentConstraint(0, row++));
		
		checkBoxPreserveAllNodesConnected = new JCheckBox("<html>Soft thresholding (preserves all nodes and their connectiveness)</html>");
		checkBoxPreserveAllNodesConnected.setOpaque(false);
		add(new JLabel("Node connections", ImageIcons.ICON_CONNECTIONS, JLabel.LEFT), GridBagLayoutHelper.createHorizontalTitleConstraint(0, row++));
		add(checkBoxPreserveAllNodesConnected, GridBagLayoutHelper.createHorizontalComponentConstraint(0, row++));
	}
	
	private void registerListeners() {
		comboAttributes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ApplicationController.instance().processController().isShowingModel()) {
					ApplicationController.instance().processController().updateVisualization();
				}
			}
		});
		comboAttributesFunctions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ApplicationController.instance().processController().isShowingModel()) {
					ApplicationController.instance().processController().updateVisualization();
				}
			}
		});
		tracesSelector.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (ApplicationController.instance().processController().isShowingModel()) {
					ApplicationController.instance().processController().updateVisualization();
				}
				labelTraces.setText("Traces to project (" + tracesSelector.getSelectedIndices().length + "/" + listModelSelectedTraces.size() + ")");
			}
		});
//		activitiesSelector.addListSelectionListener(new ListSelectionListener() {
//			@Override
//			public void valueChanged(ListSelectionEvent e) {
//				if (ApplicationController.instance().processController().isShowingModel()) {
//					ApplicationController.instance().processController().updateVisualization();
//				}
//				labelTraces.setText("Activities to map (" + activitiesSelector.getSelectedIndices().length + "/" + listModelSelectedActivities.size() + ")");
//			}
//		});
		comboColors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ApplicationController.instance().processController().isShowingModel()) {
					ApplicationController.instance().processController().updateVisualization();
				}
			}
		});
		checkBoxPreserveAllNodesConnected.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ApplicationController.instance().processController().isShowingModel()) {
					ApplicationController.instance().processController().updateVisualization();
				}
			}
		});
	}
}
