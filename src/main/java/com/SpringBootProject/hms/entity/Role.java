package com.SpringBootProject.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @NotNull(message = "Role name must not be null")
    @Column(name = "role_name", unique = true, nullable = false)
    private String roleName;
    @Size(min = 5, max = 100, message = "size of description must be between 10-100")
    @Column(name = "description", length = 100)
    private String roleDescription;

    @Override
    public String toString() {
        return this.roleName;
    }
}
