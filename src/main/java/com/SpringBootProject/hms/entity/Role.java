package com.SpringBootProject.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    @NotNull
    @Column(name = "role_name",unique = true,nullable = false)
    private String roleName;
    @Column(name = "description", length = 100)
    private String roleDescription;

    @Override
    public String toString() {
        return this.roleName;
    }
}
