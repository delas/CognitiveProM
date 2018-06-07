package cognitiveprom.view.workers;

import javax.swing.SwingWorker;

import cognitiveprom.log.CognitiveLog;
import cognitiveprom.log.io.CognitiveLogExporter;
import cognitiveprom.utils.Logger;

public class SaveFileWorker extends SwingWorker<Void, Void> {

	private String fileName;
	private CognitiveLog log;
	private CognitiveLogExporter exporter;
	
	public SaveFileWorker(String fileName, CognitiveLog log, CognitiveLogExporter exporter) {
		this.fileName = fileName;
		this.log = log;
		this.exporter = exporter;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		Logger.instance().info("Saving file `" + fileName + "`...");
		
		exporter.save(fileName, log);
		return null;
	}
	
	@Override
	protected void done() {
		Logger.instance().info("File `" + fileName + "` saved");
	}
}
