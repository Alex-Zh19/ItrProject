package com.itranzition.alex.security.jwt;

import com.itranzition.alex.model.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public static JwtUser create(User user) {
        List<String> userRoleList = new ArrayList<>();
        userRoleList.add(user.getRole());
        return new JwtUser(user.getEmail(),
                user.getPassword(), mapToGrantedAuthority(userRoleList));
    }

    private static List<GrantedAuthority> mapToGrantedAuthority(List<String> userRoles) {
        return userRoles.stream().
                map(SimpleGrantedAuthority::new).
                collect(Collectors.toList());
    }
}
