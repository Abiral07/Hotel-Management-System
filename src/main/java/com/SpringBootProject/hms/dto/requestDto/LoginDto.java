package com.SpringBootProject.hms.dto.requestDto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class LoginDto implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

    @NotEmpty(message = "UserName cannot be empty")
    private String userName;
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8,message = "password must be 8 character long")
    private String password;
}
