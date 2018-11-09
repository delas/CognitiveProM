package cognitiveprom.view.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.controllers.ApplicationController;
import cognitiveprom.view.collections.ImageIcons;

/**
 * 
 * @author Andrea Burattin
 */
public class AbstractionSlider extends ConfigurablePanel {

	private static final long serialVersionUID = -235029661799945095L;
	private static DecimalFormat df = new DecimalFormat("#.###");

	private JSlider abstractionSlider;
	private JLabel valueLabel;
	
	/**
	 * Basic class constructor
	 * 
	 * @param conf
	 */
	public AbstractionSlider(ConfigurationSet conf) {
		super(conf);
		
		placeComponents();
	}

	private void placeComponents() {
		setBackground(Color.white);
		setLayout(new BorderLayout());
		
		// set the slider
		abstractionSlider = new JSlider(JSlider.VERTICAL, 0, 100, 30);
		abstractionSlider.setBackground(Color.white);
		abstractionSlider.setMajorTickSpacing(25);
		abstractionSlider.setMinorTickSpacing(5);
		abstractionSlider.setPaintTicks(true);
		abstractionSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				ApplicationController.instance().processController().updateVisualization();
			}
		});
		abstractionSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				valueLabel.setText(df.format(getAbstractionValue()));
			}
		});
		
		JLabel helpLabel = new JLabel(ImageIcons.ICON_MAGIC);
		helpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		helpLabel.setToolTipText("<html>Use this slider to adjust the abstraction of the model. Consider that:<br/>"
				+ " - values close to 1 mean <b>maximum abstraction</b>,<br/>"
				+ " - values close to 0 indicate <b>minimum abstraction</b>.</html>");
		
		valueLabel = new JLabel(df.format(getAbstractionValue()));
		valueLabel.setHorizontalAlignment(JLabel.CENTER);
		
		add(helpLabel, BorderLayout.NORTH);
		add(abstractionSlider, BorderLayout.CENTER);
		add(valueLabel, BorderLayout.SOUTH);
	}

	public double getAbstractionValue() {
		return (double) abstractionSlider.getValue() / (double) abstractionSlider.getMaximum();
	}
}
