package org.external;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import org.components.SDR;
import org.io.TempMem;

/**
 * The Memory stores the current programm and data code for the minimal machine.
 * 
 * @author Dominik Muth
 * 
 */
public class Memory {

	private SDR sdr;
	private int addr;
	private HashMap<Integer, Integer> memory;
	private int counter;
	private int outputAdr;
	private int startPoint;

	/**
	 * Constructs a new Memory connected to a sdr
	 * 
	 * @param sdr
	 *            memory data register the memory is connected to (German:
	 *            Speicher Daten Register)
	 */
	public Memory(SDR sdr) {
		this.sdr = sdr;
		this.addr = 0;
		this.memory = new HashMap<Integer, Integer>();
		this.counter = 0;
		this.outputAdr = 0x4242;

		initMemory();
	}

	/**
	 * Sets the address bus connected to the memory to a specific value.
	 * 
	 * @param addr
	 *            value of the address bus
	 */
	public void setAddr(int addr) {
		this.addr = addr & 0xFFFFF;
		this.counter = 0;
	}

	/**
	 * Read data from memory to the memory data register. <br>
	 * Needs 3 calls to set the sdr to the value at the selected memory adress
	 */
	public void read() {
		counter++;
		if (counter == 3) {
			if (memory.containsKey(addr))
				sdr.setValue(memory.get(addr));
			else
				sdr.setValue(0);
			counter = 0;
		}
	}

	/**
	 * Writes data from sdr to the memory. <br>
	 * Needs 3 calls to write data. <br>
	 * Writes into the Adress given by setAddr(int)
	 */
	public void write() {
		counter++;
		if (counter == 3) {
			memory.put(addr, sdr.getValue());
			counter = 0;
			if (addr == outputAdr)
				System.out.println(memory.get(outputAdr));
		}
	}

	private void initMemory() {
		startPoint = 0x00000;
		LinkedList<String[]> lines = TempMem.getValueList();
		int curAddr = 0;

		for (String[] line : lines) {

			if ((line.length == 2) && line[0].toLowerCase().equals("start")
					&& isNumber(line[1])) {
				startPoint = getNum(line[1]);
			}
			if ((line.length == 3) && line[2].toLowerCase().endsWith("start")
					&& isNumber(line[0])) {
				startPoint = getNum(line[0]);
			}
			if ((line.length > 0) && !line[0].startsWith("//")) {
				switch (line.length) {
				case 1:
					if (isNumber(line[0]))
						memory.put(curAddr, getNum(line[0]));
					break;
				default:
					if (isNumber(line[1]) && isNumber(line[0])) {
						curAddr = getNum(line[0]);
						memory.put(curAddr, getNum(line[1]));
					} else if (isNumber(line[0])) {
						memory.put(curAddr, getNum(line[0]));
					}
				}
				curAddr++;
			}

		}
	}

	private boolean isNumber(String input) {
		return input.matches("(0b([01])*)|(0x([0-9A-Fa-f])*)|[0-9]*|-[0-9]*");
	}

	private int getNum(String input) {
		int number = 0;
		int base = 10;
		boolean neg = false;
		if (isNumber(input)) {

			if (input.startsWith("0x")) {
				base = 16;
				input = input.substring(2);
				input = input.toUpperCase();
			} else if (input.startsWith("0b")) {
				base = 2;
				input = input.substring(2);
			} else if (input.startsWith("-")) {
				input = input.substring(1);
				neg = true;
			}

			while (input.length() > 0) {
				if (input.charAt(0) <= 57)
					number += (input.charAt(0) - 48)
							* Math.pow(base, input.length() - 1);
				else
					number += (input.charAt(0) - 55)
							* Math.pow(base, input.length() - 1);

				input = input.substring(1);
			}

			if (number >= Math.pow(2, 13)) {
				number = -((~number) + 1);
			}
		}

		if (neg) {
			number = -number;
		}
		return number;
	}

	/**
	 * get the whole Memory... well, actually not the whole, just the parts
	 * which where already used.
	 * 
	 * @return mapping of addresses to values
	 */
	public HashMap<Integer, Integer> getMemory() {
		return new HashMap<Integer, Integer>(this.memory);
	}

	/**
	 * @return the address the program should start from
	 */
	public int getStartPoint() {
		return startPoint;
	}

	/**
	 * 
	 * @return memory content as a string, addresses + values. <br>
	 *         Each address is separated by a line break.
	 */
	public String getState() {
		Integer keys[] = new Integer[memory.size()];
		memory.keySet().toArray(keys);

		String state = "";
		Arrays.sort(keys);

		for (Integer addr : keys) {
			state += toHex(addr, 5) + " " + toHex(memory.get(addr), 6) + " //"
					+ memory.get(addr) + "\n";
		}

		return state;
	}

	/**
	 * resets the memory
	 */
	public void reload() {
		initMemory();
	}

	private String toHex(int value, int length) {
		String hex = Integer.toHexString(value);

		while (hex.length() > length) {
			hex = hex.substring(1);
		}
		while (hex.length() < length) {
			hex = "0" + hex;
		}

		hex = hex.toUpperCase();
		hex = "0x" + hex;
		return hex;
	}
}
