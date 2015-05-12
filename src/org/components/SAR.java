package org.components;

import org.external.Memory;

public class SAR {
	private Bus bus;
	private int value;
	private Memory speicher;

	public SAR(Bus bus, Memory speicher) {
		this.bus = bus;
		this.speicher = speicher;
		this.value = 0;
	}

	public void read() {
		value = bus.getValue();
		speicher.setAddr(value);
	}

	public int getValue() {
		return value;
	}
}
