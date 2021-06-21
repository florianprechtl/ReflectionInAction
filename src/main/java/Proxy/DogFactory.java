package Proxy;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DogFactory {
	private Class dogClass;
	private boolean traceIsOn = false;

	public DogFactory(String className, boolean trace) {
		try {
			dogClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e); // or whatever is appropriate
		}
		traceIsOn = trace;
	}

	public Dog newInstance(String name, int size) {
		try {
			Dog d = (Dog) dogClass.newInstance();
			d.initialize(name, size);
			if (traceIsOn) {
				d = (Dog) TracingIH.createProxy(d, new PrintWriter(System.out, true));
			}
			return d;
		} catch (InstantiationException e) {
			throw new RuntimeException(e); // or whatever is appropriate
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e); // or whatever is appropriate
		}
	}

	public static void main(String[] args)
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		DogFactory factory = new DogFactory(Husky.class.getName(), true);
		Dog d = factory.newInstance("Flo", 20);
		d.retrieveStick();
	}
}