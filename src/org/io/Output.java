package org.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Output {

	public static void saveFile(File file, String output) {

		FileWriter writer;

		try {
			writer = new FileWriter(file, false);

			writer.write(output.replace("\n",
					System.getProperty("line.separator")));

			writer.flush();

			writer.close();
		} catch (IOException e) {
			System.out.print(e.getMessage());
		}
	}

}
