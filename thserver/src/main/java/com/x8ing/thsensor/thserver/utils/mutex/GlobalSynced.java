package com.x8ing.thsensor.thserver.utils.mutex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

public class GlobalSynced<T> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ReentrantLock reentrantLock = new ReentrantLock();
    private final T syncedObject;
    private final Hooks<T> hooks;


    public GlobalSynced(T syncedObject, Hooks<T> hooks) {
        this.syncedObject = syncedObject;
        this.hooks = hooks;
    }

    public void requestOperation(MutexOperation<T> m) {

        try {

            // lock and potentially wait if required
            reentrantLock.lock();

            if (hooks != null) {
                hooks.before(syncedObject);
            }

            m.operateGlobalSynced(syncedObject);

        } catch (Throwable t1) {

            try {
                if (hooks != null) {
                    hooks.after(syncedObject); // always call after
                }
            } catch (Throwable t2) {
                log.error("Error in after hook", t2);
            }


        } finally {
            reentrantLock.unlock();
        }
    }

}
