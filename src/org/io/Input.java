package org.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Input {

	private Input() {
	}

	public static LinkedList<String[]> loadFile(File file) {
		LinkedList<String[]> list = new LinkedList<String[]>();

		try {
			String line;
			BufferedReader reader = new BufferedReader(new FileReader(file));

			while ((line = reader.readLine()) != null) {
				list.add(line.split("[ \t]+"));
			}

			reader.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return list;
	}
}
