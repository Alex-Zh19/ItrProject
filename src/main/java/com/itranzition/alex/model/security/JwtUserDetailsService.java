package com.itranzition.alex.model.security;

import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.model.security.jwt.JwtUser;
import com.itranzition.alex.model.security.jwt.JwtUserFactory;
import com.itranzition.alex.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with email %s not found", email));
        }
        JwtUser jwtUser = JwtUserFactory.create(user);
        return jwtUser;
    }
}
