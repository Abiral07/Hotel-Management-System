package com.SpringBootProject.hms.service.Impl;

import com.SpringBootProject.hms.dto.requestDto.LoginDto;
import com.SpringBootProject.hms.dto.requestDto.UserRequestDto;
import com.SpringBootProject.hms.dto.responseDto.LoginResponseJWT;
import com.SpringBootProject.hms.dto.responseDto.UserResponseDto;
import com.SpringBootProject.hms.dtoToEntity.UserConverter;
import com.SpringBootProject.hms.entity.Role;
import com.SpringBootProject.hms.entity.Users;
import com.SpringBootProject.hms.exceptions.CustomException;
import com.SpringBootProject.hms.repo.RoleRepo;
import com.SpringBootProject.hms.repo.UserRepo;
import com.SpringBootProject.hms.service.UserService;
import com.SpringBootProject.hms.utils.AesEncryption;
import com.SpringBootProject.hms.utils.RSA;
import com.SpringBootProject.hms.utils.jwt.JwtTokenUtil;
import com.SpringBootProject.hms.utils.jwt.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private RSA rsa;
    @Autowired
    private AesEncryption aesEncryption;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    HttpSession sessionObj;
    @Override
    public List<UserResponseDto> getAllUsers() {
        return userConverter.entityToDto(userRepo.findAll());
    }

    @Override
    public UserResponseDto registerUser(UserRequestDto userDto) throws CustomException {
        Users users = userConverter.RegistrationDtoToUser(userDto);
        Set<Role> role = new HashSet<>();
        role.add(roleRepo.findByRoleName("USER"));
        users.setRoles(role);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setEmail(aesEncryption.encrypt("AES/CBC/PKCS5Padding",users.getEmail())); //TODO: throw custom exception for dublicate email and mobile
        users.setMobile(rsa.rsaEncrypt(users.getMobile()));
        return userConverter.entityToDto(userRepo.save(users));
    }

    @Override
    public LoginResponseJWT userLogin(HttpServletRequest request, LoginDto loginDto) throws Exception {
        authenticate(loginDto.getUserName(), loginDto.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUserName());
        String jwtToken = jwtTokenUtil.generateToken(userDetails);
//        ----------------SESSION------------------------------------
        sessionObj = request.getSession();      //returns old session if it exists else returns null
        if (sessionObj == null)
            sessionObj = request.getSession(true);      //creating new session if session doesn't exist
        sessionObj.setAttribute("token", jwtToken);
        sessionObj.setAttribute("userName", userDetails.getUsername());
        sessionObj.setAttribute("Roles", userDetails.getAuthorities());
//        ----------------------SESSION-------------------------------------
        return new LoginResponseJWT(jwtToken);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
    @Override
    public UserResponseDto getUserById(Long id) {
        return userConverter.entityToDto(userRepo.findById(id).get());
    }

    @Override
    public List<UserResponseDto> getUserByName(String name) {
        return userConverter.entityToDto(userRepo.findByName(name));
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto user) throws CustomException{
        Users newUser = userRepo.findById(id).get();

        if (Objects.nonNull(user.getUserName()) && !"".equalsIgnoreCase(user.getUserName())) {
            if (Objects.nonNull(userRepo.findByUserName(user.getUserName()))) {
                throw new CustomException("DUPLICATE USER NAME");
            } else
                newUser.setUserName(user.getUserName());
        }
        if (Objects.nonNull(user.getPassword()) && !"".equalsIgnoreCase(user.getPassword())) {
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (Objects.nonNull(user.getFullName()) && !"".equalsIgnoreCase(user.getFullName())) {
            newUser.setFullName(user.getFullName());
        }
        if (Objects.nonNull(user.getEmail()) && !"".equalsIgnoreCase(user.getEmail())) {
            newUser.setEmail(aesEncryption.encrypt("AES/CBC/PKCS5Padding",user.getEmail()));
        }
        if (Objects.nonNull(user.getMobile()) && !"".equalsIgnoreCase(user.getMobile())) {
            newUser.setMobile(rsa.rsaEncrypt(user.getMobile()));
        }
        if (Objects.nonNull(user.getGender()) && !"".equalsIgnoreCase(user.getGender().toString())) {
            newUser.setGender(user.getGender());
        }
        if (Objects.nonNull(user.getDob()) && !"".equalsIgnoreCase(user.getDob().toString())) {
            newUser.setDob(user.getDob());
        }
        if (Objects.nonNull(user.getAge()) && !"".equalsIgnoreCase(user.getAge().toString())) {
            newUser.setAge(user.getAge());
        }
        if (Objects.nonNull(user.getAddress()) && !ObjectUtils.isEmpty(user.getAddress())) {
            newUser.setAddress(user.getAddress());
        }

        return userConverter.entityToDto(userRepo.save(newUser));
    }


}
