package com.x8ing.thsensor.thserver.web;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse r, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) r;

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, *, X-Requested-With"); // X-Requested-With avoid not allowed
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}