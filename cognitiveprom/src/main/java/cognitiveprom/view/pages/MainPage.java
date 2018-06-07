package cognitiveprom.view.pages;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JPanel;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.view.panels.AdvancedConfiguration;
import cognitiveprom.view.panels.Console;
import cognitiveprom.view.panels.ProcessVisualizer;
import cognitiveprom.view.panels.WaitingPanel;
import cognitiveprom.view.widgets.MainToolbar;

public class MainPage extends ConfigurableWindowPage {

	private static final long serialVersionUID = -8854311251130985383L;
	
	// main window components
	private JPanel centralPanel = null;
	private MainToolbar mainWindowToolbar = null;
	private Console debugConsole = null;
	private WaitingPanel waitingPanel = null;
	private ProcessVisualizer processVisualizer = null;
	
	public MainPage(ConfigurationSet conf) {
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
	
	public WaitingPanel getWaitingPanel() {
		return waitingPanel;
	}
	
	public ProcessVisualizer getProcessVisualizer() {
		return processVisualizer;
	}
	
	public void showWaitingPanel(String text) {
		showPage(waitingPanel.getClass().getCanonicalName());
		waitingPanel.start(text);
	}
	
	public void showProcessVisualizer() {
		showPage(processVisualizer.getClass().getCanonicalName());
	}
	
	/**
	 * Method to get the toolbar
	 * 
	 * @return
	 */
	public MainToolbar getToolbar() {
		return mainWindowToolbar;
	}
	
	/**
	 * Method to put into the foreground the page with the given name
	 * 
	 * @param pageName
	 */
	protected void showPage(String pageName) {
		CardLayout cl = (CardLayout) centralPanel.getLayout();
		cl.show(centralPanel, pageName);
	}

	private void placeComponents() {
		// set the main layout
		setLayout(new BorderLayout());
		
		// sets the main panel
		centralPanel = new JPanel();
		centralPanel.setLayout(new CardLayout());
		
		processVisualizer = new ProcessVisualizer(conf.getChild(ProcessVisualizer.class.getCanonicalName()));
		centralPanel.add(processVisualizer, processVisualizer.getClass().getCanonicalName());
		
		waitingPanel = new WaitingPanel(conf.getChild(WaitingPanel.class.getCanonicalName()));
		centralPanel.add(waitingPanel, waitingPanel.getClass().getCanonicalName());
		
		add(centralPanel, BorderLayout.CENTER);
		
		// add the notifications and debug console
		debugConsole = new Console(conf.getChild(Console.class.getCanonicalName()));
		add(debugConsole, BorderLayout.SOUTH);
		
		// insert the toolbar
		mainWindowToolbar = new MainToolbar();
		add(mainWindowToolbar, BorderLayout.NORTH);
	}
}
