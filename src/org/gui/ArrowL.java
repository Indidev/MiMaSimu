package org.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * Provides a JLabel containing an arrowhead to the left
 * 
 * @author Dominik Muth
 * 
 */
public class ArrowL extends Element {

	private static final long serialVersionUID = -1116163145762353961L;

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
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		int width = this.getWidth();
		int height = this.getHeight();
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, width, height);

		int[] xPoints = { width, -1, width };
		int[] yPoints = { -1, height / 2, height };

		g.setColor(borderColor);
		g.fillPolygon(new Polygon(xPoints, yPoints, 3));
		yPoints[1] = height - yPoints[1];
		g.fillPolygon(new Polygon(xPoints, yPoints, 3));

		g.setColor(currentBackground);
		xPoints[0] -= 2;
		xPoints[1] += 3;
		xPoints[2] -= 2;

		yPoints[0] = 2 / (width / yPoints[1]) + 1;
		yPoints[2] = height - 1 - yPoints[0];
		//
		// yPoints[0] = 2;
		// yPoints[1] = height - 2;
		// yPoints[2] = 2;
		//
		// xPoints[0] = 2 / ((height) / xPoints[1]) + 2;
		// xPoints[2] = width - xPoints[0];
		g.fillPolygon(new Polygon(xPoints, yPoints, 3));

		yPoints[1] = height - yPoints[1] - 1;

		g.fillPolygon(new Polygon(xPoints, yPoints, 3));
	}
}
