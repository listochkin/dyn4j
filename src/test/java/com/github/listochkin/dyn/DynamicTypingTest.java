package com.github.listochkin.dyn;

import static com.github.listochkin.dyn.DynamicTyping.cast;
import static com.github.listochkin.dyn.DynamicTyping.isOfType;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.listochkin.dyn.fixtures.CanFly;
import com.github.listochkin.dyn.fixtures.CanQuack;
import com.github.listochkin.dyn.fixtures.Duck;

public class DynamicTypingTest {

    @Test
    public void testIsTypeOf() {
        assertTrue(
                "Should be true for exact type name",
                isOfType(new UnsupportedOperationException(),
                        "java.lang.UnsupportedOperationException"));

        assertTrue(
                "Should be true for supertype name",
                isOfType(new UnsupportedOperationException(),
                        "java.lang.RuntimeException"));

        assertTrue("Should be true for interface name",
                isOfType(Integer.valueOf(1), "java.lang.Number"));
    }

    @Test
    public void testCast() {
        final Duck duck = new Duck();
        assertTrue(duck.fly() && duck.quack());

        final CanFly proxy = cast(duck, CanFly.class,
                CanQuack.class);
        assertTrue(proxy.fly());

        final CanQuack casted = (CanQuack) proxy;
        assertTrue(casted.quack());
    }

}
