package cognitiveprom.view.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

import cognitiveprom.log.io.CognitiveLogImporter;
import cognitiveprom.log.io.TSVCognitiveImporter;
import cognitiveprom.logger.Logger;
import cognitiveprom.view.frames.MainFrame;

public class TSVImporterConfigurator implements CognitiveLogImporterConfigurator {

	private CSVFormat format = CSVFormat.TDF;
	
	@Override
	public void configure(MainFrame mainFrame, CognitiveLogImporter importer, String fileName) {
		// parse all aoi
		List<String> aois = new ArrayList<String>();
		Pattern aoiNamePattern = Pattern.compile("AOI\\[(.*)\\]Hit");
		CSVParser parser = null;
		try {
			Reader reader = new InputStreamReader(new BOMInputStream(new FileInputStream(fileName)), "UTF-8");
			parser = new CSVParser(reader, format);
			CSVRecord record = parser.iterator().next();
			for (int i = 0; i < record.size(); i++) {
				String columnName = record.get(i);
				Matcher matcher = aoiNamePattern.matcher(columnName);
				if (matcher.find()) {
					String aoi = matcher.group(1);
					aois.add(aoi);
				}
			}
		} catch (IOException e) {
			Logger.instance().error(e);
		} finally {
			try {
				if (parser != null) {
					parser.close();
				}
			} catch (IOException e) {
				Logger.instance().error(e);
			}
		}
		
		// sort the aois and construct the list model
		DefaultListModel<String> model = new DefaultListModel<String>();
		Collections.sort(aois);
		for (String aoi : aois) {
			model.addElement(aoi);
		}
		
		// add to the jlist
		JList<String> list = new JList<String>();
		list.setModel(model);
		list.setSelectionInterval(0, model.getSize() - 1);
		
		// ask input to the user
		final JComponent[] inputs = new JComponent[] {
				new JLabel("Select the AOIs to import:"),
				new JScrollPane(list)
			};
		int result = JOptionPane.showConfirmDialog(mainFrame, inputs, "Select the AOI", JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			if (importer instanceof TSVCognitiveImporter) {
				for (String aoi : list.getSelectedValuesList()) {
					((TSVCognitiveImporter) importer).addAOIs(aoi);
				}
			}
		}
	}
}
