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

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties(JwtConfigurationProperties.class)
class TokenProviderTest {
    private final String TEST_EMAIL = "testEmail@mail.ru";
    private final String TEST_ROLE = "USER";
    private final long DEFAULT_EXPIRATION = 20;
    private final String TOKEN_PREFIX = "Bearer ";
    private TokenProvider tokenProvider;
    @Mock
    private JwtConfigurationProperties properties;
    @Mock
    private JwtUserDetailsService userDetailsService;
    @Mock
    private HttpServletRequest request;


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
    void createTokenNotNull() {
        String token = tokenProvider.createToken(TEST_EMAIL, TEST_ROLE);
        assertNotNull(token);
    }

    @Test
    @DisplayName("test return true when method resolve token from header correctly")
    void resolveToken() {
        String pureToken = tokenProvider.createToken(TEST_EMAIL, TEST_ROLE);
        String token = createTokenForHeader(pureToken);
        when(request.getHeader(anyString())).thenReturn(token);
        String actualResolvedToken = tokenProvider.resolveToken(request);
        assertEquals(pureToken, actualResolvedToken);
    }

    @Test
    @DisplayName("test return true when method return null due to token absence")
    void resolveTokenReturnNull() {
        when(request.getHeader(anyString())).thenReturn(null);
        String actualResolvedToken = tokenProvider.resolveToken(request);
        assertNull(actualResolvedToken);
    }

    @Test
    @DisplayName("test return true when method return null due to token invalidity (Header absence)")
    void resolveTokenNull() {
        String pureToken = tokenProvider.createToken(TEST_EMAIL, TEST_ROLE);
        when(request.getHeader(anyString())).thenReturn(pureToken);
        String actualResolvedToken = tokenProvider.resolveToken(request);
        assertNull(actualResolvedToken);
    }

    @Test
    @DisplayName("test return true when method validate token as expected")
    void validateToken() {
        String pureToken = tokenProvider.createToken(TEST_EMAIL, TEST_ROLE);
        assertTrue(tokenProvider.validateToken(pureToken));
    }

    @Test
    @DisplayName("test return true when method gets user email as expected")
    void getUserEmail() {
        String pureToken = tokenProvider.createToken(TEST_EMAIL, TEST_ROLE);
        String actualEmail = tokenProvider.getUserEmail(pureToken);
        assertEquals(TEST_EMAIL, actualEmail);
    }

    private String createTokenForHeader(String pureToken) {
        String result = new StringBuilder(TOKEN_PREFIX)
                .append(pureToken)
                .toString();
        return result;
    }
}
