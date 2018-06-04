package cognitiveprom.view.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.view.widgets.MainToolbar;

public class MainPanel extends ConfigurableWindowPage {

	private static final long serialVersionUID = -8854311251130985383L;
	
	// main window components
	private MainToolbar mainWindowToolbar = null;
	private ProgressStack progressStack = null;
	private Console debugConsole = null;
	
	public MainPanel(ConfigurationSet conf) {
		super(conf);
		
		// place the components of the window
		placeComponents();
	}
	
	/**
	 * Method to get the window console
	 * 
	 * @return
	 */
	public Console getConsole() {
		return debugConsole;
	}
	
	/**
	 * Method to get the stacked progress notifications area
	 * 
	 * @return
	 */
	public ProgressStack getProgressStack() {
		return progressStack;
	}
	
	/**
	 * Method to get the toolbar
	 * 
	 * @return
	 */
	public MainToolbar getToolbar() {
		return mainWindowToolbar;
	}

	private void placeComponents() {
		// set the main layout
		setLayout(new BorderLayout());
		
		// main window container
		JPanel mainWindowContainer = new JPanel();
		mainWindowContainer.setLayout(new GridBagLayout());
		add(mainWindowContainer, BorderLayout.CENTER);
		
		// add the notifications and debug console
		GridBagConstraints c = new GridBagConstraints();
		progressStack = new ProgressStack(conf.getChild(ProgressStack.class.getCanonicalName()));
		debugConsole = new Console(conf.getChild(Console.class.getCanonicalName()));
		JPanel south = new JPanel(new BorderLayout());
		south.add(progressStack, BorderLayout.NORTH);
		south.add(debugConsole, BorderLayout.SOUTH);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.SOUTH;
		mainWindowContainer.add(south, c);
		
		// insert the toolbar
		mainWindowToolbar = new MainToolbar();
		add(mainWindowToolbar, BorderLayout.NORTH);
	}
}
