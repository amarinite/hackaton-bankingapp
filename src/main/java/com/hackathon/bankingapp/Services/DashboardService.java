package com.hackathon.bankingapp.Services;

import com.hackathon.bankingapp.DTO.UserInfoResponse;

public interface DashboardService {
    public UserInfoResponse getUserInfo(String email);
}
