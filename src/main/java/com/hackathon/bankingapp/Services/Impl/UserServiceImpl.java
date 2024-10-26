package com.hackathon.bankingapp.Services.Impl;

import com.hackathon.bankingapp.Entities.*;
import com.hackathon.bankingapp.Repositories.UserRepository;
import com.hackathon.bankingapp.Utils.JwtService;
import com.hackathon.bankingapp.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

//    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse registerUser(RegisterRequest request) {
        // TODO: validation (check for empty fields, email format)
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .countryCode(request.getCountryCode())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse loginUser(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                // todo: exception handling
        );
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

//        if (identifier.contains("@")) {
//            user = userRepository.findByEmail(identifier).orElse(null);
//        } else {
//            user = userRepository.findByAccountNumber(identifier).orElse(null);
//        }
//
//        if (user != null && passwordEncoder.matches(password, user.getHashedPassword())) {
//            return Optional.of(user);
//        }
//        return Optional.empty();

}
