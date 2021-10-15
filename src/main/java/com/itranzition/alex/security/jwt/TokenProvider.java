package com.itranzition.alex.security.jwt;

import com.itranzition.alex.exception.JwtAuthenticationException;
import com.itranzition.alex.properties.JwtConfigurationProperties;
import com.itranzition.alex.security.JwtUserDetailsService;
import io.jsonwebtoken.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@NoArgsConstructor
public class TokenProvider {
    private static final String PREFIX = "Bearer ";
    private static final String HEADER = "Authorization";
    private static final int PREFIX_ENDING_POSITION = 7;
    private String keyword;
    private long validityMilliseconds;

    private UserDetailsService userDetailsService;
    private JwtConfigurationProperties properties;

    @Autowired
    public TokenProvider(JwtUserDetailsService jwtUserDetailsService, JwtConfigurationProperties properties) {
        this.properties = properties;
        this.userDetailsService = jwtUserDetailsService;
        keyword = properties.getKeyword();
        validityMilliseconds = properties.getExpiration();
    }

    @PostConstruct
    protected void init() {
        keyword = Base64.getEncoder().encodeToString(keyword.getBytes());
    }

    public String createToken(String email, String role) {
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

    public String resolveToken(HttpServletRequest request) {
        String requestToken = request.getHeader(HEADER);
        if (requestToken != null && requestToken.startsWith(PREFIX)) {
            return requestToken.substring(PREFIX_ENDING_POSITION, requestToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(keyword).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Jwt token is expired or invalid " + token);
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserEmail(String token) {
        return Jwts.parser().
                setSigningKey(keyword).
                parseClaimsJws(token).
                getBody().
                getSubject();
    }
}
