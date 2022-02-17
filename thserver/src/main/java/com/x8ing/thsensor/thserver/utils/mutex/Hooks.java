package com.x8ing.thsensor.thserver.utils.mutex;

public interface Hooks<T> {

    void before(T t) throws  Throwable;

    void after(T t) throws Throwable;
}
