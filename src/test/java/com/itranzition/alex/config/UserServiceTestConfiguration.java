package com.itranzition.alex.config;

import com.itranzition.alex.repository.UserRepository;
import com.itranzition.alex.service.UserService;
import com.itranzition.alex.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

@Configuration
public class UserServiceTestConfiguration {

    @Bean
    public UserService userService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        return new UserServiceImpl(userRepository, passwordEncoder);
    }

    @PostConstruct
    void init() {
        System.out.println("66666666666" + UserServiceTestConfiguration.class);
    }
}
