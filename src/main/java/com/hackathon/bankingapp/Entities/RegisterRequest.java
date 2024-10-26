package com.hackathon.bankingapp.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private String countryCode;
}
