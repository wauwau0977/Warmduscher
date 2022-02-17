package com.x8ing.thsensor.thserver.web;

import com.x8ing.thsensor.thserver.db.dao.SessionRequestRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Attention: DO NOT ADD... It will override and destroy the applicaiton properties settings
// @EnableWebMvc
// https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.servlet.spring-mvc.auto-configuration

@Configuration
@Component
public class MvcConfig implements WebMvcConfigurer {

    private final SessionRequestRepository sessionRequestRepository;

    public MvcConfig(SessionRequestRepository sessionRequestRepository) {
        super();
        this.sessionRequestRepository = sessionRequestRepository;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new MyRequestInterceptor(sessionRequestRepository));
    }

}
