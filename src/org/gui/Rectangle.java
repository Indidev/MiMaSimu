package org.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Provides a Rectangle...
 * 
 * @author Dominik Muth
 * 
 */
public class Rectangle extends Element {

	private static final long serialVersionUID = 6102509086538249529L;

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		int width = this.getWidth();
		int height = this.getHeight();
		g.setColor(currentBackground);
		g.fillRect(0, 0, width, height);
		g.setColor(borderColor);
		g.drawRect(0, 0, width - 1, height - 1);
		g.drawRect(1, 1, width - 3, height - 3);
		g.setColor(new Color(0, 0, 0, 255));
		g.setFont(new Font(Font.SANS_SERIF, 0, 9));
		g.drawString(text, (width - (text.length() * 6)) / 2, (height + 9) / 2);
	}
}
