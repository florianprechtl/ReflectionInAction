package ClassLoader;

import java.lang.reflect.Method;

public class SimpleSuiteTestProgram {
	static Class<?>[] formals = {String[].class};
	static Object[] actuals = {new String[]{""}};

	public static void main(String[] args) {
		try {
			for (int i = 0; ; i++) {
				SimpleClassLoader aClassLoader
						= new SimpleClassLoader("ClassLoader/Testcases");

				Class<?> c = aClassLoader.loadClass("ClassLoader.Testcases.TestCase" + i);
				Method m = null;
				try {
					m = c.getMethod("main", formals);
				} catch (NoSuchMethodException e) {
					System.out.println("TestCase0" + i
							+ ": no main in test case");
					break;
				}
				try {
					m.invoke(null, actuals);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (ClassNotFoundException ignored) {
			System.out.println("Class not found!");
		} // testing completed
	}
}
