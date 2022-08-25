package com.SpringBootProject.hms.service.Impl;

import com.SpringBootProject.hms.dto.requestDto.LoginDto;
import com.SpringBootProject.hms.dto.requestDto.UpdateUserDto;
import com.SpringBootProject.hms.dto.requestDto.UserRequestDto;
import com.SpringBootProject.hms.dto.responseDto.LoginResponseJWT;
import com.SpringBootProject.hms.dto.responseDto.Profile;
import com.SpringBootProject.hms.dto.responseDto.UserResponseDto;
import com.SpringBootProject.hms.dtoToEntity.UserConverter;
import com.SpringBootProject.hms.entity.Role;
import com.SpringBootProject.hms.entity.Users;
import com.SpringBootProject.hms.exceptions.CustomException;
import com.SpringBootProject.hms.exceptions.ResourceNotFoundException;
import com.SpringBootProject.hms.repo.RoleRepo;
import com.SpringBootProject.hms.repo.UserRepo;
import com.SpringBootProject.hms.service.UserService;
import com.SpringBootProject.hms.utils.AesEncryption;
import com.SpringBootProject.hms.utils.RSA;
import com.SpringBootProject.hms.utils.jwt.JwtTokenUtil;
import com.SpringBootProject.hms.utils.jwt.JwtUserDetailsService;
import io.jsonwebtoken.impl.DefaultClaims;
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
import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

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
        return userConverter.entityToDto(userRepo.findAll().stream().map(this::decryptData).collect(Collectors.toList()));
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
        users.setAge(Period.between(userDto.getDob(), LocalDate.now()).getYears());
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
        return new LoginResponseJWT(jwtToken, sessionObj.getAttribute("Roles"));
    }

    @Override
    public LoginResponseJWT refreshToken(HttpServletRequest request) {
        // From the HttpRequest get the claims
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

        Map<String, Object> expectedMap = new HashMap<>(claims);
        return new LoginResponseJWT(jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString()), sessionObj.getAttribute("Roles"));
    }

    @Override
    public String validateVerificationToken(String token) {
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        Users user = userRepo.findByUserName(userName);
        if (user == null) {
            throw new CustomException("User not found");
        }

        Calendar cal = Calendar.getInstance();
        if ((jwtTokenUtil.getExpirationDateFromToken(token).getTime()
                - cal.getTime().getTime()) <= 0) {
            throw new CustomException("Link expired");
        }

        user.setIsActive(true);
        userRepo.save(user);
        return "valid";
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
        Users users = decryptData(userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id)));
        System.out.println(users);
        return userConverter.entityToDto(users);
    }

    @Override
    public List<UserResponseDto> getUserByName(String name) {
        return userConverter.entityToDto(userRepo.findByFullNameContaining(name).stream().map(this::decryptData).collect(Collectors.toList()));
    }

    @Override
    public UserResponseDto updateUser(Long id, UpdateUserDto user) throws CustomException {
        Users dbUser = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));

        if (Objects.nonNull(user.getUserName()) && !"".equalsIgnoreCase(user.getUserName()) && !dbUser.getUsername().equalsIgnoreCase(user.getUserName())) {
            if (Objects.nonNull(userRepo.findByUserName(user.getUserName()))) {
                throw new CustomException("DUPLICATE USER NAME");
            } else
                dbUser.setUserName(user.getUserName());
        }
        if (Objects.nonNull(user.getFullName()) && !"".equalsIgnoreCase(user.getFullName()) && !dbUser.getFullName().equalsIgnoreCase(user.getFullName())) {
            dbUser.setFullName(user.getFullName());
        }
        if (Objects.nonNull(user.getEmail()) && !"".equalsIgnoreCase(user.getEmail()) && !aesEncryption.decrypt("AES/CBC/PKCS5Padding", dbUser.getEmail()).equalsIgnoreCase(user.getEmail())) {
            dbUser.setEmail(aesEncryption.encrypt("AES/CBC/PKCS5Padding", user.getEmail()));
        }
        if (Objects.nonNull(user.getMobile()) && !"".equalsIgnoreCase(user.getMobile()) && !rsa.rsaDecrypt(dbUser.getMobile()).equalsIgnoreCase(user.getUserName())) {
            dbUser.setMobile(rsa.rsaEncrypt(user.getMobile()));
        }
        if (Objects.nonNull(user.getGender()) && !"".equalsIgnoreCase(user.getGender().toString()) && !dbUser.getGender().toString().equalsIgnoreCase(user.getGender().toString())) {
            dbUser.setGender(user.getGender());
        }
        if (Objects.nonNull(user.getDob()) && !"".equalsIgnoreCase(user.getDob().toString()) && !dbUser.getDob().isEqual(user.getDob())) {
            dbUser.setDob(user.getDob());
            dbUser.setAge(Period.between(user.getDob(), LocalDate.now()).getYears());
        }
        if (Objects.nonNull(user.getAddress()) && !ObjectUtils.isEmpty(user.getAddress())) {
            dbUser.setAddress(user.getAddress());
        }

        return userConverter.entityToDto(decryptData(userRepo.save(dbUser)));
    }

    @Override
    public Profile getCurrentUser(Principal principal) {
        Users users = (Users) userDetailsService.loadUserByUsername(principal.getName());
        return new Profile(decryptData(users));
    }

    @Override
    public String updatePassword(Long id, UpdateUserDto Dto) {
        Users dbUser = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));
        if (Objects.nonNull(Dto.getOldPassword()) && !"".equalsIgnoreCase(Dto.getOldPassword()) &&
                Objects.nonNull(Dto.getNewPassword()) && !"".equalsIgnoreCase(Dto.getNewPassword())) {
            if (passwordEncoder.matches(Dto.getOldPassword(), dbUser.getPassword())) {
                dbUser.setPassword(passwordEncoder.encode(Dto.getNewPassword()));
                userRepo.save(dbUser);
                return "Password change success";
            } else {
                throw new CustomException("Old Password didn't match");
            }
        }
        return "failed to change password";
    }

    public Users decryptData(Users users) {
        users.setEmail(aesEncryption.decrypt("AES/CBC/PKCS5Padding", users.getEmail()));
        users.setMobile(rsa.rsaDecrypt(users.getMobile()));
        return users;
    }
}
