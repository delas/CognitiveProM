package cognitiveprom.view.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import cognitiveprom.view.collections.ToolbarIcons;

/**
 * This widget contains the main toolbar of the application
 * 
 * @author Andrea Burattin
 */
public class MainToolbar extends JToolBar {

	private static final long serialVersionUID = -2290088626676975817L;

	private JButton newProcess = new JButton("New Process", ToolbarIcons.ICON_NEW);
	private JButton openProcess = new JButton("Open", ToolbarIcons.ICON_OPEN);
	private JButton saveProcess = new JButton("Save As...", ToolbarIcons.ICON_SAVE);
	private JButton generateLog = new JButton("Generate Log", ToolbarIcons.ICON_LOG);
	private JButton generateStream = new JButton("Stream", ToolbarIcons.ICON_STREAM);
	private JToggleButton showConsole = new JToggleButton("", ToolbarIcons.ICON_CONSOLE);
	
	public MainToolbar() {
		setFloatable(false);
		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		add(newProcess);
		add(openProcess);
		add(saveProcess);
		add(Box.createHorizontalGlue());
		add(generateLog);
		add(generateStream);
		add(Box.createHorizontalStrut(20));
		add(showConsole);
		
		registerListeners();
		setProcessSelected(false);
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
	 * Method to set internal button statuses, depending on whether the
	 * application is showing a process or not.
	 * 
	 * @param processVisualized indicating whether the application is
	 * visualizing a process or not
	 */
	public void setProcessSelected(boolean processVisualized) {
		saveProcess.setEnabled(processVisualized);
		generateLog.setEnabled(processVisualized);
		generateStream.setEnabled(processVisualized);
	}
	
	/**
	 * Method to register the button listeners
	 */
	private void registerListeners() {
		newProcess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				ApplicationController.instance().processes().randomProcess();
			}
		});
		
		openProcess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				ApplicationController.instance().processes().openProcess();
			}
		});
		
		saveProcess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				ApplicationController.instance().processes().saveProcess();
			}
		});
		
		generateLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				ApplicationController.instance().log().generateLog();
			}
		});
		
		generateStream.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				ApplicationController.instance().log().generateStream();
			}
		});
		
		showConsole.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				ApplicationController.instance().console().setConsoleVisibility(showConsole.isSelected());
			}
		});
	}
}
