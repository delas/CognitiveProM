package cognitiveprom.controllers;

import java.util.concurrent.ExecutionException;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.processmining.framework.util.Pair;

import cognitiveprom.log.CognitiveLog;
import cognitiveprom.log.io.CognitiveLogImporter;
import cognitiveprom.utils.FileFilterHelper;
import cognitiveprom.view.frames.MainFrame;
import cognitiveprom.view.io.CognitiveLogImporterConfigurator;
import cognitiveprom.view.io.TSVImporterConfigurator;
import cognitiveprom.view.panels.MainWindow;

/**
 * This class represents the application controller, and is in charge of
 * managing the entire application workflow.
 *
 * @author Andrea Burattin
 */
public class ApplicationController {

	private static ApplicationController controller = new ApplicationController();
	
	private CognitiveLog log;
	private MainFrame mainFrame;
	private MainWindow mainWindow;
	
	/**
	 * This method returns the available instance of the application controller.
	 * 
	 * @return an application controller
	 */
	public static ApplicationController instance() {
		return controller;
	}
	
	/**
	 * Private class constructor. Access the application controller through the
	 * {@link #instance()} method.
	 */
	private ApplicationController() {
		// creates gui
		mainWindow = new MainWindow(this);
		mainFrame = new MainFrame(this);
	}
	
	/**
	 * This method returns the {@link MainFrame} built.
	 * 
	 * @return the main frame available
	 */
	public MainFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * This method returns the {@link MainWindow} built.
	 * 
	 * @return the main window available
	 */
	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public void loadFile() {
		JFileChooser fc = new JFileChooser();
		FileFilterHelper.assignImportFileFilters(fc);
		
		int returnVal = fc.showOpenDialog(ApplicationController.instance().getMainFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			final String fileName = fc.getSelectedFile().getAbsolutePath();
			final Pair<CognitiveLogImporter, CognitiveLogImporterConfigurator> importer = FileFilterHelper.getImporterFromFileName((FileNameExtensionFilter) fc.getFileFilter());
			
			if (importer.getSecond() != null) {
				importer.getSecond().configure(getMainFrame(), importer.getFirst());
			}
			
			SwingWorker<CognitiveLog, Void> worker = new SwingWorker<CognitiveLog, Void>() {
				@Override
				protected CognitiveLog doInBackground() throws Exception {
					TSVImporterConfigurator config = new TSVImporterConfigurator();
					config.configure(getMainFrame(), importer.getFirst());
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
