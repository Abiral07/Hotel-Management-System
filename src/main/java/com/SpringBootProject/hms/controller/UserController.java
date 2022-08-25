package com.SpringBootProject.hms.controller;

import com.SpringBootProject.hms.constants.PathConstant;
import com.SpringBootProject.hms.dto.requestDto.LoginDto;
import com.SpringBootProject.hms.dto.requestDto.UpdateUserDto;
import com.SpringBootProject.hms.dto.requestDto.UserRequestDto;
import com.SpringBootProject.hms.dto.responseDto.LoginResponseJWT;
import com.SpringBootProject.hms.dto.responseDto.Profile;
import com.SpringBootProject.hms.dto.responseDto.StringResponse;
import com.SpringBootProject.hms.dto.responseDto.UserResponseDto;
import com.SpringBootProject.hms.event.RegistrationCompleteEvent;
import com.SpringBootProject.hms.exceptions.CustomException;
import com.SpringBootProject.hms.service.UserService;
import com.SpringBootProject.hms.utils.jwt.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private ApplicationEventPublisher publisher;


    @PostMapping(PathConstant.REGISTRATION)
    public ResponseEntity<UserResponseDto> registerUser(@Validated @RequestBody UserRequestDto customers, final HttpServletRequest request) throws CustomException {
        UserResponseDto user = userService.registerUser(customers);
        //sending verification mail event
        publisher.publishEvent(new RegistrationCompleteEvent(user, "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()));
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping(PathConstant.VERIFY_REGISTRATION)
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "User Verified Successfully";
        }
        throw new CustomException("Bad User");
    }

    @PostMapping(PathConstant.LOGIN)
    public ResponseEntity<LoginResponseJWT> userLogin(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(userService.userLogin(request, loginDto));
    }

    @GetMapping(PathConstant.GET_ALL_USERS)
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.ACCEPTED);
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
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserDto customers) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, CustomException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeySpecException, InvalidKeyException {
        return ResponseEntity.ok(userService.updateUser(id, customers));
    }

    @GetMapping(PathConstant.REFRESH_TOKEN)
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(userService.refreshToken(request));
    }

    @GetMapping(PathConstant.CURRENT_USER)
    public Profile getCurrentUser(Principal principal) {
        System.out.println(principal.getName());
        return userService.getCurrentUser(principal);
    }

    @PostMapping(PathConstant.CHANGE_PASSWORD)
    public ResponseEntity<StringResponse> changePassword(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserDto Dto) {
        System.out.println(Dto);
        return ResponseEntity.ok(new StringResponse(userService.updatePassword(id, Dto)));
    }

    @PostMapping(PathConstant.FORGET_PASSWORD)
    public String forgetPassword() {
        return "NOT IMPLEMENTED";
    }

}
