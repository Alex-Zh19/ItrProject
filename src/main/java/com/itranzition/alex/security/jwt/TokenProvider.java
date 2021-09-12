package com.itranzition.alex.security.jwt;

import com.itranzition.alex.exception.JwtAuthenticationException;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.security.JwtUserDetailsService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
public class TokenProvider {
    @Value("${jwt.keyword}")
    private String keyword;
    @Value("${jwt.expiration}")
    private long validityMilliseconds;
    @Value("${jwt.prefix}")
    private String prefix;
    @Value("${jwt.header}")
    private String header;

    private UserDetailsService userDetailsService;

    public TokenProvider(JwtUserDetailsService jwtUserDetailsService) {
        this.userDetailsService = jwtUserDetailsService;
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
        String requestToken = request.getHeader(header);
        if (requestToken != null && requestToken.startsWith(prefix)) {
            return requestToken.substring(6, requestToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(keyword).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Jwt token is expired or invalid", HttpStatus.UNAUTHORIZED);
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
