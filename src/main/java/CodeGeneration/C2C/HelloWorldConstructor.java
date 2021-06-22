package CodeGeneration.C2C;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HelloWorldConstructor extends C2CConstructor {
	static public void main( String[] args ) throws InvocationTargetException, IllegalAccessException {
		Class c = new HelloWorldConstructor().createClass(args);
		Method m = c.getDeclaredMethods()[0];
		m.invoke(null, new String[] {null});
	}

	protected String generateMethods() {
		return super.generateMethods()
				+ " public static void main( String[] args ) { \n"
				+ " System.out.println( \"Hello world!\" );\n"
				+ " } \n";
	}
}
