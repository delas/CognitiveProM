package cognitiveprom.view.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import cognitiveprom.CognitiveProMConstants;
import cognitiveprom.controllers.ApplicationController;

/**
 * This class contains the main frame of CognitiveProM
 *
 * @author Andrea Burattin
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = -495846435104527928L;
	
	// default configuration values
	private static final int DEFAULT_WIDTH = 1600;
	private static final int DAFAULT_HEIGHT = 900;
	private static final int MINIMUM_WIDTH = 800;
	private static final int MINIMUM_HEIGHT = 450;
	
	/**
	 * 
	 * @param controller
	 */
	public MainFrame(ApplicationController controller) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(CognitiveProMConstants.SIGNATURE);
		
		setWindowState();
		
		// add here the main window
		setLayout(new BorderLayout());
		add(controller.getMainWindow());
	}
	
	/**
	 * Method to set the state of the window
	 */
	protected void setWindowState() {
		setSize(DEFAULT_WIDTH, DAFAULT_HEIGHT);
		setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
		setLocationRelativeTo(null);
	}
}
