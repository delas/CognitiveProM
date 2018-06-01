package cognitiveprom.view.io;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import cognitiveprom.log.io.CognitiveLogImporter;
import cognitiveprom.log.io.TSVCognitiveImporter;
import cognitiveprom.view.frames.MainFrame;

public class TSVImporterConfigurator implements CognitiveLogImporterConfigurator {

	@Override
	public void configure(MainFrame mainFrame, CognitiveLogImporter importer) {
		JTextArea text = new JTextArea();
		text.setRows(10);
		final JComponent[] inputs = new JComponent[] {
				new JLabel("Inset the name of all AOIs to identify, one AOI per line:"),
				text
			};
		int result = JOptionPane.showConfirmDialog(null, inputs, "Insert the AOI", JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			if (importer instanceof TSVCognitiveImporter) {
				for (String line : text.getText().split("\n")) {
					line = line.trim();
					if (!line.isEmpty()) {
						((TSVCognitiveImporter) importer).addAOIs(line.trim());
					}
				}
			}
		}
	}
}
