package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.Entities.AuthRequest;
import com.hackathon.bankingapp.Entities.AuthResponse;
import com.hackathon.bankingapp.Entities.RegisterRequest;
import com.hackathon.bankingapp.Entities.User;

import java.util.Optional;

public interface UserService {
    public AuthResponse registerUser(RegisterRequest request);

    public AuthResponse loginUser(AuthRequest request);
}
