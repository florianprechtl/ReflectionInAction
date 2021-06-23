package DesignPatterns.Decorator;

import CodeGeneration.C2C.Args;
import CodeGeneration.C2C.C2CException;
import CodeGeneration.C2C.C2IdentitySubclassOfC;
import Mopex.Mopex;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class C2InvariantCheckingC extends C2IdentitySubclassOfC {
	protected Method invMethod;

	static public void main(String[] args) {
		new C2InvariantCheckingC().createClass(args);
	}

	protected String generateClassNamePrefix() {
		return "InvariantChecking" + super.generateClassNamePrefix();
	}

	protected void checkAndProcessArgs(Args args) {
		super.checkAndProcessArgs(args);
		try {
			invMethod = inputClassObject.getMethod("invariant", null);
		} catch (NoSuchMethodException e) {
			throw new C2CException(e);
		}
		if (invMethod.getReturnType() != boolean.class) {
			throw new C2CException("invariant return not boolean");
		}
		if (inputClassObject.getPackage() != null) {
			if (!inputClassObject.getPackage().getName().equals(packageName)) {
				throw new C2CException("input class in different package");
			}
		} else if (packageName != null) {
			throw new C2CException("Input class in different package");
		}
		if (Mopex.getMethodsLackingImplementation(inputClassObject).length != 0) {
			setAbstract();
		}
	}

	protected String generateMethods() {
		int mods = Modifier.STATIC | Modifier.ABSTRACT
				| Modifier.FINAL | Modifier.PRIVATE;
		Method[] nsMethods = Mopex.selectMethods(inputClassObject,
				0,
				mods,
				Object.class);
		String result = generateCheckInvariant();
		String wrapperCode
				= " assert checkInvariant() : \"invariant failure\";\n";
		for (int i = 0; i < nsMethods.length; i++) {
			if (!invMethod.equals(nsMethods[i])) {
				int mods2 = Mopex.getModifiersWithout(nsMethods[i],
						Modifier.NATIVE);
				result += Modifier.toString(mods2) + " "
						+ Mopex.createCooperativeWrapper(nsMethods[i],
						wrapperCode,
						wrapperCode);
			}
		}
		return super.generateMethods() + result;
	}

	private String generateCheckInvariant() {
		return "private boolean checkInvariant() {\n"
				+ " StackTraceElement[] ste\n"
				+ " = (new Throwable()).getStackTrace();\n"
				+ " String className = this.getClass().getName();\n"
				+ " String mName = \"checkInvariant\";\n"
				+ " for ( int i = 1; i < ste.length; i++ ) {\n"
				+ " if ( ste[i].getClassName().equals(className)\n"
				+ " && ste[i].getMethodName().equals(mName) )\n"
				+ " return true;\n"
				+ " }\n"
				+ " return this.invariant();\n"
				+ "}\n";
	}
}