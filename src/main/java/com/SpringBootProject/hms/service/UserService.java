package com.SpringBootProject.hms.service;

import com.SpringBootProject.hms.dto.requestDto.LoginDto;
import com.SpringBootProject.hms.dto.requestDto.UpdateUserDto;
import com.SpringBootProject.hms.dto.requestDto.UserRequestDto;
import com.SpringBootProject.hms.dto.responseDto.LoginResponseJWT;
import com.SpringBootProject.hms.dto.responseDto.Profile;
import com.SpringBootProject.hms.dto.responseDto.UserResponseDto;
import com.SpringBootProject.hms.exceptions.CustomException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public interface UserService {
    /**
     * @return UserResponseDto limited data fields of Users Entity
     */
    List<UserResponseDto> getAllUsers();

    /**
     * @param user all the data needed to register a user
     * @return the data of currently registered users
     * @throws CustomException while Encryption/Decryption
     */
    UserResponseDto registerUser(UserRequestDto user) throws CustomException;

    /**
     * @param id :id of the user
     * @return the user-dto of the user found from the id else throws ResourceNotFound Error
     */
    UserResponseDto getUserById(Long id);

    /**
     * @param name : full name of the user or some keys that matches full name
     * @return users that have full name similar to the provided name
     */
    List<UserResponseDto> getUserByName(String name);

    /**
     * @param id   :id of the user
     * @param user :dto containing values that needs to be updated
     * @return updated user dto
     * @throws CustomException                    when Duplicate userName is found while trying to update userName
     * @throws InvalidAlgorithmParameterException while Encryption/Decryption
     * @throws IllegalBlockSizeException          while Encryption/Decryption
     * @throws NoSuchPaddingException             while Encryption/Decryption
     * @throws NoSuchAlgorithmException           while generating private/public rsa keys
     * @throws BadPaddingException                while Encryption/Decryption
     * @throws InvalidKeyException                while Encryption/Decryption
     * @throws IOException                        while generating private/public rsa keys
     * @throws InvalidKeySpecException            while generating private/public rsa keys
     */
    UserResponseDto updateUser(Long id, UpdateUserDto user) throws CustomException, InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException, InvalidKeySpecException;

    /**
     * @param request  :for adding user data to session from request
     * @param loginDto :contains username and password
     * @return :JWT token
     * @throws Exception while Authenticating User
     */
    LoginResponseJWT userLogin(HttpServletRequest request, LoginDto loginDto) throws Exception;

    LoginResponseJWT refreshToken(HttpServletRequest request);

    String validateVerificationToken(String token);

    Profile getCurrentUser(Principal principal);

    String updatePassword(Long id, UpdateUserDto userDto);
}
