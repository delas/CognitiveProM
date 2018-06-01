package cognitiveprom.view.panels;

import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import cognitiveprom.controllers.ApplicationController;

public class MainWindow extends JPanel {

	private static final long serialVersionUID = -8854311251130985383L;
	
	private LoadProcessPanel loadPanel;
	
	public MainWindow(ApplicationController controller) {
		setLayout(new CardLayout());
		
		// load part
		loadPanel = new LoadProcessPanel();
		loadPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.loadFile();
			}
		});
		add(loadPanel);
		
	}
}
