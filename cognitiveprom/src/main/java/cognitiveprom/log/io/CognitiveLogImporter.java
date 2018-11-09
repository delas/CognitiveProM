package cognitiveprom.log.io;

import java.io.File;

import cognitiveprom.exceptions.LogIOException;
import cognitiveprom.log.CognitiveLog;
import cognitiveprom.view.panels.ProgressReceiver;

/**
 * This abstract class should be used to define customized importer of cognitive
 * log files.
 * 
 * @author Andrea Burattin
 */
public abstract class CognitiveLogImporter {

	private ProgressReceiver progressReceiver;
	
	/**
	 * Loads a {@link CognitiveLog} from a file
	 * 
	 * @param fileName the file with the log
	 * @return the imported log
	 * @throws LogIOException
	 */
	public abstract CognitiveLog load(File fileName) throws LogIOException;
	
	/**
	 * Loads a {@link CognitiveLog} from a file
	 * 
	 * @param fileName the file with the log
	 * @return the imported log
	 * @throws LogIOException
	 */
	public CognitiveLog load(String fileName) throws LogIOException {
		return load(new File(fileName));
	}
	
	/**
	 * Sets the {@link ProgressReceiver} of this importer
	 * 
	 * @param progressReceiver
	 */
	public void setProgressReceiver(ProgressReceiver progressReceiver) {
		this.progressReceiver = progressReceiver;
	}
	
	/**
	 * Method to be used by subclasses to trigger a progress update
	 * 
	 * @param minProgress
	 * @param progress
	 * @param maxProgress
	 */
	protected void triggerProgressUpdate(int minProgress, int progress, int maxProgress) {
		if (progressReceiver != null) {
			progressReceiver.update(minProgress, progress, maxProgress);
		}
	}
}
