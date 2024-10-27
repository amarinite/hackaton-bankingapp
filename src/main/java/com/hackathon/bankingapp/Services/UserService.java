package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.AuthRequest;
import com.hackathon.bankingapp.DTO.AuthResponse;
import com.hackathon.bankingapp.DTO.RegisterRequest;
import com.hackathon.bankingapp.DTO.RegisterResponse;

public interface UserService {
    public RegisterResponse registerUser(RegisterRequest request);

    public AuthResponse loginUser(AuthRequest request);
}
