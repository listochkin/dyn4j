Dyn4j
=====

Simple duck typing for Java.

Why dyn4j
---------

Imagine that you write normal Java code:

    import static com.google.common.io.Closeables.closeQuietly;
    ...
    Scanner scanner = ...

    ...
    closeQuietly(scanner);

And then **ouch**: [`java.util.Scanner`][1] doesn't implement [`java.io.Closeable`][2] in Java 6 although it does have a `close()` method! You wish you could write something like this:

    closeQuietly(Closeable.class.cast(scanner)); // won't compile

Well, with **dyn4j** now you can!

    import static com.github.listochkin.dyn.DynamicTyping.cast;
    ...
    closeQuietly(cast(scanner, Closeable.class));

What dyn4j is
-------------

**Dyn4j** is a very small and simple library for dynamic casting and patching your Java objects. It relies on [Dynamic Proxies from Java SE][3] and should work on every compatible platform. Note that it doesn't change the target object itself, but gives you a reference to a new proxy object that behaves as you expect.

**Dyn4j** was created in need and inspired by [interfaces in Go programming language][4].

API
---

 1. `cast`

    `Type casted = cast(target, Type.class);`

    Casts your `target` object to a given type. Your `target` object _doesn't have to implement all members of_ `Type`, only those methods that you _actually call_. So, in practice `cast()` is applicable in many cases.

 2. `patch`

    `Type patched = patch(target, patch, Type.class, Other.class);`

     a. You can list as many types as you want.
     b. You don't need to specify `Type.class` if `target` extends/implements `Type`.
     c. `this` inside methods of `patch` points to `patch`. Use `target` to refer to original object.

    Gives you a reference to patched proxy. When `patched.method()` is called the proxy tries calling `patch.method()` and if it doesn't exist calls `target.method()`.

 3. `isOfType`

    `boolean b = isOfType(object, "my.type.Name")`

    Usefull for checking types dynamically. In large projects with complex structure some types might not be visible at compile time or inside an IDE. This method helps in such cases; but in general it's a sign of trouble if you need one.

Notes
-----

First of all, you can cast patched objects and patch casted ones. Mix and match `cast` and `patch` as you see fit. But be aware that although dynamic proxies in Java are very efficient and can be optimized to simple direct calls at runtime in general JVM is not designed for dynamic method resolution. So, use **dyn4j** sparingly and switch to explicit descendant types if performance becomes a problem.

Plans
-----

I consider **dyn4j** done. However, I wish to spend some time with [`invokedynamic` API from Java 7][5]. As always, pull requests are welcomed!

Installation
------------

With Maven add the following to your `pom.xml`:

    <repositories>
        <repository>
            <id>dyn4j-mvn-repo</id>
            <url>https://raw.github.com/listochkin/dyn4j/mvn-repo/</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>

    <dependency>
        <groupId>com.github.listochkin</groupId>
        <artifactId>dyn4j</artifactId>
        <version>0.0.2</version>
    </dependency>

If you don't use Maven then modify your project file accordingly. [Jars are available for download from Github repo.][6]


  [1]: http://docs.oracle.com/javase/6/docs/api/java/util/Scanner.html
  [2]: http://docs.oracle.com/javase/6/docs/api/java/io/Closeable.html
  [3]: http://docs.oracle.com/javase/6/docs/api/java/lang/reflect/Proxy.html
  [4]: http://research.swtch.com/interfaces
  [5]: http://docs.oracle.com/javase/7/docs/api/java/lang/invoke/package-summary.html
  [6]: https://github.com/listochkin/dyn4j/tree/mvn-repo/com/github/listochkin/dyn4j/
