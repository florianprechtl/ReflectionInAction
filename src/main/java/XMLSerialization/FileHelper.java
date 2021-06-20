package XMLSerialization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileHelper {

	public static final String LINE_SEPARATOR = "\r\n";

	public static void writeToFile(String text, String fileName) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(fileName, false));
			writer.write(text);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createFile(File file) {
		try {
			String fileName = file.getName();
			if (file.createNewFile()) {
				System.out.println("File newly created: " + fileName);
			} else {
				System.out.println("File exists already: " + fileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readFile(String path) {
		StringBuilder sb = new StringBuilder();

		try {
			File myObj = new File(path);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine() + LINE_SEPARATOR;
				sb.append(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}
}
