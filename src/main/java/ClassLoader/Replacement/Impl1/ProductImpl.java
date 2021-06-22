package ClassLoader.Replacement.Impl1;

import ClassLoader.Replacement.AbstractProduct;

public class ProductImpl extends AbstractProduct {
	public static Object evolve(Object o) {
		System.out.println("Impl1");
		return o;
	}
}
