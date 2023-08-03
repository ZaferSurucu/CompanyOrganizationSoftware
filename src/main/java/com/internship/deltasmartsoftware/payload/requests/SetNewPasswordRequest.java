package com.internship.deltasmartsoftware.payload.requests;

import lombok.Data;

@Data
public class SetNewPasswordRequest {
    private String token;
    private String password;
}
