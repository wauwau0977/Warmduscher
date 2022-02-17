package com.x8ing.thsensor.thserver.utils.spring;

import org.springframework.stereotype.Component;

@Component
public class StartupData {

    private long startupTimeTakenInMillis;

    public long getStartupTimeTakenInMillis() {
        return startupTimeTakenInMillis;
    }

    public void setStartupTimeTakenInMillis(long startupTimeTakenInMillis) {
        this.startupTimeTakenInMillis = startupTimeTakenInMillis;
    }
}
