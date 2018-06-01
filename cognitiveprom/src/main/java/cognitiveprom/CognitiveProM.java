package cognitiveprom;

import cognitiveprom.controllers.ApplicationController;

/**
 * Main application class
 * 
 * @author Andrea Burattin
 */
public class CognitiveProM {
	public static void main(String[] args) {
		ApplicationController.instance().getMainFrame().setVisible(true);
	}
}
