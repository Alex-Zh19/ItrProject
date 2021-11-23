package com.itranzition.alex.mapper;

import com.itranzition.alex.model.dto.RegisteredUserDto;
import com.itranzition.alex.model.dto.SignUpDto;
import com.itranzition.alex.model.dto.impl.ResponseSignUpDto;
import com.itranzition.alex.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "signUpDto.email")
    @Mapping(target = "name", source = "signUpDto.name")
    @Mapping(target = "password", source = "signUpDto.password")
    @Mapping(target = "surname", source = "signUpDto.surname", defaultValue = "NULL")
    @Mapping(target = "role", ignore = true)
    User signUpDtoToUser(SignUpDto signUpDto);

    @Mapping(target = "email", source = "signUpDto.email")
    @Mapping(target = "name", source = "signUpDto.name")
    ResponseSignUpDto signUpDtoToResponseSignUpDto(SignUpDto signUpDto);

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "name", source = "user.name")
    RegisteredUserDto userToRegisteredUserDto(User user);
}
