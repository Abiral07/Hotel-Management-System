package com.SpringBootProject.hms.event.listener;

import com.SpringBootProject.hms.dto.responseDto.UserResponseDto;
import com.SpringBootProject.hms.event.RegistrationCompleteEvent;
import com.SpringBootProject.hms.utils.jwt.JwtTokenUtil;
import com.SpringBootProject.hms.utils.jwt.JwtUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RegistrationCompleteListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //create the verification token for user with link
        UserResponseDto user = event.getUser();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
        String token = jwtTokenUtil.generateToken(userDetails);
        String url = event.getUrl() + "/verifyRegistration?token=" + token;
        //send mail to user
        log.info("Click the given link to activate user: {}", url);
    }
}
