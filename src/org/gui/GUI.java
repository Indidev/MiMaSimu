package org.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import org.Controller;

/**
 * MiMa front-end
 * 
 * @author Dominik Muth
 * 
 */
public class GUI extends JFrame {

	private static final long serialVersionUID = -4470963446194084878L;

	private Controller controller;
	private JPanel content;
	private JPanel dataContent;
	private JPanel mimaContent;
	private HashMap<String, Element> elements;
	private HashMap<String, JButton> buttons;
	private HashMap<String, JLabel> elementLabels;
	private HashMap<String, JLabel> busLabels;
	private HashMap<String, JLabel> dataLabels;
	private JScrollPane scrollPanel;
	private JTable memoryTable;
	private JSlider speed;
	private JTextField searchMem;
	private boolean changed;
	private final GUI frame = this;

	private Color backgroundColor = new Color(255, 255, 255, 255);
	private Color activeColor = new Color(0, 0x90, 0xFF, 255);
	private Color borderColor = new Color(0, 0, 0, 255);

	/**
	 * creates a new front-end
	 * 
	 * @param controller
	 *            needed for communication between front- and back-end
	 */
	public GUI(Controller controller) {
		this.setTitle("MiMa Simulator - by Dominik Muth");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.controller = controller;
		changed = false;
		elements = new HashMap<String, Element>();
		buttons = new HashMap<String, JButton>();
		elementLabels = new HashMap<String, JLabel>();
		dataLabels = new HashMap<String, JLabel>();
		busLabels = new HashMap<String, JLabel>();

		addContent();

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				if (changed) {
					int option = JOptionPane
							.showConfirmDialog(
									frame,
									"You did some changes to the Memory,\n do you want to save your changes?",
									"save changes?",
									JOptionPane.YES_NO_CANCEL_OPTION);
					if (option == JOptionPane.YES_OPTION) {
						JFileChooser fileExplorer = new JFileChooser();
						fileExplorer
								.setFileSelectionMode(JFileChooser.FILES_ONLY);
						fileExplorer.setApproveButtonText("save");

						// add Filter for only accepting .ser files
						FileFilter filter = new FileFilter() {

							@Override
							public String getDescription() {
								return ".mem, .mima";
							}

							@Override
							public boolean accept(File arg0) {
								return arg0.getName().matches(".*\\.mem")
										|| arg0.getName().matches(".*\\.mima")
										|| arg0.isDirectory();
							}
						};

						fileExplorer.setFileFilter(filter);

						// check choosen file
						if (fileExplorer.showOpenDialog(null) == 0) {
							File input = fileExplorer.getSelectedFile();

							// add suffix if not .mem
							if (!input.getName().matches(".*\\.mem")
									&& !input.getName().matches(".*\\.mima")) {
								input = new File(input.getPath() + ".mem");
							}
							frame.controller.saveMem(input);
							System.exit(NORMAL);
						}

					} else if (option == JOptionPane.NO_OPTION) {
						System.exit(NORMAL);
					}

				} else
					System.exit(NORMAL);
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});

		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}

	private void addContent() {
		content = new JPanel();
		content.setVisible(true);
		content.setBackground(backgroundColor);

		addMimaContent();
		addDataContent();
		addLabels();

		this.add(content);
	}

	private void addDataContent() {
		dataContent = new JPanel();
		dataContent.setLayout(null);
		dataContent.setPreferredSize(new Dimension(500, 470));
		dataContent.setBackground(backgroundColor);
		dataContent.setVisible(true);
		content.add(dataContent);

		addButtons();
		addSpeedSlider();
		addMemoryTable();
	}

	private void addMemoryTable() {
		memoryTable = new JTable(0xFFFFF + 1, 2);
		memoryTable.setVisible(true);
		memoryTable.setPreferredScrollableViewportSize(new Dimension(300, 400));
		memoryTable.setFillsViewportHeight(true);
		memoryTable.setSelectionBackground(activeColor);
		memoryTable.getColumnModel().getColumn(0).setHeaderValue("Address");
		memoryTable.getColumnModel().getColumn(1).setHeaderValue("Hex-value");
		for (int i = 0; i <= 0xFFFFF; i++) {
			memoryTable.setValueAt(toHex(i, 5), i, 0);
			memoryTable.setValueAt(toHex(0, 6), i, 1);
		}

		scrollPanel = new JScrollPane(memoryTable);
		scrollPanel.setSize(300, 400);
		scrollPanel.setVisible(true);
		scrollPanel.setLocation(180, 60);
		dataContent.add(scrollPanel);

		searchMem = new JTextField();
		searchMem.setSize(80, 30);
		searchMem.setLocation(400, 15);
		searchMem.setVisible(true);
		searchMem.setBackground(backgroundColor);
		dataContent.add(searchMem);

		searchMem.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent arg0) {
				if (isNumber(searchMem.getText())) {
					memoryTable.scrollRectToVisible(memoryTable.getCellRect(
							getNum(searchMem.getText()), 0, false));
				}
			}
		});

	}

	private void addLabels() {

		// Element Labels
		JLabel tmpLabel = new JLabel("Akku");
		tmpLabel.setLocation(elements.get("akkuN").getX(), elements
				.get("akkuN").getY() - 15);
		elementLabels.put("lbl_akku", tmpLabel);

		tmpLabel = new JLabel("Eins");
		tmpLabel.setLocation(elements.get("eins").getX(), elements.get("eins")
				.getY() - 15);
		elementLabels.put("lbl_eins", tmpLabel);

		tmpLabel = new JLabel("SAR");
		tmpLabel.setLocation(elements.get("sar").getX(), elements.get("sar")
				.getY() - 15);
		elementLabels.put("lbl_sar", tmpLabel);

		tmpLabel = new JLabel("SDR");
		tmpLabel.setLocation(elements.get("sdr").getX(), elements.get("sdr")
				.getY() - 15);
		elementLabels.put("lbl_sdr", tmpLabel);

		tmpLabel = new JLabel("X");
		tmpLabel.setLocation(elements.get("x").getX(),
				elements.get("x").getY() - 15);
		elementLabels.put("lbl_x", tmpLabel);

		tmpLabel = new JLabel("Y");
		tmpLabel.setLocation(elements.get("y").getX(),
				elements.get("y").getY() - 15);
		elementLabels.put("lbl_y", tmpLabel);

		tmpLabel = new JLabel("Z");
		tmpLabel.setLocation(elements.get("z").getX(),
				elements.get("z").getY() - 15);
		elementLabels.put("lbl_z", tmpLabel);

		tmpLabel = new JLabel("ALU");
		tmpLabel.setLocation(elements.get("alu").getX(), elements.get("alu")
				.getY() + elements.get("alu").getHeight() / 2 - 15);
		elementLabels.put("lbl_alu", tmpLabel);

		tmpLabel = new JLabel("IR");
		tmpLabel.setLocation(elements.get("irOP").getX(), elements.get("irOP")
				.getY() - 15);
		elementLabels.put("lbl_ir", tmpLabel);

		tmpLabel = new JLabel("IAR");
		tmpLabel.setLocation(elements.get("iar").getX(), elements.get("iar")
				.getY() - 15);
		elementLabels.put("lbl_iar", tmpLabel);

		for (JLabel label : elementLabels.values()) {
			label.setSize(100, 15);
			label.setVisible(true);
			label.setBackground(new Color(0, 0, 0, 0));
			label.setFont(new Font(label.getFont().getName(), Font.BOLD, label
					.getFont().getSize()));
			this.mimaContent.add(label);
		}

		// Bus Labels

		tmpLabel = new JLabel("<html>24 <br> / </html>");
		tmpLabel.setLocation(
				elements.get("akkuBus").getX()
						+ elements.get("akkuBus").getWidth() / 2,
				elements.get("akkuBus").getY() - 20);
		busLabels.put("lbl_akkuBus", tmpLabel);

		tmpLabel = new JLabel("<html>24 <br> / </html>");
		tmpLabel.setLocation(
				elements.get("einsBus").getX()
						+ elements.get("einsBus").getWidth() / 2,
				elements.get("einsBus").getY() - 20);
		busLabels.put("lbl_einsBus", tmpLabel);

		tmpLabel = new JLabel("<html>20 <br> / </html>");
		tmpLabel.setLocation(
				elements.get("sarBus").getX()
						+ elements.get("sarBus").getWidth() / 2,
				elements.get("sarBus").getY() - 20);
		busLabels.put("lbl_sarBus", tmpLabel);

		tmpLabel = new JLabel("<html>24 <br> / </html>");
		tmpLabel.setLocation(
				elements.get("sdrBus").getX()
						+ elements.get("sdrBus").getWidth() / 2,
				elements.get("sdrBus").getY() - 20);
		busLabels.put("lbl_sdrBus", tmpLabel);

		tmpLabel = new JLabel("<html>24 <br> / </html>");
		tmpLabel.setLocation(elements.get("xBus").getX()
				+ elements.get("xBus").getWidth() / 2, elements.get("xBus")
				.getY() - 20);
		busLabels.put("lbl_xBus", tmpLabel);

		tmpLabel = new JLabel("<html> &#32;&#47; <br> 24 </html>");
		tmpLabel.setLocation(elements.get("yBus").getX()
				+ elements.get("yBus").getWidth() / 2, elements.get("yBus")
				.getY() - 5);
		busLabels.put("lbl_yBus", tmpLabel);

		tmpLabel = new JLabel("<html>24 <br> / </html>");
		tmpLabel.setLocation(elements.get("zBus").getX()
				+ elements.get("zBus").getWidth() / 2, elements.get("zBus")
				.getY() - 20);
		busLabels.put("lbl_zBus", tmpLabel);

		tmpLabel = new JLabel("<html>24 <br> / </html>");
		tmpLabel.setLocation(
				elements.get("irBus").getX() + elements.get("irBus").getWidth()
						/ 2, elements.get("irBus").getY() - 20);
		busLabels.put("lbl_irBus", tmpLabel);

		tmpLabel = new JLabel("<html>20 <br> / </html>");
		tmpLabel.setLocation(
				elements.get("iarBus").getX()
						+ elements.get("iarBus").getWidth() / 2,
				elements.get("iarBus").getY() - 20);
		busLabels.put("lbl_iarBus", tmpLabel);

		tmpLabel = new JLabel("<html> 	&mdash; 20</html>");
		tmpLabel.setLocation(elements.get("sarMem").getX() - 2,
				elements.get("sarMem").getY()
						+ elements.get("sarMem").getHeight() / 2 - 15);
		busLabels.put("lbl_sarMem", tmpLabel);

		tmpLabel = new JLabel("<html> 	&mdash; 24</html>");
		tmpLabel.setLocation(elements.get("sdrMem").getX() - 2,
				elements.get("sdrMem").getY()
						+ elements.get("sdrMem").getHeight() / 2 - 15);
		busLabels.put("lbl_sdrMem", tmpLabel);

		for (JLabel label : busLabels.values()) {
			label.setSize(40, 30);
			label.setVisible(true);
			label.setBackground(new Color(0, 0, 0, 0));
			this.mimaContent.add(label);
			mimaContent.setComponentZOrder(label, 0);
		}

		tmpLabel = new JLabel(
				"slow  <- - - - - - - - - - - - - - - - - - - - - - - -> fast");
		tmpLabel.setVerticalAlignment(JLabel.BOTTOM);
		tmpLabel.setHorizontalAlignment(JLabel.LEFT);
		tmpLabel.setSize(speed.getWidth() + 50, 30);
		tmpLabel.setLocation(speed.getX(), speed.getY() - 30);
		dataLabels.put("lbl_speed", tmpLabel);

		tmpLabel = new JLabel("<html> Search <br> Address</html>");
		tmpLabel.setVerticalAlignment(JLabel.CENTER);
		tmpLabel.setHorizontalAlignment(JLabel.RIGHT);
		tmpLabel.setSize(100, searchMem.getHeight() + 10);
		tmpLabel.setLocation(searchMem.getX() - 110, searchMem.getY());
		dataLabels.put("lbl_search", tmpLabel);

		for (JLabel label : dataLabels.values()) {
			label.setVisible(true);
			label.setBackground(new Color(0, 0, 0, 0));
			this.dataContent.add(label);
		}
	}

	private void addSpeedSlider() {
		speed = new JSlider(0, 2000, 1);
		speed.setValue((speed.getMaximum() + speed.getMinimum()) / 2);
		speed.setSize(300, 40);
		speed.setLocation(10, 15);
		speed.setVisible(true);
		speed.setBackground(backgroundColor);
		speed.setPaintTrack(true);
		speed.setFocusable(false);
		dataContent.add(speed);

		speed.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				controller.setTimeout(2000 - speed.getValue());
			}
		});
	}

	private void addButtons() {
		JButton tmp = new JButton(">");
		tmp.setLocation(35, 70);
		tmp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JButton tmp = buttons.get("start");
				if (tmp.getText().equals(">")) {
					tmp.setText("||");
					controller.pause(false);
				} else {
					tmp.setText(">");
					controller.pause(true);
				}
			}
		});
		buttons.put("start", tmp);

		tmp = new JButton("reset");
		tmp.setLocation(35, 110);
		tmp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				buttons.get("start").setText(">");
				controller.reset();
			}
		});
		buttons.put("reset", tmp);

		tmp = new JButton("step");
		tmp.setLocation(35, 150);
		tmp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.manualClock();
			}
		});
		buttons.put("step", tmp);

		tmp = new JButton("edit memory");
		tmp.setLocation(35, 190);
		tmp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new MemEditGUI(frame);
			}
		});
		buttons.put("edtMem", tmp);

		tmp = new JButton("load memory");
		tmp.setLocation(35, 230);
		tmp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileExplorer = new JFileChooser();
				fileExplorer.setFileSelectionMode(JFileChooser.FILES_ONLY);

				// add Filter for only accepting .mem and .mima files
				FileFilter filter = new FileFilter() {

					@Override
					public String getDescription() {
						return ".mem, .mima";
					}

					@Override
					public boolean accept(File arg0) {
						return arg0.getName().matches(".*\\.mem")
								|| arg0.getName().matches(".*\\.mima")
								|| arg0.isDirectory();
					}
				};

				fileExplorer.setFileFilter(filter);

				// check choosen file
				if (fileExplorer.showOpenDialog(null) == 0) {
					File input = fileExplorer.getSelectedFile();

					// add suffix if not .mem or .mima
					if (!input.getName().matches(".*\\.mem")
							&& !input.getName().matches(".*\\.mima")) {
						input = new File(input.getPath() + ".mem");
					}
					controller.loadMem(input);
					controller.reset();
				}

			}
		});

		buttons.put("loadMem", tmp);

		tmp = new JButton("save memory");
		tmp.setLocation(35, 270);
		tmp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileExplorer = new JFileChooser();
				fileExplorer.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileExplorer.setApproveButtonText("save");

				// add Filter for only accepting .mem and .mima files
				FileFilter filter = new FileFilter() {

					@Override
					public String getDescription() {
						return ".mem, .mima";
					}

					@Override
					public boolean accept(File arg0) {
						return arg0.getName().matches(".*\\.mem")
								|| arg0.getName().matches(".*\\.mima")
								|| arg0.isDirectory();
					}
				};

				fileExplorer.setFileFilter(filter);

				// check choosen file
				if (fileExplorer.showOpenDialog(null) == 0) {
					File input = fileExplorer.getSelectedFile();

					// add suffix if not .mem or .mima
					if (!input.getName().matches(".*\\.mem")
							&& !input.getName().matches(".*\\.mima")) {
						input = new File(input.getPath() + ".mem");
					}
					controller.saveMem(input);
					changed = false;
				}

			}
		});

		buttons.put("saveMem", tmp);

		tmp = new JButton("save result");
		tmp.setLocation(35, 310);
		tmp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileExplorer = new JFileChooser();
				fileExplorer.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileExplorer.setApproveButtonText("save");

				// add Filter for only accepting .mem files
				FileFilter filter = new FileFilter() {

					@Override
					public String getDescription() {
						return ".mem, .mima";
					}

					@Override
					public boolean accept(File arg0) {
						return arg0.getName().matches(".*\\.mem")
								|| arg0.getName().matches(".*\\.mima")
								|| arg0.isDirectory();
					}
				};

				fileExplorer.setFileFilter(filter);

				// check choosen file
				if (fileExplorer.showOpenDialog(null) == 0) {
					File input = fileExplorer.getSelectedFile();

					// add suffix if not .mem or .mima
					if (!input.getName().matches(".*\\.mem")
							&& !input.getName().matches(".*\\.mima")) {
						input = new File(input.getPath() + ".mem");
					}
					controller.saveResult(input);
					changed = false;
				}

			}
		});

		buttons.put("saveRes", tmp);

		for (JButton button : buttons.values()) {
			button.setSize(110, 30);
			button.setVisible(true);
			dataContent.add(button);
		}
	}

	private void addMimaContent() {
		mimaContent = new JPanel();
		mimaContent.setLayout(null);
		mimaContent.setPreferredSize(new Dimension(479, 470));
		mimaContent.setBackground(backgroundColor);
		mimaContent.setVisible(true);
		content.add(mimaContent);

		addRectangles();
		addDottedLines();
		addAlu();
		addBus();
		addArrows();
		addBorder();

	}

	private void addDottedLines() {
		Element dottedLine = new DottedLine(true, false, false, true);
		dottedLine.setLocation(287, 139);
		dottedLine.setSize(116, 10);
		dottedLine.setBorderColor(borderColor);
		dottedLine.setVisible(true);
		mimaContent.add(dottedLine);

		dottedLine = new DottedLine(false, false, true, true);
		dottedLine.setLocation(377, 176);
		dottedLine.setSize(56, 63);
		dottedLine.setBorderColor(borderColor);
		dottedLine.setVisible(true);
		mimaContent.add(dottedLine);

		dottedLine = new DottedLine(true, false, false, false);
		dottedLine.setLocation(431, 104);
		dottedLine.setSize(2, 15);
		dottedLine.setBorderColor(borderColor);
		dottedLine.setVisible(true);
		mimaContent.add(dottedLine);
	}

	private void addRectangles() {
		elements.put("akku", new Rectangle());
		elements.put("eins", new Rectangle());
		elements.put("sar", new Rectangle());
		elements.put("sdr", new Rectangle());
		elements.put("x", new Rectangle());
		elements.put("y", new Rectangle());
		elements.put("z", new Rectangle());
		elements.put("ir", new Rectangle());
		elements.put("iar", new Rectangle());
		elements.put("akkuN", new Rectangle());
		elements.put("irOP", new Rectangle());
		elements.put("sw", new Rectangle());
		elements.put("takt", new Rectangle());

		Element current = elements.get("akku");
		current.setLocation(23, 46);
		current.setText("Akku");

		current = elements.get("eins");
		current.setLocation(18, 119);
		current.setText(toHex(1, 6));

		current = elements.get("sar");
		current.setLocation(18, 376);
		current.setText("SAR");

		current = elements.get("sdr");
		current.setLocation(274, 376);
		current.setText("SDR");

		current = elements.get("x");
		current.setLocation(219, 284);
		current.setText("X");

		current = elements.get("y");
		current.setLocation(329, 303);
		current.setText("Y");

		current = elements.get("z");
		current.setLocation(274, 174);
		current.setText("Z");

		current = elements.get("ir");
		current.setLocation(299, 119);
		current.setText("IR");

		current = elements.get("iar");
		current.setLocation(274, 46);
		current.setText("IAR");

		for (Element element : elements.values()) {
			element.setSize(76, 20);
			element.setBackgroundColor(backgroundColor);
			element.setBorderColor(borderColor);
			element.setActiveColor(activeColor);
			element.setVisible(true);

			mimaContent.add(element);
		}

		current = elements.get("sw");
		current.setLocation(403, 119);
		current.setText("ctrl");
		current.setSize(57, 57);

		current = elements.get("takt");
		current.setLocation(413, 75);
		current.setText("Takt");
		current.setSize(38, 29);

		current = elements.get("akkuN");
		current.setLocation(18, 46);
		current.setSize(7, 20);

		current = elements.get("irOP");
		current.setLocation(274, 119);
		current.setSize(27, 20);

		current = elements.get("ir");
		current.setSize(51, 20);

		current = elements.get("akku");
		current.setSize(71, 20);
	}

	private void addAlu() {
		Element alu = new AluE();
		elements.put("alu", alu);

		alu.setSize(187, 57);
		alu.setLocation(218, 211);
		alu.setBackgroundColor(backgroundColor);
		alu.setBorderColor(borderColor);
		alu.setActiveColor(activeColor);
		alu.setText("ALU");

		alu.setVisible(true);
		mimaContent.add(alu);
	}

	private void addBus() {
		HashMap<String, Element> bus = new HashMap<String, Element>();

		Element current = new DoubleLineV();
		current.setSize(6, 370);
		current.setLocation(181, 31);
		bus.put("mainBus", current);

		current = new DoubleLineH();
		current.setSize(73, 6);
		current.setLocation(101, 53);
		bus.put("akkuBus", current);

		current = new DoubleLineH();
		current.setSize(73, 6);
		current.setLocation(194, 53);
		bus.put("iarBus", current);

		current = new DoubleLineH();
		current.setSize(80, 6);
		current.setLocation(94, 126);
		bus.put("einsBus", current);

		current = new DoubleLineH();
		current.setSize(80, 6);
		current.setLocation(101, 383);
		bus.put("sarBus", current);

		current = new DoubleLineH();
		current.setSize(73, 6);
		current.setLocation(194, 126);
		bus.put("irBus", current);

		current = new DoubleLineH();
		current.setSize(80, 6);
		current.setLocation(194, 181);
		bus.put("zBus", current);

		current = new DoubleLineH();
		current.setSize(25, 6);
		current.setLocation(187, 291);
		bus.put("xBus", current);

		current = new DoubleLineH();
		current.setSize(135, 6);
		current.setLocation(187, 310);
		bus.put("yBus", current);

		current = new DoubleLineH();
		current.setSize(73, 6);
		current.setLocation(194, 383);
		bus.put("sdrBus", current);

		current = new DoubleLineV();
		current.setSize(6, 9);
		current.setLocation(253, 275);
		bus.put("xAlu", current);

		current = new DoubleLineV();
		current.setSize(6, 28);
		current.setLocation(363, 275);
		bus.put("yAlu", current);

		current = new DoubleLineV();
		current.setSize(6, 10);
		current.setLocation(308, 201);
		bus.put("zAlu", current);

		current = new DoubleLineV();
		current.setSize(6, 48);
		current.setLocation(53, 396);
		bus.put("sarMem", current);

		current = new DoubleLineV();
		current.setSize(6, 41);
		current.setLocation(308, 403);
		bus.put("sdrMem", current);

		for (Element element : bus.values()) {
			element.setBackgroundColor(backgroundColor);
			element.setBorderColor(borderColor);
			element.setActiveColor(activeColor);
			element.setVisible(true);

			mimaContent.add(element);
		}

		elements.putAll(bus);
	}

	private void addArrows() {
		HashMap<String, Element> arrowH = new HashMap<String, Element>();
		HashMap<String, Element> arrowV = new HashMap<String, Element>();

		Element current = new ArrowL();
		current.setLocation(94, 49);
		arrowH.put("akkuIn", current);

		current = new ArrowL();
		current.setLocation(94, 379);
		arrowH.put("sarIn", current);

		current = new ArrowL();
		current.setLocation(187, 379);
		arrowH.put("sdrOut", current);

		current = new ArrowL();
		current.setLocation(187, 177);
		arrowH.put("zOut", current);

		current = new ArrowL();
		current.setLocation(187, 122);
		arrowH.put("irOut", current);

		current = new ArrowL();
		current.setLocation(187, 49);
		arrowH.put("iarOut", current);

		current = new ArrowR();
		current.setLocation(174, 49);
		arrowH.put("akkuOut", current);

		current = new ArrowR();
		current.setLocation(174, 122);
		arrowH.put("einsOut", current);

		current = new ArrowR();
		current.setLocation(267, 379);
		arrowH.put("sdrIn", current);

		current = new ArrowR();
		current.setLocation(322, 306);
		arrowH.put("yIn", current);

		current = new ArrowR();
		current.setLocation(212, 287);
		arrowH.put("xIn", current);

		current = new ArrowR();
		current.setLocation(267, 122);
		arrowH.put("irIn", current);

		current = new ArrowR();
		current.setLocation(267, 49);
		arrowH.put("iarIn", current);

		for (Element element : arrowH.values()) {
			element.setSize(7, 14);
			element.setBackgroundColor(backgroundColor);
			element.setBorderColor(borderColor);
			element.setActiveColor(activeColor);
			element.setVisible(true);

			mimaContent.add(element);
		}

		current = new ArrowD();
		current.setLocation(49, 444);
		arrowV.put("sarMemO", current);

		current = new ArrowD();
		current.setLocation(304, 444);
		arrowV.put("sdrMemO", current);

		current = new ArrowD();
		current.setLocation(177, 401);
		arrowV.put("mBusD", current);

		current = new ArrowU();
		current.setLocation(177, 24);
		arrowV.put("mBusU", current);

		current = new ArrowU();
		current.setLocation(304, 194);
		arrowV.put("zIn", current);

		current = new ArrowU();
		current.setLocation(304, 396);
		arrowV.put("sdrMemI", current);

		current = new ArrowU();
		current.setLocation(249, 268);
		arrowV.put("xOut", current);

		current = new ArrowU();
		current.setLocation(359, 268);
		arrowV.put("yOut", current);

		for (Element element : arrowV.values()) {
			element.setSize(14, 7);
			element.setBackgroundColor(backgroundColor);
			element.setBorderColor(borderColor);
			element.setActiveColor(activeColor);
			element.setVisible(true);

			mimaContent.add(element);
		}

		elements.putAll(arrowV);
		elements.putAll(arrowH);
	}

	private void addBorder() {

		Element tmp = new Rectangle();
		tmp.setSize(479, 415);
		tmp.setLocation(0, 0);
		tmp.setBorderColor(new Color(0x80, 0x80, 0x80));
		tmp.setBackgroundColor(new Color(0, 0, 0, 0));
		tmp.setVisible(true);
		mimaContent.add(tmp);
	}

	/**
	 * set the graphical status of all elements
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

		LinkedList<Element> active = new LinkedList<Element>();

		for (Element element : elements.values()) {
			element.setActive(false);
		}

		if (Ar || Aw || RX || RY || RZ || E || Pr || Pw || Ir || Iw || Dr || Dw
				|| S)
			active.add(elements.get("mainBus"));

		// write actions
		if (Aw || Ar) {
			active.add(elements.get("akkuBus"));
			active.add(elements.get("akku"));
			active.add(elements.get("akkuN"));
		}
		if (Pw || Pr) {
			active.add(elements.get("iarBus"));
			active.add(elements.get("iar"));
		}
		if (Iw || Ir) {
			active.add(elements.get("ir"));
			active.add(elements.get("irOP"));
			active.add(elements.get("irBus"));
		}

		if (Dw || Dr) {
			active.add(elements.get("sdrBus"));
			active.add(elements.get("sdr"));
		}

		if (Aw)
			active.add(elements.get("akkuOut"));

		if (RZ) {
			active.add(elements.get("zBus"));
			active.add(elements.get("zOut"));
			active.add(elements.get("z"));
		}

		if (E) {
			active.add(elements.get("einsBus"));
			active.add(elements.get("einsOut"));
			active.add(elements.get("eins"));
		}

		if (Pw)
			active.add(elements.get("iarOut"));

		if (Iw)
			active.add(elements.get("irOut"));

		if (Dw)
			active.add(elements.get("sdrOut"));

		// read actions
		if (Ar)
			active.add(elements.get("akkuIn"));

		if (RX) {
			active.add(elements.get("xBus"));
			active.add(elements.get("xIn"));
			active.add(elements.get("x"));
		}
		if (RY) {
			active.add(elements.get("yBus"));
			active.add(elements.get("yIn"));
			active.add(elements.get("y"));
		}
		if (Pr)
			active.add(elements.get("iarIn"));

		if (Ir)
			active.add(elements.get("irIn"));

		if (Dr)
			active.add(elements.get("sdrIn"));

		if (S) {
			active.add(elements.get("sarBus"));
			active.add(elements.get("sarIn"));
			active.add(elements.get("sar"));
		}

		// alu function
		if (c != 0) {
			active.add(elements.get("xAlu"));
			active.add(elements.get("yAlu"));
			active.add(elements.get("zAlu"));
			active.add(elements.get("zIn"));
			active.add(elements.get("xOut"));
			active.add(elements.get("yOut"));
			active.add(elements.get("alu"));
			active.add(elements.get("x"));
			active.add(elements.get("y"));
			active.add(elements.get("z"));
		}

		// external functions
		if (R || W) {
			active.add(elements.get("sdrMem"));
			active.add(elements.get("sdr"));
			active.add(elements.get("sar"));
			active.add(elements.get("sarMem"));
			active.add(elements.get("sarMemO"));

			ListSelectionModel sel = memoryTable.getSelectionModel();
			sel.setSelectionInterval(getNum(elements.get("sar").getText()),
					getNum(elements.get("sar").getText()));
			memoryTable.scrollRectToVisible(memoryTable.getCellRect(
					getNum(elements.get("sar").getText()), 0, false));
		} else {
			ListSelectionModel sel = memoryTable.getSelectionModel();
			sel.clearSelection();
		}

		if (R)
			active.add(elements.get("sdrMemI"));

		if (W)
			active.add(elements.get("sdrMemO"));

		for (Element element : active) {
			if (element != null)
				element.setActive(true);
		}
	}

	/**
	 * set values of registers, memory and ALU
	 * 
	 * @param akku
	 * @param alu
	 * @param iar
	 * @param ir
	 * @param sar
	 * @param sdr
	 * @param x
	 * @param y
	 * @param z
	 * @param memory
	 */
	public void setValues(int akku, int alu, int iar, int ir, int sar, int sdr,
			int x, int y, int z, HashMap<Integer, Integer> memory) {

		elements.get("akku").setText(toHex(akku, 6));
		elements.get("alu").setText(toBinary(alu, 3));
		elements.get("iar").setText(toHex(iar, 5));
		elements.get("sar").setText(toHex(sar, 5));
		elements.get("sdr").setText(toHex(sdr, 6));
		elements.get("x").setText(toHex(x, 6));
		elements.get("y").setText(toHex(y, 6));
		elements.get("z").setText(toHex(z, 6));

		int opCode = (ir >> 16) & 0xFF;
		if ((opCode >> 4) != 0xF) {
			opCode >>= 4;
			ir &= 0xFFFFF;
			elements.get("ir").setText(toHex(ir, 5));
			elements.get("irOP").setText(toHex(opCode, 1));
		} else {
			ir &= 0xFFFF;
			elements.get("ir").setText(toHex(ir, 4));
			elements.get("irOP").setText(toHex(opCode, 2));
		}

		for (int key : memory.keySet()) {
			memoryTable.setValueAt(toHex(memory.get(key), 6), key, 1);
		}
	}

	/**
	 * delete specific memory values
	 * 
	 * @param memory
	 */
	public void deleteValues(HashMap<Integer, Integer> memory) {

		for (int key : memory.keySet()) {
			memoryTable.setValueAt(toHex(0, 6), key, 1);
		}
	}

	/**
	 * toggle clock status
	 * 
	 * @param active
	 *            whether the clock is active or not
	 */
	public void clock(boolean active) {
		elements.get("takt").setActive(active);
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

	private String toBinary(int value, int length) {
		String binary = Integer.toBinaryString(value);

		while (binary.length() > length) {
			binary = binary.substring(1);
		}
		while (binary.length() < length) {
			binary = "0" + binary;
		}
		binary = "0b" + binary;
		return binary;
	}

	private boolean isNumber(String input) {
		return input.matches("(0b([01])*)|(0x([0-9A-Fa-f])*)|[0-9]*");
	}

	private int getNum(String input) {
		int number = 0;
		int base = 10;
		if (isNumber(input)) {

			if (input.startsWith("0x")) {
				base = 16;
				input = input.substring(2);
				input = input.toUpperCase();
			} else if (input.startsWith("0b")) {
				base = 2;
				input = input.substring(2);
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
		}
		return number;
	}

	/**
	 * reset everything
	 */
	public void reset() {
		buttons.get("start").setText(">");
		controller.reset();
	}

	/**
	 * toggle pause
	 * 
	 * @param state
	 *            whether the simulation should be paused or not
	 */
	public void pause(boolean state) {
		if (state)
			buttons.get("start").setText(">");
		else
			buttons.get("start").setText("||");
	}

	/**
	 * 
	 * @param value
	 *            whether the memory was changed or not
	 */
	public void setChangedStatus(boolean value) {
		changed = value;
	}
}
