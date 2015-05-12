package org.components;

import java.util.HashMap;

import org.Controller;
import org.external.Memory;

public class SW {

	private ACC akku;
	private IAR iar;
	private One one;
	private IR ir;
	private Z z;
	private X x;
	private Y y;
	private ALU alu;
	private SDR sdr;
	private SAR sar;
	private int[] mikroInstruction;
	private int next;
	private Memory memory;
	private Controller controller;
	private HashMap<Integer, Integer> jumpAddress;

	public SW(ACC akku, IAR iar, One one, IR ir, Z z, X x, Y y, ALU alu,
			SDR sdr, SAR sar, Memory memory, Controller controller) {
		this.akku = akku;
		this.iar = iar;
		this.one = one;
		this.ir = ir;
		this.z = z;
		this.x = x;
		this.y = y;
		this.alu = alu;
		this.sdr = sdr;
		this.sar = sar;
		this.memory = memory;
		this.controller = controller;

		initMikrobefehle();
		initJumpAddresses();
		next = 0;
	}

	public void clock() {
		interpret();
		controller.clock(true);
	}

	public void clockOff() {
		controller.clock(false);
	}

	private void interpret() {
		int command = mikroInstruction[next];
		int adr = command & 0xFF;
		boolean Ar;
		boolean Aw;
		boolean RX;
		boolean RY;
		boolean RZ;
		boolean E;
		boolean Pr;
		boolean Pw;
		boolean Ir;
		boolean Iw;
		boolean Dr;
		boolean Dw;
		boolean S;
		int c;
		boolean R;
		boolean W;
		boolean JN;
		boolean D;

		command >>= 8;
		D = (command & 1) == 1;
		command >>= 1;
		JN = (command & 1) == 1;
		command >>= 0x1;
		W = (command & 1) == 1;
		command >>= 0x1;
		R = (command & 1) == 1;
		command >>= 0x1;
		c = (command & 7);
		command >>= 0x3;
		S = (command & 1) == 1;
		command >>= 0x1;
		Dw = (command & 1) == 1;
		command >>= 0x1;
		Dr = (command & 1) == 1;
		command >>= 0x1;
		Iw = (command & 1) == 1;
		command >>= 0x1;
		Ir = (command & 1) == 1;
		command >>= 0x1;
		Pw = (command & 1) == 1;
		command >>= 0x1;
		Pr = (command & 1) == 1;
		command >>= 0x1;
		E = (command & 1) == 1;
		command >>= 0x1;
		RZ = (command & 1) == 1;
		command >>= 0x1;
		RY = (command & 1) == 1;
		command >>= 0x1;
		RX = (command & 1) == 1;
		command >>= 0x1;
		Aw = (command & 1) == 1;
		command >>= 0x1;
		Ar = (command & 1) == 1;

		if (!JN) {

			// write actions
			if (Aw)
				akku.write();
			if (RZ)
				z.write();
			if (E)
				one.write();
			if (Pw)
				iar.write();
			if (Iw)
				ir.write();
			if (Dw)
				sdr.write();

			// read actions
			if (Ar)
				akku.read();
			if (RX)
				x.read();
			if (RY)
				y.read();
			if (Pr)
				iar.read();
			if (Ir)
				ir.read();
			if (Dr)
				sdr.read();
			if (S)
				sar.read();

			// set alu
			alu.calc(c);

			// external functions
			if (R)
				memory.read();
			if (W)
				memory.write();

		} else {
			if (akku.getN() == 0) {
				adr = 0x0;
			}
		}

		next = adr;

		if (D) {
			next = decode();
		}

		controller.setStatus(Ar, Aw, RX, RY, RZ, E, Pr, Pw, Ir, Iw, Dr, Dw, S,
				R, W, c);

		if (next == 0x5A) {
			controller.pause(true);
		}
	}

	private int decode() {
		int adr = next + 1;
		if (jumpAddress.containsKey(ir.getOp())) {
			adr = jumpAddress.get(ir.getOp());
		}

		return adr;
	}

