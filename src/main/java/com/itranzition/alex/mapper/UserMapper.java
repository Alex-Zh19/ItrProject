package com.itranzition.alex.mapper;

import com.itranzition.alex.model.dto.SignUpDto;
import com.itranzition.alex.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper USER_MAPPER= Mappers.getMapper(UserMapper.class);

   @Mappings({@Mapping(target = "email", source = "signUpDto.email"),
            @Mapping(target = "name", source = "signUpDto.name"),
            @Mapping(target = "password", source = "signUpDto.password"),
            @Mapping(target = "surname", source = "signUpDto.surname", defaultValue = "NULL")})
    User signUpDtoToUser (SignUpDto signUpDto);
}
