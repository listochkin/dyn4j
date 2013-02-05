package com.github.listochkin.dyn;

public class DynamicTyping {

    public static boolean isOfType(final Object object, final String typeName) {
        try {
            return Class.forName(typeName).isInstance(object);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
