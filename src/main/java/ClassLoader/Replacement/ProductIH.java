package ClassLoader.Replacement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProductIH implements InvocationHandler {
	static private Class[] productAInterfaces = {Product.class};
	private Product target = null;

	private ProductIH(AbstractProduct obj) {
		target = obj;
	}

	public static Product newInstance(AbstractProduct obj) {
		return (Product) Proxy.newProxyInstance(obj.getClass().getClassLoader(),
				productAInterfaces,
				new ProductIH(obj));
	}

	public Product getTarget() {
		return target;
	}

	public void setTarget(Product x) {
		target = x;
	}

	public Object invoke(Object t, Method m, Object[] args)
			throws Throwable {
		Object result = null;
		try {
			result = m.invoke(target, args);
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
		return result;
	}

	public static void main(String[] args)
			throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
		AbstractProduct.reload("");
		Product p = AbstractProduct.newInstance();
		AbstractProduct.reload("");
		Product p2 = AbstractProduct.newInstance();
		AbstractProduct.reload("");
		Product p3 = AbstractProduct.newInstance();
		AbstractProduct.reload("");
		Product p4 = AbstractProduct.newInstance();
		AbstractProduct.reload("");
		Product p5 = AbstractProduct.newInstance();
		AbstractProduct.reload("");
		Product p6 = AbstractProduct.newInstance();
	}
}
