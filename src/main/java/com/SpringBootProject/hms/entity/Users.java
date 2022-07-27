package com.SpringBootProject.hms.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private Long uid;
    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "email",nullable = false, unique = true, length = 5000)
    private String email;
    @Column(name = "mobile",nullable = false, unique = true,length = 3072)
    private String mobile;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "dob")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    private LocalDate dob;
    @Column(name = "age")
    @Min(value = 18, message = "Age should be greater than 18")
    @Max(value = 150, message = "Age cannot be more than 150")
    private Integer age;
    @Column(name = "isactive")
    private Boolean isActive;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_role",
            joinColumns = {@JoinColumn(name = "uid")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    @JoinColumn(name= "address" ,referencedColumnName = "aid")
    private Address address;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        HashSet<SimpleGrantedAuthority> set = new HashSet<>();
        this.getRoles().forEach(role ->{
            set.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        });
        return set;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addRole(Role roleFromDb) {
        this.roles.add(roleFromDb);
    }
}
