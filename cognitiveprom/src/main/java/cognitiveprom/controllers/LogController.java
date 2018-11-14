package cognitiveprom.controllers;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.deckfour.xes.model.XTrace;
import org.processmining.framework.util.Pair;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.log.CognitiveLog;
import cognitiveprom.log.io.CognitiveLogExporter;
import cognitiveprom.log.io.CognitiveLogImporter;
import cognitiveprom.log.utils.XCognitiveLogHelper;
import cognitiveprom.utils.FileFilterHelper;
import cognitiveprom.utils.RuntimeUtils;
import cognitiveprom.view.io.CognitiveLogImporterConfigurator;
import cognitiveprom.view.workers.LoadFileWorker;
import cognitiveprom.view.workers.SaveFileWorker;

/**
 * 
 * @author Andrea Burattin
 */
public class LogController {

	private static final String KEY_OPEN_LOG_LOCATION = "OPEN_LOG_LOCATION";
	private static final String KEY_SAVE_LOG_LOCATION = "SAVE_LOG_LOCATION";
	private static final String KEY_OPEN_FILE_FILTER = "OPEN_LOG_FILTER";
	private static final String KEY_SAVE_FILE_FILTER = "SAVE_LOG_FILTER";
	
	private CognitiveLog log;
	private Map<String, XTrace> cache;
	
	private ApplicationController applicationController;
	private ConfigurationSet configuration;
	
	public LogController(ApplicationController applicationController) {
		this.applicationController = applicationController;
		this.configuration = applicationController.getConfiguration(LogController.class.getCanonicalName());
	}
	
	public CognitiveLog log() {
		return log;
	}
	
	public XTrace log(String subjectName) {
		return cache.get(subjectName);
	}
	
	public Collection<String> getSubjectNames() {
		return cache.keySet();
	}
	
	public void setCognitiveLog(CognitiveLog log) {
		this.log = log;
		this.cache = new HashMap<String, XTrace>();
		
		for(XTrace trace : log) {
			cache.put(XCognitiveLogHelper.getSubjectName(trace), trace);
		}
	}
	
	public void loadFile() {
		JFileChooser fc = new JFileChooser(new File(configuration.get(KEY_OPEN_LOG_LOCATION, RuntimeUtils.getHomeFolder())));
		fc.setMultiSelectionEnabled(false);
		
		FileFilterHelper.assignImportFileFilters(fc);
		String previousFileFilter = configuration.get(KEY_OPEN_FILE_FILTER, RuntimeUtils.getHomeFolder());
		for (FileFilter ff : fc.getChoosableFileFilters()) {
			if (ff.getDescription().equals(previousFileFilter)) {
				fc.setFileFilter(ff);
			}
		}
		
		int returnVal = fc.showOpenDialog(applicationController.getMainFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			configuration.set(KEY_OPEN_LOG_LOCATION, fc.getSelectedFile().getAbsolutePath().substring(0, fc.getSelectedFile().getAbsolutePath().lastIndexOf(File.separator)));
			configuration.set(KEY_OPEN_FILE_FILTER, fc.getFileFilter().getDescription());
			
			if (fc.getFileFilter() instanceof FileNameExtensionFilter) {
				loadFile(
						fc.getSelectedFile().getAbsolutePath(),
						FileFilterHelper.getImporterFromFileFilter((FileNameExtensionFilter) fc.getFileFilter()));
			} else {
				loadFile(
						fc.getSelectedFile().getAbsolutePath(),
						FileFilterHelper.getImporterFromFileName(fc.getSelectedFile().getAbsolutePath()));
			}
		}
	}
	
	public void loadFile(String fileName, Pair<CognitiveLogImporter, CognitiveLogImporterConfigurator> importer) {
		if (importer.getSecond() != null) {
			importer.getSecond().configure(applicationController.getMainFrame(), importer.getFirst(), fileName);
		}
		applicationController.processController().reset();
		new LoadFileWorker(fileName, importer.getFirst()).execute();
	}

	public void saveFile() {
		JFileChooser fc = new JFileChooser(new File(configuration.get(KEY_SAVE_LOG_LOCATION, RuntimeUtils.getHomeFolder())));
		fc.setMultiSelectionEnabled(false);
		
		FileFilterHelper.assignExportFileFilters(fc);
		String previousFileFilter = configuration.get(KEY_SAVE_FILE_FILTER, RuntimeUtils.getHomeFolder());
		for (FileFilter ff : fc.getChoosableFileFilters()) {
			if (ff.getDescription().equals(previousFileFilter)) {
				fc.setFileFilter(ff);
			}
		}
		
		int returnVal = fc.showSaveDialog(applicationController.getMainFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			final String fileName = FileFilterHelper.fixFileName(fc.getSelectedFile().getAbsolutePath(), (FileNameExtensionFilter) fc.getFileFilter());
			configuration.set(KEY_SAVE_LOG_LOCATION, fileName.substring(0, fileName.lastIndexOf(File.separator)));
			configuration.set(KEY_SAVE_FILE_FILTER, fc.getFileFilter().getDescription());

			CognitiveLogExporter exporter = FileFilterHelper.getExporterFromFileName((FileNameExtensionFilter) fc.getFileFilter());
			new SaveFileWorker(fileName, log, exporter).execute();;
		}
	}
	
	public void closeFile() {
		log = null;
		cache = null;
	}
}
