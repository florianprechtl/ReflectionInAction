package CodeGeneration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloWorldGenerator {
	public static void main(String[] args) {
		try {
			Process p = createAndCompileClass();
			loadAndExecuteClass(p);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Process createAndCompileClass() throws IOException, InterruptedException {
		File file = getFile();
		writeToFile(file);
		Process p = compileClass();
		deleteFile(file);
		return p;
	}

	private static void loadAndExecuteClass(Process p)
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
		if (p.exitValue() == 0) {
			Class outputClassObject = Class.forName("HelloWorld");
			Class[] fpl = {String[].class};
			Method m = outputClassObject.getMethod("main", fpl);
			// Important, because otherwise the creating class can not access the main method
			m.setAccessible(true);
			m.invoke(null, new Object[]{new String[]{}});
		} else {
			InputStream errStream = p.getErrorStream();
			for (int j = errStream.available(); j > 0; j--) {
				System.out.write(errStream.read());
			}
		}
	}

	private static File getFile() throws IOException {
		File file = new File("HelloWorld.java");
		if (file.createNewFile()) {
			System.out.println("File created: " + file.getName());
		} else {
			System.out.println("File already exists.");
		}
		return file;
	}

	private static void writeToFile(File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write(
				"class HelloWorld { \n"
						+ " public static void main( String[] args ) { \n"
						+ " System.out.println( \"Hello world!\" );\n"
						+ " } \n"
						+ "} "
		);
		writer.close();
	}

	private static Process compileClass() throws IOException, InterruptedException {
		Process p
				= Runtime.getRuntime().exec("javac -d ./target/classes HelloWorld.java");
		p.waitFor();
		return p;
	}

	private static void deleteFile(File file) {
		file.delete();
	}
}
