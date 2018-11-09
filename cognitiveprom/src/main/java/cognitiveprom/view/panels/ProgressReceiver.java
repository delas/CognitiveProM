package cognitiveprom.view.panels;

/**
 * This interface represents a panel that can be the receiver of progress
 * updates.
 * 
 * @author Andrea Burattin
 */
public interface ProgressReceiver {

	/**
	 * This method is called whenever a progress update is available
	 * 
	 * @param minProgress the minimum progress
	 * @param progress the current value of the progress
	 * @param maxProgress the maximum progress
	 */
	public void update(int minProgress, int progress, int maxProgress);
}
