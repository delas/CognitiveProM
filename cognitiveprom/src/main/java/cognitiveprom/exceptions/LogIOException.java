package cognitiveprom.exceptions;

/**
 * An exception which can occur during the import or the export of a log file
 * 
 * @author Andrea Burattin
 */
public class LogIOException extends Exception {

	private static final long serialVersionUID = 6611592367102399986L;

	/**
	 * 
	 * @param message
	 */
	public LogIOException(String message) {
		super(message);
	}
}
