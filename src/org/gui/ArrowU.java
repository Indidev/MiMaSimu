package org.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * Provides a JLabel containing an arrowhead upwards
 * 
 * @author Dominik Muth
 * 
 */
public class ArrowU extends Element {

	private static final long serialVersionUID = 6475927135811184397L;

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

		int[] xPoints = { -1, width / 2, width + 1 };
		int[] yPoints = { height, -1, height };

		g.setColor(borderColor);
		g.fillPolygon(new Polygon(xPoints, yPoints, 3));
		xPoints[1] = width - xPoints[1];
		g.fillPolygon(new Polygon(xPoints, yPoints, 3));

		g.setColor(currentBackground);
		xPoints[1] = width / 2;

		yPoints[0] = height - 2;
		yPoints[1] = 1;
		yPoints[2] = height - 2;

		xPoints[0] = 2 / ((height) / xPoints[1]) + 1;
		xPoints[2] = width - 1 - xPoints[0];
		g.fillPolygon(new Polygon(xPoints, yPoints, 3));

		xPoints[1] = width - xPoints[1];
		g.fillPolygon(new Polygon(xPoints, yPoints, 3));
	}
}
