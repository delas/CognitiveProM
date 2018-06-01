package cognitiveprom.exceptions;

/**
 * An exception which can occur during the import of a log file
 * 
 * @author Andrea Burattin
 */
public class LogImportException extends Exception {

	private static final long serialVersionUID = 6611592367102399986L;

	/**
	 * 
	 * @param message
	 */
	public LogImportException(String message) {
		super(message);
	}
}
