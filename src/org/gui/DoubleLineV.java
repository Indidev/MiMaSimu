package org.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

/**
 * Providing a double vertical line
 * 
 * @author Dominik Muth
 * 
 */
public class DoubleLineV extends Element {

	private static final long serialVersionUID = -7173720215686882272L;

	/**
	 * paint Icon is needed by the interface Icon
	 * 
	 * @param c
	 *            component
	 * @param g
	 *            graphic
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		int width = this.getWidth();
		int height = this.getHeight();
		g.setColor(currentBackground);
		g.fillRect(0, 0, width, height);
		g.setColor(borderColor);
		g.drawRect(0, -1, width - 1, height + 2);
		g.drawRect(1, -1, width - 3, height + 2);
		g.setColor(new Color(0, 0, 0, 255));
	}
}
