package com.internship.deltasmartsoftware.requests;

import lombok.Data;

@Data
public class UserRequest {
    String email;
    String name;
    String password;
    String role;
}
