package com.SpringBootProject.hms.service.Impl;

import com.SpringBootProject.hms.entity.Role;
import com.SpringBootProject.hms.entity.Users;
import com.SpringBootProject.hms.exceptions.CustomException;
import com.SpringBootProject.hms.exceptions.ResourceNotFoundException;
import com.SpringBootProject.hms.repo.RoleRepo;
import com.SpringBootProject.hms.repo.UserRepo;
import com.SpringBootProject.hms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private RoleRepo roleRepository;
    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public Role addNewRole(Role role) throws CustomException {
        if(roleRepository.findByRoleName(role.getRoleName()) != null){
            throw new CustomException("Role already exist. please choose different name");
        }
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Long id,Role role) throws CustomException {
        Role role1 = roleRepository.findDistinctByRoleId(id);
        if(role1 != null){
            if(Objects.nonNull(role.getRoleName()) && !"".equalsIgnoreCase(role.getRoleName()))
                role1.setRoleName(role.getRoleName());
            if(Objects.nonNull(role.getRoleDescription()) && !"".equalsIgnoreCase(role.getRoleDescription()))
                role1.setRoleDescription(role.getRoleDescription());
            return roleRepository.save(role1);
        }
        throw new CustomException("Role not found");
    }

    @Override
    public Set<String> addRoleToUSer(Long id, Set<String> roles) {
        Users user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));
        roles.forEach(role->{
            role = role.toUpperCase();
            Role roleFromDb = roleRepository.findByRoleName(role);
            if(roleFromDb != null) {
                user.addRole(roleFromDb);
            }else{
                try {
                    throw new CustomException("Could not find "+ role +" role. Please create the role before adding it to user");
                } catch (CustomException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        userRepository.save(user);
        return roleRepository.findRoleOfUser(user.getUid());
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByRoleName(name);
    }


}
