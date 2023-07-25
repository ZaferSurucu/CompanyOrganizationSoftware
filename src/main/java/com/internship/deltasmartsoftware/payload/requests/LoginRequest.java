package com.internship.deltasmartsoftware.payload.requests;

import lombok.Data;

@Data
public class LoginRequest {
    String email;
    String password;
}
