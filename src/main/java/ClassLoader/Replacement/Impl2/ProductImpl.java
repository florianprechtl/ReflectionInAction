package ClassLoader.Replacement.Impl2;

import ClassLoader.Replacement.AbstractProduct;

public class ProductImpl extends AbstractProduct {
	public static Object evolve(Object o) {
		System.out.println("Impl2");
		return o;
	}
}