	private void initJumpAddresses() {
		this.jumpAddress = new HashMap<Integer, Integer>();
		jumpAddress.put(0x0, 0x0A); // LDC a
		jumpAddress.put(0x1, 0x10); // LDV <a>
		jumpAddress.put(0x2, 0x1A); // STV <a>
		jumpAddress.put(0x3, 0x20); // ADD <a>
		jumpAddress.put(0x4, 0x2A); // AND <a>
		jumpAddress.put(0x5, 0x30); // OR <a>
		jumpAddress.put(0x6, 0x3A); // XOR <a>
		jumpAddress.put(0x7, 0x40); // EQL <a>
		jumpAddress.put(0x8, 0x4A); // JMP a
		jumpAddress.put(0x9, 0x50); // JMPN a
		jumpAddress.put(0xA, 0x70); // LDIV <<a>> --> Akku
		jumpAddress.put(0xB, 0x7A); // STIV Akku --> <<a>>
		jumpAddress.put(0xC, 0x8A); // JMS
		jumpAddress.put(0xD, 0x90); // JIND
		jumpAddress.put(0xF0, 0x5A); // HALT
		jumpAddress.put(0xF1, 0x60); // NOT Akku
		jumpAddress.put(0xF2, 0x6A); // RAR Akku
	}

