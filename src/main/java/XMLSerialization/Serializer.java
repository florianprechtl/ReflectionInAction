package XMLSerialization;

import org.jdom.Document;
import org.jdom.Element;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Serializer {

	public static Field[] getInstanceVariables(Class<?> c) {
		List<Field> accum = new LinkedList<>();
		while(c != null) {
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

	public static Document serializeObject(Zoo source)
		throws Exception
	{
		return serializeHelper( source,
			new Document( new Element("serialized") ),
			new IdentityHashMap<>() );
	}
	private static Document serializeHelper( Object source,
											 Document target,
											 Map<Object, String> table )
		throws Exception
	{
		// Register target reference
		String id = Integer.toString( table.size() );
		table.put( source, id );

		// Create XML element for the sourceObject
		Class<?> sourceClass = source.getClass();
		Element oElt = new Element("object");
		oElt.setAttribute( "class", sourceClass.getName() );
		oElt.setAttribute( "id", id );

		// Add XML element to the target
		target.getRootElement().addContent(oElt);

		// Serialize Content
		if ( !sourceClass.isArray() ) {
			serializeNonArrayContent(source, target, table, sourceClass, oElt);
		}
		else {
			serializeArrayContent(source, target, table, sourceClass, oElt);
		}

		return target;
	}

	private static void serializeArrayContent(Object source, Document target, Map<Object, String> table, Class<?> sourceClass,
			Element oElt) throws Exception {
		Class<?> componentType = sourceClass.getComponentType();

		int length = Array.getLength(source);
		oElt.setAttribute( "length", Integer.toString(length) );
		for (int i=0; i<length; i++) {
			oElt.addContent( serializeVariable( componentType,
				Array.get(source,i),
					target,
					table) );
		}
	}

	private static void serializeNonArrayContent(Object source, Document target, Map<Object, String> table, Class<?> sourceClass,
			Element oElt) throws Exception {
		Field[] fields = getInstanceVariables(sourceClass);
		for (Field field : fields) {
			if (!Modifier.isPublic(field.getModifiers()))
				field.setAccessible(true);
			Element fElt = new Element("field");
			fElt.setAttribute("name", field.getName());
			Class<?> declClass = field.getDeclaringClass();
			fElt.setAttribute("declaringclass",
					declClass.getName());

			Class<?> fieldType = field.getType();
			Object child = field.get(source);

			if (Modifier.isTransient(field.getModifiers())) {
				child = null;
			}
			fElt.addContent(serializeVariable(fieldType, child,
					target, table));

			oElt.addContent(fElt);
		}
	}

	private static Element serializeVariable( Class<?> fieldType,
											  Object child,
											  Document target,
											  Map<Object, String> table)
		throws Exception
	{
		if (child == null) {
			return new Element("null");
		}
		else if (!fieldType.isPrimitive()) {
			Element reference = new Element("reference");
			if (table.containsKey(child)) {
				reference.setText(table.get(child).toString());
			}
			else {
				reference.setText( Integer.toString(table.size()) );
				serializeHelper(child, target, table);
			}
			return reference;
		}
		else {
			Element value = new Element("value");
			value.setText(child.toString());
			return value;
		}
	}
}
