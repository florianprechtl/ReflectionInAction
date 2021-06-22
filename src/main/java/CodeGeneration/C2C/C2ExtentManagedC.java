package CodeGeneration.C2C;

import Mopex.Mopex;
import Mopex.UQueue;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class C2ExtentManagedC extends C2CTransformation {
	private int numberOfConstructors = 0;

	static public void main(String[] args) throws InvocationTargetException, IllegalAccessException {
		Class c = new C2ExtentManagedC().createClass(args);
		Method inheritedMethod = c.getSuperclass().getDeclaredMethods()[0];
		inheritedMethod.invoke(null, new String[] {null});

		Method m = c.getDeclaredMethods()[0];
		try {
			c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		Object[] o = (Object[]) m.invoke(null);
		try {
			c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		Object[] o2 = (Object[]) m.invoke(null);
	}

	protected UQueue generateImports() {
		return super.generateImports()
				.add("java.util.Vector")
				.add("java.lang.ref.*");
	}

	protected String generateClassNamePrefix() {
		return "ExtentManaged" + super.generateClassNamePrefix();
	}

	protected String getSuperclass() {
		return inputClassName;
	}

	protected void checkAndProcessArgs(Args args) {
		super.checkAndProcessArgs(args);
		//TODO: Handle Serializable by overriding readObject
//		if (Serializable.class.isAssignableFrom(inputClassObject)) {
//			throw new C2CException("refuse Serializable input classes");
//		}
		if (Cloneable.class.isAssignableFrom(inputClassObject)) {
			throw new C2CException("Cloneable and Singleton conflict");
		}
	}

	protected String generateFields() {
		return super.generateFields()
				+ " static private Vector myExtent = new Vector();\n";
	}

	protected String generateConstructors() {
		String managementCode =
				" myExtent.add( new WeakReference(this) );\n";
		String overriddenConstructors = "";
		Constructor[] cArray = inputClassObject.getDeclaredConstructors();
		if (cArray.length != 0) {
			for (int i = 0; i < cArray.length; i++) {
				overriddenConstructors
						+= Modifier.toString(cArray[i].getModifiers())
						+ " "
						+ Mopex.createRenamedConstructor(cArray[i],
						outputClassName,
						managementCode);
			}
			numberOfConstructors = cArray.length;
		} else {
			overriddenConstructors = outputClassName
					+ "()\n {\n"
					+ managementCode
					+ " }\n";
			numberOfConstructors = 1;
		}
		return super.generateConstructors() + overriddenConstructors;
	}

	protected String generateMethods() {
		return super.generateMethods()
				+ " static public " + outputClassName + "[] getExtent() {\n"
				+ " Vector extent = new Vector();\n"
				+ " for (int i = myExtent.size()-1, j = 0; i >= 0; i--) {\n"
				+ " " + outputClassName + " anObj = \n"
				+ " (" + outputClassName + ")\n"
				+ " ((WeakReference)myExtent.elementAt(i)).get();\n"
				+ " if ( anObj != null )\n"
				+ " extent.add(anObj);\n"
				+ " else\n"
				+ " myExtent.remove(i);\n"
				+ " }\n"
				+ " return (" + outputClassName + "[])\n"
				+ " extent.toArray( new " + outputClassName + "[1]);\n"
				+ " }\n";
	}

	protected void checkPostconditions() {
		super.checkPostconditions();
		if (outputClassObject.getDeclaredConstructors().length
				!= numberOfConstructors) {
			throw new C2CException("non-ExtentManaged constructors added"
			);
		}
	}
}