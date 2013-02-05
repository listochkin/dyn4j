package com.github.listochkin.dyn;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicTyping {

    public static boolean isOfType(final Object object, final String typeName) {
        try {
            return Class.forName(typeName).isInstance(object);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static final class ProxyHandler implements InvocationHandler {
        private final Object target;

        private ProxyHandler(final Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(final Object proxy, final Method method,
                final Object[] params) throws Throwable {
            return target.getClass()
                    .getMethod(method.getName(), method.getParameterTypes())
                    .invoke(target, params);
        }
    }

    public static <T> T cast(final Object target, final Class<T> type,
            final Class<?>... additionalInterfaces) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(),
                concat(type, additionalInterfaces), new ProxyHandler(target)));
    }

    private static <T> T[] concat(final T first, final T[] rest) {
        if (rest == null) {
            @SuppressWarnings("unchecked")
            final T[] result = (T[]) Array.newInstance(first.getClass(), 1);
            result[0] = first;
            return result;
        } else {
            @SuppressWarnings("unchecked")
            final T[] result = (T[]) Array.newInstance(rest.getClass()
                    .getComponentType(), rest.length + 1);
            System.arraycopy(rest, 0, result, 1, rest.length);
            result[0] = first;
            return result;
        }
    }

}
