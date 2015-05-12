package org.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

/**
 * Provides a JLabel containing a dotted line
 * 
 * @author Dominik Muth
 * 
 */
public class DottedLine extends Element {

	private static final long serialVersionUID = 8175473157544478918L;
	boolean left, top, right, bottom;

	/**
	 * 
	 * @param left
	 *            whether the left boarder of the box should be dotted or not
	 * @param top
	 *            whether the top boarder of the box should be dotted or not
	 * @param right
	 *            whether the right boarder of the box should be dotted or not
	 * @param bottom
	 *            whether the bottom boarder of the box should be dotted or not
	 */
	public DottedLine(boolean left, boolean top, boolean right, boolean bottom) {
		super();
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		int width = this.getWidth();
		int height = this.getHeight();
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, width, height);

		g.setColor(borderColor);
		if (left) {
			int deltaY = 0;
			while (deltaY <= height) {
				g.fillRect(0, deltaY, 2, 6);
				deltaY += 8;
			}
		}
		if (right) {
			int deltaY = 0;
			while (deltaY <= height) {
				g.fillRect(width - 2, deltaY, 2, 6);
				deltaY += 8;
			}
		}
		if (top) {
			int deltaX = 0;
			while (deltaX <= width) {
				g.fillRect(deltaX, 0, 6, 2);
				deltaX += 8;
			}
		}
		if (bottom) {
			int deltaX = 0;
			while (deltaX <= width) {
				g.fillRect(deltaX, height - 2, 6, 2);
				deltaX += 8;
			}
		}

	}

}
