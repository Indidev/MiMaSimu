package org.components;

public class IR {
	private Bus bus;
	private int opCode;
	private int value;

	public IR(Bus bus) {
		this.bus = bus;
	}

	public void read() {
		value = bus.getValue();
		opCode = (value >> 16) & 0xFF;
		if ((opCode >> 4) != 0xF) {
			opCode >>= 4;
		}
	}

	public void write() {
		bus.setValue(value);
	}

	public int getOp() {
		return opCode;
	}

	public int getValue() {
		return value;
	}
}
