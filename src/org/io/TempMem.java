package org.io;

import java.util.LinkedList;

public class TempMem {

	private static String text = "";

	public static LinkedList<String[]> getValueList() {
		LinkedList<String[]> lines = new LinkedList<String[]>();
		String[] splittedLines = text.split("\n");

		for (String line : splittedLines) {
			String[] elements = line.split("[ \t]+");
			lines.add(elements);
		}

		return lines;
	}

	public static String getText() {
		return text;
	}

	public static void setText(String inputText) {
		text = inputText;
	}

	public static void setText(LinkedList<String[]> lines) {
		text = "";
		for (String[] line : lines) {
			for (String element : line) {
				text += element + " ";
			}
			text += "\n";
		}
	}
}
