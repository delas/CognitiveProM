package cognitiveprom.controllers;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.processmining.framework.util.Pair;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.log.CognitiveLog;
import cognitiveprom.log.io.CognitiveLogImporter;
import cognitiveprom.utils.FileFilterHelper;
import cognitiveprom.utils.RuntimeUtils;
import cognitiveprom.view.io.CognitiveLogImporterConfigurator;
import cognitiveprom.view.io.TSVImporterConfigurator;

/**
 * 
 * @author Andrea Burattin
 */
public class LogsController {

	private static final String KEY_LOGS_LOCATION = "LOGS_LOCATION";
	
	private ApplicationController applicationController;
	private ConfigurationSet configuration;
	
	public LogsController(ApplicationController applicationController) {
		this.applicationController = applicationController;
		this.configuration = applicationController.getConfiguration(LogsController.class.getCanonicalName());
	}

	public void loadFile() {
		JFileChooser fc = new JFileChooser(new File(configuration.get(KEY_LOGS_LOCATION, RuntimeUtils.getHomeFolder())));
		FileFilterHelper.assignImportFileFilters(fc);
		
		int returnVal = fc.showOpenDialog(applicationController.getMainFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			final String fileName = fc.getSelectedFile().getAbsolutePath();
			configuration.set(KEY_LOGS_LOCATION, fileName.substring(0, fileName.lastIndexOf(File.separator)));

			final Pair<CognitiveLogImporter, CognitiveLogImporterConfigurator> importer = FileFilterHelper.getImporterFromFileName((FileNameExtensionFilter) fc.getFileFilter());
			
			if (importer.getSecond() != null) {
				importer.getSecond().configure(applicationController.getMainFrame(), importer.getFirst());
			}
			
			SwingWorker<CognitiveLog, Void> worker = new SwingWorker<CognitiveLog, Void>() {
				@Override
				protected CognitiveLog doInBackground() throws Exception {
					TSVImporterConfigurator config = new TSVImporterConfigurator();
					config.configure(applicationController.getMainFrame(), importer.getFirst());
					return null;
//					return importer.importModel(fileName, ApplicationController.instance().getMainWindow().getProgressStack().askForNewProgress());
				}
				
				@Override
				protected void done() {
					
				}
			};
			worker.execute();
		}
	}
}
