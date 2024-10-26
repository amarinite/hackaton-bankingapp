package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.Entities.*;

public interface UserService {
    public RegisterResponse registerUser(RegisterRequest request);

    public AuthResponse loginUser(AuthRequest request);
}
