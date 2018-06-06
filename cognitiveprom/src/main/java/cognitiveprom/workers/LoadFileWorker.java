package cognitiveprom.workers;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import cognitiveprom.controllers.ApplicationController;
import cognitiveprom.log.CognitiveLog;
import cognitiveprom.log.io.CognitiveLogImporter;
import cognitiveprom.utils.Logger;

public class LoadFileWorker extends SwingWorker<CognitiveLog, Void> {

	private String fileName;
	private CognitiveLogImporter importer;
	
	public LoadFileWorker(String fileName, CognitiveLogImporter importer) {
		this.fileName = fileName;
		this.importer = importer;
	}
	
	@Override
	protected CognitiveLog doInBackground() throws Exception {
		ApplicationController.instance().showWaitingPage();
		Logger.instance().info("Loading file `" + fileName + "`...");
		
		return importer.load(fileName);
	}
	
	@Override
	protected void done() {
		Logger.instance().info("File `" + fileName + "` loaded");
		
		try {
			ApplicationController.instance().logController().setCognitiveLog(get());
		} catch (InterruptedException | ExecutionException e) { }
		
		ApplicationController.instance().getMainFrame().setTitle(fileName);
		ApplicationController.instance().showMainPage();
		
		// once the file is loaded start the mining
		ApplicationController.instance().processController().mineLog();
	}
}
