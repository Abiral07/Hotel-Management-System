package com.SpringBootProject.hms.Intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ReservationServiceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle
            (HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("******************************************************************************");
        System.out.println("Pre Handle method is Calling");
        System.out.println("Remote Host: "+ request.getRemoteHost());
        System.out.println("Remote Address: "+ request.getRemoteAddr());
        System.out.println("Remote User: "+ request.getRemoteUser());
        System.out.println("Requested SessionId: "+ request.getRequestedSessionId());
        System.out.println("SESSION CRESTED TIME: "+ request.getSession().getCreationTime());
        System.out.println("******************************************************************************");
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("******************************************************************************");
        System.out.println("Post Handle method is Calling");
        System.out.println("SESSION LAST ACCESS TIME: "+ request.getSession().getLastAccessedTime());
        System.out.println("******************************************************************************");
    }
    @Override
    public void afterCompletion
            (HttpServletRequest request, HttpServletResponse response, Object
                    handler, Exception exception) throws Exception {
        System.out.println("******************************************************************************");
        System.out.println("Request and Response is completed");
        System.out.println("******************************************************************************");
    }
}