package CodeGeneration.C2C;

import Mopex.UQueue;

public abstract class C2CConstructor extends C2C {
	private UQueue cmdLineImports;

	protected String generateFlags() {
		return "[-notpublic] [-final] [-abstract] "
				+ "[[-import name]...] [-package name] [-output name]";
	}

	protected String generateClassNamePrefix() {
		return "";
	}

	protected void checkAndProcessArgs(Args args) {
		outputClassName = args.getFlagValue("-output");
		if (outputClassName == null) {
			if (inputClassName == null) {
				throw new C2CException("no output class name");
			} else {
				outputClassName = classNamePrefix + inputClassName;
			}
		}
		packageName = args.getFlagValue("-package");
		if (packageName == null) {
			qualifiedOutputClassName = outputClassName;
		} else {
			qualifiedOutputClassName
					= packageName + "." + outputClassName;
		}
		isNotPublic = args.hasFlag("-notpublic");
		isFinal = args.hasFlag("-final");
		isInterface = args.hasFlag("-interface");
		isAbstract = args.hasFlag("-abstract");
		cmdLineImports = args.getFlagValues("-import");
		if (outputClassName.equals(inputClassName)) {
			throw new C2CException("outputClassName = inputClassName");
		}
	}

	protected UQueue generateImports() {
		return cmdLineImports;
	}

	protected String getClassLevelJavadoc() {
		return "";
	}

	protected String getSuperclass() {
		return "";
	}

	protected UQueue generateInterfaces() {
		return new UQueue(String.class);
	}

	protected String generateFields() {
		return "";
	}

	protected String generateConstructors() {
		return "";
	}

	protected String generateMethods() {
		return "";
	}

	protected String generateNestedClasses() {
		return "";
	}

	protected void checkPostconditions() {
	}
}
