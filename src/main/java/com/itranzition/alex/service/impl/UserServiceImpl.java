package com.itranzition.alex.service.impl;

import com.itranzition.alex.model.entity.User;
import com.itranzition.alex.repository.UserRepository;
import com.itranzition.alex.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        User registeredUser = userRepository.save(user);
        return registeredUser;
    }

    @Override
    @Transactional
    @Lock(value = LockModeType.PESSIMISTIC_READ)
    public User findUserByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadCredentialsException(HttpStatus.BAD_REQUEST + " email cannot be null or empty");
        }
        return userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException(HttpStatus.BAD_REQUEST
                + String.format(" User with email %s do not exist", email)));
    }

    @Override
    @Lock(value = LockModeType.PESSIMISTIC_READ)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
