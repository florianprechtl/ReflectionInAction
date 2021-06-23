package DesignPatterns.Proxy;

import CodeGeneration.C2C.Args;
import CodeGeneration.C2C.C2CException;
import CodeGeneration.C2C.C2CTransformation;
import Mopex.Mopex;
import Mopex.UQueue;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class C2ProxyForC extends C2CTransformation {
	static public void main(String[] args) {
		new C2ProxyForC().createClass(args);
	}

	protected String generateClassNamePrefix() {
		return "ProxyFor" + super.generateClassNamePrefix();
	}

	protected void checkAndProcessArgs(Args args) {
		super.checkAndProcessArgs(args);
		if (!inputClassObject.isInterface()) {
			throw new C2CException("input class is not an interface");
		}
	}

	protected UQueue generateInterfaces() {
		return super.generateInterfaces().add(inputClassName);
	}

	protected String generateFields() {
		return super.generateFields()
				+ " private " + inputClassName + " target;\n"
				+ " private static final String targetClassName = \""
				+ qualifiedInputClassName + "\";\n";
	}

	protected String generateConstructors() {
		return super.generateConstructors()
				+ outputClassName + "( " + inputClassName + " tgt ) {\n"
				+ " target = tgt;\n"
				+ "}\n";
	}

	protected String generateMethods() {
		String result = "";
		Method[] methods = Mopex.selectMethods(inputClassObject,
				Modifier.PUBLIC,
				Modifier.STATIC,
				java.lang.Object.class);
		for (int i = 0; i < methods.length; i++) {
			int mods = Mopex.getModifiersWithout(methods[i],
					Modifier.NATIVE
							| Modifier.ABSTRACT);
			result += " " + Modifier.toString(mods) + " "
					+ Mopex.headerSuffixToString(methods[i]) + "{\n";
			Class[] fpl = methods[i].getParameterTypes();
			String apl = Mopex.actualParametersToString(fpl);
			if (methods[i].getReturnType() == void.class) {
				result += " target."
						+ methods[i].getName()
						+ "(" + apl + ");\n";
			} else {
				result += " return target."
						+ methods[i].getName()
						+ "(" + apl + ");\n";
			}
			result += " }\n";
		}
		result +=
				" public boolean equals( Object obj ) {\n" +
						" return target.equals( obj );\n" +
						" }\n" +
						" public int hashCode() {\n" +
						" return target.hashCode();\n" +
						" }\n" +
						" public String equals() {\n" +
						" return target.toString();\n" +
						" }\n";
		return super.generateMethods() + result;
	}

	protected void checkPostconditions() {
		if (outputClassObject.getDeclaredConstructors().length != 1) {
			throw new C2CException("a proxy has only one constructor");
		}
		super.checkPostconditions();
	}
}
