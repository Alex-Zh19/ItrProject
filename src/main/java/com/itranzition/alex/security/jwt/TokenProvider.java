package com.itranzition.alex.security.jwt;

import com.itranzition.alex.exception.JwtAuthenticationException;
import com.itranzition.alex.security.JwtUserDetailsService;
import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
@Getter
public class TokenProvider {
    private String keyword;
    private long validityMilliseconds;
    private final String PREFIX = "Bearer ";
    private final String HEADER = "Authorization";
    private final String KEYWORD_PROP = "jwt.keyword";
    private final String VALIDITY_MILLISEC_PROP = "jwt.expiration";
    private final long DEFAULT_VALIDITY = 200;
    private UserDetailsService userDetailsService;

    @Autowired
    public TokenProvider(JwtUserDetailsService jwtUserDetailsService, Environment env) {
        this.keyword = env.getProperty(KEYWORD_PROP);
        // this.validityMilliseconds = env.getRequiredProperty(VALIDITY_MILLISEC_PROP,long.class);//does this method return
        //default value in case of ${other.prop}
        System.out.println(validityMilliseconds);
        try {
            this.validityMilliseconds = Long.parseLong(env.resolveRequiredPlaceholders(VALIDITY_MILLISEC_PROP));
        } catch (IllegalArgumentException e) {
            this.validityMilliseconds = DEFAULT_VALIDITY;
        }
        //question
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
        String requestToken = request.getHeader(HEADER);
        if (requestToken != null && requestToken.startsWith(PREFIX)) {
            return requestToken.substring(6, requestToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(keyword).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Jwt token is expired or invalid " + token, HttpStatus.UNAUTHORIZED);
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
