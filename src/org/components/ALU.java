package org.components;

public class ALU {
	private X x;
	private Y y;
	private Z z;

	public ALU(X x, Y y, Z z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void calc(int c) {
		switch (c) {
		case 0: // Do Nothing
			break;

		case 1:
			add();
			break;

		case 2:
			rot();
			break;

		case 3:
			and();
			break;

		case 4:
			or();
			break;

		case 5:
			xor();
			break;

		case 6:
			not();
			break;

		case 7:
			equ();
			break;
		}
	}

	private void equ() {

		if (x.getValue() == y.getValue()) {
			z.setValue(~0);
		} else {
			z.setValue(0);
		}
	}

	private void not() {
		z.setValue(~x.getValue());
	}

	private void xor() {
		z.setValue(x.getValue() ^ y.getValue());
	}

	private void or() {
		z.setValue(x.getValue() | y.getValue());
	}

	private void and() {
		z.setValue(x.getValue() & y.getValue());
	}

	private void rot() {
		int bit = x.getValue() & 1;
		bit <<= 23;
		z.setValue(((x.getValue() >> 1) & 0x7FFFFF) | bit);
	}

	private void add() {
		z.setValue(x.getValue() + y.getValue());
	}
}
