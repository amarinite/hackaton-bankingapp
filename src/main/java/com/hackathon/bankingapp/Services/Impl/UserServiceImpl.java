package com.hackathon.bankingapp.Services.Impl;

import com.hackathon.bankingapp.DTO.AuthRequest;
import com.hackathon.bankingapp.DTO.AuthResponse;
import com.hackathon.bankingapp.DTO.RegisterRequest;
import com.hackathon.bankingapp.DTO.RegisterResponse;
import com.hackathon.bankingapp.Entities.*;
import com.hackathon.bankingapp.Exceptions.CustomAuthenticationException;
import com.hackathon.bankingapp.Exceptions.ResourceNotFoundException;
import com.hackathon.bankingapp.Repositories.TokenRepository;
import com.hackathon.bankingapp.Repositories.UserRepository;
import com.hackathon.bankingapp.Utils.JwtService;
import com.hackathon.bankingapp.Services.UserService;
import com.hackathon.bankingapp.Utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public RegisterResponse registerUser(RegisterRequest request) {
        ValidationUtils.validateRegisterRequest(request, userRepository);

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
        revokeAllTokensByUser(user);
        saveUserToken(jwtToken, user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void revokeAllTokensByUser(User user) {
        List<Token> validTokenListByUser = tokenRepository.findAllTokensByUser(user.getId());
        if(!validTokenListByUser.isEmpty()) {
            validTokenListByUser.forEach(t -> {
                t.setLoggedOut(true);
            });
        }
        tokenRepository.saveAll(validTokenListByUser);
    }

    private void saveUserToken(String jwtToken, User user) {
        Token token = new Token();
        token.setToken(jwtToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}
