package Mopex;

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
}
