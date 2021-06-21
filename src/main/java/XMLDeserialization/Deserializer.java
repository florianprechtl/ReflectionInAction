package XMLDeserialization;

import XMLSerialization.Animal;
import XMLSerialization.Serializer;
import XMLSerialization.Zoo;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class Deserializer {

	public static void main(String[] args) {
		Animal panda1 = new Animal();
		panda1.initialize("Tian Tian",
				"male",
				"Ailuropoda melanoleuca",
				271);
		Animal panda2 = new Animal();
		panda2.initialize("Mei Xiang",
				"female",
				"Ailuropoda melanoleuca",
				221);
		Zoo national = new Zoo();
		national.initialize("National Zoological Park",
				"Washington, D.C.");
		national.add(panda1);
		national.add(panda2);
		try {
			XMLOutputter out = new XMLOutputter("\t", true);
			Document d = Serializer.serializeObject(national);
			out.output(d, System.out);

			Object o = deserializeObject(d);
			System.out.println(o);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Object deserializeObject(Document source)
			throws Exception {
		List<? extends Element> objList = source.getRootElement().getChildren();
		Map<String, Object> table = new HashMap<String, Object>();
		createInstances(table, objList);
		assignFieldValues(table, objList);
		return table.get("0");
	}

	private static void createInstances(Map<String, Object> table, List<? extends Element> objList)
			throws Exception {
		for (Element oElt : objList) {
			Class<?> cls = Class.forName(oElt.getAttributeValue("class"));
			Object instance = null;
			if (!cls.isArray()) {
				Constructor<?> c = cls.getDeclaredConstructor();
				if (!Modifier.isPublic(c.getModifiers())) {
					c.setAccessible(true);
				}
				instance = c.newInstance();
			} else {
				instance =
						Array.newInstance(
								cls.getComponentType(),
								Integer.parseInt(oElt.getAttributeValue("length")));
			}
			table.put(oElt.getAttributeValue("id"), instance);
		}
	}

	private static void assignFieldValues(Map<String, Object> table, List<? extends Element> objList)
			throws Exception {
		for (Element oElt : objList) {
			Object instance = table.get(oElt.getAttributeValue("id"));
			List<? extends Element> fElts = oElt.getChildren();
			if (!instance.getClass().isArray()) {
				for (Object elt : fElts) {
					Element fElt = (Element) elt;
					String className
							= fElt.getAttributeValue("declaringclass");
					Class<?> fieldDC = Class.forName(className);
					String fieldName = fElt.getAttributeValue("name");
					Field f = fieldDC.getDeclaredField(fieldName);
					if (!Modifier.isPublic(f.getModifiers())) {
						f.setAccessible(true);
					}
					Element vElt = (Element) fElt.getChildren().get(0);
					f.set(instance,
							deserializeValue(vElt, f.getType(), table));
				}
			} else {
				Class<?> compType =
						instance.getClass().getComponentType();
				for (int j = 0; j < fElts.size(); j++) {
					Array.set(instance, j,
							deserializeValue((Element) fElts.get(j),
									compType, table));
				}
			}
		}
	}

	private static Object deserializeValue(Element vElt,
			Class<?> fieldType,
			Map<String, Object> table)
			throws ClassNotFoundException {
		String valType = vElt.getName();
		if (valType.equals("null")) {
			return null;
		} else if (valType.equals("reference")) {
			return table.get(vElt.getText());
		} else {
			if (fieldType.equals(boolean.class)) {
				if (vElt.getText().equals("true")) {
					return Boolean.TRUE;
				} else {
					return Boolean.FALSE;
				}
			} else if (fieldType.equals(byte.class)) {
				return Byte.valueOf(vElt.getText());
			} else if (fieldType.equals(short.class)) {
				return Short.valueOf(vElt.getText());
			} else if (fieldType.equals(int.class)) {
				return Integer.valueOf(vElt.getText());
			} else if (fieldType.equals(long.class)) {
				return Long.valueOf(vElt.getText());
			} else if (fieldType.equals(float.class)) {
				return Float.valueOf(vElt.getText());
			} else if (fieldType.equals(double.class)) {
				return Double.valueOf(vElt.getText());
			} else if (fieldType.equals(char.class)) {
				return vElt.getText().charAt(0);
			} else {
				return vElt.getText();
			}
		}
	}
}
