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
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {
    @NotEmpty(message = "UseName cannot be empty")
    private String userName;
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8,message = "password must be 8 character long")
    private String password;
    private String fullName;
    @Email(message = "Not a valid email address")
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    @NotEmpty(message = "Mobile no. cannot be empty")
    @Pattern(regexp = "^(\\+\\d{1,3}(-|\\s)?)?(9\\d{9})$",message = "Enter a valid mobile no.")
    private String mobile;
    @NotNull(message = "Gender cannot be empty")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @DateTimeFormat
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    private LocalDate dob;
    @Min(value = 18, message = "Age should be greater than 18")
    @Max(value = 150, message = "Age cannot be more than 150")
    private Integer age;
    private Address address;
}
