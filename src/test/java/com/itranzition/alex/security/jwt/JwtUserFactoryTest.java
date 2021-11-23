package com.itranzition.alex.security.jwt;

import com.itranzition.alex.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class JwtUserFactoryTest {
    private static final String USER_EMAIL = "testEmail@mail.ru";
    private static final String USER_ROLE = "USER";
    private static final String USER_PASSWORD = "testPassword";
    private static final String USER_NAME = "john";
    private static final String USER_SURNAME = "doe";

    @Test
    void create() {
        User user = new User((long) 1, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_SURNAME, USER_ROLE);
        List<String> roleList = new ArrayList<>();
        roleList.add(USER_ROLE);
        JwtUser expectedUser = new JwtUser(USER_EMAIL, USER_PASSWORD,
                roleList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        JwtUser actualUser = JwtUserFactory.create(user);
        assertEquals(expectedUser, actualUser);
    }
}
