package cognitiveprom.log.io;

import java.io.File;

import com.google.common.io.Files;

import cognitiveprom.exceptions.LogImportException;
import cognitiveprom.log.CognitiveLog;

/**
 * This abstract class should be used to define customized importer of cognitive log files.
 * 
 * @author Andrea Burattin
 */
public abstract class CognitiveLogImporter {

	/**
	 * Returns a brief description of the importer. To be used, for example, on a file open filter
	 * 
	 * @return the description
	 */
	public abstract String getDescription();
	
	/**
	 * Returns the file extension supported by the extension. To be used, for example, on a file open filter
	 * 
	 * @return the supported extension
	 */
	public abstract String getSupportedFileExtension();
	
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
	
	/**
	 * Checks if the provided file is compliant with the declared supported extension
	 * 
	 * @param filename the filename to check
	 * @return <tt>true</tt> if the file is compatible with the extension, <tt>false</tt> otherwise
	 */
	public boolean isValidFile(String filename) {
		return getSupportedFileExtension().equals(Files.getFileExtension(filename));
	}
}
