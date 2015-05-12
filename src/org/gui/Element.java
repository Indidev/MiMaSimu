package org.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * Abstract Parent class for gui elements
 * 
 * @author indidev
 * 
 */
public abstract class Element extends JLabel implements Icon {

	private static final long serialVersionUID = -6837237899588421140L;

	/* attributes */
	protected Color bgColor;
	protected Color currentBackground;
	protected Color borderColor;
	protected Color activeColor;
	protected String text;
	protected boolean active;

	/**
	 * initiate the element
	 */
	public Element() {
		bgColor = new Color(0, 0, 0, 0);
		borderColor = new Color(0, 0, 0, 0);
		this.setIcon(this);
		active = false;
	}

	/**
	 * change the background color
	 * 
	 * @param bgcolor
	 *            should be self explaining
	 */
	public void setBackgroundColor(Color bgcolor) {
		this.bgColor = bgcolor;
		this.currentBackground = bgcolor;
		this.repaint();
	}

	/**
	 * change border color
	 * 
	 * @param borderColor
	 *            self explaining
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		this.repaint();
	}

	/**
	 * color the item should take if it is triggered to active
	 * 
	 * @param activeColor
	 *            the active color
	 */
	public void setActiveColor(Color activeColor) {
		this.activeColor = activeColor;
		if (active)
			currentBackground = activeColor;
		this.repaint();
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return this.text;
	}

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
	public abstract void paintIcon(Component c, Graphics g, int x, int y);

	@Override
	public int getIconHeight() {
		return this.getHeight();
	}

	@Override
	public int getIconWidth() {
		return this.getWidth();
	}

	/**
	 * toggle active status
	 * 
	 * @param state
	 *            whether the component should be active or not
	 */
	public void setActive(boolean state) {
		this.active = state;
		if (state)
			this.currentBackground = activeColor;
		else
			this.currentBackground = bgColor;

		this.repaint();
	}

	/**
	 * 
	 * @return whether the component is active at the moment
	 */
	public boolean isActive() {
		return active;
	}
}
