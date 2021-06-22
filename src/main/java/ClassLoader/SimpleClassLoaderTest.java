package ClassLoader;

public class SimpleClassLoaderTest {
	public static void main(String[] args)
			throws ClassNotFoundException,
			InstantiationException,
			IllegalAccessException {
		SimpleClassLoader firstClassLoader
				= new SimpleClassLoader("D:/Projekte/Java/ReflectionInAction/src/main/java");
		Class c1 = firstClassLoader.loadClass("ClassLoader.ConstructOnce");
		SimpleClassLoader secondClassLoader
				= new SimpleClassLoader("D:/Projekte/Java/ReflectionInAction/src/main/java");
		Class c2 = secondClassLoader.loadClass("ClassLoader.ConstructOnce");
		Object x = c1.newInstance();
		Object y = c1.newInstance();
		Object z = c2.newInstance();
	}
}
