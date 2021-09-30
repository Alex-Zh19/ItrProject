package com.itranzition.alex.config;

import com.itranzition.alex.security.JwtUserDetailsService;
import com.itranzition.alex.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class TokenProviderTestConfiguration {

    @Bean
    public TokenProvider tokenProvider(JwtUserDetailsService userDetailsService){
        return new TokenProvider(userDetailsService);
    }

    @PostConstruct
    void init() {
        System.out.println("66666666666" + TokenProviderTestConfiguration.class);
    }

}
