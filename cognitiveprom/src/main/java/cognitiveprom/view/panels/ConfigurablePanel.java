package cognitiveprom.view.panels;

import javax.swing.JPanel;

import cognitiveprom.config.ConfigurationSet;

/**
 * A panel to be used in the context of the application, since it allows to be
 * configured using a {@link ConfigurationSet}
 * 
 * @author Andrea Burattin
 */
public abstract class ConfigurablePanel extends JPanel {

	private static final long serialVersionUID = 7140139270912317446L;
	protected ConfigurationSet conf;

	/**
	 * Class constructor
	 * 
	 * @param conf
	 */
	public ConfigurablePanel(ConfigurationSet conf) {
		this.conf = conf;
	}
}
