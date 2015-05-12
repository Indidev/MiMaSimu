package org.components;

public class Y {
	private Bus bus;
	private int value;

	public Y(Bus bus) {
		this.bus = bus;
	}

	public void read() {
		this.value = bus.getValue();
	}

	public int getValue() {
		return value;
	}
}
