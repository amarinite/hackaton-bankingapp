package com.hackathon.bankingapp.Services.Impl;

import com.hackathon.bankingapp.DTO.UserInfoResponse;
import com.hackathon.bankingapp.Entities.User;
import com.hackathon.bankingapp.Exceptions.ResourceNotFoundException;
import com.hackathon.bankingapp.Repositories.UserRepository;
import com.hackathon.bankingapp.Services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;

    public UserInfoResponse getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return UserInfoResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .accountNumber(user.getAccountNumber())
                .hashedPassword(user.getPassword())
                .build();
    }

}
