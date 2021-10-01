package com.itranzition.alex.security.jwt;

import com.itranzition.alex.security.JwtUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class TokenProviderTest {
    private final String USER_EMAIL = "testEmail@mail.ru";
    private final String USER_ROLE = "USER";
    private final String USER_PASSWORD = "testPassword";
    private final String USER_NAME = "john";
    private final String KEYWORD_PROP = "jwt.keyword";

    private TokenProvider tokenProvider;
    private JwtUserDetailsService userDetailsService;
    private Environment env;

    @BeforeEach
    void setUp() {
        String keyword="key";
        env = mock(Environment.class);
        when(env.getProperty(KEYWORD_PROP)).thenReturn(Base64.getEncoder().encodeToString(keyword.getBytes()));
        userDetailsService = mock(JwtUserDetailsService.class);
        tokenProvider = new TokenProvider(userDetailsService,env);
    }

    @Test
    void createToken() {
        List<String> uRList = new ArrayList<>();
        uRList.add(USER_ROLE);
        JwtUser user = new JwtUser(1L, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_ROLE, uRList.stream().
                map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        when(userDetailsService.loadUserByUsername(any(String.class))).thenReturn(user);
        String token = tokenProvider.createToken(USER_EMAIL, USER_ROLE);
        assertNotNull(token);
    }
}
