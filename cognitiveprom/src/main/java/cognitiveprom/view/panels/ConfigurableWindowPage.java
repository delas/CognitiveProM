package cognitiveprom.view.panels;

import javax.swing.JPanel;

import cognitiveprom.config.ConfigurationSet;

/**
 * This abstract class must be derived by all the panels that wants to belong
 * to the main window.
 * 
 * @author Andrea Burattin
 */
public abstract class ConfigurableWindowPage extends JPanel {

	private static final long serialVersionUID = -1656824607024522518L;
	protected ConfigurationSet conf;
	
	/**
	 * Class constructor
	 * 
	 * @param conf
	 */
	public ConfigurableWindowPage(ConfigurationSet conf) {
		this.conf = conf;
	}
	
	/**
	 * Class to return the page name.
	 * 
	 * <strong>Attention:</strong> each page must have a unique value
	 */
	public String getName() {
		return getClass().getCanonicalName();
	}
}
