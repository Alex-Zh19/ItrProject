package com.itranzition.alex.security.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider provider;

    public JwtConfigurer(TokenProvider provider) {
        this.provider = provider;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        TokenFilter tokenFilter = new TokenFilter(provider);
        builder.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
