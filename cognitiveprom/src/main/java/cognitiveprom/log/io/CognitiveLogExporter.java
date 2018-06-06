package cognitiveprom.log.io;

import java.io.File;

import cognitiveprom.exceptions.LogIOException;
import cognitiveprom.log.CognitiveLog;

/**
 * This abstract class should be used to define customized exporter of cognitive
 * log files.
 * 
 * @author Andrea Burattin
 */
public abstract class CognitiveLogExporter {

	/**
	 * Saves a {@link CognitiveLog} into a file
	 * 
	 * @param fileName the file with the log
	 * @param log the cognitive log to save
	 * @throws LogIOException 
	 */
	public abstract void save(File fileName, CognitiveLog log) throws LogIOException;
	
	/**
	 * Saves a {@link CognitiveLog} into a file
	 * 
	 * @param fileName the file with the log
	 * @param log the cognitive log to save
	 * @throws LogIOException 
	 */
	public void save(String fileName, CognitiveLog log) throws LogIOException {
		save(new File(fileName), log);
	}
}
