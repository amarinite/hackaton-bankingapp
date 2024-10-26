package com.hackathon.bankingapp.Services.Impl;

import com.hackathon.bankingapp.Entities.*;
import com.hackathon.bankingapp.Exceptions.CustomAuthenticationException;
import com.hackathon.bankingapp.Exceptions.ResourceNotFoundException;
import com.hackathon.bankingapp.Repositories.UserRepository;
import com.hackathon.bankingapp.Utils.JwtService;
import com.hackathon.bankingapp.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse registerUser(RegisterRequest request) {
        validateRegisterRequest(request);

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return RegisterResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .accountNumber(user.getAccountNumber())
                .hashedPassword(user.getPassword())
                .build();
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        if (request.getAddress() == null || request.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        validatePasswordStrength(request.getPassword());
    }

    private void validatePasswordStrength(String password) {
        if (password.length() < 8 || password.length() > 128) {
            throw new IllegalArgumentException("Password must be between 8 and 128 characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character");
        }
        if (password.contains(" ")) {
            throw new IllegalArgumentException("Password cannot contain whitespace");
        }
    }


    public AuthResponse loginUser(AuthRequest request) {
        Optional<User> userOptional;
        if (request.getIdentifier().contains("@")) {
            userOptional = userRepository.findByEmail(request.getIdentifier());
        } else {
            userOptional = userRepository.findByAccountNumber(request.getIdentifier());
        }

        User user = userOptional.orElseThrow(() ->
                new ResourceNotFoundException("User not found with identifier: " + request.getIdentifier()));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new CustomAuthenticationException("Bad credentials");
        }

        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
