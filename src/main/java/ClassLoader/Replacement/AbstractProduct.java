package ClassLoader.Replacement;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import ClassLoader.SimpleClassLoader;

public abstract class AbstractProduct implements Product {
	static private ClassLoader cl = null;
	static private String directory = null;
	static private Class implClass;
	static private List instances = new ArrayList();

	public static Product newInstance()
			throws InstantiationException, IllegalAccessException {
		AbstractProduct obj = (AbstractProduct) implClass.newInstance();
		Product anAProxy = (Product) ProductIH.newInstance(obj);
		instances.add(new WeakReference(anAProxy));
		return anAProxy;
	}

	public static void reload(String dir)
			throws ClassNotFoundException,
			InstantiationException,
			IllegalAccessException,
			NoSuchMethodException,
			InvocationTargetException {
		cl = new SimpleClassLoader(dir);

		int index = (int)(Math.random() * 2) +  1;
		System.out.println("Change Impl");

		implClass = cl.loadClass("ClassLoader.Replacement.Impl" + index + ".ProductImpl");
		if (directory == null) {
			directory = dir;
			return;
		}
		directory = dir;
		List newInstances = new ArrayList();
		Method evolve = implClass.getDeclaredMethod("evolve", new Class[]{Object.class});
		for (Object instance : instances) {
			Proxy x = (Proxy) ((WeakReference) instance).get();
			if (x != null) {
				ProductIH aih = (ProductIH) Proxy.getInvocationHandler(x);
				Product oldObject = aih.getTarget();
				Product replacement
						= (Product) evolve.invoke(null,
						new Object[]{oldObject});
				aih.setTarget(replacement);
				newInstances.add(new WeakReference(x));
			}
		}
		instances = newInstances;
	}
}
