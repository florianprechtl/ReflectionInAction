package CodeGeneration.C2C;

public abstract class C2CTransformation extends C2CConstructor {
	protected String generateFlags() {
		return super.generateFlags() + " inputClassName";
	}
	protected void checkAndProcessArgs( Args args ){
		qualifiedInputClassName = args.getLast();
		int i = qualifiedInputClassName.lastIndexOf(".");
		if ( i == -1 )
			inputClassName = qualifiedInputClassName;
		else
			inputClassName = qualifiedInputClassName.substring(i+1);
		super.checkAndProcessArgs( args );
		try {
			inputClassObject = Class.forName( qualifiedInputClassName );
			if ( inputClassObject.isArray()
					|| inputClassObject.getDeclaringClass() != null
					|| inputClassObject.isPrimitive() )
				throw new C2CException("illegal class");
		} catch (ClassNotFoundException e) {
			throw new C2CException(e);
		}
	}
}
