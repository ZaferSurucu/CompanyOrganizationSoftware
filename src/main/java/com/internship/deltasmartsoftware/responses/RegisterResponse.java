package com.internship.deltasmartsoftware.responses;

import com.internship.deltasmartsoftware.model.*;
import lombok.Data;

import java.util.List;

@Data
public class RegisterResponse {
    Iterable<Company> companies;
    Iterable<Role> roles;
}
