package org;

import java.io.File;

import org.components.ACC;
import org.components.ALU;
import org.components.Bus;
import org.components.Clock;
import org.components.IAR;
import org.components.IR;
import org.components.One;
import org.components.SAR;
import org.components.SDR;
import org.components.SW;
import org.components.X;
import org.components.Y;
import org.components.Z;
import org.external.Memory;
import org.gui.GUI;
import org.io.Input;
import org.io.Output;
import org.io.TempMem;
import org.loadingScreen.LoadingScreen;

/**
 * Main Controller of the simulation
 * 
 * @author Dominik Muth
 * 
 */
public class Controller {
	private Bus bus;
	private ACC akku;
	private IAR iar;
	private One eins;
	private IR ir;
	private Z z;
	private X x;
	private Y y;
	private ALU alu;
	private SDR sdr;
	private SAR sar;
	private Memory speicher;
	private SW sw;
	private Clock clock;

	private GUI gui;
	private LoadingScreen ls;

	/**
	 * Creates a new Controller
	 * 
	 * @param args
	 *            arguments given by the console
	 */
	public Controller(String[] args) {
		ls = new LoadingScreen();
		ls.start();

		if ((args.length > 0)
				&& (args[0].endsWith(".mima") || args[0].endsWith(".mem"))) {
			File input = new File(args[0]);
			if (input.exists()) {
				loadMem(input);
			} else {
				System.err.println("File doesn't exist!");
			}
		}

		gui = new GUI(this);
		initMima();
		ls.stop();
		gui.setVisible(true);

		clock = new Clock(500, sw);
		clock.pause(true);
		new Thread(clock).start();
	}

	private void initMima() {
		bus = new Bus();
		akku = new ACC(bus);
		iar = new IAR(bus);
		eins = new One(bus);
		ir = new IR(bus);
		x = new X(bus);
		y = new Y(bus);
		z = new Z(bus);
		alu = new ALU(x, y, z);
		sdr = new SDR(bus);
		speicher = new Memory(sdr);
		sar = new SAR(bus, speicher);
		sw = new SW(akku, iar, eins, ir, z, x, y, alu, sdr, sar, speicher, this);

		iar.set(speicher.getStartPoint());

		gui.setStatus(false, false, false, false, false, false, false, false,
				false, false, false, false, false, false, false, 0);
		gui.setValues(0, 0, 0, 0, 0, 0, 0, 0, 0, speicher.getMemory());
	}

	/**
	 * set status of the elements
	 * 
	 * @param Ar
	 * @param Aw
	 * @param RX
	 * @param RY
	 * @param RZ
	 * @param E
	 * @param Pr
	 * @param Pw
	 * @param Ir
	 * @param Iw
	 * @param Dr
	 * @param Dw
	 * @param S
	 * @param R
	 * @param W
	 * @param c
	 */
	public void setStatus(boolean Ar, boolean Aw, boolean RX, boolean RY,
			boolean RZ, boolean E, boolean Pr, boolean Pw, boolean Ir,
			boolean Iw, boolean Dr, boolean Dw, boolean S, boolean R,
			boolean W, int c) {

		gui.setValues(akku.getValue(), c, iar.getValue(), ir.getValue(),
				sar.getValue(), sdr.getValue(), x.getValue(), y.getValue(),
				z.getValue(), speicher.getMemory());
		gui.setStatus(Ar, Aw, RX, RY, RZ, E, Pr, Pw, Ir, Iw, Dr, Dw, S, R, W, c);
	}

	/**
	 * toggle clock state
	 * 
	 * @param active
	 *            whether the clock is active or not
	 */
	public void clock(boolean active) {
		gui.clock(active);
	}

	/**
	 * toggle clock manually
	 */
	public void manualClock() {
		sw.clock();
		clock(false);
	}

	/**
	 * toggle pause
	 * 
	 * @param state
	 *            whether the simulation is paused or not
	 */
	public void pause(boolean state) {
		clock.pause(state);
		gui.pause(state);
	}

	/**
	 * reset the simulation
	 */
	public void reset() {
		clock.pause(true);
		gui.deleteValues(speicher.getMemory());
		initMima();
		clock.setSW(sw);

	}

	/**
	 * change timeout and therefor frequency of the clock
	 * 
	 * @param timeout
	 *            time in ms
	 */
	public void setTimeout(int timeout) {
		clock.setTimeout(timeout);
	}

	/**
	 * loads a memory file
	 * 
	 * @param filepath
	 *            path of the memory file
	 */
	public void loadMem(String filepath) {
		loadMem(new File(filepath));
	}

	/**
	 * loads a memory file
	 * 
	 * @param file
	 *            memory file
	 */
	public void loadMem(File file) {
		TempMem.setText(Input.loadFile(file));
	}

	/**
	 * save memory
	 * 
	 * @param filepath
	 *            file path to save memory to
	 */
	public void saveMem(String filepath) {
		saveMem(new File(filepath));
	}

	/**
	 * save memory
	 * 
	 * @param file
	 *            file to save memory to
	 */
	public void saveMem(File file) {
		Output.saveFile(file, TempMem.getText());
	}

	/**
	 * save result memory to file
	 * 
	 * @param filepath
	 *            file path to save result to
	 */
	public void saveResult(String filepath) {
		saveResult(new File(filepath));
	}

	/**
	 * save result memory to file
	 * 
	 * @param file
	 *            file to save result to
	 */
	public void saveResult(File file) {
		Output.saveFile(file, speicher.getState());
	}

}
