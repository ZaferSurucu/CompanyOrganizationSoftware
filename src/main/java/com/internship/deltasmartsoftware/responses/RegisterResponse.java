package com.internship.deltasmartsoftware.responses;

import com.internship.deltasmartsoftware.model.*;
import lombok.Data;

import java.util.List;

@Data
public class RegisterResponse {
    List<Company> companies;
    List<Role> roles;
}
