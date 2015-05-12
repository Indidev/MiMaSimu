package org.components;

public class IAR {

	private Bus bus;
	private int value;

	public IAR(Bus bus) {
		this.bus = bus;
		this.value = 0;
	}

	public void read() {
		this.value = bus.getValue() & 0xFFFFF;
	}

	public void write() {
		bus.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void set(int value) {
		this.value = value;
	}

}
