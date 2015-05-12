package org.components;

public class Z {
	private int value;
	private Bus bus;

	public Z(Bus bus) {
		this.bus = bus;
	}

	public void write() {
		bus.setValue(value);
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
