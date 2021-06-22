package CodeGeneration.C2C;

/**
 * Is used for class-to-class transformation of a class that is passed an
 * inputClassName as last flag additionally to the conventional flags.
 * */
public abstract class C2CTransformation extends C2CConstructor {
	protected String generateFlags() {
		return super.generateFlags() + " inputClassName";
	}

	protected void checkAndProcessArgs(Args args) {
		qualifiedInputClassName = args.getLast();
		int i = qualifiedInputClassName.lastIndexOf(".");
		if (i == -1) {
			inputClassName = qualifiedInputClassName;
		} else {
			inputClassName = qualifiedInputClassName.substring(i + 1);
		}
		super.checkAndProcessArgs(args);
		try {
			inputClassObject = Class.forName(qualifiedInputClassName);
			if (inputClassObject.isArray()
					|| inputClassObject.getDeclaringClass() != null
					|| inputClassObject.isPrimitive()) {
				throw new C2CException("illegal class");
			}
		} catch (ClassNotFoundException e) {
			throw new C2CException(e);
		}
	}
}
