package com.itranzition.alex.model.security.jwt;

import com.itranzition.alex.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {
    public JwtUserFactory() {
    }

    public static JwtUser create(User user){
        List <User.Role> userRoleList=new ArrayList<>();
        userRoleList.add(user.getRole());
        return new JwtUser(user.getId(), user.getEmail(), user.getName(),
                user.getPassword(), user.getRole(),mapToGrantedAuthority(userRoleList));
    }
    private static List<GrantedAuthority> mapToGrantedAuthority(List<User.Role> userRoles){
        return userRoles.stream().map(role -> new SimpleGrantedAuthority(role.name())).
                collect(Collectors.toList());
    }
}