package com.itranzition.alex.config;

import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.security.jwt.JwtConfigurer;
import com.itranzition.alex.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private static final String USER_ENDPOINT="/api/user/";
    private static final String SIGN_IN_ENDPOINT="/api/auth/signin";
    private static final String SIGN_UP_ENDPOINT="/api/auth/signup";

    @Autowired
    public SecurityConfig (TokenProvider tokenProvider){
        this.tokenProvider=tokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers(SIGN_IN_ENDPOINT).permitAll()
                .antMatchers(SIGN_UP_ENDPOINT).permitAll()
                .antMatchers(USER_ENDPOINT).hasRole("USER")
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .apply(new JwtConfigurer(tokenProvider));
    }
}
