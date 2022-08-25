package com.SpringBootProject.hms.event;

import com.SpringBootProject.hms.dto.responseDto.UserResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private UserResponseDto user;
    private String url;

    public RegistrationCompleteEvent(UserResponseDto user, String url) {
        super(user);
        this.user = user;
        this.url = url;
    }
}
