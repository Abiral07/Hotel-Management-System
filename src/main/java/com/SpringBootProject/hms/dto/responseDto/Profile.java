package com.SpringBootProject.hms.dto.responseDto;

import com.SpringBootProject.hms.entity.Gender;
import com.SpringBootProject.hms.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    private long uid;
    private String userName;
    private String fullName;
    private String email;
    private String mobile;
    private Gender gender;
    @DateTimeFormat
    private LocalDate dob;
    private Integer age;
    private Boolean isActive;
    private Set<String> roles;

    public Profile(Users user) {
        this.uid = user.getUid();
        this.userName = user.getUsername();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.mobile = user.getMobile();
        this.gender = user.getGender();
        this.dob = user.getDob();
        this.age = user.getAge();
        this.isActive = user.getIsActive();
        this.roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
