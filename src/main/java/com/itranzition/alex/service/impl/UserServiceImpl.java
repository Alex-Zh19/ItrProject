package com.itranzition.alex.service.impl;

import com.itranzition.alex.service.UserService;
import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        User registeredUser = userRepository.save(user);
        return registeredUser;
    }

    @Override
    @Transactional
    public User findUserByEmail(String email) {
        User result;
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            result = optionalUser.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("User with email %s have already exist", email));
        }
        return result;
    }
}
