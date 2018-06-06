package cognitiveprom.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.utils.SetUtils;

public class WaitingPanel extends ConfigurablePanel {
	
	/**
	 * Some waiting sentences (from Slickerbox) :)
	 */
	public static String[] waitingSentences = new String[] {
		"Patience is a virtue...",
		"Hold tight...",
		"Waiting is the hardest part...",
		"Can't be much longer...",
		"Just a little longer...",
		"Hold on there...",
		"Almost there...",
		"Just a second...",
		"A little patience..."
	};

	private static final long serialVersionUID = 8735041859396682095L;
	private JLabel label;

	public WaitingPanel(ConfigurationSet conf) {
		super(conf);
		
		// place the components of the window
		placeComponents();
	}
	
	public void start(String firstLine) {
		showNiceText(firstLine, SetUtils.getRandom(waitingSentences));
		repaint();
	}
	
	
	public void setTranslucent(boolean translucent) {
		if (translucent) {
			setBackground(new Color(255, 255, 255, 190));
		} else {
			setBackground(new Color(255, 255, 255, 255));
		}
	}

	private void placeComponents() {
		// basic setup
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		
		// setting up all widgets
		label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.LIGHT_GRAY);
		label.setFont(label.getFont().deriveFont(Font.PLAIN));
		add(label, BorderLayout.CENTER);
	}
	
	private void showNiceText(String firstLine, String text) {
		label.setText("<html><div style=\"text-align: center;\">"
				+ "<span style=\"font-size: 30px\">" + firstLine + "</span><br/><br/>"
				+ "<span style=\"font-size: 14px\">" + text + "</span>"
				+ "</div></html>");
	}
}
