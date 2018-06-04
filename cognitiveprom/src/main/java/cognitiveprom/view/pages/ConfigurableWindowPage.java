package cognitiveprom.view.pages;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.view.panels.ConfigurablePanel;

/**
 * This abstract class must be derived by all the panels that wants to belong
 * to the main window.
 * 
 * @author Andrea Burattin
 */
public abstract class ConfigurableWindowPage extends ConfigurablePanel {

	private static final long serialVersionUID = -1656824607024522518L;
	
	/**
	 * Class constructor
	 * 
	 * @param conf
	 */
	public ConfigurableWindowPage(ConfigurationSet conf) {
		super(conf);
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
