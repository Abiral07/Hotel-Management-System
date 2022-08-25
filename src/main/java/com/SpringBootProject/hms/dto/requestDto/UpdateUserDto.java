package com.SpringBootProject.hms.dto.requestDto;

import com.SpringBootProject.hms.entity.Address;
import com.SpringBootProject.hms.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDto {
    private String userName;
    @Size(min = 8, message = "password must be 8 character long")
    private String oldPassword;
    @Size(min = 8, message = "password must be 8 character long")
    private String newPassword;
    private String fullName;
    @Email(message = "Not a valid email address")
    private String email;
    @Pattern(regexp = "^(\\+\\d{1,3}(-|\\s)?)?(9\\d{9})$", message = "Enter a valid mobile no.")
    private String mobile;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @DateTimeFormat
    private LocalDate dob;
    private Address address;
}
