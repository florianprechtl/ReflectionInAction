package Proxy.Chaining;

import Mopex.Mopex;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public abstract class InvocationHandlerBase implements InvocationHandler {
	protected Object nextTarget;
	protected Object realTarget = null;

	InvocationHandlerBase(Object target) {
		nextTarget = target;
		if (nextTarget != null) {
			realTarget = findRealTarget(nextTarget);
			if (realTarget == null) {
				throw new RuntimeException("findRealTarget failure");
			}
		}
	}

	protected static final Object findRealTarget(Object t) {
		if (!Proxy.isProxyClass(t.getClass())) {
			return t;
		}
		InvocationHandler ih = Proxy.getInvocationHandler(t);
		if (InvocationHandlerBase.class.isInstance(ih)) {
			return ((InvocationHandlerBase) ih).getRealTarget();
		} else {
			try {
				Field f = Mopex.findField(ih.getClass(), "target");
				if (Object.class.isAssignableFrom(f.getType()) &&
						!f.getType().isArray()) {
					f.setAccessible(true); // suppress access checks
					Object innerTarget = f.get(ih);
					return findRealTarget(innerTarget);
				}
				return null;
			} catch (NoSuchFieldException e) {
				return null;
			} catch (SecurityException e) {
				return null;
			} catch (IllegalAccessException e) {
				return null;
			} // IllegalArgumentException cannot be raised
		}
	}

	protected final Object getRealTarget() {
		return realTarget;
	}
}