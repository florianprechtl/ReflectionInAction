package ClassLoader;

public class SimpleClassLoaderTest {
	public static void main(String[] args)
			throws ClassNotFoundException,
			InstantiationException,
			IllegalAccessException {
		SimpleClassLoader firstClassLoader
				= new SimpleClassLoader("ClassLoader/Testcases");
		Class c1 = firstClassLoader.loadClass("ClassLoader.ConstructOnce");
		SimpleClassLoader secondClassLoader
				= new SimpleClassLoader("ClassLoader/Testcases");
		Class c2 = secondClassLoader.loadClass("ClassLoader.ConstructOnce");
		Object x = c1.newInstance();
		try {
			Object y = c1.newInstance();
			throw new RuntimeException("Test fails");
		} catch (IllegalStateException e) {
		}
		Object z = c2.newInstance();
	}
}
