package org.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * Creates an ALU gui element
 * 
 * @author Dominik Muth
 * 
 */
public class AluE extends Element {

	private static final long serialVersionUID = -5608002614667432477L;

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		int width = this.getWidth();
		int height = this.getHeight();
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, width, height);

		int[] xPoints = { -1, height, width - height, width + 1, 2 * height,
				width / 2, width - 2 * height };
		int[] yPoints = { height, -1, -1, height, height, width / 2 - height,
				height };

		g.setColor(borderColor);
		g.fillPolygon(new Polygon(xPoints, yPoints, 7));

		xPoints[0] = 2;
		xPoints[1]--;
		xPoints[2]++;
		xPoints[3] = width - 3;

		yPoints[0] -= 2;
		yPoints[1] += 3;
		yPoints[2] += 3;
		yPoints[3] -= 2;
		yPoints[4] -= 2;
		yPoints[5] -= 2;
		yPoints[6] -= 2;

		g.setColor(currentBackground);
		g.fillPolygon(new Polygon(xPoints, yPoints, 7));

		g.setColor(borderColor);
		g.drawString(text, (width - (text.length() * 7)) / 2,
				(width / 2 - height) / 2 + 5);
	}
}
