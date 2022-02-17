package com.x8ing.thsensor.thserver.utils.mutex;

public interface MutexOperation<T> {

    void operateGlobalSynced(T t) throws Throwable;

}
