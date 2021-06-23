package CodeGeneration.C2C;

import java.lang.reflect.InvocationTargetException;

public class Main {
	public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
		TestHelloWorld();
		TestC2ExtentManagedC();
		TestC2IdentitySubclassOfC();
	}

	private static void TestHelloWorld() {
		try {
			HelloWorldConstructor.main(new String[] {
					"-output", "HelloWorld", "-package", "CodeGeneration.C2C"
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
					"-package", "CodeGeneration.C2C", "CodeGeneration.C2C.HelloWorld"
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private static void TestC2IdentitySubclassOfC() {
		C2IdentitySubclassOfC.main(new String[] {
				"-package", "CodeGeneration.C2C", "java.lang.RuntimeException"
		});
	}
}
