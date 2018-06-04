package cognitiveprom.view.pages;

import java.awt.BorderLayout;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.view.panels.WaitingPanel;

public class WaitingPage extends ConfigurableWindowPage {

	private static final long serialVersionUID = 8639502518410521650L;
	private WaitingPanel waitingPanel;

	public WaitingPage(ConfigurationSet conf) {
		super(conf);
		
		// place the components of the window
		placeComponents();
	}

	public void start(String firstLine) {
		waitingPanel.start(firstLine);
	}
	
	public void stop() {
		waitingPanel.stop();
	}
	
	private void placeComponents() {
		waitingPanel = new WaitingPanel(conf.getChild(WaitingPanel.class.getCanonicalName()));
		
		setLayout(new BorderLayout());
		add(waitingPanel, BorderLayout.CENTER);
	}
}
