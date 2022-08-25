package com.SpringBootProject.hms.controller;

import com.SpringBootProject.hms.constants.PathConstant;
import com.SpringBootProject.hms.entity.Role;
import com.SpringBootProject.hms.exceptions.CustomException;
import com.SpringBootProject.hms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(value = "*")
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping(PathConstant.GET_ALL_ROLES)
    public ResponseEntity<List<Role>> getAllRole(){
        return ResponseEntity.ok(roleService.getAllRole());
    }
    @GetMapping(PathConstant.GET_ROLE_BY_NAME)
    public ResponseEntity<Role> getAllRole(@PathVariable("name")String name){
        return ResponseEntity.ok(roleService.getRoleByName(name));
    }

    @PostMapping(PathConstant.ADD_ROLE)
    public ResponseEntity<Role> addNewRole(@Valid @RequestBody Role role) throws CustomException {
        return new ResponseEntity<>(roleService.addNewRole(role), HttpStatus.CREATED);
    }
    @PostMapping(PathConstant.UPDATE_ROLE)
    public ResponseEntity<Role> updateRole(@PathVariable("id") Long id, @RequestBody Role role) throws CustomException {
        return new ResponseEntity<>(roleService.updateRole(id, role), HttpStatus.CREATED);
    }
    @PostMapping(PathConstant.ADD_ROLE_TO_USER)
    public ResponseEntity<Set<String>> addRoleToUser(@PathVariable("id")Long id, @RequestBody Set<String> roles){
        return ResponseEntity.ok(roleService.addRoleToUSer(id,roles));
    }
}
