package com.itranzition.alex.security.jwt;

import com.itranzition.alex.config.TokenProviderTestConfiguration;
import com.itranzition.alex.security.JwtUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TokenProviderTestConfiguration.class)
@RunWith(SpringRunner.class)
class TokenProviderTest {
    @Value("${jwt.keyword}")
    private String keyword = "qwerty";
    @Value("${jwt.expiration?:2000}")
    private long validityMilliseconds;
    private final String prefix = "Bearer ";
    private final String header = "Authorization";
    private final String userEmail = "testEmail@mail.ru";
    private final String userRole = "USER";

    @Autowired
    private TokenProvider provider;

    @MockBean
    private JwtUserDetailsService userDetailsService;

    @Test
    void createToken() {
    }

    @Test
    void resolveToken() {
    }

    @Test
    void validateToken() {
    }

    @Test
    void getAuthentication() {
    }

    @Test
    void getUserEmail() {
    }
}