	private void initMikrobefehle() {
		// create 8 bit int array
		mikroInstruction = new int[0xFF];

		// Fetch phase
		mikroInstruction[0x00] = 0x2108801; // IAR -> SAR, X, R = 1
		mikroInstruction[0x01] = 0x1400802; // Eins -> Y, R = 1
		mikroInstruction[0x02] = 0x0001803; // Alu auf add, R = 1
		mikroInstruction[0x03] = 0x0A00004; // Z -> IAR
		mikroInstruction[0x04] = 0x0090005; // SDR -> IR

		// Decode phase
		mikroInstruction[0x05] = 0x0000100; // Decode

		// LDC c c -> Akku
		mikroInstruction[0x0A] = 0x8040000; // IR ->Akku

		// LDV a <a> -> Akku
		mikroInstruction[0x10] = 0x0048811; // IR -> SAR, R = 1
		mikroInstruction[0x11] = 0x0000812; // R = 1
		mikroInstruction[0x12] = 0x0000813; // R = 1
		mikroInstruction[0x13] = 0x8010000; // SDR -> Akku

		// STV a Akku -> <a>
		mikroInstruction[0x1A] = 0x402001B; // Akku -> SDR
		mikroInstruction[0x1B] = 0x004841C; // IR -> SAR, W = 1
		mikroInstruction[0x1C] = 0x000041D; // W = 1
		mikroInstruction[0x1D] = 0x0000400; // W = 1

		// ADD a Akku + <a> -> Akku
		mikroInstruction[0x20] = 0x0048821; // IR -> SAR, W = 1
		mikroInstruction[0x21] = 0x6000822; // Akku -> X, W = 1
		mikroInstruction[0x22] = 0x0000823; // W = 1
		mikroInstruction[0x23] = 0x1010024; // SDR -> Y
		mikroInstruction[0x24] = 0x0001025; // Alu auf add
		mikroInstruction[0x25] = 0x8800000; // Z -> Akku

		// And a Akku & <a> -> Akku
		mikroInstruction[0x2A] = 0x004882B; // IR -> SAR, W = 1
		mikroInstruction[0x2B] = 0x600082C; // Akku -> X, W = 1
		mikroInstruction[0x2C] = 0x000082D; // W = 1
		mikroInstruction[0x2D] = 0x101002E; // SDR -> Y
		mikroInstruction[0x2E] = 0x000302F; // Alu auf and
		mikroInstruction[0x2F] = 0x8800000; // Z -> Akku

		// Or a Akku | <a> -> Akku
		mikroInstruction[0x30] = 0x0048831; // IR -> SAR, W = 1
		mikroInstruction[0x31] = 0x6000832; // Akku -> X, W = 1
		mikroInstruction[0x32] = 0x0000833; // W = 1
		mikroInstruction[0x33] = 0x1010034; // SDR -> Y
		mikroInstruction[0x34] = 0x0004035; // Alu auf or
		mikroInstruction[0x35] = 0x8800000; // Z -> Akku

		// Xor a Akku | <a> -> Akku
		mikroInstruction[0x3A] = 0x004883B; // IR -> SAR, W = 1
		mikroInstruction[0x3B] = 0x600083C; // Akku -> X, W = 1
		mikroInstruction[0x3C] = 0x000083D; // W = 1
		mikroInstruction[0x3D] = 0x101003E; // SDR -> Y
		mikroInstruction[0x3E] = 0x000503F; // Alu auf xor
		mikroInstruction[0x3F] = 0x8800000; // Z -> Akku

		// Eql a Akku | <a> -> Akku
		mikroInstruction[0x40] = 0x0048841; // IR -> SAR, W = 1
		mikroInstruction[0x41] = 0x6000842; // Akku -> X, W = 1
		mikroInstruction[0x42] = 0x0000843; // W = 1
		mikroInstruction[0x43] = 0x1010044; // SDR -> Y
		mikroInstruction[0x44] = 0x0007045; // Alu auf eql
		mikroInstruction[0x45] = 0x8800000; // Z -> Akku

		// JMP a a -> IAR
		mikroInstruction[0x4A] = 0x0240000; // IR -> IAR

		// JMPN
		mikroInstruction[0x50] = 0x0000251; // IF Akku < 0 go on with micop 0x51
		mikroInstruction[0x51] = 0x0240000; // IR -> IAR

		// HALT
		mikroInstruction[0x5A] = 0x000005A; // Stop MIMA

		// NOT
		mikroInstruction[0x60] = 0x6000061; // Akku -> X
		mikroInstruction[0x61] = 0x0006062; // Alu auf neg
		mikroInstruction[0x62] = 0x8800000; // Z -> Akku

		// RAR
		mikroInstruction[0x6A] = 0x600006B; // Akku -> X
		mikroInstruction[0x6B] = 0x000206C; // Alu auf rar
		mikroInstruction[0x6C] = 0x8800000; // Z -> Akku

		// LDIV
		mikroInstruction[0x70] = 0x0048871; // IR -> SAR, R = 1
		mikroInstruction[0x71] = 0x0000872; // R = 1
		mikroInstruction[0x72] = 0x0000873; // R = 1
		mikroInstruction[0x73] = 0x0018874; // SDR -> SAR, R = 1
		mikroInstruction[0x74] = 0x0000875; // R = 1
		mikroInstruction[0x75] = 0x0000876; // R = 1
		mikroInstruction[0x76] = 0x8010000; // SDR -> Akku

		// STIV
		mikroInstruction[0x7A] = 0x004887B; // IR -> SAR, R = 1
		mikroInstruction[0x7B] = 0x000087C; // R = 1
		mikroInstruction[0x7C] = 0x000087D; // R = 1
		mikroInstruction[0x7D] = 0x001887E; // SDR -> SAR
		mikroInstruction[0x7E] = 0x402047F; // Akku -> SDR, R = 1
		mikroInstruction[0x7F] = 0x0000480; // R = 1
		mikroInstruction[0x80] = 0x0000400; // R = 1

		// JMS
		mikroInstruction[0x8A] = 0x012008B; // IAR -> SDR
		mikroInstruction[0x8B] = 0x204848C; // IR -> SAR, X, W=1
		mikroInstruction[0x8C] = 0x140048D; // Eins -> Y, W = 1
		mikroInstruction[0x8D] = 0x000148E; // Alu auf add, W = 1
		mikroInstruction[0x8E] = 0x0A00000; // Z -> IAR

		// JIND
		mikroInstruction[0x90] = 0x0048891; // IR -> SAR, R = 1
		mikroInstruction[0x91] = 0x0000892; // R = 1
		mikroInstruction[0x92] = 0x0000893; // R = 1
		mikroInstruction[0x93] = 0x0210000; // SDR -> IAR
	}
}
