package Mopex;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class Mopex {
	public static Field findField( Class cls, String name )
			throws NoSuchFieldException {
		if ( cls != null ) {
			try {
				return cls.getDeclaredField( name );
			} catch(NoSuchFieldException e){
				return findField( cls.getSuperclass(), name );
			}
		} else {
			throw new NoSuchFieldException();
		}
	}

	public static Field[] getInstanceVariables(Class<?> c) {
		List<Field> accum = new LinkedList<>();
		while (c != null) {
			Field[] fields = c.getDeclaredFields();
			for (Field field : fields) {
				if (!Modifier.isStatic(field.getModifiers())) {
					accum.add(field);
				}
			}
			c = c.getSuperclass();
		}
		Field[] result = new Field[accum.size()];
		return accum.toArray(result);
	}

	public static String getTypeName( Class cls ){
		if ( !cls.isArray() ) {
			return cls.getName();
		} else {
			return getTypeName( cls.getComponentType() ) + "[]";
		}
	}

	public static String classArrayToString( Class[] pts ){
		String result = "";
		for ( int i = 0; i < pts.length; i++) {
			result += getTypeName( pts[i] );
			if ( i < pts.length-1 )
				result += ",";
		}
		return result;
	}

	public static String actualParametersToString( Class[] pts ){
		String result = "";
		for ( int i = 0; i < pts.length; i++) {
			result += "p" + i ;
			if ( i < pts.length-1 )
				result += ",";
		}
		return result;
	}

	public static String formalParametersToString( Class[] pts ){
		String result = "";
		for ( int i = 0; i < pts.length; i++) {
			result += getTypeName( pts[i] ) + " p" + i ;
			if ( i < pts.length-1 )
				result += ",";
		}
		return result;
	}

	public static String createRenamedConstructor( Constructor c,
			String name,
			String code )
	{
		Class[] pta = c.getParameterTypes();
		String fpl = formalParametersToString( pta );
		String apl = actualParametersToString( pta );
		Class[] eTypes = c.getExceptionTypes();
		String result = name + "(" + fpl + ")\n";
		if ( eTypes.length != 0 )
			result += " throws "
					+ classArrayToString( eTypes )
					+ "\n";
		result += "{\n super(" + apl + ");\n" + code + "}\n";
		return result;
	}
}
