package cognitiveprom;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import cognitiveprom.controllers.ApplicationController;

/**
 * Main application class
 * 
 * @author Andrea Burattin
 */
public class CognitiveProM {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		// sets the look and feel
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		// gui startup
		ApplicationController.instance().showMainFrame(true);
		ApplicationController.instance().showLoadProcessPage();
	}
}
