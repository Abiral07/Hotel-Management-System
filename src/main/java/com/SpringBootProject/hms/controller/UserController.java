package com.SpringBootProject.hms.controller;

import com.SpringBootProject.hms.constants.PathConstant;
import com.SpringBootProject.hms.dto.requestDto.LoginDto;
import com.SpringBootProject.hms.dto.requestDto.UserRequestDto;
import com.SpringBootProject.hms.dto.responseDto.LoginResponseJWT;
import com.SpringBootProject.hms.dto.responseDto.UserResponseDto;
import com.SpringBootProject.hms.exceptions.CustomException;
import com.SpringBootProject.hms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping(PathConstant.REGISTRATION)
    public ResponseEntity<UserResponseDto> registerUser(@Validated @RequestBody UserRequestDto customers) throws CustomException {
        return new ResponseEntity<>(userService.registerUser(customers), HttpStatus.CREATED);
    }

    @PostMapping(PathConstant.LOGIN)
    public ResponseEntity<LoginResponseJWT> userLogin(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(userService.userLogin(request,loginDto));
    }

    @GetMapping(PathConstant.GET_ALL_USERS)
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.ACCEPTED);
    }

    @GetMapping(PathConstant.GET_USER_BY_ID)
    public ResponseEntity<UserResponseDto> getAllUsers(@PathVariable("id")Long id){
        return new ResponseEntity<>(userService.getUserById(id),HttpStatus.ACCEPTED);
    }

    @GetMapping(PathConstant.GET_USER_BY_NAME)
    public ResponseEntity<List<UserResponseDto>> getAllUsers(@PathVariable("name") String name) {
        return new ResponseEntity<>(userService.getUserByName(name), HttpStatus.ACCEPTED);
    }

    @PostMapping(PathConstant.UPDATE_USER)
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserRequestDto customers) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, CustomException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeySpecException, InvalidKeyException {
        return ResponseEntity.ok(userService.updateUser(id, customers));
    }

    @GetMapping(PathConstant.REFRESH_TOKEN)
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(new LoginResponseJWT(userService.refreshToken(request)));
    }

}
