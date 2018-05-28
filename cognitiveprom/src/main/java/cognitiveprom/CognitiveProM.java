package cognitiveprom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.deckfour.xes.in.XParser;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XLog;
import org.processmining.plugins.graphviz.visualisation.DotPanel;

import cognitiveprom.tools.DfgMinerResult;
import cognitiveprom.tools.Miner;
import cognitiveprom.tools.Visualizer;

public class CognitiveProM {

	public static void main(String[] args) throws Exception {
		String file = "C:\\Users\\andbur\\Desktop\\1.1.tsv-quadrants.xes";
//		String file = "C:\\Users\\andbur\\Desktop\\test.xes";
//		String file = "C:\\Users\\andbur\\Desktop\\test-2starts.xes";
		XParser parser = new XesXmlParser();
		XLog log = parser.parse(new File(file)).get(0);
		
		final DfgMinerResult dfg = Miner.mineDfg(log);
		
		JFrame mainFrame = new JFrame("CognitiveProM");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(1600, 900);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		
		final DotPanel diagram = new DotPanel(Visualizer.visualize(dfg, 1));
		
		diagram.setPreferredSize(new Dimension(1600, 900));
		diagram.setOpaque(true);
		diagram.setBackground(Color.white);
		
		final JSlider slider = new JSlider(JSlider.VERTICAL, 0, 100, 100);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				diagram.changeDot(Visualizer.visualize(dfg, slider.getValue() / 100d), true);
			}
		});
		
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(diagram, BorderLayout.CENTER);
		mainFrame.add(slider, BorderLayout.EAST);
		mainFrame.pack();
	}
}
