package DesignPatterns;

import DesignPatterns.Decorator.C2InvariantCheckingC;
import DesignPatterns.Proxy.C2ProxyForC;
import DesignPatterns.Singleton.C2SingletonC;
import java.lang.reflect.InvocationTargetException;

public class Main {
	public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
		TestSingleton();
		TestDecorator();
		TestProxy();
	}

	private static void TestSingleton() {
		C2SingletonC.main(new String[] {
				"-package", "DesignPatterns.Singleton", "DesignPatterns.Singleton.MyClass"
		});
	}

	private static void TestDecorator() {
		C2InvariantCheckingC.main(new String[] {
				"-package", "DesignPatterns.Decorator", "DesignPatterns.Decorator.MyClass"
		});
	}

	private static void TestProxy() {
		C2ProxyForC.main(new String[] {
				"-package", "DesignPatterns.Proxy", "DesignPatterns.Proxy.MyInterface"
		});
	}
}
