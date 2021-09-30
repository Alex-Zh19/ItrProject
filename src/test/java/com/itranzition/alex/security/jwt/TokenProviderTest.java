package com.itranzition.alex.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ActiveProfiles("test")
class TokenProviderTest {
    @Autowired
    private TokenProvider provider;

    @Value("${jwt.keyword}")
    private String keyword;
    @Value("${jwt.expiration?:2000}")
    private long validityMilliseconds;
    private final String prefix = "Bearer ";
    private final String header = "Authorization";
    private final String userEmail="testEmail@mail.ru";
    private final String userRole = "USER";

    @MockBean
    private UserDetailsService userDetailsService;

    @BeforeClass
    void init (){
        keyword = Base64.getEncoder().encodeToString(keyword.getBytes());
    }

    @BeforeEach
    void mockInit() {
        List<String> userRoleList = new ArrayList<>();
        userRoleList.add(userRole);
        JwtUser jwtUser = new JwtUser((long)1,userEmail,"john","testPassword","doe",
                mapToGrantedAuthorityTest(userRoleList));
        Mockito.when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(jwtUser);
    }

    @Test
    void createToken() {
    }

    @Test
    void resolveToken() {
    }

    @Test
    void validateToken() {
        String token=createTokenTest(userEmail,userRole);
        Assert.assertTrue(provider.validateToken(token));
    }

    @Test
    void getAuthentication() {
    }

    @Test
    void getUserEmail() {
    }

    private List<GrantedAuthority> mapToGrantedAuthorityTest(List<String> userRoles) {
        return userRoles.stream().
                map(SimpleGrantedAuthority::new).
                collect(Collectors.toList());
    }

    private String createTokenTest(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMilliseconds * 1000);
        return Jwts.builder().
                setClaims(claims).
                setIssuedAt(now).
                setExpiration(validity).
                signWith(SignatureAlgorithm.HS256, keyword).
                compact();
    }
}