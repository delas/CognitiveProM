package cognitiveprom;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import cognitiveprom.controllers.ApplicationController;
import cognitiveprom.utils.FileFilterHelper;

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
		
		if (args.length == 0) {
			ApplicationController.instance().showLoadProcessPage();
		} else {
			String fileName = args[0];
			ApplicationController.instance().log().loadFile(fileName, FileFilterHelper.getImporterFromFileName(fileName));
		}
	}
}
