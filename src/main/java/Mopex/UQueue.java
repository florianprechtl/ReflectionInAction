package Mopex;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class UQueue {
	//============= F I E L D S ======================
	private List myList = new ArrayList();
	private Object eltArray;
	private Class eltType;
	private Method equalsMethod = null;

	//============= C O N S T R U C T O R S ==========
	public UQueue(Class eltType) {
		this.eltType = eltType;
		eltArray = Array.newInstance(eltType, 0);
	}

	public UQueue(Class eltType, Method m) {
		Class[] fpl = m.getParameterTypes();
		if (!(Modifier.isStatic(m.getModifiers())
				&& m.getReturnType() == boolean.class
				&& fpl[0] == eltType
				&& fpl[1] == eltType
				&& fpl.length == 2)) {
			throw new RuntimeException("illegal signature");
		}
		equalsMethod = m;
		this.eltType = eltType;
		eltArray = Array.newInstance(eltType, 0);
	}

	//============= M E T H O D S ====================
	public boolean isEmpty() {
		return myList.size() == 0;
	}

	public int size() {
		return myList.size();
	}

	public Object remove() {
		return myList.remove(0);
	}

	public Object elementAt(int i) {
		return myList.get(i);
	}

	public UQueue add(Object element) {
		if (!eltType.isInstance(element)) {
			throw new RuntimeException("illegal arg type");
		}
		if (!contains(element)) {
			myList.add(element);
		}
		return this;
	}

	public boolean contains(Object obj) {
		if (equalsMethod == null) {
			return myList.contains(obj);
		} else {
			for (int i = 0; i < myList.size(); i++) {
				try {
					Object[] apl = {obj, myList.get(i)};
					Boolean rv = (Boolean) equalsMethod.invoke(obj, apl);
					if (rv.booleanValue()) {
						return true;
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return false;
		}
	}

	public Object[] toArray() {
		return myList.toArray((Object[]) eltArray);
	}

	public String toString(String separator) {
		String result = "";
		for (int i = 0; i < myList.size(); i++) {
			result += myList.get(i);
			if (i < myList.size() - 1) {
				result += separator;
			}
		}
		return result;
	}
}

