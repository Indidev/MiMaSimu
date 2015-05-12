package org.components;

public class ACC {
	private int value;
	private int vorzeichen;
	private Bus bus;

	public ACC(Bus bus) {
		this.bus = bus;
		this.value = 0;
		this.vorzeichen = 0;
	}

	public void read() {
		this.value = bus.getValue();

		vorzeichen = (value >> 23) & 1;
	}

	public void write() {
		bus.setValue(value);
	}

	public int getN() {
		return vorzeichen;
	}

	public int getValue() {
		return value;
	}
}
