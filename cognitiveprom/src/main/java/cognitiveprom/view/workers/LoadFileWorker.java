package cognitiveprom.view.workers;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import cognitiveprom.controllers.ApplicationController;
import cognitiveprom.log.CognitiveLog;
import cognitiveprom.log.io.CognitiveLogImporter;
import cognitiveprom.logger.Logger;

public class LoadFileWorker extends SwingWorker<CognitiveLog, Void> {

	private String fileName;
	private CognitiveLogImporter importer;
	private boolean appendFile;
	
	public LoadFileWorker(String fileName, CognitiveLogImporter importer, boolean appendFile) {
		this.fileName = fileName;
		this.importer = importer;
		this.appendFile = appendFile;
	}
	
	@Override
	protected CognitiveLog doInBackground() throws Exception {
		ApplicationController.instance().showWaitingPage("Log importing");
		Logger.instance().info("Loading file `" + fileName + "`...");
		
		importer.setProgressReceiver(ApplicationController.instance().getWaitingPageProgressReceiver());
		
		CognitiveLog log = importer.load(fileName);
		if (appendFile) {
			log.merge(ApplicationController.instance().logController().log());
		}
		
		return log;
	}
	
	@Override
	protected void done() {
		Logger.instance().info("File `" + fileName + "` loaded");
		
		try {
			ApplicationController.instance().logController().setCognitiveLog(get());
		} catch (InterruptedException | ExecutionException e) {
			Logger.instance().error(e);
		}
		
		ApplicationController.instance().getMainFrame().setTitle(fileName);
		ApplicationController.instance().showMainPage();
		
		// once the file is loaded start the mining
		ApplicationController.instance().processController().mineLog();
	}
}
