package com.x8ing.thsensor.thserver.utils;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class BeanUtils {

    private final AutowireCapableBeanFactory beanFactory;

    public BeanUtils(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void autoWire(Object bean) {
        beanFactory.autowireBean(bean);
    }


}
