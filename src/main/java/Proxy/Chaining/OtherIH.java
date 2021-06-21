package Proxy.Chaining;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class OtherIH extends InvocationHandlerBase {
	private OtherIH(Object obj) {
		super(obj);
	}

	public static Object createProxy(Object obj) {
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
				obj.getClass().getInterfaces(),
				new OtherIH(obj));
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		System.out.println("other before");
		result = method.invoke(nextTarget, args);
		System.out.println("other after");
		return result;
	}
}
