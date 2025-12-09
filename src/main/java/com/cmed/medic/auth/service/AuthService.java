package com.cmed.medic.auth.service;

import com.cmed.medic.auth.dto.UserDTO;
import com.cmed.medic.auth.dto.UserDTOwithPass;
import com.cmed.medic.auth.repository.UserRepository;
import com.cmed.medic.auth.security.JwtUtil;
import com.cmed.medic.auth.validation.UserValidation;
import com.cmed.medic.utils.ValidationResult;
import com.cmed.medic.utils.CustomExceptions.InvalidInputException;
import com.cmed.medic.utils.CustomExceptions.ResourceConflictException;
import com.cmed.medic.utils.CustomExceptions.ResourceNotFoundException;
import com.cmed.medic.utils.CustomExceptions.WrongCredentialsException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String register(String username, String rawPassword) {
        ValidationResult userValidation = UserValidation.validateUsername(username);
        if (!userValidation.isValid()) {
            throw new InvalidInputException(userValidation.getMessage());
        }

        ValidationResult passValidation = UserValidation.validatePassword(rawPassword);
        if (!passValidation.isValid()) {
            throw new InvalidInputException(passValidation.getMessage());
        }

        if (userRepository.existsByUsername(username)) {
            throw new ResourceConflictException("Username already taken");
        }
        String hashed = passwordEncoder.encode(rawPassword);
        userRepository.saveUser(username, hashed);
        return "User created";
    }

    public String login(String username, String rawPassword) {
        ValidationResult userValidation = UserValidation.validateUsername(username);
        if (!userValidation.isValid()) {
            throw new InvalidInputException(userValidation.getMessage());
        }

        ValidationResult passValidation = UserValidation.validatePassword(rawPassword);
        if (!passValidation.isValid()) {
            throw new InvalidInputException(passValidation.getMessage());
        }

        Optional<UserDTOwithPass> opt = userRepository.findByUsernameWithPass(username);
        UserDTOwithPass user = opt.orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        UserDTO userData = new UserDTO(user.id(), user.username(), user.role());
        if (!passwordEncoder.matches(rawPassword, user.passwordHash()))
            throw new WrongCredentialsException("Invalid credentials");
        return jwtUtil.generateToken(userData);
    }
}
