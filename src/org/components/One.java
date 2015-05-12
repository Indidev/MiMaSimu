package org.components;

public class One {
	private Bus bus;

	public One(Bus bus) {
		this.bus = bus;
	}

	public void write() {
		bus.setValue(1);
	}
}
