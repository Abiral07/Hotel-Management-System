package com.SpringBootProject.hms.service;

import com.SpringBootProject.hms.entity.Role;
import com.SpringBootProject.hms.entity.Room;
import com.SpringBootProject.hms.exceptions.CustomException;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> getAllRole();

    Role addNewRole(Role role) throws CustomException;

    Role updateRole(Long id, Role role) throws CustomException;

    Set<String> addRoleToUSer(Long id, Set<String> roles);

    Role getRoleByName(String name);
}
