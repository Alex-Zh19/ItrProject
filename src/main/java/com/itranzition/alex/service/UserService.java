package com.itranzition.alex.service;

import com.itranzition.alex.model.entity.User;

public interface UserService {
    User addUser(User user);

    User findUserByEmail(String email);
}
