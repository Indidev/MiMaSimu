package org.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.io.TempMem;

/**
 * Gui for memory editing
 * 
 * @author Dominik Muth
 * 
 */
public class MemEditGUI extends JFrame {

	private static final long serialVersionUID = -6127225316420096181L;
	private JPanel buttonPanel;
	private JEditorPane edtPane;
	private JScrollPane scrollPane;
	private JButton exitBtn;
	private JButton applyBtn;
	private final MemEditGUI edtGUI = this;
	private final GUI mainGUI;

	/**
	 * creates a new memory editing gui
	 * 
	 * @param main
	 *            main gui
	 */
	public MemEditGUI(GUI main) {
		this.mainGUI = main;
		mainGUI.setVisible(false);

		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(300, 40));
		buttonPanel.setVisible(true);

		edtPane = new JEditorPane();
		edtPane.setAutoscrolls(true);
		edtPane.setVisible(true);

		scrollPane = new JScrollPane(edtPane);
		scrollPane.setPreferredSize(new Dimension(300, 470));
		scrollPane.setVisible(true);

		exitBtn = new JButton("Cancel");
		exitBtn.setSize(70, 26);
		exitBtn.setVisible(true);
		buttonPanel.add(exitBtn, BorderLayout.LINE_START);
		exitBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainGUI.setVisible(true);
				edtGUI.setVisible(false);
			}
		});

		applyBtn = new JButton("apply");
		applyBtn.setSize(70, 26);
		// applyBtn.setLocation(210, 472);
		applyBtn.setVisible(true);
		buttonPanel.add(applyBtn, BorderLayout.LINE_END);
		buttonPanel.add(new JPanel(), BorderLayout.CENTER);
		applyBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!TempMem.getText().equals(edtPane.getText())) {
					mainGUI.setChangedStatus(true);
				}
				TempMem.setText(edtPane.getText());
				mainGUI.reset();

				mainGUI.setVisible(true);
				edtGUI.setVisible(false);
			}
		});

		edtPane.setText(TempMem.getText());
		edtPane.setCaretPosition(0);

		this.add(buttonPanel);
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
				if (!TempMem.getText().equals(edtPane.getText())) {
					int option = JOptionPane
							.showConfirmDialog(
									edtGUI,
									"You did some changes to the Memory,\n do you want to save your changes?",
									"save changes?",
									JOptionPane.YES_NO_CANCEL_OPTION);
					if (option == JOptionPane.YES_OPTION) {
						mainGUI.setChangedStatus(true);
						TempMem.setText(edtPane.getText());
						mainGUI.reset();

						mainGUI.setVisible(true);
						edtGUI.setVisible(false);

					} else if (option == JOptionPane.NO_OPTION) {

						mainGUI.setVisible(true);
						edtGUI.setVisible(false);
					}

				} else {

					mainGUI.setVisible(true);
					edtGUI.setVisible(false);
				}
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});

		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				edtPane.setSize(scrollPane.getWidth() - 2,
						scrollPane.getHeight());
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});

		this.add(scrollPane, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.PAGE_END);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
// Ich liebe Dich