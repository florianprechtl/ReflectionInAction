package CodeGeneration.C2C;

import Mopex.UQueue;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

public abstract class C2C {
	protected String classNamePrefix;
	protected Class inputClassObject;
	protected String inputClassName = null;
	protected String outputClassName;
	protected Class outputClassObject;
	protected String packageName;
	protected String qualifiedInputClassName = null;
	protected String qualifiedOutputClassName;
	boolean isAbstract;
	boolean isFinal;
	boolean isInterface;
	boolean isNotPublic;

	protected final void setAbstract() {
		isAbstract = true;
	}

	protected final boolean isAbstract() {
		return isAbstract;
	}

	protected final void setFinal() {
		isFinal = true;
	}

	protected final boolean isFinal() {
		return isFinal;
	}

	protected final void setInterface() {
		isInterface = true;
	}

	protected final boolean isInterface() {
		return isInterface;
	}

	protected final void setNotPublic() {
		isNotPublic = true;
	}

	protected final boolean isNotPublic() {
		return isNotPublic;
	}

	public final Class createClass(String[] args) {
		classNamePrefix = generateClassNamePrefix();
		Args myArgs = new Args(args);
		checkAndProcessArgs(myArgs);
		if (!myArgs.complete()) {
			throw new C2CException("Usage: unprocessed flags: "
					+ myArgs.toString());
		}
		UQueue itQ = generateInterfaces();
		UQueue importQ = generateImports();
		String aClassString =
				(packageName == null ? "" : "package " + packageName + ";\n")
						+ (importQ.isEmpty() ? "" : "import "
						+ importQ.toString(";\nimport ")
						+ ";\n")
						+ getClassLevelJavadoc()
						+ (isNotPublic ? "" : "public ")
						+ (isFinal ? "final " : "")
						+ (isAbstract ? "abstract " : "")
						+ (isInterface ? " interface " : " class ") + outputClassName + "\n"
						+ (getSuperclass().equals("") ? "" : " extends "
						+ getSuperclass()
						+ "\n")
						+ (itQ.isEmpty() ? "" : " implements " + itQ.toString(", "))
						+ "{\n//============= F I E L D S ======================\n"
						+ generateFields()
						+ "\n//============= C O N S T R U C T O R S ==========\n"
						+ generateConstructors()
						+ "\n//============= M E T H O D S ====================\n"
						+ generateMethods()
						+ "\n//============= N E S T E D C L A S S E S ======\n"
						+ generateNestedClasses()
						+ "}\n";
		try {
			File file = new File(outputClassName + ".java");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file);
			writer.write(aClassString);
			writer.close();
			String cp = System.getProperty("java.class.path");
			Process p =
					Runtime.getRuntime().exec("javac -d target/classes/ -classpath \""
							+ cp
							+ "\" "
							+ outputClassName
							+ ".java");
			p.waitFor();
			file.delete();
			if (p.exitValue() == 0) {
				outputClassObject =
						Class.forName(qualifiedOutputClassName);
			} else {
				InputStream errStream = p.getErrorStream();
				for (int j = errStream.available(); j > 0; j--) {
					System.out.write(errStream.read());
				}
				throw new C2CException("compile fails " + p.exitValue());
			}
		} catch (Exception e) {
			throw new C2CException(e);
		}
		checkPostconditions();
		System.out.println(outputClassName + " compiled and loaded");
		return outputClassObject;
	}

	abstract protected String generateClassNamePrefix();

	abstract protected void checkAndProcessArgs(Args args);

	abstract protected UQueue generateInterfaces();

	abstract protected UQueue generateImports();

	abstract protected String getClassLevelJavadoc();

	abstract protected String getSuperclass();

	abstract protected String generateFields();

	abstract protected String generateConstructors();

	abstract protected String generateMethods();

	abstract protected String generateNestedClasses();

	abstract protected void checkPostconditions();

	abstract protected String generateFlags();
}
