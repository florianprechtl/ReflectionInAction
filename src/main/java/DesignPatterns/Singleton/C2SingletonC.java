package DesignPatterns.Singleton;

import CodeGeneration.C2C.Args;
import CodeGeneration.C2C.C2CException;
import CodeGeneration.C2C.C2CTransformation;
import Mopex.Mopex;
import Mopex.UQueue;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class C2SingletonC extends C2CTransformation {
	private int numberOfConstructors = 0;

	static public void main(String[] args) {
		new C2SingletonC().createClass(args);
	}

	protected void checkAndProcessArgs(Args args) {
		super.checkAndProcessArgs(args);
		setFinal();
		if (inputClassObject.isInterface()) {
			throw new C2CException("cannot generate Singleton for interface");
		}
		if (Serializable.class.isAssignableFrom(inputClassObject)) {
			throw new C2CException("cannot handle Serializable input classes");
		}
		if (Cloneable.class.isAssignableFrom(inputClassObject)) {
			throw new C2CException("Cloneable and Singleton are conflicting");
		}
	}

	protected UQueue generateImports() {
		return super.generateImports().add("java.lang.ref.*");
	}

	protected String generateClassNamePrefix() {
		return "Singleton" + super.generateClassNamePrefix();
	}

	protected String getSuperclass() {
		return inputClassName;
	}

	protected String generateFields() {
		return super.generateFields()
				+ "static private WeakReference singleton = null;\n";
	}

	protected final String generateConstructors() {
		String result = "";
		Constructor[] cArray = inputClassObject.getDeclaredConstructors();
		String code = " if (singleton!=null && singleton.get()!=null)\n" +
				" throw new RuntimeException("
				+ "\"Singleton constructor failure\");\n" +
				" singleton = new WeakReference( this );\n";
		if (cArray.length != 0) {
			for (int i = 0; i < cArray.length; i++) {
				result += "private "
						+ Mopex.createRenamedConstructor(cArray[i],
						outputClassName,
						code);
			}
			numberOfConstructors = cArray.length;
		} else {
			result = "private " + outputClassName + "() {" + code + "}\n";
			numberOfConstructors = 1;
		}
		return super.generateConstructors() + result;
	}

	protected String generateMethods() {
		String result = "";
		Constructor[] cArray = inputClassObject.getDeclaredConstructors();
		if (cArray.length != 0) {
			for (int i = 0; i < cArray.length; i++) {
				Class[] pta = cArray[i].getParameterTypes();
				String fpl = Mopex.formalParametersToString(pta);
				String apl = Mopex.actualParametersToString(pta);
				Class[] eTypes = cArray[i].getExceptionTypes();
				int modifiers = cArray[i].getModifiers();
				result += "static " + Modifier.toString(modifiers) + " "
						+ outputClassName + " getInstance(" + fpl + ")\n";
				if (eTypes.length != 0) {
					result
							+= " throws " + Mopex.classArrayToString(eTypes)
							+ "\n";
				}
				result += "{\n"
						+ " if (singleton==null || singleton.get()==null)\n"
						+ " new " + outputClassName + "(" + apl + ");\n"
						+ " return (" + outputClassName + ")singleton.get();\n"
						+ "}\n";
			}
		} else {
			result = " static " + outputClassName + " getInstance() {\n"
					+ " if (singleton==null || singleton.get()==null)\n"
					+ " singleton = new " + outputClassName + "();\n"
					+ " return (" + outputClassName + ")singleton.get();\n"
					+ " }\n";
		}
		return super.generateMethods() + result;
	}

	protected void checkPostconditions() {
		super.checkPostconditions();
		if (outputClassObject.getDeclaredConstructors().length
				!= numberOfConstructors) {
			throw new C2CException("non-Singleton constructors added");
		}
	}
}
