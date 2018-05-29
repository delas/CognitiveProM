package cognitiveprom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.in.XParser;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.graphviz.visualisation.DotPanel;

import cognitiveprom.projections.AggregationValues;
import cognitiveprom.tools.DfgMinerResult;
import cognitiveprom.tools.Miner;
import cognitiveprom.tools.Utils;
import cognitiveprom.tools.Visualizer;

public class CognitiveProM {

	public static void main(String[] args) throws Exception {
		String file = "C:\\Users\\andbur\\Desktop\\1.1.tsv-quadrants.xes";
//		String file = "C:\\Users\\andbur\\Desktop\\test.xes";
//		String file = "C:\\Users\\andbur\\Desktop\\test-2starts.xes";
		XParser parser = new XesXmlParser();
		final XLog log = parser.parse(new File(file)).get(0);
		
		final DfgMinerResult dfg = Miner.mineDfg(log);
		
		JFrame mainFrame = new JFrame("CognitiveProM");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(1600, 900);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		
		// diagram panel
		final DotPanel diagram = new DotPanel(Visualizer.visualize(dfg, 1, log, "ActivityFrequency"));
		diagram.setPreferredSize(new Dimension(1600, 900));
		diagram.setOpaque(true);
		diagram.setBackground(Color.white);
		
		// slider
		final JSlider slider = new JSlider(JSlider.VERTICAL, 0, 100, 100);
		slider.setBackground(Color.white);
		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				diagram.changeDot(Visualizer.visualize(dfg, slider.getValue() / 100d, log, "ActivityFrequency"), true);
			}
		});
		
		// attribute panel selector
		JPanel attributePanel = new JPanel(new BorderLayout());
		
		DefaultComboBoxModel<AggregationValues> comboModel = new DefaultComboBoxModel<AggregationValues>();
		for (AggregationValues av : Utils.getProjectableAttributes(log)) {
			comboModel.addElement(av);
		}
		comboModel.addElement(AggregationValues.FREQUENCY);
		final JComboBox<AggregationValues> comboAttributes = new JComboBox<AggregationValues>(comboModel);
		comboAttributes.setSelectedItem(AggregationValues.FREQUENCY);
		attributePanel.add(comboAttributes, BorderLayout.NORTH);
		
		DefaultListModel<XTrace> listModel = new DefaultListModel<XTrace>();
		for (XTrace trace : log) {
			listModel.addElement(trace);
		}
		final JList<XTrace> tracesSelector = new JList<XTrace>(listModel);
		tracesSelector.setSelectionInterval(0, listModel.getSize() - 1);
		tracesSelector.setCellRenderer(new ListCellRenderer<XTrace>() {
			public Component getListCellRendererComponent(JList<? extends XTrace> list, XTrace value, int index,
					boolean isSelected, boolean cellHasFocus) {
				String traceName = ((XAttributeLiteral) value.getAttributes().get(XConceptExtension.KEY_NAME)).getValue();
				JLabel l = new JLabel(traceName);
				l.setOpaque(true);
				if (isSelected) {
					l.setBackground(tracesSelector.getSelectionBackground());
				} else {
					l.setBackground(tracesSelector.getBackground());
				}
				return l;
			}
		});
		
		comboAttributes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(comboAttributes.getSelectedItem() + tracesSelector.getSelectedValuesList().toString());
//				diagram.changeDot(Visualizer.visualize(dfg, slider.getValue() / 100d, tracesSelector.getSelectedValuesList(), comboAttributes.getSelectedItem().toString()), true);
			}
		});
		tracesSelector.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				System.out.println(comboAttributes.getSelectedItem() + tracesSelector.getSelectedValuesList().toString());
//				diagram.changeDot(Visualizer.visualize(dfg, slider.getValue() / 100d, tracesSelector.getSelectedValuesList(), comboAttributes.getSelectedItem().toString()), true);
			}
		});
		
		attributePanel.add(tracesSelector, BorderLayout.CENTER);
		
		
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(attributePanel, BorderLayout.WEST);
		mainFrame.add(diagram, BorderLayout.CENTER);
		mainFrame.add(slider, BorderLayout.EAST);
		mainFrame.pack();
	}
}
