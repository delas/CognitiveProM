package cognitiveprom.view.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import cognitiveprom.controllers.ApplicationController;
import cognitiveprom.view.collections.ImageIcons;

/**
 * This widget contains the main toolbar of the application
 * 
 * @author Andrea Burattin
 */
public class MainToolbar extends JToolBar {

	private static final long serialVersionUID = -2290088626676975817L;

	private JButton openProcess = new JButton("Open a new log", ImageIcons.ICON_OPEN);
	private JButton saveProcess = new JButton("Export log", ImageIcons.ICON_SAVE);
	private JButton saveFigure = new JButton("Export figure", ImageIcons.ICON_EXPORT_PIC);
	private JToggleButton showAdvancedConfiguration = new JToggleButton("Advanced configuration", ImageIcons.ICON_ADVANCED_CONFIG);
	private JToggleButton showTraces = new JToggleButton("View traces", ImageIcons.ICON_TRACES);
	private JToggleButton showConsole = new JToggleButton("", ImageIcons.ICON_CONSOLE);
	
	public MainToolbar() {
		setFloatable(false);
		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		add(openProcess);
		add(saveProcess);
		add(saveFigure);
		add(Box.createHorizontalGlue());
		add(showAdvancedConfiguration);
		add(showTraces);
		add(Box.createHorizontalGlue());
		add(showConsole);
		
		registerListeners();
	}
	
	/**
	 * Method to set the selection of the "show console" button
	 * 
	 * @param visible
	 */
	public void setShowConsoleSelected(boolean visible) {
		showConsole.setSelected(visible);
	}
	
	/**
	 * Method to set the selection of the "show advanced configuration" button
	 * 
	 * @param visible
	 */
	public void setShowAdvancedConfigurationSelected(boolean visible) {
		showAdvancedConfiguration.setSelected(visible);
	}
	
	/**
	 * Method to set the selection of the "show advanced configuration" button
	 * 
	 * @param visible
	 */
	public void setShowTracesSelected(boolean visible) {
		showTraces.setSelected(visible);
	}
	
	/**
	 * Method to register the button listeners
	 */
	private void registerListeners() {
		openProcess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ApplicationController.instance().showLoadProcessPage();
				ApplicationController.instance().logController().loadFile();
			}
		});
		
		saveProcess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ApplicationController.instance().logController().saveFile();
			}
		});
		
		saveFigure.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ApplicationController.instance().getMainPage().getProcessVisualizer().getGraphVisualizer().exportView();
			}
		});
		
		showAdvancedConfiguration.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ApplicationController.instance().processController().setAdvancedConfigurationVisibility(showAdvancedConfiguration.isSelected());
			}
		});
		
		showTraces.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ApplicationController.instance().processController().setTracesVisibility(showTraces.isSelected());
			}
		});
		
		showConsole.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ApplicationController.instance().consoleController().setConsoleVisibility(showConsole.isSelected());
			}
		});
	}
}
