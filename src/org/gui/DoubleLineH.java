package org.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

/**
 * Providing a double horizontal line
 * 
 * @author Dominik Muth
 * 
 */
public class DoubleLineH extends Element {

	private static final long serialVersionUID = -5621739947042669647L;

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
		g.drawRect(-1, 0, width + 2, height - 1);
		g.drawRect(-1, 1, width + 2, height - 3);
		g.setColor(new Color(0, 0, 0, 255));
	}
}
