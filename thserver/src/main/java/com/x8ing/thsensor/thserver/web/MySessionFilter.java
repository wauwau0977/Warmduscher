package com.x8ing.thsensor.thserver.web;

import com.x8ing.thsensor.thserver.db.dao.SessionDeviceRepository;
import com.x8ing.thsensor.thserver.db.entity.SessionDevice;
import com.x8ing.thsensor.thserver.utils.UUIDUtils;
import com.x8ing.thsensor.thserver.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;


@Component
public class MySessionFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public final static String TH_SERVER_SESSION_ID = "TH-SERVER-SESSION-ID";

    private final SessionDeviceRepository sessionDeviceRepository;

    public MySessionFilter(SessionDeviceRepository sessionDeviceRepository) {
        this.sessionDeviceRepository = sessionDeviceRepository;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        Cookie cookie = WebUtils.getCookie(request, TH_SERVER_SESSION_ID);

        if (cookie == null) {
            cookie = new Cookie(TH_SERVER_SESSION_ID, UUIDUtils.generateShortTextUUID());
            cookie.setHttpOnly(false);
            cookie.setMaxAge(60 * 60 * 24 * 365); // 1Y
            cookie.setPath("/");
        }

        String sessionId = cookie.getValue();

        // save only relevant requests
        if (isSessionRelevantRequest(request)) {

            // need to save it
            SessionDevice sessionDevice = new SessionDevice();
            sessionDevice.setSessionId(sessionId);
            sessionDevice.setIp(Utils.getRequestIP(request));
            sessionDevice.setAgentString(request.getHeader("User-Agent"));
            sessionDevice.setClientId(getClientId(request));

            sessionDeviceRepository.findById(sessionId).ifPresentOrElse(
                    device -> log.debug("Is already in DB"),
                    () -> {
                        log.info("Need to create a session device, as it did not exist." + sessionDevice);
                        sessionDeviceRepository.save(sessionDevice);
                    });
        }

        //add cookie to response
        response.addCookie(cookie);

        // keep going with the filters
        chain.doFilter(request, response);
    }

    public static String getClientId(HttpServletRequest request) {
        return request.getHeader(TH_SERVER_SESSION_ID);
    }

    public static boolean isSessionRelevantRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path != null
                && Stream.of("html", "htm", "css", "png", "svg", "woff", "woff2", "json", "js").noneMatch(s -> StringUtils.endsWithIgnoreCase(path, s))
                && !StringUtils.equals("/", path)
                && !StringUtils.equals("/pi11", path)
                && !StringUtils.equals("/pi11/", path)
                && !StringUtils.equalsIgnoreCase(request.getMethod(), HttpMethod.OPTIONS.name()) // ignore preflight requests
                ;
    }

    @Override
    public void destroy() {

    }
}