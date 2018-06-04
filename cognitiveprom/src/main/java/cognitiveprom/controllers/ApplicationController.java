package cognitiveprom.controllers;

import java.io.IOException;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.config.UIConfiguration;
import cognitiveprom.view.frames.MainFrame;
import cognitiveprom.view.panels.LoadProcessPanel;
import cognitiveprom.view.panels.MainWindow;

/**
 * This class represents the application controller, and is in charge of
 * managing the entire application workflow.
 *
 * @author Andrea Burattin
 */
public class ApplicationController {

	private static ApplicationController controller = new ApplicationController();
	
	private LogsController logsController;
	private ConfigurationSet configuration;
	
	private MainFrame mainFrame;
	private MainWindow mainWindow;
	private LoadProcessPanel loadProcessPanel;
	
	
	/**
	 * This method returns the available instance of the application controller.
	 * 
	 * @return an application controller
	 */
	public static ApplicationController instance() {
		return controller;
	}
	
	/**
	 * Private class constructor. Access the application controller through the
	 * {@link #instance()} method.
	 */
	private ApplicationController() {
		configuration = UIConfiguration.master();
		
		// creates children controllers
		logsController = new LogsController(this);
		
		// creates the panels
		mainWindow = new MainWindow(configuration.getChild(MainWindow.class.getCanonicalName()));
		loadProcessPanel = new LoadProcessPanel(configuration.getChild(LoadProcessPanel.class.getCanonicalName()));
		
		// creates the main frame
		mainFrame = new MainFrame(this);
	}
	
	/**
	 * This method returns the {@link MainFrame} built.
	 * 
	 * @return the main frame available
	 */
	public MainFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * This method returns the {@link MainWindow} built.
	 * 
	 * @return the main window available
	 */
	public MainWindow getMainWindow() {
		return mainWindow;
	}
	
	public LoadProcessPanel getLoadProcessPanel() {
		return loadProcessPanel;
	}
	
	/**
	 * This method closes the application controller (to be invoked just before
	 * exiting the application)
	 * @throws IOException 
	 */
	public void close() throws IOException {
		UIConfiguration.save();
	}
	
	/**
	 * This method returns the general configuration
	 * 
	 * @return the general configuration
	 */
	public ConfigurationSet getGeneralConfiguration() {
		return configuration;
	}
	
	/**
	 * This method returns the configuration associated to the provided root
	 * 
	 * @param root the root of the configuration
	 * @return the configuration associated to the provided root
	 */
	public ConfigurationSet getConfiguration(String root) {
		return configuration.getChild(root);
	}
	
	/**
	 * This method returns the logs controller
	 * 
	 * @return the logs controller
	 */
	public LogsController log() {
		return logsController;
	}
}
