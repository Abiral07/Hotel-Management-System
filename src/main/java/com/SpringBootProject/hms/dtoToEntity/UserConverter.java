package com.SpringBootProject.hms.dtoToEntity;

import com.SpringBootProject.hms.dto.requestDto.UserRequestDto;
import com.SpringBootProject.hms.dto.responseDto.UserResponseDto;
import com.SpringBootProject.hms.entity.Users;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    @Autowired
    private ModelMapper modelMapper;

    public UserResponseDto entityToDto(Users user) {
        return modelMapper.map(user, UserResponseDto.class);
    }

    public List<UserResponseDto> entityToDto(List<Users> user) {
        return user.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public Users DtoToEntity(UserResponseDto dto) {
        return modelMapper.map(dto, Users.class);
    }

    public List<Users> DtoToEntity(List<UserResponseDto> dto) {
        return dto.stream().map(this::DtoToEntity).collect(Collectors.toList());
    }

    //    -----------------------------------------------------------------------------
    public Users RegistrationDtoToUser(UserRequestDto dto) {
        return modelMapper.map(dto, Users.class);
    }
}
