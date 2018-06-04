package cognitiveprom.log.io;

import java.io.File;

import cognitiveprom.exceptions.LogImportException;
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
	 * @throws LogImportException
	 */
	public abstract CognitiveLog load(File fileName) throws LogImportException;
	
	/**
	 * Loads a {@link CognitiveLog} from a file
	 * 
	 * @param fileName the file with the log
	 * @return the imported log
	 * @throws LogImportException
	 */
	public CognitiveLog load(String fileName) throws LogImportException {
		return load(new File(fileName));
	}
}
