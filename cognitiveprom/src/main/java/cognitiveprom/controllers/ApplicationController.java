package cognitiveprom.controllers;

import java.io.IOException;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.config.UIConfiguration;
import cognitiveprom.utils.CPUUtils;
import cognitiveprom.utils.Logger;
import cognitiveprom.view.frames.MainFrame;
import cognitiveprom.view.pages.LoadProcessPage;
import cognitiveprom.view.pages.MainPage;
import cognitiveprom.view.pages.WaitingPage;

/**
 * This class represents the application controller, and is in charge of
 * managing the entire application workflow.
 *
 * @author Andrea Burattin
 */
public class ApplicationController {

	private static ApplicationController controller = new ApplicationController();
	
	private LogController logsController;
	private ProcessController modelController;
	private ConsoleController consoleController;
	
	private ConfigurationSet configuration;
	private MainFrame mainFrame;
	private MainPage mainPage;
	private LoadProcessPage loadProcessPage;
	private WaitingPage waitingPage;
	
	
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
		// creates the master configuration
		configuration = UIConfiguration.master();
		
		// creates the panels
		mainPage = new MainPage(configuration.getChild(MainPage.class.getCanonicalName()));
		loadProcessPage = new LoadProcessPage(configuration.getChild(LoadProcessPage.class.getCanonicalName()));
		waitingPage = new WaitingPage(configuration.getChild(WaitingPage.class.getCanonicalName()));
		
		// creates the main frame
		mainFrame = new MainFrame(this);
		mainFrame.addPage(mainPage);
		mainFrame.addPage(loadProcessPage);
		mainFrame.addPage(waitingPage);

		// creates children controllers
		logsController = new LogController(this);
		modelController = new ProcessController(this);
		consoleController = new ConsoleController(this);
		
		// initialization logging
		Logger.instance().debug("Application started!");
		Logger.instance().debug("You have " + CPUUtils.CPUAvailable() + " CPU(s) available");
	}
	
	/**
	 * Sets the visibility of the main frame
	 * 
	 * @param show
	 */
	public void showMainFrame(boolean show) {
		mainFrame.setVisible(show);
	}
	
	/**
	 * Shows the load process page
	 */
	public void showLoadProcessPage() {
		mainFrame.setTitle(null);
		mainFrame.showPage(loadProcessPage.getClass().getCanonicalName());
	}
	
	/**
	 * Shows the main page
	 */
	public void showMainPage() {
		mainFrame.showPage(mainPage.getClass().getCanonicalName());
	}
	
	/**
	 * Shows the waiting page
	 */
	public void showWaitingPage() {
		mainFrame.showPage(waitingPage.getClass().getCanonicalName());
		waitingPage.start("Importing log...");
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
	public MainPage getMainPage() {
		return mainPage;
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
	public LogController logController() {
		return logsController;
	}
	
	/**
	 * This method returns the model controller
	 * 
	 * @return the model controller
	 */
	public ProcessController processController() {
		return modelController;
	}
	
	/**
	 * This method returns the console controller
	 * 
	 * @return the console controller
	 */
	public ConsoleController consoleController() {
		return consoleController;
	}
}
