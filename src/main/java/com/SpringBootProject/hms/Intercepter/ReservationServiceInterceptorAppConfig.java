package com.SpringBootProject.hms.Intercepter;

import com.SpringBootProject.hms.config.WebSecurityConfig;
import com.SpringBootProject.hms.constants.PathConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class ReservationServiceInterceptorAppConfig extends WebMvcConfigurerAdapter {
    @Autowired
    ReservationServiceInterceptor reservationServiceInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(reservationServiceInterceptor)
                .addPathPatterns(
                        PathConstant.GET_ALL_RESERVATION,
                        PathConstant.UPDATE_RESERVATION
                        );
    }
}
