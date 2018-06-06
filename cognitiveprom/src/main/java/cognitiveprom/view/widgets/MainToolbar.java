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

//	private JButton newProcess = new JButton("New Session", ToolbarIcons.ICON_NEW);
	private JButton openProcess = new JButton("Open a new log", ImageIcons.ICON_OPEN);
	private JButton saveProcess = new JButton("Export log", ImageIcons.ICON_SAVE);
	private JButton saveFigure = new JButton("Export figure", ImageIcons.ICON_EXPORT_PIC);
//	private JButton generateLog = new JButton("Generate Log", ToolbarIcons.ICON_LOG);
//	private JButton generateStream = new JButton("Stream", ToolbarIcons.ICON_STREAM);
	private JToggleButton showConsole = new JToggleButton("", ImageIcons.ICON_CONSOLE);
	
	public MainToolbar() {
		setFloatable(false);
		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
//		add(newProcess);
		add(openProcess);
		add(saveProcess);
		add(saveFigure);
		add(Box.createHorizontalGlue());
//		add(generateLog);
//		add(generateStream);
		add(Box.createHorizontalStrut(20));
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
	 * Method to register the button listeners
	 */
	private void registerListeners() {
		openProcess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ApplicationController.instance().showLoadProcessPage();
				ApplicationController.instance().log().loadFile();
			}
		});
		
		saveProcess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ApplicationController.instance().log().saveFile();
			}
		});
		
		saveFigure.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ApplicationController.instance().getMainPage().getProcessVisualizer().getGraphVisualizer().exportView();
			}
		});
		
		showConsole.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ApplicationController.instance().console().setConsoleVisibility(showConsole.isSelected());
			}
		});
	}
}
