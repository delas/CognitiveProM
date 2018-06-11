package cognitiveprom.view.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.controllers.ApplicationController;
import cognitiveprom.log.utils.XCognitiveLogHelper;
import cognitiveprom.utils.GridBagLayoutHelper;
import cognitiveprom.view.collections.ImageIcons;

/**
 * 
 * @author Andrea Burattin
 */
public class TracesVisualizer extends ConfigurablePanel {

	private static final long serialVersionUID = 1564244018050763210L;
	protected static final int WIDTH = 400;
	
	private JLabel labelTraces;
	private JList<XTrace> tracesSelector;
	private JList<XEvent> eventsList;
	private DefaultListModel<XTrace> listModelSelectedTraces;
	private DefaultListModel<XEvent> listModelEvents;

	/**
	 * Basic class constructor
	 * 
	 * @param conf
	 */
	public TracesVisualizer(ConfigurationSet conf) {
		super(conf);
		
		setPreferredSize(new Dimension(WIDTH, 0));
		setMinimumSize(new Dimension(WIDTH, 0));
		
		setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, getBackground()));
		setBackground(Color.white);
		
		placeComponents();
		registerListeners();
	}
	
	public void populateComponents() {
		listModelSelectedTraces.removeAllElements();
		for (XTrace trace : ApplicationController.instance().logController().log().getLog()) {
			listModelSelectedTraces.addElement(trace);
		}
		labelTraces.setText("Traces (" + listModelSelectedTraces.size() + "):");
	}
	
	private void placeComponents() {
		// construct all elements
		// the list of active traces
		listModelSelectedTraces = new DefaultListModel<XTrace>();
		tracesSelector = new JList<XTrace>(listModelSelectedTraces);
		tracesSelector.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tracesSelector.setCellRenderer(new ListCellRenderer<XTrace>() {
			public Component getListCellRendererComponent(JList<? extends XTrace> list, XTrace value, int index, boolean isSelected, boolean cellHasFocus) {
				String traceName = XCognitiveLogHelper.getSubjectName(value);
				String color = "gray";
				JLabel l = new JLabel();
				l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
				l.setOpaque(true);
				if (isSelected) {
					l.setBackground(tracesSelector.getSelectionBackground());
					l.setForeground(tracesSelector.getSelectionForeground());
					color = "white";
				} else {
					l.setBackground(tracesSelector.getBackground());
					l.setForeground(tracesSelector.getForeground());
				}
				l.setText("<html>" + traceName + "<br/><font color=" + color + ">" + value.size() + " events</font></html>");
				return l;
			}
		});
		
		// the list of events
		listModelEvents = new DefaultListModel<XEvent>();
		eventsList = new JList<XEvent>(listModelEvents);
		eventsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		eventsList.setCellRenderer(new ListCellRenderer<XEvent>() {

			@Override
			public Component getListCellRendererComponent(JList<? extends XEvent> list, XEvent value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel l = new JLabel(XCognitiveLogHelper.getAOIName(value));
				l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
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
		
		// add all elements to the panel
		setLayout(new GridBagLayout());
		labelTraces = new JLabel("", ImageIcons.ICON_TRACES, JLabel.LEFT);
		add(labelTraces, GridBagLayoutHelper.createHorizontalTitleConstraint(0, 0));
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.5;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(new JScrollPane(tracesSelector), c);
		
		add(new JLabel("Events for selected trace:"), GridBagLayoutHelper.createHorizontalTitleConstraint(1, 0));
		c = new GridBagConstraints();
		c.insets = new Insets(0, 5, 5, 5);
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.5;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		add(new JScrollPane(eventsList), c);
	}
	
	private void registerListeners() {
		tracesSelector.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				listModelEvents.clear();
				for(XEvent event : tracesSelector.getSelectedValue()) {
					listModelEvents.addElement(event);
				}
			}
		});
	}
}
