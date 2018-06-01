package cognitiveprom.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LoadProcessPanel extends JPanel {

	private static final long serialVersionUID = 8735041859396682095L;

	public LoadProcessPanel() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		
		JLabel label = new JLabel("<html><div style=\"text-align: center;\">"
				+ "<span style=\"font-size: 30px\">No data to display</span><br/><br/>"
				+ "<span style=\"font-size: 14px\">Start by opening a XES log file or importing data from a supported file format</span>"
				+ "</div></html>");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.LIGHT_GRAY);
		label.setFont(label.getFont().deriveFont(Font.PLAIN));
		
		add(label, BorderLayout.CENTER);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
}
