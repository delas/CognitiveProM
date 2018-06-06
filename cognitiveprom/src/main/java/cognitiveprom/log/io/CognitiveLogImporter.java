package cognitiveprom.log.io;

import java.io.File;

import cognitiveprom.exceptions.LogIOException;
import cognitiveprom.log.CognitiveLog;

/**
 * This abstract class should be used to define customized importer of cognitive
 * log files.
 * 
 * @author Andrea Burattin
 */
public abstract class CognitiveLogImporter {

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
}
