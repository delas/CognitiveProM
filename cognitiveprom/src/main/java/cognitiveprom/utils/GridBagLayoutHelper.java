package cognitiveprom.utils;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GridBagLayoutHelper {

	public static GridBagConstraints createHorizontalConstraint(int x, int y, int insetLR, int insetTop, int insetBottom) {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(insetTop, insetLR, insetBottom, insetLR);
		c.gridx = x;
		c.gridy = y;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		return c;
	}
	
	public static GridBagConstraints createHorizontalTitleConstraint(int x, int y) {
		return createHorizontalConstraint(x, y, 5, 5, 2);
	}
	
	public static GridBagConstraints createHorizontalComponentConstraint(int x, int y) {
		return createHorizontalConstraint(x, y, 5, 0, 5);
	}
}
