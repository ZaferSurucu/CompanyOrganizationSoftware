package com.internship.deltasmartsoftware.responses;

import lombok.Data;

@Data
public class AuthResponse {

    String message;
    int userId;
    String email;
    String accessToken;
    String refreshToken;
}