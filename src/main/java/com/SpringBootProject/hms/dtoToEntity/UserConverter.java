package com.SpringBootProject.hms.dtoToEntity;

import com.SpringBootProject.hms.dto.requestDto.UserRequestDto;
import com.SpringBootProject.hms.dto.responseDto.UserResponseDto;
import com.SpringBootProject.hms.entity.Users;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    public UserResponseDto entityToDto(Users user){
        UserResponseDto dto = new UserResponseDto();
        dto.setUserName(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setMobile(user.getMobile());
        dto.setGender(user.getGender());
        dto.setDob(user.getDob());
        dto.setAge(user.getAge());
//        dto.setAddressId(user.getAddress().getAddId());
        return dto;
    }
    public List<UserResponseDto> entityToDto(List<Users> user){
        return user.stream().map(this::entityToDto).collect(Collectors.toList());
    }
    public Users DtoToEntity(UserResponseDto dto){
        Users user = new Users();
        user.setUserName(dto.getUserName());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getMobile());
        user.setGender(dto.getGender());
        user.setDob(dto.getDob());
        user.setAge(dto.getAge());
        user.setIsActive(true);
//        user.setAddress(dto.getAddress());
        return user;
    }

    public List<Users> DtoToEntity(List<UserResponseDto> dto){
        return dto.stream().map(this::DtoToEntity).collect(Collectors.toList());
    }
//    -----------------------------------------------------------------------------
public Users RegistrationDtoToUser(UserRequestDto dto){
    Users user = new Users();
    user.setUserName(dto.getUserName());
    user.setPassword(dto.getPassword());
    user.setFullName(dto.getFullName());
    user.setEmail(dto.getEmail());
    user.setMobile(dto.getMobile());
    user.setGender(dto.getGender());
    user.setDob(dto.getDob());
    user.setAge(dto.getAge());
    user.setIsActive(true);
    user.setAddress(dto.getAddress());
    return user;
}
}
