package org.components;

public class SDR {

	private Bus bus;
	private int value;

	public SDR(Bus bus) {
		this.bus = bus;
	}

	public void read() {
		value = bus.getValue();
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
