package com.SpringBootProject.hms.service;

import com.SpringBootProject.hms.entity.Role;
import com.SpringBootProject.hms.exceptions.CustomException;

import java.util.List;
import java.util.Set;

public interface RoleService {
    /**
     * @return list of all roles
     */
    List<Role> getAllRole();

    /**
     * @param role body of the new role
     * @return newly added role
     * @throws CustomException exception duplicate role name
     */
    Role addNewRole(Role role) throws CustomException;

    /**
     * @param id   :id of the role
     * @param role :updated fields
     * @return updated role
     * @throws CustomException
     */
    Role updateRole(Long id, Role role) throws CustomException;

    /**
     * @param id    :id of the user
     * @param roles :Set of roles to be added
     * @return set of all roles given to the user
     */
    Set<String> addRoleToUSer(Long id, Set<String> roles);

    /**
     * @param name :name of the role
     * @return role if found having the :name
     */
    Role getRoleByName(String name);
}
