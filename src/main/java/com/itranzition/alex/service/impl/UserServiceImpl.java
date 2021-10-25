package com.itranzition.alex.service.impl;

import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.repository.UserRepository;
import com.itranzition.alex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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
                    String.format("User with email %s do not exist", email));
        }
        return result;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
