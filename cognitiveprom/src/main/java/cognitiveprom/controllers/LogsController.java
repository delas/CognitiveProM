package cognitiveprom.controllers;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.processmining.framework.util.Pair;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.log.CognitiveLog;
import cognitiveprom.log.io.CognitiveLogImporter;
import cognitiveprom.utils.FileFilterHelper;
import cognitiveprom.utils.Logger;
import cognitiveprom.utils.RuntimeUtils;
import cognitiveprom.view.io.CognitiveLogImporterConfigurator;

/**
 * 
 * @author Andrea Burattin
 */
public class LogsController {

	private static final String KEY_LOGS_LOCATION = "LOGS_LOCATION";
	private static final String KEY_FILE_FILTER = "LOGS_FILTER";
	
	private CognitiveLog log;
	
	private ApplicationController applicationController;
	private ConfigurationSet configuration;
	
	public LogsController(ApplicationController applicationController) {
		this.applicationController = applicationController;
		this.configuration = applicationController.getConfiguration(LogsController.class.getCanonicalName());
	}
	
	public CognitiveLog loadedLog() {
		return log;
	}
	
	public void loadFile() {
		JFileChooser fc = new JFileChooser(new File(configuration.get(KEY_LOGS_LOCATION, RuntimeUtils.getHomeFolder())));
		fc.setMultiSelectionEnabled(false);
		
		FileFilterHelper.assignImportFileFilters(fc);
		String previousFileFilter = configuration.get(KEY_FILE_FILTER, RuntimeUtils.getHomeFolder());
		for (FileFilter ff : fc.getChoosableFileFilters()) {
			if (ff.getDescription().equals(previousFileFilter)) {
				fc.setFileFilter(ff);
			}
		}
		
		int returnVal = fc.showOpenDialog(applicationController.getMainFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			final String fileName = fc.getSelectedFile().getAbsolutePath();
			configuration.set(KEY_LOGS_LOCATION, fileName.substring(0, fileName.lastIndexOf(File.separator)));
			configuration.set(KEY_FILE_FILTER, fc.getFileFilter().getDescription());

			final Pair<CognitiveLogImporter, CognitiveLogImporterConfigurator> importer = FileFilterHelper.getImporterFromFileName((FileNameExtensionFilter) fc.getFileFilter());
			
			if (importer.getSecond() != null) {
				importer.getSecond().configure(applicationController.getMainFrame(), importer.getFirst());
			}
			
			SwingWorker<CognitiveLog, Void> worker = new SwingWorker<CognitiveLog, Void>() {
				@Override
				protected CognitiveLog doInBackground() throws Exception {
					log = importer.getFirst().load(fileName);
					
					Logger.instance().info("Loaded file `" + fileName + "`");
					return log;
				}
				
				@Override
				protected void done() {
					applicationController.showMainPanel();
				}
			};
			worker.execute();
		}
	}
}
