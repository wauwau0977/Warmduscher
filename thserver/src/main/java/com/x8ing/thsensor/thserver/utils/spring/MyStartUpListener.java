package com.x8ing.thsensor.thserver.utils.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Duration;

/**
 * Register in src/main/resources/META-INF/spring.factories
 * org.springframework.boot.SpringApplicationRunListener= com.x8ing.thsensor.thserver.utils.spring.MyStartUpListener
 */
public class MyStartUpListener implements SpringApplicationRunListener {


    public MyStartUpListener(SpringApplication application, String[] args) {
        super();
    }


    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        StartupData startupData = context.getBean(StartupData.class);
        startupData.setStartupTimeTakenInMillis(timeTaken.toMillis());
    }
}
