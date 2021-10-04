package com.itranzition.alex.security.jwt;

import com.itranzition.alex.security.JwtUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

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
    private final String VALIDITY_MILLISEC_PROP = "jwt.expiration";
    private final long DEFAULT_VALIDITY = 200;

    private TokenProvider tokenProvider;
    private JwtUserDetailsService userDetailsService;
    private Environment env;

    @BeforeEach
    void setUp() {
        String keyword = "key";
        env = mock(Environment.class);
        when(env.getProperty(KEYWORD_PROP)).thenReturn(Base64.getEncoder().encodeToString(keyword.getBytes()));
        when(env.getProperty(VALIDITY_MILLISEC_PROP, long.class, DEFAULT_VALIDITY)).thenReturn(DEFAULT_VALIDITY);
        userDetailsService = mock(JwtUserDetailsService.class);
        tokenProvider = new TokenProvider(userDetailsService, env);
    }

    @Test
    void shouldReturnTrueOnCreationNotNullToken() {
        System.out.println(env.getProperty(VALIDITY_MILLISEC_PROP, long.class, DEFAULT_VALIDITY));//does this method return
        List<String> uRList = new ArrayList<>();
        uRList.add(USER_ROLE);
        JwtUser user = new JwtUser(1L, USER_EMAIL, USER_NAME, USER_PASSWORD, USER_ROLE, uRList.stream().
                map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        when(userDetailsService.loadUserByUsername(any(String.class))).thenReturn(user);
        String token = tokenProvider.createToken(USER_EMAIL, USER_ROLE);
        assertNotNull(token);
    }
}
