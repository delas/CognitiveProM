package cognitiveprom.controllers;

import java.io.IOException;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.config.UIConfiguration;
import cognitiveprom.utils.CPUUtils;
import cognitiveprom.utils.Logger;
import cognitiveprom.view.frames.MainFrame;
import cognitiveprom.view.panels.LoadProcessPanel;
import cognitiveprom.view.panels.MainPanel;

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
	private MainPanel mainPanel;
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
		mainPanel = new MainPanel(configuration.getChild(MainPanel.class.getCanonicalName()));
		loadProcessPanel = new LoadProcessPanel(configuration.getChild(LoadProcessPanel.class.getCanonicalName()));
		
		// creates the main frame
		mainFrame = new MainFrame(this);
		mainFrame.addPage(mainPanel);
		mainFrame.addPage(loadProcessPanel);
		
		// initialization logging
		Logger.instance().debug("Application started!");
		Logger.instance().debug("You have " + CPUUtils.CPUAvailable() + " CPU(s) available");
	}
	
	public void showMainFrame() {
		mainFrame.setVisible(true);
	}
	
	public void showLoadProcessPanel() {
		mainFrame.showPage(loadProcessPanel.getClass().getCanonicalName());
	}
	
	public void showMainPanel() {
		
	}

	/**
	 * 
	 * @return
	 */
	public MainFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * 
	 * @return
	 */
	public MainPanel getMainWindow() {
		return mainPanel;
	}
	
	/**
	 * 
	 * @return
	 */
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
		Logger.instance().debug("Application terminated");
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
