package com.x8ing.thsensor.thserver.utils;

import java.util.concurrent.Callable;
import java.util.function.Function;


public interface Unsafe {

    static <R, P> R execute(Function<P, R> f, P param) {
        if (f == null) {
            return null;
        }

        try {
            return f.apply(param);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static <R> R execute(Callable<R> c) {
        if (c == null) {
            return null;
        }

        try {
            return c.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    static void execute(UnsafeRunnable c) {
        if (c == null) {
            return;
        }

        try {
            c.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
