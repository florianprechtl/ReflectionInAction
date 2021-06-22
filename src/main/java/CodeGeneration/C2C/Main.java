package CodeGeneration.C2C;

import java.lang.reflect.InvocationTargetException;

public class Main {
	public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
		TestHelloWorld();
		TestC2ExtentManagedC();
	}

	private static void TestHelloWorld() {
		try {
			HelloWorldConstructor.main(new String[] {
					"-output", "HelloWorld"
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private static void TestC2ExtentManagedC() {
		try {
			C2ExtentManagedC.main(new String[] {
					"-output", "HelloWorld2", "HelloWorld"
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
