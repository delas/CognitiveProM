package cognitiveprom.view.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.controllers.ApplicationController;

public class LoadProcessPage extends ConfigurableWindowPage {

	private static final long serialVersionUID = 8735041859396682095L;

	public LoadProcessPage(ConfigurationSet conf) {
		super(conf);
		
		// place the components of the window
		placeComponents();
		
		// listener
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ApplicationController.instance().logController().loadFile();
			}
		});
	}

	private void placeComponents() {
		// basic setup
		setBackground(Color.WHITE);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setLayout(new BorderLayout());
		
		// setting up all widgets
		JLabel label = new JLabel("<html><div style=\"text-align: center;\">"
				+ "<span style=\"font-size: 30px\">No data to display</span><br/><br/>"
				+ "<span style=\"font-size: 14px\">Click to open a XES log file or to import data from a supported file format</span>"
				+ "</div></html>");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.LIGHT_GRAY);
		label.setFont(label.getFont().deriveFont(Font.PLAIN));
		add(label, BorderLayout.CENTER);
	}
}
