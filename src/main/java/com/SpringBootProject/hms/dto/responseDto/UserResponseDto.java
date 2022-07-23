package com.SpringBootProject.hms.dto.responseDto;

import com.SpringBootProject.hms.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String userName;
    private String fullName;
    private String email;
    private String mobile;
//    @Enumerated(EnumType.STRING)
    private Gender gender;
    @DateTimeFormat
    private LocalDate dob;
    private Integer age;
//    private Long addressId;
}
