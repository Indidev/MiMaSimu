package org.components;

public class X {
	private Bus bus;
	private int value;

	public X(Bus bus) {
		this.bus = bus;
	}

	public void read() {
		this.value = bus.getValue();
	}

	public int getValue() {
		return value;
	}
}
