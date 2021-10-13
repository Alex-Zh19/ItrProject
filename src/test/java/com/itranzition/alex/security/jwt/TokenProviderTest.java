package com.itranzition.alex.security.jwt;

import com.itranzition.alex.properties.JwtConfigurationProperties;
import com.itranzition.alex.security.JwtUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties(JwtConfigurationProperties.class)
class TokenProviderTest {
    private final String USER_EMAIL = "testEmail@mail.ru";
    private final String USER_ROLE = "USER";
    private final long DEFAULT_EXPIRATION = 20;
    private TokenProvider tokenProvider;
    @Mock
    private JwtConfigurationProperties properties;
    @Mock
    private JwtUserDetailsService userDetailsService;


    @BeforeEach
    void setUp() {
        String pureKeyword = "key";
        String encodedKeyword = Base64.getEncoder().encodeToString(pureKeyword.getBytes());
        when(properties.getKeyword()).thenReturn(encodedKeyword);
        when(properties.getExpiration()).thenReturn(DEFAULT_EXPIRATION);
        tokenProvider = new TokenProvider(userDetailsService, properties);
    }

    @Test
    @DisplayName("test return true when method create not null token")
    void shouldReturnTrueOnCreationNotNullToken() {
        List<String> uRList = new ArrayList<>();
        uRList.add(USER_ROLE);
        String token = tokenProvider.createToken(USER_EMAIL, USER_ROLE);
        assertNotNull(token);
    }
}
